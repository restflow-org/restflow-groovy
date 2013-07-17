package org.restflow.groovy;

import org.yaml.snakeyaml.Yaml;

public class GroovyYamlTemplate extends GroovyTemplateActor {

	@Override
	public synchronized void step() throws Exception {

		_abstractActorStep();
		
		String yamlString = _expandTemplate();
		Yaml yamlParser = new Yaml();		
		Object dataStructure = yamlParser.load(yamlString);
		
		for (String key : _outputSignature.keySet()) {
			_outputValues.put(key, dataStructure);
		}

		_state = ActorFSM.STEPPED;
	}
}
