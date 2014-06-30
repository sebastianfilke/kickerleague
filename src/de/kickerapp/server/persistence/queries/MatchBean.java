package de.kickerapp.server.persistence.queries;

import java.util.List;

import de.kickerapp.server.dao.DoubleMatch;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.server.dao.SingleMatchHistory;
import de.kickerapp.server.dao.fetchplans.MatchHistoryPlan;
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
	public static List<SingleMatchHistory> getSingleMatchesForPlayer(final PlayerDto playerDto) {
		final QueryContainer conPlayer = new QueryContainer();
		conPlayer.setPlans(MatchHistoryPlan.PLAYER1);
		conPlayer.setQuery("player1 == :id");
		conPlayer.setOrdering("matchNumber asc");
		conPlayer.setParameter(playerDto.getId());
		final List<SingleMatchHistory> singleMatchesPlayer1 = PMFactory.getList(SingleMatchHistory.class, conPlayer);

		return singleMatchesPlayer1;
	}

}
