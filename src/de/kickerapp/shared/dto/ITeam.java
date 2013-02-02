package de.kickerapp.shared.dto;

import java.util.Date;

import de.kickerapp.shared.common.IBase;
import de.kickerapp.shared.common.Tendency;

public interface ITeam extends IBase {

	public PlayerDto getPlayer1();

	public void setPlayer1(PlayerDto player1);

	public PlayerDto getPlayer2();

	public void setPlayer2(PlayerDto player2);

	public Date getLastMatchDate();

	public void setLastMatchDate(Date lastMatchDate);

	public Integer getWins();

	public void setWins(Integer wins);

	public Integer getLosses();

	public void setLosses(Integer losses);

	public Integer getShotGoals();

	public void setShotGoals(Integer shotGoals);

	public Integer getGetGoals();

	public void setGetGoals(Integer getGoals);

	public Integer getPrevTablePlace();

	public void setPrevTablePlace(Integer prevTablePlace);

	public Integer getCurTablePlace();

	public void setCurTablePlace(Integer curTablePlace);

	public Integer getPoints();

	public void setPoints(Integer points);

	public Tendency getTendency();

	public void setTendency(Tendency tendency);

}
