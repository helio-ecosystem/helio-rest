package helio.rest.model.configuration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import helio.AbstractHelio;
import helio.rest.HelioRest;


@Entity
public class HelioTranslationConfiguration  {

    @Transient @JsonIgnore
	public static final String ID = "singleton:configuration:translation";
	@Id @JsonIgnore
	@Column(name="configuration_id")
	private String id;
	private boolean silentAcceptanceOfUnknownDatatypes;
	private boolean eagerLiteralValidation;
	private boolean bNodeUIDGeneration;
	private boolean owlRuleOverOWLRuleWarnings;
	private String defaultMappingProcessor;	
	
	public HelioTranslationConfiguration() {
		super();
		this.id = ID;
		this.bNodeUIDGeneration = AbstractHelio.isbNodeUIDGeneration();
		this.eagerLiteralValidation = AbstractHelio.isEagerLiteralValidation();
		this.owlRuleOverOWLRuleWarnings = AbstractHelio.isOwlRuleOverOWLRuleWarnings();
		this.silentAcceptanceOfUnknownDatatypes = AbstractHelio.isSilentAcceptanceOfUnknownDatatypes();
		apply();
		defaultMappingProcessor = HelioRest.DEFAULT_MAPPING_PROCESSOR;
	}

	public String getId() {
		return id;
	}

	public boolean isSilentAcceptanceOfUnknownDatatypes() {
		return silentAcceptanceOfUnknownDatatypes;
	}
	public void setSilentAcceptanceOfUnknownDatatypes(boolean silentAcceptanceOfUnknownDatatypes) {
		this.silentAcceptanceOfUnknownDatatypes = silentAcceptanceOfUnknownDatatypes;
		apply();
	}
	public boolean isEagerLiteralValidation() {
		return eagerLiteralValidation;
	}
	public void setEagerLiteralValidation(boolean eagerLiteralValidation) {
		this.eagerLiteralValidation = eagerLiteralValidation;
		apply();
	}
	public boolean isbNodeUIDGeneration() {
		return bNodeUIDGeneration;
	}
	public void setbNodeUIDGeneration(boolean bNodeUIDGeneration) {
		this.bNodeUIDGeneration = bNodeUIDGeneration;
		apply();
	}
	public boolean isOwlRuleOverOWLRuleWarnings() {
		return owlRuleOverOWLRuleWarnings;
	}
	public void setOwlRuleOverOWLRuleWarnings(boolean owlRuleOverOWLRuleWarnings) {
		this.owlRuleOverOWLRuleWarnings = owlRuleOverOWLRuleWarnings;
		apply();
	}

	public String getDefaultMappingProcessor() {
		return defaultMappingProcessor;
	}

	public void setDefaultMappingProcessor(String defaultMappingProcessor) {
		this.defaultMappingProcessor = defaultMappingProcessor;
	}

	public void setId(String id) {
		this.id = id;
	}

	private void apply() {
		AbstractHelio.setbNodeUIDGeneration(this.isbNodeUIDGeneration());
		AbstractHelio.setEagerLiteralValidation(this.isEagerLiteralValidation());
		AbstractHelio.setOwlRuleOverOWLRuleWarnings(this.isOwlRuleOverOWLRuleWarnings());
		AbstractHelio.setSilentAcceptanceOfUnknownDatatypes(this.isSilentAcceptanceOfUnknownDatatypes());		
	}


}
