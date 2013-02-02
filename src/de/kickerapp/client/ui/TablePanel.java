package de.kickerapp.client.ui;

import java.util.ArrayList;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.theme.gray.client.tabs.GrayTabPanelBottomAppearance;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.TabPanel.TabPanelAppearance;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.ShowDataEvent;
import de.kickerapp.client.event.ShowDataEventHandler;
import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.properties.TeamProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.shared.common.Tendency;
import de.kickerapp.shared.dto.IPlayer;
import de.kickerapp.shared.dto.ITeam;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;

public class TablePanel extends BasePanel implements ShowDataEventHandler {

	private ListStore<IPlayer> storeSingleTable;

	private ListStore<ITeam> storeDoubleTable;

	private AppButton btnUpdate;

	private int activeWidget;

	private TabPanel tabPanel;

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

		AppEventBus.addHandler(ShowDataEvent.ALL_PANEL, this);
		AppEventBus.addHandler(ShowDataEvent.TABLE_PANEL, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		super.initLayout();

		activeWidget = 0;

		tabPanel = new TabPanel((TabPanelAppearance) GWT.create(GrayTabPanelBottomAppearance.class));
		tabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				final TabPanel panel = (TabPanel) event.getSource();
				final Widget w = event.getSelectedItem();

				activeWidget = panel.getWidgetIndex(w);

				final UpdatePanelEvent updateEvent = new UpdatePanelEvent();
				updateEvent.setActiveWidget(activeWidget);
				AppEventBus.fireEvent(updateEvent);
			}
		});

		tabPanel.add(createSingleTableGrid(), "Einzel");
		tabPanel.add(createDoubleTableGrid(), "Doppel");
		tabPanel.setBodyBorder(false);
		tabPanel.setBorders(false);

		add(tabPanel);
	}

	public Grid<IPlayer> createSingleTableGrid() {
		final IdentityValueProvider<IPlayer> identity = new IdentityValueProvider<IPlayer>();
		final RowNumberer<IPlayer> numberer = new RowNumberer<IPlayer>(identity);
		numberer.setCell(new AbstractCell<IPlayer>() {
			@Override
			public void render(Context context, IPlayer value, SafeHtmlBuilder sb) {
				sb.append(value.getCurTablePlace());
			}
		});

		final ColumnConfig<IPlayer, String> ccPlayerName = new ColumnConfig<IPlayer, String>(KickerProperties.TABLE_PROPERTY.label(), 140, "Spieler");
		final ColumnConfig<IPlayer, Integer> ccSingleMatches = new ColumnConfig<IPlayer, Integer>(KickerProperties.TABLE_PROPERTY.singleMatches(), 100,
				"Anzahl Spiele");
		final ColumnConfig<IPlayer, Integer> ccSingleWins = new ColumnConfig<IPlayer, Integer>(KickerProperties.TABLE_PROPERTY.singleWins(), 100, "Siege");
		final ColumnConfig<IPlayer, Integer> ccSingleLosses = new ColumnConfig<IPlayer, Integer>(KickerProperties.TABLE_PROPERTY.singleLosses(), 100,
				"Niederlagen");
		final ColumnConfig<IPlayer, String> ccSingleGoals = new ColumnConfig<IPlayer, String>(KickerProperties.TABLE_PROPERTY.singleGoals(), 100, "Tore");
		final ColumnConfig<IPlayer, String> ccSingleGoalDifference = new ColumnConfig<IPlayer, String>(KickerProperties.TABLE_PROPERTY.singleGoalDifference(),
				100, "Tordifferenz");
		final ColumnConfig<IPlayer, Integer> ccPoints = new ColumnConfig<IPlayer, Integer>(KickerProperties.TABLE_PROPERTY.points(), 100, "Punkte");
		final ColumnConfig<IPlayer, Tendency> ccTendency = new ColumnConfig<IPlayer, Tendency>(KickerProperties.TABLE_PROPERTY.tendency(), 60, "Tendenz");

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

		storeSingleTable = new ListStore<IPlayer>(KickerProperties.TABLE_PROPERTY.id());

		final Grid<IPlayer> grid = new Grid<IPlayer>(storeSingleTable, new ColumnModel<IPlayer>(columns));
		grid.getView().setAutoExpandColumn(ccPlayerName);
		grid.getView().setAutoExpandMax(1000);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);

		return grid;
	}

	public Grid<ITeam> createDoubleTableGrid() {
		final IdentityValueProvider<ITeam> identity = new IdentityValueProvider<ITeam>();
		final RowNumberer<ITeam> numberer = new RowNumberer<ITeam>(identity);
		numberer.setCell(new AbstractCell<ITeam>() {
			@Override
			public void render(Context context, ITeam value, SafeHtmlBuilder sb) {
				sb.append(value.getCurTablePlace());
			}
		});

		final ColumnConfig<ITeam, String> ccPlayerName = new ColumnConfig<ITeam, String>(TeamProperty.teamLabel, 140, "Team");
		final ColumnConfig<ITeam, Integer> ccSingleMatches = new ColumnConfig<ITeam, Integer>(TeamProperty.matches, 100, "Anzahl Spiele");
		final ColumnConfig<ITeam, Integer> ccSingleWins = new ColumnConfig<ITeam, Integer>(KickerProperties.TEAM_PROPERTY.wins(), 100, "Siege");
		final ColumnConfig<ITeam, Integer> ccSingleLosses = new ColumnConfig<ITeam, Integer>(KickerProperties.TEAM_PROPERTY.losses(), 100, "Niederlagen");
		final ColumnConfig<ITeam, String> ccSingleGoals = new ColumnConfig<ITeam, String>(TeamProperty.goals, 100, "Tore");
		final ColumnConfig<ITeam, String> ccSingleGoalDifference = new ColumnConfig<ITeam, String>(TeamProperty.goalDifference, 100, "Tordifferenz");
		final ColumnConfig<ITeam, Integer> ccPoints = new ColumnConfig<ITeam, Integer>(KickerProperties.TEAM_PROPERTY.points(), 100, "Punkte");
		final ColumnConfig<ITeam, Tendency> ccTendency = new ColumnConfig<ITeam, Tendency>(KickerProperties.TEAM_PROPERTY.tendency(), 60, "Tendenz");

		final ArrayList<ColumnConfig<ITeam, ?>> columns = new ArrayList<ColumnConfig<ITeam, ?>>();
		columns.add(numberer);
		columns.add(ccPlayerName);
		columns.add(ccSingleMatches);
		columns.add(ccSingleWins);
		columns.add(ccSingleLosses);
		columns.add(ccSingleGoals);
		columns.add(ccSingleGoalDifference);
		columns.add(ccPoints);
		columns.add(ccTendency);

		storeDoubleTable = new ListStore<ITeam>(KickerProperties.TEAM_PROPERTY.id());

		final Grid<ITeam> grid = new Grid<ITeam>(storeDoubleTable, new ColumnModel<ITeam>(columns));
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
		if (activeWidget == 0) {
			getSingleTable();
		} else {
			getDoubleTable();
		}
	}

	private void getSingleTable() {
		btnUpdate.setEnabled(false);
		mask("Aktualisiere...");
		storeSingleTable.clear();

		KickerServices.PLAYER_SERVICE.getAllPlayers(new AsyncCallback<ArrayList<PlayerDto>>() {
			@Override
			public void onSuccess(ArrayList<PlayerDto> result) {
				storeSingleTable.addAll(result);
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

	private void getDoubleTable() {
		btnUpdate.setEnabled(false);
		mask("Aktualisiere...");
		storeDoubleTable.clear();

		KickerServices.TEAM_SERVICE.getAllTeams(new AsyncCallback<ArrayList<TeamDto>>() {
			@Override
			public void onSuccess(ArrayList<TeamDto> result) {
				storeDoubleTable.addAll(result);
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
	public void showData(ShowDataEvent event) {
		if (activeWidget != event.getActiveWidget()) {
			activeWidget = event.getActiveWidget();
			tabPanel.setActiveWidget(tabPanel.getWidget(activeWidget));
		}
		getTable();
	}

}
