package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.MatchService;
import de.kickerapp.server.dto.Match;
import de.kickerapp.server.dto.Match.Set;
import de.kickerapp.server.dto.Player;
import de.kickerapp.server.dto.Player.PlayerStats;
import de.kickerapp.server.persistence.Icebox;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.match.MatchDto;
import de.kickerapp.shared.match.PlayerDto;
import de.kickerapp.shared.match.TeamDto;

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
	public MatchDto createMatch(MatchDto matchDto) throws IllegalArgumentException {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final Transaction txn = pm.currentTransaction();
		try {
			txn.begin();

			final Match match = new Match();
			match.setMatchDate(matchDto.getMatchDate());
			final Set sets = new Set(matchDto.getSets().getSetsTeam1(), matchDto.getSets().getSetsTeam2());
			match.setSets(sets);

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
			matchDto.getTeam1().getPlayer1().setServiceObject(Icebox.freeze(team1Player1));
			matchDto.getTeam2().getPlayer1().setServiceObject(Icebox.freeze(team2Player1));

			match.getTeam1().add(team1Player1.getKey().getId());
			match.getTeam2().add(team2Player1.getKey().getId());

			Match persistedMatch = pm.makePersistent(match);
			persistedMatch = pm.detachCopy(persistedMatch);

			matchDto.setServiceObject(Icebox.freeze(persistedMatch));

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
		matchDto.setMatchNumber(Long.toString(dbMatch.getKey().getId()));
		matchDto.setMatchDate(dbMatch.getMatchDate());

		Player playerTeam1 = pm.getObjectById(Player.class, dbMatch.getTeam1().get(0));
		playerTeam1 = pm.detachCopy(playerTeam1);
		matchDto.setTeam1(new TeamDto(createPlayer(playerTeam1)));

		final String team1 = playerTeam1.getLastName() + " " + playerTeam1.getFirstName();
		matchDto.setLabelTeam1(team1);

		Player playerTeam2 = pm.getObjectById(Player.class, dbMatch.getTeam2().get(0));
		playerTeam2 = pm.detachCopy(playerTeam2);
		matchDto.setTeam2(new TeamDto(createPlayer(playerTeam2)));

		final String team2 = playerTeam2.getLastName() + " " + playerTeam2.getFirstName();
		matchDto.setLabelTeam2(team2);

		final StringBuilder builder = new StringBuilder();
		final int size = 3;
		for (int i = 0; i < size; i++) {
			final Integer setTeam1 = dbMatch.getSets().getResultTeam1().get(i);
			final Integer setTeam2 = dbMatch.getSets().getResultTeam2().get(i);
			if (setTeam1 != null && setTeam2 != null) {
				builder.append(setTeam1);
				builder.append(":");
				builder.append(setTeam2);

				if (i < size - 1) {
					final Integer nextSetTeam1 = dbMatch.getSets().getResultTeam1().get(i + 1);
					final Integer nextSetTeam2 = dbMatch.getSets().getResultTeam2().get(i + 1);
					if (nextSetTeam1 != null && nextSetTeam2 != null) {
						builder.append(", ");
					}
				}
			}
		}
		matchDto.setLabelSets(builder.toString());

		// final SetDto setDto = new SetDto();
		// setDto.setSetsTeam1(dbMatch.getSets().getResultTeam1());
		// setDto.setSetsTeam1(dbMatch.getSets().getResultTeam2());
		// matchDto.setSets(setDto);

		matchDto.setServiceObject(Icebox.freeze(dbMatch));

		return matchDto;
	}

	private PlayerDto createPlayer(Player dbPlayer) {
		final PlayerDto player = new PlayerDto(dbPlayer.getLastName(), dbPlayer.getFirstName());
		player.setId(dbPlayer.getKey().getId());
		player.setNickName(dbPlayer.getNickName());
		player.setEMail(dbPlayer.getEMail());
		player.setServiceObject(Icebox.freeze(dbPlayer));

		return player;
	}

}
