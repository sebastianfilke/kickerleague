package de.kickerapp.client.model;

import java.util.Date;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.match.IMatch;

public interface MatchProperty extends PropertyAccess<IMatch> {

	@Path("id")
	public ModelKeyProvider<IMatch> id();

	public ValueProvider<IMatch, String> matchNumber();

	public ValueProvider<IMatch, Date> matchDate();

	public ValueProvider<IMatch, String> labelTeam1();

	public ValueProvider<IMatch, String> labelTeam2();

	public ValueProvider<IMatch, String> labelSets();

}
