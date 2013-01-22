package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.PlayerService;
import de.kickerapp.server.dto.Player;
import de.kickerapp.server.dto.Player.PlayerStats;
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
		Player newPlayer = new Player();
		newPlayer.setLastName(player.getLastName());
		newPlayer.setFirstName(player.getFirstName());
		newPlayer.setNickName(player.getNickName());
		newPlayer.setEMail(player.getEMail());

		newPlayer = PMFactory.insertObject(newPlayer);
		player.setServiceObject(Icebox.freeze(newPlayer));
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
		query.setOrdering("stats.points desc");

		final List<Player> dbPlayers = (List<Player>) query.execute();
		for (Player dbPlayer : dbPlayers) {
			dbPlayer = pm.detachCopy(dbPlayer);
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

		final PlayerStats playerStats = dbPlayer.getPlayerStats();

		// Single Match
		final int wins = playerStats.getSingleWins();
		final int losses = playerStats.getSingleLosses();

		player.setSingleMatches(wins + losses);
		player.setSingleWins(wins);
		player.setSingleLosses(losses);
		player.setSingleGoals(playerStats.getSingleShotGoals() + ":" + playerStats.getSingleGetGoals());

		final int goalDifference = playerStats.getSingleShotGoals() - playerStats.getSingleGetGoals();
		if (goalDifference >= 0) {
			player.setSingleGoalDifference("+" + Integer.toString(goalDifference));
		} else {
			player.setSingleGoalDifference(Integer.toString(goalDifference));
		}

		// Double Match
		player.setDoubleWins(playerStats.getDoubleWins());
		player.setDoubleLosses(playerStats.getDoubleLosses());
		player.setDoubleGoals(playerStats.getDoubleShotGoals() + ":" + playerStats.getDoubleGetGoals());
		player.setPoints(playerStats.getPoints());
		player.setTendency(playerStats.getTendency());

		player.setServiceObject(Icebox.freeze(dbPlayer));

		return player;
	}

}
