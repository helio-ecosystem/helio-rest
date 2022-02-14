package helio.rest.service;

import java.util.Optional;

import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.HelioTask;

public class MappingService extends HelioTaskService {


	public static void addUpdateMapping(String taskId, String mapping, String reader) throws IncompatibleMappingException, IncorrectMappingException  {
		HelioTask task = getHelio(taskId); // throws exception if not exists
		//TODO: PARSE MAPPING
		//TODO: Mapping mapping = null;
		//Helio helio = new Helio();
		//helio.createFrom(mapping);
		//if everything was fine
		task.setMappingContent(mapping);
		task.setMappingReader(reader);
		//task.setHelio(helio);
		repository.delete(taskId);
		repository.persist(task);
	}

	public static void deleteMapping(String id) {
		boolean exists  = repository.exists(id);
		if(!exists)
			throw new IllegalArgumentException("Requested Helio task not found");
		HelioTask task = getHelio(id);
		repository.delete(id);
		task.setMappingContent(null);
		task.setHelio(null);
		repository.persist(task);
	}

	public static String getMapping(String id) {
		Optional<HelioTask> opt = repository.retrieve(id);
		if(!opt.isPresent())
			throw new ResourceNotPresentException("Requested Helio task not found");
		
		return opt.get().getMappingContent();
	}

	
}
