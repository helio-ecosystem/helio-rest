package helio.rest.controller;

import java.util.List;
import java.util.UUID;

import org.apache.http.HttpHeaders;
import org.eclipse.jetty.http.MimeTypes;
import spark.Request;
import helio.Helio;
import helio.rest.HelioRest;
import helio.rest.HelioService;
import helio.rest.exception.InternalServiceException;
import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
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

	public static final Route createUpdate = (Request request, Response response) -> {
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
		String id = request.params("id");
		String builder = request.params("builder");
		String body = request.body();
		if(body==null || body.isBlank())
			throw new InvalidRequestException(HelioService.concat("Missing mapping in the body for instantiating the translation task. Provide a valid mapping and use the argument ?builder for parsing the mapping (by default ", HelioRest.DEFAULT_MAPPING_PROCESSOR, " is used)"));

		HelioTranslationTask task = new HelioTranslationTask();
		if(!body.isBlank()) {
			if(builder==null || builder.isEmpty())
				builder = HelioRest.DEFAULT_MAPPING_PROCESSOR;
			task.setMappingProcessor(builder);
			task.setMappingContent(body);
			
			if(id==null)
				id = UUID.randomUUID().toString();
			task.setId(id);
		}
		
		boolean existed = HelioTaskService.createUpdateTask(task);
		try {
			task.asemble();
		}catch(Exception e) {
			throw new InternalServiceException(e.toString());
		}
		
		response.status(201);
		if(existed)
			response.status(204);

		return task;
	};

	public static final Route delete = (Request request, Response response) -> {
		String id = fetchId(request);

		HelioTaskService.deleteHelio(id);

		response.status(204);
		response.body("");
		return "";
	};

	public static final Route getMapping = (Request request, Response response) -> {
		String id = fetchId(request);
		HelioTranslationTask task =  HelioTaskService.getTranslationTask(id);
		String mappingBuilder = task.getMappingProcessor();
		if(mappingBuilder!=null && (mappingBuilder.equals(HelioRest.DEFAULT_MAPPING_PROCESSOR)|| mappingBuilder.equals("JMappingProcessor")))
			response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());

		response.status(200);
		return task.getMappingContent();
	
	};

	public static final Route getTranslationData = (Request request, Response response) -> {
		String id = fetchId(request);
		StringBuilder str = new StringBuilder();
		
			Helio helio = HelioTranslationTask.helios.get(id);
			if(helio==null)
				throw new ResourceNotPresentException("Provided id belongs to no translation task");
		try {
		List<String> data = helio.readAndFlushAll();
		
		data.parallelStream().forEach(elem -> str.append(elem));
		//String mappingBuilder = task.getMappingProcessor();
		//if(mappingBuilder!=null && (mappingBuilder.equals(HelioRest.DEFAULT_MAPPING_PROCESSOR)))
		response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
		}catch(Exception e) {
			throw new InternalServiceException(e.toString());
		}
		return str.toString();
	};
	
	protected static final String fetchId(Request request) {
		String id = request.params("id");
		if(id==null || id.isEmpty())
			throw new InvalidRequestException("Missing id");
		return id;
	}

}
