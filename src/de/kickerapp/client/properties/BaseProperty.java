package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.BaseDto;

public interface BaseProperty<T extends BaseDto> extends PropertyAccess<T> {

	@Path("id")
	ModelKeyProvider<T> id();

}
