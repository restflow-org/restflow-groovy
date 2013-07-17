package org.restflow.groovy;

import java.io.IOException;

import org.restflow.test.RestFlowCommandTestCase;


public class TestGroovyYamlTemplate  extends RestFlowCommandTestCase {

	@Override
	public void setUp() throws IOException {
		testCaseResourcePath = "/src/test/resources/org/restflow/groovy/test/TestGroovyYamlTemplate/";
		super.setUp();
	}

	public void test_SimpleYaml() throws Exception {
		initializeTestEnvironment("");
		runRestFlowWithArguments( new String[] {
				"-base", testWorkingDirectory.getPath(),
				"-run", "run",
				"-f", testResourceDirectory + "/SimpleYaml.yaml"
			}
		);
		assertEquals("", testRun.getStderr());
		assertEquals(
				"[First item in the list., {Info about first file={filename=/a/path/to/a/file.txt, extension=txt}, " +
				"name=First file}, " +
				"{Info about second file={filename=/path/to/second/file.dat, ext=dat}, " + 
				"color=255-0-255, name=Tiny 2, description=The second file, visibility=1}]" + EOL, 
			testRun.getStdout());
	}
	
	public void test_YamlWithOneInput() throws Exception {
		initializeTestEnvironment("");
		runRestFlowWithArguments( new String[] {
				"-base", testWorkingDirectory.getPath(),
				"-run", "run",
				"-f", testResourceDirectory + "/YamlWithOneInput.yaml"
			}
		);
		assertEquals("", testRun.getStderr());
		assertEquals(
				"[First item in the list., {Info about first file={filename=/a/path/to/a/one.txt, extension=txt}, " +
				"name=one file}, " +
				"{Info about second file={filename=/path/to/second/file.dat, ext=dat}, " + 
				"color=255-0-255, name=Tiny 2, description=The second file, visibility=1}]" + EOL, 
			testRun.getStdout());
	}

	public void test_YamlWithTwoInputs() throws Exception {
		initializeTestEnvironment("");
		runRestFlowWithArguments( new String[] {
				"-base", testWorkingDirectory.getPath(),
				"-run", "run",
				"-f", testResourceDirectory + "/YamlWithTwoInputs.yaml"
			}
		);
		assertEquals("", testRun.getStderr());
		assertEquals(
				"[First item in the list., {Info about first file={filename=/a/path/to/a/first.txt, extension=txt}, " +
				"name=first file}, " +
				"{Info about second file={filename=/path/to/Second/file.dat, ext=dat}, " + 
				"color=255-0-255, name=Tiny 2, description=The Second file, visibility=1}]" + EOL, 
			testRun.getStdout());
	}

}
