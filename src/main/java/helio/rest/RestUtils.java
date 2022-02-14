package helio.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RestUtils {

	private static final GsonBuilder GSON_BUILDER = new GsonBuilder();
	public static final String toJson(Object o) {
		return RestUtils.GSON_BUILDER.excludeFieldsWithoutExposeAnnotation().create().toJsonTree(o).toString();
	}

	public static final Gson getGSON() {
		return RestUtils.GSON_BUILDER.excludeFieldsWithoutExposeAnnotation().create();
	}
}
