package de.kickerapp.client.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.IMatch;
import de.kickerapp.shared.dto.IPlayer;

public interface PlayerProperty extends PropertyAccess<IMatch> {

	@Path("id")
	public ModelKeyProvider<IPlayer> id();

	@Path("label")
	public LabelProvider<IPlayer> label();

	public ValueProvider<IPlayer, String> lastName();

	public ValueProvider<IPlayer, String> firstName();

	public ValueProvider<IPlayer, String> nickName();

}