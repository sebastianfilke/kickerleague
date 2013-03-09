package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import de.kickerapp.client.services.PagingService;
import de.kickerapp.server.entity.Player;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.services.PlayerServiceHelper.PlayerNameComparator;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Dienst zur Verarbeitung der PagingComboBoxen im Clienten.
 * 
 * @author Sebastian Filke
 */
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

		final String[] queries = query.split(" ");
		for (PlayerDto player : result) {
			for (String curQuery : queries) {
				if (player.getLastName().toLowerCase().contains(curQuery.toLowerCase()) || player.getFirstName().toLowerCase().contains(curQuery.toLowerCase())
						|| player.getNickName().toLowerCase().contains(curQuery.toLowerCase())) {
					if (!filteredResult.contains(player)) {
						filteredResult.add(player);
					}
				}
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

	public ArrayList<PlayerDto> getAllPlayers() {
		final ArrayList<PlayerDto> playerDtos = new ArrayList<PlayerDto>();

		final List<Player> dbPlayers = PMFactory.getList(Player.class);
		for (Player dbPlayer : dbPlayers) {
			final PlayerDto player = PlayerServiceHelper.createDtoPlayer(dbPlayer, MatchType.BOTH);

			playerDtos.add(player);
		}
		Collections.sort(playerDtos, new PlayerNameComparator());
		return playerDtos;
	}

}
