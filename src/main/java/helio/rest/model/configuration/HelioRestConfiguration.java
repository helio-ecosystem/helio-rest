package helio.rest.model.configuration;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import helio.configuration.Configuration;

@Entity
@JsonIgnoreProperties(value = { "id" })
public class HelioRestConfiguration {

	@Id
	protected String id;
	private int port;
	private int maxThreads;
	private int minThreads;
	private int timeOutMillis;

	// private int eventsSize;

	// private String eventsFile;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "endpoint_configuration_id", unique = true)
	private EndpointSparqlConfiguration endpointConfiguration;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "translation_configuration_id", unique = true)
	private HelioTranslationConfiguration translationConfiguration;


	public HelioRestConfiguration() {
		super();
	}

	public static HelioRestConfiguration getDefault() {
		HelioRestConfiguration configuration = new HelioRestConfiguration();
		configuration.setMaxThreads(20);
		configuration.setMinThreads(2);
		configuration.setPort(4567);
		configuration.setTimeOutMillis(30000);
		HelioTranslationConfiguration translationConfiguration = HelioTranslationConfiguration.build(Configuration.createDefault());
		configuration.setTranslationConfiguration(translationConfiguration);
		return configuration;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getMinThreads() {
		return minThreads;
	}

	public void setMinThreads(int minThreads) {
		this.minThreads = minThreads;
	}

	public int getTimeOutMillis() {
		return timeOutMillis;
	}

	public void setTimeOutMillis(int timeOutMillis) {
		this.timeOutMillis = timeOutMillis;
	}

	public EndpointSparqlConfiguration getEndpointConfiguration() {
		return endpointConfiguration;
	}

	public void setEndpointConfiguration(EndpointSparqlConfiguration endpointConfiguration) {
		this.endpointConfiguration = endpointConfiguration;
	}

	public HelioTranslationConfiguration getTranslationConfiguration() {
		return translationConfiguration;
	}

	public void setTranslationConfiguration(HelioTranslationConfiguration translationConfiguration) {
		this.translationConfiguration = translationConfiguration;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
