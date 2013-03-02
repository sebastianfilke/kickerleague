package de.kickerapp.server.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.PlayerService;
import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.PlayerDoubleStats;
import de.kickerapp.server.entity.PlayerSingleStats;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Dienst zur Verarbeitung von Spielern im Clienten.
 * 
 * @author Sebastian Filke
 */
public class PlayerServiceImpl extends RemoteServiceServlet implements PlayerService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 3828516373887924192L;

	/**
	 * Comparator zur Sortierung der Betriebsmittelarten.
	 * 
	 * @author Sebastian Filke, GIGATRONIK München GmbH
	 */
	private class PlayerTableComparator implements Comparator<PlayerDto>, Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 7262644006482460970L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(PlayerDto p1, PlayerDto p2) {
			int comp = 0;
			if (p1.getPlayerSingleStats() != null && p2.getPlayerSingleStats() != null) {
				if (p1.getPlayerSingleStats().getSingleCurTablePlace() == 0) {
					comp = 1;
				} else if (p2.getPlayerSingleStats().getSingleCurTablePlace() == 0) {
					comp = -1;
				} else {
					comp = p1.getPlayerSingleStats().getSingleCurTablePlace().compareTo(p2.getPlayerSingleStats().getSingleCurTablePlace());
				}
			} else if (p1.getPlayerDoubleStats() != null && p2.getPlayerDoubleStats() != null) {
				if (p1.getPlayerDoubleStats().getDoubleCurTablePlace() == 0) {
					comp = 1;
				} else if (p2.getPlayerDoubleStats().getDoubleCurTablePlace() == 0) {
					comp = -1;
				} else {
					comp = p1.getPlayerDoubleStats().getDoubleCurTablePlace().compareTo(p2.getPlayerDoubleStats().getDoubleCurTablePlace());
				}
			}
			return comp;
		}
	}

	/**
	 * Comparator zur Sortierung der Betriebsmittelarten.
	 * 
	 * @author Sebastian Filke, GIGATRONIK München GmbH
	 */
	private class PlayerComparator implements Comparator<PlayerDto>, Serializable {

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
	 * {@inheritDoc}
	 */
	@Override
	public PlayerDto createPlayer(PlayerDto player) throws IllegalArgumentException {
		Player newPlayer = new Player();
		final int playerId = PMFactory.getNextId(Player.class);
		final Key playerKey = KeyFactory.createKey(Player.class.getSimpleName(), playerId);
		newPlayer.setKey(playerKey);

		newPlayer.setLastName(player.getLastName());
		newPlayer.setFirstName(player.getFirstName());
		newPlayer.setNickName(player.getNickName());
		newPlayer.setEMail(player.getEMail());

		PlayerSingleStats newSingleStats = new PlayerSingleStats();
		final int playerSingleStatsId = PMFactory.getNextId(PlayerSingleStats.class);
		final Key playerSingleStatsKey = KeyFactory.createKey(PlayerSingleStats.class.getSimpleName(), playerSingleStatsId);
		newSingleStats.setKey(playerSingleStatsKey);

		newSingleStats = PMFactory.persistObject(newSingleStats);

		PlayerDoubleStats newDoubleStats = new PlayerDoubleStats();
		final int playerDoubleStatsId = PMFactory.getNextId(PlayerDoubleStats.class);
		final Key playerDoubleStatsKey = KeyFactory.createKey(PlayerDoubleStats.class.getSimpleName(), playerDoubleStatsId);
		newDoubleStats.setKey(playerDoubleStatsKey);

		newDoubleStats = PMFactory.persistObject(newDoubleStats);

		newPlayer.setPlayerSingleStats(newSingleStats.getKey().getId());
		newPlayer.setPlayerDoubleStats(newDoubleStats.getKey().getId());

		newPlayer = PMFactory.persistObject(newPlayer);

		return player;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<PlayerDto> getAllPlayers(MatchType matchType) throws IllegalArgumentException {
		final ArrayList<PlayerDto> playerDtos = new ArrayList<PlayerDto>();

		final List<Player> dbPlayers = PMFactory.getList(Player.class);
		for (Player dbPlayer : dbPlayers) {
			final PlayerDto player = PlayerServiceHelper.createPlayer(dbPlayer, matchType);

			playerDtos.add(player);
		}
		if (matchType == MatchType.UNKNOWN) {
			Collections.sort(playerDtos, new PlayerComparator());
		} else {
			Collections.sort(playerDtos, new PlayerTableComparator());
		}
		return playerDtos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerDto updatePlayer(PlayerDto player) throws IllegalArgumentException {
		Player dbPlayer = PMFactory.getObjectById(Player.class, player.getId());

		dbPlayer.setLastName(player.getLastName());
		dbPlayer.setFirstName(player.getFirstName());
		dbPlayer.setNickName(player.getNickName());
		dbPlayer.setEMail(player.getEMail());

		dbPlayer = PMFactory.persistObject(dbPlayer);

		return player;
	}

}
