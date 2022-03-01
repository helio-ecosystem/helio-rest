package helio.rest.service;

import java.util.Optional;

import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.configuration.EndpointSparqlConfiguration;
import helio.rest.model.configuration.HelioRestConfiguration;
import helio.rest.model.configuration.HelioTranslationConfiguration;
import helio.rest.repository.Repository;

public class HelioConfigurationService {


	protected static final Repository<HelioRestConfiguration> repository = new Repository<>(HelioRestConfiguration.class);

	private static final String SINGLETON_CONFIGURATION_ID = "singleton-id";
	private static final String SINGLETON_CONFIGURATION_ID_SPARQL = "singleton-id-sparql";
	private static final String SINGLETON_CONFIGURATION_ID_TRANSLATION = "singleton-id-translation";


	public static HelioRestConfiguration getSingleton() {
		Optional<HelioRestConfiguration> opt = repository.retrieve(SINGLETON_CONFIGURATION_ID);
		if(!opt.isPresent())
			throw new ResourceNotPresentException("Requested configuration not found");
		HelioRestConfiguration s = opt.get();
		return opt.get();
	}

	public static HelioRestConfiguration updateSingleton(HelioRestConfiguration newConfiguration) {

		repository.delete(SINGLETON_CONFIGURATION_ID);
		newConfiguration = setupIds(newConfiguration);

		repository.persist(newConfiguration);
		return newConfiguration;
	}

	private static HelioRestConfiguration setupIds(HelioRestConfiguration configuration) {
		configuration.setId(SINGLETON_CONFIGURATION_ID);
		EndpointSparqlConfiguration conf1 = configuration.getEndpointConfiguration();
		if(conf1!=null) {
			conf1.setId(SINGLETON_CONFIGURATION_ID_SPARQL);
			configuration.setEndpointConfiguration(conf1);
		}
		HelioTranslationConfiguration conf2 = configuration.getTranslationConfiguration();
		if(conf2!=null) {
			conf2.setId(SINGLETON_CONFIGURATION_ID_TRANSLATION);
			configuration.setTranslationConfiguration(conf2);
		}
		return configuration;
	}

	public static HelioRestConfiguration createDefault(boolean override) {
		boolean exists = repository.exists(SINGLETON_CONFIGURATION_ID);
		if(!exists || (exists && override) ) {
			HelioRestConfiguration defaultConfiguration = HelioRestConfiguration.getDefault();

			EndpointSparqlConfiguration sparql = new EndpointSparqlConfiguration();
			sparql.setQueryEndpoint("http://localhost:7200/repositories/app");
			sparql.setUpdateEnpoint("http://localhost:7200/repositories/app/statements");
			sparql.setMain(true);
			defaultConfiguration.setEndpointConfiguration(sparql);

			defaultConfiguration = setupIds(defaultConfiguration);
			repository.delete(SINGLETON_CONFIGURATION_ID);
			repository.persist(defaultConfiguration);
			return defaultConfiguration;
		}
		return repository.retrieve(SINGLETON_CONFIGURATION_ID).get();
	}


}
