package de.kickerapp.server;

import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.PlayerService;
import de.kickerapp.shared.persistence.PMFactory;
import de.kickerapp.shared.player.Player;

/**
 * The server side implementation of the RPC service.
 */
public class PlayerServiceImpl extends RemoteServiceServlet implements
		PlayerService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -2286450096364102814L;

	/**
	 * {@inheritDoc}
	 */
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
