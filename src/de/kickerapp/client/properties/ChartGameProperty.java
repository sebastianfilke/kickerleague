package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.ChartGameDto;

public interface ChartGameProperty extends PropertyAccess<ChartGameDto> {

	@Path("id")
	ModelKeyProvider<ChartGameDto> id();

	ValueProvider<ChartGameDto, String> month();

	ValueProvider<ChartGameDto, Integer> wins();

	ValueProvider<ChartGameDto, Integer> defeats();

	ValueProvider<ChartGameDto, Integer> winDifference = new ValueProvider<ChartGameDto, Integer>() {
		@Override
		public Integer getValue(ChartGameDto object) {
			final int winDifference = object.getWins() - object.getDefeats();
			return winDifference;
		}

		@Override
		public void setValue(ChartGameDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "winDifference";
		}
	};

}
