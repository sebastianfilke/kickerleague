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
	/** Die Spielpunkte der Teams bzw. Spieler. */
	@Embedded
	@Persistent(defaultFetchGroup = "true")
	private MatchPoints matchPoints;
	/** Die Spielsätze der Teams bzw. Spieler. */
	@Embedded
	@Persistent(defaultFetchGroup = "true")
	private MatchSets matchSets;

	/**
	 * Erzeugt ein neues Spiel ohne Angaben.
	 */
	public Match() {
		super();

		matchNumber = 0;
		matchDate = null;
		matchType = MatchType.NONE;
		team1 = null;
		team2 = null;
		matchPoints = new MatchPoints();
		matchSets = new MatchSets();
	}

	/**
	 * Liefert die Spielnummer.
	 * 
	 * @return Die Spielnummer als {@link Integer}.
	 */
	public Integer getMatchNumber() {
		return matchNumber;
	}

	/**
	 * Setzt die Spielnummer.
	 * 
	 * @param matchNumber Die Spielnummer als {@link Integer}.
	 */
	public void setMatchNumber(Integer matchNumber) {
		this.matchNumber = matchNumber;
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
	 * Setzt das Spieldatum.
	 * 
	 * @param matchDate Das Spieldatum als {@link Date}.
	 */
	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}

	/**
	 * Liefert den Spieltyp.
	 * 
	 * @return Der Spieltyp als {@link MatchType}.
	 */
	public MatchType getMatchType() {
		return matchType;
	}

	/**
	 * Setzt den Spieltyp.
	 * 
	 * @param matchType Der Spieltyp als {@link MatchType}.
	 */
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}

	/**
	 * Liefert die Datenbank-Id des ersten Teams bzw. Spielers.
	 * 
	 * @return Die Datenbank-Id des ersten Teams bzw. Spielers als {@link Long}.
	 */
	public Long getTeam1() {
		return team1;
	}

	/**
	 * Setzt die Datenbank-Id des ersten Teams bzw. Spielers.
	 * 
	 * @param team1 Die Datenbank-Id des ersten Teams bzw. Spielers als {@link Long}.
	 */
	public void setTeam1(Long team1) {
		this.team1 = team1;
	}

	/**
	 * Liefert die Datenbank-Id des zweiten Teams bzw. Spielers.
	 * 
	 * @return Die Datenbank-Id des zweiten Teams bzw. Spielers als {@link Long}.
	 */
	public Long getTeam2() {
		return team2;
	}

	/**
	 * Setzt die Datenbank-Id des zweiten Teams bzw. Spielers.
	 * 
	 * @param team2 Die Datenbank-Id des zweiten Teams bzw. Spielers als {@link Long}.
	 */
	public void setTeam2(Long team2) {
		this.team2 = team2;
	}

	/**
	 * Liefert die Spielpunkte der Teams bzw. Spieler.
	 * 
	 * @return Die Spielpunkte der Teams bzw. Spieler als {@link MatchPoints}.
	 */
	public MatchPoints getMatchPoints() {
		return matchPoints;
	}

	/**
	 * Setzt die Spielpunkte der Teams bzw. Spieler.
	 * 
	 * @param matchPoints Die Spielpunkte der Teams bzw. Spieler als {@link MatchPoints}.
	 */
	public void setMatchPoints(MatchPoints matchPoints) {
		this.matchPoints = matchPoints;
	}

	/**
	 * Liefert die Spielsätze der Teams bzw. Spieler.
	 * 
	 * @return Die Spielsätze der Teams bzw. Spieler als {@link MatchSets}.
	 */
	public MatchSets getMatchSets() {
		return matchSets;
	}

	/**
	 * Setzt die Spielsätze der Teams bzw. Spieler.
	 * 
	 * @param matchSets Die Spielsätze der Teams bzw. Spieler als {@link MatchSets}.
	 */
	public void setMatchSets(MatchSets matchSets) {
		this.matchSets = matchSets;
	}

	/**
	 * Datenklasse zum Halten der Informationen für die Punkte eines Spiels.
	 * 
	 * @author Sebastian Filke
	 */
	@PersistenceCapable
	public static class MatchPoints implements Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 8275268972473429142L;

		/** Die Punkte des ersten Teams bzw. Spielers. */
		@Persistent
		private ArrayList<Integer> matchPointsTeam1;
		/** Die Punkte des ersten Teams bzw. Spielers. */
		@Persistent
		private ArrayList<Integer> matchPointsTeam2;

		/**
		 * Erzeugt Spielpunkte mit leeren Punktzahlen.
		 */
		public MatchPoints() {
			this.matchPointsTeam1 = new ArrayList<Integer>();
			this.matchPointsTeam2 = new ArrayList<Integer>();
		}

		/**
		 * Erzeugt Spielpunkte mit übergebenen Punktzahlen.
		 * 
		 * @param matchPointsTeam1 Die Punkte des ersten Teams bzw. Spielers als {@link ArrayList}.
		 * @param matchPointsTeam2 Die Punkte des zweiten Teams bzw. Spielers als {@link ArrayList}.
		 */
		public MatchPoints(ArrayList<Integer> matchPointsTeam1, ArrayList<Integer> matchPointsTeam2) {
			this.matchPointsTeam1 = matchPointsTeam1;
			this.matchPointsTeam2 = matchPointsTeam2;
		}

		/**
		 * Liefert die Punkte des ersten Teams bzw. Spielers.
		 * 
		 * @return Die Punkte des ersten Teams bzw. Spielers als {@link ArrayList}.
		 */
		public ArrayList<Integer> getMatchPointsTeam1() {
			return matchPointsTeam1;
		}

		/**
		 * Setzt die Punkte des ersten Teams bzw. Spielers.
		 * 
		 * @param matchPointsTeam1 Die Punkte des ersten Teams bzw. Spielers als {@link ArrayList}.
		 */
		public void setMatchPointsTeam1(ArrayList<Integer> matchPointsTeam1) {
			this.matchPointsTeam1 = matchPointsTeam1;
		}

		/**
		 * Liefert die Punkte des zweiten Teams bzw. Spielers.
		 * 
		 * @return Die Punkte des zweiten Teams bzw. Spielers als {@link ArrayList}.
		 */
		public ArrayList<Integer> getMatchPointsTeam2() {
			return matchPointsTeam2;
		}

		/**
		 * Setzt die Punkte des zweiten Teams bzw. Spielers.
		 * 
		 * @param matchPointsTeam2 Die Punkte des zweiten Teams bzw. Spielers als {@link ArrayList}.
		 */
		public void setMatchPointsTeam2(ArrayList<Integer> matchPointsTeam2) {
			this.matchPointsTeam2 = matchPointsTeam2;
		}

	}

	/**
	 * Datenklasse zum Halten der Informationen für die Spielsätze eines Spiels.
	 * 
	 * @author Sebastian Filke
	 */
	@PersistenceCapable
	public static class MatchSets implements Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 5964399211443702907L;

		/** Die Sätze des ersten Teams bzw. Spielers. */
		@Persistent
		private ArrayList<Integer> matchSetsTeam1;
		/** Die Sätze des zweiten Teams bzw. Spielers. */
		@Persistent
		private ArrayList<Integer> matchSetsTeam2;

		/**
		 * Erzeugt Spielsätze mit leeren Sätzen.
		 */
		public MatchSets() {
			this.matchSetsTeam1 = new ArrayList<Integer>();
			this.matchSetsTeam2 = new ArrayList<Integer>();
		}

		/**
		 * Erzeugt Spielsätze mit übergebenen Sätzen.
		 * 
		 * @param matchSetsTeam1 Die Sätzen des ersten Teams bzw. Spielers als {@link ArrayList}.
		 * @param matchSetsTeam2 Die Sätzen des zweiten Teams bzw. Spielers als {@link ArrayList}.
		 */
		public MatchSets(ArrayList<Integer> matchSetsTeam1, ArrayList<Integer> matchSetsTeam2) {
			this.matchSetsTeam1 = matchSetsTeam1;
			this.matchSetsTeam2 = matchSetsTeam2;
		}

		/**
		 * Liefert die Sätze des ersten Teams bzw. Spielers.
		 * 
		 * @return Die Sätze des ersten Teams bzw. Spielers als {@link ArrayList}.
		 */
		public ArrayList<Integer> getMatchSetsTeam1() {
			return matchSetsTeam1;
		}

		/**
		 * Setzt die Sätze des ersten Teams bzw. Spielers.
		 * 
		 * @param matchSetsTeam1 Die Sätze des ersten Teams bzw. Spielers als {@link ArrayList}.
		 */
		public void setMatchSetsTeam1(ArrayList<Integer> matchSetsTeam1) {
			this.matchSetsTeam1 = matchSetsTeam1;
		}

		/**
		 * Liefert die Sätze des zweiten Teams bzw. Spielers.
		 * 
		 * @return Die Sätze des zweiten Teams bzw. Spielers als {@link ArrayList}.
		 */
		public ArrayList<Integer> getMatchSetsTeam2() {
			return matchSetsTeam2;
		}

		/**
		 * Setzt die Sätze des zweiten Teams bzw. Spielers.
		 * 
		 * @param matchSetsTeam2 Die Sätze des zweiten Teams bzw. Spielers als {@link ArrayList}.
		 */
		public void setMatchSetsTeam2(ArrayList<Integer> matchSetsTeam2) {
			this.matchSetsTeam2 = matchSetsTeam2;
		}

	}

}
