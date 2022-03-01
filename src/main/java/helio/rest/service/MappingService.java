package helio.rest.service;

import java.util.Optional;

import helio.Helio;
import helio.rest.RestUtils;
import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.HelioTask;

public class MappingService extends HelioTaskService {


	public static void addUpdateMapping(String taskId, String rawMapping, String reader) {
		HelioTask task = getHelio(taskId); // throws exception if not exists
		try {
			// if everything was fine
			task.setMappingContent(rawMapping);
			task.setMappingReader(reader);

			Helio helio = task.asemble();
			repository.delete(taskId);
			repository.persist(task);
			RestUtils.currentTasks.put(task.getId(), helio);
		} catch (Exception e) {
			throw new InvalidRequestException(e.toString());
		}
	}



	public static void deleteMapping(String id) {
		HelioTask task = getHelio(id); // throws exception if not found
		repository.delete(id);
		task.setMappingContent(null);
		task.setMappingReader(null);
		RestUtils.currentTasks.remove(id);
		repository.persist(task);
	}

	public static String getMapping(String id) {
		Optional<HelioTask> opt = repository.retrieve(id);
		if (!opt.isPresent())
			throw new ResourceNotPresentException("Requested Helio task not found");

		return opt.get().getMappingContent();
	}

}
