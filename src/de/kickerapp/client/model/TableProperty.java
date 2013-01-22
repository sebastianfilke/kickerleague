package de.kickerapp.client.model;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.common.Tendency;
import de.kickerapp.shared.match.IPlayer;

public interface TableProperty extends PropertyAccess<IPlayer> {

	@Path("id")
	public ModelKeyProvider<IPlayer> id();

	public ValueProvider<IPlayer, String> label();

	public ValueProvider<IPlayer, Integer> singleWins();

	public ValueProvider<IPlayer, Integer> singleLosses();

	public ValueProvider<IPlayer, Integer> singleMatches();

	public ValueProvider<IPlayer, String> singleGoals();

	public ValueProvider<IPlayer, String> singleGoalDifference();

	public ValueProvider<IPlayer, Integer> doubleWins();

	public ValueProvider<IPlayer, Integer> doubleLosses();

	public ValueProvider<IPlayer, String> doubleGoals();

	public ValueProvider<IPlayer, Tendency> tendency();

	public ValueProvider<IPlayer, Integer> points();

}
