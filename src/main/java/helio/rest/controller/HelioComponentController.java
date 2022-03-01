package helio.rest.controller;

import java.util.stream.Collectors;


import helio.rest.RestUtils;
import helio.rest.exception.InvalidRequestException;
import helio.rest.model.HelioComponent;
import helio.rest.service.HelioComponentService;
import spark.Response;
import spark.Request;
import spark.Route;

public class HelioComponentController {



	public static final Route list = (Request request, Response response) -> {
		return HelioComponentService.list().parallelStream().map(RestUtils::toJson).collect(Collectors.toList());
	};

	public static final Route get = (Request request, Response response) -> {
		String id = fetchId(request);
		return RestUtils.toJson(HelioComponentService.get(id)); // throws exception if not found
	};

	public static final Route create = (Request request, Response response) -> {
		String body = request.body();
		if(body==null || body.isBlank())
			throw new InvalidRequestException("Missing Component information, send a Json with the mandatory keys: \"source\", \"clazz\", \"type\".");

		HelioComponent component = (HelioComponent) RestUtils.fromJson(body, HelioComponent.class);
		component.setId();
		HelioComponentService.add(component);
		response.status(201);

		return RestUtils.toJson(component);
	};

	public static final Route delete = (Request request, Response response) -> {
		String id = fetchId(request);

		HelioComponentService.delete(id);

		response.status(204);
		response.body("");
		return "";
	};

	public static final Route restore = (Request request, Response response) -> {
		HelioComponentService.list().parallelStream().forEach(cmp -> HelioComponentService.delete(cmp.getId()));
		RestUtils.loadDefaultComponents();
		return "";
	};


	protected static final String fetchId(Request request) {
		String id = request.params("id");
		if(id==null || id.isEmpty())
			throw new InvalidRequestException("Missing valid Component id");
		return id;
	}
}
