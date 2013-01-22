package de.kickerapp.shared.match;

import java.util.Date;

import de.kickerapp.shared.common.IBase;
import de.kickerapp.shared.common.Tendency;

/**
 * Interface zum Datenklasse zum Halten der Informationen eines Spielers.
 * 
 * @author Sebastian Filke
 */
public interface IPlayer extends IBase {

	/**
	 * Setzt den Nachnamen des Spielers.
	 * 
	 * @param lastName Der Nachname des Spielers als {@link String}.
	 */
	public void setLastName(String lastName);

	/**
	 * Liefert den Nachnamen des Spielers.
	 * 
	 * @return Der Nachname des Spielers.
	 */
	public String getLastName();

	/**
	 * Setzt den Vornamen des Spielers.
	 * 
	 * @param firstName Der Vornamen des Spielers als {@link String}.
	 */
	public void setFirstName(String firstName);

	/**
	 * Liefert den Vornamen des Spielers.
	 * 
	 * @return Der Vornamen des Spielers.
	 */
	public String getFirstName();

	/**
	 * Setzt den Nicknamen des Spielers.
	 * 
	 * @param nickName Der Nicknamen des Spielers als {@link String}.
	 */
	public void setNickName(String nickName);

	/**
	 * Liefert den Nicknamen des Spielers.
	 * 
	 * @return Der Nicknamen des Spielers.
	 */
	public String getNickName();

	/**
	 * Setzt die E-Mail Adresse des Spielers.
	 * 
	 * @param eMail Die E-Mail Adresse des Spielers als {@link String}.
	 */
	public void setEMail(String eMail);

	/**
	 * Liefert die E-Mail Adresse des Spielers.
	 * 
	 * @return Die E-Mail Adresse des Spielers als {@link String}.
	 */
	public String getEMail();

	/**
	 * Setzt das Datum des letzten Spiels des Spielers.
	 * 
	 * @param lastMatchDate Das Datum des letzten Spiels des Spielers als
	 *            {@link Date}.
	 */
	public void setLastMatchDate(Date lastMatchDate);

	/**
	 * Liefert das Datum des letzten Spiels des Spielers.
	 * 
	 * @return Das Datum des letzten Spiels des Spielers als {@link Date}.
	 */
	public Date getLastMatchDate();

	public Integer getSingleMatches();

	public void setSingleMatches(Integer singleMatches);

	public Integer getSingleWins();

	public void setSingleWins(Integer singleWins);

	public Integer getSingleLosses();

	public void setSingleLosses(Integer singleLosses);

	public String getSingleGoals();

	public void setSingleGoals(String singleGetGoals);

	public String getSingleGoalDifference();

	public void setSingleGoalDifference(String singleGoalDifferenz);

	public Integer getDoubleWins();

	public void setDoubleWins(Integer doubleWins);

	public Integer getDoubleLosses();

	public void setDoubleLosses(Integer doubleLosses);

	public String getDoubleGoals();

	public void setDoubleGoals(String doubleGoals);

	public Tendency getTendency();

	public void setTendency(Tendency tendency);

	public Integer getPoints();

	public void setPoints(Integer points);

}
