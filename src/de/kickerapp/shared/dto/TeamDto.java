package de.kickerapp.shared.dto;

import java.util.Date;

/**
 * Client-Datenklasse zum Halten der Informationen für ein Team.
 * 
 * @author Sebastian Filke
 */
public class TeamDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -412089566249613806L;

	/** Der erste Spieler des Teams. */
	private PlayerDto player1;
	/** Der zweite Spieler des Teams. */
	private PlayerDto player2;
	/** Das Datum des letzten Spiels des Teams. */
	private Date lastMatchDate;
	/** Die Teamspiel-Statistik des Teams. */
	private TeamStatsDto teamStatsDto;

	/**
	 * Erzeugt ein neues Team ohne Angaben und leeren Statistiken.
	 */
	public TeamDto() {
		super();

		player1 = null;
		player2 = null;
		lastMatchDate = null;
		teamStatsDto = null;
	}

	/**
	 * Erzeugt ein neues Team ohne Angaben und leeren Statistiken.
	 * 
	 * @param player1 Der erste Spieler des Teams als {@link PlayerDto}.
	 */
	public TeamDto(PlayerDto player1) {
		this();
		this.player1 = player1;
	}

	/**
	 * Erzeugt ein neues Team mit den übergebenen Spielern.
	 * 
	 * @param player1 Der erste Spieler des Teams als {@link PlayerDto}.
	 * @param player2 Der zweite Spieler des Teams als {@link PlayerDto}.
	 */
	public TeamDto(PlayerDto player1, PlayerDto player2) {
		this();
		this.player1 = player1;
		this.player2 = player2;
	}

	/**
	 * Liefert den ersten Spieler des Teams.
	 * 
	 * @return Der erste Spieler des Teams als {@link PlayerDto}.
	 */
	public PlayerDto getPlayer1() {
		return player1;
	}

	/**
	 * Setzt den ersten Spieler des Teams.
	 * 
	 * @param player1 Der erste Spieler des Teams als {@link PlayerDto}.
	 */
	public void setPlayer1(PlayerDto player1) {
		this.player1 = player1;
	}

	/**
	 * Liefert den zweiten Spieler des Teams.
	 * 
	 * @return Der zweite Spieler des Teams als {@link PlayerDto}.
	 */
	public PlayerDto getPlayer2() {
		return player2;
	}

	/**
	 * Setzt den zweiten Spieler des Teams.
	 * 
	 * @param player2 Der zweite Spieler des Teams als {@link PlayerDto}.
	 */
	public void setPlayer2(PlayerDto player2) {
		this.player2 = player2;
	}

	/**
	 * Liefert das Datum des letzten Spiels des Teams.
	 * 
	 * @return Das Datum des letzten Spiels des Teams als {@link Date}.
	 */
	public Date getLastMatchDate() {
		return lastMatchDate;
	}

	/**
	 * Setzt das Datum des letzten Spiels des Teams.
	 * 
	 * @param lastMatchDate Das Datum des letzten Spiels des Teams als {@link Date}.
	 */
	public void setLastMatchDate(Date lastMatchDate) {
		this.lastMatchDate = lastMatchDate;
	}

	/**
	 * Liefert die Teamspiel-Statistik des Teams.
	 * 
	 * @return Die Teamspiel-Statistik des Teams als {@link TeamStatsDto}.
	 */
	public TeamStatsDto getTeamStatsDto() {
		return teamStatsDto;
	}

	/**
	 * Setzt die Teamspiel-Statistik des Teams.
	 * 
	 * @param teamStatsDto Die Teamspiel-Statistik des Teams als {@link TeamStatsDto}.
	 */
	public void setTeamStatsDto(TeamStatsDto teamStatsDto) {
		this.teamStatsDto = teamStatsDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(getId()).append(", ");
		sb.append("lastMatchDate=").append(lastMatchDate);
		sb.append("]");

		return sb.toString();
	}

}
