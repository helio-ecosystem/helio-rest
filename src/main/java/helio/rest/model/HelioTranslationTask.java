package helio.rest.model;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import helio.Helio;
import helio.blueprints.TranslationUnit;
import helio.blueprints.components.Components;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.exceptions.TranslationUnitExecutionException;
import helio.rest.HelioService;

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

	private int threads;

	@Transient @JsonIgnore
	public static Map<String, Helio> helios = new ConcurrentHashMap<>();
	
	
	// -- Constructor

	public HelioTranslationTask() {
		this.threads = 1;	
	}

	// -- Ancillary methods
	@JsonIgnore
	public void asemble() throws IncompatibleMappingException, IncorrectMappingException, ExtensionNotFoundException, TranslationUnitExecutionException {
		Helio helio = new Helio(threads);
		if (mappingContent != null && !mappingContent.isBlank() && mappingProcessor != null && !mappingProcessor.isBlank()) {
			Set<TranslationUnit> units = Components.newBuilderInstance(mappingProcessor).parseMapping(mappingContent);
			for(TranslationUnit unit:units) 
				helio.add(unit);
			helios.put(this.id, helio);
		} else {
			String message = HelioService.concat("Translation task '",this.id,"' can not be asembled because it has no mapping");
			throw new IncorrectMappingException(message);
		}
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

	
	public String getMappingLink() {
		if(!mappingContent.isBlank())
			return HelioService.concat("/api/",id,"/mapping");
		return null;
	}

	public String getDataLink() {
		if(!mappingContent.isBlank())
			return HelioService.concat("/api/",id,"/data");
		return null;
	}



	public String getMappingProcessor() {
		return mappingProcessor;
	}

	public void setMappingProcessor(String mappingProcessor) {
		this.mappingProcessor = mappingProcessor;
	}
	
	@JsonIgnore
	public Helio getHelio() {
		return helios.get(this.id);
	}

	@JsonIgnore
	public void setHelio(Helio helio) {
		helios.put(this.id, helio);
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
