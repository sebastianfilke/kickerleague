package de.kickerapp.client.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
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
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;

import de.kickerapp.client.ui.model.ResultData;
import de.kickerapp.client.ui.model.data.Result;
import de.kickerapp.client.ui.model.properties.ResultProperty;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppComboBox;
import de.kickerapp.client.widgets.AppTextField;

public class ResultPanel extends BasePanel {

	/** Die Ergebnisse für Spiel 1. */
	private AppComboBox<ResultData> cbResultGame1Team1, cbResultGame1Team2;
	/** Die Ergebnisse für Spiel 2. */
	private AppComboBox<ResultData> cbResultGame2Team1, cbResultGame2Team2;
	/** Die Ergebnisse für Spiel 3. */
	private AppComboBox<ResultData> cbResultGame3Team1, cbResultGame3Team2;

	public ResultPanel() {
		super();
		initLayout();
	}

	@Override
	public void initLayout() {
		super.initLayout();
		VerticalLayoutContainer vlcMain = new VerticalLayoutContainer();

		final FieldSet fieldSetResult = createResultFieldSet();
		vlcMain.add(fieldSetResult, new VerticalLayoutData(1, -1, new Margins(0, 0, 5, 0)));

		final FieldSet fieldSetSetPlayers = createPlayerFieldSet();
		vlcMain.add(fieldSetSetPlayers, new VerticalLayoutData(1, -1));

		AppButton bReset = new AppButton("Eingaben zurücksetzen");
		bReset.setToolTip("Setzt alle Eingegebenen Daten zurück");
		AppButton bReport = new AppButton("Ergebnis eintragen");
		bReport.setToolTip("Speichert das Eregebnis und trägt es in die Liste ein");

		add(vlcMain, new MarginData(10));
		addButton(bReset);
		addButton(bReport);
	}

	private FieldSet createPlayerFieldSet() {
		FieldSet fsPlazers = new FieldSet();
		fsPlazers.setHeadingText("Spieler");

		VerticalLayoutContainer clcPlayers = new VerticalLayoutContainer();

		AppTextField tfPlayer1 = new AppTextField("Spieler 1");
		AppTextField tfPlayer2 = new AppTextField("Spieler 2");

		HBoxLayoutContainer hblcResultInputGame1 = createPlayerContainer(tfPlayer1, tfPlayer2);
		clcPlayers.add(hblcResultInputGame1, new VerticalLayoutData(1, -1, new Margins(0, 0, 5, 0)));

		AppTextField tfPlayer3 = new AppTextField("Spieler 3");
		AppTextField tfPlayer4 = new AppTextField("Spieler 4");

		HBoxLayoutContainer hblcResultInputGame2 = createPlayerContainer(tfPlayer3, tfPlayer4);
		clcPlayers.add(hblcResultInputGame2, new VerticalLayoutData(1, -1));

		fsPlazers.add(clcPlayers);

		return fsPlazers;
	}

	private HBoxLayoutContainer createPlayerContainer(AppTextField tfPlayer1, AppTextField tfPlayer2) {
		HBoxLayoutContainer hblcResultInput = new HBoxLayoutContainer();
		hblcResultInput.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		hblcResultInput.setPack(BoxLayoutPack.CENTER);

		hblcResultInput.add(tfPlayer1, new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblcResultInput.add(tfPlayer2, new BoxLayoutData());

		return hblcResultInput;
	}

	private FieldSet createResultFieldSet() {
		FieldSet fsResult = new FieldSet();
		fsResult.setHeadingText("Ergebnis");

		VerticalLayoutContainer clcResult = new VerticalLayoutContainer();

		cbResultGame1Team1 = createResultComboBox();
		cbResultGame1Team2 = createResultComboBox();
		HBoxLayoutContainer hblcResultInputGame1 = createResultInputContainer(cbResultGame1Team1, cbResultGame1Team2, "Satz 1");
		clcResult.add(hblcResultInputGame1, new VerticalLayoutData(1, -1, new Margins(0, 0, 5, 0)));

		cbResultGame2Team1 = createResultComboBox();
		cbResultGame2Team2 = createResultComboBox();
		HBoxLayoutContainer hblcResultInputGame2 = createResultInputContainer(cbResultGame2Team1, cbResultGame2Team2, "Satz 2");
		clcResult.add(hblcResultInputGame2, new VerticalLayoutData(1, -1, new Margins(0, 0, 5, 0)));

		cbResultGame3Team1 = createResultComboBox();
		cbResultGame3Team2 = createResultComboBox();
		HBoxLayoutContainer hblcResultInputGame3 = createResultInputContainer(cbResultGame3Team1, cbResultGame3Team2, "Satz 3");
		clcResult.add(hblcResultInputGame3, new VerticalLayoutData(1, -1));

		fsResult.add(clcResult);

		return fsResult;
	}

	private HBoxLayoutContainer createResultInputContainer(AppComboBox<ResultData> cbResultTeam1, AppComboBox<ResultData> cbResultTeam2, String label) {
		HBoxLayoutContainer hblcResultInput = new HBoxLayoutContainer();
		hblcResultInput.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		hblcResultInput.setPack(BoxLayoutPack.CENTER);
		hblcResultInput.add(new FieldLabel(cbResultTeam1, label), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblcResultInput.add(new Label(":"), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblcResultInput.add(cbResultTeam2, new BoxLayoutData());

		return hblcResultInput;
	}

	private AppComboBox<ResultData> createResultComboBox() {
		ResultProperty resultProperty = GWT.create(ResultProperty.class);

		ListStore<ResultData> storeResult = new ListStore<ResultData>(resultProperty.id());
		storeResult.addAll(createResultData());

		AppComboBox<ResultData> cbResult = new AppComboBox<ResultData>(storeResult, resultProperty.label());
		cbResult.setTriggerAction(TriggerAction.ALL);
		cbResult.setAllowTextSelection(true);
		cbResult.setForceSelection(true);
		cbResult.setTypeAhead(false);
		cbResult.setWidth(80);

		return cbResult;
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
