package de.kickerapp.client.ui.controller;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
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
import com.sencha.gxt.widget.core.client.grid.RowNumberer.RowNumbererAppearance;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.SelectNavigationElementEvent;
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
import de.kickerapp.client.ui.base.BasePanel;
import de.kickerapp.client.ui.navigation.NavigationElement;
import de.kickerapp.client.ui.resources.IconProvider;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.StoreFilterToggleButton;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.BaseDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;

/**
 * Controller-Klasse für die Ansicht der Team- bzw. Spielertabellen.
 * 
 * @author Sebastian Filke
 */
public class TablesPanel extends BasePanel implements ShowDataEventHandler, TabPanelEventHandler, UpdatePanelEventHandler {

	/** Der Store für die Einzelspielertabelle. */
	private ListStore<PlayerDto> storeSingleTable;
	/** Der Store für die Doppelspielertabelle. */
	private ListStore<PlayerDto> storeDoubleTable;
	/** Der Store für die Teamtabelle. */
	private ListStore<TeamDto> storeTeamTable;
	/** Der TabPanel zum Anzeigen der verschiedenen Tabellen. */
	private TabPanel tabPanel;
	/** Der aktuelle oder anzuzeigende Tab. */
	private int activeTab;
	/** Der Angabe, ob die Einzelspielertabelle, Doppelspielertabelle sowie die Teamtabelle aktualisiert werden sollen. */
	private boolean doUpdateSingleTable, doUpdateDoubleTable, doUpdateTeamTable;

	/**
	 * Erzeugt einen neuen Controller für die Team- bzw. Spielertabellen.
	 */
	public TablesPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initLayout() {
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
		AppEventBus.addHandler(TabPanelEvent.TABLES, this);
		AppEventBus.addHandler(UpdatePanelEvent.ALL, this);
		AppEventBus.addHandler(UpdatePanelEvent.TABLES, this);
	}

