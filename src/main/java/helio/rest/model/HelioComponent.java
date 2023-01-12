package helio.rest.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import helio.blueprints.components.Component;
import helio.rest.HelioService;

@Entity
public class HelioComponent {

	@Id
	private String id;
	
	@Column( name = "component", columnDefinition="BLOB NOT NULL")
	@Lob
	private String component;
	
	public HelioComponent() {
		super();
	}

	public HelioComponent(Component component) {
		this.component = HelioService.toJson(component);
		this.id = String.valueOf(component.getClazz()).replace('-', '0');
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = String.valueOf(this.component.hashCode());
	}

	public Component getComponent() throws JsonMappingException, JsonProcessingException {
		return HelioService.MAPPER.readValue(this.component, Component.class);
	}

	public void setComponent(Component component) {
		this.component = HelioService.toJson(component);
		setId(String.valueOf(this.component.hashCode()).replace('-', '0'));
	}

	@Override
	public int hashCode() {
		return Objects.hash(component, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HelioComponent other = (HelioComponent) obj;
		return Objects.equals(component, other.component) && id == other.id;
	}

	private static final Gson gson= new Gson();
	@Override
	public String toString() {
		JsonObject jsonObject =  gson.fromJson(this.component, JsonObject.class);
		JsonObject component = new JsonObject();
		component.addProperty("id", this.id);
		component.add("component", jsonObject);
		return component.toString();
	}

	

}
