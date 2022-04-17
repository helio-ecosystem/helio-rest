package helio.rest.service;

import java.util.List;
import java.util.Optional;

import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.exceptions.TranslationUnitExecutionException;
import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.HelioTranslationTask;
import helio.rest.repository.Repository;

public class HelioTaskService {

	protected static final Repository<HelioTranslationTask> repository = new Repository<>(HelioTranslationTask.class);

	public static boolean createUpdateTask(HelioTranslationTask task) throws IncompatibleMappingException, IncorrectMappingException, ExtensionNotFoundException, TranslationUnitExecutionException {
		Optional<HelioTranslationTask> exists  = repository.retrieve(task.getId());
		if(exists.isPresent()) {
			HelioTranslationTask old = exists.get();
			old.setMappingContent(task.getMappingContent());
			if(task.getMappingProcessor()!=null && task.getMappingProcessor()!=old.getMappingProcessor())
				old.setMappingProcessor(task.getMappingProcessor());
		}else {
			repository.persist(task);
		}
		
		return exists.isPresent();
	}

	public static HelioTranslationTask getTranslationTask(String id) {
		Optional<HelioTranslationTask> opt = repository.retrieve(id);
		if(!opt.isPresent())
			throw new ResourceNotPresentException("Requested Helio task not found");

		return opt.get();
	}

	public static List<HelioTranslationTask> listHelioTasks() {
		return repository.retrieve();
	}

	public static void deleteHelio(String id) {
		boolean exists  = repository.exists(id);
		if(!exists)
			throw new InvalidRequestException("Requested Helio task not found");
		HelioTranslationTask.helios.remove(id); // delete in memory
		repository.delete(id);
	}




}
