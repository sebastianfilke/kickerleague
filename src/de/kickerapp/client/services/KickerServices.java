package de.kickerapp.client.services;

import com.google.gwt.core.client.GWT;

public interface KickerServices {

	// public MatchServiceAsync MATCH_SERVICE = GWT.create(MatchService.class);

	public PagingServiceAsync PAGING_SERVICE = GWT.create(PagingService.class);

	public PlayerServiceAsync PLAYER_SERVICE = GWT.create(PlayerService.class);

}
