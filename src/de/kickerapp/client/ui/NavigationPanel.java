package de.kickerapp.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.NavigationEvent;
import de.kickerapp.client.widgets.AppToggleButton;

public class NavigationPanel extends BasePanel {

	private AppToggleButton tbnTable;

	private AppToggleButton tbnResult;

	private AppToggleButton tbnInput;

	private AppToggleButton tbnPlayer;

	private ToggleGroup tgroup;

	/**
	 * Erzeugt einen neuen Controller zum Eintragen neuer Spieler für die
	 * Applikation.
	 */
	public NavigationPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		super.initLayout();
		setHeaderVisible(true);

		initButtons();
		initButtonHandlers();

		add(createButtonLayout());
	}

	private void initButtons() {
		tgroup = new ToggleGroup();

		tbnTable = createButton("Tabelle");
		tbnResult = createButton("Ergebnisse");
		tbnInput = createButton("Spiel eintragen");
		tbnPlayer = createButton("Spieler");

		tgroup.setValue(tbnTable);
	}

	private void initButtonHandlers() {
		tbnTable.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.TABLE));
				}
			}
		});
		tbnResult.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.RESULT));
				}
			}
		});
		tbnInput.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.INPUT));
				}
			}
		});
		tbnPlayer.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.PLAYER));
				}
			}
		});
	}

	/**
	 * Hilfsmethode zum Erzeugen eines <code>Button</code> für die
	 * Navigationsleiste.
	 * 
	 * @param title Der Titel des <code>Button</code> als <code>String</code>.
	 * @return Der erzeugte <code>Button</code>.
	 */
	private AppToggleButton createButton(String title) {
		final AppToggleButton button = new AppToggleButton(title);
		button.setAllowDepress(false);
		button.setHeight(30);
		tgroup.add(button);

		return button;
	}

	private VBoxLayoutContainer createButtonLayout() {
		final VBoxLayoutContainer vlcButtons = new VBoxLayoutContainer();
		vlcButtons.setPadding(new Padding(5));
		vlcButtons.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);

		vlcButtons.add(tbnTable, new BoxLayoutData(new Margins(0, 0, 10, 0)));
		vlcButtons.add(tbnResult, new BoxLayoutData(new Margins(0, 0, 10, 0)));
		vlcButtons.add(tbnInput, new BoxLayoutData(new Margins(0, 0, 10, 0)));
		vlcButtons.add(tbnPlayer, new BoxLayoutData(new Margins()));

		return vlcButtons;
	}
}
