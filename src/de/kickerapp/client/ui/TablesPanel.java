package de.kickerapp.client.ui;

import java.util.ArrayList;
import java.util.Comparator;

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
import com.sencha.gxt.widget.core.client.TabItemConfig;
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
import de.kickerapp.client.exception.AppExceptionHandler;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.properties.PlayerProperty;
import de.kickerapp.client.properties.TeamProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.StoreFilterToggleButton;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;

public class TablesPanel extends BasePanel implements ShowDataEventHandler, UpdatePanelEventHandler, TabPanelEventHandler {

	private ListStore<PlayerDto> storeSingleTable;

	private ListStore<PlayerDto> storeDoubleTable;

	private ListStore<TeamDto> storeTeamTable;

	private StoreFilterField<PlayerDto> sffSingleTable;

	private StoreFilterField<PlayerDto> sffDoubleTable;

	private StoreFilterField<TeamDto> sffTeamTable;

	private int activeTab;

	private TabPanel tabPanel;

	private boolean doUpdateSingleTable, doUpdateDoubleTable, doUpdateTeamTable;

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
		setHeadingHtml("<span id='panelHeading'>Aktuelle Tabellen</span>");

		doUpdateSingleTable = true;
		doUpdateDoubleTable = true;
		doUpdateTeamTable = true;
		activeTab = 0;

		storeSingleTable = new ListStore<PlayerDto>(KickerProperties.PLAYER_PROPERTY.id());
		storeDoubleTable = new ListStore<PlayerDto>(KickerProperties.PLAYER_PROPERTY.id());
		storeTeamTable = new ListStore<TeamDto>(KickerProperties.TEAM_PROPERTY.id());

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

