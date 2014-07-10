package de.kickerapp.client.ui.resources;

import com.google.gwt.core.client.GWT;

import de.kickerapp.client.ui.resources.templates.KickerTemplates;

/**
 * Klasse für den komfortableren Zugriff auf die templatebasierten Vorlagen.
 * 
 * @author Sebastian Filke
 */
public final class TemplateProvider {

	/** Die Templates der Applikation. */
	private final KickerTemplates templates;

	/**
	 * Klasse zum Halten der Instanz von {@link TemplateProvider}.
	 * 
	 * @author Sebastian Filke
	 */
	private static class LazyHolder {

		/** Die Instanz der Klasse {@link TemplateProvider}. */
		private static final TemplateProvider INSTANCE = new TemplateProvider();
	}

	/**
	 * Privater Konstruktor soll nicht von außen aufgerufen werden.
	 */
	private TemplateProvider() {
		templates = GWT.create(KickerTemplates.class);
	}

	/**
	 * Statische Methode <code>getInstance()</code> liefert die einzige Instanz der {@link TemplateProvider}-Klasse.
	 * 
	 * @return Die einzige Instanz der {@link TemplateProvider}-Klasse.
	 */
	private static TemplateProvider getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * Liefert die Templates der Applikation.
	 * 
	 * @return Die Templates der Applikation.
	 */
	public static KickerTemplates get() {
		return getInstance().templates;
	}

}
