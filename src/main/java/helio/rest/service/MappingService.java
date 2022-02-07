package helio.rest.service;

import helio.Helio;
import helio.blueprints.Components;
import helio.blueprints.components.MappingReader;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.mappings.Mapping;
import helio.rest.model.MappingType;
import helio.rest.model.ServiceMapping;

public class MappingService {

	
	
	public static void insertMapping(String id, String name, String mappingContent, String mappingReader) throws IncompatibleMappingException, IncorrectMappingException {
		// instantiate mapping
		MappingReader reader = Components.getMappingReaders().get(mappingReader);
		Mapping mapping = reader.readMapping(mappingContent);
		Helio.addTranslationsTasks(mapping);
		// create persistent object
		ServiceMapping toStore = ServiceMapping.create(id, name, mappingContent, MappingType.RML);
		// TODO: soreData(toStore)
	}
}
