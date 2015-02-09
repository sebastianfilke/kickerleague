package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.client.ui.resources.IconProvider;
import de.kickerapp.shared.dto.PlayerDoubleStatsDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.PlayerSingleStatsDto;

public interface PlayerProperty extends PropertyAccess<PlayerDto> {

	@Path("id")
	ModelKeyProvider<PlayerDto> id();

	LabelProvider<PlayerDto> label = new LabelProvider<PlayerDto>() {
		@Override
		public String getLabel(PlayerDto item) {
			final StringBuilder sb = new StringBuilder();

			sb.append(item.getLastName());
			sb.append(", ");
			sb.append(item.getFirstName());
			sb.append(" (");
			sb.append(item.getNickName());
			sb.append(")");

			return sb.toString();
		}
	};

	ValueProvider<PlayerDto, String> playerName = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			sb.append(object.getLastName());
			sb.append(", ");
			sb.append(object.getFirstName());
			sb.append(" (");
			sb.append(object.getNickName());
			sb.append(")");

			return sb.toString();
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "playerName";
		}
	};

	ValueProvider<PlayerDto, Integer> singleMatches = new ValueProvider<PlayerDto, Integer>() {
		@Override
		public Integer getValue(PlayerDto object) {
			final PlayerSingleStatsDto playerSingleStatsDto = object.getPlayerSingleStatsDto();
			return playerSingleStatsDto.getWins() + playerSingleStatsDto.getDefeats();
		}

		@Override
		public void setValue(PlayerDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "singleMatches";
		}
	};

	ValueProvider<PlayerDto, Integer> singleWins = new ValueProvider<PlayerDto, Integer>() {
		@Override
		public Integer getValue(PlayerDto object) {
			return object.getPlayerSingleStatsDto().getWins();
		}

		@Override
		public void setValue(PlayerDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "singleWins";
		}
	};

	ValueProvider<PlayerDto, Integer> singleDefeats = new ValueProvider<PlayerDto, Integer>() {
		@Override
		public Integer getValue(PlayerDto object) {
			return object.getPlayerSingleStatsDto().getDefeats();
		}

		@Override
		public void setValue(PlayerDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "singleDefeats";
		}
	};

	ValueProvider<PlayerDto, String> singleSets = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerSingleStatsDto playerSingleStatsDto = object.getPlayerSingleStatsDto();
			sb.append(playerSingleStatsDto.getWinSets());
			sb.append(":");
			sb.append(playerSingleStatsDto.getLostSets());

			return sb.toString();
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "singleSets";
		}
	};

	ValueProvider<PlayerDto, String> singleSetDifference = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerSingleStatsDto playerSingleStatsDto = object.getPlayerSingleStatsDto();
			final int setDifference = playerSingleStatsDto.getWinSets() - playerSingleStatsDto.getLostSets();
			if (setDifference >= 0) {
				sb.append("+" + Integer.toString(setDifference));
			} else {
				sb.append(Integer.toString(setDifference));
			}
			return sb.toString();
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "singleSetDifference";
		}
	};

	ValueProvider<PlayerDto, String> singleGoals = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerSingleStatsDto playerSingleStatsDto = object.getPlayerSingleStatsDto();
			sb.append(playerSingleStatsDto.getShotGoals());
			sb.append(":");
			sb.append(playerSingleStatsDto.getGetGoals());

			return sb.toString();
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "singleGoals";
		}
	};

	ValueProvider<PlayerDto, String> singleGoalDifference = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerSingleStatsDto playerSingleStatsDto = object.getPlayerSingleStatsDto();
			final int goalDifference = playerSingleStatsDto.getShotGoals() - playerSingleStatsDto.getGetGoals();
			if (goalDifference >= 0) {
				sb.append("+" + Integer.toString(goalDifference));
			} else {
				sb.append(Integer.toString(goalDifference));
			}
			return sb.toString();
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "singleGoalDifference";
		}
	};

	ValueProvider<PlayerDto, String> singlePoints = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			return Integer.toString(object.getPlayerSingleStatsDto().getPoints());
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "singlePoints";
		}
	};

	ValueProvider<PlayerDto, ImageResource> singleTendency = new ValueProvider<PlayerDto, ImageResource>() {
		@Override
		public ImageResource getValue(PlayerDto object) {
			ImageResource image = null;

			switch (object.getPlayerSingleStatsDto().getTendency()) {
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
		public void setValue(PlayerDto object, ImageResource value) {
		}

		@Override
		public String getPath() {
			return "singleTendency";
		}
	};

	ValueProvider<PlayerDto, Integer> doubleMatches = new ValueProvider<PlayerDto, Integer>() {
		@Override
		public Integer getValue(PlayerDto object) {
			final PlayerDoubleStatsDto playerDoubleStatsDto = object.getPlayerDoubleStatsDto();
			return playerDoubleStatsDto.getWins() + playerDoubleStatsDto.getDefeats();
		}

		@Override
		public void setValue(PlayerDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "doubleMatches";
		}
	};

	ValueProvider<PlayerDto, Integer> doubleWins = new ValueProvider<PlayerDto, Integer>() {
		@Override
		public Integer getValue(PlayerDto object) {
			return object.getPlayerDoubleStatsDto().getWins();
		}

		@Override
		public void setValue(PlayerDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "doubleWins";
		}
	};

	ValueProvider<PlayerDto, Integer> doubleDefeats = new ValueProvider<PlayerDto, Integer>() {
		@Override
		public Integer getValue(PlayerDto object) {
			return object.getPlayerDoubleStatsDto().getDefeats();
		}

		@Override
		public void setValue(PlayerDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "doubleDefeats";
		}
	};

	ValueProvider<PlayerDto, String> doubleSets = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerDoubleStatsDto playerDoubleStatsDto = object.getPlayerDoubleStatsDto();
			sb.append(playerDoubleStatsDto.getWinSets());
			sb.append(":");
			sb.append(playerDoubleStatsDto.getLostSets());

			return sb.toString();
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "doubleSets";
		}
	};

	ValueProvider<PlayerDto, String> doubleSetDifference = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerDoubleStatsDto playerDoubleStatsDto = object.getPlayerDoubleStatsDto();
			final int setDifference = playerDoubleStatsDto.getWinSets() - playerDoubleStatsDto.getLostSets();
			if (setDifference >= 0) {
				sb.append("+" + Integer.toString(setDifference));
			} else {
				sb.append(Integer.toString(setDifference));
			}
			return sb.toString();
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "doubleSetDifference";
		}
	};

	ValueProvider<PlayerDto, String> doubleGoals = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerDoubleStatsDto playerDoubleStatsDto = object.getPlayerDoubleStatsDto();
			sb.append(playerDoubleStatsDto.getShotGoals());
			sb.append(":");
			sb.append(playerDoubleStatsDto.getGetGoals());

			return sb.toString();
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "doubleGoals";
		}
	};

	ValueProvider<PlayerDto, String> doubleGoalDifference = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerDoubleStatsDto playerDoubleStatsDto = object.getPlayerDoubleStatsDto();
			final int goalDifference = playerDoubleStatsDto.getShotGoals() - playerDoubleStatsDto.getGetGoals();
			if (goalDifference >= 0) {
				sb.append("+" + Integer.toString(goalDifference));
			} else {
				sb.append(Integer.toString(goalDifference));
			}
			return sb.toString();
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "doubleGoalDifference";
		}
	};

	ValueProvider<PlayerDto, String> doublePoints = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			return Integer.toString(object.getPlayerDoubleStatsDto().getPoints());
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "doublePoints";
		}
	};

	ValueProvider<PlayerDto, ImageResource> doubleTendency = new ValueProvider<PlayerDto, ImageResource>() {
		@Override
		public ImageResource getValue(PlayerDto object) {
			ImageResource image = null;

			switch (object.getPlayerDoubleStatsDto().getTendency()) {
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
		public void setValue(PlayerDto object, ImageResource value) {
		}

		@Override
		public String getPath() {
			return "doubleTendency";
		}
	};

}
