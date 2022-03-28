package helio.rest.controller;

import java.util.UUID;

import org.apache.http.HttpHeaders;
import org.eclipse.jetty.http.MimeTypes;
import spark.Request;
import helio.rest.HelioService;
import helio.rest.exception.InvalidRequestException;
import helio.rest.model.HelioTranslationTask;
import helio.rest.service.HelioTaskService;
import spark.Response;
import spark.Route;

public class TranslationTaskController {

	public static final Route list = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
		return HelioTaskService.listHelioTasks();
	};

	public static final Route get = (Request request, Response response) -> {
		String id = fetchId(request);
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
		return HelioTaskService.getTranslationTask(id); // throws exception if not found
	};

	public static final Route create = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());

		String body = request.body();
		//if(body==null || body.isBlank())
		//	throw new InvalidRequestException("Missing Helio task information, send a Json with the keys: \"id\" (mandatory), \"name\" (optional), \"description\" (optional).");
		// Do not create again the default configuration, reuse the existing one instead
		HelioTranslationTask task = new HelioTranslationTask();
		if(!body.isBlank()) 
			task = (HelioTranslationTask) HelioService.fromJson(body, HelioTranslationTask.class);
		
		if(task.getId()==null)
			task.setId(UUID.randomUUID().toString());

		HelioTaskService.createHelio(task);
		response.status(201);

		return task;
	};

	public static final Route delete = (Request request, Response response) -> {
		String id = fetchId(request);

		HelioTaskService.deleteHelio(id);

		response.status(204);
		response.body("");
		return "";
	};


	protected static final String fetchId(Request request) {
		String id = request.params("id");
		if(id==null || id.isEmpty())
			throw new InvalidRequestException("Missing valid Helio task id");
		return id;
	}

}
