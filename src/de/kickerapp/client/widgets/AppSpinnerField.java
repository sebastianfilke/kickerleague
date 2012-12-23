package de.kickerapp.client.widgets;

import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;

/**
 * Erweiterung des {@link SpinnerField}s für die Application.
 * 
 * @author Sebastian Filke
 * @param <N> Der Datentyp des Spinnerfields.
 */
public class AppSpinnerField<N extends Number> extends SpinnerField<N> {

	/**
	 * Erzeugt ein neues Spinnerfield mit übergebenem
	 * {@link NumberPropertyEditor}.
	 * 
	 * @param editor Der Editor.
	 */
	public AppSpinnerField(NumberPropertyEditor<N> editor) {
		super(editor);
	}

}
