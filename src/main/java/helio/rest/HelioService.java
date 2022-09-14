package helio.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import helio.blueprints.components.Component;
import helio.blueprints.components.Components;
import helio.rest.exception.InternalServiceException;
import helio.rest.model.HelioComponent;
import helio.rest.service.HelioComponentService;
import helio.rest.service.HelioTaskService;

public class HelioService {

	
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
	
	public static final String concat(String ... args) {
		StringBuilder builder = new StringBuilder();
		for(int index=0; index < args.length; index++) {
			builder.append(args[index]);
		}
		return builder.toString();
	}

	// -- Translation tasks methods
	
	public static void initTasks(){
		HelioTaskService.listHelioTasks().stream().forEach(hTask -> {
			try {
				hTask.asemble();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} );
	}

	// -- default components methods
	public static String defaultComponentsFile = "./default-components.json";
	public static void loadDefaultComponents() {
			List<Component> components = readDefaultComponents();
			components.parallelStream()
				.map(cmp -> new HelioComponent(cmp))
				.filter(hCmp -> !HelioComponentService.isStored(hCmp))
				.forEach(hCmp -> HelioComponentService.add(hCmp));
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
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		});
	}

}
