package org.restflow.groovy;

import org.restflow.RestFlow;
import org.restflow.test.RestFlowTestCase;
import org.restflow.util.StdoutRecorder;


public class TestGroovyTools extends RestFlowTestCase {
	
	public static String RestFlowInvocationCommand = "java -classpath target/classes" +
											  System.getProperty("path.separator") + "target/test-classes" +
											  System.getProperty("path.separator") + 
											  "target/dependency/* org.restflow.RestFlow";

	public void testTimeHamming() throws Exception {

		StdoutRecorder _stdoutRecorder = new StdoutRecorder(false);
		
		_stdoutRecorder.recordExecution(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {
				RestFlow.main(new String[]{
						"-i", "restflowFile=classpath:workflows/HammingSequence/HammingSequence.yaml",
						"-i", "workflowName=HammingSequence",
						"-f", "classpath:org/restflow/groovy/tools/timer.yaml",
						"-base", "RESTFLOW_TESTRUNS_DIR" } );		
			}
		});
		
		assertMatchesRegexp(
				"1" 							+ EOL +
				"2" 							+ EOL +
				"3" 							+ EOL +
				"4" 							+ EOL +
				"5" 							+ EOL +
				"6" 							+ EOL +
				"8" 							+ EOL +
				"9" 							+ EOL +
				"10" 							+ EOL +
				"12" 							+ EOL +
				"15" 							+ EOL +
				"16" 							+ EOL +
				"18" 							+ EOL +
				"20" 							+ EOL +
				"24" 							+ EOL +
				"25" 							+ EOL +
				"27" 							+ EOL +
				"30" 							+ EOL +
				"... Hamming Sequence ..." 		+ EOL +
				"1" 							+ EOL +
				"2" 							+ EOL +
				"3" 							+ EOL +
				"4" 							+ EOL +
				"5" 							+ EOL +
				"6" 							+ EOL +
				"8" 							+ EOL +
				"9" 							+ EOL +
				"10" 							+ EOL +
				"12" 							+ EOL +
				"15" 							+ EOL +
				"16" 							+ EOL +
				"18" 							+ EOL +
				"20" 							+ EOL +
				"24" 							+ EOL +
				"25" 							+ EOL +
				"27" 							+ EOL +
				"30" 							+ EOL +
				"Execution time .* ms"			+ EOL,
				_stdoutRecorder.getStdoutRecording());
	}
}
