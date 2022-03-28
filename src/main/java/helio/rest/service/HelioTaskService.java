package helio.rest.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import helio.rest.HelioRest;
import helio.rest.exception.InternalServiceException;
import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.HelioTranslationTask;
import helio.rest.model.configuration.ServiceConfiguration;
import helio.rest.model.configuration.HelioTranslationConfiguration;
import helio.rest.repository.Repository;
import sparql.streamline.core.SparqlEndpointConfiguration;

public class HelioTaskService {

	protected static final Repository<HelioTranslationTask> repository = new Repository<>(HelioTranslationTask.class);

	public static void createHelio(HelioTranslationTask task) {
		boolean exists  = repository.exists(task.getId());
		if(exists)
			throw new InvalidRequestException("A task with the provided \"id\" already exists");

		//TODO: REMOVE if(task.getSparqlConfiguration()==null)
		//	task.setConfiguration(HelioRest.serviceConfiguration.getSparqlConfiguration());
		
		repository.persist(task);
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
		repository.delete(id);
	}




}
