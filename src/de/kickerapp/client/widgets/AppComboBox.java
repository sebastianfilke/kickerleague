package de.kickerapp.client.widgets;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent.TriggerClickHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Erweiterung des {@link TextField}s für die Applikation.
 * 
 * @author Sebastian Filke
 * @param <T> Der Datentyp der ComboBox.
 */
public class AppComboBox<T> extends ComboBox<T> {

	/** Die Angabe, ob der Trigger geklickt wurde. */
	private boolean triggerClick;

	/**
	 * Erzeugt eine neue ComboBox mit übergebenem {@link ListStore} und {@link LabelProvider}.
	 * 
	 * @param store Der Datenspeicher der ComboBox.
	 * @param labelProvider Der LabelProvider der ComboBox.
	 */
	public AppComboBox(ListStore<T> store, LabelProvider<? super T> labelProvider) {
		super(store, labelProvider);
		addTriggerClickHandler();
	}

	/**
	 * Erzeugt ein neues TextField mit übergebenem Standardtext, falls die ComboBox leer ist.
	 * 
	 * @param store Der Datenspeicher der ComboBox.
	 * @param labelProvider Der LabelProvider der ComboBox.
	 * @param emptyText Der Standardtext des TextFields als {@link String}.
	 */
	public AppComboBox(ListStore<T> store, LabelProvider<? super T> labelProvider, String emptyText) {
		super(store, labelProvider);
		addTriggerClickHandler();
		setEmptyText(emptyText);
	}

	/**
	 * Erzeugt ein neues TextField mit übergebener {@link ComboBoxCell} und mit übergebenem Standardtext, falls die ComboBox leer ist.
	 * 
	 * @param cell Die Zelle der ComboBox als {@link ComboBoxCell}
	 * @param emptyText Der Standardtext des TextFields als {@link String}.
	 */
	public AppComboBox(ComboBoxCell<T> cell, String emptyText) {
		super(cell);
		addTriggerClickHandler();
		setEmptyText(emptyText);
	}

	/**
	 * Fügt den Händler für Trigger-Events hinzu.
	 */
	private void addTriggerClickHandler() {
		addTriggerClickHandler(new TriggerClickHandler() {
			@Override
			public void onTriggerClick(TriggerClickEvent event) {
				triggerClick = true;
			}
		});
	}

	/**
	 * Liefert die Angabe, ob der Trigger geklickt wurde.
	 * 
	 * @return <code>true</code> falls der Trigger geklickt wurde, andernfalls <code>false</code>.
	 */
	public boolean isTriggerClick() {
		return triggerClick;
	}

	/**
	 * Setzt die Angabe, ob der Trigger geklickt wurde.
	 * 
	 * @param triggerClick <code>true</code> falls der Trigger geklickt wurde, andernfalls <code>false</code>.
	 */
	public void setTriggerClick(boolean triggerClick) {
		this.triggerClick = triggerClick;
	}

}
