package de.kickerapp.client.widgets;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * Erweiterung des {@link TextButton} für die Applikation.
 * 
 * @author Sebastian Filke
 */
public class AppButton extends TextButton {

	/**
	 * Erzeugt einen neuen TextButton.
	 */
	public AppButton() {
		super();
	}

	/**
	 * Erzeugt einen neuen TextButton mit übergebenem Text.
	 * 
	 * @param text Der Text des Buttons als {@link String}.
	 */
	public AppButton(String text) {
		super(text);
	}

	/**
	 * Erzeugt einen neuen TextButton mit übergebenem Text und Icon.
	 * 
	 * @param text Der Text des Buttons als {@link String}.
	 * @param icon Das Icon des Buttons als {@link ImageResource}.
	 */
	public AppButton(String text, ImageResource icon) {
		super(text, icon);
	}

}
