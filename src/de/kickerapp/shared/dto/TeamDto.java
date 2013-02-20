package de.kickerapp.shared.dto;

import java.util.Date;

import de.kickerapp.shared.common.BaseData;
import de.kickerapp.shared.common.Tendency;

public class TeamDto extends BaseData implements ITeam {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -412089566249613806L;

	private PlayerDto player1;

	private PlayerDto player2;

	private Date lastMatchDate;

	private Integer wins;

	private Integer losses;

	private Integer shotGoals;

	private Integer getGoals;

	private Integer prevTablePlace;

	private Integer curTablePlace;

	private Integer lastMatchPoints;

	private Integer points;

	private Tendency tendency;

	public TeamDto() {
		super();

		player1 = null;
		player2 = null;
		lastMatchDate = null;
		wins = 0;
		losses = 0;
		shotGoals = 0;
		getGoals = 0;
		prevTablePlace = 0;
		curTablePlace = 0;
		lastMatchPoints = 0;
		points = 0;
		tendency = Tendency.Constant;
	}

	public TeamDto(PlayerDto player1) {
		this();
		this.player1 = player1;
	}

	public TeamDto(PlayerDto player1, PlayerDto player2) {
		this();
		this.player1 = player1;
		this.player2 = player2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerDto getPlayer1() {
		return player1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlayer1(PlayerDto player1) {
		this.player1 = player1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerDto getPlayer2() {
		return player2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlayer2(PlayerDto player2) {
		this.player2 = player2;
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
	public void setLastMatchDate(Date lastMatchDate) {
		this.lastMatchDate = lastMatchDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getWins() {
		return wins;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWins(Integer wins) {
		this.wins = wins;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getLosses() {
		return losses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLosses(Integer losses) {
		this.losses = losses;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getShotGoals() {
		return shotGoals;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShotGoals(Integer shotGoals) {
		this.shotGoals = shotGoals;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getGetGoals() {
		return getGoals;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGetGoals(Integer getGoals) {
		this.getGoals = getGoals;
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
	public Integer getLastMatchPoints() {
		return lastMatchPoints;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLastMatchPoints(Integer lastMatchPoints) {
		this.lastMatchPoints = lastMatchPoints;
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
