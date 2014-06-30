package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.ChartGameDataDto;

public interface ChartProperty extends PropertyAccess<ChartGameDataDto> {

	@Path("id")
	public ModelKeyProvider<ChartGameDataDto> id();

	public ValueProvider<ChartGameDataDto, String> month();

	public ValueProvider<ChartGameDataDto, Integer> shotGoals();

	public ValueProvider<ChartGameDataDto, Integer> getGoals();

	public ValueProvider<ChartGameDataDto, Integer> goalDifference = new ValueProvider<ChartGameDataDto, Integer>() {
		@Override
		public Integer getValue(ChartGameDataDto object) {
			final int goalDifference = object.getShotGoals() - object.getGetGoals();
			return goalDifference;
		}

		@Override
		public void setValue(ChartGameDataDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "goalDifference";
		}
	};

	public ValueProvider<ChartGameDataDto, Integer> wins();

	public ValueProvider<ChartGameDataDto, Integer> defeats();

	public ValueProvider<ChartGameDataDto, Integer> winDifference = new ValueProvider<ChartGameDataDto, Integer>() {
		@Override
		public Integer getValue(ChartGameDataDto object) {
			final int winDifference = object.getWins() - object.getDefeats();
			return winDifference;
		}

		@Override
		public void setValue(ChartGameDataDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "winDifference";
		}
	};

}
