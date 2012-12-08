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
	
	@Persistent(mappedBy = "match")
	private ArrayList<Set> sets;

	/**
	 * Erzeugt ein neues Spiel ohne Angaben.
	 */
	public Match() {
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
	

	public ArrayList<Set> getSets() {
		return sets;
	}

	public void setSets(ArrayList<Set> sets) {
		this.sets = sets;
	}

}
