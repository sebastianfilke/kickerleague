package de.kickerapp.client.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.kickerapp.shared.dto.ChartGoalDto;

public interface ChartGoalProperty extends BaseProperty<ChartGoalDto> {

	ValueProvider<ChartGoalDto, String> month();

	ValueProvider<ChartGoalDto, Integer> shotGoals();

	ValueProvider<ChartGoalDto, Integer> getGoals();

	ValueProvider<ChartGoalDto, Integer> goalDifference = new ValueProvider<ChartGoalDto, Integer>() {
		@Override
		public Integer getValue(ChartGoalDto object) {
			final int goalDifference = object.getShotGoals() - object.getGetGoals();
			return goalDifference;
		}

		@Override
		public void setValue(ChartGoalDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "goalDifference";
		}
	};

}
