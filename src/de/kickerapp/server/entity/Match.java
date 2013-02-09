package de.kickerapp.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import de.kickerapp.shared.common.MatchType;

/**
 * Datenklasse zum Halten der Informationen für ein Spiel.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class Match extends BaseEntity {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -77744879325097514L;

	/** Die Spielnummer. */
	@Persistent
	private Integer matchNumber;
	/** Das Spieldatum. */
	@Persistent
	private Date matchDate;
	/** Der Spieltyp. */
	@Persistent
	private MatchType matchType;
	/** Die Datenbank-Id des ersten Teams bzw. Spielers. */
	@Persistent
	private Long team1;
	/** Die Datenbank-Id des zweiten Teams bzw. Spielers. */
	@Persistent
	private Long team2;
	/** Die Liste der Spielsätze. */
	@Embedded
	@Persistent(defaultFetchGroup = "true")
	private MatchSets matchSets;

	/**
	 * Erzeugt ein neues Spiel ohne Angaben.
	 */
	public Match() {
		super();
	}

	public Integer getMatchNumber() {
		return matchNumber;
	}

	public void setMatchNumber(Integer matchNumber) {
		this.matchNumber = matchNumber;
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

	public MatchType getMatchType() {
		return matchType;
	}

	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}

	public Long getTeam1() {
		return team1;
	}

	public void setTeam1(Long team1) {
		this.team1 = team1;
	}

	public Long getTeam2() {
		return team2;
	}

	public void setTeam2(Long team2) {
		this.team2 = team2;
	}

	public MatchSets getMatchSets() {
		return matchSets;
	}

	public void setMatchSets(MatchSets matchSets) {
		this.matchSets = matchSets;
	}

	/**
	 * Datenklasse zum Halten der Informationen für einen Satz.
	 * 
	 * @author Sebastian Filke
	 */
	@PersistenceCapable
	public static class MatchSets implements Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 5964399211443702907L;

		/** Das Ergebnis. */
		@Persistent
		private ArrayList<Integer> setsTeam1;
		/** Das zugehörige Spiel. */
		@Persistent
		private ArrayList<Integer> setsTeam2;

		/**
		 * Erzeugt einen neuen Satz ohne Angaben.
		 */
		public MatchSets() {
			this.setsTeam1 = new ArrayList<Integer>();
			this.setsTeam2 = new ArrayList<Integer>();
		}

		/**
		 * Erzeugt einen neuen Satz ohne Angaben.
		 */
		public MatchSets(ArrayList<Integer> setsTeam1, ArrayList<Integer> setsTeam2) {
			this.setsTeam1 = setsTeam1;
			this.setsTeam2 = setsTeam2;
		}

		public ArrayList<Integer> getSetsTeam1() {
			return setsTeam1;
		}

		public void setSetsTeam1(ArrayList<Integer> setsTeam1) {
			this.setsTeam1 = setsTeam1;
		}

		public ArrayList<Integer> getSetsTeam2() {
			return setsTeam2;
		}

		public void setSetsTeam2(ArrayList<Integer> setsTeam2) {
			this.setsTeam2 = setsTeam2;
		}

	}

}
