package de.kickerapp.server.services;

import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.PlayerService;
import de.kickerapp.server.dto.Player;
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

		Player newPlayer = new Player();
		newPlayer.setFirstName(player.getFirstName());
		newPlayer.setLastName(player.getLastName());
		newPlayer.setEMail(player.getEMail());
		newPlayer.setNickName(player.getNickName());

		try {
			newPlayer = pm.makePersistent(newPlayer);

		} finally {
			pm.close();
		}
		return player;
	}

}
