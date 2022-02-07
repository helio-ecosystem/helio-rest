package helio.rest.controller;


import java.util.UUID;

import helio.blueprints.Components;
import helio.blueprints.components.MappingReader;
import helio.blueprints.exceptions.IncorrectMappingException;
import helio.blueprints.mappings.Mapping;
import helio.rest.model.ServiceMapping;
import helio.rest.service.MappingService;
import spark.Request;
import spark.Response;
import spark.Route;

public class MappingController {

	public static final Route listMappings = (Request request, Response response) -> {    
		
		return "";
	};
	
	public static final Route getMapping = (Request request, Response response) -> {    
		
		return "";
	};
	
	public static final Route deleteMapping = (Request request, Response response) -> {    
		
		return "";
	};
	
	public static final Route registerRmlMapping = (Request request, Response response) -> {    
		String id = getId(request);
		String name = request.attribute("name");
		String content = getMappingContent(request);
		MappingService.insertMapping(id, name, content, "RmlReader");
		response.status(201);
		return "";
	};
	
	
	private static final String getId(Request request) {
		String id = request.params("id");
		if(id==null || id.isBlank())
			id = UUID.randomUUID().toString();
		return id;
	}
	
	private static final String getMappingContent(Request request) {
		String mappingContent = request.body();
		if(mappingContent==null || mappingContent.isBlank())
			throw new IncorrectMappingException("Provided mapping can not be null or empty");
		return mappingContent;
	}
}
