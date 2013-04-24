package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.ChartDataDto;

public interface ChartProperty extends PropertyAccess<ChartDataDto> {

	@Path("id")
	public ModelKeyProvider<ChartDataDto> id();

	public ValueProvider<ChartDataDto, String> month();

	public ValueProvider<ChartDataDto, Integer> shotGoals();

	public ValueProvider<ChartDataDto, Integer> getGoals();

	public ValueProvider<ChartDataDto, Integer> goalDifference = new ValueProvider<ChartDataDto, Integer>() {
		@Override
		public Integer getValue(ChartDataDto object) {
			final int goalDifference = object.getShotGoals() - object.getGetGoals();
			return goalDifference;
		}

		@Override
		public void setValue(ChartDataDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "goalDifference";
		}
	};

	public ValueProvider<ChartDataDto, Integer> wins();

	public ValueProvider<ChartDataDto, Integer> defeats();

	public ValueProvider<ChartDataDto, Integer> winDifference = new ValueProvider<ChartDataDto, Integer>() {
		@Override
		public Integer getValue(ChartDataDto object) {
			final int winDifference = object.getWins() - object.getDefeats();
			return winDifference;
		}

		@Override
		public void setValue(ChartDataDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "winDifference";
		}
	};

}
