package de.kickerapp.client.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.event.UpdatePanelEventHandler;
import de.kickerapp.client.properties.TableProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.shared.common.Tendency;
import de.kickerapp.shared.dto.IPlayer;
import de.kickerapp.shared.dto.PlayerDto;

public class TablePanel extends BasePanel implements UpdatePanelEventHandler {

	private ListStore<IPlayer> store;
	private AppButton btnUpdate;

	public TablePanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();

		AppEventBus.addHandler(UpdatePanelEvent.ALL_PANEL, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		super.initLayout();

		add(createGrid());
	}

	public Grid<IPlayer> createGrid() {
		final TableProperty tableProperty = GWT.create(TableProperty.class);

		final IdentityValueProvider<IPlayer> identity = new IdentityValueProvider<IPlayer>();
		final RowNumberer<IPlayer> numberer = new RowNumberer<IPlayer>(identity);

		final ColumnConfig<IPlayer, String> ccPlayerName = new ColumnConfig<IPlayer, String>(tableProperty.label(), 140, "Spieler");
		final ColumnConfig<IPlayer, Integer> ccSingleMatches = new ColumnConfig<IPlayer, Integer>(tableProperty.singleMatches(), 100, "Anzahl Spiele");
		final ColumnConfig<IPlayer, Integer> ccSingleWins = new ColumnConfig<IPlayer, Integer>(tableProperty.singleWins(), 100, "Siege");
		final ColumnConfig<IPlayer, Integer> ccSingleLosses = new ColumnConfig<IPlayer, Integer>(tableProperty.singleLosses(), 100, "Niederlagen");
		final ColumnConfig<IPlayer, String> ccSingleGoals = new ColumnConfig<IPlayer, String>(tableProperty.singleGoals(), 100, "Tore");
		final ColumnConfig<IPlayer, String> ccSingleGoalDifference = new ColumnConfig<IPlayer, String>(tableProperty.singleGoalDifference(), 100, "Tordifferenz");
		final ColumnConfig<IPlayer, Integer> ccPoints = new ColumnConfig<IPlayer, Integer>(tableProperty.points(), 100, "Punkte");
		final ColumnConfig<IPlayer, Tendency> ccTendency = new ColumnConfig<IPlayer, Tendency>(tableProperty.tendency(), 60, "Tendenz");

		final ArrayList<ColumnConfig<IPlayer, ?>> columns = new ArrayList<ColumnConfig<IPlayer, ?>>();
		columns.add(numberer);
		columns.add(ccPlayerName);
		columns.add(ccSingleMatches);
		columns.add(ccSingleWins);
		columns.add(ccSingleLosses);
		columns.add(ccSingleGoals);
		columns.add(ccSingleGoalDifference);
		columns.add(ccPoints);
		columns.add(ccTendency);

		store = new ListStore<IPlayer>(tableProperty.id());

		final Grid<IPlayer> grid = new Grid<IPlayer>(store, new ColumnModel<IPlayer>(columns));
		grid.getView().setAutoExpandColumn(ccPlayerName);
		grid.getView().setAutoExpandMax(1000);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);

		return grid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initPanelButtons(Portlet portletTable) {
		btnUpdate = new AppButton("Aktualisieren");
		btnUpdate.setToolTip("Aktualisiert die Tabelle");
		btnUpdate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				getTable();
			}
		});
		portletTable.addButton(btnUpdate);
	}

	private void getTable() {
		btnUpdate.setEnabled(false);
		mask("Aktualisiere...");
		store.clear();
		KickerServices.PLAYER_SERVICE.getAllPlayers(new AsyncCallback<ArrayList<PlayerDto>>() {
			@Override
			public void onSuccess(ArrayList<PlayerDto> result) {
				store.addAll(result);
				btnUpdate.setEnabled(true);
				unmask();
			}

			@Override
			public void onFailure(Throwable caught) {
				btnUpdate.setEnabled(true);
				unmask();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePanel() {
		getTable();
	}

}
