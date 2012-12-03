package de.kickerapp.client.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;

import de.kickerapp.client.model.properties.ResultProperty;
import de.kickerapp.client.services.ServiceProvider;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppComboBox;
import de.kickerapp.client.widgets.AppTextField;
import de.kickerapp.shared.match.Match;
import de.kickerapp.shared.match.Result;
import de.kickerapp.shared.match.ResultData;

/**
 * Controller-Klasse zum Eintragen der Ergebnisse und Spieler eines Spiels.
 * 
 * @author Sebastian Filke
 */
public class ResultPanel extends BasePanel {

	/** Die Ergebnisse für den ersten Satz. */
	private AppComboBox<ResultData> cbResultGame1Team1, cbResultGame1Team2;
	/** Die Ergebnisse für Satz 2. */
	private AppComboBox<ResultData> cbResultGame2Team1, cbResultGame2Team2;
	/** Die Ergebnisse für Satz 3. */
	private AppComboBox<ResultData> cbResultGame3Team1, cbResultGame3Team2;
	/** Die Spieler des ersten Teams. */
	private AppTextField tfPlayer1, tfPlayer2;
	/** Die Spieler des zweiten Teams. */
	private AppTextField tfPlayer3, tfPlayer4;

	/**
	 * Erzeugt einen neuen Controller zum Eintragen der Ergebnisse und Spieler
	 * eines Spiels.
	 */
	public ResultPanel() {
		super();
	}

	@Override
	public void initLayout() {
		super.initLayout();
		VerticalLayoutContainer vlcMain = new VerticalLayoutContainer();

		final FieldSet fieldSetResult = createResultFieldSet();
		vlcMain.add(fieldSetResult, new VerticalLayoutData(1, -1, new Margins(0, 0, 5, 0)));

		final FieldSet fieldSetSetPlayers = createPlayersFieldSet();
		vlcMain.add(fieldSetSetPlayers, new VerticalLayoutData(1, -1));

		initPanelButtons();

		add(vlcMain, new MarginData(10));
	}

	/**
	 * Erzeugt das Fieldset mit den ComboBoxen zum Eintragen des Ergebnisses.
	 * 
	 * @return Das erzeugte FieldSet.
	 */
	private FieldSet createResultFieldSet() {
		FieldSet fsResult = new FieldSet();
		fsResult.setHeadingText("Ergebnis");

		VerticalLayoutContainer vlcResult = new VerticalLayoutContainer();

		cbResultGame1Team1 = createResultComboBox();
		cbResultGame1Team2 = createResultComboBox();
		vlcResult.add(createResultContainer(cbResultGame1Team1, cbResultGame1Team2, "Satz 1"), new VerticalLayoutData(1, -1, new Margins(0, 0, 5, 0)));

		cbResultGame2Team1 = createResultComboBox();
		cbResultGame2Team2 = createResultComboBox();
		vlcResult.add(createResultContainer(cbResultGame2Team1, cbResultGame2Team2, "Satz 2"), new VerticalLayoutData(1, -1, new Margins(0, 0, 5, 0)));

		cbResultGame3Team1 = createResultComboBox();
		cbResultGame3Team2 = createResultComboBox();
		vlcResult.add(createResultContainer(cbResultGame3Team1, cbResultGame3Team2, "Satz 3"), new VerticalLayoutData(1, -1));

		fsResult.add(vlcResult);

		return fsResult;
	}

