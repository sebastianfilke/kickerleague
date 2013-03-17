package de.kickerapp.server.services;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.PlayerDoubleStats;
import de.kickerapp.server.entity.PlayerSingleStats;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.common.MatchType;
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
	 * @param matchType Der Spieltyp für welchen die Daten erstellt werden sollen.
	 * @return Die Client-Datenklasse.
	 */
	public static PlayerDto createDtoPlayer(Player dbPlayer, MatchType matchType) {
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

	/**
	 * Setzt die die Einzelspiel-Statistik eines Spielers.
	 * 
	 * @param dbPlayer Die Objekt-Datenklasse.
	 * @param playerDto Die Client-Datenklasse.
	 */
	private static void setPlayerSingleStats(Player dbPlayer, final PlayerDto playerDto) {
		final PlayerSingleStats dbPlayerSingleStats = PMFactory.getObjectById(PlayerSingleStats.class, dbPlayer.getPlayerSingleStats());

		// Single Match
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
	 */
	private static void setPlayerDoubleStats(Player dbPlayer, PlayerDto playerDto) {
		final PlayerDoubleStats dbPlayerDoubleStats = PMFactory.getObjectById(PlayerDoubleStats.class, dbPlayer.getPlayerDoubleStats());

		// Double Match
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
