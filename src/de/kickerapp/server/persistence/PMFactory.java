package de.kickerapp.server.persistence;

import java.io.Serializable;
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

	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T getObjectById(Class<T> clazz, Long id) {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		T object = (T) pm.getObjectById(clazz, id);
		object = pm.detachCopy(object);

		return object;
	}

	/**
	 * @param object
	 * @return
	 */
	public static <T extends Serializable> T persistObject(T object) {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();
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
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final Query query = pm.newQuery(clazz);
		final List<T> dbObject = (List<T>) query.execute();

		int id = dbObject.size();
		id++;

		return id;
	}

}
