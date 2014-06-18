package de.kickerapp.server.services;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.PlayerDoubleStats;
import de.kickerapp.server.dao.PlayerSingleStats;
import de.kickerapp.shared.dto.PlayerDoubleStatsDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.PlayerSingleStatsDto;

/**
 * Hilfsklasse für den Dienst zur Verarbeitung von Spielern im Clienten.
 * 
 * @author Sebastian Filke
 */
public class PlayerServiceHelper {

	/**
	 * Comparator zur Sortierung der Spieler nach Nachnamen.
	 * 
	 * @author Sebastian Filke
	 */
	protected static class PlayerNameComparator implements Comparator<PlayerDto>, Serializable {

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
	 * Liefert den Spieler anhand der Db-Id.
	 * 
	 * @param id Die Db-Id des Spielers.
	 * @param dbPlayers Die Objekt-Datenklasse-Liste aller Spieler.
	 * @return Der Spieler.
	 */
	protected static Player getPlayerById(Long id, List<Player> dbPlayers) {
		Player dbPlayer = null;
		for (Player dbCurrentPlayer : dbPlayers) {
			if (dbCurrentPlayer.getKey().getId() == id) {
				dbPlayer = dbCurrentPlayer;
				break;
			}
		}
		return dbPlayer;
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
		setPlayerSingleStats(dbPlayer, playerDto);

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
		setPlayerDoubleStats(dbPlayer, playerDto);

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
		setPlayerSingleStats(dbPlayer, playerDto);
		setPlayerDoubleStats(dbPlayer, playerDto);

		return playerDto;
	}

	/**
	 * Setzt die die Einzelspiel-Statistik eines Spielers.
	 * 
	 * @param dbPlayer Die Objekt-Datenklasse.
	 * @param playerDto Die Client-Datenklasse.
	 */
	private static void setPlayerSingleStats(Player dbPlayer, final PlayerDto playerDto) {
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

		playerDto.setPlayerSingleStatsDto(playerSingleStatsDto);
	}

	/**
	 * Setzt die die Doppelspiel-Statistik eines Spielers.
	 * 
	 * @param dbPlayer Die Objekt-Datenklasse.
	 * @param playerDto Die Client-Datenklasse.
	 */
	private static void setPlayerDoubleStats(Player dbPlayer, PlayerDto playerDto) {
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

		playerDto.setPlayerDoubleStatsDto(playerDoubleStatsDto);
	}

}
