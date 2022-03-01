package helio.rest.model.configuration;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import helio.configuration.Configuration;

@Entity
@JsonIgnoreProperties(value = { "id" })
public class HelioTranslationConfiguration  {

	@Id
	@Column(name="translation_configuration_id")
	private String id;
	private String namespace;
	private int threads;
	private boolean silentAcceptanceOfUnknownDatatypes;
	private boolean eagerLiteralValidation;
	private boolean bNodeUIDGeneration;
	private boolean owlRuleOverOWLRuleWarnings;


	public HelioTranslationConfiguration() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public int getThreads() {
		return threads;
	}
	public void setThreads(int threads) {
		this.threads = threads;
	}
	public boolean isSilentAcceptanceOfUnknownDatatypes() {
		return silentAcceptanceOfUnknownDatatypes;
	}
	public void setSilentAcceptanceOfUnknownDatatypes(boolean silentAcceptanceOfUnknownDatatypes) {
		this.silentAcceptanceOfUnknownDatatypes = silentAcceptanceOfUnknownDatatypes;
	}
	public boolean isEagerLiteralValidation() {
		return eagerLiteralValidation;
	}
	public void setEagerLiteralValidation(boolean eagerLiteralValidation) {
		this.eagerLiteralValidation = eagerLiteralValidation;
	}
	public boolean isbNodeUIDGeneration() {
		return bNodeUIDGeneration;
	}
	public void setbNodeUIDGeneration(boolean bNodeUIDGeneration) {
		this.bNodeUIDGeneration = bNodeUIDGeneration;
	}
	public boolean isOwlRuleOverOWLRuleWarnings() {
		return owlRuleOverOWLRuleWarnings;
	}
	public void setOwlRuleOverOWLRuleWarnings(boolean owlRuleOverOWLRuleWarnings) {
		this.owlRuleOverOWLRuleWarnings = owlRuleOverOWLRuleWarnings;
	}




	@Override
	public int hashCode() {
		return Objects.hash(bNodeUIDGeneration, eagerLiteralValidation, id, namespace, owlRuleOverOWLRuleWarnings,
				silentAcceptanceOfUnknownDatatypes, threads);
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		HelioTranslationConfiguration other = (HelioTranslationConfiguration) obj;
		return bNodeUIDGeneration == other.bNodeUIDGeneration && eagerLiteralValidation == other.eagerLiteralValidation
				&& Objects.equals(id, other.id) && Objects.equals(namespace, other.namespace)
				&& owlRuleOverOWLRuleWarnings == other.owlRuleOverOWLRuleWarnings
				&& silentAcceptanceOfUnknownDatatypes == other.silentAcceptanceOfUnknownDatatypes
				&& threads == other.threads;
	}








	public static HelioTranslationConfiguration build(Configuration configuration) {
		HelioTranslationConfiguration hConf = new HelioTranslationConfiguration();
		hConf.setbNodeUIDGeneration(configuration.isbNodeUIDGeneration());
		hConf.setEagerLiteralValidation(configuration.isEagerLiteralValidation());
		hConf.setNamespace(configuration.getNamespace());
		hConf.setOwlRuleOverOWLRuleWarnings(configuration.isOwlRuleOverOWLRuleWarnings());
		hConf.setSilentAcceptanceOfUnknownDatatypes(configuration.isSilentAcceptanceOfUnknownDatatypes());
		hConf.setThreads(configuration.getThreads());
		return hConf;
	}

	public Configuration transform() {
		Configuration configuration = new Configuration();
		configuration.setbNodeUIDGeneration(this.isbNodeUIDGeneration());
		configuration.setEagerLiteralValidation(this.isEagerLiteralValidation());
		configuration.setNamespace(this.getNamespace());
		configuration.setOwlRuleOverOWLRuleWarnings(this.isOwlRuleOverOWLRuleWarnings());
		configuration.setSilentAcceptanceOfUnknownDatatypes(this.isSilentAcceptanceOfUnknownDatatypes());
		configuration.setThreads(this.getThreads());
		return configuration;
	}


}
