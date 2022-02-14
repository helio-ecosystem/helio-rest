package helio.rest.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.gson.annotations.Expose;

import helio.rest.RestUtils;

@Entity
public class Component {

	@Id
	@Expose
	private String source;
	@Expose
	private String clazz;
	@Expose
	private String type;

	public Component() {
		super();
	}

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}


	@Override
	public int hashCode() {
		return Objects.hash(clazz, source, type);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		Component other = (Component) obj;
		return Objects.equals(clazz, other.clazz) && Objects.equals(source, other.source)
				&& Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return RestUtils.toJson(this);
	}


}
