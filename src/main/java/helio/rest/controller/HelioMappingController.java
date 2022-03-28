package helio.rest.controller;

import org.apache.http.HttpHeaders;
import org.eclipse.jetty.http.MimeTypes;
import spark.Request;
import helio.blueprints.Components;
import helio.rest.HelioService;
import helio.rest.exception.InvalidRequestException;
import helio.rest.service.MappingService;
import spark.Response;
import spark.Route;

public class HelioMappingController {

	
	
	
	public static final Route getMapping = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());

		String id = TranslationTaskController.fetchId(request);
		return MappingService.getMapping(id);
	};

	public static final Route addUpdateMapping = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());

		String id = TranslationTaskController.fetchId(request);
		String body = request.body();
		if(body==null || body.isBlank())
			throw new InvalidRequestException("Missing mapping in body");

		String reader = request.queryParams("reader");
		if(reader!=null)
			reader = getReader(reader);

		MappingService.addUpdateMapping(id, body, reader);
		response.status(201);
		response.body(reader);

		return body;
	};

	private static String getReader(String arg) {
		if("rml".equals(arg)) {
			return "RmlReader";
		}else if("json".equals(arg)) {
			return "JsonReader";
		}
		return null;
	}

	public static final Route removeMapping = (Request request, Response response) -> {
		String id = TranslationTaskController.fetchId(request);
		MappingService.deleteMapping(id);
		response.status(204);
		return "";
	};

	// Processors
	
	public static final Route listProcessors = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
		response.status(200);
		return HelioService.toJson(Components.getMappingProcessors().keySet());
	};


}
