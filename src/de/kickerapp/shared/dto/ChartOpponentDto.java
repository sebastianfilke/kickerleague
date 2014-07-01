package de.kickerapp.shared.dto;

import de.kickerapp.shared.common.BaseDto;

public class ChartOpponentDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 9037519409303287728L;

	private String opponentName;

	private Integer playedGames;

	private Integer wins;

	private Integer defeats;

	public ChartOpponentDto() {
		super();

		opponentName = "";
		playedGames = 0;
		wins = 0;
		defeats = 0;
	}

	public String getOpponentName() {
		return opponentName;
	}

	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	public Integer getPlayedGames() {
		return playedGames;
	}

	public void setPlayedGames(Integer playedGames) {
		this.playedGames = playedGames;
	}

	public Integer getWins() {
		return wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Integer getDefeats() {
		return defeats;
	}

	public void setDefeats(Integer defeats) {
		this.defeats = defeats;
	}

}
