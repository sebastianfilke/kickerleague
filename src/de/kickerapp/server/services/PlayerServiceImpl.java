package de.kickerapp.server.services;

import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.PlayerService;
import de.kickerapp.server.dto.Player;
import de.kickerapp.server.persistence.PMFactory;

/**
 * The server side implementation of the RPC service.
 */
public class PlayerServiceImpl extends RemoteServiceServlet implements PlayerService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 3828516373887924192L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Player createPlayer(Player player) throws IllegalArgumentException {
		PersistenceManager pm = PMFactory.get().getPersistenceManager();

		try {
			player = pm.makePersistent(player);

		} finally {
			pm.close();
		}
		return player;
	}

}
