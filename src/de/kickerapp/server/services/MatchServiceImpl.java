package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.MatchService;
import de.kickerapp.server.entity.Match;
import de.kickerapp.server.entity.Match.MatchSets;
import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.Player.PlayerStats;
import de.kickerapp.server.entity.Team;
import de.kickerapp.server.entity.Team.TeamStats;
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

			Match match = new Match();

			int matchNumber = MatchServiceHelper.getMatchCount();
			final Key matchKey = KeyFactory.createKey(Match.class.getSimpleName(), ++matchNumber);
			match.setKey(matchKey);
			match.setMatchNumber(matchNumber);
			match.setMatchDate(matchDto.getMatchDate());

			final MatchSets sets = new MatchSets(matchDto.getSets().getSetsTeam1(), matchDto.getSets().getSetsTeam2());
			match.setMatchSets(sets);

			if (matchDto.getMatchType() == MatchType.Single) {
				createSingleMatch(matchDto, match);
			} else {
				createDoubleMatch(matchDto, pm, match);
			}

			match = PMFactory.persistObject(match);

			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
		return matchDto;
	}

	private void createSingleMatch(MatchDto matchDto, final Match match) {
		match.setMatchType(MatchType.Single);

		Player team1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1().getPlayer1().getId());
		Player team2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2().getPlayer1().getId());

		final boolean team1Winner = isTeam1Winner(matchDto);
		if (team1Winner) {
			team1Player1 = updatePlayerStats(team1Player1, matchDto, team1Winner);
			team2Player1 = updatePlayerStats(team2Player1, matchDto, !team1Winner);
		} else {
			team1Player1 = updatePlayerStats(team1Player1, matchDto, team1Winner);
			team2Player1 = updatePlayerStats(team2Player1, matchDto, !team1Winner);
		}
		match.setTeam1(team1Player1.getKey().getId());
		match.setTeam2(team2Player1.getKey().getId());
	}

	private void createDoubleMatch(MatchDto matchDto, final PersistenceManager pm, final Match match) {
		match.setMatchType(MatchType.Double);

		Player team1Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam1().getPlayer1().getId());
		Player team1Player2 = PMFactory.getObjectById(Player.class, matchDto.getTeam1().getPlayer2().getId());

		Team team1 = new Team(team1Player1, team1Player2);
		team1 = PMFactory.persistObject(team1);

		Player team2Player1 = PMFactory.getObjectById(Player.class, matchDto.getTeam2().getPlayer1().getId());
		Player team2Player2 = PMFactory.getObjectById(Player.class, matchDto.getTeam2().getPlayer2().getId());

		Team team2 = new Team(team2Player1, team2Player2);
		team2 = PMFactory.persistObject(team2);

		final boolean team1Winner = isTeam1Winner(matchDto);
		if (team1Winner) {
			team1Player1 = updatePlayerStats(team1Player1, matchDto, team1Winner);
			team1Player2 = updatePlayerStats(team1Player2, matchDto, team1Winner);
			team2Player1 = updatePlayerStats(team2Player1, matchDto, !team1Winner);
			team2Player2 = updatePlayerStats(team2Player2, matchDto, !team1Winner);

			updateTeamStats(team1, matchDto, team1Winner);
			updateTeamStats(team2, matchDto, !team1Winner);
		} else {
			team1Player1 = updatePlayerStats(team1Player1, matchDto, team1Winner);
			team1Player2 = updatePlayerStats(team1Player2, matchDto, team1Winner);
			team2Player1 = updatePlayerStats(team2Player1, matchDto, !team1Winner);
			team2Player2 = updatePlayerStats(team2Player2, matchDto, !team1Winner);

			updateTeamStats(team1, matchDto, team1Winner);
			updateTeamStats(team2, matchDto, !team1Winner);
		}
		match.setTeam1(team1.getKey().getId());
		match.setTeam2(team2.getKey().getId());
	}

	private boolean isTeam1Winner(MatchDto matchDto) {
		boolean team1Winner = false;
		int size = 0;
		for (Integer result : matchDto.getSets().getSetsTeam1()) {
			if (result != null && result == 6) {
				size++;
			}
			if (size == 2) {
				team1Winner = true;
				break;
			}
		}
		return team1Winner;
	}

	public Player updatePlayerStats(Player player, MatchDto matchDto, boolean winner) {
		final long playerId = player.getKey().getId();
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
		final int goalsTeam1 = getGoalsTeam1(matchDto);
		final int goalsTeam2 = getGoalsTeam2(matchDto);
		if (matchDto.getTeam1().getPlayer1().getId() == playerId || matchDto.getTeam1().getPlayer2() != null
				&& matchDto.getTeam1().getPlayer2().getId() == playerId) {
			final int shotGoals = playerStats.getSingleShotGoals() + goalsTeam1;
			playerStats.setSingleShotGoals(shotGoals);

			final int getGoals = playerStats.getSingleGetGoals() + goalsTeam2;
			playerStats.setSingleGetGoals(getGoals);
		} else if (matchDto.getTeam2().getPlayer1().getId() == playerId || matchDto.getTeam2().getPlayer2() != null
				&& matchDto.getTeam2().getPlayer2().getId() == playerId) {
			final int shotGoals = playerStats.getSingleShotGoals() + goalsTeam2;
			playerStats.setSingleShotGoals(shotGoals);

			final int getGoals = playerStats.getSingleGetGoals() + goalsTeam1;
			playerStats.setSingleGetGoals(getGoals);
		}
		player.setLastMatchDate(matchDto.getMatchDate());

		player = PMFactory.persistObject(player);

		return player;
	}

	private Team updateTeamStats(Team team, MatchDto matchDto, boolean winner) {
		final TeamStats teamStats = team.getTeamStats();
		if (winner) {
			final int wins = teamStats.getWins() + 1;
			teamStats.setWins(wins);

			final int points = teamStats.getPoints() + 20;
			teamStats.setPoints(points);
		} else {
			final int losses = teamStats.getLosses() + 1;
			teamStats.setLosses(losses);

			final int points = teamStats.getPoints() - 20;
			teamStats.setPoints(points);
		}
		final int goalsTeam1 = getGoalsTeam1(matchDto);
		final int goalsTeam2 = getGoalsTeam2(matchDto);
		if (team.getPlayers().contains(matchDto.getTeam1().getPlayer1().getId()) || team.getPlayers().contains(matchDto.getTeam1().getPlayer2().getId())) {
			final int shotGoals = teamStats.getShotGoals() + goalsTeam1;
			teamStats.setShotGoals(shotGoals);

			final int getGoals = teamStats.getGetGoals() + goalsTeam2;
			teamStats.setGetGoals(getGoals);
		} else if (team.getPlayers().contains(matchDto.getTeam2().getPlayer1().getId()) || team.getPlayers().contains(matchDto.getTeam2().getPlayer2().getId())) {
			final int shotGoals = teamStats.getShotGoals() + goalsTeam2;
			teamStats.setShotGoals(shotGoals);

			final int getGoals = teamStats.getGetGoals() + goalsTeam1;
			teamStats.setGetGoals(getGoals);
		}
		team.setLastMatchDate(matchDto.getMatchDate());

		team = PMFactory.persistObject(team);

		return team;
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
		final MatchDto matchDto = new MatchDto();
		matchDto.setId(dbMatch.getKey().getId());
		matchDto.setMatchNumber(Long.toString(dbMatch.getMatchNumber()));
		matchDto.setMatchDate(dbMatch.getMatchDate());
		matchDto.setMatchType(dbMatch.getMatchType());

		if (dbMatch.getMatchType() == MatchType.Single) {
			final Player team1Player1 = PMFactory.getObjectById(Player.class, dbMatch.getTeam1());
			matchDto.setTeam1(new TeamDto(PlayerServiceHelper.createPlayer(team1Player1)));

			final Player team2Player2 = PMFactory.getObjectById(Player.class, dbMatch.getTeam2());
			matchDto.setTeam2(new TeamDto(PlayerServiceHelper.createPlayer(team2Player2)));
		} else {
			final Team team1 = PMFactory.getObjectById(Team.class, dbMatch.getTeam1());

			final Player team1Player1 = PMFactory.getObjectById(Player.class, (Long) team1.getPlayers().toArray()[0]);
			final Player team1Player2 = PMFactory.getObjectById(Player.class, (Long) team1.getPlayers().toArray()[1]);

			matchDto.setTeam1(new TeamDto(PlayerServiceHelper.createPlayer(team1Player1), PlayerServiceHelper.createPlayer(team1Player2)));

			final Team team2 = PMFactory.getObjectById(Team.class, dbMatch.getTeam2());

			final Player team2Player1 = PMFactory.getObjectById(Player.class, (Long) team2.getPlayers().toArray()[0]);
			final Player team2Player2 = PMFactory.getObjectById(Player.class, (Long) team2.getPlayers().toArray()[1]);

			matchDto.setTeam2(new TeamDto(PlayerServiceHelper.createPlayer(team2Player1), PlayerServiceHelper.createPlayer(team2Player2)));
		}
		final MatchSetDto setDto = new MatchSetDto();
		setDto.setSetsTeam1(dbMatch.getMatchSets().getSetsTeam1());
		setDto.setSetsTeam2(dbMatch.getMatchSets().getSetsTeam2());
		matchDto.setSets(setDto);

		return matchDto;
	}

}
