package helio.rest.service;

import java.util.Optional;

import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.ServiceConfiguration;
import helio.rest.repository.Repository;

public class HelioConfigurationService {


	protected static final Repository<ServiceConfiguration> repository = new Repository<>(ServiceConfiguration.class);

	public static boolean existsSingleton() {
		return repository.exists(ServiceConfiguration.ID);
	}

	public static ServiceConfiguration getSingleton() {
		Optional<ServiceConfiguration> opt = repository.retrieve(ServiceConfiguration.ID);
		if(!opt.isPresent())
			throw new ResourceNotPresentException("Requested configuration not found");
		return opt.get();
	}

	public static ServiceConfiguration updateSingleton(ServiceConfiguration newConfiguration) {

		repository.delete(ServiceConfiguration.ID);
		repository.persist(newConfiguration);
		return newConfiguration;
	}


	public static ServiceConfiguration createDefault(boolean override) {
		boolean exists = false;
		try {
			exists = repository.exists(ServiceConfiguration.ID);
		}catch(Exception e) {
			// Silent acceptance of exception, the configuration does not exists
		}
		if(!exists || (exists && override) ) {
			ServiceConfiguration defaultConfiguration = ServiceConfiguration.getDefault();

			if(exists)
				repository.delete(ServiceConfiguration.ID);
			repository.persist(defaultConfiguration);
			return defaultConfiguration;
		}
		return repository.retrieve(ServiceConfiguration.ID).get();
	}


}
