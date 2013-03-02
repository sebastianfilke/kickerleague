package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.shared.dto.PlayerDoubleStatsDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.PlayerSingleStatsDto;

public interface PlayerProperty extends PropertyAccess<PlayerDto> {

	@Path("id")
	public ModelKeyProvider<PlayerDto> id();

	public LabelProvider<PlayerDto> label = new LabelProvider<PlayerDto>() {
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

	public ValueProvider<PlayerDto, String> playerName = new ValueProvider<PlayerDto, String>() {
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

	public ValueProvider<PlayerDto, Integer> singleMatches = new ValueProvider<PlayerDto, Integer>() {
		@Override
		public Integer getValue(PlayerDto object) {
			final PlayerSingleStatsDto playerSingleStatsDto = object.getPlayerSingleStats();
			return playerSingleStatsDto.getWins() + playerSingleStatsDto.getLosses();
		}

		@Override
		public void setValue(PlayerDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "singleMatches";
		}
	};

	public ValueProvider<PlayerDto, Integer> singleWins = new ValueProvider<PlayerDto, Integer>() {
		@Override
		public Integer getValue(PlayerDto object) {
			return object.getPlayerSingleStats().getWins();
		}

		@Override
		public void setValue(PlayerDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "singleWins";
		}
	};

	public ValueProvider<PlayerDto, Integer> singleLosses = new ValueProvider<PlayerDto, Integer>() {
		@Override
		public Integer getValue(PlayerDto object) {
			return object.getPlayerSingleStats().getLosses();
		}

		@Override
		public void setValue(PlayerDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "singleLosses";
		}
	};

	public ValueProvider<PlayerDto, String> singleGoals = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerSingleStatsDto playerSingleStatsDto = object.getPlayerSingleStats();
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

	public ValueProvider<PlayerDto, String> singleGoalDifference = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerSingleStatsDto playerSingleStatsDto = object.getPlayerSingleStats();
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

	public ValueProvider<PlayerDto, String> singlePoints = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			return Integer.toString(object.getPlayerSingleStats().getPoints());
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "singlePoints";
		}
	};

	public ValueProvider<PlayerDto, ImageResource> singleTendency = new ValueProvider<PlayerDto, ImageResource>() {
		@Override
		public ImageResource getValue(PlayerDto object) {
			ImageResource image = null;

			switch (object.getPlayerSingleStats().getTendency()) {
			case Upward:
				image = KickerIcons.ICON.tendencyUp();
				break;
			case Downward:
				image = KickerIcons.ICON.tendencyDown();
				break;
			default:
				image = KickerIcons.ICON.tendencyConstant();
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

	public ValueProvider<PlayerDto, Integer> doubleMatches = new ValueProvider<PlayerDto, Integer>() {
		@Override
		public Integer getValue(PlayerDto object) {
			final PlayerDoubleStatsDto playerDoubleStatsDto = object.getPlayerDoubleStats();
			return playerDoubleStatsDto.getWins() + playerDoubleStatsDto.getLosses();
		}

		@Override
		public void setValue(PlayerDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "doubleMatches";
		}
	};

	public ValueProvider<PlayerDto, Integer> doubleWins = new ValueProvider<PlayerDto, Integer>() {
		@Override
		public Integer getValue(PlayerDto object) {
			return object.getPlayerDoubleStats().getWins();
		}

		@Override
		public void setValue(PlayerDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "doubleWins";
		}
	};

	public ValueProvider<PlayerDto, Integer> doubleLosses = new ValueProvider<PlayerDto, Integer>() {
		@Override
		public Integer getValue(PlayerDto object) {
			return object.getPlayerDoubleStats().getLosses();
		}

		@Override
		public void setValue(PlayerDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "doubleLosses";
		}
	};

	public ValueProvider<PlayerDto, String> doubleGoals = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerDoubleStatsDto playerDoubleStatsDto = object.getPlayerDoubleStats();
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

	public ValueProvider<PlayerDto, String> doubleGoalDifference = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerDoubleStatsDto playerDoubleStatsDto = object.getPlayerDoubleStats();
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

	public ValueProvider<PlayerDto, String> doublePoints = new ValueProvider<PlayerDto, String>() {
		@Override
		public String getValue(PlayerDto object) {
			return Integer.toString(object.getPlayerDoubleStats().getPoints());
		}

		@Override
		public void setValue(PlayerDto object, String value) {
		}

		@Override
		public String getPath() {
			return "doublePoints";
		}
	};

	public ValueProvider<PlayerDto, ImageResource> doubleTendency = new ValueProvider<PlayerDto, ImageResource>() {
		@Override
		public ImageResource getValue(PlayerDto object) {
			ImageResource image = null;

			switch (object.getPlayerDoubleStats().getTendency()) {
			case Upward:
				image = KickerIcons.ICON.tendencyUp();
				break;
			case Downward:
				image = KickerIcons.ICON.tendencyDown();
				break;
			default:
				image = KickerIcons.ICON.tendencyConstant();
				break;
			}
			return image;
		}

		@Override
		public void setValue(PlayerDto object, ImageResource value) {
		}

		@Override
		public String getPath() {
			return "doubleTendency	";
		}
	};

}
