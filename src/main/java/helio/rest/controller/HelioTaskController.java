package helio.rest.controller;

import helio.rest.RestUtils;
import helio.rest.model.HelioTask;
import helio.rest.service.HelioTaskService;
import spark.Request;
import spark.Response;
import spark.Route;

public class HelioTaskController {

	public static final Route list = (Request request, Response response) -> {
		return HelioTaskService.listHelio();
	};
	
	public static final Route get = (Request request, Response response) -> {
		String id = fetchId(request);
		return HelioTaskService.getHelio(id); // throws exception if not found
	};
	
	public static final Route create = (Request request, Response response) -> {
		String body = request.body();
		if(body==null || body.isBlank())
			throw new IllegalArgumentException("Missing Helio task information, send a Json with the keys: \"id\" (mandatory), \"name\" (optional), \"description\" (optional).");

		HelioTask task = RestUtils.getGSON().fromJson(body, HelioTask.class);
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
			throw new IllegalArgumentException("Missing valid Helio task id");
		return id;
	}
	
}
