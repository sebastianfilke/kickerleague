package de.kickerapp.server.services;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import de.kickerapp.server.entity.Match;
import de.kickerapp.server.persistence.PMFactory;

public class MatchServiceHelper {

	@SuppressWarnings("unchecked")
	public static int getMatchCount() {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final Query query = pm.newQuery(Match.class);
		final List<Match> dbMatches = (List<Match>) query.execute();

		return dbMatches.size();
	}

}
