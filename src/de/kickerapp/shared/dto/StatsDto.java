package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseDto;
import de.kickerapp.shared.common.Tendency;

public class StatsDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -3916608887988403202L;

	private Integer wins;

	private Integer losses;

	private Integer shotGoals;

	private Integer getGoals;

	private Integer prevTablePlace;

	private Integer curTablePlace;

	private Integer lastMatchPoints;

	private Integer points;

	private Tendency tendency;

	public StatsDto() {
		super();

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

	/**
	 * {@inheritDoc}
	 */

	public Integer getWins() {
		return wins;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	/**
	 * {@inheritDoc}
	 */

	public Integer getLosses() {
		return losses;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setLosses(Integer losses) {
		this.losses = losses;
	}

	/**
	 * {@inheritDoc}
	 */

	public Integer getShotGoals() {
		return shotGoals;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setShotGoals(Integer shotGoals) {
		this.shotGoals = shotGoals;
	}

	/**
	 * {@inheritDoc}
	 */

	public Integer getGetGoals() {
		return getGoals;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setGetGoals(Integer getGoals) {
		this.getGoals = getGoals;
	}

	/**
	 * {@inheritDoc}
	 */

	public Integer getPrevTablePlace() {
		return prevTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setPrevTablePlace(Integer prevTablePlace) {
		this.prevTablePlace = prevTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */

	public Integer getCurTablePlace() {
		return curTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setCurTablePlace(Integer curTablePlace) {
		this.curTablePlace = curTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getLastMatchPoints() {
		return lastMatchPoints;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLastMatchPoints(Integer lastMatchPoints) {
		this.lastMatchPoints = lastMatchPoints;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getPoints() {
		return points;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}

	/**
	 * {@inheritDoc}
	 */
	public Tendency getTendency() {
		return tendency;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTendency(Tendency tendency) {
		this.tendency = tendency;
	}

}
