package de.kickerapp.server.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

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
	 * @param <T> Der Typ der Klasse.
	 * @return Die Instanzen.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> List<T> getList(final Class<T> clazz) {
		final PersistenceManager pm = get().getPersistenceManager();

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
	 * Liefert die Instanz für die übergebene Klasse.
	 * 
	 * @param clazz Die Klasse für welche die Instanz gesucht werden sollen.
	 * @param id Die Db-Id der Instanz.
	 * @param <T> Der Typ der Klasse.
	 * @return Die Instanz.
	 */
	public static <T extends Serializable> T getObjectById(final Class<T> clazz, final Long id) {
		final PersistenceManager pm = get().getPersistenceManager();

		T object = null;
		try {
			object = (T) pm.getObjectById(clazz, id);
			object = pm.detachCopy(object);
		} finally {
			pm.close();
		}
		return object;
	}

	/**
	 * Speichert die Instanz.
	 * 
	 * @param object Das Db-Objekt zum Speichern.
	 * @param <T> Der Typ der Klasse.
	 * @return Das gespeicherte Db-Objekt.
	 */
	public static <T extends Serializable> T persistObject(final T object) {
		final PersistenceManager pm = get().getPersistenceManager();
		final Transaction txn = pm.currentTransaction();

		T result;
		try {
			txn.begin();
			result = pm.makePersistent(object);
			result = pm.detachCopy(result);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
			pm.close();
		}
		return result;
	}

	/**
	 * Speichert alle Instanzen.
	 * 
	 * @param objects Die Db-Objekte zum Speichern.
	 * @param <T> Die Typen der Klassen.
	 * @return Die gespeicherten Db-Objekte.
	 */
	@SafeVarargs
	public static <T extends Serializable> Object[] persistAllObjects(final T... objects) {
		final PersistenceManager pm = get().getPersistenceManager();
		final Transaction txn = pm.currentTransaction();

		Object[] result;
		try {
			txn.begin();
			result = pm.makePersistentAll(objects);
			result = pm.detachCopyAll(result);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
			pm.close();
		}
		return result;
	}

	/**
	 * Speichert alle Instanzen.
	 * 
	 * @param objects Die Db-Objekte zum Speichern.
	 * @param <T> Der Typ der Klassen.
	 * @return Die gespeicherten Db-Objekte.
	 */
	public static <T extends Serializable> List<T> persistList(final Collection<T> objects) {
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
	 * Liefert die nächste Db-Id für die übergebene Klasse.
	 * 
	 * @param clazzName Der Name der Klasse für welche die nächste Db-Id geliefert werden soll.
	 * @param <T> Der Typ der Klasse.
	 * @return Die nächste Db-Id.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> int getNextId(final String clazzName) {
		final PersistenceManager pm = get().getPersistenceManager();

		final Query query = pm.newQuery("select key from " + clazzName);
		int id = 0;
		try {
			final List<T> dbObject = (List<T>) query.execute();
			id = dbObject.size();
			id++;
		} finally {
			query.closeAll();
			pm.close();
		}
		return id;
	}

}
