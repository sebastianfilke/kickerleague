package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.ChartGoalDto;

public interface ChartGoalProperty extends PropertyAccess<ChartGoalDto> {

	@Path("id")
	public ModelKeyProvider<ChartGoalDto> id();

	public ValueProvider<ChartGoalDto, String> month();

	public ValueProvider<ChartGoalDto, Integer> shotGoals();

	public ValueProvider<ChartGoalDto, Integer> getGoals();

	public ValueProvider<ChartGoalDto, Integer> goalDifference = new ValueProvider<ChartGoalDto, Integer>() {
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
