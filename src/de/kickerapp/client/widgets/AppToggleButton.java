package de.kickerapp.client.widgets;

import com.sencha.gxt.widget.core.client.button.ToggleButton;

public class AppToggleButton extends ToggleButton {

	/**
	 * Creates a new toggle button.
	 */
	public AppToggleButton() {
		super();
	}

	/**
	 * Creates a new toggle button.
	 * 
	 * @param text the button text
	 */
	public AppToggleButton(String text) {
		this();
		setText(text);
	}

}
