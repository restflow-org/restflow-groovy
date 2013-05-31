package org.restflow.groovy;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.restflow.beans.TextScanner;
import org.restflow.directors.DemandDrivenDirector;
import org.restflow.test.WorkflowTestCase;
import org.yaml.snakeyaml.Yaml;

public class TestWorkflowsGroovy extends WorkflowTestCase {

	public TestWorkflowsGroovy() {
		super("org/restflow/groovy/test/TestWorkflowsGroovy");
	}

	public void setUp() throws Exception {
		super.setUp();
		org.restflow.RestFlow.enableLog4J();
		_importSchemeToResourceMap.put("actors", "classpath:/org/restflow/groovy/");
		_importSchemeToResourceMap.put("testActors", "classpath:/org/restflow/groovy/");
	}
	
	public void test_Groovy_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("GroovyWorkflow", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}
	
	public void test_DirectFilesGroovy() throws Exception {		
		_useWorkingDirectory();
		_loadAndRunWorkflow("DirectFilesGroovy", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertEquals(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
		assertFileResourcesMatchExactly("messages1");
		assertFileResourcesMatchExactly("messages2");
		assertFileResourcesMatchExactly("messages3");
		assertFileResourcesMatchExactly("products");
	}
	
	public void test_DirectDirectoriesGroovy() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("DirectDirectoriesGroovy", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertEquals(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
		assertFileResourcesMatchExactly("data_set_1");
		assertFileResourcesMatchExactly("data_set_2");
		assertFileResourcesMatchExactly("data_set_3");
		assertFileResourcesMatchExactly("data_set_4");
		assertFileResourcesMatchExactly("scratch");
	}
	
	public void test_ScratchFiles_MTDataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("ScratchFiles", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
		assertFileResourcesMatchExactly("messages");
		assertFileResourcesMatchExactly("scratch");
	}

	public void test_ScratchFiles_PublishSubscribeDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("ScratchFiles", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
		assertFileResourcesMatchExactly("messages");
		assertFileResourcesMatchExactly("scratch");
	}

	public void test_ScratchFiles_GroovyActor_DataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("ScratchFiles", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
		assertFileResourcesMatchExactly("messages");
		assertFileResourcesMatchExactly("scratch");
	}

	public void test_BasicFiles_GroovyActor_DataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("BasicFiles", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertFileResourcesMatchExactly("messages");
	}
	
	public void test_WorkspaceFiles() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("WorkspaceFiles", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertStringsEqualWhenLineEndingsNormalized(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
	}
	
	@SuppressWarnings("unchecked")
	public void test_liveReport() throws Exception {
		_useWorkingDirectory();
		//setOutputTemplatePath(null);
		
		loadAndRunReport("liveReport", "status");
		
		Yaml yaml = new Yaml();
		Map<String, Object> report = (Map<String,Object>)yaml.load(_stdoutRecorder.getStdoutRecording());
		//System.out.println(report);
		
		Map<String,Object> meta = (Map<String,Object>)report.get("meta");
		assertEquals("Should have 5 meta entries", 5, meta.size());	
		
		assertTrue("Should have host", meta.containsKey("host"));
		assertTrue("Should have pid", meta.containsKey("pid"));
		assertTrue("Should have running", meta.containsKey("running"));
		
		Map<String,Object> inputs = (Map<String,Object>)report.get("inputs");
		
		assertNotNull("should include inputs in model",inputs);
		assertEquals("Should have 5 inputs entries", inputs.size(),5);	
		
		assertEquals("Input file is wrong", "1.0", inputs.get("InputResolution"));
	}
	
	
	public void test_SimulateDataCollection_DataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_teeLogToStandardOutput = true;
		_loadAndRunWorkflow("SimulateDataCollection", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertStringMatchesTemplate(_getExpectedStdout("stdout_datadriven.txt"), _runner.getStdoutRecording());
		assertFileResourcesMatchExactly("sample");
		assertFileResourcesMatchExactly("data");
		assertFileMatchesTemplate("_metadata/log.txt");
	}

	public void test_SimulateDataCollection_MTDataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("SimulateDataCollection", _MTDataDrivenDirector());
		assertFileResourcesMatchExactly("sample");
		assertFileResourcesMatchExactly("data");
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertFileMatchesTemplate("_metadata/log.txt");
	}

	@SuppressWarnings("unchecked")
	public void test_SimulateDataCollection_PublishSubscribeDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("SimulateDataCollection", _publishSubscribeDirector());
		assertFileResourcesMatchExactly("sample");
		assertFileResourcesMatchExactly("data");
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertFileMatchesTemplate("_metadata/log.txt");
		
		TextScanner s = new TextScanner();
		s.addDefaultTags();

		s.setAbsorbWhiteSpaceSymbol("~");
		
		s.setTokenMatcherPrefix("<%");
		s.setTokenMatcherSuffix("%>");
		
		
		List<String> params = new Vector<String>();
		params.add("<%trace[]:TRACE_LINE%>");
		params.add("<%log[]:LOG_LINE%>");
		s.getTags().put("DATE","<%month:STRING%> <%day:INT%>");
		s.getTags().put("TIME","<%hours:INT%>:<%minutes:INT%>:<%seconds:INT%>");
		s.getTags().put("LOG_STAMP","<%:DATE%> <%time:TIME%>");
		s.getTags().put("TRACE_LINE","Sample DRT<%num:INT%>    Image <%imageNum:INT%>    Average intensity: <%avgIntensity:FLOAT%>");
		s.getTags().put("LOG_LINE","<%stamp:LOG_STAMP%> <%text:TEXT_BLOCK%>\n");
		
		s.setTemplate(params);
		s.compile();
		
		Map<String,Object> result = s.search(_runner.getStdoutRecording());
		List<Object> logs = (List<Object>)result.get("log");
		assertEquals ("6 log lines",6,logs.size());

		List<Object> trace = (List<Object>)result.get("trace");
		assertEquals ("26 trace lines",26,trace.size());
		
		Map<Object,Object> firstLine = (Map<Object,Object>)trace.get(0);
		assertEquals(firstLine.get("imageNum"),"001");
	}

	public void test_SimulateDataCollectionNested_PublishSubscribeDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("SimulateDataCollectionNested", _publishSubscribeDirector());
		assertStringMatchesTemplate(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertFileMatchesTemplate("_metadata/log.txt");
		assertFileResourcesMatchExactly("sample");
	}

	public void test_DataCollectionNestedFeedback_PublishSubscribeDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("DataCollectionNestedFeedback", _publishSubscribeDirector());
		assertStringMatchesTemplate(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertFileMatchesTemplate("_metadata/log.txt");
		assertFileResourcesMatchExactly("_metadata/products.yaml");
		assertFileResourcesMatchExactly("sample");
	}

	public void test_DataCollectionNestedFeedback_MTDataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("DataCollectionNestedFeedback", _MTDataDrivenDirector());
		assertStringMatchesTemplate(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertFileMatchesTemplate("_metadata/log.txt");
		assertFileResourcesMatchExactly("sample");
	}
		
	public void test_DataCollectionNestedFeedback_DataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("DataCollectionNestedFeedback", _dataDrivenDirector());
		assertStringMatchesTemplate(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertFileMatchesTemplate("_metadata/log.txt");
		assertFileResourcesMatchExactly("sample");
	}

	public void test_SampleScreening_DataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_teeLogToStandardOutput = true;
		//_finalReporter = null;
		_loadAndRunWorkflow("SampleScreening", _publishSubscribeDirector());
		//assertEquals(_getExpectedTrace(), _runner.getTraceAsString());
		assertStringMatchesTemplate(_getExpectedStdout("stdout_datadriven.txt"), _runner.getStdoutRecording());
		//assertFileMatchesTemplate("_metadata/log.txt");
	}
	
	public void test_Lists_JavaActor_DataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("Lists", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout("stdout_data.txt"), _runner.getStdoutRecording());		
	}

