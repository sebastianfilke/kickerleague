package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.PlayerService;
import de.kickerapp.server.entity.Player;
import de.kickerapp.server.persistence.PMFactory;
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
	public PlayerDto createPlayer(PlayerDto player) throws IllegalArgumentException {
		Player newPlayer = new Player();
		newPlayer.setLastName(player.getLastName());
		newPlayer.setFirstName(player.getFirstName());
		newPlayer.setNickName(player.getNickName());
		newPlayer.setEMail(player.getEMail());

		newPlayer = PMFactory.persistObject(newPlayer);
		player.setId(newPlayer.getKey().getId());

		return player;
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PlayerDto> getAllPlayers() throws IllegalArgumentException {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final ArrayList<PlayerDto> players = new ArrayList<PlayerDto>();

		final Query query = pm.newQuery(Player.class);
		query.setOrdering("playerStats.points desc");

		final List<Player> dbPlayers = (List<Player>) query.execute();
		for (Player dbPlayer : dbPlayers) {
			dbPlayer = pm.detachCopy(dbPlayer);
			final PlayerDto player = PlayerServiceHelper.createPlayer(dbPlayer);

			players.add(player);
		}
		return players;
	}

}
