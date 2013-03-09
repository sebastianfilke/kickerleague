package de.kickerapp.client.services;

import com.google.gwt.core.client.GWT;

public interface KickerServices {
	
	public final ChartServiceAsync CHART_SERVICE = GWT.create(ChartService.class);

	public final MatchServiceAsync MATCH_SERVICE = GWT.create(MatchService.class);

	public final PagingServiceAsync PAGING_SERVICE = GWT.create(PagingService.class);

	public final PlayerServiceAsync PLAYER_SERVICE = GWT.create(PlayerService.class);

	public final TeamServiceAsync TEAM_SERVICE = GWT.create(TeamService.class);

}
