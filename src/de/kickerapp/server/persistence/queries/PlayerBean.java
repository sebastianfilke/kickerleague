package de.kickerapp.server.persistence.queries;

import java.util.List;

import de.kickerapp.server.dao.Player;
import de.kickerapp.server.persistence.PMFactory;

/**
 * Klasse für den Zugriff auf Instanzen von Spielern.
 * 
 * @author Sebastian Filke
 */
public class PlayerBean {

	/**
	 * Liefert alle Spieler mit mindestens einem Spiel.
	 * 
	 * @return Alle Spieler mit mindestens einem Spiel.
	 */
	public static List<Player> getPlayerWithAtLeastOneMatch() {
		final QueryContainer conPlayer = new QueryContainer();
		conPlayer.setQuery("lastMatchDate != null");

		final List<Player> dbPlayers = PMFactory.getList(Player.class, conPlayer);

		return dbPlayers;
	}

}
