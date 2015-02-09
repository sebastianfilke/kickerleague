package de.kickerapp.client.properties;

import com.google.gwt.core.client.GWT;

/**
 * Die Properties für die Applikation.
 * 
 * @author Sebastian Filke
 */
public class KickerProperties {

	/** Die Schnittstelle zur Verknüpfung der Attribute der Spiel-Chart-Beans. */
	public static final ChartGameProperty CHART_GAME_PROPERTY = GWT.create(ChartGameProperty.class);
	/** Die Schnittstelle zur Verknüpfung der Attribute der Tor-Chart-Beans. */
	public static final ChartGoalProperty CHART_GOAL_PROPERTY = GWT.create(ChartGoalProperty.class);
	/** Die Schnittstelle zur Verknüpfung der Attribute der Punkte-Chart-Beans. */
	public static final ChartPointProperty CHART_POINT_PROPERTY = GWT.create(ChartPointProperty.class);
	/** Die Schnittstelle zur Verknüpfung der Attribute der Gegner-Chart-Beans. */
	public static final ChartOpponentProperty CHART_OPPONENT_PROPERTY = GWT.create(ChartOpponentProperty.class);
	/** Die Schnittstelle zur Verknüpfung der Attribute der Spiel-Beans. */
	public static final MatchProperty MATCH_PROPERTY = GWT.create(MatchProperty.class);
	/** Die Schnittstelle zur Verknüpfung der Attribute der Spieler-Beans. */
	public static final PlayerProperty PLAYER_PROPERTY = GWT.create(PlayerProperty.class);
	/** Die Schnittstelle zur Verknüpfung der Attribute der Team-Beans. */
	public static final TeamProperty TEAM_PROPERTY = GWT.create(TeamProperty.class);

}
