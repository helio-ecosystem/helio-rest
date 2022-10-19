package helio.rest.repository;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import helio.rest.HelioRest;
import helio.rest.model.HelioComponent;
import helio.rest.model.HelioTranslationTask;

public class HibernateUtil {
	private static StandardServiceRegistry registry;
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();

				// Hibernate settings equivalent to hibernate.cfg.xml's properties
				Properties settings = new Properties();
				settings.put(Environment.DRIVER, "org.h2.Driver");
				settings.put(Environment.URL, "jdbc:h2:file:" + HelioRest.DEFAULT_PERSISTENCE);
				settings.put(Environment.USER, "sa");
				settings.put(Environment.PASS, "");
				settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");

				settings.put(Environment.SHOW_SQL, "false");

				settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

				settings.put(Environment.HBM2DDL_AUTO, "update");
				// settings.put("hbm2ddl.auto", "update");

				configuration.setProperties(settings);
				configuration.addAnnotatedClass(HelioComponent.class);
				configuration.addAnnotatedClass(HelioTranslationTask.class);

				registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();

				// Create SessionFactory
				sessionFactory = configuration.buildSessionFactory(registry);

			} catch (Exception e) {
				e.printStackTrace();
				if (registry != null) {
					StandardServiceRegistryBuilder.destroy(registry);
				}
			}
		}
		return sessionFactory;
	}

	protected static void shutdown() {
		if (registry != null) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}
}