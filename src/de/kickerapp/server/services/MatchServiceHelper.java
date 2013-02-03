package de.kickerapp.server.services;

import de.kickerapp.server.entity.Match;
import de.kickerapp.server.entity.Player;
import de.kickerapp.server.entity.Team;
import de.kickerapp.server.persistence.PMFactory;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.MatchDto;
import de.kickerapp.shared.dto.MatchSetDto;
import de.kickerapp.shared.dto.TeamDto;

public class MatchServiceHelper {

	public static MatchDto createMatch(Match dbMatch) {
		final MatchDto matchDto = new MatchDto();
		matchDto.setId(dbMatch.getKey().getId());
		matchDto.setMatchNumber(Long.toString(dbMatch.getMatchNumber()));
		matchDto.setMatchDate(dbMatch.getMatchDate());
		matchDto.setMatchType(dbMatch.getMatchType());

		final MatchType matchType = MatchType.None;
		if (dbMatch.getMatchType() == MatchType.Single) {
			final Player team1Player1 = PMFactory.getObjectById(Player.class, dbMatch.getTeam1());
			matchDto.setTeam1(new TeamDto(PlayerServiceHelper.createPlayer(team1Player1, matchType)));

			final Player team2Player2 = PMFactory.getObjectById(Player.class, dbMatch.getTeam2());
			matchDto.setTeam2(new TeamDto(PlayerServiceHelper.createPlayer(team2Player2, matchType)));
		} else {
			final Team team1 = PMFactory.getObjectById(Team.class, dbMatch.getTeam1());

			final Player team1Player1 = PMFactory.getObjectById(Player.class, (Long) team1.getPlayers().toArray()[0]);
			final Player team1Player2 = PMFactory.getObjectById(Player.class, (Long) team1.getPlayers().toArray()[1]);

			matchDto.setTeam1(new TeamDto(PlayerServiceHelper.createPlayer(team1Player1, matchType), PlayerServiceHelper.createPlayer(team1Player2, matchType)));

			final Team team2 = PMFactory.getObjectById(Team.class, dbMatch.getTeam2());

			final Player team2Player1 = PMFactory.getObjectById(Player.class, (Long) team2.getPlayers().toArray()[0]);
			final Player team2Player2 = PMFactory.getObjectById(Player.class, (Long) team2.getPlayers().toArray()[1]);

			matchDto.setTeam2(new TeamDto(PlayerServiceHelper.createPlayer(team2Player1, matchType), PlayerServiceHelper.createPlayer(team2Player2, matchType)));
		}
		final MatchSetDto setDto = new MatchSetDto();
		setDto.setSetsTeam1(dbMatch.getMatchSets().getSetsTeam1());
		setDto.setSetsTeam2(dbMatch.getMatchSets().getSetsTeam2());
		matchDto.setSets(setDto);

		return matchDto;
	}

}
