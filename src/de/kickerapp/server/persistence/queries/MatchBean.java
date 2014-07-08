package de.kickerapp.server.persistence.queries;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.kickerapp.server.dao.DoubleMatch;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.server.dao.SingleMatchHistory;
import de.kickerapp.server.dao.fetchplans.MatchHistoryPlan;
import de.kickerapp.server.dao.fetchplans.MatchPlan;
import de.kickerapp.server.dao.fetchplans.TeamPlan;
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
		final int currentIdSingleMatches = PMFactory.getCurrentId(SingleMatch.class.getName());
		final int currentIdDoubleMatches = PMFactory.getCurrentId(DoubleMatch.class.getName());

		final int matchNumber = currentIdSingleMatches + currentIdDoubleMatches + 1;

		return matchNumber;
	}

	public static List<SingleMatch> getSingleMatchesFrom(Date date) {
		final QueryContainer conMatch = new QueryContainer();
		conMatch.setPlans(MatchPlan.COMMENT, MatchPlan.BOTHPLAYERS);
		conMatch.setQuery("matchDate >= :date");
		conMatch.setParameter(new Object[] { date });

		final List<SingleMatch> dbSingleMatches = PMFactory.getList(SingleMatch.class, conMatch);

		return dbSingleMatches;
	}

	public static List<DoubleMatch> getDoubleMatchesFrom(Date date) {
		final QueryContainer conMatch = new QueryContainer();
		conMatch.setPlans(MatchPlan.COMMENT, MatchPlan.BOTHTEAMS, TeamPlan.BOTHPLAYERS);
		conMatch.setQuery("matchDate >= :date");
		conMatch.setParameter(new Object[] { date });

		final List<DoubleMatch> dbDoubleMatches = PMFactory.getList(DoubleMatch.class, conMatch);

		return dbDoubleMatches;
	}

	/**
	 * Liefert sämtliche Einzelspiele für den übergebenen Spieler.
	 * 
	 * @param playerDto Der Spieler.
	 * @return Die Einzelspiele.
	 */
	public static List<SingleMatchHistory> getSingleMatchesForPlayer(final PlayerDto playerDto, final Date selectedDate) {
		final Calendar firstDate = getFirstDate(selectedDate);
		final Calendar lastDate = getLastDate(selectedDate);

		final QueryContainer conPlayer = new QueryContainer();
		conPlayer.setPlans(MatchHistoryPlan.BOTHPLAYERS);
		conPlayer.setQuery("player1 == :id && matchDate >= :startDate && matchDate <= :lastDate");
		conPlayer.setOrdering("matchDate asc");
		conPlayer.setParameter(new Object[] { playerDto.getId(), firstDate.getTime(), lastDate.getTime() });

		final List<SingleMatchHistory> singleMatchesPlayer = PMFactory.getList(SingleMatchHistory.class, conPlayer);

		return singleMatchesPlayer;
	}

	private static Calendar getFirstDate(Date date) {
		final Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(date);
		lastDate.set(Calendar.DAY_OF_YEAR, 1);
		lastDate.set(Calendar.AM_PM, Calendar.AM);
		lastDate.set(Calendar.MINUTE, 0);
		lastDate.set(Calendar.SECOND, 0);
		lastDate.set(Calendar.HOUR, 0);

		return lastDate;
	}

	private static Calendar getLastDate(Date date) {
		final Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(date);
		lastDate.set(Calendar.DAY_OF_YEAR, 365);
		lastDate.set(Calendar.AM_PM, Calendar.PM);
		lastDate.set(Calendar.MINUTE, 59);
		lastDate.set(Calendar.SECOND, 59);
		lastDate.set(Calendar.HOUR, 11);

		return lastDate;
	}

}
