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

/**
 * Controller-Klasse für die Team- bzw. Spielertabellen.
 * 
 * @author Sebastian Filke
 */
public class TablesPanel extends BasePanel implements ShowDataEventHandler, UpdatePanelEventHandler, TabPanelEventHandler {

	/** Der Store für die Einzelspielertabelle. */
	private ListStore<PlayerDto> storeSingleTable;
	/** Der Store für die Doppelspielertabelle. */
	private ListStore<PlayerDto> storeDoubleTable;
	/** Der Store für die Teamtabelle. */
	private ListStore<TeamDto> storeTeamTable;
	/** Der aktuelle oder anzuzeigende Tab. */
	private int activeTab;
	/** Der TabPanel zum Anzeigen der verschiedenen Tabellen. */
	private TabPanel tabPanel;
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

	/**
	 * Erzeugt den TabPanel zum Anzeigen der verschiedenen Tabellen.
	 * 
	 * @return Der erzeugte TabPanel.
	 */
	private TabPanel createTabPanel() {
		final PlainTabPanel tabPanel = new PlainTabPanel();
		tabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				final TabPanel panel = (TabPanel) event.getSource();
				final Widget w = event.getSelectedItem();

				activeTab = panel.getWidgetIndex(w);
				tabPanel.forceLayout();
				getTableForActiveTab();
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
		vlcDoubleTable.add(createDoubleTableGrid(), new VerticalLayoutData(1, 1));

		final TabItemConfig ticDoubleTable = new TabItemConfig("Doppeltabelle");
		ticDoubleTable.setIcon(KickerIcons.ICON.group());

		final VerticalLayoutContainer vlcTeamTable = new VerticalLayoutContainer();
		vlcTeamTable.add(createTeamTableToolBar(), new VerticalLayoutData(1, -1));
		vlcTeamTable.add(createTeamTableGrid(), new VerticalLayoutData(1, 1));

		final TabItemConfig ticTeamTable = new TabItemConfig("Teamtabelle");
		ticTeamTable.setIcon(KickerIcons.ICON.group_link());

		tabPanel.add(vlcSingleTable, ticSingleTable);
		tabPanel.add(vlcDoubleTable, ticDoubleTable);
		tabPanel.add(vlcTeamTable, ticTeamTable);
		tabPanel.setBodyBorder(false);
		tabPanel.setBorders(false);

		return tabPanel;
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
		sfcNoMatchPlayer.setIcon(KickerIcons.ICON.table_sort());
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
		sffSingleTable.setWidth(250);
		sffSingleTable.bind(storeSingleTable);

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
		ccSingleGoals.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
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
		});
		final ColumnConfig<PlayerDto, String> ccSingleGoalDifference = new ColumnConfig<PlayerDto, String>(PlayerProperty.singleGoalDifference, 100,
				"Tordifferenz");
		ccSingleGoalDifference.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				final Integer goalDifference1 = Integer.parseInt(o1.substring(1, o1.length()));
				final Integer goalDifference2 = Integer.parseInt(o2.substring(1, o2.length()));

				return goalDifference1.compareTo(goalDifference2);
			}
		});
		final ColumnConfig<PlayerDto, String> ccSinglePoints = new ColumnConfig<PlayerDto, String>(PlayerProperty.singlePoints, 100, "Punkte");
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
				final Integer points1 = Integer.parseInt(o1);
				final Integer points2 = Integer.parseInt(o2);

				return points1.compareTo(points2);
			}
		});
		final ColumnConfig<PlayerDto, ImageResource> ccSingleTendency = new ColumnConfig<PlayerDto, ImageResource>(PlayerProperty.singleTendency, 80, "Tendenz");
		ccSingleTendency.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final PlayerDto player = storeSingleTable.findModelWithKey(context.getKey().toString());
				sb.appendHtmlConstant("<span qtitle='Vorher' qtip='Platz " + player.getPlayerSingleStatsDto().getPrevTablePlace() + "'>");
				sb.append(AbstractImagePrototype.create(value).getSafeHtml());
				sb.appendHtmlConstant("</span>");
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
		columns.add(ccSingleLosses);
		columns.add(ccSingleGoals);
		columns.add(ccSingleGoalDifference);
		columns.add(ccSinglePoints);
		columns.add(ccSingleTendency);

		final Grid<PlayerDto> grid = new Grid<PlayerDto>(storeSingleTable, new ColumnModel<PlayerDto>(columns));
		grid.getView().setAutoExpandColumn(ccPlayerName);
		grid.getView().setAutoExpandMax(1000);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		new QuickTip(grid);

		return grid;
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
		sfcNoMatchPlayer.setIcon(KickerIcons.ICON.table_sort());
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
		final ColumnConfig<PlayerDto, Integer> ccDoubleMatches = new ColumnConfig<PlayerDto, Integer>(PlayerProperty.doubleMatches, 100, "Anzahl Spiele");
		final ColumnConfig<PlayerDto, Integer> ccDoubleWins = new ColumnConfig<PlayerDto, Integer>(PlayerProperty.doubleWins, 100, "Siege");
		final ColumnConfig<PlayerDto, Integer> ccDoubleLosses = new ColumnConfig<PlayerDto, Integer>(PlayerProperty.doubleLosses, 100, "Niederlagen");
		final ColumnConfig<PlayerDto, String> ccDoubleGoals = new ColumnConfig<PlayerDto, String>(PlayerProperty.doubleGoals, 100, "Tore");
		ccDoubleGoals.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
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
		});
		final ColumnConfig<PlayerDto, String> ccDoubleGoalDifference = new ColumnConfig<PlayerDto, String>(PlayerProperty.doubleGoalDifference, 100,
				"Tordifferenz");
		ccDoubleGoalDifference.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				final Integer goalDifference1 = Integer.parseInt(o1.substring(1, o1.length()));
				final Integer goalDifference2 = Integer.parseInt(o2.substring(1, o2.length()));

				return goalDifference1.compareTo(goalDifference2);
			}
		});
		final ColumnConfig<PlayerDto, String> ccDoublePoints = new ColumnConfig<PlayerDto, String>(PlayerProperty.doublePoints, 100, "Punkte");
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
				final Integer points1 = Integer.parseInt(o1);
				final Integer points2 = Integer.parseInt(o2);

				return points1.compareTo(points2);
			}
		});
		final ColumnConfig<PlayerDto, ImageResource> ccDoubleTendency = new ColumnConfig<PlayerDto, ImageResource>(PlayerProperty.doubleTendency, 80, "Tendenz");
		ccDoubleTendency.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final PlayerDto player = storeDoubleTable.findModelWithKey(context.getKey().toString());
				sb.appendHtmlConstant("<span qtitle='Vorher' qtip='Platz " + player.getPlayerDoubleStatsDto().getPrevTablePlace() + "'>");
				sb.append(AbstractImagePrototype.create(value).getSafeHtml());
				sb.appendHtmlConstant("</span>");
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
		columns.add(ccDoubleLosses);
		columns.add(ccDoubleGoals);
		columns.add(ccDoubleGoalDifference);
		columns.add(ccDoublePoints);
		columns.add(ccDoubleTendency);

		final Grid<PlayerDto> grid = new Grid<PlayerDto>(storeDoubleTable, new ColumnModel<PlayerDto>(columns));
		grid.getView().setAutoExpandColumn(ccPlayerName);
		grid.getView().setAutoExpandMax(1000);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		new QuickTip(grid);

		return grid;
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
	public Grid<TeamDto> createTeamTableGrid() {
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

		final ColumnConfig<TeamDto, String> ccTeamName = new ColumnConfig<TeamDto, String>(TeamProperty.teamName, 140, "Team");
		final ColumnConfig<TeamDto, Integer> ccTeamMatches = new ColumnConfig<TeamDto, Integer>(TeamProperty.teamMatches, 100, "Anzahl Spiele");
		final ColumnConfig<TeamDto, Integer> ccTeamWins = new ColumnConfig<TeamDto, Integer>(TeamProperty.teamWins, 100, "Siege");
		final ColumnConfig<TeamDto, Integer> ccTeamLosses = new ColumnConfig<TeamDto, Integer>(TeamProperty.teamLosses, 100, "Niederlagen");
		final ColumnConfig<TeamDto, String> ccTeamGoals = new ColumnConfig<TeamDto, String>(TeamProperty.teamGoals, 100, "Tore");
		ccTeamGoals.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
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
		});
		final ColumnConfig<TeamDto, String> ccTeamGoalDifference = new ColumnConfig<TeamDto, String>(TeamProperty.teamGoalDifference, 100, "Tordifferenz");
		ccTeamGoalDifference.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				final Integer goalDifference1 = Integer.parseInt(o1.substring(1, o1.length()));
				final Integer goalDifference2 = Integer.parseInt(o2.substring(1, o2.length()));

				return goalDifference1.compareTo(goalDifference2);
			}
		});
		final ColumnConfig<TeamDto, String> ccTeamPoints = new ColumnConfig<TeamDto, String>(TeamProperty.teamPoints, 100, "Punkte");
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
				final Integer points1 = Integer.parseInt(o1);
				final Integer points2 = Integer.parseInt(o2);

				return points1.compareTo(points2);
			}
		});
		final ColumnConfig<TeamDto, ImageResource> ccTeamTendency = new ColumnConfig<TeamDto, ImageResource>(TeamProperty.teamTendency, 80, "Tendenz");
		ccTeamTendency.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final TeamDto team = storeTeamTable.findModelWithKey(context.getKey().toString());
				sb.appendHtmlConstant("<span qtitle='Vorher' qtip='Platz " + team.getTeamStatsDto().getPrevTablePlace() + "'>");
				sb.append(AbstractImagePrototype.create(value).getSafeHtml());
				sb.appendHtmlConstant("</span>");
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
		columns.add(ccTeamLosses);
		columns.add(ccTeamGoals);
		columns.add(ccTeamGoalDifference);
		columns.add(ccTeamPoints);
		columns.add(ccTeamTendency);

		final Grid<TeamDto> grid = new Grid<TeamDto>(storeTeamTable, new ColumnModel<TeamDto>(columns));
		grid.getView().setAutoExpandColumn(ccTeamName);
		grid.getView().setAutoExpandMax(1000);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		new QuickTip(grid);

		return grid;
	}

	/**
	 * Fügt die letzten Spielpunkte für ein Team bzw. Spieler hinzu.
	 * 
	 * @param value Die aktuelle Punkte des Teams bzw Spielers.
	 * @param sb Der SafteHtmlBuilder.
	 * @param lastMatchPoints Die letzten Punkte für ein Team bzw. Spieler.
	 */
	private void concatLastMatchPoints(String value, SafeHtmlBuilder sb, final int lastMatchPoints) {
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
					AppExceptionHandler.handleException(caught);
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
					AppExceptionHandler.handleException(caught);
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
					AppExceptionHandler.handleException(caught);
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
		final AppButton btnUpdate = new AppButton("Aktualisieren", KickerIcons.ICON.table_refresh());
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
		getTableForActiveTab();
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
