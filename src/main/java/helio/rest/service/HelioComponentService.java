package helio.rest.service;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import helio.blueprints.Component;
import helio.blueprints.Components;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.rest.exception.InternalServiceException;
import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.HelioComponent;
import helio.rest.repository.Repository;

public class HelioComponentService {

	private static final Repository<HelioComponent> repository = new Repository<>(HelioComponent.class);

	public static boolean isStored(HelioComponent component) {
		try {
			return repository.exists(component.getId());
		}catch(InternalServiceException e) {
			return false;
		}
	}
	
	public static Component add(Component component) {
		HelioComponent cmp  = new HelioComponent(component);
		return add( cmp);
	}
	
	public static Component add(HelioComponent component) {
		boolean exists = repository.exists(component.getId());
		Component rawComponent = null;
		if (exists)
			throw new InvalidRequestException("The component provided already already exists");
		try {
			// Register into Helio
			rawComponent = component.getComponent();
			Components.registerAndLoad(rawComponent);
			repository.persist(component);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotPresentException(e.toString());
		}
		return rawComponent;
	}

	public static HelioComponent get(String id) {
		Optional<HelioComponent> opt = repository.retrieve(id);
		if (!opt.isPresent())
			throw new ResourceNotPresentException("Requested Component was not found");

		return opt.get();
	}

	public static List<HelioComponent> list() {
		return repository.retrieve();
	}

	public static void delete(String id) {
		HelioComponent hComponent = get(id); // throw exception if not found
		repository.delete(hComponent.getId());
		try {
			Components.getRegistered().remove(hComponent.getComponent());
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}
}
