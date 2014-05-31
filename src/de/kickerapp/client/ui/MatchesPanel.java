package de.kickerapp.client.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.i18n.client.constants.TimeZoneConstants;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
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
import com.sencha.gxt.widget.core.client.grid.GroupSummaryView;
import com.sencha.gxt.widget.core.client.grid.SummaryColumnConfig;
import com.sencha.gxt.widget.core.client.grid.SummaryRenderer;
import com.sencha.gxt.widget.core.client.grid.SummaryType;
import com.sencha.gxt.widget.core.client.grid.filters.DateFilter;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.ListFilter;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
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
import de.kickerapp.client.ui.base.BasePanel;
import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.MatchDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;

/**
 * Controller-Klasse für die Spielergebnisse.
 * 
 * @author Sebastian Filke
 */
public class MatchesPanel extends BasePanel implements ShowDataEventHandler, UpdatePanelEventHandler {

	/** Der Store für die Spielergebnisse. */
	private ListStore<MatchDto> storeMatches;
	/** Der Angabe, ob die Spielergebnisse aktualisiert werden sollen. */
	private boolean doUpdateMatches;

	/**
	 * Erzeugt einen neuen Controller für die Spielergebnisse.
	 */
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

		doUpdateMatches = true;

		storeMatches = new ListStore<MatchDto>(KickerProperties.MATCH_PROPERTY.id());

		final VerticalLayoutContainer vlcMatches = new VerticalLayoutContainer();
		vlcMatches.add(createMatchesToolBar(), new VerticalLayoutData(1, -1));
		vlcMatches.add(createMatchesGrid(), new VerticalLayoutData(1, 1));

		add(vlcMatches);
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

	/**
	 * Erzeugt die ToolBar für die Spielergebnisse.
	 * 
	 * @return Die erzeugte ToolBar.
	 */
	private ToolBar createMatchesToolBar() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		final StoreFilterField<MatchDto> sffGrid = new StoreFilterField<MatchDto>() {
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

				final String[] queries = filter.split(" ");

				final boolean containsPlayer1 = checkPlayer(team1.getPlayer1(), queries);
				boolean containsPlayer2 = false;
				if (team1.getPlayer2() != null) {
					containsPlayer2 = checkPlayer(team1.getPlayer2(), queries);
				}

				boolean select = false;
				if (item.getMatchType() == MatchType.SINGLE) {
					if (queries.length == 1) {
						select = containsPlayer1;
					} else {
						select = false;
					}
				} else {
					if (queries.length == 1) {
						select = containsPlayer1 || containsPlayer2;
					} else {
						select = containsPlayer1 && containsPlayer2;
					}
				}
				return select;
			}

