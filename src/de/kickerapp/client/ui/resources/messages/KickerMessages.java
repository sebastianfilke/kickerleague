package de.kickerapp.client.ui.resources.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Die Messages für die Applikation.
 * 
 * @author Sebastian Filke
 */
public interface KickerMessages extends Messages {

	/** Die Messages. */
	public final KickerMessages MESSAGE = GWT.create(KickerMessages.class);

	// CHECKSTYLE:OFF

	@DefaultMessage("Kicker League")
	String applicationTitle();

}