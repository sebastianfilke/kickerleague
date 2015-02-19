package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.PlayerService;
import de.kickerapp.server.dao.DoubleMatchYearAggregation;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.PlayerDoubleStats;
import de.kickerapp.server.dao.PlayerSingleStats;
import de.kickerapp.server.dao.SingleMatchYearAggregation;
import de.kickerapp.server.dao.fetchplans.MatchAggregationPlan;
import de.kickerapp.server.dao.fetchplans.PlayerPlan;
import de.kickerapp.server.persistence.JCacheFactory;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.services.PlayerServiceHelper.PlayerNameComparator;
import de.kickerapp.server.services.PlayerServiceHelper.PlayerTableComparator;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.exception.KickerLeagueException;

/**
 * Dienst zur Verarbeitung von Spielern im Klienten.
 * 
 * @author Sebastian Filke
 */
public class PlayerServiceImpl extends RemoteServiceServlet implements PlayerService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerDto createPlayer(PlayerDto playerDto) throws KickerLeagueException {
		final Player dbPlayer = new Player();
		final int playerId = PMFactory.getNextId(Player.class.getName());
		final Key playerKey = KeyFactory.createKey(Player.class.getSimpleName(), playerId);
		dbPlayer.setKey(playerKey);

		dbPlayer.setLastName(playerDto.getLastName());
		dbPlayer.setFirstName(playerDto.getFirstName());
		dbPlayer.setNickName(playerDto.getNickName());
		dbPlayer.seteMail(playerDto.geteMail());
		dbPlayer.setLocked(playerDto.isLocked());

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

		// Lösche paginierte Spieler aus dem Cache
		JCacheFactory.get().remove(JCacheFactory.PAGEDPLAYERS);

		return playerDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerDto updatePlayer(PlayerDto playerDto) throws KickerLeagueException {
		final Player dbPlayer = PMFactory.getObjectById(Player.class, playerDto.getId());

		dbPlayer.setLastName(playerDto.getLastName());
		dbPlayer.setFirstName(playerDto.getFirstName());
		dbPlayer.setNickName(playerDto.getNickName());
		dbPlayer.seteMail(playerDto.geteMail());
		dbPlayer.setLocked(playerDto.isLocked());

		PMFactory.persistObject(dbPlayer);

		// Lösche paginierte Spieler aus dem Cache
		JCacheFactory.get().remove(JCacheFactory.PAGEDPLAYERS);

		return playerDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<PlayerDto> getAllPlayers(MatchType matchType) throws KickerLeagueException {
		final ArrayList<PlayerDto> playerDtos = new ArrayList<PlayerDto>();

		List<Player> dbPlayers = null;
		if (matchType == MatchType.SINGLE) {
			dbPlayers = PMFactory.getList(Player.class, PlayerPlan.PLAYERSINGLESTATS);
		} else if (matchType == MatchType.DOUBLE) {
			dbPlayers = PMFactory.getList(Player.class, PlayerPlan.PLAYERDOUBLESTATS);
		} else if (matchType == MatchType.BOTH) {
			dbPlayers = PMFactory.getList(Player.class, PlayerPlan.BOTHSTATS);
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
	public HashMap<Integer, ArrayList<PlayerDto>> getSingleMatchYearAggregation() throws KickerLeagueException {
		final List<SingleMatchYearAggregation> dbYearAggregations = PMFactory.getList(SingleMatchYearAggregation.class, MatchAggregationPlan.PLAYER);

		final HashMap<Integer, ArrayList<PlayerDto>> yearAggregations = new HashMap<Integer, ArrayList<PlayerDto>>();
		for (SingleMatchYearAggregation dbYearAggregation : dbYearAggregations) {
			final ArrayList<PlayerDto> playerDtos = yearAggregations.get(dbYearAggregation.getYear());
			if (playerDtos == null) {
				final ArrayList<PlayerDto> newPlayerDtos = new ArrayList<PlayerDto>();
				newPlayerDtos.add(PlayerServiceHelper.createPlayerDto(dbYearAggregation.getPlayer()));
				yearAggregations.put(dbYearAggregation.getYear(), newPlayerDtos);
			} else {
				boolean contains = false;
				for (PlayerDto playerDto : playerDtos) {
					if (playerDto.getId() == dbYearAggregation.getPlayer().getKey().getId()) {
						contains = true;
						break;
					}
				}
				if (!contains) {
					playerDtos.add(PlayerServiceHelper.createPlayerDto(dbYearAggregation.getPlayer()));
				}
			}
		}
		return yearAggregations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<Integer, ArrayList<PlayerDto>> getDoubleMatchYearAggregation() throws KickerLeagueException {
		final List<DoubleMatchYearAggregation> dbYearAggregations = PMFactory.getList(DoubleMatchYearAggregation.class, MatchAggregationPlan.PLAYER);

		final HashMap<Integer, ArrayList<PlayerDto>> yearAggregations = new HashMap<Integer, ArrayList<PlayerDto>>();
		for (DoubleMatchYearAggregation dbYearAggregation : dbYearAggregations) {
			final ArrayList<PlayerDto> playerDtos = yearAggregations.get(dbYearAggregation.getYear());
			if (playerDtos == null) {
				final ArrayList<PlayerDto> newPlayerDtos = new ArrayList<PlayerDto>();
				newPlayerDtos.add(PlayerServiceHelper.createPlayerDto(dbYearAggregation.getPlayer()));
				yearAggregations.put(dbYearAggregation.getYear(), newPlayerDtos);
			} else {
				boolean contains = false;
				for (PlayerDto playerDto : playerDtos) {
					if (playerDto.getId() == dbYearAggregation.getPlayer().getKey().getId()) {
						contains = true;
						break;
					}
				}
				if (!contains) {
					playerDtos.add(PlayerServiceHelper.createPlayerDto(dbYearAggregation.getPlayer()));
				}
			}
		}
		return yearAggregations;
	}

}
