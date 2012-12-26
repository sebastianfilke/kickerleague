package de.kickerapp.shared.match;

import de.kickerapp.shared.common.BaseData;

public class TeamDto extends BaseData {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -412089566249613806L;

	private PlayerDto player1;

	private PlayerDto player2;

	public TeamDto() {

	}

	public TeamDto(PlayerDto player1) {
		this.player1 = player1;
	}

	public TeamDto(PlayerDto player1, PlayerDto player2) {
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

}
