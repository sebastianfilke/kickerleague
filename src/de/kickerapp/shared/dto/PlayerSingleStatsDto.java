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

	private Integer singleLastMatchPoints;

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
		singleLastMatchPoints = 0;
		singlePoints = 0;
		singleTendency = Tendency.Constant;
	}

	public Integer getSingleWins() {
		return singleWins;
	}

	public void setSingleWins(Integer singleWins) {
		this.singleWins = singleWins;
	}

	public Integer getSingleLosses() {
		return singleLosses;
	}

	public void setSingleLosses(Integer singleLosses) {
		this.singleLosses = singleLosses;
	}

	public Integer getSingleShotGoals() {
		return singleShotGoals;
	}

	public void setSingleShotGoals(Integer singleShotGoals) {
		this.singleShotGoals = singleShotGoals;
	}

	public Integer getSingleGetGoals() {
		return singleGetGoals;
	}

	public void setSingleGetGoals(Integer singleGetGoals) {
		this.singleGetGoals = singleGetGoals;
	}

	public Integer getSinglePrevTablePlace() {
		return singlePrevTablePlace;
	}

	public void setSinglePrevTablePlace(Integer singlePrevTablePlace) {
		this.singlePrevTablePlace = singlePrevTablePlace;
	}

	public Integer getSingleCurTablePlace() {
		return singleCurTablePlace;
	}

	public void setSingleCurTablePlace(Integer singleCurTablePlace) {
		this.singleCurTablePlace = singleCurTablePlace;
	}

	public Integer getSingleLastMatchPoints() {
		return singleLastMatchPoints;
	}

	public void setSingleLastMatchPoints(Integer singleLastMatchPoints) {
		this.singleLastMatchPoints = singleLastMatchPoints;
	}

	public Integer getSinglePoints() {
		return singlePoints;
	}

	public void setSinglePoints(Integer singlePoints) {
		this.singlePoints = singlePoints;
	}

	public Tendency getSingleTendency() {
		return singleTendency;
	}

	public void setSingleTendency(Tendency singleTendency) {
		this.singleTendency = singleTendency;
	}

}
