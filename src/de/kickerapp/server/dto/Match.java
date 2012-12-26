package de.kickerapp.server.dto;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Datenklasse zum Halten der Informationen für ein Spiel.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class Match extends BaseEntity {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -77744879325097514L;

	/** Das Spieldatum. */
	@Persistent
	private Date matchDate;
	/** Die Liste der Spielsätze. */
	@Persistent
	private ArrayList<Set> sets;

	/**
	 * Erzeugt ein neues Spiel ohne Angaben.
	 */
	public Match() {
		sets = new ArrayList<Set>();
	}

	/**
	 * Setzt das Spieldatum.
	 * 
	 * @param matchDate Das Spieldatum als {@link Date}.
	 */
	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}

	/**
	 * Liefert das Spieldatum.
	 * 
	 * @return Das Spieldatum als {@link Date}.
	 */
	public Date getMatchDate() {
		return matchDate;
	}

	/**
	 * Setzt die Liste der Spielsätze.
	 * 
	 * @param sets Die Liste der Spielsätze.
	 */
	public void setSets(ArrayList<Set> sets) {
		this.sets = sets;
	}

	/**
	 * Liefert die Liste der Spielsätze.
	 * 
	 * @return Die Liste der Spielsätze.
	 */
	public ArrayList<Set> getSets() {
		return sets;
	}

}
