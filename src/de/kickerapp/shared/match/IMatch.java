package de.kickerapp.shared.match;

import java.util.Date;

import de.kickerapp.shared.common.IBase;

public interface IMatch extends IBase {

	public void setMatchNumber(String matchNumber);

	public String getMatchNumber();

	public void setMatchDate(Date matchDate);

	public Date getMatchDate();

	public void setTeam1(String team1);

	public String getTeam1();

	public void setTeam2(String team2);

	public String getTeam2();

	public void setMatchResult(String matchResult);

	public String getMatchResult();

}
