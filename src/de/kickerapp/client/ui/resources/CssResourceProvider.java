package de.kickerapp.client.ui.resources;

import com.google.gwt.core.client.GWT;

import de.kickerapp.client.ui.resources.css.KickerCssResources;

/**
 * Klasse für den komfortableren Zugriff auf die Styles der Vorlagen.
 * 
 * @author Sebastian Filke
 */
public final class CssResourceProvider {

	/** Die Styles der Applikation. */
	private final KickerCssResources resources;

	/**
	 * Klasse zum Halten der Instanz von {@link CssResourceProvider}.
	 * 
	 * @author Sebastian Filke
	 */
	private static class LazyHolder {

		/** Die Instanz der Klasse {@link CssResourceProvider}. */
		private static final CssResourceProvider INSTANCE = new CssResourceProvider();
	}

	/**
	 * Privater Konstruktor soll nicht von außen aufgerufen werden.
	 */
	private CssResourceProvider() {
		resources = GWT.create(KickerCssResources.class);
		resources.pagingStyle().ensureInjected();
		resources.opponentStyle().ensureInjected();
	}

	/**
	 * Statische Methode <code>getInstance()</code> liefert die einzige Instanz der {@link CssResourceProvider}-Klasse.
	 * 
	 * @return Die einzige Instanz der {@link CssResourceProvider}-Klasse.
	 */
	private static CssResourceProvider getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * Liefert die Styles der Applikation.
	 * 
	 * @return Die Styles der Applikation.
	 */
	public static KickerCssResources get() {
		return getInstance().resources;
	}

}
