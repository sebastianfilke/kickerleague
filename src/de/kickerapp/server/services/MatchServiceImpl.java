package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.MatchService;
import de.kickerapp.server.entity.Match;
import de.kickerapp.server.entity.Match.MatchSets;
import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.Player.PlayerStats;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.MatchDto;
import de.kickerapp.shared.dto.MatchSetDto;
import de.kickerapp.shared.dto.TeamDto;

/**
 * Dienst zur Verarbeitung von Spielen im Clienten.
 * 
 * @author Sebastian Filke
 */
public class MatchServiceImpl extends RemoteServiceServlet implements MatchService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 6552395856577483840L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MatchDto createSingleMatch(MatchDto matchDto) throws IllegalArgumentException {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final Transaction txn = pm.currentTransaction();
		try {
			txn.begin();

			final Match match = new Match();

			int matchNumber = MatchServiceHelper.getMatchCount();
			match.setMatchNumber(++matchNumber);
			match.setMatchDate(matchDto.getMatchDate());
			match.setMatchType(MatchType.Single);

			final MatchSets sets = new MatchSets(matchDto.getSets().getSetsTeam1(), matchDto.getSets().getSetsTeam2());
			match.setMatchSets(sets);

			int size = 0;
			for (Integer result : matchDto.getSets().getSetsTeam1()) {
				if (result != null && result == 6) {
					size++;
				}
			}
			Player team1Player1 = pm.getObjectById(Player.class, matchDto.getTeam1().getPlayer1().getId());
			team1Player1 = pm.detachCopy(team1Player1);
			Player team2Player1 = pm.getObjectById(Player.class, matchDto.getTeam2().getPlayer1().getId());
			team2Player1 = pm.detachCopy(team2Player1);
			if (size == 2) {
				team1Player1 = updatePlayerStats(team1Player1, matchDto, true);
				team2Player1 = updatePlayerStats(team2Player1, matchDto, false);
			} else {
				team1Player1 = updatePlayerStats(team1Player1, matchDto, false);
				team2Player1 = updatePlayerStats(team2Player1, matchDto, true);
			}

			match.setTeam1(team1Player1.getKey().getId());
			match.setTeam2(team2Player1.getKey().getId());

			Match persistedMatch = pm.makePersistent(match);
			persistedMatch = pm.detachCopy(persistedMatch);

			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		return matchDto;
	}

	public Player updatePlayerStats(Player player, MatchDto match, boolean winner) {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final PlayerStats playerStats = player.getPlayerStats();
		if (winner) {
			final int wins = playerStats.getSingleWins() + 1;
			playerStats.setSingleWins(wins);

			final int points = playerStats.getPoints() + 20;
			playerStats.setPoints(points);
		} else {
			final int losses = playerStats.getSingleLosses() + 1;
			playerStats.setSingleLosses(losses);

			final int points = playerStats.getPoints() - 20;
			playerStats.setPoints(points);
		}
		final int goalsTeam1 = getGoalsTeam1(match);
		final int goalsTeam2 = getGoalsTeam2(match);
		if (match.getTeam1().getPlayer1().getId() == player.getKey().getId()) {
			final int shotGoals = playerStats.getSingleShotGoals() + goalsTeam1;
			playerStats.setSingleShotGoals(shotGoals);

			final int getGoals = playerStats.getSingleGetGoals() + goalsTeam2;
			playerStats.setSingleGetGoals(getGoals);
		} else if (match.getTeam2().getPlayer1().getId() == player.getKey().getId()) {
			final int shotGoals = playerStats.getSingleShotGoals() + goalsTeam2;
			playerStats.setSingleShotGoals(shotGoals);

			final int getGoals = playerStats.getSingleGetGoals() + goalsTeam1;
			playerStats.setSingleGetGoals(getGoals);
		}
		player.setLastMatchDate(match.getMatchDate());

		Player persistedPlayer = pm.makePersistent(player);
		persistedPlayer = pm.detachCopy(persistedPlayer);

		return persistedPlayer;
	}

	private int getGoalsTeam1(MatchDto match) {
		int goals = 0;
		for (Integer result : match.getSets().getSetsTeam1()) {
			if (result != null) {
				goals = goals + result;
			}
		}
		return goals;
	}

	private int getGoalsTeam2(MatchDto match) {
		int goals = 0;
		for (Integer result : match.getSets().getSetsTeam2()) {
			if (result != null) {
				goals = goals + result;
			}
		}
		return goals;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<MatchDto> getAllMatches() throws IllegalArgumentException {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final ArrayList<MatchDto> matches = new ArrayList<MatchDto>();

		final Query query = pm.newQuery(Match.class);
		final List<Match> dbMatches = (List<Match>) query.execute();
		for (Match dbMatch : dbMatches) {
			dbMatch = pm.detachCopy(dbMatch);
			final MatchDto matchDto = createMatch(dbMatch);

			matches.add(matchDto);
		}
		return matches;
	}

	private MatchDto createMatch(Match dbMatch) {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final MatchDto matchDto = new MatchDto();
		matchDto.setId(dbMatch.getKey().getId());
		matchDto.setMatchNumber(Long.toString(dbMatch.getMatchNumber()));
		matchDto.setMatchDate(dbMatch.getMatchDate());

		Player playerTeam1 = pm.getObjectById(Player.class, dbMatch.getTeam1());
		playerTeam1 = pm.detachCopy(playerTeam1);
		matchDto.setTeam1(new TeamDto(PlayerServiceHelper.createPlayer(playerTeam1)));

		Player playerTeam2 = pm.getObjectById(Player.class, dbMatch.getTeam2());
		playerTeam2 = pm.detachCopy(playerTeam2);
		matchDto.setTeam2(new TeamDto(PlayerServiceHelper.createPlayer(playerTeam2)));

		MatchSetDto setDto = new MatchSetDto();
		setDto.setSetsTeam1(dbMatch.getMatchSets().getSetsTeam1());
		setDto.setSetsTeam2(dbMatch.getMatchSets().getSetsTeam2());
		matchDto.setSets(setDto);

		return matchDto;
	}

}
