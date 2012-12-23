package de.kickerapp.client.widgets;

import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * Erweiterung des {@link TextButton} für die Application.
 * 
 * @author Sebastian Filke
 */
public class AppButton extends TextButton {

	/**
	 * Erzeugt einen neuen TextButton.
	 */
	public AppButton() {
		super();
		setWidth(100);
	}

	/**
	 * Erzeugt einen neuen TextButton mit übergebenem Text.
	 * 
	 * @param text Der Text des Buttons als {@link String}.
	 */
	public AppButton(String text) {
		super(text);
	}

}
