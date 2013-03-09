package de.kickerapp.client.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.i18n.client.constants.TimeZoneConstants;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GroupingView;
import com.sencha.gxt.widget.core.client.grid.filters.DateFilter;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.ListFilter;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.ShowDataEvent;
import de.kickerapp.client.event.ShowDataEventHandler;
import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.event.UpdatePanelEventHandler;
import de.kickerapp.client.exception.AppExceptionHandler;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.properties.MatchProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.shared.dto.MatchDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;

/**
 * @author Sebastian Filke
 */
public class MatchesPanel extends BasePanel implements ShowDataEventHandler, UpdatePanelEventHandler {

	private ListStore<MatchDto> store;

	private StoreFilterField<MatchDto> sffGrid;

	private boolean doUpdateMatches;

	public MatchesPanel() {
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
		setHeadingHtml("<span id='panelHeading'>Zuletzt Gespielt</span>");

		store = new ListStore<MatchDto>(KickerProperties.MATCH_PROPERTY.id());

		doUpdateMatches = true;

		final VerticalLayoutContainer vlcMain = new VerticalLayoutContainer();
		vlcMain.add(createToolBar(), new VerticalLayoutData(1, -1));
		vlcMain.add(createGrid(), new VerticalLayoutData(1, 1));

		add(vlcMain);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();

		AppEventBus.addHandler(ShowDataEvent.MATCHES, this);
		AppEventBus.addHandler(UpdatePanelEvent.ALL, this);
		AppEventBus.addHandler(UpdatePanelEvent.MATCHES, this);
	}

	private ToolBar createToolBar() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		sffGrid = new StoreFilterField<MatchDto>() {
			@Override
			protected boolean doSelect(Store<MatchDto> store, MatchDto parent, MatchDto item, String filter) {
				boolean select = false;

				select = checkTeam1(item, filter);
				if (!select) {
					select = checkTeam2(item, filter);
				}
				return select;
			}

			private boolean checkTeam1(MatchDto item, String filter) {
				final TeamDto team1 = item.getTeam1Dto();

				final PlayerDto player1 = team1.getPlayer1();
				if (player1.getLastName().toLowerCase().contains(filter) || player1.getFirstName().toLowerCase().contains(filter)) {
					return true;
				}

				final PlayerDto player2 = team1.getPlayer2();
				if (player2 != null) {
					if (player2.getLastName().toLowerCase().contains(filter) || player2.getFirstName().toLowerCase().contains(filter)) {
						return true;
					}
				}
				return false;
			}

			private boolean checkTeam2(MatchDto item, String filter) {
				final TeamDto team2 = item.getTeam2Dto();

				final PlayerDto player1 = team2.getPlayer1();
				if (player1.getLastName().toLowerCase().contains(filter) || player1.getFirstName().toLowerCase().contains(filter)) {
					return true;
				}

				final PlayerDto player2 = team2.getPlayer2();
				if (player2 != null) {
					if (player2.getLastName().toLowerCase().contains(filter) || player2.getFirstName().toLowerCase().contains(filter)) {
						return true;
					}
				}
				return false;
			}
		};
		sffGrid.bind(store);
		sffGrid.setWidth(250);
		sffGrid.setEmptyText("Nach Spieler/Team suchen...");

