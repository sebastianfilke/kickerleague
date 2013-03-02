package de.kickerapp.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.form.CheckBox;

public abstract class StoreFilterCheckBox<T> extends CheckBox {

	protected List<Store<T>> stores = new ArrayList<Store<T>>();

	protected StoreFilter<T> filter;

	public StoreFilterCheckBox() {
		super();
		setAutoValidate(true);
		setValidateOnBlur(false);

		filter = new StoreFilter<T>() {
			@Override
			public boolean select(Store<T> store, T parent, T item) {
				final boolean v = getValue();
				return doSelect(store, parent, item, v);
			}
		};
	}

	public void bind(Store<T> store) {
		stores.add(store);
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
		focus();
	}

	@Override
	protected boolean validateValue(Boolean value) {
		final boolean ret = super.validateValue(value);
		onFilter();
		return ret;
	}

	protected abstract boolean doSelect(Store<T> store, T parent, T item, boolean filter);

}
