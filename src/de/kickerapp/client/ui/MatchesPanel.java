package de.kickerapp.client.ui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Portlet;
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

/**
 * @author Sebastian Filke
 */
public class MatchesPanel extends BasePanel {

	private ListStore<IMatch> store;
	private AppButton btnUpdate;

	public MatchesPanel() {
		super();
	}

	@Override
	public void initLayout() {
		super.initLayout();

		add(createGrid());
	}

	public Grid<IMatch> createGrid() {
		final MatchProperty matchProperty = GWT.create(MatchProperty.class);

		final ColumnConfig<IMatch, String> ccNumber = new ColumnConfig<IMatch, String>(matchProperty.matchNumber(), 40, "Nr.");
		final ColumnConfig<IMatch, Date> ccMatchDate = new ColumnConfig<IMatch, Date>(matchProperty.matchDate(), 100, "Datum");
		ccMatchDate.setCell(new DateCell(DateTimeFormat.getFormat("dd.MM.yyyy HH:mm")));
		final ColumnConfig<IMatch, String> ccTeam1 = new ColumnConfig<IMatch, String>(matchProperty.labelTeam1(), 120, "Team 1");
		final ColumnConfig<IMatch, String> ccTeam2 = new ColumnConfig<IMatch, String>(matchProperty.labelTeam2(), 120, "Team 2");
		final ColumnConfig<IMatch, String> ccResult = new ColumnConfig<IMatch, String>(matchProperty.labelSets(), 120, "Ergebnis");

		final ArrayList<ColumnConfig<IMatch, ?>> columns = new ArrayList<ColumnConfig<IMatch, ?>>();
		columns.add(ccNumber);
		columns.add(ccMatchDate);
		columns.add(ccTeam1);
		columns.add(ccTeam2);
		columns.add(ccResult);

		store = new ListStore<IMatch>(matchProperty.id());

		final Grid<IMatch> grid = new Grid<IMatch>(store, new ColumnModel<IMatch>(columns));
		grid.getView().setAutoExpandColumn(ccResult);
		grid.getView().setStripeRows(true);
		grid.getView().setColumnLines(true);
		grid.getView().setForceFit(true);

		return grid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initPanelButtons(Portlet portletMatches) {
		btnUpdate = new AppButton("Aktualisieren");
		btnUpdate.setToolTip("Aktualisiert die Ergebnisse der zuletzt gespieleten Spiele");
		btnUpdate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				getMatches();
			}
		});
		portletMatches.addButton(btnUpdate);
	}

	private void getMatches() {
		mask("Aktualisiere...");
		CursorDefs.showWaitCursor();
		btnUpdate.setEnabled(false);
		store.clear();
		KickerServices.MATCH_SERVICE.getAllMatches(new AsyncCallback<ArrayList<MatchDto>>() {
			@Override
			public void onSuccess(ArrayList<MatchDto> result) {
				store.addAll(result);
				btnUpdate.setEnabled(true);
				CursorDefs.showDefaultCursor();
				unmask();
			}

			@Override
			public void onFailure(Throwable caught) {
				btnUpdate.setEnabled(true);
				CursorDefs.showDefaultCursor();
				unmask();
			}
		});
	}

}
