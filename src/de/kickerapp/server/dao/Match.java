package de.kickerapp.server.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import de.kickerapp.server.dao.fetchplans.MatchPlan;

/**
 * Datenklasse zum Halten der Informationen für ein Einzel- bzw. Doppelspiel.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
@FetchGroup(name = MatchPlan.COMMENT, members = { @Persistent(name = "matchComment") })
public class Match extends BaseDao {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Die Spielnummer. */
	@Persistent
	private Integer matchNumber;
	/** Das Spieldatum. */
	@Persistent
	private Date matchDate;
	/** Der Kommentar zum Spiel. */
	@Persistent
	private String matchComment;
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
		matchComment = "";
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
	 * Liefert den Kommentar zum Spiel.
	 * 
	 * @return Der Kommentar zum Spiel als {@link String}.
	 */
	public String getMatchComment() {
		return matchComment;
	}

	/**
	 * Setzt den Kommentar zum Spiel.
	 * 
	 * @param matchComment Der Kommentar zum Spiel als {@link String}.
	 */
	public void setMatchComment(String matchComment) {
		this.matchComment = matchComment;
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
	@PersistenceCapable(detachable = "true")
	public static class MatchPoints implements Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 1L;

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
			super();

			matchPointsTeam1 = new ArrayList<Integer>();
			matchPointsTeam2 = new ArrayList<Integer>();
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

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
			sb.append(" [");
			sb.append("matchPointsTeam1=");
			for (int i = 0; i < matchPointsTeam1.size(); i++) {
				sb.append(matchPointsTeam1.get(i));
				if (i < matchPointsTeam1.size() - 1) {
					sb.append(",");
				}
			}
			sb.append(",").append("matchPointsTeam2=");
			for (int i = 0; i < matchPointsTeam2.size(); i++) {
				sb.append(matchPointsTeam2.get(i));
				if (i < matchPointsTeam2.size() - 1) {
					sb.append(",");
				}
			}
			sb.append("]");

			return sb.toString();
		}

	}

	/**
	 * Datenklasse zum Halten der Informationen für die Spielsätze eines Spiels.
	 * 
	 * @author Sebastian Filke
	 */
	@PersistenceCapable(detachable = "true")
	public static class MatchSets implements Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 1L;

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
			super();

			matchSetsTeam1 = new ArrayList<Integer>();
			matchSetsTeam2 = new ArrayList<Integer>();
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

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
			sb.append(" [");
			sb.append("matchSetsTeam1=");
			for (int i = 0; i < matchSetsTeam1.size(); i++) {
				sb.append(matchSetsTeam1.get(i));
				if (i < matchSetsTeam1.size() - 1) {
					sb.append(",");
				}
			}
			sb.append(",").append("matchSetsTeam2=");
			for (int i = 0; i < matchSetsTeam2.size(); i++) {
				sb.append(matchSetsTeam2.get(i));
				if (i < matchSetsTeam2.size() - 1) {
					sb.append(",");
				}
			}
			sb.append("]");

			return sb.toString();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(getKey().getId()).append(", ");
		sb.append("matchNumber=").append(matchNumber).append(", ");
		sb.append("matchDate=").append(matchDate).append(", ");
		sb.append("matchComment=").append(matchComment);
		sb.append("]");

		return sb.toString();
	}

}
