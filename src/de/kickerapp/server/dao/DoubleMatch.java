package de.kickerapp.server.dao;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

import de.kickerapp.server.dao.fetchplans.MatchPlan;

/**
 * Datenklasse zum Halten der Informationen für ein Doppelspiel.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
@FetchGroup(name = MatchPlan.BOTHTEAMS, members = { @Persistent(name = "team1"), @Persistent(name = "team2") })
public class DoubleMatch extends Match {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Das erste Team. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private Team team1;
	/** Das zweite Team. */
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private Team team2;

	/**
	 * Erzeugt ein neues Doppelspiel ohne Angaben.
	 */
	public DoubleMatch() {
		super();

		team1 = null;
		team2 = null;
	}

	/**
	 * Liefert das erste Team.
	 * 
	 * @return Das erste Team als {@link Team}.
	 */
	public Team getTeam1() {
		return team1;
	}

	/**
	 * Setzt das erste Team.
	 * 
	 * @param team1 Das erste Team als {@link Team}.
	 */
	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	/**
	 * Liefert das zweite Team.
	 * 
	 * @return Das zweite Team als {@link Team}.
	 */
	public Team getTeam2() {
		return team2;
	}

	/**
	 * Setzt das zweite Team.
	 * 
	 * @param team2 Das zweite Team als {@link Team}.
	 */
	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return super.toString();
	}

}
