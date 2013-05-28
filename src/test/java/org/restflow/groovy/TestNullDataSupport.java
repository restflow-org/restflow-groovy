package org.restflow.groovy;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.restflow.WorkflowContext;
import org.restflow.WorkflowContextBuilder;
import org.restflow.actors.CloneableBean;
import org.restflow.actors.JavaActor;
import org.restflow.actors.JavaActorBuilder;
import org.restflow.actors.SubworkflowBuilder;
import org.restflow.actors.Workflow;
import org.restflow.actors.WorkflowBuilder;
import org.restflow.data.ConsumableObjectStore;
import org.restflow.data.ControlProtocol;
import org.restflow.data.DataProtocol;
import org.restflow.data.FileProtocol;
import org.restflow.data.MultiResourcePacket;
import org.restflow.data.Packet;
import org.restflow.data.Protocol;
import org.restflow.data.PublishedResource;
import org.restflow.data.SingleResourcePacket;
import org.restflow.data.StderrProtocol;
import org.restflow.data.StdoutProtocol;
import org.restflow.data.Uri;
import org.restflow.data.UriTemplate;
import org.restflow.exceptions.NullInputException;
import org.restflow.exceptions.NullOutputException;
import org.restflow.exceptions.WorkflowRuntimeException;
import org.restflow.groovy.GroovyActor;
import org.restflow.groovy.GroovyActorBuilder;
import org.restflow.groovy.GroovyNodeBuilder;
import org.restflow.metadata.NoopTraceRecorder;
import org.restflow.nodes.ActorNodeBuilder;
import org.restflow.nodes.InPortalBuilder;
import org.restflow.nodes.JavaNodeBuilder;
import org.restflow.nodes.WorkflowNodeBuilder;
import org.restflow.test.RestFlowTestCase;
import org.restflow.util.PortableIO;
import org.restflow.util.StdoutRecorder;
import org.restflow.util.TestUtilities;


@SuppressWarnings("unused")
public class TestNullDataSupport extends RestFlowTestCase {

	private WorkflowContext			_context;
	private WorkflowContext			_noopContext;
	private ConsumableObjectStore 	_store;
	private String 					_testRunsDirectoryPath;
	
	public void setUp() throws Exception {
		super.setUp();
		_store = new ConsumableObjectStore();
		_testRunsDirectoryPath = TestUtilities.getTestRunsDirectoryPath();
		_context = new WorkflowContextBuilder().build();
		_noopContext = new WorkflowContextBuilder()
			.recorder(new NoopTraceRecorder())
			.build();
	}
	
	
	public void testGroovyActor_AllowedNullInputsAndOutputs() throws Exception {
		
		 Map<String,Object> nullable = new HashMap<String,Object>();
		 nullable.put("nullable", true);
		
		GroovyActor actor = new GroovyActorBuilder()
			.name("greatest")
			.context(_context)
			.input("a", nullable)
			.input("b", nullable)
			.step("if 	   (a == null && b == null) c = null;	" +
				  "else if (a != null && b == null) c = a;		" +
				  "else if (b != null && a == null) c = b;		" +
				  "else if (a > b) 				    c = a;		" +
				  "else  							c = b;		" 
				)
			.output("c", nullable)
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();

		actor.set("a", 2);
		actor.set("b", 1);
		actor.step();
		assertEquals(2, actor.get("c"));

		actor.set("a", 3);
		actor.set("b", 4);
		actor.step();
		assertEquals(4, actor.get("c"));

		actor.set("a", null);
		actor.set("b", 5);
		actor.step();
		assertEquals(5, actor.get("c"));

		actor.set("a", 6);
		actor.set("b", null);
		actor.step();
		assertEquals(6, actor.get("c"));
	
		actor.set("a", null);
		actor.set("b", null);
		actor.step();
		assertNull(actor.get("c"));
	}

	
	public void testGroovyActor_DisallowedNullInput() throws Exception {
		
		GroovyActor actor = new GroovyActorBuilder()
			.name("buffer")
			.context(_context)
			.input("a")
			.step("c = a;")
			.output("c")
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();

		actor.set("a", 2);
		actor.step();
		assertEquals(2, actor.get("c"));

		actor.set("a", 0);
		actor.step();
		assertEquals(0, actor.get("c"));
		
		Exception exception = null;
		try {
			actor.set("a", null);
		} catch(NullInputException e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("Null data received on non-nullable input 'a' of actor 'buffer'", exception.getMessage());
	}
	
	
	
	public void testGroovyActor_DisallowedNullOutput() throws Exception {
		
		 Map<String,Object> nullable = new HashMap<String,Object>();
		 nullable.put("nullable", true);
		
		GroovyActor actor = new GroovyActorBuilder()
			.name("buffer")
			.context(_context)
			.input("a", nullable)
			.step("c = a;")
			.output("c")
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();

		actor.set("a", 2);
		actor.step();
		assertEquals(2, actor.get("c"));

		actor.set("a", 0);
		actor.step();
		assertEquals(0, actor.get("c"));
		
		actor.set("a", null);
		
		Exception exception = null;
		try {
			actor.step();
		} catch(NullOutputException e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("Null data produced on non-nullable output 'c' of actor 'buffer'", exception.getMessage());
	}
	
	
	
	
	public void testNullDataViaFileProtocol() throws Exception {
		
		// create a run directory for this test
		File runDirectory = PortableIO.createUniqueTimeStampedDirectory(
				_testRunsDirectoryPath, "testWriteFile");
		
		// create a run context specifying the run directory
		WorkflowContext context = new WorkflowContextBuilder()
			.store(_store)
			.scheme("file", new FileProtocol())
			.runDirectory(runDirectory)
			.build();
		
		Map<String,Object> nullable = new HashMap<String,Object>();
		nullable.put("nullable", true);
		
		Map<String,Object> fileType = new HashMap<String,Object>();
		fileType.put("type", "String");

		/// build the workflow
		final Workflow workflow = new WorkflowBuilder() 

			.context(context)
			
			.node(new ActorNodeBuilder()
				.actor(new GroovyActorBuilder()
					.step(	"greetingOne = 'Hello'; 	" +
							"greetingTwo = null;		"
					)
					.output("greetingOne")
					.output("greetingTwo", nullable)
				)
				.outflow("greetingOne", "file:/greetingOne.txt")
				.outflow("greetingTwo", "file:/greetingTwo.txt")
			)
				
			.node(new ActorNodeBuilder()
				.actor(new GroovyActorBuilder()
					.input("messageOne", fileType)
					.input("messageTwo", nullable)
					.step(	"println(messageOne); 		" +
							"println(messageTwo);		"
					)
				)
				.inflow("file:/greetingOne.txt", "messageOne")
				.inflow("file:/greetingTwo.txt", "messageTwo")
			)
				
			.build();
		
		workflow.configure();
		workflow.initialize();
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {workflow.run();}});

		assertEquals(
				"Hello"		+ EOL +
				"null"		+ EOL, 
			recorder.getStdoutRecording());
		
		// confirm type and value of published data items
		assertTrue(_store.get("/greetingOne.txt") instanceof String);
		assertEquals("Hello", _store.take("/greetingOne.txt"));

		assertTrue(_store.containsKey("/greetingTwo.txt"));
		assertEquals(null, _store.take("/greetingTwo.txt"));
		
		// make sure nothing else was published
		assertEquals(0, _store.size());
		
		// check that the run directory was created
		assertTrue(runDirectory.exists());
		assertTrue(runDirectory.isDirectory());
		
		// get the file listing for the run directory
		File[] runFiles = runDirectory.listFiles();

		// check the names, types, and contents of the published files
		File greetingFileOne = new File(runDirectory + "/greetingOne.txt");
		assertTrue(greetingFileOne.isFile());
		assertEquals("Hello" + PortableIO.EOL, PortableIO.readTextFile(greetingFileOne));

		File greetingFileTwo = new File(runDirectory + "/greetingTwo.txt");
		assertFalse(greetingFileTwo.exists());
	}
	
