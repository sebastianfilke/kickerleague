package de.kickerapp.client.model;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.match.ResultDto;

public interface ResultProperty extends PropertyAccess<ResultDto> {

	public ModelKeyProvider<ResultDto> id();

	@Path("matchResult")
	public LabelProvider<ResultDto> nameLabel();

	public ValueProvider<ResultDto, String> matchResult();

}
