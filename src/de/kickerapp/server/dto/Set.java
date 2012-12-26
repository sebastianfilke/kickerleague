package de.kickerapp.server.dto;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.datanucleus.annotations.Unowned;

/**
 * Datenklasse zum Halten der Informationen für einen Satz.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class Set extends BaseEntity {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 106622955398483447L;

	/** Das Ergebnis. */
	@Persistent
	private String result;
	/** Das zugehörige Spiel. */
	@Persistent
	@Unowned
	private Match match;
	/** Das zugehörige Team. */
	@Persistent
	@Unowned
	private Team team;

	/**
	 * Erzeugt einen neuen Satz ohne Angaben.
	 */
	public Set() {
	}

	/**
	 * Setzt das Ergebnis des Satzes.
	 * 
	 * @param result Das Ergebnis.
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * Liefert das Ergebnis des Satzes.
	 * 
	 * @return Das Ergebnis als {@link String}.
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Setzt das zugehörige Spiel für den Satz.
	 * 
	 * @param match Das zugehörige Spiel.
	 */
	public void setMatch(Match match) {
		this.match = match;
	}

	/**
	 * Liefert das zugehörige Spiel für den Satz.
	 * 
	 * @return Das zugehörige Spiel für den Satz.
	 */
	public Match getMatch() {
		return match;
	}

	/**
	 * Setzt das zugehörige Team für den Satz.
	 * 
	 * @param team Das zugehörige Team.
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * Liefert das zugehörige Team für den Satz.
	 * 
	 * @return Das zugehörige Team für den Satz.
	 */
	public Team getTeam() {
		return team;
	}

}
