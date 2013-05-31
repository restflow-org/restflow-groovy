package org.restflow.groovy;

import org.restflow.groovy.GroovyDirector;
import org.restflow.test.WorkflowTestCase;


public class TestGroovyDirectorWorkflows extends WorkflowTestCase {

	public TestGroovyDirectorWorkflows() {
		super("org/restflow/groovy/test/TestGroovyDirectorWorkflows");
		_resourceDirectory = "/src/test/resources/";		
	}
	
	public void test_Hello1() throws Exception {

		GroovyDirector director = new GroovyDirector();
		
		
		String script = "CreateGreeting.step(); RenderGreeting.step(); ";
		director.setSchedule(script);
		
		director.afterPropertiesSet();
		
		_loadAndRunWorkflow("hello", "hello1", director, "HelloWorld");
		assertEquals(_getExpectedResultFile("hello1_trace.txt"), _runner
				.getTraceReport());
		assertEquals(_getExpectedStdout("hello1_stdout.txt"), _runner
				.getStdoutRecording());
	}
	
	public void test_Hello2() throws Exception {
		GroovyDirector director = new GroovyDirector();
		String script = "CreateGreeting.step(); RenderGreeting.step(); ";
		//String script = "CreateGreeting.trigger(); director._publishOutputs( CreateGreeting ); RenderGreeting.trigger() ; ";		
		director.setSchedule(script);
		director.afterPropertiesSet();

		_loadAndRunWorkflow("hello", "hello2", director, "HelloWorld");
		assertEquals(_getExpectedResultFile("hello2_trace.txt"), _runner
				.getTraceReport());
		assertEquals(_getExpectedStdout("hello2_stdout.txt"), _runner
				.getStdoutRecording());
	}

	public void test_Hello3() throws Exception {
		GroovyDirector director = new GroovyDirector();
		
		String script = "CreateGreeting.step(); RenderGreeting.step(); "
				+ "CreateGreeting.step(); RenderGreeting.step(); "
				+ "CreateGreeting.step(); RenderGreeting.step(); ";
		
		director.setSchedule(script);
		director.afterPropertiesSet();

		_loadAndRunWorkflow("hello", "hello3", director, "HelloWorld");
		assertEquals(_getExpectedResultFile("hello3_trace.txt"), _runner
				.getTraceReport());
		assertEquals(_getExpectedStdout("hello3_stdout.txt"), _runner
				.getStdoutRecording());
	}	


	public void test_Hello4() throws Exception {
		GroovyDirector director = new GroovyDirector();
		
		String script = "CreateGreeting.step(); EmphasizeGreeting.step(); RenderGreeting.step(); "
				+ "CreateGreeting.step(); EmphasizeGreeting.step(); RenderGreeting.step(); "
				+ "CreateGreeting.step(); EmphasizeGreeting.step(); RenderGreeting.step(); ";		
		director.setSchedule(script);
		director.afterPropertiesSet();
		
		_loadAndRunWorkflow("hello", "hello4", director, "HelloWorld");
		assertEquals(_getExpectedResultFile("hello4_trace.txt"), _runner.getTraceReport());	
		assertEquals(_getExpectedStdout("hello4_stdout.txt"), _runner.getStdoutRecording());		
	}

	public void test_Hello5() throws Exception {
		
		GroovyDirector director = new GroovyDirector();

		String script = "CreateGreeting.step(); CreateEmphasis.step(); EmphasizeGreeting.step(); RenderGreeting.step(); "
				+ "CreateGreeting.step(); CreateEmphasis.step(); EmphasizeGreeting.step(); RenderGreeting.step(); "
				+ "CreateGreeting.step(); CreateEmphasis.step(); EmphasizeGreeting.step(); RenderGreeting.step(); ";		
		director.setSchedule(script);		
		director.afterPropertiesSet();
		
		_loadAndRunWorkflow("hello", "hello5", director, "HelloWorld");
		assertEquals(_getExpectedResultFile("hello5_trace.txt"), _runner.getTraceReport());		
		assertEquals(_getExpectedStdout("hello5_stdout.txt"), _runner.getStdoutRecording());		
	}

