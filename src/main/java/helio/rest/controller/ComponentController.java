package helio.rest.controller;


import org.apache.http.HttpHeaders;
import org.eclipse.jetty.http.MimeTypes;

import helio.blueprints.components.Component;
import helio.rest.HelioService;
import helio.rest.exception.InvalidRequestException;
import helio.rest.service.HelioComponentService;
import spark.Response;
import spark.Request;
import spark.Route;

public class ComponentController {



	public static final Route list = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
		return HelioComponentService.list();
	};

	public static final Route get = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
		String id = fetchId(request);
		return HelioService.toJson(HelioComponentService.get(id)); // throws exception if not found
	};

	public static final Route create = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());

		String body = request.body();
		if(body==null || body.isBlank())
			throw new InvalidRequestException("Missing Component information, send a Json with the mandatory keys: \"source\", \"clazz\", \"type\".");

		Component component = (Component) HelioService.fromJson(body, Component.class);
		HelioComponentService.add(component);
		response.status(201);

		return HelioService.toJson(component);
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
		HelioService.loadDefaultComponents();
		return "";
	};


	protected static final String fetchId(Request request) {
		String id = request.params("id");
		if(id==null || id.isEmpty())
			throw new InvalidRequestException("Missing valid Component id");
		return id;
	}
}
