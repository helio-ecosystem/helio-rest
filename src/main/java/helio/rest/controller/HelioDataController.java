package helio.rest.controller;

import spark.Request;
import helio.rest.RestUtils;
import helio.rest.exception.InvalidRequestException;
import spark.Response;
import spark.Route;

public class HelioDataController {


	public static final Route runTasks = (Request request, Response response) -> {
		if(RestUtils.currentTasks.isEmpty())
			throw new InvalidRequestException("No translation task with a correct mapping was registered");
		RestUtils.currentTasks.entrySet().parallelStream().forEach(elem -> elem.getValue().runSynchronous());
		response.status(200);
		return "";
	};

	public static final Route runTask = (Request request, Response response) -> {
		if(RestUtils.currentTasks.isEmpty())
			throw new InvalidRequestException("No translation task with a correct mapping was registered");
		RestUtils.currentTasks.entrySet().parallelStream().forEach(elem -> elem.getValue().runSynchronous());
		response.status(200);
		return "";
	};

//	private static final String BULK_QUERY = "CONSTRUCT {?s ?p ?o} WHERE {?s ?p ?o}";
//	public static final Route getData = (Request request, Response response) -> {
//		return Sparql.query(BULK_QUERY, ResultsFormat.FMT_RDF_TURTLE);
//	};

}
