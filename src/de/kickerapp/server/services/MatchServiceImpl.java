package de.kickerapp.server.services;

import java.util.ArrayList;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.MatchService;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.match.Match;

public class MatchServiceImpl extends RemoteServiceServlet implements MatchService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 6552395856577483840L;

	/**
	 * {@inheritDoc}
	 */
	public Match createMatch(Match match) throws IllegalArgumentException {
		PersistenceManager pm = PMFactory.get().getPersistenceManager();

		try {
			match = pm.makePersistent(match);
		} finally {
			pm.close();
		}
		return match;
	}

	/**
	 * {@inheritDoc}
	 */
	public ArrayList<Match> getAllMatches() throws IllegalArgumentException {
		PersistenceManager pm = PMFactory.get().getPersistenceManager();

		ArrayList<Match> matches = new ArrayList<Match>();
		Extent<Match> extent = pm.getExtent(Match.class);
		for (Match m : extent) {
			matches.add(m);
		}

		return matches;
	}

}