		toolBar.add(createBtnUpdate());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(sffGrid);
		return toolBar;
	}

	public Grid<MatchDto> createGrid() {
		final ColumnConfig<MatchDto, Integer> ccNumber = new ColumnConfig<MatchDto, Integer>(KickerProperties.MATCH_PROPERTY.matchNumber(), 40, "Nr.");
		ccNumber.setGroupable(false);
		final ColumnConfig<MatchDto, Date> ccMatchDate = new ColumnConfig<MatchDto, Date>(KickerProperties.MATCH_PROPERTY.matchDate(), 120, "Datum");
		ccMatchDate.setGroupable(false);
		final TimeZoneConstants t = (TimeZoneConstants) GWT.create(TimeZoneConstants.class);
		ccMatchDate.setCell(new DateCell(DateTimeFormat.getFormat("dd.MM.yyyy HH:mm"), TimeZone.createTimeZone(t.europeBerlin())));
		final ColumnConfig<MatchDto, String> ccGroupDate = new ColumnConfig<MatchDto, String>(KickerProperties.MATCH_PROPERTY.groupDate(), 160, "Gruppe");
		ccGroupDate.setComparator(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				final String date1 = o1.substring(o1.length() - 10, o1.length());
				final String date2 = o2.substring(o2.length() - 10, o2.length());

				final Integer year1 = Integer.parseInt(date1.substring(6, 10));
				final Integer year2 = Integer.parseInt(date2.substring(6, 10));

				int comp = year2.compareTo(year1);
				if (comp == 0) {
					final Integer month1 = Integer.parseInt(date1.substring(3, 5));
					final Integer month2 = Integer.parseInt(date2.substring(3, 5));

					comp = month2.compareTo(month1);
					if (comp == 0) {
						final Integer day1 = Integer.parseInt(date1.substring(0, 2));
						final Integer day2 = Integer.parseInt(date2.substring(0, 2));
						comp = day2.compareTo(day1);
					}
				}
				return comp;
			}
		});
		final ColumnConfig<MatchDto, String> ccMatchType = new ColumnConfig<MatchDto, String>(MatchProperty.matchType, 80, "Typ");
		final ColumnConfig<MatchDto, String> ccTeam1 = new ColumnConfig<MatchDto, String>(MatchProperty.team1, 270, "Spieler/Team 1");
		ccTeam1.setGroupable(false);
		ccTeam1.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final MatchDto match = store.findModelWithKey(context.getKey().toString());
				if (match != null && isTeam1Winner(match)) {
					sb.appendHtmlConstant("<b>" + value + "</b>");
				} else {
					sb.appendHtmlConstant(value);
				}
			}
		});
		final ColumnConfig<MatchDto, String> ccTeam2 = new ColumnConfig<MatchDto, String>(MatchProperty.team2, 270, "Spieler/Team 2");
		ccTeam2.setGroupable(false);
		ccTeam2.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final MatchDto match = store.findModelWithKey(context.getKey().toString());
				if (match != null && !isTeam1Winner(match)) {
					sb.appendHtmlConstant("<b>" + value + "</b>");
				} else {
					sb.appendHtmlConstant(value);
				}
			}
		});
		final ColumnConfig<MatchDto, String> ccMatchResult = new ColumnConfig<MatchDto, String>(MatchProperty.matchResult, 60, "Ergebnis");
		ccMatchResult.setGroupable(false);
		final ColumnConfig<MatchDto, String> ccMatchSets = new ColumnConfig<MatchDto, String>(MatchProperty.matchSets, 80, "Sätze");
		ccMatchSets.setGroupable(false);
		final ColumnConfig<MatchDto, String> ccMatchPointsTeam1 = new ColumnConfig<MatchDto, String>(MatchProperty.matchPointsTeam1, 120,
				"Punkte Spieler/Team1");
		ccMatchSets.setGroupable(false);
		final ColumnConfig<MatchDto, String> ccMatchPointsTeam2 = new ColumnConfig<MatchDto, String>(MatchProperty.matchPointsTeam2, 120,
				"Punkte Spieler/Team2");
		ccMatchSets.setGroupable(false);

		final ArrayList<ColumnConfig<MatchDto, ?>> columns = new ArrayList<ColumnConfig<MatchDto, ?>>();
		columns.add(ccNumber);
		columns.add(ccMatchDate);
		columns.add(ccGroupDate);
		columns.add(ccMatchType);
		columns.add(ccTeam1);
		columns.add(ccTeam2);
		columns.add(ccMatchResult);
		columns.add(ccMatchSets);
		columns.add(ccMatchPointsTeam1);
		columns.add(ccMatchPointsTeam2);

		final GroupingView<MatchDto> view = new GroupingView<MatchDto>();
		view.setAutoExpandColumn(ccMatchSets);
		view.setShowGroupedColumn(false);
		view.setAutoExpandMax(1000);
		view.groupBy(ccGroupDate);
		view.setStripeRows(true);
		view.setColumnLines(true);

		final Grid<MatchDto> grid = new Grid<MatchDto>(store, new ColumnModel<MatchDto>(columns));
		grid.setView(view);

		final ListStore<String> lsMatchType = new ListStore<String>(new ModelKeyProvider<String>() {
			@Override
			public String getKey(String item) {
				return item.toString();
			}
		});
		lsMatchType.add("Einzel");
		lsMatchType.add("Doppel");

		final ListFilter<MatchDto, String> lfMatchType = new ListFilter<MatchDto, String>(MatchProperty.matchType, lsMatchType);
		final DateFilter<MatchDto> dfMatchDate = new DateFilter<MatchDto>(KickerProperties.MATCH_PROPERTY.matchDate());

		final GridFilters<MatchDto> filters = new GridFilters<MatchDto>();
		filters.initPlugin(grid);
		filters.setLocal(true);
		filters.addFilter(lfMatchType);
		filters.addFilter(dfMatchDate);

		return grid;
	}

	private boolean isTeam1Winner(MatchDto matchDto) {
		boolean team1Winner = false;
		int size = 0;
		for (Integer result : matchDto.getMatchSetsDto().getMatchSetsTeam1()) {
			if (result != null && result == 6) {
				size++;
			}
			if (size == 2) {
				team1Winner = true;
				break;
			}
		}
		return team1Winner;
	}

	private AppButton createBtnUpdate() {
		final AppButton btnUpdate = new AppButton("Aktualisieren", KickerIcons.ICON.table_refresh());
		btnUpdate.setToolTip("Aktualisiert die Ergebnisse der zuletzt gespielten Spiele");
		btnUpdate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				setDoUpdate();
				getMatches();
			}

			private void setDoUpdate() {
				doUpdateMatches = true;
			}
		});
		return btnUpdate;
	}

	private void getMatches() {
		if (doUpdateMatches) {
			mask("Aktualisiere...");
			clearInput();
			KickerServices.MATCH_SERVICE.getAllMatches(new AsyncCallback<ArrayList<MatchDto>>() {
				@Override
				public void onSuccess(ArrayList<MatchDto> result) {
					store.replaceAll(result);
					doUpdateMatches = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdateMatches = false;
					unmask();
					AppExceptionHandler.handleException(caught);
				}
			});
		}
	}

	private void clearInput() {
		sffGrid.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showData(ShowDataEvent event) {
		getMatches();
	}

	@Override
	public void updatePanel(UpdatePanelEvent event) {
		doUpdateMatches = true;
	}

}
