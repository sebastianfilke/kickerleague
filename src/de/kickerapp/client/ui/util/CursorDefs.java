package de.kickerapp.client.ui.util;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Konstantendefinitionen zu Mauszeiger im Clienten.
 * 
 * @author Sebastian Filke
 */
public final class CursorDefs {

	/**
	 * Privater Konstruktor soll nicht von außen aufgerufen werden.
	 */
	private CursorDefs() {
	}

	/** Konstante für den <tt>Standard</tt>-Mauszeiger. */
	public static void showDefaultCursor() {
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
	}

	/** Konstante für den <tt>Warten</tt>-Mauszeiger. */
	public static void showWaitCursor() {
		DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
	}

}
