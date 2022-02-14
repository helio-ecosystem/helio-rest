package helio.rest.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import helio.rest.exception.RepositoryException;

public class Repository<T> {

	private Class<T> innerClass;
	private String queryRetrieve;
	private String queryList;
	private String queryDelete;
	private String queryExists;
	
	public Repository(Class<T> clazz) {
		innerClass = clazz;
		queryRetrieve = concat("FROM ",innerClass.getSimpleName(), " t WHERE t.id = :id");
		queryList = concat("FROM ",innerClass.getSimpleName());
		queryDelete = concat("DELETE ",innerClass.getSimpleName(), " t WHERE t.id = :id");
		
		queryExists = concat("SELECT count(*) FROM ",innerClass.getSimpleName(), " t WHERE t.id = :id");
	}

	public void persist(T object) {
		Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            session.save(object);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RepositoryException(e.toString());
        }
	}

	public boolean exists(String id) {
		try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
			session.beginTransaction();
			return session.createQuery(this.queryExists, Long.class).setParameter("id", id).uniqueResult() > 0;
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new RepositoryException(e.toString());
        }
	}
	
	
	public Optional<T> retrieve(String id) {
		try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
			session.beginTransaction();
			return session.createQuery(this.queryRetrieve, innerClass).setParameter("id", id).uniqueResultOptional();
        } catch (Exception e) {
        	throw new RepositoryException(e.toString());
        }
	}

	public List<T> retrieve() {
		try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
			session.beginTransaction();
			return session.createQuery(queryList, innerClass).list();
        } catch (Exception e) {
        	throw new RepositoryException(e.toString());
        }
	}

	public void delete(String id) {
		try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
			session.beginTransaction();
			session.createQuery(queryDelete).setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			throw new RepositoryException(e.toString());
        }
	}

	private String concat(String ...strings ) {
		StringBuilder br = new StringBuilder();
		for (String string : strings)
			br.append(string);
		return br.toString();
	}
}
