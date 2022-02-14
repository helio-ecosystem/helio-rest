package helio.rest.controller;

import helio.rest.service.MappingService;
import spark.Request;
import spark.Response;
import spark.Route;

public class MappingController {

	public static final Route getMapping = (Request request, Response response) -> {
		String id = HelioTaskController.fetchId(request);
		return MappingService.getMapping(id);
	};

	public static final Route addUpdateMapping = (Request request, Response response) -> {
		String id = HelioTaskController.fetchId(request);
		String body = request.body();
		if(body==null || body.isBlank())
			throw new IllegalArgumentException("Missing mapping in body");
		String reader = null; //TODO: solve the reader issue
		
		MappingService.addUpdateMapping(id, body, reader);
		response.status(201);
		response.body(reader);
		
		return body;
	};

	public static final Route removeMapping = (Request request, Response response) -> {
		String id = HelioTaskController.fetchId(request);
		MappingService.deleteMapping(id);
		response.status(204);
		return "";
	};


}
