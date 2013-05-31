/**
    This performs coarse-grained unit tests for the RestFlow CLI  
 * 
 */
package org.restflow.groovy;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.restflow.RestFlow;
import org.restflow.beans.TextScanner;
import org.restflow.test.RestFlowTestCase;
import org.restflow.util.PortableIO;
import org.restflow.util.StdoutRecorder;


public class TestWorkflowToDot extends RestFlowTestCase {
	
	public void testHammingToDot() throws Exception {

		StdoutRecorder _stdoutRecorder = new StdoutRecorder(false);
		_stdoutRecorder.recordExecution(new StdoutRecorder.WrappedCode() {
			@Override
			public void execute() throws Exception {
				RestFlow.main(new String[]{
						"-i","restflowFile=classpath:/org/restflow/test/TestWorkflows/HammingSequence/HammingSequence.yaml",
						"-i","workflowName=HammingSequence",
						"-f","classpath:org/restflow/groovy/tools/dot.yaml",
						"-base", "RESTFLOW_TESTRUNS_DIR" } );		
			}
		});
		
		String actualOutput = _stdoutRecorder.getStdoutRecording();
		String expectedOutput = PortableIO.readTextFileOnFilesystem("src/test/resources/ssrl/workflow/RestFlow/hammingDot.txt");
		
		assertStringsEqualWhenLineEndingsNormalized(expectedOutput , actualOutput);
	}

	public void testWorkflowToDot_FileProtocol() throws Exception {

		StdoutRecorder _stdoutRecorder = new StdoutRecorder(false);
		_stdoutRecorder.recordExecution(new StdoutRecorder.WrappedCode() {
			@Override
			public void execute() throws Exception {
				RestFlow.main(new String[]{
						"-i","restflowFile=classpath:testWorkflowToDot/helloWorld.yaml",
						"-i","workflowName=HelloWorld",
						"-f","classpath:org/restflow/groovy/tools/dot.yaml",
						"-base", "RESTFLOW_TESTRUNS_DIR" } );		
			}
		});
		
		String actualOutput = _stdoutRecorder.getStdoutRecording();
		String expectedOutput = PortableIO.readTextFileOnFilesystem("src/test/resources/ssrl/workflow/RestFlow/helloWorldDot.txt");
		
		assertStringsEqualWhenLineEndingsNormalized(expectedOutput , actualOutput);
		
	}	
	
	public void testTimeHamming() throws Exception {

		StdoutRecorder _stdoutRecorder = new StdoutRecorder(false);
		_stdoutRecorder.recordExecution(new StdoutRecorder.WrappedCode() {
			@Override
			public void execute() throws Exception {
				RestFlow.main(new String[]{
						"-i","restflowFile=classpath:/org/restflow/test/TestWorkflows/HammingSequence/HammingSequence.yaml",
						"-i","workflowName=HammingSequence",
						"-f","classpath:org/restflow/groovy/tools/timer.yaml",
						"-base", "RESTFLOW_TESTRUNS_DIR" } );		
			}
		});
		
		String actualOutput = _stdoutRecorder.getStdoutRecording();

		TextScanner s = new TextScanner();
		s.addDefaultTags();
		s.setAbsorbWhiteSpaceSymbol("~");
		List<String> params = new Vector<String>();
		params.add("{workflowOutput:TEXT_BLOCK}*** Hamming Sequence ***{sequence:TEXT_BLOCK}Execution time ~ {time:INT} ~ ms");
		s.setTemplate(params);
		s.compile();
		Map<String,Object> result = s.search( actualOutput );
		
		String expectedOutput = PortableIO.readTextFileOnFilesystem("src/test/resources/ssrl/workflow/RestFlow/hammingStdout.txt");
		assertStringsEqualWhenLineEndingsNormalized(expectedOutput , (String)result.get("workflowOutput"));

		Object execTime = result.get("time");
		assertNotNull("no time found", execTime);
		assertTrue("time must be position", Integer.parseInt((String)execTime) > 0);
		
	}

/*
	public void testVisualizeToolsToDot() throws Exception {

		StdoutRecorder _stdoutRecorder = new StdoutRecorder(false);
		_stdoutRecorder.recordExecution(new StdoutRecorder.WrappedCode() {
			@Override
			public void execute() throws Exception {
				RestFlow.main(new String[]{
						"-i","restflowFile=classpath:tools/visualize.yaml",
						"-i","workflowName=Visualize.WorkflowGraph",
						"-f","classpath:tools/visualize.yaml",
						"-base", "RESTFLOW_TESTRUNS_DIR" } );		
			}
		});
		
		String actualOutput = _stdoutRecorder.getStdoutRecording();
		String expectedOutput = PortableIO.readTextFile("src/test/resources/ssrl/workflow/RestFlow/hammingDot.txt");
		
		assertStringsEqualWhenLineEndingsNormalized(expectedOutput , actualOutput);
		
	}	
	*/
	
}
