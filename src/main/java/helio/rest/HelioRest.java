package helio.rest;


import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.delete;

import java.util.Arrays;
import java.util.List;

import static spark.Spark.get;
import helio.rest.controller.ComponentController;
import helio.rest.controller.TranslationTaskController;
import helio.rest.exception.Exceptions;
import helio.rest.exception.InternalServiceException;
import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.repository.HibernateUtil;
import helio.rest.spark.CorsFilter;
import helio.rest.spark.OptionsController;


public class HelioRest {

	// -- Attributes
	public static String DEFAULT_MAPPING_PROCESSOR = "SIoTRxBuilder";
	public static String DEFAULT_COMPONENTS = "./default-components.json";
	public static String DEFAULT_PERSISTENCE = "./rest-db";
	
	// -- Main

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		System.out.println(LOGO);
		 // Configuration
		configure(args);
		
		// List routes
        before(new CorsFilter());
        new OptionsController();
        
        path("/api", () -> {
        	// translation tasks CRUD
	        get("/", TranslationTaskController.list); // DONE
	        get("", TranslationTaskController.list);  // DONE
	        
	        
	        get("/:id", TranslationTaskController.get);
	        post("/:id", TranslationTaskController.createUpdate);  // DONE
	        delete("/:id", TranslationTaskController.delete);
	        // DONE: use put for changing the configuration

	        get("/:id/mapping", TranslationTaskController.getMapping);
	        get("/:id/data", TranslationTaskController.getTranslationData);
	        
	       
	        //TODO: meter una query federada para hablar de los horizontal services
	        // el Knowledge graph service te da la vista interoperable de tus datos
	       
	    });

       // post("/sparql", TranslationTaskController.queryData);
        
        path("/component", () -> {
        	get("/", ComponentController.list);
	        get("", ComponentController.list);
	        delete("", ComponentController.restore);
	        post("", ComponentController.create);
	        post("/", ComponentController.create);
	        get("/:id", ComponentController.get);
	        delete("/:id", ComponentController.delete);
        });
       
        // Exceptions
        exception(InternalServiceException.class, InternalServiceException.handle);
        exception(ResourceNotPresentException.class, ResourceNotPresentException.handle);
        exception(InvalidRequestException.class, InvalidRequestException.handle);
        exception(Exception.class, Exceptions.handleException);


	}

	private static final String PORT = "--port=";
	private static final String PERSISTENCE = "--persistence=";
	private static final String COMPONENTS_DEFAULT = "--components=";
	private static final String BUILDER_DEFAULT = "--default_builder=";
	
	
	private static void configure(String[] args) {
	
		List<String> arguments = Arrays.asList(args);
		for(String arg: arguments) {
			if(arg.startsWith(PORT)) {
				port(Integer.valueOf(arg.replace(PORT, "")));
			}else if(arg.startsWith(PERSISTENCE)) {
				HelioRest.DEFAULT_PERSISTENCE = arg.replace(PERSISTENCE, "");
			}else if(arg.startsWith(COMPONENTS_DEFAULT)) {
				HelioRest.DEFAULT_COMPONENTS = arg.replace(COMPONENTS_DEFAULT, "");
			}else if(arg.startsWith(BUILDER_DEFAULT)) {
				HelioRest.DEFAULT_MAPPING_PROCESSOR = arg.replace(BUILDER_DEFAULT, "");
			}
			
		}
		// Init Db
		HibernateUtil.getSessionFactory();
		// The following methods need to be ran in the order
        // they are currently written
        HelioService.loadDefaultComponents();
        HelioService.registerComponents();
        HelioService.initTasks();
	}
	
	private static final String LOGO = ""
			+ "██╗  ██╗███████╗██╗     ██╗ ██████╗ \n"
			+ "██║  ██║██╔════╝██║     ██║██╔═══██╗\n"
			+ "███████║█████╗  ██║     ██║██║   ██║\n"
			+ "██╔══██║██╔══╝  ██║     ██║██║   ██║\n"
			+ "██║  ██║███████╗███████╗██║╚██████╔╝\n"
			+ "╚═╝  ╚═╝╚══════╝╚══════╝╚═╝ ╚═════╝ \n"
			+ "                                    \n"
			+ "██████╗ ███████╗███████╗████████╗   \n"
			+ "██╔══██╗██╔════╝██╔════╝╚══██╔══╝   \n"
			+ "██████╔╝█████╗  ███████╗   ██║      \n"
			+ "██╔══██╗██╔══╝  ╚════██║   ██║      \n"
			+ "██║  ██║███████╗███████║   ██║      \n"
			+ "╚═╝  ╚═╝╚══════╝╚══════╝   ╚═╝      \n"
			+ "				v0.4.9* \n";

}
