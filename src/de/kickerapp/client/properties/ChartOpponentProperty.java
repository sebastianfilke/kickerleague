package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.ChartOpponentDto;

public interface ChartOpponentProperty extends PropertyAccess<ChartOpponentDto> {

	@Path("id")
	public ModelKeyProvider<ChartOpponentDto> id();

	public ValueProvider<ChartOpponentDto, Integer> playedMatches = new ValueProvider<ChartOpponentDto, Integer>() {
		@Override
		public Integer getValue(ChartOpponentDto object) {
			return object.getWins() + object.getDefeats();
		}

		@Override
		public void setValue(ChartOpponentDto object, Integer value) {
		}

		@Override
		public String getPath() {
			return "playedGames";
		}
	};

	public ValueProvider<ChartOpponentDto, String> opponentName();

}
