package de.kickerapp.server.services;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.PlayerService;
import de.kickerapp.server.dto.Player;
import de.kickerapp.server.persistence.Icebox;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.match.PlayerDto;

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
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();
		final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		final Transaction txn = datastore.beginTransaction();

		try {
			Player newPlayer = new Player();
			newPlayer.setLastName(player.getLastName());
			newPlayer.setFirstName(player.getFirstName());
			newPlayer.setNickName(player.getNickName());
			newPlayer.setEMail(player.getEMail());

			newPlayer = pm.makePersistent(newPlayer);
			player.setServiceObject(Icebox.freeze(newPlayer));

			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		return player;
	}

}
