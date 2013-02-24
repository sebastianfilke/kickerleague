package de.kickerapp.shared.dto;

import java.util.Date;

import de.kickerapp.shared.common.BaseData;
import de.kickerapp.shared.common.MatchType;

public class MatchDto extends BaseData implements IMatch {

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
	@Override
	public void setMatchNumber(String matchNumber) {
		this.matchNumber = matchNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMatchNumber() {
		return matchNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getMatchDate() {
		return matchDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGroupDate(String groupDate) {
		this.groupDate = groupDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGroupDate() {
		return groupDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MatchType getMatchType() {
		return matchType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTeam1(TeamDto team1) {
		this.team1 = team1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TeamDto getTeam1() {
		return team1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTeam2(TeamDto team2) {
		this.team2 = team2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TeamDto getTeam2() {
		return team2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MatchPointsDto getPoints() {
		return points;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPoints(MatchPointsDto points) {
		this.points = points;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSets(MatchSetDto sets) {
		this.sets = sets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MatchSetDto getSets() {
		return sets;
	}

}
