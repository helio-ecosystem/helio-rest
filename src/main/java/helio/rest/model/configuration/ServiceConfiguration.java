package helio.rest.model.configuration;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import helio.rest.HelioService;
import sparql.streamline.core.SparqlEndpointConfiguration;


@Entity
public class ServiceConfiguration {
	@Transient @JsonIgnore
	public static final String ID = "singleton:configuration:service";
	@Id @JsonIgnore
	protected String id;
	private int port;
	private int maxThreads;
	private int minThreads;
	private int timeOutMillis;
	
	@Column( name = "sparql_endpoint", columnDefinition="BLOB")
	@Lob
	private String sparqlConfiguration;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "translation_configuration_id", unique = true)
	private HelioTranslationConfiguration translationConfiguration;


	public ServiceConfiguration() {
		super();
		id = ID;
		translationConfiguration = new HelioTranslationConfiguration();
	}

	public static ServiceConfiguration getDefault() {
		ServiceConfiguration configuration = new ServiceConfiguration();
		configuration.setMaxThreads(20);
		configuration.setMinThreads(2);
		configuration.setPort(4567);
		configuration.setTimeOutMillis(30000);
		return configuration;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getSparqlConfigurationRaw() throws JsonMappingException, JsonProcessingException {
		return this.sparqlConfiguration;
	}
	public SparqlEndpointConfiguration getSparqlConfiguration() throws JsonMappingException, JsonProcessingException {
		return HelioService.MAPPER.readValue(this.sparqlConfiguration, SparqlEndpointConfiguration.class);
	}

	public void setSparqlConfiguration(String sparqlConfiguration) {
		this.sparqlConfiguration = sparqlConfiguration;
	}

	public HelioTranslationConfiguration getTranslationConfiguration() {
		return translationConfiguration;
	}

	public void setTranslationConfiguration(HelioTranslationConfiguration translationConfiguration) {
		this.translationConfiguration = translationConfiguration;
	}
	
	public String toString() {
		return HelioService.toJson(this);
	}

	
}
