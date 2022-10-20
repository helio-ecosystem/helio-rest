package helio.rest.controller;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.http.HttpHeaders;
import org.eclipse.jetty.http.MimeTypes;

import com.github.jsonldjava.shaded.com.google.common.collect.Maps;

import spark.Request;
import helio.blueprints.TranslationUnit;
import helio.rest.HelioRest;
import helio.rest.HelioService;
import helio.rest.exception.InternalServiceException;
import helio.rest.exception.InvalidRequestException;
import helio.rest.model.HelioTranslationTask;
import helio.rest.service.HelioTaskService;
import spark.Response;
import spark.Route;

public class TranslationTaskController {
	
	private static ExecutorService service = Executors.newScheduledThreadPool(10);

	
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
		String builder = request.queryParams("builder");
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
		
		boolean existed = false;
		try {
			task.asemble(true);
			existed = HelioTaskService.createUpdateTask(task);
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
		HelioTranslationTask task = HelioTaskService.getTranslationTask(id);
		task.asemble(false);
		Map<String, Object> params = Maps.newHashMap();
		for(Entry<String,String[]> entries: request.queryMap().toMap().entrySet()) {
			if(entries.getValue().length==1) {
				params.put(entries.getKey(), entries.getValue()[0]);
			}else {
				params.put(entries.getKey(), entries.getValue());
			}
		}
		
		// TODO: add to params things from the headers or from the body
		try {
			return task.getUnits().parallelStream().map(uniT -> runUnit(uniT, params)).collect(Collectors.joining());
		}catch(Exception e) {
			throw new InternalServiceException(e.toString());
		}
	};
	
	public static String runUnit(TranslationUnit unit, Map<String,Object> args)  {
		String result =  "";
		try {
		Future<String> f = service.submit(unit.getTask(args));
		
		result = f.get();
		
		//f.cancel(true);
		}catch(Exception e) {
			throw new InternalServiceException(e.toString());
		}
		return result;
	}
	
//	public static final Route queryData = (Request request, Response response) -> {
//		String query = request.body();
//		if(query==null || query.isEmpty())
//			throw new InvalidRequestException("Provide a valid SELECT SPARQL query");
//		Model model = ModelFactory.createDefaultModel();
//		HelioTaskService.listHelioTasks()
//					.parallelStream()
//					.filter(task -> task.getMappingProcessor().equals(HelioRest.DEFAULT_MAPPING_PROCESSOR))
//					.map(task -> HelioTranslationTask.helios.get(task.getId()))
//					.flatMap(helio -> {
//						try {
//							return helio.readAndFlushAll().stream();
//						} catch (HelioExecutionException | TranslationUnitExecutionException e) {
//							e.printStackTrace();
//						}
//						return null;
//					})
//					.filter(elem -> elem !=null).forEach(fragment -> {
//						try {
//							model.add(JSONLD11.loadIntoModel(fragment));
//						} catch (IncompatibleMappingException e) {
//							e.printStackTrace();
//						}
//					});
//		
//		HelioTaskService.listHelioTasks()
//		.parallelStream()
//		.filter(task -> !task.getMappingProcessor().equals(HelioRest.DEFAULT_MAPPING_PROCESSOR))
//		.map(task -> HelioTranslationTask.helios.get(task.getId()))
//		.flatMap(helio -> {
//			try {
//				return helio.readAndFlushAll().stream();
//			} catch (HelioExecutionException | TranslationUnitExecutionException e) {
//				e.printStackTrace();
//			}
//			return null;
//		})
//		.filter(elem -> elem !=null).forEach(fragment -> {
//				//model.read(new Bytefragment);
//			Model model2 = ModelFactory.createDefaultModel();
//			model2.read(new ByteArrayInputStream(fragment.getBytes()), null, "TURTLE");
//			model.add(model);
//			model2.close();
//		});
//		
//		
//	    QueryExecution qexec = QueryExecutionFactory.create(QueryFactory.create(query), model);
//	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		ResultSetFormatter.output(stream, qexec.execSelect(), ResultsFormat.FMT_RS_JSON);
//		return stream;
//	};
	
	
	
	protected static final String fetchId(Request request) {
		String id = request.params("id");
		if(id==null || id.isEmpty())
			throw new InvalidRequestException("Missing id");
		return id;
	}

}