			private boolean checkTeam2(MatchDto item, String filter) {
				final TeamDto team2 = item.getTeam2Dto();

				final String[] queries = filter.split(" ");

				final boolean containsPlayer1 = checkPlayer(team2.getPlayer1(), queries);
				boolean containsPlayer2 = false;
				if (team2.getPlayer2() != null) {
					containsPlayer2 = checkPlayer(team2.getPlayer2(), queries);
				}

				boolean select = false;
				if (item.getMatchType() == MatchType.SINGLE) {
					if (queries.length == 1) {
						select = containsPlayer1;
					} else {
						select = false;
					}
				} else {
					if (queries.length == 1) {
						select = containsPlayer1 || containsPlayer2;
					} else {
						select = containsPlayer1 && containsPlayer2;
					}
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
		sffGrid.setEmptyText("Nach Spieler/Team suchen...");
		sffGrid.setWidth(250);
		sffGrid.bind(storeMatches);

		toolBar.add(createBtnUpdate());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(sffGrid);

		return toolBar;
	}

	/**
	 * Erzeugt das Grid für die Spielergebnisse.
	 * 
	 * @return Das erzeugte Grid.
	 */
	public Grid<MatchDto> createMatchesGrid() {
		// Spielnummer
		final SummaryColumnConfig<MatchDto, Integer> ccMatchNumber = new SummaryColumnConfig<MatchDto, Integer>(KickerProperties.MATCH_PROPERTY.matchNumber(),
				60, "Nr.");
		ccMatchNumber.setSummaryType(new SummaryType.CountSummaryType<Integer>());
		ccMatchNumber.setSummaryRenderer(new SummaryRenderer<MatchDto>() {
			@Override
			public SafeHtml render(Number value, Map<ValueProvider<? super MatchDto, ?>, Number> data) {
				return SafeHtmlUtils.fromTrustedString(value.intValue() > 1 ? "(" + value.intValue() + " Spiele)" : "(1 Spiel)");
			}
		});
		ccMatchNumber.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ccMatchNumber.setMenuDisabled(true);
		ccMatchNumber.setGroupable(false);

		// Datum
		final SummaryColumnConfig<MatchDto, Date> ccMatchDate = new SummaryColumnConfig<MatchDto, Date>(KickerProperties.MATCH_PROPERTY.matchDate(), 110,
				"Datum");
		ccMatchDate.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ccMatchDate.setGroupable(false);
		final TimeZoneConstants timeZoneConstant = (TimeZoneConstants) GWT.create(TimeZoneConstants.class);
		ccMatchDate.setCell(new DateCell(DateTimeFormat.getFormat("dd.MM.yyyy HH:mm"), TimeZone.createTimeZone(timeZoneConstant.europeBerlin())));

		// Gruppe
		final SummaryColumnConfig<MatchDto, String> ccGroupDate = new SummaryColumnConfig<MatchDto, String>(KickerProperties.MATCH_PROPERTY.groupDate(), 160,
				"Gruppe");
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

		// Typ
		final SummaryColumnConfig<MatchDto, String> ccMatchType = new SummaryColumnConfig<MatchDto, String>(MatchProperty.matchType, 70, "Typ");
		ccMatchType.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		// Spieler/Team 1
		final SummaryColumnConfig<MatchDto, String> ccTeam1 = new SummaryColumnConfig<MatchDto, String>(MatchProperty.team1, 270, "Spieler/Team 1");
		ccTeam1.setMenuDisabled(true);
		ccTeam1.setGroupable(false);
		ccTeam1.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final MatchDto match = storeMatches.findModelWithKey(context.getKey().toString());
				if (match != null && isTeam1Winner(match)) {
					sb.appendHtmlConstant("<b>" + value + "</b>");
				} else {
					sb.appendHtmlConstant(value);
				}
			}
		});

		// Spieler/Team 2
		final SummaryColumnConfig<MatchDto, String> ccTeam2 = new SummaryColumnConfig<MatchDto, String>(MatchProperty.team2, 270, "Spieler/Team 2");
		ccTeam2.setMenuDisabled(true);
		ccTeam2.setGroupable(false);
		ccTeam2.setCell(new AbstractCell<String>() {
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				final MatchDto match = storeMatches.findModelWithKey(context.getKey().toString());
				if (match != null && !isTeam1Winner(match)) {
					sb.appendHtmlConstant("<b>" + value + "</b>");
				} else {
					sb.appendHtmlConstant(value);
				}
			}
		});

		// Ergebnis
		final SummaryColumnConfig<MatchDto, String> ccMatchResult = new SummaryColumnConfig<MatchDto, String>(MatchProperty.matchResult, 60, "Ergebnis");
		ccMatchResult.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ccMatchResult.setMenuDisabled(true);
		ccMatchResult.setGroupable(false);

		// Sätze
		final SummaryColumnConfig<MatchDto, String> ccMatchSets = new SummaryColumnConfig<MatchDto, String>(MatchProperty.matchSets, 80, "Sätze");
		ccMatchSets.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ccMatchSets.setMenuDisabled(true);
		ccMatchSets.setGroupable(false);

		// Punkte Spieler/Team1
		final SummaryColumnConfig<MatchDto, String> ccMatchPointsTeam1 = new SummaryColumnConfig<MatchDto, String>(MatchProperty.matchPointsTeam1, 120,
				"Punkte Spieler/Team1");
		ccMatchPointsTeam1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ccMatchPointsTeam1.setMenuDisabled(true);
		ccMatchSets.setGroupable(false);

		// Punkte Spieler/Team2
		final SummaryColumnConfig<MatchDto, String> ccMatchPointsTeam2 = new SummaryColumnConfig<MatchDto, String>(MatchProperty.matchPointsTeam2, 120,
				"Punkte Spieler/Team2");
		ccMatchPointsTeam2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ccMatchPointsTeam2.setMenuDisabled(true);
		ccMatchSets.setGroupable(false);

		// Kommentar
		final SummaryColumnConfig<MatchDto, ImageResource> ccLabelComment = new SummaryColumnConfig<MatchDto, ImageResource>(MatchProperty.matchComment, 25);
		ccLabelComment.setHeader(AbstractImagePrototype.create(KickerIcons.ICON.comments10()).getSafeHtml());
		ccLabelComment.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		ccLabelComment.setToolTip(SafeHtmlUtils.fromString("Kommentar zum Spiel"));
		ccLabelComment.setMenuDisabled(true);
		ccLabelComment.setGroupable(false);
		ccLabelComment.setCell(new ImageResourceCell() {
			@Override
			public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
				final MatchDto selectedMatch = storeMatches.findModelWithKey(context.getKey().toString());
				if (selectedMatch.getMatchCommentDto() != null) {
					sb.appendHtmlConstant("<div qtitle='Kommentar' qtip='" + selectedMatch.getMatchCommentDto().getComment() + "'>");
					sb.append(AbstractImagePrototype.create(value).getSafeHtml());
					sb.appendHtmlConstant("</div>");
				}
			}
		});

		final ArrayList<ColumnConfig<MatchDto, ?>> columns = new ArrayList<ColumnConfig<MatchDto, ?>>();
		columns.add(ccMatchNumber);
		columns.add(ccMatchDate);
		columns.add(ccGroupDate);
		columns.add(ccMatchType);
		columns.add(ccTeam1);
		columns.add(ccTeam2);
		columns.add(ccMatchResult);
		columns.add(ccMatchSets);
		columns.add(ccMatchPointsTeam1);
		columns.add(ccMatchPointsTeam2);
		columns.add(ccLabelComment);

		final GroupSummaryView<MatchDto> view = new GroupSummaryView<MatchDto>();
		view.setAutoExpandColumn(ccMatchSets);
		view.setShowGroupedColumn(false);
		view.setAutoExpandMax(1000);
		view.groupBy(ccGroupDate);
		view.setStripeRows(true);

		final Grid<MatchDto> grid = new Grid<MatchDto>(storeMatches, new ColumnModel<MatchDto>(columns));
		grid.setView(view);
		new QuickTip(grid);

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

	/**
	 * Liefert die Angabe, ob das erste Team gewonnen hat.
	 * 
	 * @param matchDto Das Spiel.
	 * @return <code>true</code> falls, das erste Team gewonnen hat, ansonsten <code>false</code>.
	 */
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

	/**
	 * Liefert die Daten für die Spielergebnisse.
	 */
	private void getMatches() {
		if (doUpdateMatches) {
			mask("Aktualisiere...");
			KickerServices.MATCH_SERVICE.getAllMatches(new AsyncCallback<ArrayList<MatchDto>>() {
				@Override
				public void onSuccess(ArrayList<MatchDto> result) {
					storeMatches.replaceAll(result);
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

	/**
	 * Erzeugt den Button zum Aktualisieren der Spielergebnisse.
	 * 
	 * @return Der erzeugte Button.
	 */
	private AppButton createBtnUpdate() {
		final AppButton btnUpdate = new AppButton("Aktualisieren", KickerIcons.ICON.update());
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
