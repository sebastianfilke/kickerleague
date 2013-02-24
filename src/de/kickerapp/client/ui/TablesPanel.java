package de.kickerapp.client.ui;

import java.util.ArrayList;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.ShowDataEvent;
import de.kickerapp.client.event.ShowDataEventHandler;
import de.kickerapp.client.event.TabPanelEvent;
import de.kickerapp.client.event.TabPanelEventHandler;
import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.event.UpdatePanelEventHandler;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.properties.PlayerProperty;
import de.kickerapp.client.properties.TeamProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.IPlayer;
import de.kickerapp.shared.dto.ITeam;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;

public class TablesPanel extends BasePanel implements ShowDataEventHandler, UpdatePanelEventHandler, TabPanelEventHandler {

	private ListStore<IPlayer> storeSingleTable;

	private ListStore<IPlayer> storeDoubleTableSingleView;

	private ListStore<ITeam> storeDoubleTableTeamView;

	private StoreFilterField<IPlayer> sffSingleTable;

	private StoreFilterField<IPlayer> sffDoubleTableSingleView;

	private StoreFilterField<ITeam> sffDoubleTableTeamView;

	private int activeWidget;

	private TabPanel tabPanel;

	private boolean doUpdateSingleTable, doUpdateDoubleTableSingleView, doUpdateDoubleTableTeamView;

	public TablesPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		super.initLayout();
		setHeadingHtml("<span id='panelHeading'>Aktuelle Tabelle</span>");

		doUpdateSingleTable = true;
		doUpdateDoubleTableSingleView = true;
		doUpdateDoubleTableTeamView = true;
		activeWidget = 0;

		storeSingleTable = new ListStore<IPlayer>(KickerProperties.PLAYER_PROPERTY.id());
		storeDoubleTableSingleView = new ListStore<IPlayer>(KickerProperties.PLAYER_PROPERTY.id());
		storeDoubleTableTeamView = new ListStore<ITeam>(KickerProperties.TEAM_PROPERTY.id());

		tabPanel = createTabPanel();

		add(tabPanel, new MarginData(5, 0, 0, 0));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();

