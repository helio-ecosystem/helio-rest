package helio.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import helio.Helio;
import helio.blueprints.Component;
import helio.blueprints.Components;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.rest.exception.InternalServiceException;
import helio.rest.model.HelioComponent;
import helio.rest.service.HelioComponentService;
import helio.rest.service.HelioTaskService;

public class RestUtils {

	// -- Serialization methods

	public static final ObjectMapper MAPPER = new ObjectMapper();

	public static final String toJson(Object o) {
		try {
			return MAPPER.writeValueAsString(o);
		}catch(JsonProcessingException e) {
			throw new InternalServiceException( e.toString());
		}
	}

	public static final Object fromJson(String str, Class<?> clazz) throws IOException {
		try {
			return MAPPER.readValue(str.getBytes(), clazz);
		}catch(JsonProcessingException e) {
			throw new InternalServiceException( e.toString());
		}
	}

	/*public static final GsonBuilder GSON_BUILDER = new GsonBuilder();
	public static final String toJson(Object o) {
		return RestUtils.GSON_BUILDER.excludeFieldsWithoutExposeAnnotation().create().toJsonTree(o).toString();
	}

	public static final Gson getGSON() {
		return RestUtils.GSON_BUILDER.excludeFieldsWithoutExposeAnnotation().create();
	}*/

	// -- translation tasks methods

	public static Map<String, Helio> currentTasks = new HashMap<>();
	public static void initTasks(){
		HelioTaskService.listHelio().parallelStream().forEach(hTask -> {
			try {
				if(hTask.getMappingContent()!=null && !hTask.getMappingContent().isBlank() && hTask.isActive() )
					currentTasks.put(hTask.getId(), hTask.asemble());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} );
	}

	// -- default components methods
	public static String defaultComponentsFile = "./default-components.json";
	public static void loadDefaultComponents() {
			List<HelioComponent> hComponents = HelioComponentService.list();
			List<Component> components = readDefaultComponents();
			for (Component component : components) {
				Optional<HelioComponent> found =  hComponents.parallelStream()
						.filter(hcmp -> hcmp.equivalent(component)).findFirst();
				if(!found.isPresent()) {
					HelioComponentService.add(new HelioComponent(component));
				}
			}
	}

	private static List<Component> readDefaultComponents() {
		try {
			String content = Files.readString(Paths.get(defaultComponentsFile));
			return MAPPER.readValue(content, new TypeReference<List<Component>>(){});
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;

	}


	protected static void registerComponents() {
		HelioComponentService.list().parallelStream().forEach(helioComponent -> {
			try {
				Components.registerAndLoad(helioComponent.getComponent());
			} catch (ExtensionNotFoundException e) {
				System.out.println(e.toString());
			}
		});
	}

}
