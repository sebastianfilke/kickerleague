package de.kickerapp.server.dao;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * Datenklasse zum Halten der Informationen für die Anzahl der Spiele eines Teams bzw. Spielers für Einzel- bzw. Doppelspiele.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable(detachable = "true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public class MatchYearAggregation extends BaseDao {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 2255546933965500040L;

	/** Das Jahr. */
	@Persistent
	private Integer year;
	/** Die Anzahl der Spiele. */
	@Persistent
	private Integer sumMatches;

	/**
	 * Erzeugt eine neue Anzahl von Spielen ohne Angaben.
	 */
	public MatchYearAggregation() {
		super();

		year = 2000;
		sumMatches = 0;
	}

	/**
	 * Liefert das Jahr.
	 * 
	 * @return Das Jahr als {@link Integer}.
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * Setzt das Jahr.
	 * 
	 * @param year Das Jahr als {@link Integer}.
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * Liefert die Anzahl der Spiele.
	 * 
	 * @return Die Anzahl der Spiele als {@link Integer}.
	 */
	public Integer getSumMatches() {
		return sumMatches;
	}

	/**
	 * Setzt die Anzahl der Spiele.
	 * 
	 * @param sumMatches Die Anzahl der Spiele als {@link Integer}.
	 */
	public void setSumMatches(Integer sumMatches) {
		this.sumMatches = sumMatches;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(getKey().getId()).append(", ");
		sb.append("year=").append(year).append(", ");
		sb.append("sumMatches=").append(sumMatches);
		sb.append("]");

		return sb.toString();
	}

}
