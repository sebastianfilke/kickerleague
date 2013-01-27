package de.kickerapp.shared.dto;

import java.util.Date;

import de.kickerapp.shared.common.IBase;

/**
 * @author Basti
 */
public interface IMatch extends IBase {

	public void setMatchNumber(String matchNumber);

	public String getMatchNumber();

	public void setMatchDate(Date matchDate);

	public Date getMatchDate();

	public void setTeam1(TeamDto team1);

	public TeamDto getTeam1();

	public void setTeam2(TeamDto team2);

	public TeamDto getTeam2();

	public void setSets(MatchSetDto sets);

	public MatchSetDto getSets();

}
