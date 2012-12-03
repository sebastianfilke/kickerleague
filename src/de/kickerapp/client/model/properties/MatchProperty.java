package de.kickerapp.client.model.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.match.MatchData;

public interface MatchProperty extends PropertyAccess<MatchData> {

	public ModelKeyProvider<MatchData> id();

	@Path("name")
	public LabelProvider<MatchData> label();

	public ValueProvider<MatchData, String> set1();

	public ValueProvider<MatchData, String> player1();

	public ValueProvider<MatchData, String> player2();

}
