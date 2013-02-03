package de.kickerapp.client.ui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.filters.DateFilter;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.ListFilter;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.ShowDataEvent;
import de.kickerapp.client.event.ShowDataEventHandler;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.properties.MatchProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.IMatch;
import de.kickerapp.shared.dto.MatchDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;

/**
 * @author Sebastian Filke
 */
public class MatchesPanel extends BasePanel implements ShowDataEventHandler {

	private ListStore<IMatch> store;
	private AppButton btnUpdate;
	private StoreFilterField<IMatch> sffGrid;

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

		store = new ListStore<IMatch>(KickerProperties.MATCH_PROPERTY.id());

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

		AppEventBus.addHandler(ShowDataEvent.ALL_PANEL, this);
	}

	private ToolBar createToolBar() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		sffGrid = new StoreFilterField<IMatch>() {
			@Override
			protected boolean doSelect(Store<IMatch> store, IMatch parent, IMatch item, String filter) {
				boolean select = false;

				select = checkTeam1(item, filter);
				if (!select) {
					select = checkTeam2(item, filter);
				}
				return select;
			}

			private boolean checkTeam1(IMatch item, String filter) {
				final TeamDto team1 = item.getTeam1();

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

			private boolean checkTeam2(IMatch item, String filter) {
				final TeamDto team2 = item.getTeam2();

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

		toolBar.add(sffGrid);
		return toolBar;
	}

	public Grid<IMatch> createGrid() {
		final ColumnConfig<IMatch, String> ccNumber = new ColumnConfig<IMatch, String>(KickerProperties.MATCH_PROPERTY.matchNumber(), 40, "Nr.");
		final ColumnConfig<IMatch, Date> ccMatchDate = new ColumnConfig<IMatch, Date>(KickerProperties.MATCH_PROPERTY.matchDate(), 120, "Datum");
		ccMatchDate.setCell(new DateCell(DateTimeFormat.getFormat("dd.MM.yyyy HH:mm")));
		final ColumnConfig<IMatch, MatchType> ccMatchType = new ColumnConfig<IMatch, MatchType>(KickerProperties.MATCH_PROPERTY.matchType(), 80, "Typ");
		final ColumnConfig<IMatch, String> ccTeam1 = new ColumnConfig<IMatch, String>(MatchProperty.team1, 220, "Spieler/Team 1");
		final ColumnConfig<IMatch, String> ccTeam2 = new ColumnConfig<IMatch, String>(MatchProperty.team2, 220, "Spieler/Team 2");
		final ColumnConfig<IMatch, String> ccMatchResult = new ColumnConfig<IMatch, String>(MatchProperty.matchResult, 60, "Ergebnis");
		final ColumnConfig<IMatch, String> ccMatchSets = new ColumnConfig<IMatch, String>(MatchProperty.matchSets, 150, "Sätze");

		final ArrayList<ColumnConfig<IMatch, ?>> columns = new ArrayList<ColumnConfig<IMatch, ?>>();
		columns.add(ccNumber);
		columns.add(ccMatchDate);
		columns.add(ccMatchType);
		columns.add(ccTeam1);
		columns.add(ccTeam2);
		columns.add(ccMatchResult);
		columns.add(ccMatchSets);

		final Grid<IMatch> grid = new Grid<IMatch>(store, new ColumnModel<IMatch>(columns));
		grid.getView().setAutoExpandColumn(ccMatchSets);
		grid.getView().setAutoExpandMax(1000);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);

		final ListStore<MatchType> lsMatchType = new ListStore<MatchType>(new ModelKeyProvider<MatchType>() {
			@Override
			public String getKey(MatchType item) {
				return item.toString();
			}
		});
		lsMatchType.add(MatchType.Single);
		lsMatchType.add(MatchType.Double);

		final ListFilter<IMatch, MatchType> lfMatchType = new ListFilter<IMatch, MatchType>(KickerProperties.MATCH_PROPERTY.matchType(), lsMatchType);
		final DateFilter<IMatch> dfMatchDate = new DateFilter<IMatch>(KickerProperties.MATCH_PROPERTY.matchDate());

		final GridFilters<IMatch> filters = new GridFilters<IMatch>();
		filters.initPlugin(grid);
		filters.setLocal(true);
		filters.addFilter(lfMatchType);
		filters.addFilter(dfMatchDate);

		return grid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initPanelButtons(Portlet portletMatches) {
		btnUpdate = new AppButton("Aktualisieren");
		btnUpdate.setToolTip("Aktualisiert die Ergebnisse der zuletzt gespielten Spiele");
		btnUpdate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				getMatches();
			}
		});
		portletMatches.addButton(btnUpdate);
	}

	private void getMatches() {
		btnUpdate.setEnabled(false);
		mask("Aktualisiere...");
		clearInput();
		KickerServices.MATCH_SERVICE.getAllMatches(new AsyncCallback<ArrayList<MatchDto>>() {
			@Override
			public void onSuccess(ArrayList<MatchDto> result) {
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

	private void clearInput() {
		sffGrid.clear();
		store.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showData(ShowDataEvent event) {
		getMatches();
	}

}
