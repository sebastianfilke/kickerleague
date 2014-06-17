package de.kickerapp.server.persistence.queries;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import de.kickerapp.server.dao.Match;
import de.kickerapp.server.persistence.PMFactory;

public final class MatchBean {

	/**
	 * Liefert sämtliche Instanzen für die übergebene Klasse.
	 * 
	 * @return Die Instanzen.
	 */
	@SuppressWarnings("unchecked")
	public static List<Match> getAllMatches() {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();
		pm.getFetchPlan().addGroup("withComments");

		final Query query = pm.newQuery(Match.class);
		List<Match> list = new ArrayList<Match>();
		try {
			list = (List<Match>) query.execute();
			list = (List<Match>) pm.detachCopyAll(list);
		} finally {
			query.closeAll();
			pm.close();
		}
		return list;
	}

}
