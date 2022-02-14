package helio.rest.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

import helio.Helio;
import helio.rest.RestUtils;

@Entity
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

	@Transient
	private Helio helio;

	public HelioTask() {
		super();
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

	public Helio getHelio() {
		return helio;
	}

	public void setHelio(Helio helio) {
		this.helio = helio;
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
