package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.ChartDto;

public interface ChartProperty extends PropertyAccess<ChartDto> {

	@Path("id")
	public ModelKeyProvider<ChartDto> id();

	public ValueProvider<ChartDto, String> month();

	public ValueProvider<ChartDto, Integer> shotGoals();

	public ValueProvider<ChartDto, Integer> getGoals();

	public ValueProvider<ChartDto, Integer> goalDifference = new ValueProvider<ChartDto, Integer>() {
		@Override
		public Integer getValue(ChartDto object) {
			final int goalDifference = object.getShotGoals() - object.getGetGoals();
			return goalDifference;
		}

		@Override
		public void setValue(ChartDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "goalDifference";
		}
	};

	public ValueProvider<ChartDto, Integer> wins();

	public ValueProvider<ChartDto, Integer> losses();

	public ValueProvider<ChartDto, Integer> winDifference = new ValueProvider<ChartDto, Integer>() {
		@Override
		public Integer getValue(ChartDto object) {
			final int winDifference = object.getWins() - object.getLosses();
			return winDifference;
		}

		@Override
		public void setValue(ChartDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "winDifference";
		}
	};

}
