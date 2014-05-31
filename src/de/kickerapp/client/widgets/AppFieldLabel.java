package de.kickerapp.client.widgets;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;

/**
 * Erweiterung des {@link FieldLabel} für die Applikation.
 * 
 * @author Sebastian Filke
 */
public class AppFieldLabel extends FieldLabel {

	/**
	 * Erzeugt ein neues FieldLabel.
	 */
	public AppFieldLabel() {
		super();
	}

	/**
	 * Erzeugt ein neues FieldLabel mit übergebenem Widget, Text und Ausrichtung.
	 * 
	 * @param widget Das Widget für das FieldLabel.
	 * @param label Der Text für das FieldLabel.
	 * @param labelAlign Die Ausrichtung für das FieldLabel.
	 */
	public AppFieldLabel(IsWidget widget, String label, LabelAlign labelAlign) {
		super(widget, label);
		setLabelAlign(labelAlign);
	}

}
