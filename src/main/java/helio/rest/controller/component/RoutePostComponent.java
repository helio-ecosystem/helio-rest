package helio.rest.controller.component;

import java.io.ByteArrayInputStream;

import spark.Request;
import spark.Response;
import spark.Route;

public class RoutePostComponent extends PostComponent implements Route {

	private static final long serialVersionUID = -1795858893659813221L;

	@Override
	public Object handle(Request request, Response response) throws Exception {
		String body = request.body();
		routes.put("/test", body) ;
		response.status(204);
		return "";
	}

}
