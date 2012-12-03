package de.kickerapp.client.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import de.kickerapp.client.model.properties.MatchProperty;
import de.kickerapp.client.services.ServiceProvider;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.shared.match.Match;
import de.kickerapp.shared.match.MatchData;

/**
 * @author Sebastian Filke
 */
public class MatchesPanel extends BasePanel {

	private ListStore<MatchData> store;

	public MatchesPanel() {
		super();
	}

	@Override
	public void initLayout() {
		super.initLayout();

		initPanelButtons();

		add(createGrid());
	}

	public Grid<MatchData> createGrid() {
		MatchProperty matchProperty = GWT.create(MatchProperty.class);

		ColumnConfig<MatchData, String> nameCol = new ColumnConfig<MatchData, String>(matchProperty.set1(), 200, "Ergebnis");
		nameCol.setWidth(80);
		ColumnConfig<MatchData, String> symbolCol = new ColumnConfig<MatchData, String>(matchProperty.player1(), 100, "Spieler 1");
		symbolCol.setWidth(80);
		ColumnConfig<MatchData, String> lastCol = new ColumnConfig<MatchData, String>(matchProperty.player2(), 75, "Spieler 2");

		ArrayList<ColumnConfig<MatchData, ?>> columns = new ArrayList<ColumnConfig<MatchData, ?>>();
		columns.add(nameCol);
		columns.add(symbolCol);
		columns.add(lastCol);
		ColumnModel<MatchData> cm = new ColumnModel<MatchData>(columns);

		store = new ListStore<MatchData>(matchProperty.id());

		final Grid<MatchData> grid = new Grid<MatchData>(store, cm);
		grid.getView().setAutoExpandColumn(nameCol);
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
		store.clear();
		ServiceProvider.get().getAllMatches(new AsyncCallback<ArrayList<Match>>() {
			@Override
			public void onSuccess(ArrayList<Match> result) {
				store.addAll(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

}
