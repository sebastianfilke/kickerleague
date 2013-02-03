package de.kickerapp.client.properties;

import com.google.gwt.core.client.GWT;

public interface KickerProperties {

	public final MatchProperty MATCH_PROPERTY = GWT.create(MatchProperty.class);

	public final PlayerProperty PLAYER_PROPERTY = GWT.create(PlayerProperty.class);

	public final TeamProperty TEAM_PROPERTY = GWT.create(TeamProperty.class);

}
