package de.kickerapp.server.services;

import java.util.ArrayList;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.MatchService;
import de.kickerapp.server.dto.Match;
import de.kickerapp.server.dto.Player;
import de.kickerapp.server.dto.Set;
import de.kickerapp.server.dto.Team;
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
		PersistenceManager pm = PMFactory.get().getPersistenceManager();

		Match match = new Match();
		match.setMatchDate(matchDto.getMatchDate());

		Team team = new Team();

		Player player = new Player();
		player.setFirstName("Sepp");
		player.setLastName("Horst");
		player.setLastMatchDate(match.getMatchDate());

		player = new Player();
		player.setFirstName("Depp");
		player.setLastName("Nepp");
		player.setLastMatchDate(match.getMatchDate());

		Set set = new Set();
		set.setMatch(match);
		set.setResult("1");
		set.setTeam(team);

		try {
			match = pm.makePersistent(match);
		} finally {
			pm.close();
		}

		return matchDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<MatchDto> getAllMatches() throws IllegalArgumentException {
		PersistenceManager pm = PMFactory.get().getPersistenceManager();

		ArrayList<MatchDto> matches = new ArrayList<MatchDto>();
		Extent<Match> extent = pm.getExtent(Match.class);
		for (Match m : extent) {
			// TODO
		}

		return matches;
	}

}
