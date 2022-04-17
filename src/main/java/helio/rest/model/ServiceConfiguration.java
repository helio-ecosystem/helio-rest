package helio.rest.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import helio.rest.HelioService;


@Entity
public class ServiceConfiguration {
	@Transient @JsonIgnore
	public static final String ID = "singleton:configuration:service";
	@Id @JsonIgnore
	protected String id;
	private int port;
	private int maxThreads;
	private int minThreads;
	private int timeOutMillis;
	

	public ServiceConfiguration() {
		super();
		id = ID;
	}

	public static ServiceConfiguration getDefault() {
		ServiceConfiguration configuration = new ServiceConfiguration();
		configuration.setMaxThreads(20);
		configuration.setMinThreads(2);
		configuration.setPort(4567);
		configuration.setTimeOutMillis(30000);
		return configuration;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public int getMinThreads() {
		return minThreads;
	}

	public void setMinThreads(int minThreads) {
		this.minThreads = minThreads;
	}

	public int getTimeOutMillis() {
		return timeOutMillis;
	}

	public void setTimeOutMillis(int timeOutMillis) {
		this.timeOutMillis = timeOutMillis;
	}


	public String toString() {
		return HelioService.toJson(this);
	}

	
}
