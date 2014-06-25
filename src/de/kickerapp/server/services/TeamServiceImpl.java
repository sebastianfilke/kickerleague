package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.TeamService;
import de.kickerapp.server.dao.Team;
import de.kickerapp.server.dao.fetchplans.TeamPlan;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.services.TeamServiceHelper.TeamTableComparator;
import de.kickerapp.shared.dto.TeamDto;

/**
 * Dienst zur Verarbeitung von Teams im Clienten.
 * 
 * @author Sebastian Filke
 */
public class TeamServiceImpl extends RemoteServiceServlet implements TeamService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -5563601523296995378L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<TeamDto> getAllTeams() throws IllegalArgumentException {
		final ArrayList<TeamDto> teamDtos = new ArrayList<TeamDto>();

		final List<Team> dbTeams = PMFactory.getList(Team.class, TeamPlan.TEAMSTATS, TeamPlan.BOTHPLAYERS);

		for (Team dbTeam : dbTeams) {
			final TeamDto teamDto = TeamServiceHelper.createDtoTeam(dbTeam);

			teamDtos.add(teamDto);
		}
		Collections.sort(teamDtos, new TeamTableComparator());

		return teamDtos;
	}

}
