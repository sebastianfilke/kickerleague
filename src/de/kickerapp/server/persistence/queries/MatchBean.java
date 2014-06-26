package de.kickerapp.server.persistence.queries;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import de.kickerapp.server.dao.DoubleMatch;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Klasse für den Zugriff auf Instanzen von Spielen.
 * 
 * @author Sebastian Filke
 */
public final class MatchBean {

	/**
	 * Liefert die nächste Spielnummer für die Spiele.
	 * 
	 * @return Die nächste Spielnummer als <code>int</code>.
	 */
	public static int getNextMatchNumber() {
		final int sizeSingleMatches = PMFactory.getEntitySize(SingleMatch.class.getName());
		final int sizeDoubleMatches = PMFactory.getEntitySize(DoubleMatch.class.getName());

		final int matchNumber = sizeSingleMatches + sizeDoubleMatches + 1;

		return matchNumber;
	}

	/**
	 * Liefert sämtliche Einzelspiele für den übergebenen Spieler.
	 * 
	 * @param playerDto Der Spieler.
	 * @return Die Einzelspiele.
	 */
	public static List<SingleMatch> getSingleMatchesForPlayer(final PlayerDto playerDto) {
		final List<SingleMatch> list = new ArrayList<SingleMatch>();

		list.addAll(getSingleMatchesForPlayer1(playerDto));
		list.addAll(getSingleMatchesForPlayer2(playerDto));

		return list;
	}

	/**
	 * Liefert sämtliche Einzelspiele für den übergebenen Spieler, falls es sich um den ersten Spieler handelt.
	 * 
	 * @param playerDto Der Spieler.
	 * @return Die Einzelspiele.
	 */
	@SuppressWarnings("unchecked")
	private static List<SingleMatch> getSingleMatchesForPlayer1(final PlayerDto playerDto) {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();
		// pm.getFetchPlan().addGroup(MatchPlan.BOTHPLAYERS);

		final Query query = pm.newQuery(SingleMatch.class);
		query.setFilter("player1 == :id");

		List<SingleMatch> list = new ArrayList<SingleMatch>();
		try {
			list = (List<SingleMatch>) query.execute(playerDto.getId());
			list = (List<SingleMatch>) pm.detachCopyAll(list);
		} finally {
			query.closeAll();
			pm.close();
		}
		return list;
	}

	/**
	 * Liefert sämtliche Einzelspiele für den übergebenen Spieler, falls es sich um den zweiten Spieler handelt.
	 * 
	 * @param playerDto Der Spieler.
	 * @return Die Einzelspiele.
	 */
	@SuppressWarnings("unchecked")
	private static List<SingleMatch> getSingleMatchesForPlayer2(final PlayerDto playerDto) {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();
		// pm.getFetchPlan().addGroup(MatchPlan.BOTHPLAYERS);

		final Query query = pm.newQuery(SingleMatch.class);
		query.setFilter("player2 == :id");

		List<SingleMatch> list = new ArrayList<SingleMatch>();
		try {
			list = (List<SingleMatch>) query.execute(playerDto.getId());
			list = (List<SingleMatch>) pm.detachCopyAll(list);
		} finally {
			query.closeAll();
			pm.close();
		}
		return list;
	}

}
