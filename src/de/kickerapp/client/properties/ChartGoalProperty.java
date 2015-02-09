package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.ChartGoalDto;

public interface ChartGoalProperty extends PropertyAccess<ChartGoalDto> {

	@Path("id")
	ModelKeyProvider<ChartGoalDto> id();

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
