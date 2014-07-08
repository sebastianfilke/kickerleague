package de.kickerapp.server.services;

import java.util.Comparator;

import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.PlayerDoubleStats;
import de.kickerapp.server.dao.PlayerSingleStats;
import de.kickerapp.shared.dto.PlayerDoubleStatsDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.PlayerSingleStatsDto;

/**
 * Hilfsklasse für den Dienst zur Verarbeitung von Spielern im Klienten.
 * 
 * @author Sebastian Filke
 */
public class PlayerServiceHelper {

	/**
	 * Comparator zur Sortierung der Spieler nach Nachnamen.
	 * 
	 * @author Sebastian Filke
	 */
	protected static class PlayerNameComparator implements Comparator<PlayerDto> {

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
	protected static class PlayerTableComparator implements Comparator<PlayerDto> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(PlayerDto p1, PlayerDto p2) {
			int comp = 0;
			if (p1.getPlayerSingleStatsDto() != null && p2.getPlayerSingleStatsDto() != null) {
				if (p1.getPlayerSingleStatsDto().getCurTablePlace() == 0) {
					comp = 1;
				} else if (p2.getPlayerSingleStatsDto().getCurTablePlace() == 0) {
					comp = -1;
				} else {
					comp = p1.getPlayerSingleStatsDto().getCurTablePlace().compareTo(p2.getPlayerSingleStatsDto().getCurTablePlace());
				}
			} else if (p1.getPlayerDoubleStatsDto() != null && p2.getPlayerDoubleStatsDto() != null) {
				if (p1.getPlayerDoubleStatsDto().getCurTablePlace() == 0) {
					comp = 1;
				} else if (p2.getPlayerDoubleStatsDto().getCurTablePlace() == 0) {
					comp = -1;
				} else {
					comp = p1.getPlayerDoubleStatsDto().getCurTablePlace().compareTo(p2.getPlayerDoubleStatsDto().getCurTablePlace());
				}
			}
			return comp;
		}
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbPlayer Die Objekt-Datenklasse.
	 * @return Die Client-Datenklasse ohne Spielstatistiken.
	 */
	protected static PlayerDto createPlayerDto(Player dbPlayer) {
		final PlayerDto playerDto = new PlayerDto(dbPlayer.getLastName(), dbPlayer.getFirstName());
		playerDto.setId(dbPlayer.getKey().getId());
		playerDto.setNickName(dbPlayer.getNickName());
		playerDto.setEMail(dbPlayer.getEMail());
		playerDto.setLastMatchDate(dbPlayer.getLastMatchDate());

		return playerDto;
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbPlayer Die Objekt-Datenklasse.
	 * @return Die Client-Datenklasse mit Einzelspiel-Statistik.
	 */
	protected static PlayerDto createPlayerDtoWithSingleStats(Player dbPlayer) {
		final PlayerDto playerDto = createPlayerDto(dbPlayer);
		playerDto.setPlayerSingleStatsDto(createPlayerSingleStats(dbPlayer));

		return playerDto;
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbPlayer Die Objekt-Datenklasse.
	 * @return Die Client-Datenklasse mit Doppelspiel-Statistik.
	 */
	protected static PlayerDto createPlayerDtoWithDoubleStats(Player dbPlayer) {
		final PlayerDto playerDto = createPlayerDto(dbPlayer);
		playerDto.setPlayerDoubleStatsDto(createPlayerDoubleStats(dbPlayer));

		return playerDto;
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbPlayer Die Objekt-Datenklasse.
	 * @return Die Client-Datenklasse mit Einzelspiel- und Doppelspiel-Statistik.
	 */
	protected static PlayerDto createPlayerDtoWithAllStats(Player dbPlayer) {
		final PlayerDto playerDto = createPlayerDto(dbPlayer);
		playerDto.setPlayerSingleStatsDto(createPlayerSingleStats(dbPlayer));
		playerDto.setPlayerDoubleStatsDto(createPlayerDoubleStats(dbPlayer));

		return playerDto;
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbPlayer Die Objekt-Datenklasse.
	 * @return Die Client-Datenklasse.
	 */
	private static PlayerSingleStatsDto createPlayerSingleStats(Player dbPlayer) {
		final PlayerSingleStats dbPlayerSingleStats = dbPlayer.getPlayerSingleStats();

		final PlayerSingleStatsDto playerSingleStatsDto = new PlayerSingleStatsDto();
		playerSingleStatsDto.setId(dbPlayerSingleStats.getKey().getId());
		playerSingleStatsDto.setWins(dbPlayerSingleStats.getWins());
		playerSingleStatsDto.setDefeats(dbPlayerSingleStats.getDefeats());
		playerSingleStatsDto.setWinSets(dbPlayerSingleStats.getWinSets());
		playerSingleStatsDto.setLostSets(dbPlayerSingleStats.getLostSets());
		playerSingleStatsDto.setShotGoals(dbPlayerSingleStats.getShotGoals());
		playerSingleStatsDto.setGetGoals(dbPlayerSingleStats.getGetGoals());
		playerSingleStatsDto.setPrevTablePlace(dbPlayerSingleStats.getPrevTablePlace());
		playerSingleStatsDto.setCurTablePlace(dbPlayerSingleStats.getCurTablePlace());
		playerSingleStatsDto.setLastMatchPoints(dbPlayerSingleStats.getLastMatchPoints());
		playerSingleStatsDto.setPoints(dbPlayerSingleStats.getPoints());
		playerSingleStatsDto.setTendency(dbPlayerSingleStats.getTendency());

		return playerSingleStatsDto;
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbPlayer Die Objekt-Datenklasse.
	 * @return Die Client-Datenklasse.
	 */
	private static PlayerDoubleStatsDto createPlayerDoubleStats(Player dbPlayer) {
		final PlayerDoubleStats dbPlayerDoubleStats = dbPlayer.getPlayerDoubleStats();

		final PlayerDoubleStatsDto playerDoubleStatsDto = new PlayerDoubleStatsDto();
		playerDoubleStatsDto.setId(dbPlayerDoubleStats.getKey().getId());
		playerDoubleStatsDto.setWins(dbPlayerDoubleStats.getWins());
		playerDoubleStatsDto.setDefeats(dbPlayerDoubleStats.getDefeats());
		playerDoubleStatsDto.setWinSets(dbPlayerDoubleStats.getWinSets());
		playerDoubleStatsDto.setLostSets(dbPlayerDoubleStats.getLostSets());
		playerDoubleStatsDto.setShotGoals(dbPlayerDoubleStats.getShotGoals());
		playerDoubleStatsDto.setGetGoals(dbPlayerDoubleStats.getGetGoals());
		playerDoubleStatsDto.setPrevTablePlace(dbPlayerDoubleStats.getPrevTablePlace());
		playerDoubleStatsDto.setCurTablePlace(dbPlayerDoubleStats.getCurTablePlace());
		playerDoubleStatsDto.setLastMatchPoints(dbPlayerDoubleStats.getLastMatchPoints());
		playerDoubleStatsDto.setPoints(dbPlayerDoubleStats.getPoints());
		playerDoubleStatsDto.setTendency(dbPlayerDoubleStats.getTendency());

		return playerDoubleStatsDto;
	}

}
