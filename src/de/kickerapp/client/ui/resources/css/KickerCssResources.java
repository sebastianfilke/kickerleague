package de.kickerapp.client.ui.resources.css;

import com.google.gwt.resources.client.ClientBundle;

/**
 * Die Styles für die Applikation.
 * 
 * @author Sebastian Filke
 */
public interface KickerCssResources extends ClientBundle {

	// CHECKSTYLE:OFF

	@Source("Paging.css")
	PagingStyle pagingStyle();

	@Source("Opponent.css")
	OpponentStyle opponentStyle();

}
