package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.ChartPointDto;

public interface ChartPointProperty extends PropertyAccess<ChartPointDto> {

	@Path("id")
	public ModelKeyProvider<ChartPointDto> id();

	public ValueProvider<ChartPointDto, Integer> matchNumber();

	public ValueProvider<ChartPointDto, Integer> points();

}
