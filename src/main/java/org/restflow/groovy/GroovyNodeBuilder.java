package org.restflow.groovy;

import org.restflow.nodes.ActorNodeBuilder;
import org.restflow.nodes.ActorWorkflowNode;


public class GroovyNodeBuilder extends ActorNodeBuilder {
	
	private GroovyActorBuilder _groovyActorBuilder = new GroovyActorBuilder();

	public GroovyNodeBuilder step(String script) {
		_groovyActorBuilder.step(script);
		return this;
	}
	
	public GroovyNodeBuilder state(String name) {
		_groovyActorBuilder.state(name);
		return this;
	}
	
	public ActorWorkflowNode build() throws Exception {
		_groovyActorBuilder.context(_context);
		_actor = _groovyActorBuilder.build();
		return super.build();
	}

}
