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
 * @param <T> Der Datentyp der ComboBox.
 */
public class AppComboBox<T> extends ComboBox<T> {

	/**
	 * Erzeugt eine neue ComboBox mit übergebenem {@link ListStore} und
	 * {@link LabelProvider}.
	 * 
	 * @param store Der Datenspeicher der ComboBox.
	 * @param labelProvider Der LabelProvider der ComboBox.
	 */
	public AppComboBox(ListStore<T> store, LabelProvider<? super T> labelProvider) {
		super(store, labelProvider);
	}

	/**
	 * Erzeugt ein neues TextField mit übergebenem Standardtext, falls das
	 * Textfield leer ist.
	 * 
	 * @param emptyText Der Standardtext des TextFields als {@link String}.
	 */
	public AppComboBox(ListStore<T> store, LabelProvider<? super T> labelProvider, String emptyText) {
		super(store, labelProvider);
		setEmptyText(emptyText);
	}
	
	/**
	   * Creates a new combo box with the given cell.
	   * 
	   * @param cell the cell
	   */
	  public AppComboBox(ComboBoxCell<T> cell, String emptyText) {
	    super(cell);
	    setEmptyText(emptyText);
	  }

}
