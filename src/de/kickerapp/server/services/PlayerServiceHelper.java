package de.kickerapp.server.services;

import java.io.Serializable;
import java.util.Comparator;

import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.PlayerDoubleStats;
import de.kickerapp.server.entity.PlayerSingleStats;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDoubleStatsDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.PlayerSingleStatsDto;

public class PlayerServiceHelper {

	/**
	 * Comparator zur Sortierung der Spieler nach Nachnamen.
	 * 
	 * @author Sebastian Filke
	 */
	protected static class PlayerComparator implements Comparator<PlayerDto>, Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 2081602779517954979L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(PlayerDto p1, PlayerDto p2) {
			return p1.getLastName().compareTo(p2.getLastName());
		}
	}

	/**
	 * Comparator zur Sortierung der Spieler für die Tabelle.
	 * 
	 * @author Sebastian Filke
	 */
	protected static class PlayerTableComparator implements Comparator<PlayerDto>, Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 7262644006482460970L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(PlayerDto p1, PlayerDto p2) {
			int comp = 0;
			if (p1.getPlayerSingleStats() != null && p2.getPlayerSingleStats() != null) {
				if (p1.getPlayerSingleStats().getCurTablePlace() == 0) {
					comp = 1;
				} else if (p2.getPlayerSingleStats().getCurTablePlace() == 0) {
					comp = -1;
				} else {
					comp = p1.getPlayerSingleStats().getCurTablePlace().compareTo(p2.getPlayerSingleStats().getCurTablePlace());
				}
			} else if (p1.getPlayerDoubleStats() != null && p2.getPlayerDoubleStats() != null) {
				if (p1.getPlayerDoubleStats().getCurTablePlace() == 0) {
					comp = 1;
				} else if (p2.getPlayerDoubleStats().getCurTablePlace() == 0) {
					comp = -1;
				} else {
					comp = p1.getPlayerDoubleStats().getCurTablePlace().compareTo(p2.getPlayerDoubleStats().getCurTablePlace());
				}
			}
			return comp;
		}
	}

	public static PlayerDto createPlayer(Player dbPlayer, MatchType matchType) {
		final PlayerDto playerDto = new PlayerDto(dbPlayer.getLastName(), dbPlayer.getFirstName());
		playerDto.setId(dbPlayer.getKey().getId());
		playerDto.setNickName(dbPlayer.getNickName());
		playerDto.setEMail(dbPlayer.getEMail());
		playerDto.setLastMatchDate(dbPlayer.getLastMatchDate());

		switch (matchType) {
		case SINGLE:
			setPlayerSingleStats(dbPlayer, playerDto);
			break;
		case DOUBLE:
			setPlayerDoubleStats(dbPlayer, playerDto);
			break;
		case BOTH:
			setPlayerSingleStats(dbPlayer, playerDto);
			setPlayerDoubleStats(dbPlayer, playerDto);
			break;
		default:
			break;
		}
		return playerDto;
	}

	private static void setPlayerSingleStats(Player dbPlayer, final PlayerDto playerDto) {
		final PlayerSingleStats playerSingleStats = PMFactory.getObjectById(PlayerSingleStats.class, dbPlayer.getPlayerSingleStats());

		// Single Match
		final PlayerSingleStatsDto playerSingleStatsDto = new PlayerSingleStatsDto();

		playerSingleStatsDto.setWins(playerSingleStats.getWins());
		playerSingleStatsDto.setLosses(playerSingleStats.getLosses());
		playerSingleStatsDto.setShotGoals(playerSingleStats.getShotGoals());
		playerSingleStatsDto.setGetGoals(playerSingleStats.getGetGoals());
		playerSingleStatsDto.setPrevTablePlace(playerSingleStats.getPrevTablePlace());
		playerSingleStatsDto.setCurTablePlace(playerSingleStats.getCurTablePlace());
		playerSingleStatsDto.setLastMatchPoints(playerSingleStats.getLastMatchPoints());
		playerSingleStatsDto.setPoints(playerSingleStats.getPoints());
		playerSingleStatsDto.setTendency(playerSingleStats.getTendency());

		playerDto.setPlayerSingleStats(playerSingleStatsDto);
	}

	private static void setPlayerDoubleStats(Player dbPlayer, PlayerDto playerDto) {
		final PlayerDoubleStats playerDoubleStats = PMFactory.getObjectById(PlayerDoubleStats.class, dbPlayer.getPlayerDoubleStats());

		// Double Match
		final PlayerDoubleStatsDto playerDoubleStatsDto = new PlayerDoubleStatsDto();

		playerDoubleStatsDto.setWins(playerDoubleStats.getWins());
		playerDoubleStatsDto.setLosses(playerDoubleStats.getLosses());
		playerDoubleStatsDto.setShotGoals(playerDoubleStats.getShotGoals());
		playerDoubleStatsDto.setGetGoals(playerDoubleStats.getGetGoals());
		playerDoubleStatsDto.setPrevTablePlace(playerDoubleStats.getPrevTablePlace());
		playerDoubleStatsDto.setCurTablePlace(playerDoubleStats.getCurTablePlace());
		playerDoubleStatsDto.setLastMatchPoints(playerDoubleStats.getLastMatchPoints());
		playerDoubleStatsDto.setPoints(playerDoubleStats.getPoints());
		playerDoubleStatsDto.setTendency(playerDoubleStats.getTendency());

		playerDto.setPlayerDoubleStats(playerDoubleStatsDto);
	}

}