				activeTab = panel.getWidgetIndex(w);
				tabPanel.forceLayout();
				getTable();
			}
		});
		tabPanel.setResizeTabs(true);
		tabPanel.setTabWidth(200);

		final VerticalLayoutContainer vlcSingleTable = new VerticalLayoutContainer();
		vlcSingleTable.add(createSingleTableToolBar(), new VerticalLayoutData(1, -1));
		vlcSingleTable.add(createSingleTableGrid(), new VerticalLayoutData(1, 1));

		final TabItemConfig ticSingleTable = new TabItemConfig("Einzeltabelle");
		ticSingleTable.setIcon(KickerIcons.ICON.user());

		final VerticalLayoutContainer vlcDoubleTable = new VerticalLayoutContainer();
		vlcDoubleTable.add(createDoubleTableToolBar(), new VerticalLayoutData(1, -1));
		vlcDoubleTable.add(createDoubleTableSingleViewGrid(), new VerticalLayoutData(1, 1));

		final TabItemConfig ticDoubleTable = new TabItemConfig("Doppeltabelle");
		ticDoubleTable.setIcon(KickerIcons.ICON.group());

		final VerticalLayoutContainer vlcTeamTable = new VerticalLayoutContainer();
		vlcTeamTable.add(createTeamTableToolBar(), new VerticalLayoutData(1, -1));
		vlcTeamTable.add(createDoubleTableTeamViewGrid(), new VerticalLayoutData(1, 1));

		final TabItemConfig ticTeamTable = new TabItemConfig("Teamtabelle");
		ticTeamTable.setIcon(KickerIcons.ICON.group_link());

		tabPanel.add(vlcSingleTable, ticSingleTable);
		tabPanel.add(vlcDoubleTable, ticDoubleTable);
		tabPanel.add(vlcTeamTable, ticTeamTable);
		tabPanel.setBodyBorder(false);
		tabPanel.setBorders(false);

		return tabPanel;
	}

	private ToolBar createSingleTableToolBar() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		final StoreFilterToggleButton<PlayerDto> sfcNoMatchPlayer = new StoreFilterToggleButton<PlayerDto>() {
			@Override
			protected boolean doSelect(Store<PlayerDto> store, PlayerDto parent, PlayerDto item, boolean filter) {
				if (!(item.getPlayerSingleStatsDto().getCurTablePlace() == 0)) {
					return true;
				}
				return false;
			}
		};
		sfcNoMatchPlayer.bind(storeSingleTable);
		sfcNoMatchPlayer.setIcon(KickerIcons.ICON.table_sort());
		sfcNoMatchPlayer.setText("Alle Spieler anzeigen");
		sfcNoMatchPlayer.setToolTip("Aktivieren um auch Spieler anzuzeigen, welche noch kein Spiel und damit keinen Tabellenplatz haben");

		sffSingleTable = new StoreFilterField<PlayerDto>() {
			@Override
			protected boolean doSelect(Store<PlayerDto> store, PlayerDto parent, PlayerDto item, String filter) {
				if (item.getLastName().toLowerCase().contains(filter) || item.getFirstName().toLowerCase().contains(filter)
						|| item.getNickName().toLowerCase().contains(filter)) {
					return true;
				}
				return false;
			}
		};
		sffSingleTable.bind(storeSingleTable);
		sffSingleTable.setWidth(250);
		sffSingleTable.setEmptyText("Nach Spieler suchen...");

		toolBar.add(createBtnUpdate());
		toolBar.add(sfcNoMatchPlayer);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(sffSingleTable);

		return toolBar;
	}

	public Grid<PlayerDto> createSingleTableGrid() {
		final IdentityValueProvider<PlayerDto> identity = new IdentityValueProvider<PlayerDto>();
		final RowNumberer<PlayerDto> numberer = new RowNumberer<PlayerDto>(identity);
		numberer.setCell(new AbstractCell<PlayerDto>() {
			@Override
			public void render(Context context, PlayerDto value, SafeHtmlBuilder sb) {
				sb.append(value.getPlayerSingleStatsDto().getCurTablePlace());
			}
		});
		numberer.setComparator(new Comparator<PlayerDto>() {
			@Override
			public int compare(PlayerDto o1, PlayerDto o2) {
				return o1.getPlayerSingleStatsDto().getCurTablePlace().compareTo(o2.getPlayerSingleStatsDto().getCurTablePlace());
			}
		});
		numberer.setSortable(true);

		final ColumnConfig<PlayerDto, String> ccPlayerName = new ColumnConfig<PlayerDto, String>(PlayerProperty.playerName, 140, "Spieler");
		final ColumnConfig<PlayerDto, Integer> ccSingleMatches = new ColumnConfig<PlayerDto, Integer>(PlayerProperty.singleMatches, 100, "Anzahl Spiele");
		final ColumnConfig<PlayerDto, Integer> ccSingleWins = new ColumnConfig<PlayerDto, Integer>(PlayerProperty.singleWins, 100, "Siege");
		final ColumnConfig<PlayerDto, Integer> ccSingleLosses = new ColumnConfig<PlayerDto, Integer>(PlayerProperty.singleLosses, 100, "Niederlagen");
		final ColumnConfig<PlayerDto, String> ccSingleGoals = new ColumnConfig<PlayerDto, String>(PlayerProperty.singleGoals, 100, "Tore");
		final ColumnConfig<PlayerDto, String> ccSingleGoalDifference = new ColumnConfig<PlayerDto, String>(PlayerProperty.singleGoalDifference, 100,
				"Tordifferenz");
		final ColumnConfig<PlayerDto, String> ccPoints = new ColumnConfig<PlayerDto, String>(PlayerProperty.singlePoints, 100, "Punkte");
		ccPoints.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final PlayerDto player = storeSingleTable.findModelWithKey(context.getKey().toString());
				final int singleLastMatchPoints = player.getPlayerSingleStatsDto().getLastMatchPoints();
				getPoints(value, sb, singleLastMatchPoints);
			}
		});
		final ColumnConfig<PlayerDto, ImageResource> ccTendency = new ColumnConfig<PlayerDto, ImageResource>(PlayerProperty.singleTendency, 80, "Tendenz");
		ccTendency.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final PlayerDto player = storeSingleTable.findModelWithKey(context.getKey().toString());
				sb.appendHtmlConstant("<span qtitle='Vorher' qtip='Platz " + player.getPlayerSingleStatsDto().getPrevTablePlace() + "'>");
				sb.append(AbstractImagePrototype.create(value).getSafeHtml());
				sb.appendHtmlConstant("</span>");
			}
		});
		ccTendency.setComparator(new Comparator<ImageResource>() {
			@Override
			public int compare(ImageResource o1, ImageResource o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		final ArrayList<ColumnConfig<PlayerDto, ?>> columns = new ArrayList<ColumnConfig<PlayerDto, ?>>();
		columns.add(numberer);
		columns.add(ccPlayerName);
		columns.add(ccSingleMatches);
		columns.add(ccSingleWins);
		columns.add(ccSingleLosses);
		columns.add(ccSingleGoals);
		columns.add(ccSingleGoalDifference);
		columns.add(ccPoints);
		columns.add(ccTendency);

		final Grid<PlayerDto> grid = new Grid<PlayerDto>(storeSingleTable, new ColumnModel<PlayerDto>(columns));
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

	private ToolBar createDoubleTableToolBar() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		final StoreFilterToggleButton<PlayerDto> sfcNoMatchPlayer = new StoreFilterToggleButton<PlayerDto>() {
			@Override
			protected boolean doSelect(Store<PlayerDto> store, PlayerDto parent, PlayerDto item, boolean filter) {
				if (!(item.getPlayerDoubleStatsDto().getCurTablePlace() == 0)) {
					return true;
				}
				return false;
			}
		};
		sfcNoMatchPlayer.bind(storeDoubleTable);
		sfcNoMatchPlayer.setIcon(KickerIcons.ICON.table_sort());
		sfcNoMatchPlayer.setText("Alle Spieler anzeigen");
		sfcNoMatchPlayer.setToolTip("Aktivieren um auch Spieler anzuzeigen, welche noch kein Spiel und damit keinen Tabellenplatz haben");

		sffDoubleTable = new StoreFilterField<PlayerDto>() {
			@Override
			protected boolean doSelect(Store<PlayerDto> store, PlayerDto parent, PlayerDto item, String filter) {
				if (item.getLastName().toLowerCase().contains(filter) || item.getFirstName().toLowerCase().contains(filter)
						|| item.getNickName().toLowerCase().contains(filter)) {
					return true;
				}
				return false;
			}
		};
		sffDoubleTable.bind(storeDoubleTable);
		sffDoubleTable.setWidth(250);
		sffDoubleTable.setEmptyText("Nach Spieler suchen...");

		toolBar.add(createBtnUpdate());
		toolBar.add(sfcNoMatchPlayer);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(sffDoubleTable);
		return toolBar;
	}

	private Widget createDoubleTableSingleViewGrid() {
		final IdentityValueProvider<PlayerDto> identity = new IdentityValueProvider<PlayerDto>();
		final RowNumberer<PlayerDto> numberer = new RowNumberer<PlayerDto>(identity);
		numberer.setCell(new AbstractCell<PlayerDto>() {
			@Override
			public void render(Context context, PlayerDto value, SafeHtmlBuilder sb) {
				sb.append(value.getPlayerDoubleStatsDto().getCurTablePlace());
			}
		});
		numberer.setComparator(new Comparator<PlayerDto>() {
			@Override
			public int compare(PlayerDto o1, PlayerDto o2) {
				return o1.getPlayerDoubleStatsDto().getCurTablePlace().compareTo(o2.getPlayerDoubleStatsDto().getCurTablePlace());
			}
		});
		numberer.setSortable(true);

		final ColumnConfig<PlayerDto, String> ccPlayerName = new ColumnConfig<PlayerDto, String>(PlayerProperty.playerName, 140, "Spieler");
		final ColumnConfig<PlayerDto, Integer> ccSingleMatches = new ColumnConfig<PlayerDto, Integer>(PlayerProperty.doubleMatches, 100, "Anzahl Spiele");
		final ColumnConfig<PlayerDto, Integer> ccDoubleWins = new ColumnConfig<PlayerDto, Integer>(PlayerProperty.doubleWins, 100, "Siege");
		final ColumnConfig<PlayerDto, Integer> ccDoubleLosses = new ColumnConfig<PlayerDto, Integer>(PlayerProperty.doubleLosses, 100, "Niederlagen");
		final ColumnConfig<PlayerDto, String> ccDoubleGoals = new ColumnConfig<PlayerDto, String>(PlayerProperty.doubleGoals, 100, "Tore");
		final ColumnConfig<PlayerDto, String> ccSingleGoalDifference = new ColumnConfig<PlayerDto, String>(PlayerProperty.doubleGoalDifference, 100,
				"Tordifferenz");
		final ColumnConfig<PlayerDto, String> ccPoints = new ColumnConfig<PlayerDto, String>(PlayerProperty.doublePoints, 100, "Punkte");
		ccPoints.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final PlayerDto player = storeDoubleTable.findModelWithKey(context.getKey().toString());
				final int lastMatchPoints = player.getPlayerDoubleStatsDto().getLastMatchPoints();
				getPoints(value, sb, lastMatchPoints);
			}
		});
		final ColumnConfig<PlayerDto, ImageResource> ccTendency = new ColumnConfig<PlayerDto, ImageResource>(PlayerProperty.doubleTendency, 80, "Tendenz");
		ccTendency.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final PlayerDto player = storeDoubleTable.findModelWithKey(context.getKey().toString());
				sb.appendHtmlConstant("<span qtitle='Vorher' qtip='Platz " + player.getPlayerDoubleStatsDto().getPrevTablePlace() + "'>");
				sb.append(AbstractImagePrototype.create(value).getSafeHtml());
				sb.appendHtmlConstant("</span>");
			}
		});
		ccTendency.setComparator(new Comparator<ImageResource>() {
			@Override
			public int compare(ImageResource o1, ImageResource o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		final ArrayList<ColumnConfig<PlayerDto, ?>> columns = new ArrayList<ColumnConfig<PlayerDto, ?>>();
		columns.add(numberer);
		columns.add(ccPlayerName);
		columns.add(ccSingleMatches);
		columns.add(ccDoubleWins);
		columns.add(ccDoubleLosses);
		columns.add(ccDoubleGoals);
		columns.add(ccSingleGoalDifference);
		columns.add(ccPoints);
		columns.add(ccTendency);

		final Grid<PlayerDto> grid = new Grid<PlayerDto>(storeDoubleTable, new ColumnModel<PlayerDto>(columns));
		grid.getView().setAutoExpandColumn(ccPlayerName);
		grid.getView().setAutoExpandMax(1000);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		new QuickTip(grid);

		return grid;
	}

	private ToolBar createTeamTableToolBar() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		sffTeamTable = new StoreFilterField<TeamDto>() {
			@Override
			protected boolean doSelect(Store<TeamDto> store, TeamDto parent, TeamDto item, String filter) {
				return checkTeam(item, filter);
			}

			private boolean checkTeam(TeamDto item, String filter) {
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
		sffTeamTable.bind(storeTeamTable);
		sffTeamTable.setWidth(250);
		sffTeamTable.setEmptyText("Nach Spieler/Team suchen...");

		toolBar.add(createBtnUpdate());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(sffTeamTable);
		return toolBar;
	}

	public Grid<TeamDto> createDoubleTableTeamViewGrid() {
		final IdentityValueProvider<TeamDto> identity = new IdentityValueProvider<TeamDto>();
		final RowNumberer<TeamDto> numberer = new RowNumberer<TeamDto>(identity);
		numberer.setCell(new AbstractCell<TeamDto>() {
			@Override
			public void render(Context context, TeamDto value, SafeHtmlBuilder sb) {
				sb.append(value.getTeamStatsDto().getCurTablePlace());
			}
		});
		numberer.setComparator(new Comparator<TeamDto>() {
			@Override
			public int compare(TeamDto o1, TeamDto o2) {
				return o1.getTeamStatsDto().getCurTablePlace().compareTo(o2.getTeamStatsDto().getCurTablePlace());
			}
		});
		numberer.setSortable(true);

		final ColumnConfig<TeamDto, String> ccPlayerName = new ColumnConfig<TeamDto, String>(TeamProperty.teamName, 140, "Team");
		final ColumnConfig<TeamDto, Integer> ccSingleMatches = new ColumnConfig<TeamDto, Integer>(TeamProperty.teamMatches, 100, "Anzahl Spiele");
		final ColumnConfig<TeamDto, Integer> ccSingleWins = new ColumnConfig<TeamDto, Integer>(TeamProperty.teamWins, 100, "Siege");
		final ColumnConfig<TeamDto, Integer> ccSingleLosses = new ColumnConfig<TeamDto, Integer>(TeamProperty.teamLosses, 100, "Niederlagen");
		final ColumnConfig<TeamDto, String> ccSingleGoals = new ColumnConfig<TeamDto, String>(TeamProperty.teamGoals, 100, "Tore");
		final ColumnConfig<TeamDto, String> ccSingleGoalDifference = new ColumnConfig<TeamDto, String>(TeamProperty.teamGoalDifference, 100, "Tordifferenz");
		final ColumnConfig<TeamDto, String> ccPoints = new ColumnConfig<TeamDto, String>(TeamProperty.teamPoints, 100, "Punkte");
		ccPoints.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final TeamDto team = storeTeamTable.findModelWithKey(context.getKey().toString());
				final int lastMatchPoints = team.getTeamStatsDto().getLastMatchPoints();
				getPoints(value, sb, lastMatchPoints);
			}
		});
		final ColumnConfig<TeamDto, ImageResource> ccTendency = new ColumnConfig<TeamDto, ImageResource>(TeamProperty.teamTendency, 80, "Tendenz");
		ccTendency.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final TeamDto team = storeTeamTable.findModelWithKey(context.getKey().toString());
				sb.appendHtmlConstant("<span qtitle='Vorher' qtip='Platz " + team.getTeamStatsDto().getPrevTablePlace() + "'>");
				sb.append(AbstractImagePrototype.create(value).getSafeHtml());
				sb.appendHtmlConstant("</span>");
			}
		});
		ccTendency.setComparator(new Comparator<ImageResource>() {
			@Override
			public int compare(ImageResource o1, ImageResource o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		final ArrayList<ColumnConfig<TeamDto, ?>> columns = new ArrayList<ColumnConfig<TeamDto, ?>>();
		columns.add(numberer);
		columns.add(ccPlayerName);
		columns.add(ccSingleMatches);
		columns.add(ccSingleWins);
		columns.add(ccSingleLosses);
		columns.add(ccSingleGoals);
		columns.add(ccSingleGoalDifference);
		columns.add(ccPoints);
		columns.add(ccTendency);

		final Grid<TeamDto> grid = new Grid<TeamDto>(storeTeamTable, new ColumnModel<TeamDto>(columns));
		grid.getView().setAutoExpandColumn(ccPlayerName);
		grid.getView().setAutoExpandMax(1000);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		new QuickTip(grid);

		return grid;
	}

	private void getTable() {
		if (activeTab == 0) {
			getSingleTable();
		} else if (activeTab == 1) {
			getDoubleTable();
		} else {
			getDoubleTableTeamView();
		}
	}

	private void getSingleTable() {
		if (doUpdateSingleTable) {
			mask("Aktualisiere...");
			KickerServices.PLAYER_SERVICE.getAllPlayers(MatchType.SINGLE, new AsyncCallback<ArrayList<PlayerDto>>() {
				@Override
				public void onSuccess(ArrayList<PlayerDto> result) {
					storeSingleTable.replaceAll(result);
					doUpdateSingleTable = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdateSingleTable = false;
					unmask();
					AppExceptionHandler.handleException(caught);
				}
			});
		}
	}

	private void getDoubleTable() {
		if (doUpdateDoubleTable) {
			mask("Aktualisiere...");
			KickerServices.PLAYER_SERVICE.getAllPlayers(MatchType.DOUBLE, new AsyncCallback<ArrayList<PlayerDto>>() {
				@Override
				public void onSuccess(ArrayList<PlayerDto> result) {
					storeDoubleTable.replaceAll(result);
					doUpdateDoubleTable = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdateDoubleTable = false;
					unmask();
					AppExceptionHandler.handleException(caught);
				}
			});
		}
	}

	private void getDoubleTableTeamView() {
		if (doUpdateTeamTable) {
			mask("Aktualisiere...");
			KickerServices.TEAM_SERVICE.getAllTeams(new AsyncCallback<ArrayList<TeamDto>>() {
				@Override
				public void onSuccess(ArrayList<TeamDto> result) {
					storeTeamTable.replaceAll(result);
					doUpdateTeamTable = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdateTeamTable = false;
					unmask();
					AppExceptionHandler.handleException(caught);
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
				if (activeTab == 0) {
					doUpdateSingleTable = true;
				} else if (activeTab == 1) {
					doUpdateDoubleTable = true;
				} else {
					doUpdateTeamTable = true;
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
		if (activeTab != event.getActiveTab()) {
			activeTab = event.getActiveTab();
			tabPanel.setActiveWidget(tabPanel.getWidget(activeTab));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePanel(UpdatePanelEvent event) {
		doUpdateSingleTable = true;
		doUpdateDoubleTable = true;
		doUpdateTeamTable = true;
	}

}
