package de.kickerapp.server.persistence;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * Singeltonklasse zum Halten der Instanz des PersistenceManager.
 * 
 * @author Sebastian Filke
 */
public final class PMFactory {

	/** Die einzige Instanz der {@link PersistenceManagerFactory}. */
	private static final PersistenceManagerFactory instance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	/**
	 * Privater Konstruktor zur Verhinderung externen Instantiierung.
	 */
	private PMFactory() {
	}

	/**
	 * Liefert die einzige Instanz der {@link PersistenceManagerFactory}.
	 * 
	 * @return Die Instanz.
	 */
	public static PersistenceManagerFactory get() {
		return instance;
	}

}
