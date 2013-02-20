package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseData;
import de.kickerapp.shared.common.Tendency;

public class PlayerDoubleStatsDto extends BaseData {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -839062201651956733L;

	private Integer doubleWins;

	private Integer doubleLosses;

	private Integer doubleShotGoals;

	private Integer doubleGetGoals;

	private Integer doublePrevTablePlace;

	private Integer doubleCurTablePlace;

	private Integer doubleLastMatchPoints;

	private Integer doublePoints;

	private Tendency doubleTendency;

	public PlayerDoubleStatsDto() {
		super();
		doubleWins = 0;
		doubleLosses = 0;
		doubleShotGoals = 0;
		doubleGetGoals = 0;
		doublePrevTablePlace = 0;
		doubleCurTablePlace = 0;
		doubleLastMatchPoints = 0;
		doublePoints = 0;
		doubleTendency = Tendency.Constant;
	}

	public Integer getDoubleWins() {
		return doubleWins;
	}

	public void setDoubleWins(Integer doubleWins) {
		this.doubleWins = doubleWins;
	}

	public Integer getDoubleLosses() {
		return doubleLosses;
	}

	public void setDoubleLosses(Integer doubleLosses) {
		this.doubleLosses = doubleLosses;
	}

	public Integer getDoubleShotGoals() {
		return doubleShotGoals;
	}

	public void setDoubleShotGoals(Integer doubleShotGoals) {
		this.doubleShotGoals = doubleShotGoals;
	}

	public Integer getDoubleGetGoals() {
		return doubleGetGoals;
	}

	public void setDoubleGetGoals(Integer doubleGetGoals) {
		this.doubleGetGoals = doubleGetGoals;
	}

	public Integer getDoublePrevTablePlace() {
		return doublePrevTablePlace;
	}

	public void setDoublePrevTablePlace(Integer doublePrevTablePlace) {
		this.doublePrevTablePlace = doublePrevTablePlace;
	}

	public Integer getDoubleCurTablePlace() {
		return doubleCurTablePlace;
	}

	public void setDoubleCurTablePlace(Integer doubleCurTablePlace) {
		this.doubleCurTablePlace = doubleCurTablePlace;
	}

	public Integer getDoubleLastMatchPoints() {
		return doubleLastMatchPoints;
	}

	public void setDoubleLastMatchPoints(Integer doubleLastMatchPoints) {
		this.doubleLastMatchPoints = doubleLastMatchPoints;
	}

	public Integer getDoublePoints() {
		return doublePoints;
	}

	public void setDoublePoints(Integer doublePoints) {
		this.doublePoints = doublePoints;
	}

	public Tendency getDoubleTendency() {
		return doubleTendency;
	}

	public void setDoubleTendency(Tendency doubleTendency) {
		this.doubleTendency = doubleTendency;
	}

}
