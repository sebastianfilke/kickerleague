package de.kickerapp.server.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

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
	private class PlayerComparator implements Comparator<PlayerDto>, Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = 7262644006482460970L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(PlayerDto p1, PlayerDto p2) {
			if (p1.getPlayerSingleStats() != null && p2.getPlayerSingleStats() != null) {
				return p2.getPlayerSingleStats().getSinglePoints().compareTo(p1.getPlayerSingleStats().getSinglePoints());
			} else if (p1.getPlayerDoubleStats() != null && p2.getPlayerDoubleStats() != null) {
				return p2.getPlayerDoubleStats().getDoublePoints().compareTo(p1.getPlayerDoubleStats().getDoublePoints());
			}
			return 0;
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
	@SuppressWarnings("unchecked")
	public ArrayList<PlayerDto> getAllPlayers(MatchType matchType) throws IllegalArgumentException {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final ArrayList<PlayerDto> playerDtos = new ArrayList<PlayerDto>();

		final Query query = pm.newQuery(Player.class);

		final List<Player> dbPlayers = (List<Player>) query.execute();
		for (Player dbPlayer : dbPlayers) {
			dbPlayer = pm.detachCopy(dbPlayer);
			final PlayerDto player = PlayerServiceHelper.createPlayer(dbPlayer, matchType);

			playerDtos.add(player);
		}
		Collections.sort(playerDtos, new PlayerComparator());
		return playerDtos;
	}

}
