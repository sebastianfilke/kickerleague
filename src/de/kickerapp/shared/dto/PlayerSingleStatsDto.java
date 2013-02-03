package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseData;
import de.kickerapp.shared.common.Tendency;

public class PlayerSingleStatsDto extends BaseData {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -5045714917141004133L;

	private Integer singleWins;

	private Integer singleLosses;

	private Integer singleShotGoals;

	private Integer singleGetGoals;

	private Integer singlePrevTablePlace;

	private Integer singleCurTablePlace;

	private Integer singlePoints;

	private Tendency singleTendency;

	public PlayerSingleStatsDto() {
		super();
		singleWins = 0;
		singleLosses = 0;
		singleShotGoals = 0;
		singleGetGoals = 0;
		singlePrevTablePlace = 0;
		singleCurTablePlace = 0;
		singlePoints = 0;
		singleTendency = Tendency.Constant;
	}

	/**
	 * {@inheritDoc}
	 */

	public Integer getSingleWins() {
		return singleWins;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setSingleWins(Integer singleWins) {
		this.singleWins = singleWins;
	}

	/**
	 * {@inheritDoc}
	 */

	public Integer getSingleLosses() {
		return singleLosses;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setSingleLosses(Integer singleLosses) {
		this.singleLosses = singleLosses;
	}

	/**
	 * {@inheritDoc}
	 */

	public Integer getSingleShotGoals() {
		return singleShotGoals;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setSingleShotGoals(Integer singleShotGoals) {
		this.singleShotGoals = singleShotGoals;
	}

	/**
	 * {@inheritDoc}
	 */

	public Integer getSingleGetGoals() {
		return singleGetGoals;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setSingleGetGoals(Integer singleGetGoals) {
		this.singleGetGoals = singleGetGoals;
	}

	/**
	 * {@inheritDoc}
	 */

	public Integer getSinglePrevTablePlace() {
		return singlePrevTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setSinglePrevTablePlace(Integer singlePrevTablePlace) {
		this.singlePrevTablePlace = singlePrevTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */

	public Integer getSingleCurTablePlace() {
		return singleCurTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */

	public void setSingleCurTablePlace(Integer singleCurTablePlace) {
		this.singleCurTablePlace = singleCurTablePlace;
	}

	/**
	 * {@inheritDoc}
	 */

	public Integer getSinglePoints() {
		return singlePoints;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSinglePoints(Integer singlePoints) {
		this.singlePoints = singlePoints;
	}

	/**
	 * {@inheritDoc}
	 */
	public Tendency getSingleTendency() {
		return singleTendency;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSingleTendency(Tendency singleTendency) {
		this.singleTendency = singleTendency;
	}

}
