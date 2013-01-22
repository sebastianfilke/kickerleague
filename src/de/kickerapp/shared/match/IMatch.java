package de.kickerapp.shared.match;

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

	public void setLabelTeam1(String team1);

	public String getLabelTeam1();

	public void setLabelTeam2(String team2);

	public String getLabelTeam2();

	public void setLabelSets(String sets);

	public String getLabelSets();

	public void setTeam1(TeamDto team1);

	public TeamDto getTeam1();

	public void setTeam2(TeamDto team2);

	public TeamDto getTeam2();

	public void setSets(SetDto sets);

	public SetDto getSets();

}
