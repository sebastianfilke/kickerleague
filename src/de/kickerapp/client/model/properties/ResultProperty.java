package de.kickerapp.client.model.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.match.ResultData;

public interface ResultProperty extends PropertyAccess<ResultData> {

	public ModelKeyProvider<ResultData> id();

	@Path("name")
	public LabelProvider<ResultData> nameLabel();

	public ValueProvider<ResultData, String> name();

}
