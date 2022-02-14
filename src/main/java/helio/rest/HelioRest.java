package helio.rest;


import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.delete;

import helio.rest.controller.HelioTaskController;
import helio.rest.controller.MappingController;
import helio.rest.exception.Exceptions;
import helio.rest.exception.RepositoryException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.spark.CorsFilter;
import helio.rest.spark.OptionsController;


public class HelioRest {


	public static final String APP_PACKAGE = "helio.rest";

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// Configure Spark
        port(4567);

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
	        get("/:id/mapping", MappingController.getMapping);
	        post("/:id/mapping", MappingController.addUpdateMapping);
	        delete("/:id/mapping", MappingController.removeMapping);
//	        // Data generation (synchronous) & data associated retrieval
//	        post("/generate", DataGenerationController.generateData);
//	        post("/data", DataGenerationController.getData);
	       // TODO:  post("/:id/generate", DataGenerationController.generateData);
	       // TODO: post("/:id/data", DataGenerationController.getData);
	       // TODO: ? post("/sparql", DataGenerationController.getData);
        });

       // get("/helio", HelioController.generateSynchronous);*/

        // Exceptions
        exception(RepositoryException.class, RepositoryException.handle);
        exception(ResourceNotPresentException.class, ResourceNotPresentException.handle);
        exception(IllegalArgumentException.class, Exceptions.handleIllegalArgumentException);
        exception(Exception.class, Exceptions.handleException);

      
	}
}
