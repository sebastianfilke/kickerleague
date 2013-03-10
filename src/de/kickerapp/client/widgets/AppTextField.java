package de.kickerapp.client.widgets;

import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Erweiterung des {@link TextField}s für die Applikation.
 * 
 * @author Sebastian Filke
 */
public class AppTextField extends TextField {

	/**
	 * Erzeugt ein neues TextField.
	 */
	public AppTextField() {
		super();
	}

	/**
	 * Erzeugt ein neues TextField mit übergebenem Standardtext, falls das Textfeld leer ist.
	 * 
	 * @param emptyText Der Standardtext des TextFields als {@link String}.
	 */
	public AppTextField(String emptyText) {
		super();
		setEmptyText(emptyText);
	}

}
