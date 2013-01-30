package de.kickerapp.server.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import de.kickerapp.shared.common.Tendency;

/**
 * Datenklasse zum Halten der Informationen für einen Team.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class Team extends BaseEntity {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 7796571017092493823L;

	/** Die Spieler des Teams. */
	@Persistent
	private Set<Long> players;
	/** Das Datum des letzten Spiels des Teams. */
	@Persistent
	private Date lastMatchDate;
	/** Die Statistik des Teams. */
	@Embedded
	@Persistent(defaultFetchGroup = "true")
	private TeamStats teamStats;

	/**
	 * Erzeugt ein neues Team.
	 */
	public Team() {
		super();
		players = new HashSet<Long>();
		teamStats = new TeamStats();
	}

	public Team(Player player1, Player player2) {
		this();
		players.add(player1.getKey().getId());
		players.add(player2.getKey().getId());
	}

	public Set<Long> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Long> players) {
		this.players = players;
	}

	/**
	 * Setzt das Datum des letzten Spiels des Spielers.
	 * 
	 * @param lastMatchDate Das Datum des letzten Spiels des Spielers als
	 *            {@link Date}.
	 */
	public void setLastMatchDate(Date lastMatchDate) {
		this.lastMatchDate = lastMatchDate;
	}

	/**
	 * Liefert das Datum des letzten Spiels des Spielers.
	 * 
	 * @return Das Datum des letzten Spiels des Spielers als {@link Date}.
	 */
	public Date getLastMatchDate() {
		return lastMatchDate;
	}

	public TeamStats getTeamStats() {
		return teamStats;
	}

	public void setTeamStats(TeamStats teamStats) {
		this.teamStats = teamStats;
	}

	@PersistenceCapable
	public static class TeamStats implements Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 8012892836503722333L;

		@Persistent
		private Integer wins;

		@Persistent
		private Integer losses;

		@Persistent
		private Integer shotGoals;

		@Persistent
		private Integer getGoals;

		@Persistent
		private Integer prevTablePlace;

		@Persistent
		private Integer curTablePlace;

		@Persistent
		private Integer points;

		@Persistent
		private Tendency tendency;

		public TeamStats() {
			wins = 0;
			losses = 0;
			shotGoals = 0;
			getGoals = 0;
			prevTablePlace = 0;
			curTablePlace = 0;
			points = 1000;
			tendency = Tendency.Constant;
		}

		public Integer getWins() {
			return wins;
		}

		public void setWins(Integer wins) {
			this.wins = wins;
		}

		public Integer getLosses() {
			return losses;
		}

		public void setLosses(Integer losses) {
			this.losses = losses;
		}

		public Integer getShotGoals() {
			return shotGoals;
		}

		public void setShotGoals(Integer shotGoals) {
			this.shotGoals = shotGoals;
		}

		public Integer getGetGoals() {
			return getGoals;
		}

		public void setGetGoals(Integer getGoals) {
			this.getGoals = getGoals;
		}

		public Integer getPrevTablePlace() {
			return prevTablePlace;
		}

		public void setPrevTablePlace(Integer prevTablePlace) {
			this.prevTablePlace = prevTablePlace;
		}

		public Integer getCurTablePlace() {
			return curTablePlace;
		}

		public void setCurTablePlace(Integer curTablePlace) {
			this.curTablePlace = curTablePlace;
		}

		public Integer getPoints() {
			return points;
		}

		public void setPoints(Integer points) {
			this.points = points;
		}

		public Tendency getTendency() {
			return tendency;
		}

		public void setTendency(Tendency tendency) {
			this.tendency = tendency;
		}

	}

}
