package de.kickerapp.client.properties;

import com.google.gwt.core.client.GWT;

public interface KickerProperties {

	public final ChartGameProperty CHART_GAME_PROPERTY = GWT.create(ChartGameProperty.class);

	public final ChartGoalProperty CHART_GOAL_PROPERTY = GWT.create(ChartGoalProperty.class);

	public final ChartOpponentProperty CHART_OPPONENT_PROPERTY = GWT.create(ChartOpponentProperty.class);

	public final ChartPointProperty CHART_POINT_PROPERTY = GWT.create(ChartPointProperty.class);

	public final MatchProperty MATCH_PROPERTY = GWT.create(MatchProperty.class);

	public final PlayerProperty PLAYER_PROPERTY = GWT.create(PlayerProperty.class);

	public final TeamProperty TEAM_PROPERTY = GWT.create(TeamProperty.class);

}
