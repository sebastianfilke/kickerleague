package de.kickerapp.shared.match;

import java.util.Date;

import de.kickerapp.shared.common.BaseData;

public class MatchDto extends BaseData implements IMatch {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2656034118993930642L;

	private String matchNumber;

	private Date matchDate;

	private TeamDto team1;
	
	private TeamDto team2;

	private String matchResult;
	
	@Override
	public void setMatchNumber(String matchNumber) {
		this.matchNumber = matchNumber;
	}

	@Override
	public String getMatchNumber() {
		return matchNumber;
	}

	@Override
	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}

	@Override
	public Date getMatchDate() {
		return matchDate;
	}

	@Override
	public void setTeam1(TeamDto team1) {
		this.team1 = team1;
	}

	@Override
	public TeamDto getTeam1() {
		return team1;
	}

	@Override
	public void setTeam2(TeamDto team2) {
		this.team2 = team2;
	}

	@Override
	public TeamDto getTeam2() {
		return team2;
	}

	@Override
	public void setMatchResult(String matchResult) {
		this.matchResult = matchResult;
	}

	@Override
	public String getMatchResult() {
		return matchResult;
	}

}
