package helio.rest.exception;

import org.apache.http.protocol.HTTP;

import helio.rest.model.ApiError;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class Exceptions {

	
	
	@SuppressWarnings("rawtypes")
	public static final ExceptionHandler handleException = (Exception exception, Request request, Response response) -> {
		response.status(500);
		response.header(HTTP.CONTENT_TYPE, "application/json");
		ApiError error = new ApiError(500, exception.toString());
		response.body(error.toString());
	};
	
	@SuppressWarnings("rawtypes")
	public static final ExceptionHandler handleIllegalArgumentException = (Exception exception, Request request, Response response) -> {
		response.status(400);
		response.header(HTTP.CONTENT_TYPE, "application/json");
		ApiError error = new ApiError(400, exception.toString());
		response.body(error.toString());
	};
}
