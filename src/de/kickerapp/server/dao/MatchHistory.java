package de.kickerapp.server.dao;

import java.util.Date;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Datenklasse zum Halten der Informationen für den Verlauf eines Einzel- bzw. Doppelspiels.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public class MatchHistory extends BaseDao {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 2265186956159719422L;

	/** Die Spielnummer. */
	@Persistent
	private Integer matchNumber;
	/** Das Spieldatum. */
	@Persistent
	private Date matchDate;
	/** Die Gesamtpunkte des Teams bzw. Spielers. */
	@Persistent
	private Integer totalPoints;
	/** Die Spielpunkte des Teams bzw. Spielers. */
	@Persistent
	private Integer matchPoints;
	/** Die geschossenen Tore des Teams bzw. Spielers. */
	@Persistent
	private Integer shotGoals;
	/** Die kassierten Spiele des Teams bzw. Spielers. */
	@Persistent
	private Integer getGoals;
	/** Der Tabellenplatz des Teams bzw. Spielers. */
	@Persistent
	private Integer tablePlace;
	/** Die Angabe ob das Spiel gewonnen oder verloren wurde. */
	@Persistent
	private boolean winner;

	/**
	 * Erzeugt einen neuen Verlauf ohne Angaben.
	 */
	public MatchHistory() {
		super();

		matchNumber = 0;
		matchDate = null;
		totalPoints = 1000;
		matchPoints = 0;
		shotGoals = 0;
		getGoals = 0;
		tablePlace = 0;
		winner = false;
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
	 * Liefert die Gesamtpunkte des Teams bzw. Spielers.
	 * 
	 * @return Die Gesamtpunkte des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getTotalPoints() {
		return totalPoints;
	}

	/**
	 * Setzt die Gesamtpunkte des Teams bzw. Spielers.
	 * 
	 * @param totalPoints Die Gesamtpunkte des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}

	/**
	 * Liefert die Spielpunkte des Teams bzw. Spielers.
	 * 
	 * @return Die Spielpunkte des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getMatchPoints() {
		return matchPoints;
	}

	/**
	 * Setzt die Spielpunkte des Teams bzw. Spielers.
	 * 
	 * @param matchPoints Die Spielpunkte des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setMatchPoints(Integer matchPoints) {
		this.matchPoints = matchPoints;
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
	 * Liefert den Tabellenplatz des Teams bzw. Spielers.
	 * 
	 * @return Der Tabellenplatz des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getTablePlace() {
		return tablePlace;
	}

	/**
	 * Setzt den Tabellenplatz des Teams bzw. Spielers.
	 * 
	 * @param tablePlace Der Tabellenplatz des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setTablePlace(Integer tablePlace) {
		this.tablePlace = tablePlace;
	}

	/**
	 * Liefert die Angabe ob das Spiel gewonnen oder verloren wurde.
	 * 
	 * @return <code>true</code> falls das Spiel gewonnen wurde, andernfalls <code>false</code>.
	 */
	public boolean isWinner() {
		return winner;
	}

	/**
	 * Setzt die Angabe ob das Spiel gewonnen oder verloren wurde.
	 * 
	 * @param winner <code>true</code> falls das Spiel gewonnen wurde, andernfalls <code>false</code>.
	 */
	public void setWinner(boolean winner) {
		this.winner = winner;
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
		sb.append("totalPoints=").append(totalPoints).append(", ");
		sb.append("matchPoints=").append(matchPoints).append(", ");
		sb.append("shotGoals=").append(shotGoals).append(", ");
		sb.append("getGoals=").append(getGoals).append(", ");
		sb.append("tablePlace=").append(tablePlace).append(", ");
		sb.append("winner=").append(winner);
		sb.append("]");

		return sb.toString();
	}

}
