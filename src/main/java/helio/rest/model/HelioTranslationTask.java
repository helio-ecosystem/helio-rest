package helio.rest.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import helio.Helio;
import helio.TranslationUnitAlfa;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.exceptions.MappingExecutionException;
import helio.blueprints.mappings.TripleMapping;
import helio.blueprints.objects.TranslationUnit;
import helio.components.processors.MappingProcessors;
import helio.rest.HelioRest;
import helio.rest.HelioService;
import sparql.streamline.core.SparqlEndpoint;
import sparql.streamline.core.SparqlEndpointConfiguration;
import sparql.streamline.exception.SparqlConfigurationException;

@Entity
public class HelioTranslationTask {

	// -- Attributes
	@Id
	private String id;

	@Column(name = "content", columnDefinition = "BLOB")
	@Lob
	@JsonIgnore
	private String mappingContent;

	private String mappingProcessor;

	@Column(name = "sparql_configuration", columnDefinition = "BLOB")
	@Lob
	private String configuration;

	private int threads;
	
	@Transient @JsonIgnore
	private Helio helio;

	// TODO: private List<HelioTranslationUnit> translationUnits;
	
	// -- Constructor

	public HelioTranslationTask() {
		this.id = String.valueOf(UUID.randomUUID());
		this.threads = 10;
		this.helio = new Helio();
		//this.translationUnits = new ArrayList<>();
		this.mappingProcessor = HelioRest.serviceConfiguration.getTranslationConfiguration().getDefaultMappingProcessor();
		try {
			this.configuration = HelioRest.serviceConfiguration.getSparqlConfigurationRaw();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -- Ancillary methods

	public String asemble() throws IncompatibleMappingException, IncorrectMappingException, ExtensionNotFoundException,
			SparqlConfigurationException, MappingExecutionException {
		
		this.helio.clearExecutors();
		StringBuilder errors = new StringBuilder();
		Set<TripleMapping> mapping = new HashSet<>();
		if (mappingContent != null && !mappingContent.isBlank() && mappingProcessor != null
				&& !mappingProcessor.isBlank()) {
			mapping = MappingProcessors.processMapping(mappingProcessor, mappingContent);
		} else if (mappingContent != null && !mappingContent.isBlank() && mappingProcessor == null) {
			mapping = MappingProcessors.processMapping(mappingContent);
		}
		mapping.parallelStream().map(tm -> {
			try {
				TranslationUnit unit = new TranslationUnitAlfa(new SparqlEndpoint(getSparqlConfiguration()), tm, threads);
				//TODO: add here the information
				//HelioTranslationUnit helioUnit = new HelioTranslationUnit();
				//this.translationUnits.add(helioUnit);
				return unit;
			} catch (Exception e) {
				errors.append(e.toString());
				e.printStackTrace();

			}
			return null;
		}).filter(elem -> elem != null).forEach(tUnit -> this.helio.add(tUnit));
	
		return errors.toString();
	}

	// -- Getters & Setters

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMappingContent() {
		return mappingContent;
	}

	public void setMappingContent(String mappingContent)  {
		this.mappingContent = mappingContent;
	}

	public String getMappingProcessor() {
		return mappingProcessor;
	}

	public void setMappingProcessor(String mappingProcessor) {
		this.mappingProcessor = mappingProcessor;
	}

	public SparqlEndpointConfiguration getSparqlConfiguration() throws JsonMappingException, JsonProcessingException {
		return HelioService.MAPPER.readValue(this.configuration, SparqlEndpointConfiguration.class);
	}

	public void setSparqlConfiguration(String sparqlConfiguration) {
		this.configuration = sparqlConfiguration;
	}
	
	public Helio getHelio() {
		return helio;
	}


	public void setHelio(Helio helio) {
		this.helio = helio;
	}



	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	// -- Hash Code & Equals
	

	// --

	@Override
	public String toString() {
		return HelioService.toJson(this);
	}

}
