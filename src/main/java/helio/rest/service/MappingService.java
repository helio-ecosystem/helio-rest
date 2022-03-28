package helio.rest.service;

import java.util.Optional;

import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.HelioTranslationTask;

public class MappingService extends HelioTaskService {
	
	public static void addUpdateMapping(String taskId, String rawMapping, String reader) {
		HelioTranslationTask task = getTranslationTask(taskId); // throws exception if not exists
		try {
			// if everything was fine
			task.setMappingContent(rawMapping);
			task.asemble();
			repository.delete(taskId);
			repository.persist(task);
		} catch (Exception e) {
			throw new InvalidRequestException(e.toString());
		}
		
	}



	public static void deleteMapping(String id) {
		HelioTranslationTask task = getTranslationTask(id); // throws exception if not found
		repository.delete(id);
		task.setMappingContent(null);
		try {
			task.asemble();
			repository.persist(task);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

	public static String getMapping(String id) {
		Optional<HelioTranslationTask> opt = repository.retrieve(id);
		if (!opt.isPresent())
			throw new ResourceNotPresentException("Requested Helio task not found");

		return opt.get().getMappingContent();
	}

}
