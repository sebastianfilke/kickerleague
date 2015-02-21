package de.kickerapp.server.persistence.queries;

import java.util.List;

import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.fetchplans.PlayerPlan;
import de.kickerapp.server.persistence.PMFactory;

/**
 * Klasse für den Zugriff auf Instanzen von Spielern.
 * 
 * @author Sebastian Filke
 */
public class PlayerBean {

	/**
	 * Liefert alle Spieler welche nicht gesperrt sind.
	 * 
	 * @return Alle Spieler welche nicht gesperrt sind.
	 */
	public static List<Player> getAllUnlockedPlayers() {
		final QueryContainer conPlayer = new QueryContainer();
		conPlayer.setPlans(PlayerPlan.BOTHSTATS);
		conPlayer.setQuery("locked != true");

		final List<Player> dbPlayers = PMFactory.getList(Player.class, conPlayer);

		return dbPlayers;
	}

}
