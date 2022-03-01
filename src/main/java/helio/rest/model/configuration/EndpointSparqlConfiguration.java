package helio.rest.model.configuration;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import sparql.streamline.core.SparqlEndpointConfiguration;

@Entity
@JsonIgnoreProperties(value = { "id", "main" })
public class EndpointSparqlConfiguration  {

	@Id
	@Column(name="endpoint_configuration_id")
	private String id;
	private String queryEndpoint;
	private String updateEnpoint;
	private String username;
	private String password;
	private boolean main;


	public EndpointSparqlConfiguration() {
		super();
	}

	public static EndpointSparqlConfiguration build(SparqlEndpointConfiguration configuration) {
		EndpointSparqlConfiguration conf = new EndpointSparqlConfiguration();
		conf.setQueryEndpoint(configuration.getEndpointQuery());
		conf.setUpdateEnpoint(configuration.getEndpointUpdate());
		conf.setUsername(configuration.getUsername());
		conf.setPassword(configuration.getPassword());
		return conf;
	}

	public SparqlEndpointConfiguration transform() {
		SparqlEndpointConfiguration conf = new SparqlEndpointConfiguration();
		conf.setEndpointQuery(this.queryEndpoint);
		conf.setEndpointUpdate(this.updateEnpoint);
		conf.setUsername(this.username);
		conf.setPassword(this.password);
		return conf;
	}

	public String getQueryEndpoint() {
		return queryEndpoint;
	}

	public void setQueryEndpoint(String queryEndpoint) {
		this.queryEndpoint = queryEndpoint;
	}

	public String getUpdateEnpoint() {
		return updateEnpoint;
	}

	public void setUpdateEnpoint(String updateEnpoint) {
		this.updateEnpoint = updateEnpoint;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.main = main;
	}

	@Override
	public int hashCode() {
		return Objects.hash(queryEndpoint, updateEnpoint);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		EndpointSparqlConfiguration other = (EndpointSparqlConfiguration) obj;
		return Objects.equals(queryEndpoint, other.queryEndpoint) && Objects.equals(updateEnpoint, other.updateEnpoint);
	}

}
