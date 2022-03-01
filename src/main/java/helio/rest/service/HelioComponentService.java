package helio.rest.service;

import java.util.List;
import java.util.Optional;

import helio.blueprints.Components;
import helio.blueprints.exceptions.ExtensionNotFoundException;
import helio.rest.exception.InvalidRequestException;
import helio.rest.exception.ResourceNotPresentException;
import helio.rest.model.HelioComponent;
import helio.rest.repository.Repository;

public class HelioComponentService {

	private static final Repository<HelioComponent> repository = new Repository<>(HelioComponent.class);

	public static HelioComponent add(HelioComponent component) {
		boolean exists = repository.exists(component.getId());
		if (exists)
			throw new InvalidRequestException("The component provided already already exists");
		try {
			//** Helio **//
			Components.registerAndLoad(component.getComponent());
			// Persistence
			repository.persist(component);
		} catch (ExtensionNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return component;
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
		Components.getRegistered().remove(hComponent.getComponent());

	}
}
