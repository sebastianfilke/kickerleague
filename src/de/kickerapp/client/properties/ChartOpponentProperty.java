package de.kickerapp.client.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.kickerapp.shared.dto.ChartOpponentDto;

public interface ChartOpponentProperty extends BaseProperty<ChartOpponentDto> {

	ValueProvider<ChartOpponentDto, Integer> playedMatches = new ValueProvider<ChartOpponentDto, Integer>() {
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

	ValueProvider<ChartOpponentDto, String> opponentName();

}
