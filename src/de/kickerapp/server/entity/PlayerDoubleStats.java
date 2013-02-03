package de.kickerapp.server.entity;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import de.kickerapp.shared.common.Tendency;

@PersistenceCapable
public class PlayerDoubleStats extends BaseEntity {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -7869682689379852096L;

	@Persistent
	private Integer wins;

	@Persistent
	private Integer losses;

	@Persistent
	private Integer shotGoals;

	@Persistent
	private Integer getGoals;

	@Persistent
	private Integer prevTablePlace;

	@Persistent
	private Integer curTablePlace;

	@Persistent
	private Integer points;

	@Persistent
	private Tendency tendency;

	public PlayerDoubleStats() {
		wins = 0;
		losses = 0;
		shotGoals = 0;
		getGoals = 0;
		prevTablePlace = 0;
		curTablePlace = 0;
		points = 1000;
		tendency = Tendency.Constant;
	}

	public Integer getWins() {
		return wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Integer getLosses() {
		return losses;
	}

	public void setLosses(Integer losses) {
		this.losses = losses;
	}

	public Integer getShotGoals() {
		return shotGoals;
	}

	public void setShotGoals(Integer shotGoals) {
		this.shotGoals = shotGoals;
	}

	public Integer getGetGoals() {
		return getGoals;
	}

	public void setGetGoals(Integer getGoals) {
		this.getGoals = getGoals;
	}

	public Integer getPrevTablePlace() {
		return prevTablePlace;
	}

	public void setPrevTablePlace(Integer prevTablePlace) {
		this.prevTablePlace = prevTablePlace;
	}

	public Integer getCurTablePlace() {
		return curTablePlace;
	}

	public void setCurTablePlace(Integer curTablePlace) {
		this.curTablePlace = curTablePlace;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Tendency getTendency() {
		return tendency;
	}

	public void setTendency(Tendency tendency) {
		this.tendency = tendency;
	}

}
