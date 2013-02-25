package de.kickerapp.server.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

/**
 * Singeltonklasse zum Halten der Instanz des PersistenceManager.
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
	public static <T extends Serializable> List<T> getList(Class<T> clazz) {
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

	public static <T extends Serializable> T getObjectById(Class<T> clazz, Long id) {
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
	 * @param object
	 * @return
	 */
	public static <T extends Serializable> T persistObject(T object) {
		final PersistenceManager pm = get().getPersistenceManager();
		final Transaction txn = pm.currentTransaction();
		try {
			txn.begin();
			object = pm.makePersistent(object);
			object = pm.detachCopy(object);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
			pm.close();
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> int getNextId(Class<T> clazz) {
		final PersistenceManager pm = get().getPersistenceManager();

		final Query query = pm.newQuery(clazz);
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
