package org.restflow.groovy;

import org.restflow.test.WorkflowTestCase;

public class TestParallelWorkflow_YamlFileDefinitions extends WorkflowTestCase {

	public TestParallelWorkflow_YamlFileDefinitions() {
		super("org/restflow/groovy");
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
		assertStringMatchesTemplate(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertFileMatchesTemplate("_metadata/log.txt");
		assertFileResourcesMatchExactly("_metadata/products.yaml");
		assertFileResourcesMatchExactly("sample");
		assertFileResourcesMatchExactly("scratch");
	}
	
	public void test_SimulateDataCollectionNestedParallel_MTDataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("SimulateDataCollectionNestedParallel", _MTDataDrivenDirector());
		assertStringMatchesTemplate(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertFileMatchesTemplate("_metadata/log.txt");
		assertFileResourcesMatchExactly("_metadata/products.yaml");
		assertFileResourcesMatchExactly("sample");
		assertFileResourcesMatchExactly("scratch");
	}
	
	public void test_SimulateDataCollectionNestedParallel_PublishSubscribeDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("SimulateDataCollectionNestedParallel", _publishSubscribeDirector());
		assertStringMatchesTemplate(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertFileMatchesTemplate("_metadata/log.txt");
		assertFileResourcesMatchExactly("_metadata/products.yaml");
		assertFileResourcesMatchExactly("sample");
		assertFileResourcesMatchExactly("scratch");
	}
	
	public void test_SimulateDataCollectionParallelAnalysis_MTDataDrivenDirector() throws Exception {
		_useWorkingDirectory();
		_loadAndRunWorkflow("SimulateDataCollectionParallelAnalysis", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
	}
}
