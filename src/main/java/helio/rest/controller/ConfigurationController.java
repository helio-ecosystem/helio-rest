package helio.rest.controller;

import org.apache.http.HttpHeaders;
import org.eclipse.jetty.http.MimeTypes;

import helio.rest.HelioService;
import helio.rest.exception.InvalidRequestException;
import helio.rest.model.configuration.ServiceConfiguration;
import helio.rest.service.HelioConfigurationService;
import spark.Response;
import spark.Request;
import spark.Route;


public class ConfigurationController{

	 // -- Methods for Main Configuration
	public static final Route getSingleton = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
		return HelioConfigurationService.getSingleton(); // throws exception if not found
	};

	public static final Route updateSingleton = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
		response.status(201);
		String body = request.body();
		if(body==null || body.isBlank())
			throw new InvalidRequestException("Missing configuration information, send a valid Json.");
		// re-load the in-memory translation tasks
		ServiceConfiguration conf = (ServiceConfiguration) HelioService.fromJson(body, ServiceConfiguration.class);
		return HelioConfigurationService.updateSingleton(conf);
	};

	public static final Route restoreSingleton = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
		return HelioConfigurationService.createDefault(true);
	};



}
