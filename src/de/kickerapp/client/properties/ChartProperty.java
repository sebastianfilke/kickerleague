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

	public ValueProvider<ChartDto, Integer> goalDifference();

}
