package de.kickerapp.client.properties;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;

import de.kickerapp.client.ui.resources.IconProvider;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;
import de.kickerapp.shared.dto.TeamStatsDto;

public interface TeamProperty extends BaseProperty<TeamDto> {

	LabelProvider<TeamDto> label = new LabelProvider<TeamDto>() {
		@Override
		public String getLabel(TeamDto item) {
			final StringBuilder sb = new StringBuilder();

			final PlayerDto player1 = item.getPlayer1();
			final PlayerDto player2 = item.getPlayer2();

			sb.append(player1.getLastName()).append(", ");
			sb.append(player1.getFirstName());
			sb.append(" | ");
			sb.append(player2.getLastName()).append(", ");
			sb.append(player2.getFirstName());

			return sb.toString();
		}
	};

	ValueProvider<TeamDto, String> teamName = new ValueProvider<TeamDto, String>() {
		@Override
		public String getValue(TeamDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerDto player1 = object.getPlayer1();
			final PlayerDto player2 = object.getPlayer2();

			sb.append(player1.getLastName()).append(", ");
			sb.append(player1.getFirstName());
			sb.append(" | ");
			sb.append(player2.getLastName()).append(", ");
			sb.append(player2.getFirstName());

			return sb.toString();
		}

		@Override
		public void setValue(TeamDto object, String value) {
		}

		@Override
		public String getPath() {
			return "teamName";
		}
	};

	ValueProvider<TeamDto, Integer> teamMatches = new ValueProvider<TeamDto, Integer>() {
		@Override
		public Integer getValue(TeamDto object) {
			final TeamStatsDto teamStatsDto = object.getTeamStatsDto();
			return teamStatsDto.getWins() + teamStatsDto.getDefeats();
		}

		@Override
		public void setValue(TeamDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "teamMatches";
		}
	};

	ValueProvider<TeamDto, Integer> teamWins = new ValueProvider<TeamDto, Integer>() {
		@Override
		public Integer getValue(TeamDto object) {
			return object.getTeamStatsDto().getWins();
		}

		@Override
		public void setValue(TeamDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "teamWins";
		}
	};

	ValueProvider<TeamDto, Integer> teamDefeats = new ValueProvider<TeamDto, Integer>() {
		@Override
		public Integer getValue(TeamDto object) {
			return object.getTeamStatsDto().getDefeats();
		}

		@Override
		public void setValue(TeamDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "teamDefeats";
		}
	};

	ValueProvider<TeamDto, String> teamSets = new ValueProvider<TeamDto, String>() {
		@Override
		public String getValue(TeamDto object) {
			final StringBuilder sb = new StringBuilder();

			final TeamStatsDto teamStatsDto = object.getTeamStatsDto();
			sb.append(teamStatsDto.getWinSets());
			sb.append(":");
			sb.append(teamStatsDto.getLostSets());

			return sb.toString();
		}

		@Override
		public void setValue(TeamDto object, String value) {
		}

		@Override
		public String getPath() {
			return "teamSets";
		}
	};

	ValueProvider<TeamDto, String> teamSetDifference = new ValueProvider<TeamDto, String>() {
		@Override
		public String getValue(TeamDto object) {
			final StringBuilder sb = new StringBuilder();

			final TeamStatsDto teamStatsDto = object.getTeamStatsDto();
			final int setDifference = teamStatsDto.getWinSets() - teamStatsDto.getLostSets();
			if (setDifference >= 0) {
				sb.append("+" + Integer.toString(setDifference));
			} else {
				sb.append(Integer.toString(setDifference));
			}
			return sb.toString();
		}

		@Override
		public void setValue(TeamDto object, String value) {
		}

		@Override
		public String getPath() {
			return "teamSetDifference";
		}
	};

	ValueProvider<TeamDto, String> teamGoals = new ValueProvider<TeamDto, String>() {
		@Override
		public String getValue(TeamDto object) {
			final StringBuilder sb = new StringBuilder();

			final TeamStatsDto teamStatsDto = object.getTeamStatsDto();
			sb.append(teamStatsDto.getShotGoals());
			sb.append(":");
			sb.append(teamStatsDto.getGetGoals());

			return sb.toString();
		}

		@Override
		public void setValue(TeamDto object, String value) {
		}

		@Override
		public String getPath() {
			return "teamGoals";
		}
	};

	ValueProvider<TeamDto, String> teamGoalDifference = new ValueProvider<TeamDto, String>() {
		@Override
		public String getValue(TeamDto object) {
			final StringBuilder sb = new StringBuilder();

			final TeamStatsDto teamStatsDto = object.getTeamStatsDto();
			final int goalDifference = teamStatsDto.getShotGoals() - teamStatsDto.getGetGoals();
			if (goalDifference >= 0) {
				sb.append("+" + Integer.toString(goalDifference));
			} else {
				sb.append(Integer.toString(goalDifference));
			}
			return sb.toString();
		}

		@Override
		public void setValue(TeamDto object, String value) {
		}

		@Override
		public String getPath() {
			return "teamGoalDifference";
		}
	};

	ValueProvider<TeamDto, String> teamPoints = new ValueProvider<TeamDto, String>() {
		@Override
		public String getValue(TeamDto object) {
			return Integer.toString(object.getTeamStatsDto().getPoints());
		}

		@Override
		public void setValue(TeamDto object, String value) {
		}

		@Override
		public String getPath() {
			return "teamPoints";
		}
	};

	ValueProvider<TeamDto, ImageResource> teamTendency = new ValueProvider<TeamDto, ImageResource>() {
		@Override
		public ImageResource getValue(TeamDto object) {
			ImageResource image = null;

			switch (object.getTeamStatsDto().getTendency()) {
			case UPWARD:
				image = IconProvider.get().arrow_up10();
				break;
			case DOWNWARD:
				image = IconProvider.get().arrow_down10();
				break;
			default:
				image = IconProvider.get().arrow_right10();
				break;
			}
			return image;
		}

		@Override
		public void setValue(TeamDto object, ImageResource value) {
		}

		@Override
		public String getPath() {
			return "teamTendency";
		}
	};

}
