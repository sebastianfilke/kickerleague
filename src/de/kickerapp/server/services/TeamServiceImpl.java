package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.TeamService;
import de.kickerapp.server.entity.Team;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.dto.TeamDto;

public class TeamServiceImpl extends RemoteServiceServlet implements TeamService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -5563601523296995378L;

	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<TeamDto> getAllTeams() throws IllegalArgumentException {
		final PersistenceManager pm = PMFactory.get().getPersistenceManager();

		final ArrayList<TeamDto> teams = new ArrayList<TeamDto>();

		final Query query = pm.newQuery(Team.class);
		query.setOrdering("teamStats.points desc");

		final List<Team> dbTeams = (List<Team>) query.execute();
		for (Team dbTeam : dbTeams) {
			dbTeam = pm.detachCopy(dbTeam);
			final TeamDto player = TeamServiceHelper.createTeam(dbTeam);

			teams.add(player);
		}
		return teams;
	}

}
