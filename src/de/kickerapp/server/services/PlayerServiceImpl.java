package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.PlayerService;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.PlayerDoubleStats;
import de.kickerapp.server.dao.PlayerSingleStats;
import de.kickerapp.server.dao.fetchplans.PlayerPlan;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.services.PlayerServiceHelper.PlayerNameComparator;
import de.kickerapp.server.services.PlayerServiceHelper.PlayerTableComparator;
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
	 * {@inheritDoc}
	 */
	@Override
	public PlayerDto createPlayer(PlayerDto playerDto) throws IllegalArgumentException {
		final Player dbPlayer = new Player();
		final int playerId = PMFactory.getNextId(Player.class.getName());
		final Key playerKey = KeyFactory.createKey(Player.class.getSimpleName(), playerId);
		dbPlayer.setKey(playerKey);

		dbPlayer.setLastName(playerDto.getLastName());
		dbPlayer.setFirstName(playerDto.getFirstName());
		dbPlayer.setNickName(playerDto.getNickName());
		dbPlayer.setEMail(playerDto.getEMail());

		final PlayerSingleStats dbPlayerSingleStats = new PlayerSingleStats();
		final int playerSingleStatsId = PMFactory.getNextId(PlayerSingleStats.class.getName());
		final Key playerSingleStatsKey = KeyFactory.createKey(dbPlayer.getKey(), PlayerSingleStats.class.getSimpleName(), playerSingleStatsId);
		dbPlayerSingleStats.setKey(playerSingleStatsKey);

		final PlayerDoubleStats dbPlayerDoubleStats = new PlayerDoubleStats();
		final int playerDoubleStatsId = PMFactory.getNextId(PlayerDoubleStats.class.getName());
		final Key playerDoubleStatsKey = KeyFactory.createKey(dbPlayer.getKey(), PlayerDoubleStats.class.getSimpleName(), playerDoubleStatsId);
		dbPlayerDoubleStats.setKey(playerDoubleStatsKey);

		dbPlayer.setPlayerSingleStats(dbPlayerSingleStats);
		dbPlayer.setPlayerDoubleStats(dbPlayerDoubleStats);

		PMFactory.persistObject(dbPlayer);

		return playerDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<PlayerDto> getAllPlayers(MatchType matchType) throws IllegalArgumentException {
		final ArrayList<PlayerDto> playerDtos = new ArrayList<PlayerDto>();

		List<Player> dbPlayers = null;
		if (matchType == MatchType.SINGLE) {
			dbPlayers = PMFactory.getList(Player.class, PlayerPlan.PLAYERSINGLESTATS);
		} else if (matchType == MatchType.DOUBLE) {
			dbPlayers = PMFactory.getList(Player.class, PlayerPlan.PLAYERDOUBLESTATS);
		} else if (matchType == MatchType.BOTH) {
			dbPlayers = PMFactory.getList(Player.class, PlayerPlan.ALLSTATS);
		} else {
			dbPlayers = PMFactory.getList(Player.class);
		}

		for (Player dbPlayer : dbPlayers) {
			PlayerDto player = null;
			if (matchType == MatchType.SINGLE) {
				player = PlayerServiceHelper.createPlayerDtoWithSingleStats(dbPlayer);
			} else if (matchType == MatchType.DOUBLE) {
				player = PlayerServiceHelper.createPlayerDtoWithDoubleStats(dbPlayer);
			} else if (matchType == MatchType.BOTH) {
				player = PlayerServiceHelper.createPlayerDtoWithAllStats(dbPlayer);
			} else {
				player = PlayerServiceHelper.createPlayerDto(dbPlayer);
			}

			playerDtos.add(player);
		}
		if (matchType == MatchType.NONE) {
			Collections.sort(playerDtos, new PlayerNameComparator());
		} else {
			Collections.sort(playerDtos, new PlayerTableComparator());
		}
		return playerDtos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerDto updatePlayer(PlayerDto playerDto) throws IllegalArgumentException {
		final Player dbPlayer = PMFactory.getObjectById(Player.class, playerDto.getId());

		dbPlayer.setLastName(playerDto.getLastName());
		dbPlayer.setFirstName(playerDto.getFirstName());
		dbPlayer.setNickName(playerDto.getNickName());
		dbPlayer.setEMail(playerDto.getEMail());

		PMFactory.persistObject(dbPlayer);

		return playerDto;
	}

}
