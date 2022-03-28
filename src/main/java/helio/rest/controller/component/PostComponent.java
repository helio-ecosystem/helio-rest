package helio.rest.controller.component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;

import helio.blueprints.components.AsyncDataProvider;
import helio.blueprints.objects.TranslationUnit;

public class PostComponent implements AsyncDataProvider  {

	private static final long serialVersionUID = -2102725741416434068L;

	protected TranslationUnit unit;
	public static Map<String, String> routes = new HashMap<>();
	
	
	@Override
	public void configure(JsonObject configuration) {
		// empty
		
	}

	@Override
	public InputStream getData() {
		while(true) {
			while(!routes.isEmpty()) {
				for(Entry<String,String> entry : routes.entrySet()) {
					unit.translate(new ByteArrayInputStream(entry.getValue().getBytes()));
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setTranslationUnit(TranslationUnit translationUnit) {
		this.unit = translationUnit;
	}

}
