package helio.rest.exception;

import org.apache.http.protocol.HTTP;

import spark.ExceptionHandler;
import spark.Request;
import helio.rest.model.ApiError;
import spark.Response;

public class InvalidRequestException extends RuntimeException {

	private static final long serialVersionUID = -7982702000551927171L;

	public InvalidRequestException(String msg) {
		super(msg);
	}


	@SuppressWarnings("rawtypes")
	public static final ExceptionHandler handle = (Exception exception, Request request, Response response) -> {
		response.status(400);
		response.header(HTTP.CONTENT_TYPE, "application/json");
		ApiError error = new ApiError(400, exception.toString());
		response.body(error.toString());
	};

}
