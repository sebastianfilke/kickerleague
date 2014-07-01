package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.ChartOpponentDto;

public interface ChartOpponentProperty extends PropertyAccess<ChartOpponentDto> {

	@Path("id")
	public ModelKeyProvider<ChartOpponentDto> id();

	public ValueProvider<ChartOpponentDto, String> opponentName();

	public ValueProvider<ChartOpponentDto, Integer> playedGames();

}
