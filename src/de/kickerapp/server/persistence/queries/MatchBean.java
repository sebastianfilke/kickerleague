package de.kickerapp.server.persistence.queries;

import java.util.ArrayList;
import java.util.List;

import de.kickerapp.server.dao.DoubleMatch;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.server.dao.fetchplans.MatchPlan;
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

		final QueryContainer conPlayer1 = new QueryContainer();
		conPlayer1.setPlans(MatchPlan.BOTHPLAYERS);
		conPlayer1.setQuery("player1 == :id");
		conPlayer1.setParameter(playerDto.getId());
		final List<SingleMatch> singleMatchesPlayer1 = PMFactory.getList(SingleMatch.class, conPlayer1);

		final QueryContainer conPlayer2 = new QueryContainer();
		conPlayer2.setPlans(MatchPlan.BOTHPLAYERS);
		conPlayer2.setQuery("player2 == :id");
		conPlayer2.setParameter(playerDto.getId());
		final List<SingleMatch> singleMatchesPlayer2 = PMFactory.getList(SingleMatch.class, conPlayer2);

		list.addAll(singleMatchesPlayer1);
		list.addAll(singleMatchesPlayer2);

		return list;
	}

}
