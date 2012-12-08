package de.kickerapp.server.services;

import java.util.ArrayList;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.MatchService;
import de.kickerapp.server.dto.Match;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.match.MatchData;

public class MatchServiceImpl extends RemoteServiceServlet implements MatchService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 6552395856577483840L;

	/**
	 * {@inheritDoc}
	 */
	public MatchData createMatch(MatchData match) throws IllegalArgumentException {
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
	public ArrayList<MatchData> getAllMatches() throws IllegalArgumentException {
		PersistenceManager pm = PMFactory.get().getPersistenceManager();

		ArrayList<MatchData> matches = new ArrayList<MatchData>();
		Extent<Match> extent = pm.getExtent(Match.class);
		for (Match m : extent) {
			// TODO
		}

		return matches;
	}

}