	public void testNullDataIntoSubworkflow() throws Exception {
		
		// create a run directory for this test
		File runDirectory = PortableIO.createUniqueTimeStampedDirectory(
				_testRunsDirectoryPath, "testWriteFile");
		
		// create a run context specifying the run directory
		WorkflowContext context = new WorkflowContextBuilder()
			.store(_store)
			.scheme("file", new FileProtocol())
			.runDirectory(runDirectory)
			.build();
		
		Map<String,Object> nullable = new HashMap<String,Object>();
		nullable.put("nullable", true);
		
		Map<String,Object> fileType = new HashMap<String,Object>();
		fileType.put("type", "String");

		/// build the workflow
		final Workflow workflow = new WorkflowBuilder() 

			.name("Top")
		
			.context(context)
			
			.node(new ActorNodeBuilder()
				.actor(new GroovyActorBuilder()
					.step(	"greetingOne = 'Hello'; 	" +
							"greetingTwo = null;		"
					)
					.output("greetingOne")
					.output("greetingTwo", nullable)
				)
				.outflow("greetingOne", "file:/greetingOne.txt")
				.outflow("greetingTwo", "file:/greetingTwo.txt")
			)
			
			.node(new WorkflowNodeBuilder()
			
				.name("Sub")
				.prefix("/sub{STEP}")
		
				.inflow("file:/greetingOne.txt", "messageOne", "file:/messageOne.txt")
				.inflow("file:/greetingTwo.txt", "messageTwo", "file:/messageTwo.txt", nullable)
						
				.node(new ActorNodeBuilder()
					.inflow("file:/messageOne.txt", "messageOne")
					.inflow("file:/messageTwo.txt", "messageTwo")
					.actor(new GroovyActorBuilder()
						.input("messageOne", fileType)
						.input("messageTwo", nullable)
						.step(	"println(messageOne); 		" +
								"println(messageTwo);		"
						)
					)
				)				
			)

			.build();
		
		workflow.configure();
		workflow.initialize();
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {workflow.run();}});

		assertEquals(
				"Hello"		+ EOL +
				"null"		+ EOL, 
			recorder.getStdoutRecording());
		
		// confirm type and value of published data items
		assertEquals("Hello", _store.take("/greetingOne.txt"));
		
		File messageOneFile = (File)_store.take("/sub1/messageOne.txt");
		assertEquals("Hello" + EOL, PortableIO.readTextFile(messageOneFile));
		
		assertTrue(_store.containsKey("/greetingTwo.txt"));
		assertEquals(null, _store.take("/greetingTwo.txt"));
		
		assertTrue(_store.containsKey("/sub1/messageTwo.txt"));
		assertEquals(null, _store.take("/sub1/messageTwo.txt"));

		// make sure nothing else was published
		assertEquals(0, _store.size());
		
		// check that the run directory was created
		assertTrue(runDirectory.exists());
		assertTrue(runDirectory.isDirectory());
		
		// get the file listing for the run directory
		File[] runFiles = runDirectory.listFiles();

		// check the names, types, and contents of the published files
		File greetingFileOne = new File(runDirectory + "/greetingOne.txt");
		assertTrue(greetingFileOne.isFile());
		assertEquals("Hello" + PortableIO.EOL, PortableIO.readTextFile(greetingFileOne));

		File greetingFileTwo = new File(runDirectory + "/greetingTwo.txt");
		assertFalse(greetingFileTwo.exists());
	}
}
