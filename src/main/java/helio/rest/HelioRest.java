package helio.rest;


import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

import helio.rest.controller.HelioComponentController;
import helio.rest.controller.HelioConfigurationController;
import helio.rest.controller.HelioDataController;
import helio.rest.controller.HelioMappingController;
import helio.rest.controller.HelioTaskController;
import helio.rest.exception.Exceptions;
import helio.rest.exception.InternalServiceException;
import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.configuration.HelioRestConfiguration;
import helio.rest.service.HelioConfigurationService;
import helio.rest.spark.CorsFilter;
import helio.rest.spark.OptionsController;


public class HelioRest {


	public static final String APP_PACKAGE = "helio.rest";

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		 // Configuration
		configure();
		// The following methods need to be ran in the order
        // they are currently written
        RestUtils.loadDefaultComponents();
        RestUtils.registerComponents();
        RestUtils.initTasks();

		// List routes
        before(new CorsFilter());
        new OptionsController();

        path("/helio", () -> {
        	// translation tasks CRUD
	        get("/", HelioTaskController.list);
	        get("", HelioTaskController.list);
	        post("", HelioTaskController.create);
	        post("/", HelioTaskController.create);
	        get("/:id", HelioTaskController.get);
	        delete("/:id", HelioTaskController.delete);
	        // Mapping CRUD
	        get("/:id/mapping", HelioMappingController.getMapping);
	        post("/:id/mapping", HelioMappingController.addUpdateMapping);
	        delete("/:id/mapping", HelioMappingController.removeMapping);
	        // Data generation (synchronous) & data associated retrieval
	        put("", HelioDataController.runTasks);
	        //post("/data", HelioDataController.getData);

	        // TODO:  post("/:id/generate", DataGenerationController.generateData);
	       // TODO: post("/:id/data", DataGenerationController.getData);
	       // TODO: ? post("/sparql", DataGenerationController.getData);


        });

        path("/component", () -> {
        	get("/", HelioComponentController.list);
	        get("", HelioComponentController.list);
	        delete("", HelioComponentController.restore);
	        post("", HelioComponentController.create);
	        post("/", HelioComponentController.create);
	        get("/:id", HelioComponentController.get);
	        delete("/:id", HelioComponentController.delete);
        });

        path("/configuration", () -> {
	         get("/", HelioConfigurationController.getSingleton);
	         get("", HelioConfigurationController.getSingleton);
		     post("", HelioConfigurationController.updateSingleton);
		     post("/", HelioConfigurationController.updateSingleton);
		     put("", HelioConfigurationController.restoreSingleton);
		     put("/", HelioConfigurationController.restoreSingleton);
	       });

        // Exceptions
        exception(InternalServiceException.class, InternalServiceException.handle);
        exception(ResourceNotPresentException.class, ResourceNotPresentException.handle);
        exception(InvalidRequestException.class, InvalidRequestException.handle);
        exception(Exception.class, Exceptions.handleException);




	}

	private static void configure() {
		HelioRestConfiguration configuration = HelioConfigurationService.createDefault(false);
		port(configuration.getPort());
		// TODO:ADD OTHER PARAMETERS
	}
}
