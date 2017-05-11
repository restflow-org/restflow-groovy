package org.restflow.groovy;

import org.restflow.test.WorkflowTestCase;
import org.restflow.util.TestUtilities;

public class TestParallelWorkflow_YamlFileDefinitions extends WorkflowTestCase {

	public TestParallelWorkflow_YamlFileDefinitions() {
		super("org/restflow/groovy/test/TestWorkflowsGroovy");
	}
	
	public void setUp() throws Exception {
		super.setUp();
		org.restflow.RestFlow.enableLog4J();
		_importSchemeToResourceMap.put("actors", "classpath:/org/restflow/groovy/");
		_importSchemeToResourceMap.put("testActors", "classpath:/org/restflow/groovy/");
	}
	
	public void test_SimulateDataCollectionNestedParallel_DataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("SimulateDataCollectionNestedParallel", _dataDrivenDirector());
		TestUtilities.assertStringMatchesTemplate(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertFileMatchesTemplate(".metadata/log.txt");
//		assertFileResourcesMatchExactly(".metadata/products.yaml");
		assertFileResourcesMatchExactly("sample");
	}
	
	public void test_SimulateDataCollectionNestedParallel_MTDataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("SimulateDataCollectionNestedParallel", _MTDataDrivenDirector());
		TestUtilities.assertStringMatchesTemplate(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertFileMatchesTemplate(".metadata/log.txt");
//		assertFileResourcesMatchExactly(".metadata/products.yaml");
		assertFileResourcesMatchExactly("sample");
	}
	
	public void test_SimulateDataCollectionNestedParallel_PublishSubscribeDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("SimulateDataCollectionNestedParallel", _publishSubscribeDirector());
		TestUtilities.assertStringMatchesTemplate(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertFileMatchesTemplate(".metadata/log.txt");
//		assertFileResourcesMatchExactly(".metadata/products.yaml");
		assertFileResourcesMatchExactly("sample");
	}
	
	public void test_SimulateDataCollectionParallelAnalysis_MTDataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("SimulateDataCollectionParallelAnalysis", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
	}
}
