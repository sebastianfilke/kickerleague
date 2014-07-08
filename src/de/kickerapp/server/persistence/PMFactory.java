package de.kickerapp.server.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import de.kickerapp.server.dao.BaseDao;
import de.kickerapp.server.dao.Sequence;
import de.kickerapp.server.persistence.queries.QueryContainer;

/**
 * Singeltonklasse zum Halten der Instanz des {@link PersistenceManager}s.
 * 
 * @author Sebastian Filke
 */
public final class PMFactory {

	/** Die einzige Instanz der {@link PersistenceManagerFactory}. */
	private static final PersistenceManagerFactory INSTANCE = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	/**
	 * Privater Konstruktor zur Verhinderung externe Instantiierung.
	 */
	private PMFactory() {
	}

	/**
	 * Liefert die einzige Instanz der {@link PersistenceManagerFactory}.
	 * 
	 * @return Die Instanz.
	 */
	public static PersistenceManagerFactory get() {
		return INSTANCE;
	}

	/**
	 * Liefert sämtliche Instanzen für die übergebene Klasse.
	 * 
	 * @param clazz Die Klasse für welche die Instanzen gesucht werden sollen.
	 * @param plans Die zusätzlichen Attribute welche gezogen werden sollen.
	 * @param <T> Der Typ der Klasse.
	 * @return Die Instanzen.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseDao> List<T> getList(final Class<T> clazz, final String... plans) {
		final PersistenceManager pm = get().getPersistenceManager();
		for (String plan : plans) {
			pm.getFetchPlan().addGroup(plan);
		}

		final Query query = pm.newQuery(clazz);

		List<T> list = new ArrayList<T>();
		try {
			list = (List<T>) query.execute();
			list = (List<T>) pm.detachCopyAll(list);
		} finally {
			query.closeAll();
			pm.close();
		}
		return list;
	}

	/**
	 * Liefert sämtliche Instanzen für die übergebene Klasse.
	 * 
	 * @param clazz Die Klasse für welche die Instanzen gesucht werden sollen.
	 * @param container Der Container zum Abfragen der Instanzen.
	 * @param <T> Der Typ der Klasse.
	 * @return Die Instanzen.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseDao> List<T> getList(final Class<T> clazz, final QueryContainer container) {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();
		for (String plan : container.getPlans()) {
			pm.getFetchPlan().addGroup(plan);
		}

		final Query query = pm.newQuery(clazz);
		query.setFilter(container.getQuery());
		if (!container.getOrdering().isEmpty()) {
			query.setOrdering(container.getOrdering());
		}

		List<T> list = new ArrayList<T>();
		try {
			if (container.getParameter() != null) {
				list = (List<T>) query.executeWithArray(container.getParameter());
			} else {
				list = (List<T>) query.execute();
			}
			list = (List<T>) pm.detachCopyAll(list);
		} finally {
			query.closeAll();
			pm.close();
		}
		return list;
	}

	/**
	 * Liefert die Instanz für die übergebene Klasse.
	 * 
	 * @param clazz Die Klasse für welche die Instanz gesucht werden sollen.
	 * @param id Die DB-ID der Instanz.
	 * @param plans Die zusätzlichen Attribute welche gezogen werden sollen.
	 * @param <T> Der Typ der Klasse.
	 * @return Die Instanz.
	 */
	public static <T extends BaseDao> T getObjectById(final Class<T> clazz, final Long id, final String... plans) {
		final PersistenceManager pm = get().getPersistenceManager();
		for (String plan : plans) {
			pm.getFetchPlan().addGroup(plan);
		}

		T object = null;
		try {
			object = (T) pm.getObjectById(clazz, id);
			object = (T) pm.detachCopy(object);
		} finally {
			pm.close();
		}
		return object;
	}

	/**
	 * Speichert die Instanz.
	 * 
	 * @param object Das DB-Objekt zum Speichern.
	 * @param <T> Der Typ der Klasse.
	 * @return Das gespeicherte DB-Objekt.
	 */
	public static <T extends BaseDao> T persistObject(final T object) {
		final PersistenceManager pm = get().getPersistenceManager();

		T result;
		try {
			result = pm.makePersistent(object);
			result = pm.detachCopy(object);
		} finally {
			pm.close();
		}
		return result;
	}

	/**
	 * Speichert alle Instanzen.
	 * 
	 * @param objects Die DB-Objekte zum Speichern.
	 * @param <T> Die Typen der Klassen.
	 * @return Die gespeicherten DB-Objekte.
	 */
	@SafeVarargs
	public static <T extends BaseDao> Object[] persistAllObjects(final T... objects) {
		final PersistenceManager pm = get().getPersistenceManager();

		Object[] result;
		try {
			result = pm.makePersistentAll(objects);
			result = pm.detachCopyAll(result);
		} finally {
			pm.close();
		}
		return result;
	}

	/**
	 * Speichert alle Instanzen.
	 * 
	 * @param objects Die DB-Objekte zum Speichern.
	 * @param <T> Der Typ der Klassen.
	 * @return Die gespeicherten DB-Objekte.
	 */
	public static <T extends BaseDao> List<T> persistList(final Collection<T> objects) {
		final PersistenceManager pm = get().getPersistenceManager();

		List<T> result = new ArrayList<T>();
		try {
			result = (List<T>) pm.makePersistentAll(objects);
			result = (List<T>) pm.detachCopyAll(result);
		} finally {
			pm.close();
		}
		return result;
	}

	/**
	 * Liefert die aktuelle DB-ID für die übergebene Klasse.
	 * 
	 * @param clazzName Der Name der Klasse für welche die aktuelle DB-ID geliefert werden soll.
	 * @param <T> Der Typ der Klasse.
	 * @return Die aktuelle DB-ID.
	 */
	public static <T extends BaseDao> int getCurrentId(final String clazzName) {
		final PersistenceManager pm = get().getPersistenceManager();

		final Query query = pm.newQuery(Sequence.class);
		query.setFilter("sequenceName == :clazzName");
		query.setUnique(true);

		int id = 0;
		try {
			final Sequence sequence = (Sequence) query.execute(clazzName);
			if (sequence == null) {
				id = 0;
			} else {
				id = sequence.getSequenceID();
			}
		} finally {
			query.closeAll();
			pm.close();
		}
		return id;
	}

	/**
	 * Liefert die nächste DB-ID für die übergebene Klasse.
	 * 
	 * @param clazzName Der Name der Klasse für welche die nächste DB-ID geliefert werden soll.
	 * @param <T> Der Typ der Klasse.
	 * @return Die nächste DB-ID.
	 */
	public static <T extends BaseDao> int getNextId(final String clazzName) {
		final PersistenceManager pm = get().getPersistenceManager();

		final Query query = pm.newQuery(Sequence.class);
		query.setFilter("sequenceName == :clazzName");
		query.setUnique(true);

		int id = 0;
		try {
			Sequence sequence = (Sequence) query.execute(clazzName);
			if (sequence == null) {
				sequence = new Sequence();
				sequence.setSequenceName(clazzName);
			}
			id = sequence.getSequenceID() + 1;
			sequence.setSequenceID(id);

			pm.makePersistent(sequence);
		} finally {
			query.closeAll();
			pm.close();
		}
		return id;
	}

}
