package de.kickerapp.shared.dto;

import java.util.Date;

import de.kickerapp.shared.common.BaseDto;
import de.kickerapp.shared.common.MatchType;

public class MatchDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 2656034118993930642L;

	private String matchNumber;

	private Date matchDate;

	private String groupDate;

	private MatchType matchType;

	private TeamDto team1;

	private TeamDto team2;

	private MatchPointsDto points;

	private MatchSetDto sets;

	public MatchDto() {
		super();
		matchNumber = "";
		matchDate = null;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void setMatchNumber(String matchNumber) {
		this.matchNumber = matchNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public String getMatchNumber() {
		return matchNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public Date getMatchDate() {
		return matchDate;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void setGroupDate(String groupDate) {
		this.groupDate = groupDate;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public String getGroupDate() {
		return groupDate;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public MatchType getMatchType() {
		return matchType;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void setTeam1(TeamDto team1) {
		this.team1 = team1;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public TeamDto getTeam1() {
		return team1;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void setTeam2(TeamDto team2) {
		this.team2 = team2;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public TeamDto getTeam2() {
		return team2;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public MatchPointsDto getPoints() {
		return points;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void setPoints(MatchPointsDto points) {
		this.points = points;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public void setSets(MatchSetDto sets) {
		this.sets = sets;
	}

	/**
	 * {@inheritDoc}
	 */
	
	public MatchSetDto getSets() {
		return sets;
	}

}
