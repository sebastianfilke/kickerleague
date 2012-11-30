package de.kickerapp.client.ui.model.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.client.ui.model.ResultData;

public interface ResultProperty extends PropertyAccess<ResultData> {

	public ModelKeyProvider<ResultData> id();

	@Path("name")
	public LabelProvider<ResultData> label();

	public ValueProvider<ResultData, String> name();

}
