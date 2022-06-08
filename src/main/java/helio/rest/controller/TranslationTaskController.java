package helio.rest.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.http.HttpHeaders;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.eclipse.jetty.http.MimeTypes;
import spark.Request;
import helio.Helio;
import helio.blueprints.TranslationUnit;
import helio.blueprints.exceptions.HelioExecutionException;
import helio.blueprints.exceptions.IncompatibleMappingException;
import helio.blueprints.exceptions.TranslationUnitExecutionException;
import helio.rest.HelioRest;
import helio.rest.HelioService;
import helio.rest.JSONLD11;
import helio.rest.exception.InternalServiceException;
import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
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
		HelioTranslationTask task = HelioTaskService.getTranslationTask(id);
		task.asemble();
			
		try {
			return task.getUnits().parallelStream().map(uniT -> runUnit(uniT)).collect(Collectors.joining());
		
		//data.parallelStream().forEach(elem -> str.append(elem));
		//String mappingBuilder = task.getMappingProcessor();
		//if(mappingBuilder!=null && (mappingBuilder.equals(HelioRest.DEFAULT_MAPPING_PROCESSOR)))
		//response.header(HttpHeaders.CONTENT_TYPE, MimeTypes.Type.APPLICATION_JSON_UTF_8.asString());
		}catch(Exception e) {
			throw new InternalServiceException(e.toString());
		}
		//return str.toString();
	};
	
	public static String runUnit(TranslationUnit unit)  {
		String result =  "";
		try {
		Future<?> f = service.submit(unit.getTask());
		f.get();
		result = unit.getDataTranslated().get(0);
		
		f.cancel(true);
		}catch(Exception e) {
			e.printStackTrace();
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