	/**
	 * Erzeugt den TabPanel zum Anzeigen der verschiedenen Tabellen.
	 * 
	 * @return Der erzeugte TabPanel.
	 */
	private TabPanel createTabPanel() {
		final PlainTabPanel plaonTabPanel = new PlainTabPanel();
		plaonTabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				final TabPanel panel = (TabPanel) event.getSource();
				final Widget widget = event.getSelectedItem();

				activeTab = panel.getWidgetIndex(widget);
				getTableForActiveTab();
			}
		});
		plaonTabPanel.setResizeTabs(true);
		plaonTabPanel.setTabWidth(200);

		final VerticalLayoutContainer vlcSingleTable = new VerticalLayoutContainer();
		vlcSingleTable.add(createSingleTableToolBar(), new VerticalLayoutData(1, -1));
		vlcSingleTable.add(createSingleTableGrid(), new VerticalLayoutData(1, 1));

		final TabItemConfig ticSingleTable = new TabItemConfig("Einzeltabelle");
		ticSingleTable.setIcon(IconProvider.get().player_single());

		final VerticalLayoutContainer vlcDoubleTable = new VerticalLayoutContainer();
		vlcDoubleTable.add(createDoubleTableToolBar(), new VerticalLayoutData(1, -1));
		vlcDoubleTable.add(createDoubleTableGrid(), new VerticalLayoutData(1, 1));

		final TabItemConfig ticDoubleTable = new TabItemConfig("Doppeltabelle");
		ticDoubleTable.setIcon(IconProvider.get().player_double());

		final VerticalLayoutContainer vlcTeamTable = new VerticalLayoutContainer();
		vlcTeamTable.add(createTeamTableToolBar(), new VerticalLayoutData(1, -1));
		vlcTeamTable.add(createTeamTableGrid(), new VerticalLayoutData(1, 1));

		final TabItemConfig ticTeamTable = new TabItemConfig("Teamtabelle");
		ticTeamTable.setIcon(IconProvider.get().player_team());

		plaonTabPanel.add(vlcSingleTable, ticSingleTable);
		plaonTabPanel.add(vlcDoubleTable, ticDoubleTable);
		plaonTabPanel.add(vlcTeamTable, ticTeamTable);
		plaonTabPanel.setBodyBorder(false);
		plaonTabPanel.setBorders(false);

		return plaonTabPanel;
	}

	/**
	 * Erzeugt die ToolBar für die Einzelspielertabelle.
	 * 
	 * @return Die erzeugte ToolBar.
	 */
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
		sfcNoMatchPlayer.setToolTip("Aktivieren um auch Spieler anzuzeigen, welche noch kein Spiel und damit keinen Tabellenplatz haben");
		sfcNoMatchPlayer.setText("Alle Spieler anzeigen");
		sfcNoMatchPlayer.setIcon(IconProvider.get().player_filter());
		sfcNoMatchPlayer.bind(storeSingleTable);

		final StoreFilterField<PlayerDto> sffSingleTable = new StoreFilterField<PlayerDto>() {
			@Override
			protected boolean doSelect(Store<PlayerDto> store, PlayerDto parent, PlayerDto item, String filter) {
				if (item.getLastName().toLowerCase().contains(filter.toLowerCase()) || item.getFirstName().toLowerCase().contains(filter.toLowerCase())
						|| item.getNickName().toLowerCase().contains(filter.toLowerCase())) {
					return true;
				}
				return false;
			}
		};
		sffSingleTable.setEmptyText("Nach Spieler suchen...");
		sffSingleTable.bind(storeSingleTable);
		sffSingleTable.setWidth(250);

		toolBar.add(createBtnUpdate());
		toolBar.add(sfcNoMatchPlayer);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(sffSingleTable);

		return toolBar;
	}

	/**
	 * Erzeugt das Grid für die Einzelspielertabelle.
	 * 
	 * @return Das erzeugte Grid.
	 */
	private Grid<PlayerDto> createSingleTableGrid() {
		final RowNumbererAppearance appearance = GWT.create(RowNumbererAppearance.class);
		final RowNumberer<PlayerDto> numberer = new RowNumberer<PlayerDto>();
		numberer.setCell(new AbstractCell<PlayerDto>() {
			@Override
			public void render(Context context, PlayerDto value, SafeHtmlBuilder sb) {
				appearance.renderCell(value.getPlayerSingleStatsDto().getCurTablePlace(), sb);
			}
		});
		numberer.setComparator(new Comparator<PlayerDto>() {
			@Override
			public int compare(PlayerDto o1, PlayerDto o2) {
				return o1.getPlayerSingleStatsDto().getCurTablePlace().compareTo(o2.getPlayerSingleStatsDto().getCurTablePlace());
			}
		});
		numberer.setHideable(false);
		numberer.setSortable(true);

		// Spieler
		final ColumnConfig<PlayerDto, String> ccPlayerName = new ColumnConfig<PlayerDto, String>(PlayerProperty.playerName, 140, "Spieler");

		// Spiele
		final ColumnConfig<PlayerDto, Integer> ccSingleMatches = createColumnConfig(PlayerProperty.singleMatches, 90, "Spiele");

		// Siege
		final ColumnConfig<PlayerDto, Integer> ccSingleWins = createColumnConfig(PlayerProperty.singleWins, 90, "Siege");

		// Niederlagen
		final ColumnConfig<PlayerDto, Integer> ccSingleDefeats = createColumnConfig(PlayerProperty.singleDefeats, 90, "Niederlagen");

		// Sätze
		final ColumnConfig<PlayerDto, String> ccSingleSets = createColumnConfig(PlayerProperty.singleSets, 90, "Sätze");
		ccSingleSets.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return compareSets(o1, o2);
			}
		});

		// Satzdifferenz
		final ColumnConfig<PlayerDto, String> ccSingleSetDifference = createColumnConfig(PlayerProperty.singleSetDifference, 90, "Satzdifferenz");
		ccSingleSetDifference.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return compareSetDifference(o1, o2);
			}
		});

		// Tore
		final ColumnConfig<PlayerDto, String> ccSingleGoals = createColumnConfig(PlayerProperty.singleGoals, 90, "Tore");
		ccSingleGoals.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return compareGoals(o1, o2);
			}
		});

		// Tordifferenz
		final ColumnConfig<PlayerDto, String> ccSingleGoalDifference = createColumnConfig(PlayerProperty.singleGoalDifference, 90, "Tordifferenz");
		ccSingleGoalDifference.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return compareGoalDifference(o1, o2);
			}
		});

		// Punkte
		final ColumnConfig<PlayerDto, String> ccSinglePoints = createColumnConfig(PlayerProperty.singlePoints, 90, "Punkte");
		ccSinglePoints.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final PlayerDto player = storeSingleTable.findModelWithKey(context.getKey().toString());
				final int singleLastMatchPoints = player.getPlayerSingleStatsDto().getLastMatchPoints();
				concatLastMatchPoints(value, sb, singleLastMatchPoints);
			}
		});
		ccSinglePoints.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return comparePoints(o1, o2);
			}
		});

		// Tendenz
		final ColumnConfig<PlayerDto, ImageResource> ccSingleTendency = createColumnConfig(PlayerProperty.singleTendency, 80, "Tendenz");
		ccSingleTendency.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final PlayerDto player = storeSingleTable.findModelWithKey(context.getKey().toString());
				sb.appendHtmlConstant("<div qtitle='Vorheriger Tabellenplatz' qtip='Platz " + player.getPlayerSingleStatsDto().getPrevTablePlace()
						+ "' qwidth='160px'>");
				sb.append(AbstractImagePrototype.create(value).getSafeHtml());
				sb.appendHtmlConstant("</div>");
			}
		});
		ccSingleTendency.setComparator(new Comparator<ImageResource>() {
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
		columns.add(ccSingleDefeats);
		columns.add(ccSingleSets);
		columns.add(ccSingleSetDifference);
		columns.add(ccSingleGoals);
		columns.add(ccSingleGoalDifference);
		columns.add(ccSinglePoints);
		columns.add(ccSingleTendency);

		final Grid<PlayerDto> gridSingleTable = new Grid<PlayerDto>(storeSingleTable, new ColumnModel<PlayerDto>(columns));
		gridSingleTable.getView().setAutoExpandColumn(ccPlayerName);
		gridSingleTable.getView().setAutoExpandMax(1000);
		gridSingleTable.getView().setStripeRows(true);
		new QuickTip(gridSingleTable);

		return gridSingleTable;
	}

	/**
	 * Erzeugt die ToolBar für die Doppelspielertabelle.
	 * 
	 * @return Die erzeugte ToolBar.
	 */
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
		sfcNoMatchPlayer.setToolTip("Aktivieren um auch Spieler anzuzeigen, welche noch kein Spiel und damit keinen Tabellenplatz haben");
		sfcNoMatchPlayer.setText("Alle Spieler anzeigen");
		sfcNoMatchPlayer.setIcon(IconProvider.get().player_filter());
		sfcNoMatchPlayer.bind(storeDoubleTable);

		final StoreFilterField<PlayerDto> sffDoubleTable = new StoreFilterField<PlayerDto>() {
			@Override
			protected boolean doSelect(Store<PlayerDto> store, PlayerDto parent, PlayerDto item, String filter) {
				if (item.getLastName().toLowerCase().contains(filter.toLowerCase()) || item.getFirstName().toLowerCase().contains(filter.toLowerCase())
						|| item.getNickName().toLowerCase().contains(filter.toLowerCase())) {
					return true;
				}
				return false;
			}
		};
		sffDoubleTable.setEmptyText("Nach Spieler suchen...");
		sffDoubleTable.setWidth(250);
		sffDoubleTable.bind(storeDoubleTable);

		toolBar.add(createBtnUpdate());
		toolBar.add(sfcNoMatchPlayer);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(sffDoubleTable);

		return toolBar;
	}

	/**
	 * Erzeugt das Grid für die Doppelspielertabelle.
	 * 
	 * @return Das erzeugte Grid.
	 */
	private Grid<PlayerDto> createDoubleTableGrid() {
		final RowNumbererAppearance appearance = GWT.create(RowNumbererAppearance.class);
		final RowNumberer<PlayerDto> numberer = new RowNumberer<PlayerDto>();
		numberer.setCell(new AbstractCell<PlayerDto>() {
			@Override
			public void render(Context context, PlayerDto value, SafeHtmlBuilder sb) {
				appearance.renderCell(value.getPlayerDoubleStatsDto().getCurTablePlace(), sb);
			}
		});
		numberer.setComparator(new Comparator<PlayerDto>() {
			@Override
			public int compare(PlayerDto o1, PlayerDto o2) {
				return o1.getPlayerDoubleStatsDto().getCurTablePlace().compareTo(o2.getPlayerDoubleStatsDto().getCurTablePlace());
			}
		});
		numberer.setHideable(false);
		numberer.setSortable(true);

		// Spieler
		final ColumnConfig<PlayerDto, String> ccPlayerName = new ColumnConfig<PlayerDto, String>(PlayerProperty.playerName, 140, "Spieler");

		// Spiele
		final ColumnConfig<PlayerDto, Integer> ccDoubleMatches = createColumnConfig(PlayerProperty.doubleMatches, 90, "Spiele");

		// Siege
		final ColumnConfig<PlayerDto, Integer> ccDoubleWins = createColumnConfig(PlayerProperty.doubleWins, 90, "Siege");

		// Niederlagen
		final ColumnConfig<PlayerDto, Integer> ccDoubleDefeats = createColumnConfig(PlayerProperty.doubleDefeats, 90, "Niederlagen");

		// Sätze
		final ColumnConfig<PlayerDto, String> ccDoubleSets = createColumnConfig(PlayerProperty.doubleSets, 90, "Sätze");
		ccDoubleSets.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return compareSets(o1, o2);
			}
		});

		// Satzdifferenz
		final ColumnConfig<PlayerDto, String> ccDoubleSetDifference = createColumnConfig(PlayerProperty.doubleSetDifference, 90, "Satzdifferenz");
		ccDoubleSetDifference.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return compareSetDifference(o1, o2);
			}
		});

		// Tore
		final ColumnConfig<PlayerDto, String> ccDoubleGoals = createColumnConfig(PlayerProperty.doubleGoals, 90, "Tore");
		ccDoubleGoals.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return compareGoals(o1, o2);
			}
		});

		// Tordifferenz
		final ColumnConfig<PlayerDto, String> ccDoubleGoalDifference = createColumnConfig(PlayerProperty.doubleGoalDifference, 90, "Tordifferenz");
		ccDoubleGoalDifference.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return compareGoalDifference(o1, o2);
			}
		});

		// Punkte
		final ColumnConfig<PlayerDto, String> ccDoublePoints = createColumnConfig(PlayerProperty.doublePoints, 90, "Punkte");
		ccDoublePoints.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final PlayerDto player = storeDoubleTable.findModelWithKey(context.getKey().toString());
				final int lastMatchPoints = player.getPlayerDoubleStatsDto().getLastMatchPoints();
				concatLastMatchPoints(value, sb, lastMatchPoints);
			}
		});
		ccDoublePoints.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return comparePoints(o1, o2);
			}
		});

		// Tendenz
		final ColumnConfig<PlayerDto, ImageResource> ccDoubleTendency = createColumnConfig(PlayerProperty.doubleTendency, 80, "Tendenz");
		ccDoubleTendency.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final PlayerDto player = storeDoubleTable.findModelWithKey(context.getKey().toString());
				sb.appendHtmlConstant("<div qtitle='Vorheriger Tabellenplatz' qtip='Platz " + player.getPlayerDoubleStatsDto().getPrevTablePlace()
						+ "' qwidth='160px'>");
				sb.append(AbstractImagePrototype.create(value).getSafeHtml());
				sb.appendHtmlConstant("</div>");
			}
		});
		ccDoubleTendency.setComparator(new Comparator<ImageResource>() {
			@Override
			public int compare(ImageResource o1, ImageResource o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		final ArrayList<ColumnConfig<PlayerDto, ?>> columns = new ArrayList<ColumnConfig<PlayerDto, ?>>();
		columns.add(numberer);
		columns.add(ccPlayerName);
		columns.add(ccDoubleMatches);
		columns.add(ccDoubleWins);
		columns.add(ccDoubleDefeats);
		columns.add(ccDoubleSets);
		columns.add(ccDoubleSetDifference);
		columns.add(ccDoubleGoals);
		columns.add(ccDoubleGoalDifference);
		columns.add(ccDoublePoints);
		columns.add(ccDoubleTendency);

		final Grid<PlayerDto> gridDoubleTable = new Grid<PlayerDto>(storeDoubleTable, new ColumnModel<PlayerDto>(columns));
		gridDoubleTable.getView().setAutoExpandColumn(ccPlayerName);
		gridDoubleTable.getView().setAutoExpandMax(1000);
		gridDoubleTable.getView().setStripeRows(true);
		new QuickTip(gridDoubleTable);

		return gridDoubleTable;
	}

	/**
	 * Erzeugt die ToolBar für die Teamtabelle.
	 * 
	 * @return Die erzeugte ToolBar.
	 */
	private ToolBar createTeamTableToolBar() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		final StoreFilterField<TeamDto> sffTeamTable = new StoreFilterField<TeamDto>() {
			@Override
			protected boolean doSelect(Store<TeamDto> store, TeamDto parent, TeamDto item, String filter) {
				return doSelectTeam(item, filter);
			}

			private boolean doSelectTeam(TeamDto item, String filter) {
				final String[] queries = filter.split(" ");

				final boolean containsPlayer1 = checkPlayer(item.getPlayer1(), queries);
				final boolean containsPlayer2 = checkPlayer(item.getPlayer2(), queries);

				boolean select = false;
				if (queries.length == 1) {
					select = containsPlayer1 || containsPlayer2;
				} else {
					select = containsPlayer1 && containsPlayer2;
				}
				return select;
			}

			private boolean checkPlayer(PlayerDto playerDto, final String[] queries) {
				boolean containsPlayer1 = false;
				for (String curQuery : queries) {
					final PlayerDto player1 = playerDto;
					if (player1.getLastName().toLowerCase().contains(curQuery.toLowerCase())
							|| player1.getFirstName().toLowerCase().contains(curQuery.toLowerCase())) {
						containsPlayer1 = true;
						break;
					}
				}
				return containsPlayer1;
			}
		};
		sffTeamTable.setEmptyText("Nach Spieler/Team suchen...");
		sffTeamTable.setWidth(250);
		sffTeamTable.bind(storeTeamTable);

		toolBar.add(createBtnUpdate());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(sffTeamTable);

		return toolBar;
	}

	/**
	 * Erzeugt das Grid für die Teamtabelle.
	 * 
	 * @return Das erzeugte Grid.
	 */
	private Grid<TeamDto> createTeamTableGrid() {
		final RowNumbererAppearance appearance = GWT.create(RowNumbererAppearance.class);
		final RowNumberer<TeamDto> numberer = new RowNumberer<TeamDto>();
		numberer.setCell(new AbstractCell<TeamDto>() {
			@Override
			public void render(Context context, TeamDto value, SafeHtmlBuilder sb) {
				appearance.renderCell(value.getTeamStatsDto().getCurTablePlace(), sb);
			}
		});
		numberer.setComparator(new Comparator<TeamDto>() {
			@Override
			public int compare(TeamDto o1, TeamDto o2) {
				return o1.getTeamStatsDto().getCurTablePlace().compareTo(o2.getTeamStatsDto().getCurTablePlace());
			}
		});
		numberer.setHideable(false);
		numberer.setSortable(true);

		// Team
		final ColumnConfig<TeamDto, String> ccTeamName = new ColumnConfig<TeamDto, String>(TeamProperty.teamName, 140, "Team");

		// Spiele
		final ColumnConfig<TeamDto, Integer> ccTeamMatches = createColumnConfig(TeamProperty.teamMatches, 90, "Spiele");

		// Siege
		final ColumnConfig<TeamDto, Integer> ccTeamWins = createColumnConfig(TeamProperty.teamWins, 90, "Siege");

		// Niederlagen
		final ColumnConfig<TeamDto, Integer> ccTeamDefeats = createColumnConfig(TeamProperty.teamDefeats, 90, "Niederlagen");

		// Sätze
		final ColumnConfig<TeamDto, String> ccTeamSets = createColumnConfig(TeamProperty.teamSets, 90, "Sätze");
		ccTeamSets.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return compareSets(o1, o2);
			}
		});

		// Satzdifferenz
		final ColumnConfig<TeamDto, String> ccTeamSetDifference = createColumnConfig(TeamProperty.teamSetDifference, 90, "Satzdifferenz");
		ccTeamSetDifference.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return compareSetDifference(o1, o2);
			}
		});

		// Tore
		final ColumnConfig<TeamDto, String> ccTeamGoals = createColumnConfig(TeamProperty.teamGoals, 90, "Tore");
		ccTeamGoals.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return compareGoals(o1, o2);
			}
		});

		// Tordifferenz
		final ColumnConfig<TeamDto, String> ccTeamGoalDifference = createColumnConfig(TeamProperty.teamGoalDifference, 90, "Tordifferenz");
		ccTeamGoalDifference.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return compareGoalDifference(o1, o2);
			}
		});

		// Punkte
		final ColumnConfig<TeamDto, String> ccTeamPoints = createColumnConfig(TeamProperty.teamPoints, 90, "Punkte");
		ccTeamPoints.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final TeamDto team = storeTeamTable.findModelWithKey(context.getKey().toString());
				final int lastMatchPoints = team.getTeamStatsDto().getLastMatchPoints();
				concatLastMatchPoints(value, sb, lastMatchPoints);
			}
		});
		ccTeamPoints.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return comparePoints(o1, o2);
			}
		});

		// Tendenz
		final ColumnConfig<TeamDto, ImageResource> ccTeamTendency = createColumnConfig(TeamProperty.teamTendency, 80, "Tendenz");
		ccTeamTendency.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final TeamDto team = storeTeamTable.findModelWithKey(context.getKey().toString());
				sb.appendHtmlConstant("<div qtitle='Vorheriger Tabellenplatz' qtip='Platz " + team.getTeamStatsDto().getPrevTablePlace() + "' qwidth='160px'>");
				sb.append(AbstractImagePrototype.create(value).getSafeHtml());
				sb.appendHtmlConstant("</div>");
			}
		});
		ccTeamTendency.setComparator(new Comparator<ImageResource>() {
			@Override
			public int compare(ImageResource o1, ImageResource o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		final ArrayList<ColumnConfig<TeamDto, ?>> columns = new ArrayList<ColumnConfig<TeamDto, ?>>();
		columns.add(numberer);
		columns.add(ccTeamName);
		columns.add(ccTeamMatches);
		columns.add(ccTeamWins);
		columns.add(ccTeamDefeats);
		columns.add(ccTeamSets);
		columns.add(ccTeamSetDifference);
		columns.add(ccTeamGoals);
		columns.add(ccTeamGoalDifference);
		columns.add(ccTeamPoints);
		columns.add(ccTeamTendency);

		final Grid<TeamDto> gridTeamTable = new Grid<TeamDto>(storeTeamTable, new ColumnModel<TeamDto>(columns));
		gridTeamTable.getView().setAutoExpandColumn(ccTeamName);
		gridTeamTable.getView().setAutoExpandMax(1000);
		gridTeamTable.getView().setStripeRows(true);
		new QuickTip(gridTeamTable);

		return gridTeamTable;
	}

	/**
	 * Erzeugt die Konfiguration für eine Spalte.
	 * 
	 * @param provider Der Lieferant für den Wert.
	 * @param width Die Breite der Spalte als <code>int</code>.
	 * @param header Der Text der Spalte als <code>String</code>.
	 * @param <M> Der Typ der Klasse.
	 * @param <N> Der Typ der Klasse.
	 * @return Die erzeugte Konfiguration.
	 */
	private <M extends BaseDto, N extends Object> ColumnConfig<M, N> createColumnConfig(ValueProvider<M, N> provider, int width, String header) {
		final ColumnConfig<M, N> cc = new ColumnConfig<M, N>(provider, width, header);
		cc.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		return cc;
	}

	/**
	 * Fügt die letzten Spielpunkte für ein Team bzw. Spieler hinzu.
	 * 
	 * @param value Die aktuelle Punkte des Teams bzw Spielers.
	 * @param sb Der SafteHtmlBuilder.
	 * @param lastMatchPoints Die letzten Punkte für ein Team bzw. Spieler.
	 */
	private void concatLastMatchPoints(String value, SafeHtmlBuilder sb, final int lastMatchPoints) {
		final String style = "style='color: " + (lastMatchPoints >= 0 ? "#82B22C" : "#FF5240") + "'";

		final StringBuilder builder = new StringBuilder();
		if (lastMatchPoints >= 0) {
			builder.append("+" + Integer.toString(lastMatchPoints));
		} else {
			builder.append(Integer.toString(lastMatchPoints));
		}
		sb.appendHtmlConstant(value);
		sb.appendHtmlConstant(" <span " + style + ">(" + builder.toString() + ")</span>");
	}

	/**
	 * Vergleicht die Sätze der Teams bzw. Spieler miteinander.
	 * 
	 * @param o1 Die Sätze vom ersten Team bzw. vom ersten Spieler.
	 * @param o2 Die Sätze vom zweiten Team bzw. vom zweiten Spieler.
	 * @return Der Vergleich als {@link Integer}.
	 */
	private int compareSets(String o1, String o2) {
		final Integer winSets1 = Integer.parseInt(o1.substring(0, o1.indexOf(":")));
		final Integer winSets2 = Integer.parseInt(o2.substring(0, o2.indexOf(":")));

		int comp = winSets1.compareTo(winSets2);
		if (comp == 0) {
			final Integer lostSets1 = Integer.parseInt(o1.substring(o1.indexOf(":") + 1, o1.length()));
			final Integer lostSets2 = Integer.parseInt(o2.substring(o2.indexOf(":") + 1, o2.length()));

			comp = lostSets2.compareTo(lostSets1);
		}
		return comp;
	}

	/**
	 * Vergleicht den Satzunterschied der Teams bzw. Spieler miteinander.
	 * 
	 * @param o1 Der Satzunterschied vom ersten Team bzw. vom ersten Spieler.
	 * @param o2 Der Satzunterschied vom zweiten Team bzw. vom zweiten Spieler.
	 * @return Der Vergleich als {@link Integer}.
	 */
	private int compareSetDifference(String o1, String o2) {
		Integer setDifference1 = 0;
		if (o1.startsWith("+")) {
			setDifference1 = Integer.parseInt(o1.substring(1, o1.length()));
		} else {
			setDifference1 = Integer.parseInt(o1);
		}
		Integer setDifference2 = 0;
		if (o2.startsWith("+")) {
			setDifference2 = Integer.parseInt(o2.substring(1, o2.length()));
		} else {
			setDifference2 = Integer.parseInt(o2);
		}
		return setDifference1.compareTo(setDifference2);
	}

	/**
	 * Vergleicht die Tore der Teams bzw. Spieler miteinander.
	 * 
	 * @param o1 Die Tore vom ersten Team bzw. vom ersten Spieler.
	 * @param o2 Die Tore vom zweiten Team bzw. vom zweiten Spieler.
	 * @return Der Vergleich als {@link Integer}.
	 */
	private int compareGoals(String o1, String o2) {
		final Integer shotGoals1 = Integer.parseInt(o1.substring(0, o1.indexOf(":")));
		final Integer shotGoals2 = Integer.parseInt(o2.substring(0, o2.indexOf(":")));

		int comp = shotGoals1.compareTo(shotGoals2);
		if (comp == 0) {
			final Integer getGoals1 = Integer.parseInt(o1.substring(o1.indexOf(":") + 1, o1.length()));
			final Integer getGoals2 = Integer.parseInt(o2.substring(o2.indexOf(":") + 1, o2.length()));

			comp = getGoals2.compareTo(getGoals1);
		}
		return comp;
	}

	/**
	 * Vergleicht den Punkteunterschied der Teams bzw. Spieler miteinander.
	 * 
	 * @param o1 Der Punkteunterschied vom ersten Team bzw. vom ersten Spieler.
	 * @param o2 Der Punkteunterschied vom zweiten Team bzw. vom zweiten Spieler.
	 * @return Der Vergleich als {@link Integer}.
	 */
	private int compareGoalDifference(String o1, String o2) {
		Integer goalDifference1 = 0;
		if (o1.startsWith("+")) {
			goalDifference1 = Integer.parseInt(o1.substring(1, o1.length()));
		} else {
			goalDifference1 = Integer.parseInt(o1);
		}
		Integer goalDifference2 = 0;
		if (o2.startsWith("+")) {
			goalDifference2 = Integer.parseInt(o2.substring(1, o2.length()));
		} else {
			goalDifference2 = Integer.parseInt(o2);
		}
		return goalDifference1.compareTo(goalDifference2);
	}

	/**
	 * Vergleicht die Punkte der Teams bzw. Spieler miteinander.
	 * 
	 * @param o1 Die Punkte vom ersten Team bzw. vom ersten Spieler.
	 * @param o2 Die Punkte vom zweiten Team bzw. vom zweiten Spieler.
	 * @return Der Vergleich als {@link Integer}.
	 */
	private int comparePoints(String o1, String o2) {
		final Integer points1 = Integer.parseInt(o1);
		final Integer points2 = Integer.parseInt(o2);

		return points1.compareTo(points2);
	}

	/**
	 * Liefert die Team- bzw. Spielertabelle für das aktuell aktive Tab.
	 */
	private void getTableForActiveTab() {
		if (activeTab == 0) {
			getSingleTable();
		} else if (activeTab == 1) {
			getDoubleTable();
		} else {
			getDoubleTableTeamView();
		}
	}

	/**
	 * Liefert die Daten für die Einzelspielertabelle.
	 */
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
					AppExceptionHandler.getInstance().handleException(caught, false);
				}
			});
		}
	}

	/**
	 * Liefert die Daten für die Doppelspielertabelle.
	 */
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
					AppExceptionHandler.getInstance().handleException(caught, false);
				}
			});
		}
	}

	/**
	 * Liefert die Daten für die Teamtabelle.
	 */
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
					AppExceptionHandler.getInstance().handleException(caught, false);
				}
			});
		}
	}

	/**
	 * Erzeugt den Button zum Aktualisieren der Tabellen.
	 * 
	 * @return Der erzeugte Button.
	 */
	private AppButton createBtnUpdate() {
		final AppButton btnUpdate = new AppButton("Aktualisieren", IconProvider.get().update());
		btnUpdate.setToolTip("Aktualisiert die Tabelle");
		btnUpdate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				setDoUpdate();
				getTableForActiveTab();
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
		final NavigationElement navigationElement = event.getNavigationElement();
		if (navigationElement != NavigationElement.UNKOWN) {
			activeTab = navigationElement.getTabIndex();
			tabPanel.setActiveWidget(tabPanel.getWidget(activeTab));
		}
		AppEventBus.fireEvent(new SelectNavigationElementEvent(SelectNavigationElementEvent.TABLES, activeTab));
		getTableForActiveTab();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActiveWidget(TabPanelEvent event) {
		activeTab = event.getTabIndex();
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
