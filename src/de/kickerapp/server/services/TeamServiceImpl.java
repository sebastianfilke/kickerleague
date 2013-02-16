package de.kickerapp.server.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.TeamService;
import de.kickerapp.server.entity.Team;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.dto.TeamDto;

public class TeamServiceImpl extends RemoteServiceServlet implements TeamService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -5563601523296995378L;

	/**
	 * Comparator zur Sortierung der Betriebsmittelarten.
	 * 
	 * @author Sebastian Filke, GIGATRONIK München GmbH
	 */
	private class TeamComparator implements Comparator<TeamDto>, Serializable {

		/** Konstante für die SerialVersionUID. */
		private static final long serialVersionUID = -3174540122634747743L;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(TeamDto t1, TeamDto t2) {
			return t2.getPoints().compareTo(t1.getPoints());
		}
	}

	@Override
	public ArrayList<TeamDto> getAllTeams() throws IllegalArgumentException {
		final ArrayList<TeamDto> teamDtos = new ArrayList<TeamDto>();

		final List<Team> dbTeams = PMFactory.getList(Team.class);
		for (Team dbTeam : dbTeams) {
			final TeamDto player = TeamServiceHelper.createTeam(dbTeam);

			teamDtos.add(player);
		}
		Collections.sort(teamDtos, new TeamComparator());
		return teamDtos;
	}

}
