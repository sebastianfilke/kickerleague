package de.kickerapp.client.widgets;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Erweiterung des {@link TextField}s für die Application.
 * 
 * @author Sebastian Filke
 * @param T Der Datentyp der ComboBox.
 */
public class AppComboBox<T> extends ComboBox<T> {

	public AppComboBox(ComboBoxCell<T> cell) {
		super(cell);
	}

	public AppComboBox(ListStore<T> store, LabelProvider<? super T> labelProvider) {
		super(store, labelProvider);
	}

}
