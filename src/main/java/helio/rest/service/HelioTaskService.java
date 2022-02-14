package helio.rest.service;

import java.util.List;
import java.util.Optional;

import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.HelioTask;
import helio.rest.repository.Repository;

public class HelioTaskService {

	protected static final Repository<HelioTask> repository = new Repository<>(HelioTask.class);

	public static void createHelio(HelioTask task) {
		boolean exists  = repository.exists(task.getId());
		if(exists)
			throw new IllegalArgumentException("A task with the provided \"id\" already exists");
		repository.persist(task);
	}
	
	public static HelioTask getHelio(String id) {
		Optional<HelioTask> opt = repository.retrieve(id);
		if(!opt.isPresent())
			throw new ResourceNotPresentException("Requested Helio task not found");
		
		return opt.get();
	}

	public static List<HelioTask> listHelio() {
		return repository.retrieve();
	}

	public static void deleteHelio(String id) {
		boolean exists  = repository.exists(id);
		if(!exists)
			throw new IllegalArgumentException("Requested Helio task not found");
		repository.delete(id);
	}




}
