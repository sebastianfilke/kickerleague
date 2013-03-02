package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.IChart;
import de.kickerapp.shared.dto.IMatch;

public interface ChartProperty extends PropertyAccess<IChart> {

	@Path("id")
	public ModelKeyProvider<IChart> id();
}
