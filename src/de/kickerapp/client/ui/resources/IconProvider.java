package de.kickerapp.client.ui.resources;

import com.google.gwt.core.client.GWT;

import de.kickerapp.client.ui.resources.icons.KickerIcons;

/**
 * Klasse für den komfortableren Zugriff auf die Icons.
 * 
 * @author Sebastian Filke
 */
public final class IconProvider {

	/** Die Icons der Applikation. */
	private final KickerIcons icons;

	/**
	 * Klasse zum Halten der Instanz von {@link IconProvider}.
	 * 
	 * @author Sebastian Filke
	 */
	private static class LazyHolder {

		/** Die Instanz der Klasse {@link IconProvider}. */
		private static final IconProvider INSTANCE = new IconProvider();
	}

	/**
	 * Privater Konstruktor soll nicht von außen aufgerufen werden.
	 */
	private IconProvider() {
		icons = GWT.create(KickerIcons.class);
	}

	/**
	 * Statische Methode <code>getInstance()</code> liefert die einzige Instanz der {@link IconProvider}-Klasse.
	 * 
	 * @return Die einzige Instanz der {@link IconProvider}-Klasse.
	 */
	private static IconProvider getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * Liefert die Icons der Applikation.
	 * 
	 * @return Die Icons der Applikation.
	 */
	public static KickerIcons get() {
		return getInstance().icons;
	}

}