	public void test_Lists_JavaActor_MTDataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("Lists", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
	}

	public void test_Lists_JavaActor_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("Lists", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout("stdout_publish.txt"), _runner.getStdoutRecording());		
	}
	
	public void test_CaughtActorExceptions_PublishSubscribeDirector() throws Exception {	
		_useWorkingDirectory();
		_loadAndRunWorkflow("CaughtActorExceptions", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
		assertEquals(_getExpectedStderr(), _runner.getStderrRecording());
		assertEquals(_getExpectedProducts(), _runner.getProductsAsString());
	}
	
	public void test_CaughtActorExceptions_DataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("CaughtActorExceptions", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertEquals(_getExpectedStdout("stdout_data.txt"), _runner.getStdoutRecording());
		assertEquals(_getExpectedStderr(), _runner.getStderrRecording());
		assertEquals(_getExpectedProducts("products_data.yaml"), _runner.getProductsAsString());
	}
	
	public void test_UncaughtActorException_DataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("UncaughtActorException", _dataDrivenDirector());
		assertEquals(_getExpectedResultFile("trace_data.txt"), _runner.getTraceReport());
		assertEquals(_getExpectedStdout("stdout_data.txt"), _runner.getStdoutRecording());
		assertTrue(_runner.getStderrRecording().startsWith(
				"Actor UncaughtActorException.ThrowExceptionOnThirdStep.(inner bean) threw exception: " + 
				"java.lang.Exception: More than three steps!"));
		assertEquals(_getExpectedProducts("products_data.yaml"), _runner.getProductsAsString());
	}
	
	public void test_UncaughtActorException_PublishSubscribeDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("UncaughtActorException", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
		assertTrue(_runner.getStderrRecording().startsWith(
				"Actor UncaughtActorException.ThrowExceptionOnThirdStep.(inner bean) threw exception: " + 
				"java.lang.Exception: More than three steps!"));
		assertEquals(_getExpectedProducts("products_publish.yaml"), _runner.getProductsAsString());
	}

	public void test_UncaughtActorException_DemandDrivenDirector() throws Exception {
		_useWorkingDirectory();
		DemandDrivenDirector director = _demandDrivenDirector();
		director.setFiringCount(6);
		_loadAndRunWorkflow("UncaughtActorException", director);
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
		assertTrue(_runner.getStderrRecording().startsWith(
				"Actor UncaughtActorException.ThrowExceptionOnThirdStep.(inner bean) threw exception: " + 
				"java.lang.Exception: More than three steps!"));
		assertEquals(_getExpectedProducts("products_demand.yaml"), _runner.getProductsAsString());
	}

	public void test_UncaughtActorException_MTDataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("UncaughtActorException", _MTDataDrivenDirector());
		assertTrue(_runner.getStderrRecording().startsWith(
				"Actor UncaughtActorException.ThrowExceptionOnThirdStep.(inner bean) threw exception: " + 
				"java.lang.Exception: More than three steps!"));
	}
	
	public void test_UncaughtActorExceptionNested_DataDrivenDirector() throws Exception {
		_useWorkingDirectory();		
		try {
			_loadAndRunWorkflow("UncaughtActorExceptionNested", _dataDrivenDirector());
		} catch (Exception e) {
			System.err.println(e);
		}
		
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());

		assertTrue(_runner.getStderrRecording().startsWith(
				"Actor UncaughtActorExceptionNested.RepeatIntegers.RepeaterWorkflow threw exception: " +
				"org.restflow.exceptions.ActorException: " + 
				"Actor UncaughtActorExceptionNested.RepeatIntegers.ThrowExceptionOnThirdStep.(inner bean) threw exception: " +
				"java.lang.Exception: Value of 30 not allowed!"));
		
		assertEquals(_getExpectedProducts(), _runner.getProductsAsString());
	}
	
	public void test_EndFlowOnActorException_DataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("EndFlowOnActorException", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());	
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
		assertEquals(_getExpectedStderr("stderr_data.txt"), _runner.getStderrRecording());
	}
	
	public void test_EndFlowOnActorException_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("EndFlowOnActorException", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
		assertEquals(_getExpectedStderr("stderr_publish.txt"), _runner.getStderrRecording());
	}
	
	public void test_EndFlowOnActorException_MTDataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("EndFlowOnActorException", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
		assertEquals(_getExpectedStderr("stderr_mtdata.txt"), _runner.getStderrRecording());
	}

	public void test_EndFlowOnActorException_DemandDrivenDirector() throws Exception {
		DemandDrivenDirector director = _demandDrivenDirector();
		director.setFiringCount(3);
		_loadAndRunWorkflow("EndFlowOnActorException", director);
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
		assertEquals(_getExpectedResultFile("trace_demand.txt"), _runner.getTraceReport());	
		assertEquals(_getExpectedStderr("stderr_demand.txt"), _runner.getStderrRecording());
	}

	public void test_MissingFiles_DataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("MissingFiles", _dataDrivenDirector());
	}
	
	public void test_FileHandles_DataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("FileHandles", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;	
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
		assertFileResourcesMatchExactly("messages");
		assertFileResourcesMatchExactly("scratch");
	}
	
	public void test_ConditionalRouting_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("ConditionalRouting", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
	}

	public void test_ConditionalRouting_DataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("ConditionalRouting", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
	}

	public void test_NestedWorkflowEnableInputs_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("NestedWorkflowEnableInputs", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}
	
	public void test_Lists_GroovyActor_DataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("Lists", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout("stdout_data.txt"), _runner.getStdoutRecording());		
	}

	public void test_Lists_GroovyActor_MTDataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("Lists", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;
	}

	public void test_Lists_GroovyActor_PublishSubscribeDirector() throws Exception {
		_loadAndRunWorkflow("Lists", _publishSubscribeDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout("stdout_publish.txt"), _runner.getStdoutRecording());		
	}
}
	