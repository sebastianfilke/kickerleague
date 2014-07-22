package de.kickerapp.server.persistence.queries;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.kickerapp.server.dao.DoubleMatch;
import de.kickerapp.server.dao.DoubleMatchHistory;
import de.kickerapp.server.dao.Player;
import de.kickerapp.server.dao.SingleMatch;
import de.kickerapp.server.dao.SingleMatchHistory;
import de.kickerapp.server.dao.SingleMatchYearAggregation;
import de.kickerapp.server.dao.fetchplans.MatchHistoryPlan;
import de.kickerapp.server.dao.fetchplans.MatchPlan;
import de.kickerapp.server.dao.fetchplans.TeamPlan;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.services.MatchServiceHelper.MatchHistoryAscendingComparator;
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
		conMatch.setParameter(new Object[] { clearTimeForDate(date) });

		final List<SingleMatch> dbSingleMatches = PMFactory.getList(SingleMatch.class, conMatch);

		return dbSingleMatches;
	}

	public static List<DoubleMatch> getDoubleMatchesFrom(Date date) {
		final QueryContainer conMatch = new QueryContainer();
		conMatch.setPlans(MatchPlan.COMMENT, MatchPlan.BOTHTEAMS, TeamPlan.BOTHPLAYERS);
		conMatch.setQuery("matchDate >= :date");
		conMatch.setParameter(new Object[] { clearTimeForDate(date) });

		final List<DoubleMatch> dbDoubleMatches = PMFactory.getList(DoubleMatch.class, conMatch);

		return dbDoubleMatches;
	}

	/**
	 * Liefert sämtliche Einzelspiele für den übergebenen Spieler.
	 * 
	 * @param playerDto Der Spieler.
	 * @return Die Einzelspiele.
	 */
	public static List<SingleMatchHistory> getSingleMatchesForPlayer(final PlayerDto playerDto, final Integer year) {
		final QueryContainer conPlayer = new QueryContainer();
		conPlayer.setPlans(MatchHistoryPlan.BOTHPLAYERS);
		conPlayer.setQuery("player1 == :id && matchDate >= :startDate && matchDate <= :lastDate");
		conPlayer.setParameter(new Object[] { playerDto.getId(), getFirstDate(year), getLastDate(year) });

		final List<SingleMatchHistory> singleMatchesPlayer = PMFactory.getList(SingleMatchHistory.class, conPlayer);

		Collections.sort(singleMatchesPlayer, new MatchHistoryAscendingComparator());

		return singleMatchesPlayer;
	}

	/**
	 * Liefert sämtliche Doppelspiele für den übergebenen Spieler.
	 * 
	 * @param playerDto Der Spieler.
	 * @return Die Doppelspiele.
	 */
	public static List<DoubleMatchHistory> getDoubleMatchesForPlayer(final PlayerDto playerDto, final Integer year) {
		final QueryContainer conPlayer = new QueryContainer();
		conPlayer.setPlans(MatchHistoryPlan.ALLPLAYERS);
		conPlayer.setQuery("player1 == :id && matchDate >= :startDate && matchDate <= :lastDate");
		conPlayer.setParameter(new Object[] { playerDto.getId(), getFirstDate(year), getLastDate(year) });

		final List<DoubleMatchHistory> doubleMatchesPlayer = PMFactory.getList(DoubleMatchHistory.class, conPlayer);

		Collections.sort(doubleMatchesPlayer, new MatchHistoryAscendingComparator());

		return doubleMatchesPlayer;
	}

	public static SingleMatchYearAggregation getSingleMatchYearAggregationForPlayer(final Player player, final int year) {
		final QueryContainer conMatch = new QueryContainer();
		conMatch.setQuery("player == :id && year == :year");
		conMatch.setParameter(new Object[] { player.getKey().getId(), year });

		SingleMatchYearAggregation dbSingleMatchYearAggregation = PMFactory.getObject(SingleMatchYearAggregation.class, conMatch);
		if (dbSingleMatchYearAggregation == null) {
			dbSingleMatchYearAggregation = new SingleMatchYearAggregation();
			final int matchYearAggregationId = PMFactory.getNextId(SingleMatchYearAggregation.class.getName());
			final Key matchYearAggregationKey = KeyFactory.createKey(SingleMatchYearAggregation.class.getSimpleName(), matchYearAggregationId);
			dbSingleMatchYearAggregation.setKey(matchYearAggregationKey);
			dbSingleMatchYearAggregation.setPlayer(player);
			dbSingleMatchYearAggregation.setYear(year);
		}
		return dbSingleMatchYearAggregation;
	}

	private static Date clearTimeForDate(Date date) {
		final Calendar clearedDate = Calendar.getInstance();
		clearedDate.setTime(date);
		clearedDate.set(Calendar.MINUTE, 0);
		clearedDate.set(Calendar.SECOND, 0);
		clearedDate.set(Calendar.HOUR, 0);

		return clearedDate.getTime();
	}

	private static Date getFirstDate(Integer year) {
		final Calendar firstDate = Calendar.getInstance();
		firstDate.set(Calendar.YEAR, year);
		firstDate.set(Calendar.DAY_OF_YEAR, 1);
		firstDate.set(Calendar.AM_PM, Calendar.AM);
		firstDate.set(Calendar.MINUTE, 0);
		firstDate.set(Calendar.SECOND, 0);
		firstDate.set(Calendar.HOUR, 0);

		return firstDate.getTime();
	}

	private static Date getLastDate(Integer year) {
		final Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.YEAR, year);
		lastDate.set(Calendar.DAY_OF_YEAR, 365);
		lastDate.set(Calendar.AM_PM, Calendar.PM);
		lastDate.set(Calendar.MINUTE, 59);
		lastDate.set(Calendar.SECOND, 59);
		lastDate.set(Calendar.HOUR, 11);

		return lastDate.getTime();
	}

}
