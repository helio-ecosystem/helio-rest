package helio.rest.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;

import helio.Helio;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.mappings.Mapping;
import helio.exceptions.ConfigurationException;
import helio.rest.RestUtils;
import helio.rest.model.configuration.EndpointSparqlConfiguration;
import helio.rest.model.configuration.HelioTranslationConfiguration;
import sparql.streamline.exception.SparqlConfigurationException;

@Entity
@JsonIgnoreProperties(value = { "mappingContent", "mappingReader" })
public class HelioTask {

	@Id
	@Expose
	private String id;
	@Expose
	private String name;
	@Expose
	private String description;

	@Column( name = "content", columnDefinition="BLOB")
	@Lob
	private String mappingContent;

	private String mappingReader;

	@Expose
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "endpoint_configuration_id", unique = false)
	private EndpointSparqlConfiguration endpoint;
	@Expose
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "translation_configuration_id", unique = false)
	private HelioTranslationConfiguration configuration;

	private boolean isActive = true;

	public HelioTask() {

	}

	public Helio asemble() throws IncompatibleMappingException, IncorrectMappingException, ExtensionNotFoundException, SparqlConfigurationException, ConfigurationException {

		Mapping mapping =  ModelUtils.readMapping(mappingContent, mappingReader);
		if(mapping==null)
			throw new IncorrectMappingException("Provided mapping can not be parsed, check for syntax errors or missing components (Readers) for parsing it");
		// Create helio tasks
		Helio helio = new Helio();
		helio.setSparqlEndpointConfiguration(endpoint.transform());
		helio.setConfiguration(configuration.transform());
		helio.addMapping(mapping);
		return helio;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EndpointSparqlConfiguration getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(EndpointSparqlConfiguration endpoint) {
		this.endpoint = endpoint;
	}

	public HelioTranslationConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(HelioTranslationConfiguration configuration) {
		this.configuration = configuration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMappingContent() {
		return mappingContent;
	}

	public void setMappingContent(String mappingContent) {
		this.mappingContent = mappingContent;
	}

	public String getMappingReader() {
		return mappingReader;
	}

	public void setMappingReader(String mappingReader) {
		this.mappingReader = mappingReader;
	}


	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		HelioTask other = (HelioTask) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return RestUtils.toJson(this);
	}




}
