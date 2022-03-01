package helio.rest.service;

import java.util.List;
import java.util.Optional;

import helio.rest.exception.InternalServiceException;
import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.HelioTask;
import helio.rest.model.configuration.EndpointSparqlConfiguration;
import helio.rest.model.configuration.HelioTranslationConfiguration;
import helio.rest.repository.Repository;

public class HelioTaskService {

	protected static final Repository<HelioTask> repository = new Repository<>(HelioTask.class);

	public static void createHelio(HelioTask task) {
		boolean exists  = repository.exists(task.getId());
		if(exists)
			throw new InvalidRequestException("A task with the provided \"id\" already exists");

		if(task.getConfiguration()==null) {
			HelioTranslationConfiguration conf = HelioConfigurationService.getSingleton().getTranslationConfiguration();
			if(conf==null) throw new InternalServiceException("Provide a valid (general) configuration for the translation tasks (maybe restore default configuration), or provide a specific configuration for this task. ");
			task.setConfiguration(conf);
		}
		if(task.getEndpoint()==null) {
			EndpointSparqlConfiguration conf = HelioConfigurationService.getSingleton().getEndpointConfiguration();
			if(conf==null) throw new InternalServiceException("Provide a valid (general) configuration for the SPARQL endpoint (maybe restore default configuration), or provide a specific configuration for this task. ");
			task.setEndpoint(conf);
		}

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
			throw new InvalidRequestException("Requested Helio task not found");
		repository.delete(id);
	}




}