		AppEventBus.addHandler(ShowDataEvent.TABLES, this);
		AppEventBus.addHandler(TabPanelEvent.TYPE, this);
		AppEventBus.addHandler(UpdatePanelEvent.ALL, this);
		AppEventBus.addHandler(UpdatePanelEvent.TABLES, this);
	}

	private TabPanel createTabPanel() {
		final PlainTabPanel tabPanel = new PlainTabPanel();
		tabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				final TabPanel panel = (TabPanel) event.getSource();
				final Widget w = event.getSelectedItem();

				activeWidget = panel.getWidgetIndex(w);
				getTable();
			}
		});
		tabPanel.setResizeTabs(true);
		tabPanel.setTabWidth(200);

		final VerticalLayoutContainer vlcSingleTable = new VerticalLayoutContainer();
		vlcSingleTable.add(createSingleTableToolBar(), new VerticalLayoutData(1, -1));
		vlcSingleTable.add(createSingleTableGrid(), new VerticalLayoutData(1, 1));

		final VerticalLayoutContainer vlcDoubleTableSingleView = new VerticalLayoutContainer();
		vlcDoubleTableSingleView.add(createDoubleTableSingleViewToolBar(), new VerticalLayoutData(1, -1));
		vlcDoubleTableSingleView.add(createDoubleTableSingleViewGrid(), new VerticalLayoutData(1, 1));

		final VerticalLayoutContainer vlcDoubleTableTeamView = new VerticalLayoutContainer();
		vlcDoubleTableTeamView.add(createDoubleTableTeamViewToolBar(), new VerticalLayoutData(1, -1));
		vlcDoubleTableTeamView.add(createDoubleTableTeamViewGrid(), new VerticalLayoutData(1, 1));

		tabPanel.add(vlcSingleTable, "Spielertabelle (Einzelansicht)");
		tabPanel.add(vlcDoubleTableSingleView, "Spielertabelle (Doppelansicht)");
		tabPanel.add(vlcDoubleTableTeamView, "Teamtabelle");
		tabPanel.setBodyBorder(false);
		tabPanel.setBorders(false);

		return tabPanel;
	}

	private ToolBar createSingleTableToolBar() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		sffSingleTable = new StoreFilterField<IPlayer>() {
			@Override
			protected boolean doSelect(Store<IPlayer> store, IPlayer parent, IPlayer item, String filter) {
				if (item.getLastName().toLowerCase().contains(filter) || item.getFirstName().toLowerCase().contains(filter)) {
					return true;
				}
				return false;
			}
		};
		sffSingleTable.bind(storeSingleTable);
		sffSingleTable.setWidth(250);
		sffSingleTable.setEmptyText("Nach Spieler suchen...");

		toolBar.add(createBtnUpdate());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(sffSingleTable);
		return toolBar;
	}

	public Grid<IPlayer> createSingleTableGrid() {
		final IdentityValueProvider<IPlayer> identity = new IdentityValueProvider<IPlayer>();
		final RowNumberer<IPlayer> numberer = new RowNumberer<IPlayer>(identity);
		numberer.setCell(new AbstractCell<IPlayer>() {
			@Override
			public void render(Context context, IPlayer value, SafeHtmlBuilder sb) {
				sb.append(value.getPlayerSingleStats().getSingleCurTablePlace());
			}
		});

		final ColumnConfig<IPlayer, String> ccPlayerName = new ColumnConfig<IPlayer, String>(PlayerProperty.playerName, 140, "Spieler");
		final ColumnConfig<IPlayer, Integer> ccSingleMatches = new ColumnConfig<IPlayer, Integer>(PlayerProperty.singleMatches, 100, "Anzahl Spiele");
		final ColumnConfig<IPlayer, Integer> ccSingleWins = new ColumnConfig<IPlayer, Integer>(PlayerProperty.singleWins, 100, "Siege");
		final ColumnConfig<IPlayer, Integer> ccSingleLosses = new ColumnConfig<IPlayer, Integer>(PlayerProperty.singleLosses, 100, "Niederlagen");
		final ColumnConfig<IPlayer, String> ccSingleGoals = new ColumnConfig<IPlayer, String>(PlayerProperty.singleGoals, 100, "Tore");
		final ColumnConfig<IPlayer, String> ccSingleGoalDifference = new ColumnConfig<IPlayer, String>(PlayerProperty.singleGoalDifference, 100, "Tordifferenz");
		final ColumnConfig<IPlayer, String> ccPoints = new ColumnConfig<IPlayer, String>(PlayerProperty.singlePoints, 100, "Punkte");
		ccPoints.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final IPlayer player = storeSingleTable.findModelWithKey(context.getKey().toString());
				final int singleLastMatchPoints = player.getPlayerSingleStats().getSingleLastMatchPoints();
				getPoints(value, sb, singleLastMatchPoints);
			}
		});
		final ColumnConfig<IPlayer, ImageResource> ccTendency = new ColumnConfig<IPlayer, ImageResource>(PlayerProperty.singleTendency, 60, "Tendenz");
		ccTendency.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final IPlayer player = storeSingleTable.findModelWithKey(context.getKey().toString());
				sb.appendHtmlConstant("<span qtitle='Vorher' qtip='Platz " + player.getPlayerSingleStats().getSinglePrevTablePlace() + "'>");
				sb.append(AbstractImagePrototype.create(value).getSafeHtml());
				sb.appendHtmlConstant("</span>");
			}
		});

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

		final Grid<IPlayer> grid = new Grid<IPlayer>(storeSingleTable, new ColumnModel<IPlayer>(columns));
		grid.getView().setAutoExpandColumn(ccPlayerName);
		grid.getView().setAutoExpandMax(1000);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		new QuickTip(grid);

		return grid;
	}

	private void getPoints(String value, SafeHtmlBuilder sb, final int lastMatchPoints) {
		final String style = "style='color: " + (lastMatchPoints >= 0 ? "green" : "red") + "'";

		final StringBuilder builder = new StringBuilder();
		if (lastMatchPoints >= 0) {
			builder.append("+" + Integer.toString(lastMatchPoints));
		} else {
			builder.append(Integer.toString(lastMatchPoints));
		}

		sb.appendHtmlConstant(value);
		sb.appendHtmlConstant(" <span " + style + ">(" + builder.toString() + ")</span>");
	}

	private ToolBar createDoubleTableSingleViewToolBar() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		sffDoubleTableSingleView = new StoreFilterField<IPlayer>() {
			@Override
			protected boolean doSelect(Store<IPlayer> store, IPlayer parent, IPlayer item, String filter) {
				if (item.getLastName().toLowerCase().contains(filter) || item.getFirstName().toLowerCase().contains(filter)) {
					return true;
				}
				return false;
			}
		};
		sffDoubleTableSingleView.bind(storeDoubleTableSingleView);
		sffDoubleTableSingleView.setWidth(250);
		sffDoubleTableSingleView.setEmptyText("Nach Spieler suchen...");

		toolBar.add(createBtnUpdate());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(sffDoubleTableSingleView);
		return toolBar;
	}

	private Widget createDoubleTableSingleViewGrid() {
		final IdentityValueProvider<IPlayer> identity = new IdentityValueProvider<IPlayer>();
		final RowNumberer<IPlayer> numberer = new RowNumberer<IPlayer>(identity);
		numberer.setCell(new AbstractCell<IPlayer>() {
			@Override
			public void render(Context context, IPlayer value, SafeHtmlBuilder sb) {
				sb.append(value.getPlayerDoubleStats().getDoubleCurTablePlace());
			}
		});

		final ColumnConfig<IPlayer, String> ccPlayerName = new ColumnConfig<IPlayer, String>(PlayerProperty.playerName, 140, "Spieler");
		final ColumnConfig<IPlayer, Integer> ccSingleMatches = new ColumnConfig<IPlayer, Integer>(PlayerProperty.doubleMatches, 100, "Anzahl Spiele");
		final ColumnConfig<IPlayer, Integer> ccDoubleWins = new ColumnConfig<IPlayer, Integer>(PlayerProperty.doubleWins, 100, "Siege");
		final ColumnConfig<IPlayer, Integer> ccDoubleLosses = new ColumnConfig<IPlayer, Integer>(PlayerProperty.doubleLosses, 100, "Niederlagen");
		final ColumnConfig<IPlayer, String> ccDoubleGoals = new ColumnConfig<IPlayer, String>(PlayerProperty.doubleGoals, 100, "Tore");
		final ColumnConfig<IPlayer, String> ccSingleGoalDifference = new ColumnConfig<IPlayer, String>(PlayerProperty.doubleGoalDifference, 100, "Tordifferenz");
		final ColumnConfig<IPlayer, String> ccPoints = new ColumnConfig<IPlayer, String>(PlayerProperty.doublePoints, 100, "Punkte");
		ccPoints.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final IPlayer player = storeDoubleTableSingleView.findModelWithKey(context.getKey().toString());
				final int lastMatchPoints = player.getPlayerDoubleStats().getDoubleLastMatchPoints();
				getPoints(value, sb, lastMatchPoints);
			}
		});
		final ColumnConfig<IPlayer, ImageResource> ccTendency = new ColumnConfig<IPlayer, ImageResource>(PlayerProperty.doubleTendency, 60, "Tendenz");
		ccTendency.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final IPlayer player = storeDoubleTableSingleView.findModelWithKey(context.getKey().toString());
				sb.appendHtmlConstant("<span qtitle='Vorher' qtip='Platz " + player.getPlayerDoubleStats().getDoublePrevTablePlace() + "'>");
				sb.append(AbstractImagePrototype.create(value).getSafeHtml());
				sb.appendHtmlConstant("</span>");
			}
		});

		final ArrayList<ColumnConfig<IPlayer, ?>> columns = new ArrayList<ColumnConfig<IPlayer, ?>>();
		columns.add(numberer);
		columns.add(ccPlayerName);
		columns.add(ccSingleMatches);
		columns.add(ccDoubleWins);
		columns.add(ccDoubleLosses);
		columns.add(ccDoubleGoals);
		columns.add(ccSingleGoalDifference);
		columns.add(ccPoints);
		columns.add(ccTendency);

		final Grid<IPlayer> grid = new Grid<IPlayer>(storeDoubleTableSingleView, new ColumnModel<IPlayer>(columns));
		grid.getView().setAutoExpandColumn(ccPlayerName);
		grid.getView().setAutoExpandMax(1000);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		new QuickTip(grid);

		return grid;
	}

	private ToolBar createDoubleTableTeamViewToolBar() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		sffDoubleTableTeamView = new StoreFilterField<ITeam>() {
			@Override
			protected boolean doSelect(Store<ITeam> store, ITeam parent, ITeam item, String filter) {
				return checkTeam(item, filter);
			}

			private boolean checkTeam(ITeam item, String filter) {
				final PlayerDto player1 = item.getPlayer1();
				if (player1.getLastName().toLowerCase().contains(filter) || player1.getFirstName().toLowerCase().contains(filter)) {
					return true;
				}

				final PlayerDto player2 = item.getPlayer2();
				if (player2 != null) {
					if (player2.getLastName().toLowerCase().contains(filter) || player2.getFirstName().toLowerCase().contains(filter)) {
						return true;
					}
				}
				return false;
			}
		};
		sffDoubleTableTeamView.bind(storeDoubleTableTeamView);
		sffDoubleTableTeamView.setWidth(250);
		sffDoubleTableTeamView.setEmptyText("Nach Spieler/Team suchen...");

		toolBar.add(createBtnUpdate());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(sffDoubleTableTeamView);
		return toolBar;
	}

	public Grid<ITeam> createDoubleTableTeamViewGrid() {
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
		final ColumnConfig<ITeam, String> ccPoints = new ColumnConfig<ITeam, String>(TeamProperty.points, 100, "Punkte");
		ccPoints.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final ITeam team = storeDoubleTableTeamView.findModelWithKey(context.getKey().toString());
				final int lastMatchPoints = team.getLastMatchPoints();
				getPoints(value, sb, lastMatchPoints);
			}
		});
		final ColumnConfig<ITeam, ImageResource> ccTendency = new ColumnConfig<ITeam, ImageResource>(TeamProperty.tendency, 60, "Tendenz");
		ccTendency.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final ITeam team = storeDoubleTableTeamView.findModelWithKey(context.getKey().toString());
				sb.appendHtmlConstant("<span qtitle='Vorher' qtip='Platz " + team.getPrevTablePlace() + "'>");
				sb.append(AbstractImagePrototype.create(value).getSafeHtml());
				sb.appendHtmlConstant("</span>");
			}
		});

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

		final Grid<ITeam> grid = new Grid<ITeam>(storeDoubleTableTeamView, new ColumnModel<ITeam>(columns));
		grid.getView().setAutoExpandColumn(ccPlayerName);
		grid.getView().setAutoExpandMax(1000);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		new QuickTip(grid);

		return grid;
	}

	private void getTable() {
		if (activeWidget == 0) {
			getSingleTable();
		} else if (activeWidget == 1) {
			getDoubleTableSingleView();
		} else {
			getDoubleTableTeamView();
		}
	}

	private void getSingleTable() {
		if (doUpdateSingleTable) {
			mask("Aktualisiere...");
			storeSingleTable.clear();
			KickerServices.PLAYER_SERVICE.getAllPlayers(MatchType.SINGLE, new AsyncCallback<ArrayList<PlayerDto>>() {
				@Override
				public void onSuccess(ArrayList<PlayerDto> result) {
					storeSingleTable.addAll(result);
					doUpdateSingleTable = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdateSingleTable = false;
					unmask();
				}
			});
		}
	}

	private void getDoubleTableSingleView() {
		if (doUpdateDoubleTableSingleView) {
			mask("Aktualisiere...");
			storeDoubleTableSingleView.clear();
			KickerServices.PLAYER_SERVICE.getAllPlayers(MatchType.DOUBLE, new AsyncCallback<ArrayList<PlayerDto>>() {
				@Override
				public void onSuccess(ArrayList<PlayerDto> result) {
					storeDoubleTableSingleView.addAll(result);
					doUpdateDoubleTableSingleView = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdateDoubleTableSingleView = false;
					unmask();
				}
			});
		}
	}

	private void getDoubleTableTeamView() {
		if (doUpdateDoubleTableTeamView) {
			mask("Aktualisiere...");
			storeDoubleTableTeamView.clear();
			KickerServices.TEAM_SERVICE.getAllTeams(new AsyncCallback<ArrayList<TeamDto>>() {
				@Override
				public void onSuccess(ArrayList<TeamDto> result) {
					storeDoubleTableTeamView.addAll(result);
					doUpdateDoubleTableTeamView = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdateDoubleTableTeamView = false;
					unmask();
				}
			});
		}
	}

	private AppButton createBtnUpdate() {
		final AppButton btnUpdate = new AppButton("Aktualisieren", KickerIcons.ICON.table_refresh());
		btnUpdate.setToolTip("Aktualisiert die Tabelle");
		btnUpdate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				setDoUpdate();
				getTable();
			}

			private void setDoUpdate() {
				if (activeWidget == 0) {
					doUpdateSingleTable = true;
				} else if (activeWidget == 1) {
					doUpdateDoubleTableSingleView = true;
				} else {
					doUpdateDoubleTableTeamView = true;
				}
			}
		});
		return btnUpdate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showData(ShowDataEvent event) {
		getTable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActiveWidget(TabPanelEvent event) {
		if (activeWidget != event.getActiveWidget()) {
			activeWidget = event.getActiveWidget();
			tabPanel.setActiveWidget(tabPanel.getWidget(activeWidget));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePanel(UpdatePanelEvent event) {
		if (event.getActiveWidget() == 0) {
			doUpdateSingleTable = true;
		} else {
			doUpdateDoubleTableSingleView = true;
			doUpdateDoubleTableTeamView = true;
		}
	}

}
