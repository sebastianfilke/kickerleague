package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import de.kickerapp.client.services.PagingService;
import de.kickerapp.server.dto.Player;
import de.kickerapp.server.persistence.Icebox;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.match.PlayerDto;

public class PagingServiceImpl extends RemoteServiceServlet implements PagingService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1711104572514007282L;

	@Override
	public PagingLoadResult<PlayerDto> getPagedPlayers(String query, PagingLoadConfig config) throws IllegalArgumentException {
		final ArrayList<PlayerDto> result = getAllPlayers();
		final ArrayList<PlayerDto> filteredResult = createFilteredResult(query, result);
		final ArrayList<PlayerDto> sublistResult = createSubListResult(config, filteredResult);

		return new PagingLoadResultBean<PlayerDto>(sublistResult, result.size(), config.getOffset());
	}

	private ArrayList<PlayerDto> createFilteredResult(String query, ArrayList<PlayerDto> result) {
		final ArrayList<PlayerDto> filteredResult = new ArrayList<PlayerDto>();

		for (PlayerDto player : result) {
			if (player.getLastName().toLowerCase().contains(query.toLowerCase()) || player.getFirstName().toLowerCase().contains(query.toLowerCase())
					|| player.getNickName().toLowerCase().contains(query.toLowerCase())) {
				filteredResult.add(player);
			}
		}
		return filteredResult;
	}

	private ArrayList<PlayerDto> createSubListResult(PagingLoadConfig config, ArrayList<PlayerDto> filteredResult) {
		final ArrayList<PlayerDto> sublist = new ArrayList<PlayerDto>();

		final int start = config.getOffset();
		int limit = filteredResult.size();
		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}
		for (int i = config.getOffset(); i < limit; i++) {
			sublist.add(filteredResult.get(i));
		}
		return sublist;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<PlayerDto> getAllPlayers() {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final ArrayList<PlayerDto> players = new ArrayList<PlayerDto>();

		final Query query = pm.newQuery(Player.class);

		final List<Player> dbPlayers = (List<Player>) query.execute();
		for (Player dbPlayer : dbPlayers) {
			final PlayerDto player = createPlayer(dbPlayer);

			players.add(player);
		}
		return players;
	}

	private PlayerDto createPlayer(Player dbPlayer) {
		final PlayerDto player = new PlayerDto(dbPlayer.getLastName(), dbPlayer.getFirstName());
		player.setId(dbPlayer.getKey().getId());
		player.setNickName(dbPlayer.getNickName());
		player.setEMail(dbPlayer.getEMail());
		player.setServiceObject(Icebox.freeze(dbPlayer));

		return player;
	}

}
