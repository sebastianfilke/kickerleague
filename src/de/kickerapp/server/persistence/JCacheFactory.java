package de.kickerapp.server.persistence;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;

import de.kickerapp.client.exception.AppExceptionHandler;

/**
 * Singeltonklasse zum Halten der Instanz des {@link Cache}s.
 * 
 * @author Sebastian Filke
 */
public final class JCacheFactory {

	/** Die Liste der paginierten Spieler. */
	public static final String PAGEDPLAYERS = "de.kickerapp.server.services.PagingServiceImpl";

	/** Der Logger der Klasse. */
	private static final transient Logger LOGGER = Logger.getLogger(AppExceptionHandler.class.getName());

	/** Die einzige Instanz des {@link Cache}. */
	private Cache instance;

	/**
	 * Privater Konstruktor zur Verhinderung externe Instantiierung.
	 */
	private JCacheFactory() {
		final HashMap<String, Integer> props = new HashMap<String, Integer>();
		props.put(GCacheFactory.EXPIRATION_DELTA, 1800);

		try {
			final CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
			instance = cacheFactory.createCache(props);
		} catch (CacheException ce) {
			LOGGER.log(Level.SEVERE, ce.getMessage(), ce);
		}
	}

	/**
	 * Klasse zum Halten der Instanz von {@link JCacheFactory}.
	 * 
	 * @author Sebastian Filke
	 */
	private static class LazyHolder {

		/** Die Instanz der Klasse {@link JCacheFactory}. */
		private static final JCacheFactory INSTANCE = new JCacheFactory();
	}

	/**
	 * Statische Methode <code>getInstance()</code> liefert die einzige Instanz der {@link JCacheFactory}-Klasse.
	 * 
	 * @return Die einzige Instanz der {@link JCacheFactory}-Klasse.
	 */
	private static JCacheFactory getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * Liefert die einzige Instanz des {@link Cache}.
	 * 
	 * @return Die Instanz.
	 */
	public static Cache get() {
		return getInstance().instance;
	}

}
