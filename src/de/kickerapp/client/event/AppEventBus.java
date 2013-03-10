package de.kickerapp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Klasse zum zentralen Zugriff auf den Eventbus Handlermanager.
 * 
 * @author Sebastian Filke
 */
public final class AppEventBus {

	/** Die einzige Instanz der {@link AppEventBus}. */
	private static final AppEventBus INSTANCE = new AppEventBus();

	/** Der HandlerManager. */
	private HandlerManager eventBus;

	/**
	 * Privater Konstruktor zur Verhinderung externe Instantiierung.
	 */
	private AppEventBus() {
		eventBus = new HandlerManager(this);
	}

	/**
	 * Liefert die einzige Instanz des {@link AppEventBus}.
	 * 
	 * @return Die Instanz.
	 */
	private static AppEventBus get() {
		return INSTANCE;
	}

	/**
	 * Liefert den Handlermanager (EventBus).
	 * 
	 * @return Den Handlermanager.
	 */
	private HandlerManager getEventBus() {
		return eventBus;
	}

	/**
	 * Statische Methode <code>fireEvent()</code> löst das angegebene Ereignis aus.
	 * 
	 * @param event Das Ereignis.
	 */
	public static void fireEvent(GwtEvent<?> event) {
		get().getEventBus().fireEvent(event);
	}

	/**
	 * Fügt einen Handler hinzu.
	 * 
	 * @param <H> Der Typ des Handlers.
	 * @param type Der Ereignistyp der mit diesem Handler verbunden ist.
	 * @param handler Der Handler
	 * @return Die Registrierung des Handlers, die gespeichert werden kann um den Handler später wieder entfernen zu können.
	 */
	public static <H extends EventHandler> HandlerRegistration addHandler(GwtEvent.Type<H> type, final H handler) {
		return get().getEventBus().addHandler(type, handler);
	}

}
