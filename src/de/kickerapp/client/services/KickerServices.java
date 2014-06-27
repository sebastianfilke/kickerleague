package de.kickerapp.client.services;

import com.google.gwt.core.client.GWT;

/**
 * Die Services für die Applikation.
 * 
 * @author Sebastian Filke
 */
public interface KickerServices {

	/** Die asynchrone Schnittstelle zur Verarbeitung von Diagrammen im Klienten. */
	public final ChartServiceAsync CHART_SERVICE = GWT.create(ChartService.class);
	/** Die asynchrone Schnittstelle zur Verarbeitung von Spielen im Klienten. */
	public final MatchServiceAsync MATCH_SERVICE = GWT.create(MatchService.class);
	/** Die asynchrone Schnittstelle zur Verarbeitung von paginierten-Listen. */
	public final PagingServiceAsync PAGING_SERVICE = GWT.create(PagingService.class);
	/** Die asynchrone Schnittstelle zur Verarbeitung der Spieler im Klienten. */
	public final PlayerServiceAsync PLAYER_SERVICE = GWT.create(PlayerService.class);
	/** Die asynchrone Schnittstelle zur Verarbeitung der Teams im Klienten. */
	public final TeamServiceAsync TEAM_SERVICE = GWT.create(TeamService.class);

}
