package de.kickerapp.server.persistence.queries;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import de.kickerapp.server.dao.DoubleMatch;
import de.kickerapp.server.dao.Match;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.dto.PlayerDto;

public final class MatchBean {

	/**
	 * Liefert sämtliche Instanzen für die übergebene Klasse.
	 * 
	 * @return Die Instanzen.
	 */
	@SuppressWarnings("unchecked")
	public static List<Match> getSinglePlayerMatchesForPlayer(final PlayerDto playerDto) {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final Query query = pm.newQuery(Match.class);
		query.setFilter("matchType == SINGLE && team1 == :id");
		query.setOrdering("matchNumber asc");

		List<Match> list = new ArrayList<Match>();
		try {
			list = (List<Match>) query.execute(playerDto.getId());
			list = (List<Match>) pm.detachCopyAll(list);
		} finally {
			query.closeAll();
			pm.close();
		}
		return list;
	}

	public static int getNextMatchNumber() {
		final List<SingleMatch> singleMatches = PMFactory.getList(SingleMatch.class);
		final List<DoubleMatch> doubleMatches = PMFactory.getList(DoubleMatch.class);

		final int matchNumber = singleMatches.size() + doubleMatches.size() + 1;

		return matchNumber;
	}

}
