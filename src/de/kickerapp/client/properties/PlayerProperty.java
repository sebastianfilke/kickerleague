package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.common.Tendency;
import de.kickerapp.shared.dto.IMatch;
import de.kickerapp.shared.dto.IPlayer;
import de.kickerapp.shared.dto.PlayerDoubleStatsDto;
import de.kickerapp.shared.dto.PlayerSingleStatsDto;

public interface PlayerProperty extends PropertyAccess<IMatch> {

	@Path("id")
	public ModelKeyProvider<IPlayer> id();

	@Path("label")
	public LabelProvider<IPlayer> label();

	public ValueProvider<IPlayer, String> playerName = new ValueProvider<IPlayer, String>() {
		@Override
		public String getValue(IPlayer object) {
			final StringBuilder sb = new StringBuilder();

			sb.append(object.getLastName());
			sb.append(", ");
			sb.append(object.getFirstName());

			return sb.toString();
		}

		@Override
		public void setValue(IPlayer object, String value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, Integer> singleMatches = new ValueProvider<IPlayer, Integer>() {
		@Override
		public Integer getValue(IPlayer object) {
			final PlayerSingleStatsDto playerSingleStatsDto = object.getPlayerSingleStats();
			return playerSingleStatsDto.getSingleWins() + playerSingleStatsDto.getSingleLosses();
		}

		@Override
		public void setValue(IPlayer object, Integer value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, Integer> singleWins = new ValueProvider<IPlayer, Integer>() {
		@Override
		public Integer getValue(IPlayer object) {
			return object.getPlayerSingleStats().getSingleWins();
		}

		@Override
		public void setValue(IPlayer object, Integer value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, Integer> singleLosses = new ValueProvider<IPlayer, Integer>() {
		@Override
		public Integer getValue(IPlayer object) {
			return object.getPlayerSingleStats().getSingleLosses();
		}

		@Override
		public void setValue(IPlayer object, Integer value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, String> singleGoals = new ValueProvider<IPlayer, String>() {
		@Override
		public String getValue(IPlayer object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerSingleStatsDto playerSingleStatsDto = object.getPlayerSingleStats();
			sb.append(playerSingleStatsDto.getSingleShotGoals());
			sb.append(":");
			sb.append(playerSingleStatsDto.getSingleGetGoals());

			return sb.toString();
		}

		@Override
		public void setValue(IPlayer object, String value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, String> singleGoalDifference = new ValueProvider<IPlayer, String>() {
		@Override
		public String getValue(IPlayer object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerSingleStatsDto playerSingleStatsDto = object.getPlayerSingleStats();
			final int goalDifference = playerSingleStatsDto.getSingleShotGoals() - playerSingleStatsDto.getSingleGetGoals();
			if (goalDifference >= 0) {
				sb.append("+" + Integer.toString(goalDifference));
			} else {
				sb.append(Integer.toString(goalDifference));
			}
			return sb.toString();
		}

		@Override
		public void setValue(IPlayer object, String value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, Integer> singlePoints = new ValueProvider<IPlayer, Integer>() {
		@Override
		public Integer getValue(IPlayer object) {
			return object.getPlayerSingleStats().getSinglePoints();
		}

		@Override
		public void setValue(IPlayer object, Integer value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, Tendency> singleTendency = new ValueProvider<IPlayer, Tendency>() {
		@Override
		public Tendency getValue(IPlayer object) {
			return object.getPlayerSingleStats().getSingleTendency();
		}

		@Override
		public void setValue(IPlayer object, Tendency value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, Integer> doubleMatches = new ValueProvider<IPlayer, Integer>() {
		@Override
		public Integer getValue(IPlayer object) {
			final PlayerDoubleStatsDto playerDoubleStatsDto = object.getPlayerDoubleStats();
			return playerDoubleStatsDto.getDoubleWins() + playerDoubleStatsDto.getDoubleLosses();
		}

		@Override
		public void setValue(IPlayer object, Integer value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, Integer> doubleWins = new ValueProvider<IPlayer, Integer>() {
		@Override
		public Integer getValue(IPlayer object) {
			return object.getPlayerDoubleStats().getDoubleWins();
		}

		@Override
		public void setValue(IPlayer object, Integer value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, Integer> doubleLosses = new ValueProvider<IPlayer, Integer>() {
		@Override
		public Integer getValue(IPlayer object) {
			return object.getPlayerDoubleStats().getDoubleLosses();
		}

		@Override
		public void setValue(IPlayer object, Integer value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, String> doubleGoals = new ValueProvider<IPlayer, String>() {
		@Override
		public String getValue(IPlayer object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerDoubleStatsDto playerDoubleStatsDto = object.getPlayerDoubleStats();
			sb.append(playerDoubleStatsDto.getDoubleShotGoals());
			sb.append(":");
			sb.append(playerDoubleStatsDto.getDoubleGetGoals());

			return sb.toString();
		}

		@Override
		public void setValue(IPlayer object, String value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, String> doubleGoalDifference = new ValueProvider<IPlayer, String>() {
		@Override
		public String getValue(IPlayer object) {
			final StringBuilder sb = new StringBuilder();

			final PlayerDoubleStatsDto playerDoubleStatsDto = object.getPlayerDoubleStats();
			final int goalDifference = playerDoubleStatsDto.getDoubleShotGoals() - playerDoubleStatsDto.getDoubleGetGoals();
			if (goalDifference >= 0) {
				sb.append("+" + Integer.toString(goalDifference));
			} else {
				sb.append(Integer.toString(goalDifference));
			}
			return sb.toString();
		}

		@Override
		public void setValue(IPlayer object, String value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, Integer> doublePoints = new ValueProvider<IPlayer, Integer>() {
		@Override
		public Integer getValue(IPlayer object) {
			return object.getPlayerDoubleStats().getDoublePoints();
		}

		@Override
		public void setValue(IPlayer object, Integer value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IPlayer, Tendency> doubleTendency = new ValueProvider<IPlayer, Tendency>() {
		@Override
		public Tendency getValue(IPlayer object) {
			return object.getPlayerDoubleStats().getDoubleTendency();
		}

		@Override
		public void setValue(IPlayer object, Tendency value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

}
