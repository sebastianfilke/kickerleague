package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseDto;
import de.kickerapp.shared.common.Tendency;

/**
 * Client-Basidatenklasse zum Halten der Informationen für die Statistiken von Teams bzw. Spielern.
 * 
 * @author Sebastian Filke
 */
public class StatsDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -3916608887988403202L;

	/** Die gewonnen Spiele des Teams bzw. Spielers. */
	private Integer wins;
	/** Die verlorenen Spiele des Teams bzw. Spielers. */
	private Integer losses;
	/** Die geschossenen Tore des Teams bzw. Spielers. */
	private Integer shotGoals;
	/** Die kassierten Spiele des Teams bzw. Spielers. */
	private Integer getGoals;
	/** Der vorherige Tabellenplatz des Teams bzw. Spielers. */
	private Integer prevTablePlace;
	/** Die aktuelle Tabellenplatz des Teams bzw. Spielers. */
	private Integer curTablePlace;
	/** Die letzten Punkte des Teams bzw. Spielers. */
	private Integer lastMatchPoints;
	/** Die aktuellen Punkte des Teams bzw. Spielers. */
	private Integer points;
	/** Die aktuelle Tendenz des Teams bzw. Spielers. */
	private Tendency tendency;

	/**
	 * Erzeugt eine leere Statistik für ein Team bzw. Spieler.
	 */
	public StatsDto() {
		super();

		wins = 0;
		losses = 0;
		shotGoals = 0;
		getGoals = 0;
		prevTablePlace = 0;
		curTablePlace = 0;
		lastMatchPoints = 0;
		points = 1000;
		tendency = Tendency.Constant;
	}

	/**
	 * Liefert die gewonnen Spiele des Teams bzw. Spielers.
	 * 
	 * @return Die gewonnen Spiele des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getWins() {
		return wins;
	}

	/**
	 * Setzt die gewonnen Spiele des Teams bzw. Spielers.
	 * 
	 * @param wins Die gewonnen Spiele des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setWins(Integer wins) {
		this.wins = wins;
	}

	/**
	 * Liefert die verlorenen Spiele des Teams bzw. Spielers.
	 * 
	 * @return Die verlorenen Spiele des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getLosses() {
		return losses;
	}

	/**
	 * Setzt die verlorenen Spiele des Teams bzw. Spielers.
	 * 
	 * @param losses Die verlorenen Spiele des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setLosses(Integer losses) {
		this.losses = losses;
	}

	/**
	 * Liefert die geschossenen Tore des Teams bzw. Spielers.
	 * 
	 * @return Die geschossenen Tore des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getShotGoals() {
		return shotGoals;
	}

	/**
	 * Setzt die geschossenen Tore des Teams bzw. Spielers.
	 * 
	 * @param shotGoals Die geschossenen Tore des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setShotGoals(Integer shotGoals) {
		this.shotGoals = shotGoals;
	}

	/**
	 * Liefert die kassierten Tore des Teams bzw. Spielers.
	 * 
	 * @return Die kassierten Tore des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getGetGoals() {
		return getGoals;
	}

	/**
	 * Setzt die kassierten Tore des Teams bzw. Spielers.
	 * 
	 * @param getGoals Die kassierten Tore des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setGetGoals(Integer getGoals) {
		this.getGoals = getGoals;
	}

	/**
	 * Liefert den vorherigen Tabellenplatz des Teams bzw. Spielers.
	 * 
	 * @return Der vorherige Tabellenplatz des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getPrevTablePlace() {
		return prevTablePlace;
	}

	/**
	 * Setzt den vorherigen Tabellenplatz des Teams bzw. Spielers.
	 * 
	 * @param prevTablePlace Der vorherige Tabellenplatz des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setPrevTablePlace(Integer prevTablePlace) {
		this.prevTablePlace = prevTablePlace;
	}

	/**
	 * Liefert den aktuellen Tabellenplatz des Teams bzw. Spielers.
	 * 
	 * @return Der aktuelle Tabellenplatz des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getCurTablePlace() {
		return curTablePlace;
	}

	/**
	 * Setzt den aktuellen Tabellenplatz des Teams bzw. Spielers.
	 * 
	 * @param curTablePlace Der aktuellen Tabellenplatz des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setCurTablePlace(Integer curTablePlace) {
		this.curTablePlace = curTablePlace;
	}

	/**
	 * Liefert die letzten Punkte des Teams bzw. Spielers.
	 * 
	 * @return Die letzten Punkte des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getLastMatchPoints() {
		return lastMatchPoints;
	}

	/**
	 * Setzt die letzten Punkte des Teams bzw. Spielers.
	 * 
	 * @param lastMatchPoints Die letzten Punkte des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setLastMatchPoints(Integer lastMatchPoints) {
		this.lastMatchPoints = lastMatchPoints;
	}

	/**
	 * Liefert die aktuellen Punkte des Teams bzw. Spielers.
	 * 
	 * @return Die letzten Punkte des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getPoints() {
		return points;
	}

	/**
	 * Setzt die aktuellen Punkte des Teams bzw. Spielers.
	 * 
	 * @param points Die aktuellen Punkte des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}

	/**
	 * Liefert die aktuelle Tendenz des Teams bzw. Spielers.
	 * 
	 * @return Die aktuelle Tendenz des Teams bzw. Spielers als {@link Tendency}.
	 */
	public Tendency getTendency() {
		return tendency;
	}

	/**
	 * Setzt die aktuelle Tendenz des Teams bzw. Spielers.
	 * 
	 * @param tendency Die aktuelle Tendenz des Teams bzw. Spielers als {@link Tendency}.
	 */
	public void setTendency(Tendency tendency) {
		this.tendency = tendency;
	}

}