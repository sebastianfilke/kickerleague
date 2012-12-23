package de.kickerapp.client.ui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import de.kickerapp.client.model.MatchProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.util.CursorDefs;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.shared.match.IMatch;
import de.kickerapp.shared.match.MatchDto;
import de.kickerapp.shared.match.TeamDto;

/**
 * @author Sebastian Filke
 */
public class MatchesPanel extends BasePanel {

	private ListStore<IMatch> store;

	public MatchesPanel() {
		super();
	}

	@Override
	public void initLayout() {
		super.initLayout();

		initPanelButtons();

		add(createGrid());
	}

	public Grid<IMatch> createGrid() {
		MatchProperty matchProperty = GWT.create(MatchProperty.class);

		ColumnConfig<IMatch, String> ccNumber = new ColumnConfig<IMatch, String>(matchProperty.matchNumber(), 200, "Nr.");
		ccNumber.setWidth(80);
		ColumnConfig<IMatch, Date> ccMatchDate = new ColumnConfig<IMatch, Date>(matchProperty.matchDate(), 100, "Datum");
		ccMatchDate.setWidth(80);
		ColumnConfig<IMatch, TeamDto> ccTeam1 = new ColumnConfig<IMatch, TeamDto>(matchProperty.team1(), 75, "Team 1");
		ccTeam1.setWidth(80);
		ColumnConfig<IMatch, TeamDto> ccTeam2 = new ColumnConfig<IMatch, TeamDto>(matchProperty.team2(), 75, "Team 2");
		ccTeam2.setWidth(80);
		ColumnConfig<IMatch, String> ccResult = new ColumnConfig<IMatch, String>(matchProperty.matchResult(), 75, "Ergebnis");
		ccResult.setWidth(80);

		ArrayList<ColumnConfig<IMatch, ?>> columns = new ArrayList<ColumnConfig<IMatch, ?>>();
		columns.add(ccNumber);
		columns.add(ccMatchDate);
		columns.add(ccTeam1);
		columns.add(ccTeam2);
		columns.add(ccResult);
		ColumnModel<IMatch> cm = new ColumnModel<IMatch>(columns);

		store = new ListStore<IMatch>(matchProperty.id());

		final Grid<IMatch> grid = new Grid<IMatch>(store, cm);
		grid.getView().setAutoExpandColumn(ccResult);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		grid.getView().setForceFit(true);

		return grid;
	}

	/**
	 * 
	 */
	private void initPanelButtons() {
		AppButton bGetMatches = new AppButton("Aktualisieren");
		bGetMatches.setToolTip("Aktualisiert die Ergebnisse der zuletzt gespieleten Spiele");
		bGetMatches.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				getMatches();
			}
		});
		addButton(bGetMatches);
	}

	private void getMatches() {
		CursorDefs.showWaitCursor();
		store.clear();

		// KickerServices.MATCH_SERVICE.getAllMatches(new
		// AsyncCallback<ArrayList<MatchDto>>() {
		// @Override
		// public void onSuccess(ArrayList<MatchDto> result) {
		// store.addAll(result);
		// CursorDefs.showDefaultCursor();
		// }
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// // TODO Auto-generated method stub
		// }
		// });
	}

}
