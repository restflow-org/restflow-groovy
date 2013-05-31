package org.restflow.groovy;

import org.restflow.directors.DemandDrivenDirector;
import org.restflow.test.WorkflowTestCase;

public class TestWorkflows extends WorkflowTestCase {

	public TestWorkflows() {
		super("org/restflow/test/TestWorkflows");
	}

	public void setUp() throws Exception {
		super.setUp();
		org.restflow.RestFlow.enableLog4J();
		_importSchemeToResourceMap.put("actors", "classpath:/org/restflow/groovy/");
		_importSchemeToResourceMap.put("testActors", "classpath:/org/restflow/groovy/");
	}	
	
//	public void test_TestNestedInflowBindings() throws Exception {
//		configureForGroovyActor();
//		_loadAndRunWorkflow("NestedInflowBinding", _publishSubscribeDirector());
//		assertEquals(_getExpectedTrace(), _runner.getTraceAsString());;
//		assertEquals(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
//	}
//	

	public void test_Maps_MTDataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("Maps", _publishSubscribeDirector());
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
	}

	public void test_HelloWorld_GroovyActor_DataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("HelloWorld", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
		assertEquals(_getExpectedProducts(), _runner.getProductsAsString());
	}
	
	public void test_HelloWorld_GroovyActor_MTDataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("HelloWorld", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
		assertEquals(_getExpectedProducts(), _runner.getProductsAsString());
	}
	
	public void test_HelloWorld_Groovyctor_PublishSubscribeDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("HelloWorld", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
		assertEquals(_getExpectedProducts(), _runner.getProductsAsString());
	}

	public void test_HelloWorld_GroovyActor_DemandDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("HelloWorld", _demandDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
		assertEquals(_getExpectedProducts(), _runner.getProductsAsString());
	}	

	public void test_AveragerWorkflow_GroovyActor_DataDrivenDirector() throws Exception {

		_loadAndRunWorkflow("AveragerWorkflow", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
	}
	
	public void test_AveragerWorkflow_GroovyActor_MTDataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("AveragerWorkflow", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
	}
	
	public void test_AveragerWorkflow_GroovyActor_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("AveragerWorkflow", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
	}
	
	public void test_BranchingWorkflow_GroovyActor_DataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("BranchingWorkflow", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout("stdout_data.txt"), _runner.getStdoutRecording());		
	}	

	public void test_BranchingWorkflow_GroovyActor_MTDataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("BranchingWorkflow", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
	}	
	
	public void test_BranchingWorkflow_GroovyActor_DemandDrivenDirector() throws Exception {
		DemandDrivenDirector director = _demandDrivenDirector();
		director.setFiringCount(3);
		_loadAndRunWorkflow("BranchingWorkflow", director);
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout("stdout_demand.txt"), _runner.getStdoutRecording());		
	}	

	public void test_BranchingWorkflow_GroovyActor_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("BranchingWorkflow", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertEquals(_getExpectedStdout("stdout_publish.txt"), _runner.getStdoutRecording());		
	}	


	public void test_MergingWorkflow_GroovyActor_DataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("MergingWorkflow", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
	}	
	
	public void test_MergingWorkflow_GroovyActor_MTDataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("MergingWorkflow", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
	}	
	
	public void test_MergingWorkflow_GroovyActor_DemandDrivenDirector() throws Exception {
		DemandDrivenDirector director = _demandDrivenDirector();
		director.setFiringCount(3);
		_loadAndRunWorkflow("MergingWorkflow", director);
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
	}	

	public void test_MergingWorkflow_GroovyActor_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("MergingWorkflow", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
	}	
	
	public void test_CountToThree_GroovyActor_DataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("CountToThree", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}	

	public void test_CountToThree_GroovyActor_MTDataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("CountToThree", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}	
		
	public void test_CountToThree_GroovyActor_DemandDrivenDirector() throws Exception {
		DemandDrivenDirector director = _demandDrivenDirector();
		director.setFiringCount(3);
		_loadAndRunWorkflow("CountToThree", director);
		assertEquals(_getExpectedResultFile("trace_demand.txt"), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}	

	public void test_CountToThree_GroovyActor_PublishSubscribeDirector() throws Exception {	
		_loadAndRunWorkflow("CountToThree", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}

	public void test_IntegerFilter_GroovyActor_DataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("IntegerFilter", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}	

	public void test_IntegerFilter_GroovyActor_MTDataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("IntegerFilter", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}	
	
	public void test_IntegerFilter_GroovyActor_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("IntegerFilter", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}	

	public void test_AdderLoop_GroovyActor_DataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("AdderLoop", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}

	public void test_AdderLoop_GroovyActor_MTDataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("AdderLoop", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}

	public void test_AdderLoop_GroovyActor_PublishSubscribeDrivenDirector() throws Exception {
		_loadAndRunWorkflow("AdderLoop", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}
	
	public void test_IntegerStreamMerge_GroovyActor_DataDrivenDirector() throws Exception {	
		_loadAndRunWorkflow("IntegerStreamMerge", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertEquals(_getExpectedStdout("stdout_data.txt"), _runner.getStdoutRecording());
	}
	
	public void test_IntegerStreamMerge_GroovyActor_MTDataDrivenDirector() throws Exception {	
		_loadAndRunWorkflow("IntegerStreamMerge", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
	}		

	public void test_IntegerStreamMerge_GroovyActor_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("IntegerStreamMerge", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertEquals(_getExpectedStdout("stdout_publish.txt"), _runner.getStdoutRecording());
	}	

	public void test_IntegerStreamMergeDuplicates_GroovyActor_DataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("IntegerStreamMergeDuplicates", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertEquals(_getExpectedStdout("stdout_data.txt"), _runner.getStdoutRecording());
		System.out.println(_runner.getStderrRecording());
	}
	
	public void test_IntegerStreamMergeDuplicates_GroovyActor_MTDataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("IntegerStreamMergeDuplicates", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		System.out.println(_runner.getStderrRecording());
	}	
	
	public void test_IntegerStreamMergeDuplicates_GroovyActor_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("IntegerStreamMergeDuplicates", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertEquals(_getExpectedStdout("stdout_publish.txt"), _runner.getStdoutRecording());
		System.out.println(_runner.getStderrRecording());
	}	

	public void test_HammingSequence_GroovyActor_DataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("HammingSequence", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}

	public void test_HammingSequence_GroovyActor_MTDataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("HammingSequence", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}
		
	public void test_HammingSequence_GroovyActor_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("HammingSequence", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}
	
	public void test_OrderedConcurrentMultipliers_MTDataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("ConcurrentMultipliers", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());	
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}

}