package de.kickerapp.server.dto;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

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
	
	@Persistent
	private Match match;

	@Persistent
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

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}
