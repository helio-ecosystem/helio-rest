package helio.rest.controller;

import helio.rest.repository.TranslationTaskRepository;
import spark.Request;
import spark.Response;
import spark.Route;

public class DataGenerationController {


	public static final Route generateData = (Request request, Response response) -> {
		return TranslationTaskRepository.retrieve();
	};


	public static final Route getData = (Request request, Response response) -> {
		return TranslationTaskRepository.retrieve();
	};

}