	/**
	 * Erzeugt einen horizontal ausgerichteten Container mit zwei ComboBoxen für
	 * das Ergebnis und einem Label.
	 * 
	 * @param cbResultTeam1 Die ComboBox für das erste Team.
	 * @param cbResultTeam2 Die ComboBox für das zweite Team.
	 * @param label Das Label zum Anzeigen des Satzes.
	 * @return Der erzeugte Container.
	 */
	private HBoxLayoutContainer createResultContainer(AppComboBox<ResultData> cbResultTeam1, AppComboBox<ResultData> cbResultTeam2, String label) {
		HBoxLayoutContainer hblcResultInput = new HBoxLayoutContainer();
		hblcResultInput.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		hblcResultInput.setPack(BoxLayoutPack.CENTER);

		hblcResultInput.add(new FieldLabel(cbResultTeam1, label), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblcResultInput.add(new Label(":"), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblcResultInput.add(cbResultTeam2, new BoxLayoutData());

		return hblcResultInput;
	}

	/**
	 * Erzeugt eine ComboBox zur Eingabe des Ergebnisses.
	 * 
	 * @return Die erzeugte ComboBox.
	 */
	private AppComboBox<ResultData> createResultComboBox() {
		ResultProperty resultProperty = GWT.create(ResultProperty.class);

		ListStore<ResultData> storeResult = new ListStore<ResultData>(resultProperty.id());
		storeResult.addAll(createResultData());

		AppComboBox<ResultData> cbResult = new AppComboBox<ResultData>(storeResult, resultProperty.nameLabel());
		cbResult.setTriggerAction(TriggerAction.ALL);
		cbResult.setAllowTextSelection(true);
		cbResult.setForceSelection(true);
		cbResult.setAllowBlank(false);
		cbResult.setTypeAhead(false);
		cbResult.setWidth(80);

		return cbResult;
	}

	/**
	 * Erzeugt das Fieldset mit vier Textfeldern zum Eintragen der Spieler.
	 * 
	 * @return Das erzeugte Fieldset.
	 */
	private FieldSet createPlayersFieldSet() {
		FieldSet fsPlayers = new FieldSet();
		fsPlayers.setHeadingText("Spieler");

		VerticalLayoutContainer vlcPlayers = new VerticalLayoutContainer();

		tfPlayer1 = new AppTextField("Spieler 1");
		tfPlayer2 = new AppTextField("Spieler 2");
		vlcPlayers.add(createPlayerContainer(tfPlayer1, tfPlayer2), new VerticalLayoutData(1, -1, new Margins(0, 0, 5, 0)));

		tfPlayer3 = new AppTextField("Spieler 3");
		tfPlayer4 = new AppTextField("Spieler 4");
		vlcPlayers.add(createPlayerContainer(tfPlayer3, tfPlayer4), new VerticalLayoutData(1, -1));

		fsPlayers.add(vlcPlayers);

		return fsPlayers;
	}

	/**
	 * Erzeugt einen horizontal ausgerichteten Container mit zwei Textfeldern
	 * für die Spieler.
	 * 
	 * @param tfPlayer1 Das Textfeld für den ersten Spieler.
	 * @param tfPlayer2 Das Textfeld für den zweiten Spieler.
	 * @return Der erzeugte Container.
	 */
	private HBoxLayoutContainer createPlayerContainer(AppTextField tfPlayer1, AppTextField tfPlayer2) {
		HBoxLayoutContainer hblcResultInput = new HBoxLayoutContainer();
		hblcResultInput.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);

		BoxLayoutData flex = new BoxLayoutData(new Margins(0, 5, 0, 0));
		flex.setFlex(1);

		BoxLayoutData flex2 = new BoxLayoutData();
		flex2.setFlex(1);

		hblcResultInput.add(tfPlayer1, flex);
		hblcResultInput.add(tfPlayer2, flex2);

		return hblcResultInput;
	}

	/**
	 * 
	 */
	private void initPanelButtons() {
		AppButton bReset = new AppButton("Eingaben zurücksetzen");
		bReset.setToolTip("Setzt alle eingegebenen Daten zurück");
		bReset.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
			}
		});
		AppButton bReport = new AppButton("Ergebnis eintragen");
		bReport.setToolTip("Speichert das Ergebnis und trägt es in die Liste ein");
		bReport.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				createMatch();
			}
		});
		addButton(bReset);
		addButton(bReport);
	}

	private void createMatch() {
		Match match = new Match();
		match.setSet1(cbResultGame1Team1.getValue().getName().toString() + ":" + cbResultGame1Team2.getValue().getName().toString());
		match.setPlayer1(tfPlayer1.getValue());
		match.setPlayer2(tfPlayer2.getValue());
		ServiceProvider.get().createMatch(match, new AsyncCallback<Match>() {
			@Override
			public void onSuccess(Match result) {

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	private ArrayList<ResultData> createResultData() {
		ArrayList<ResultData> resultList = new ArrayList<ResultData>();

		for (int i = 1; i <= 6; i++) {
			Result result = new Result();
			result.setId(i);
			result.setName(Integer.toString(i));
			resultList.add(result);
		}
		return resultList;
	}

}
