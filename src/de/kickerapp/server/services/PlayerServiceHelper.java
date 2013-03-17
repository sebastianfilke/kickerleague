package de.kickerapp.server.services;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.PlayerDoubleStats;
import de.kickerapp.server.entity.PlayerSingleStats;
import de.kickerapp.server.entity.Stats;
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
	 * Liefert die Einzelspiel- bzw. Doppelspiel-Statistik eines Spieler anhand der Db-Id.
	 * 
	 * @param id Die Db-Id der Einzelspiel- bzw. Doppelspiel-Statistik des Spieler.
	 * @param dbStats Die Objekt-Datenklasse-Liste aller Einzelspiel- bzw. Doppelspiel-Statistiken.
	 * @return Die Einzelspiel- bzw. Doppelspiel-Statistik.
	 */
	protected static Stats getPlayerStatsById(Long id, List<? extends Stats> dbStats) {
		Stats dbStat = null;
		for (Stats dbCurrentStats : dbStats) {
			if (dbCurrentStats.getKey().getId() == id) {
				dbStat = dbCurrentStats;
				break;
			}
		}
		return dbStat;
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
	 * @param dbPlayerSingleStats Die Objekt-Datenklasse-Liste aller Einzelspiel-Statistiken.
	 * @return Die Client-Datenklasse mit Einzelspiel-Statistik.
	 */
	protected static PlayerDto createPlayerDtoWithSingleStats(Player dbPlayer, List<PlayerSingleStats> dbPlayerSingleStats) {
		final PlayerDto playerDto = createPlayerDto(dbPlayer);
		setPlayerSingleStats(dbPlayer, playerDto, dbPlayerSingleStats);

		return playerDto;
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbPlayer Die Objekt-Datenklasse.
	 * @param dbPlayerDoubleStats Die Objekt-Datenklasse-Liste aller Doppelspiel-Statistiken.
	 * @return Die Client-Datenklasse mit Doppelspiel-Statistik.
	 */
	protected static PlayerDto createPlayerDtoWithDoubleStats(Player dbPlayer, List<PlayerDoubleStats> dbPlayerDoubleStats) {
		final PlayerDto playerDto = createPlayerDto(dbPlayer);
		setPlayerDoubleStats(dbPlayer, playerDto, dbPlayerDoubleStats);

		return playerDto;
	}

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbPlayer Die Objekt-Datenklasse.
	 * @param dbSingleStats Die Objekt-Datenklasse-Liste aller Einzelspiel-Statistiken.
	 * @param dbPlayerDoubleStats Die Objekt-Datenklasse-Liste aller Doppelspiel-Statistiken.
	 * @return Die Client-Datenklasse mit Einzelspiel- und Doppelspiel-Statistik.
	 */
	protected static PlayerDto createPlayerDtoWithAllStats(Player dbPlayer, List<PlayerSingleStats> dbSingleStats, List<PlayerDoubleStats> dbPlayerDoubleStats) {
		final PlayerDto playerDto = createPlayerDto(dbPlayer);
		setPlayerSingleStats(dbPlayer, playerDto, dbSingleStats);
		setPlayerDoubleStats(dbPlayer, playerDto, dbPlayerDoubleStats);

		return playerDto;
	}

	/**
	 * Setzt die die Einzelspiel-Statistik eines Spielers.
	 * 
	 * @param dbPlayer Die Objekt-Datenklasse.
	 * @param playerDto Die Client-Datenklasse.
	 * @param dbSingleStats Die Objekt-Datenklasse-Liste aller Einzelspiel-Statistiken.
	 */
	private static void setPlayerSingleStats(Player dbPlayer, final PlayerDto playerDto, List<PlayerSingleStats> dbSingleStats) {
		final PlayerSingleStats dbPlayerSingleStats = (PlayerSingleStats) getPlayerStatsById(dbPlayer.getPlayerSingleStats(), dbSingleStats);

		final PlayerSingleStatsDto playerSingleStatsDto = new PlayerSingleStatsDto();

		playerSingleStatsDto.setWins(dbPlayerSingleStats.getWins());
		playerSingleStatsDto.setLosses(dbPlayerSingleStats.getLosses());
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
	 * @param dbDoubleStats Die Objekt-Datenklasse-Liste aller Doppelspiel-Statistiken.
	 */
	private static void setPlayerDoubleStats(Player dbPlayer, PlayerDto playerDto, List<PlayerDoubleStats> dbDoubleStats) {
		final PlayerDoubleStats dbPlayerDoubleStats = (PlayerDoubleStats) getPlayerStatsById(dbPlayer.getPlayerDoubleStats(), dbDoubleStats);

		final PlayerDoubleStatsDto playerDoubleStatsDto = new PlayerDoubleStatsDto();

		playerDoubleStatsDto.setWins(dbPlayerDoubleStats.getWins());
		playerDoubleStatsDto.setLosses(dbPlayerDoubleStats.getLosses());
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
