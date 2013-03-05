package de.kickerapp.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.button.ToggleButton;

public abstract class StoreFilterToggleButton<T> extends ToggleButton {

	protected List<Store<T>> stores = new ArrayList<Store<T>>();

	protected StoreFilter<T> filter;

	public StoreFilterToggleButton() {
		super();

		filter = new StoreFilter<T>() {
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

	public void bind(Store<T> store) {
		stores.add(store);
		onFilter();
	};

	protected void applyFilters(Store<T> store) {
		if (!getValue()) {
			store.addFilter(filter);
			store.setEnableFilters(true);
		} else {
			store.removeFilter(filter);
		}
	}

	protected void onFilter() {
		for (Store<T> s : stores) {
			applyFilters(s);
		}
	}

	protected abstract boolean doSelect(Store<T> store, T parent, T item, boolean filter);

}
