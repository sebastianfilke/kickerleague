package de.kickerapp.shared.dto;

import java.util.Date;

import de.kickerapp.shared.common.BaseData;
import de.kickerapp.shared.common.Tendency;

/**
 * Datenklasse zum Halten der Informationen eines Spielers.
 * 
 * @author Sebastian Filke
 */
public class PlayerDto extends BaseData implements IPlayer {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -8362977112070597348L;

	/** Der Nachname des Spielers. */
	private String lastName;
	/** Der Vorname des Spielers. */
	private String firstName;
	/** Der Nickname des Spielers. */
	private String nickName;
	/** Die E-Mail Adresse des Spielers. */
	private String eMail;
	/** Das Datum des letzten Spiels des Spielers. */
	private Date lastMatchDate;

	private Integer singleMatches;

	private Integer singleWins;

	private Integer singleLosses;

	private String singleGoals;

	private String singleGoalDifference;

	private Integer doubleWins;

	private Integer doubleLosses;

	private String doubleGoals;

	private Integer prevTablePlace;

	private Integer curTablePlace;

	private Integer points;

	private Tendency tendency;

	/**
	 * Erzeugt einen neuen Spieler ohne Angaben.
	 */
	public PlayerDto() {
		super();

		lastName = "";
		firstName = "";
		nickName = "";
		eMail = "";
		lastMatchDate = null;
		singleMatches = 0;
		singleWins = 0;
		singleLosses = 0;
		singleGoals = "";
		singleGoalDifference = "";
		curTablePlace = 0;
	}

	/**
	 * Erzeugt einen neuen Spieler mit Vor- und Nachnamen.
	 * 
	 * @param lastName Der Nachname des Spielers als <code>String</code>.
	 * @param firstName Der Vornamen des Spielers als <code>String</code>.
	 */
	public PlayerDto(String lastName, String firstName) {
		this();
		this.lastName = lastName;
		this.firstName = firstName;
		setLabel(firstName + " " + lastName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLastName() {
		return lastName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFirstName() {
		return firstName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNickName() {
		return nickName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEMail(String eMail) {
		this.eMail = eMail;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getEMail() {
		return eMail;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLastMatchDate(Date lastMatchDate) {
		this.lastMatchDate = lastMatchDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getLastMatchDate() {
		return lastMatchDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getSingleMatches() {
		return singleMatches;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSingleMatches(Integer singleMatches) {
		this.singleMatches = singleMatches;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getSingleWins() {
		return singleWins;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSingleWins(Integer singleWins) {
		this.singleWins = singleWins;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getSingleLosses() {
		return singleLosses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSingleLosses(Integer singleLosses) {
		this.singleLosses = singleLosses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSingleGoals() {
		return singleGoals;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSingleGoals(String singleGoals) {
		this.singleGoals = singleGoals;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSingleGoalDifference() {
		return singleGoalDifference;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSingleGoalDifference(String singleGoalDifference) {
		this.singleGoalDifference = singleGoalDifference;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getDoubleWins() {
		return doubleWins;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDoubleWins(Integer doubleWins) {
		this.doubleWins = doubleWins;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getDoubleLosses() {
		return doubleLosses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDoubleLosses(Integer doubleLosses) {
		this.doubleLosses = doubleLosses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDoubleGoals() {
		return doubleGoals;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDoubleGoals(String doubleGoals) {
		this.doubleGoals = doubleGoals;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getPrevTablePlace() {
		return prevTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPrevTablePlace(Integer prevTablePlace) {
		this.prevTablePlace = prevTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getCurTablePlace() {
		return curTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCurTablePlace(Integer curTablePlace) {
		this.curTablePlace = curTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getPoints() {
		return points;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPoints(Integer points) {
		this.points = points;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tendency getTendency() {
		return tendency;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTendency(Tendency tendency) {
		this.tendency = tendency;
	}

}
