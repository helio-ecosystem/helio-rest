package helio.rest;


import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.patch;
import static spark.Spark.path;
import static spark.Spark.after;
import static spark.Spark.redirect;

import helio.rest.controller.MappingController;

import static spark.Spark.port;


public class HelioRest {

	public static void main(String[] args) {
		// Configure Spark
        port(4567);
        
        path("/mappings", () -> {
	        get("/", MappingController.listMappings);
	        get("", MappingController.listMappings);
	        get("/:id", MappingController.getMapping);
	        delete("/:id", MappingController.deleteMapping);
	        post("/rml/:id", MappingController.registerRmlMapping);
	        post("/json/:id", MappingController.registerJsonMapping);
	        post("/rml-11/:id", MappingController.registerRml11Mapping);
        });
        
        
        
	}
}
