package de.kickerapp.server.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.Embedded;
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

	@Persistent
	private ArrayList<Long> team1;

	@Persistent
	private ArrayList<Long> team2;
	/** Die Liste der Spielsätze. */
	@Embedded
	@Persistent(defaultFetchGroup = "true")
	private Set sets;

	/**
	 * Erzeugt ein neues Spiel ohne Angaben.
	 */
	public Match() {
		super();
		team1 = new ArrayList<Long>();
		team2 = new ArrayList<Long>();
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

	public ArrayList<Long> getTeam1() {
		return team1;
	}

	public void setTeam1(ArrayList<Long> team1) {
		this.team1 = team1;
	}

	public ArrayList<Long> getTeam2() {
		return team2;
	}

	public void setTeam2(ArrayList<Long> team2) {
		this.team2 = team2;
	}

	public Set getSets() {
		return sets;
	}

	public void setSets(Set sets) {
		this.sets = sets;
	}

	/**
	 * Datenklasse zum Halten der Informationen für einen Satz.
	 * 
	 * @author Sebastian Filke
	 */
	@PersistenceCapable
	public static class Set implements Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 5964399211443702907L;

		/** Das Ergebnis. */
		@Persistent
		private ArrayList<Integer> resultTeam1;
		/** Das zugehörige Spiel. */
		@Persistent
		private ArrayList<Integer> resultTeam2;

		/**
		 * Erzeugt einen neuen Satz ohne Angaben.
		 */
		public Set() {
			this.resultTeam1 = new ArrayList<Integer>();
			this.resultTeam2 = new ArrayList<Integer>();
		}

		/**
		 * Erzeugt einen neuen Satz ohne Angaben.
		 */
		public Set(ArrayList<Integer> resultTeam1, ArrayList<Integer> resultTeam2) {
			this.resultTeam1 = resultTeam1;
			this.resultTeam2 = resultTeam2;
		}

		public ArrayList<Integer> getResultTeam1() {
			return resultTeam1;
		}

		public void setResultTeam1(ArrayList<Integer> resultTeam1) {
			this.resultTeam1 = resultTeam1;
		}

		public ArrayList<Integer> getResultTeam2() {
			return resultTeam2;
		}

		public void setResultTeam2(ArrayList<Integer> resultTeam2) {
			this.resultTeam2 = resultTeam2;
		}
	}

}
