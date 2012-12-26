package de.kickerapp.server.services;

import java.util.ArrayList;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.MatchService;
import de.kickerapp.server.dto.Match;
import de.kickerapp.server.dto.Player;
import de.kickerapp.server.dto.Set;
import de.kickerapp.server.dto.Team;
import de.kickerapp.server.persistence.Icebox;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.match.MatchDto;

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

		Match match = new Match();
		match.setMatchDate(matchDto.getMatchDate());

		match = PMFactory.insertObject(match);

		Team team1 = new Team();
		final Player team1Player1 = Icebox.melt(matchDto.getTeam1().getPlayer1().getServiceObject());
		team1.setPlayer(team1Player1);

		team1 = PMFactory.insertObject(team1);

		final ArrayList<Set> setsTeam1 = new ArrayList<Set>();
		for (int i = 1; i < 4; i++) {
			final Set set = new Set();
			set.setMatch(match);
			set.setResult(Integer.toString(i));
			set.setTeam(team1);

			setsTeam1.add(set);
		}

		Team team2 = new Team();
		final Player team2Player1 = Icebox.melt(matchDto.getTeam2().getPlayer1().getServiceObject());
		team2.setPlayer(team2Player1);

		team2 = PMFactory.insertObject(team2);

		final ArrayList<Set> setsTeam2 = new ArrayList<Set>();
		for (int i = 5; i < 8; i++) {
			final Set set = new Set();
			set.setMatch(match);
			set.setResult(Integer.toString(i));
			set.setTeam(team2);

			setsTeam2.add(set);
		}

		team1.setSets(setsTeam1);
		team2.setSets(setsTeam2);

		Transaction txn = pm.currentTransaction();
		try {
			txn.begin();
			pm.makePersistentAll(team1);
			pm.makePersistentAll(team2);
			pm.makePersistentAll(match);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}

		// match.getSets().addAll(setsTeam1);
		// match.getSets().addAll(setsTeam2);

		txn = pm.currentTransaction();
		try {
			txn.begin();
			pm.makePersistentAll(match);
			txn.commit();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}

		return matchDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<MatchDto> getAllMatches() throws IllegalArgumentException {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final ArrayList<MatchDto> matches = new ArrayList<MatchDto>();
		final Extent<Match> extent = pm.getExtent(Match.class);
		for (Match m : extent) {}

		return matches;
	}

}
