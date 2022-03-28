package helio.rest.controller;

import spark.Request;
import helio.rest.HelioService;
import helio.rest.exception.InvalidRequestException;
import spark.Response;
import spark.Route;

public class HelioDataController {


	public static final Route runTasks = (Request request, Response response) -> {
		HelioService.currentTasks.forEach(task -> task.getHelio().runSynchronous());
		response.status(200);
		return "";
	};

	public static final Route runTask = (Request request, Response response) -> {
			String id = request.params("id");
			if(id==null || id.isEmpty())
				throw new InvalidRequestException("Missing valid translation task id");

			HelioService.currentTasks.parallelStream().filter(task -> task.getId().equals(id) ).forEach(task -> task.getHelio().runSynchronous());
			response.status(200);
			return "";
		};
	
	

	public static final Route runTaskUnit = (Request request, Response response) -> {
		String id = request.params("id");
		String unitId = request.params("unitId");
		if(id==null || id.isEmpty())
			throw new InvalidRequestException("Missing valid translation task id");
		if(unitId==null || unitId.isEmpty())
			throw new InvalidRequestException("Missing valid translation unit id");
		
		
		HelioService.currentTasks.parallelStream().filter(task -> task.getId().equals(id) ).forEach(task -> task.getHelio().runSynchronous());
		response.status(200);
		return "";
	};
//	private static final String BULK_QUERY = "CONSTRUCT {?s ?p ?o} WHERE {?s ?p ?o}";
//	public static final Route getData = (Request request, Response response) -> {
//		return Sparql.query(BULK_QUERY, ResultsFormat.FMT_RDF_TURTLE);
//	};

}
