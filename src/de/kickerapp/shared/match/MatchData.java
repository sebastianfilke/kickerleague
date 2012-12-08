package de.kickerapp.shared.match;

import java.util.Date;

import de.kickerapp.shared.common.BaseData;

public class MatchData extends BaseData implements IMatch {

	private String matchNumber;

	private Date matchDate;

	private String team1;

	private String team2;

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
	public void setTeam1(String team1) {
		this.team1 = team1;
	}

	@Override
	public String getTeam1() {
		return team1;
	}

	@Override
	public void setTeam2(String team2) {
		this.team2 = team2;
	}

	@Override
	public String getTeam2() {
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
