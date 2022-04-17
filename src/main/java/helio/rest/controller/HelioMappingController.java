package helio.rest.controller;

import org.apache.http.HttpHeaders;
import org.eclipse.jetty.http.MimeTypes;
import spark.Request;
import helio.blueprints.components.Components;
import helio.rest.HelioService;
import helio.rest.exception.InvalidRequestException;
import spark.Response;
import spark.Route;

public class HelioMappingController {


	private static String getReader(String arg) {
		if("rml".equals(arg)) {
			return "JMappingProcessor";
		}else if("jsonld".equals(arg)) {
			return "JLD11Builder";
		}else if("json".equals(arg)) {
			return "JMappingProcessor";
		}
		return null;
	}

	// Processors
	
	public static final Route listProcessors = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
		response.status(200);
		return HelioService.toJson(Components.getMappingProcessors().keySet());
	};


}
