package helio.rest;


import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.delete;

import static spark.Spark.put;
import static spark.Spark.get;
import helio.rest.controller.ComponentController;
import helio.rest.controller.ConfigurationController;
import helio.rest.controller.HelioMappingController;
import helio.rest.controller.TranslationTaskController;
import helio.rest.exception.Exceptions;
import helio.rest.exception.InternalServiceException;
import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.ServiceConfiguration;
import helio.rest.service.HelioConfigurationService;
import helio.rest.spark.CorsFilter;
import helio.rest.spark.OptionsController;


public class HelioRest {

	// -- Attributes
	public static ServiceConfiguration serviceConfiguration =null;
	public static final String DEFAULT_MAPPING_PROCESSOR = "JLD11Builder";
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
        
        path("/api", () -> {
        	// translation tasks CRUD
	        get("/", TranslationTaskController.list); // DONE
	        get("", TranslationTaskController.list);  // DONE
	        
	        
	        // TODO: check if providing a jsonld mapping with the other builder it trhows error
	        get("/:id", TranslationTaskController.get);
	        post("/:id", TranslationTaskController.createUpdate);  // DONE
	        delete("/:id", TranslationTaskController.delete);
	        // DONE: use put for changing the configuration

	        get("/:id/mapping", TranslationTaskController.getMapping);
	        get("/:id/data", TranslationTaskController.getTranslationData);
	        
	        
	       
	        // TODO: ? post("/sparql", DataGenerationController.getData);
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
//        post("/test", new RoutePostComponent());
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
