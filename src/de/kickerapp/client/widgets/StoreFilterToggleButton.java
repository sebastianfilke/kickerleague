package de.kickerapp.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.button.ToggleButton;

/**
 * Erweiterung des {@link ToggleButton}s für die Applikation.
 * 
 * @author Sebastian Filke
 * @param <T> Der Datentyp des ToggleButtons.
 */
public abstract class StoreFilterToggleButton<T> extends ToggleButton {

	/** Die Datenspeicher welche gefiltert werden sollen. */
	private List<Store<T>> stores = new ArrayList<Store<T>>();
	/** Der Filter für die Datenspeicher. */
	private StoreFilter<T> storeFilter;

	/**
	 * Erzeugt einen neuen StoreFilterToggleButton.
	 */
	public StoreFilterToggleButton() {
		super();

		storeFilter = new StoreFilter<T>() {
			@Override
			public boolean select(Store<T> store, T parent, T item) {
				final boolean v = getValue();
				return doSelect(store, parent, item, v);
			}
		};
		addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				onFilter();
			}
		});
	}

	/**
	 * Verbindet den Store mit dem Feld.
	 * 
	 * @param store Der hinzuzufügende Store.
	 */
	public void bind(Store<T> store) {
		stores.add(store);
		onFilter();
	}

	/**
	 * Wendet den Filter auf den Store an.
	 * 
	 * @param store Der zu filternde Store.
	 */
	private void applyFilters(Store<T> store) {
		if (!getValue()) {
			store.addFilter(storeFilter);
			store.setEnableFilters(true);
		} else {
			store.removeFilter(storeFilter);
		}
	}

	/**
	 * Wendet den Filter auf alle verbundenen Stores an.
	 */
	private void onFilter() {
		for (Store<T> s : stores) {
			applyFilters(s);
		}
	}

	/**
	 * Zu überschreibende Methode zur Filterung der einzelnen Elemente.
	 * 
	 * @param store Der Store.
	 * @param parent Der Vaterdatensatz.
	 * @param item Der Datensatz.
	 * @param filter Der Filter.
	 * @return <code>true</code> falls das das Element sichtbar sein soll, andernfalls <code>false</code>.
	 */
	protected abstract boolean doSelect(Store<T> store, T parent, T item, boolean filter);

}
