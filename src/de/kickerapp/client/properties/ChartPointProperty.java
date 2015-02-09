package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.ChartPointDto;

public interface ChartPointProperty extends PropertyAccess<ChartPointDto> {

	@Path("id")
	ModelKeyProvider<ChartPointDto> id();

	ValueProvider<ChartPointDto, Integer> matchNumber();

	ValueProvider<ChartPointDto, Integer> points();

}
