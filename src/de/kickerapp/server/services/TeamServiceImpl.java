package de.kickerapp.server.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.TeamService;
import de.kickerapp.server.dao.Team;
import de.kickerapp.server.dao.TeamMatchYearAggregation;
import de.kickerapp.server.dao.fetchplans.MatchAggregationPlan;
import de.kickerapp.server.dao.fetchplans.TeamPlan;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.server.services.TeamServiceHelper.TeamTableComparator;
import de.kickerapp.shared.dto.TeamDto;
import de.kickerapp.shared.exception.KickerLeagueException;

/**
 * Dienst zur Verarbeitung von Teams im Klienten.
 * 
 * @author Sebastian Filke
 */
public class TeamServiceImpl extends RemoteServiceServlet implements TeamService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<TeamDto> getAllTeams() throws KickerLeagueException {
		final ArrayList<TeamDto> teamDtos = new ArrayList<TeamDto>();

		final List<Team> dbTeams = PMFactory.getList(Team.class, TeamPlan.TEAMSTATS, TeamPlan.BOTHPLAYERS);

		for (Team dbTeam : dbTeams) {
			final TeamDto teamDto = TeamServiceHelper.createTeamDtoWithTeamStats(dbTeam);

			teamDtos.add(teamDto);
		}
		Collections.sort(teamDtos, new TeamTableComparator());

		return teamDtos;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<Integer, ArrayList<TeamDto>> getTeamMatchYearAggregation() throws KickerLeagueException {
		final List<TeamMatchYearAggregation> dbYearAggregations = PMFactory.getList(TeamMatchYearAggregation.class, MatchAggregationPlan.TEAM,
				TeamPlan.BOTHPLAYERS);

		final HashMap<Integer, ArrayList<TeamDto>> yearAggregations = new HashMap<Integer, ArrayList<TeamDto>>();
		for (TeamMatchYearAggregation dbYearAggregation : dbYearAggregations) {
			final ArrayList<TeamDto> teamDtos = yearAggregations.get(dbYearAggregation.getYear());
			if (teamDtos == null) {
				final ArrayList<TeamDto> newTeamDtos = new ArrayList<TeamDto>();
				newTeamDtos.add(TeamServiceHelper.createTeamDto(dbYearAggregation.getTeam()));
				yearAggregations.put(dbYearAggregation.getYear(), newTeamDtos);
			} else {
				boolean contains = false;
				for (TeamDto teamDto : teamDtos) {
					if (teamDto.getId() == dbYearAggregation.getTeam().getKey().getId()) {
						contains = true;
						break;
					}
				}
				if (!contains) {
					teamDtos.add(TeamServiceHelper.createTeamDto(dbYearAggregation.getTeam()));
				}
			}
		}
		return yearAggregations;
	}

}
