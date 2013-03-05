package de.kickerapp.shared.dto;

import java.util.Date;

import de.kickerapp.shared.common.BaseDto;

public class TeamDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -412089566249613806L;

	private PlayerDto player1;

	private PlayerDto player2;

	private Date lastMatchDate;

	private TeamStatsDto teamStatsDto;

	public TeamDto() {
		super();

		player1 = null;
		player2 = null;
		lastMatchDate = null;
		teamStatsDto = null;
	}

	public TeamDto(PlayerDto player1) {
		this();
		this.player1 = player1;
	}

	public TeamDto(PlayerDto player1, PlayerDto player2) {
		this();
		this.player1 = player1;
		this.player2 = player2;
	}

	public PlayerDto getPlayer1() {
		return player1;
	}

	public void setPlayer1(PlayerDto player1) {
		this.player1 = player1;
	}

	public PlayerDto getPlayer2() {
		return player2;
	}

	public void setPlayer2(PlayerDto player2) {
		this.player2 = player2;
	}

	public Date getLastMatchDate() {
		return lastMatchDate;
	}

	public void setLastMatchDate(Date lastMatchDate) {
		this.lastMatchDate = lastMatchDate;
	}

	public TeamStatsDto getTeamStatsDto() {
		return teamStatsDto;
	}

	public void setTeamStatsDto(TeamStatsDto teamStatsDto) {
		this.teamStatsDto = teamStatsDto;
	}

}
