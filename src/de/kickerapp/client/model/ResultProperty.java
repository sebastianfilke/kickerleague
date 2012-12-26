package de.kickerapp.client.model;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.match.IResult;

public interface ResultProperty extends PropertyAccess<IResult>, IResult {

	@Path("id")
	public ModelKeyProvider<IResult> id();

	public ValueProvider<IResult, String> matchResult();

}
