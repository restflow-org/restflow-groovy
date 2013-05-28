package org.restflow.groovy;

import java.sql.Connection;

import org.restflow.WorkflowContext;
import org.restflow.WorkflowContextBuilder;
import org.restflow.actors.Workflow;
import org.restflow.actors.WorkflowBuilder;
import org.restflow.data.ConsumableObjectStore;
import org.restflow.data.InflowProperty;
import org.restflow.groovy.GroovyNodeBuilder;
import org.restflow.metadata.WritableTrace;
import org.restflow.test.RestFlowTestCase;


public class TestTraceDatabase_LoadWorkflow_Groovy extends RestFlowTestCase {

	private WorkflowContext 		_context;
	private ConsumableObjectStore	_store;
	
	public void setUp() throws Exception {
		super.setUp();
		_store = new ConsumableObjectStore();
		_context = new WorkflowContextBuilder()
			.store(_store)
			.build();
	}
	
	public void testLoadWorkflow_TopLevelNodes_GroovyActors() throws Exception {
		
		final Workflow workflow = new WorkflowBuilder()

		.name("OneShotInflow")
		.context(_context)
		
		.node(new GroovyNodeBuilder()
			.name("CreateSingletonData")
			.constant("constant", 5)
			.step("value = constant;")
			.outflow("value", "/multiplier"))

		.node(new GroovyNodeBuilder()
			.name("CreateSequenceData")
			.sequence("c", new Integer[] {3, 8, 2})
			.step("value = c;")
			.outflow("v", "/multiplicand"))
		
		.node(new GroovyNodeBuilder()
			.name("MultiplySequenceBySingleton")
			.inflow("/multiplier", "a", InflowProperty.ReceiveOnce)
			.inflow("/multiplicand", "b")
			.step("c = a * b;")
			.outflow("c", "/product"))

		.node(new GroovyNodeBuilder()
			.name("RenderProducts")
			.inflow("/product", "v")
			.step("println(v);"))
			
		.build();

		// create the database and get a manager for it
		Connection connection = WritableTrace.createPrivateVolatileDatabase();
		WritableTrace.createTraceDBTables(_context, connection);
		WritableTrace manager = new WritableTrace(connection);
		
		// load the workflow into the database
		manager.storeWorkflowGraph(workflow, null);
		
		assertEquals(
				"ActorId ActorName                                       " 		+ EOL +
				"------- ----------------------------------------------- " 		+ EOL +
				"1       OneShotInflow                                   " 		+ EOL +
				"2       CreateSingletonData_actor                       " 		+ EOL +
				"3       CreateSequenceData_actor                        " 		+ EOL +
				"4       MultiplySequenceBySingleton_actor               " 		+ EOL +
				"5       RenderProducts_actor                            " 		+ EOL, 
			manager.dumpActorTable());

		assertEquals(
				"NodeId ParentNodeID ActorID StepCount NodeName                                           LocalNodeName                   " 	+ EOL +
				"------ ------------ ------- --------- -------------------------------------------------- ------------------------------- " 	+ EOL +
				"1                   1       0         OneShotInflow                                      OneShotInflow                   " 	+ EOL +
				"2      1            2       0         OneShotInflow.CreateSingletonData                  CreateSingletonData             " 	+ EOL +
				"3      1            3       0         OneShotInflow.CreateSequenceData                   CreateSequenceData              " 	+ EOL +
				"4      1            4       0         OneShotInflow.MultiplySequenceBySingleton          MultiplySequenceBySingleton     " 	+ EOL +
				"5      1            5       0         OneShotInflow.RenderProducts                       RenderProducts                  " 	+ EOL, 
			manager.dumpNodeTable());
		
		assertEquals(
				"PortId NodeID NodeVariableID PortDirection PacketCount PortName        UriTemplate                                       " 	+ EOL +
				"------ ------ -------------- ------------- ----------- --------------- ------------------------------------------------- " 	+ EOL +
				"1      2      1              o             0           value           /multiplier                                       " 	+ EOL +
				"2      3      2              o             0           v               /multiplicand                                     " 	+ EOL +
				"3      4      3              i             0           b               /multiplicand                                     " 	+ EOL +
				"4      4      4              i             0           a               /multiplier                                       " 	+ EOL +
				"5      4      5              o             0           c               /product                                          " 	+ EOL +
				"6      5      6              i             0           v               /product                                          " 	+ EOL, 
			manager.dumpPortTable());

		assertEquals(
				"VariableID ActorID VariableClass DataTypeID VariableName " 	+ EOL +
				"---------- ------- ------------- ---------- ------------ " 	+ EOL +
				"1          2       o                        value        " 	+ EOL +
				"2          3       o                        v            " 	+ EOL +
				"3          4       i                        b            " 	+ EOL +
				"4          4       i                        a            " 	+ EOL +
				"5          4       o                        c            " 	+ EOL +
				"6          5       i                        v            " 	+ EOL, 
			manager.dumpActorVariableTable());

		assertEquals(
				"NodeVariableID NodeID ActorVariableID " 	+ EOL +
				"-------------- ------ --------------- " 	+ EOL +
				"1              2      1               " 	+ EOL +
				"2              3      2               " 	+ EOL +
				"3              4      3               " 	+ EOL +
				"4              4      4               " 	+ EOL +
				"5              4      5               " 	+ EOL +
				"6              5      6               " 	+ EOL, 
			manager.dumpNodeVariableTable());
		
		assertEquals(
				"InPortID OutPortID " 	+ EOL +
				"-------- --------- " 	+ EOL +
				"3        2         " 	+ EOL +
				"4        1         " 	+ EOL +
				"6        5         " 	+ EOL,
			manager.dumpChannelTable());
	}

	
}
