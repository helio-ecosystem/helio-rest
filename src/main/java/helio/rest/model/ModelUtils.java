package helio.rest.model;

import helio.Mappings;
import helio.blueprints.Components;
import helio.blueprints.components.MappingReader;
import helio.blueprints.mappings.Mapping;
import helio.rest.exception.InvalidRequestException;

class ModelUtils {

	protected static Mapping readMapping(String rawMapping, String reader)throws InvalidRequestException {
		Mapping mapping = new Mapping();
		if (reader == null) {
			mapping = Mappings.readMapping(rawMapping);
		} else {
			MappingReader readerObj = Components.getMappingReaders().get(reader);
			if (readerObj == null)
				throw new InvalidRequestException("Requested mapping reader does not exists");
			try {
				mapping = readerObj.readMapping(rawMapping);
			} catch (Exception e) {
				throw new InvalidRequestException(e.toString());
			}
		}
		return mapping;
	}

}
