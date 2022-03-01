package helio.rest.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.gson.annotations.Expose;

import helio.Utils;
import helio.blueprints.Component;
import helio.blueprints.ComponentType;

@Entity
public class HelioComponent {

	@Id
	@Expose
	private String id;
	@Expose
	private String source;
	@Expose
	private String clazz;
	@Expose
	private ComponentType type;

	public HelioComponent() {
		super();
	}

	public HelioComponent(Component component) {

		this.source = component.getSource();
		this.clazz = component.getClazz();
		this.type = component.getType();
		this.id = String.valueOf(Utils.concatenate(this.source, this.clazz, this.type.toString()).hashCode()).replace('-', '0');
	}

	public void setId() {
		this.id = String.valueOf(Utils.concatenate(this.source, this.clazz, this.type.toString()).hashCode()).replace('-', '0');
	}

	public Component getComponent() {
		return new Component(source, clazz, type);
	}

	public boolean equivalent(Component component) {
		boolean equivalent = (this.source==null && component.getSource()==null)
				|| (this.source!=null && component.getSource()!=null && this.source.equals(component.getSource())) ;
		equivalent &= (this.clazz!=null && component.getClazz()!=null && this.clazz.equals(component.getClazz()));
		equivalent &= (this.type!=null && component.getType()!=null && this.type.equals(component.getType()));
		return equivalent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		HelioComponent other = (HelioComponent) obj;
		return Objects.equals(id, other.id);
	}


}
