package de.kickerapp.client.widgets;

import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;

public class AppSpinnerField<N extends Number> extends SpinnerField<N> {

	public AppSpinnerField(NumberPropertyEditor<N> editor) {
		super(editor);
	}

}
