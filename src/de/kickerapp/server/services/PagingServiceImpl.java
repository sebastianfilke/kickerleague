package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import de.kickerapp.client.services.PagingService;
import de.kickerapp.server.dto.Player;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.match.IPlayer;
import de.kickerapp.shared.match.PlayerDto;

public class PagingServiceImpl extends RemoteServiceServlet implements PagingService {

	@Override
	public PagingLoadResultBean<PlayerDto> getPagedPlayers(PagingLoadConfig config) throws IllegalArgumentException {
		final List<IPlayer> result = new ArrayList<IPlayer>();

		final List<PlayerDto> sublistResult = getPlayers();

		PlayerDto dto = new PlayerDto("Basti", "Filke");
		dto.setNickName("test");

		result.add(dto);

		ArrayList<IPlayer> sublist = new ArrayList<IPlayer>();
		int start = config.getOffset();
		int limit = result.size();
		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}
		for (int i = config.getOffset(); i < limit; i++) {
			sublist.add(result.get(i));
		}

		return new PagingLoadResultBean<PlayerDto>(sublistResult, config.getOffset(), result.size());
	}

	private ArrayList<PlayerDto> getPlayers() {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final ArrayList<PlayerDto> players = new ArrayList<PlayerDto>();

		Query query = pm.newQuery(Player.class);

		List<Player> dbPlayers = (List<Player>) query.execute();
		for (Player dbPlayer : dbPlayers) {
			final PlayerDto player = new PlayerDto(dbPlayer.getFirstName(), dbPlayer.getLastName());
			player.setNickName(dbPlayer.getNickName());

			players.add(player);
		}

		return players;
	}

}