	public void test_Hello6() throws Exception {
		GroovyDirector director = new GroovyDirector();

		String script = "CreateGreeting.step(); CreateEmphasis.step(); EmphasizeGreeting.step(); RenderGreeting.step(); "
				+ "CreateGreeting.step(); CreateEmphasis.step(); EmphasizeGreeting.step(); RenderGreeting.step(); "
				+ "CreateGreeting.step(); CreateEmphasis.step(); EmphasizeGreeting.step(); RenderGreeting.step(); ";		
		director.setSchedule(script);	
		director.afterPropertiesSet();
		
		_loadAndRunWorkflow("hello", "hello6", director, "HelloWorld");
		assertEquals(_getExpectedResultFile("hello6_trace.txt"),_runner.getTraceReport());
		assertEquals(_getExpectedStdout("hello6_stdout.txt"), _runner.getStdoutRecording());		
	}

	public void test_Hello7() throws Exception {
		GroovyDirector director = new GroovyDirector();

		String script = "CreateGreeting.step(); CreateEmphasis.step(); EmphasizeGreeting.step(); RenderGreeting.step(); "
				+ "CreateGreeting.step(); CreateEmphasis.step(); EmphasizeGreeting.step(); RenderGreeting.step(); "
				+ "CreateGreeting.step(); CreateEmphasis.step(); EmphasizeGreeting.step(); RenderGreeting.step(); ";		
		director.setSchedule(script);			
		director.afterPropertiesSet();
		
		_loadAndRunWorkflow("hello", "hello7", director, "HelloWorld");
		assertEquals(_getExpectedResultFile("hello7_trace.txt"), _runner.getTraceReport());		
		assertEquals(_getExpectedStdout("hello7_stdout.txt"), _runner.getStdoutRecording());		
	}

	public void test_NestedWorkflow1() throws Exception {
		GroovyDirector director = new GroovyDirector();
		String pipeline = " GenerateIntegerSequence.step(); IncrementByDefaultIncrement.step(); RenderFirstIncrement.step(); RenderIncrementedIntegers.step(); ";		
		String script = pipeline + pipeline +pipeline +pipeline +pipeline +pipeline;
		director.setSchedule(script);			
		director.afterPropertiesSet();
		
		_loadAndRunWorkflow("nested", "nestedworkflow1", director, "NestedWorkflow");
		assertEquals(_getExpectedResultFile("nestedworkflow1_trace.txt"), _runner.getTraceReport());
		assertEquals(_getExpectedStdout("nestedworkflow1_stdout.txt"), _runner.getStdoutRecording());		
	}

	public void test_CountToTen() throws Exception {
		GroovyDirector director = new GroovyDirector();
		String pipeline = " while (true) {CountToTen.step(); PrintNumber.step(); if (CountToTen.out.number.value == 'EndOfStream') break; };  ";		
		String script = pipeline;
		director.setSchedule(script);			
		director.afterPropertiesSet();
		
		_loadAndRunWorkflow("behaviors", "counttoten", director, "Count");
		assertEquals(_getExpectedResultFile("counttoten_trace.txt"), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout("counttoten_stdout.txt"), _runner.getStdoutRecording());
	}

	public void test_Filter() throws Exception {
		GroovyDirector director = new GroovyDirector();
		String script = "while (true) {CountToTen.step(); FilterNumbers.step(); if (FilterNumbers.out.outNumber.ready ) { PrintNumbers.step()};  if (CountToTen.out.number.value == 'EndOfStream') break; };";		
		director.setSchedule(script);				
		director.afterPropertiesSet();
		
		_loadAndRunWorkflow("behaviors", "filter", director, "Filter");
		assertEquals(_getExpectedResultFile("filter_trace.txt"), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout("filter_stdout.txt"), _runner.getStdoutRecording());
	}


	public void test_Loop() throws Exception {
		_loadAndRunWorkflow("behaviors", "loop", _dataDrivenDirector(), "Loop");
		assertEquals(_getExpectedResultFile("loop_trace.txt"), _runner.getTraceReport());;		
		assertEquals(_getExpectedStdout("loop_stdout.txt"), _runner.getStdoutRecording());
	}
}
