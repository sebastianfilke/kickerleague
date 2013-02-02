package de.kickerapp.client.widgets;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;

import de.kickerapp.shared.dto.IMatch;

public class AppRowNumberer extends RowNumberer<IMatch> {

	public AppRowNumberer(IdentityValueProvider<IMatch> valueProvider) {
		super(valueProvider);
	}

}
