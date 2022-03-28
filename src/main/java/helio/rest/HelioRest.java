package helio.rest;


import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

import helio.rest.controller.ComponentController;
import helio.rest.controller.ConfigurationController;
import helio.rest.controller.HelioDataController;
import helio.rest.controller.HelioMappingController;
import helio.rest.controller.TranslationTaskController;
import helio.rest.controller.component.RoutePostComponent;
import helio.rest.exception.Exceptions;
import helio.rest.exception.InternalServiceException;
import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.configuration.ServiceConfiguration;
import helio.rest.service.HelioConfigurationService;
import helio.rest.spark.CorsFilter;
import helio.rest.spark.OptionsController;


public class HelioRest {

	// -- Attributes
	public static ServiceConfiguration serviceConfiguration =null;
	public static final String DEFAULT_MAPPING_PROCESSOR = "JMappingProcessor";
	// -- Main

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		 // Configuration
		configure();
		// The following methods need to be ran in the order
        // they are currently written
        HelioService.loadDefaultComponents();
        HelioService.registerComponents();
        HelioService.initTasks();

		// List routes
        before(new CorsFilter());
        new OptionsController();
        
        path("/helio", () -> {
        	// translation tasks CRUD
	        get("/", TranslationTaskController.list);
	        get("", TranslationTaskController.list);
	        post("", TranslationTaskController.create);
	        post("/", TranslationTaskController.create);
	        get("/:id", TranslationTaskController.get);
	        delete("/:id", TranslationTaskController.delete);
	        // Mapping CRUD
	        get("/:id/mapping", HelioMappingController.getMapping);
	        post("/:id/mapping", HelioMappingController.addUpdateMapping);
	        delete("/:id/mapping", HelioMappingController.removeMapping);
	        // Data generation (synchronous) & data associated retrieval
	        put("", HelioDataController.runTasks);
	        put(":/id", HelioDataController.runTask);
	        //TODO:  put("/:id/:unitId", HelioDataController.runTaskUnit);
	        
	        
	        //post("/data", HelioDataController.getData);

	        
	        // TODO: ? post("/sparql", DataGenerationController.getData);
	        
	        // TODO: add endpoint for reading resources, add @context to translation task
	        // TODO: if the context is present and not mime, then json-ld 1.1
        });

        path("/component", () -> {
        	get("/", ComponentController.list);
	        get("", ComponentController.list);
	        delete("", ComponentController.restore);
	        post("", ComponentController.create);
	        post("/", ComponentController.create);
	        get("/:id", ComponentController.get);
	        delete("/:id", ComponentController.delete);
        });
        get("/processors", HelioMappingController.listProcessors);

        path("/configuration", () -> {
	         get("/", ConfigurationController.getSingleton);
	         get("", ConfigurationController.getSingleton);
		     post("", ConfigurationController.updateSingleton);
		     post("/", ConfigurationController.updateSingleton);
		     put("", ConfigurationController.restoreSingleton);
		     put("/", ConfigurationController.restoreSingleton);
	       });
        post("/test", new RoutePostComponent());
        // Exceptions
        exception(InternalServiceException.class, InternalServiceException.handle);
        exception(ResourceNotPresentException.class, ResourceNotPresentException.handle);
        exception(InvalidRequestException.class, InvalidRequestException.handle);
        exception(Exception.class, Exceptions.handleException);




	}

	private static void configure() {
		if(serviceConfiguration == null)
			serviceConfiguration = HelioConfigurationService.createDefault(false);
		// TODO:ADD OTHER PARAMETERS
		port(serviceConfiguration.getPort());
	}
}
