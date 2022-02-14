package helio.rest.exception;

import org.apache.http.protocol.HTTP;

import helio.rest.model.ApiError;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class RepositoryException extends RuntimeException {

	private static final long serialVersionUID = -7982702000551927171L;

	public RepositoryException(String msg) {
		super(msg);
	}


	@SuppressWarnings("rawtypes")
	public static final ExceptionHandler handle = (Exception exception, Request request, Response response) -> {
		response.status(500);
		response.header(HTTP.CONTENT_TYPE, "application/json");
		ApiError error = new ApiError(500, exception.toString());
		response.body(error.toString());
	};

}
