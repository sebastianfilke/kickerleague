package de.kickerapp.server.dao;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

import de.kickerapp.server.dao.fetchplans.MatchAggregationPlan;

/**
 * Datenklasse zum Halten der Informationen für die Anzahl der Doppelspiele eines Teams.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
@FetchGroup(name = MatchAggregationPlan.TEAM, members = { @Persistent(name = "team") })
public class TeamMatchYearAggregation extends MatchYearAggregation {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Das Team. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private Team team;

	/**
	 * Erzeugt eine neue Anzahl von Spielen ohne Angaben.
	 */
	public TeamMatchYearAggregation() {
		super();

		team = null;
	}

	/**
	 * Liefert das Team.
	 * 
	 * @return Das Team als {@link Player}.
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Setzt das Team.
	 * 
	 * @param team Das Team als {@link Player}.
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return super.toString();
	}

}
