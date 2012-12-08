package de.kickerapp.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.Portlet;
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
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.IntegerPropertyEditor;
import com.sencha.gxt.widget.core.client.form.Radio;

import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.util.CursorDefs;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppSpinnerField;
import de.kickerapp.client.widgets.AppTextField;
import de.kickerapp.shared.match.MatchData;

/**
 * Controller-Klasse zum Eintragen der Ergebnisse und Spieler eines Spiels.
 * 
 * @author Sebastian Filke
 */
public class ResultPanel extends BasePanel {

	/** Die Ergebnisse für den ersten Satz. */
	private AppSpinnerField<Integer> sfResultGame1Team1, sfResultGame1Team2;
	/** Die Ergebnisse für den zweiten Satz. */
	private AppSpinnerField<Integer> sfResultGame2Team1, sfResultGame2Team2;
	/** Die Ergebnisse für den dritten Satz. */
	private AppSpinnerField<Integer> sfResultGame3Team1, sfResultGame3Team2;
	/** Die Spieler des ersten Teams. */
	private AppTextField tfPlayer1Team1, tfPlayer2Team1;
	/** Die Spieler des zweiten Teams. */
	private AppTextField tfPlayer1Team2, tfPlayer2Team2;

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

		final VerticalLayoutContainer vlcMain = new VerticalLayoutContainer();

		final FieldSet fieldSetResult = createResultFieldSet();
		vlcMain.add(fieldSetResult, new VerticalLayoutData(1, -1, new Margins(0, 0, 5, 0)));

		final FieldSet fieldSetSetPlayers = createPlayersFieldSet();
		vlcMain.add(fieldSetSetPlayers, new VerticalLayoutData(1, -1));

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

		sfResultGame1Team1 = createResultComboBox("Satz 1");
		sfResultGame1Team2 = createResultComboBox("Satz 1");
		vlcResult.add(createFieldSetResultContainer(sfResultGame1Team1, sfResultGame1Team2), new VerticalLayoutData(1, -1));

		sfResultGame2Team1 = createResultComboBox("Satz 2");
		sfResultGame2Team2 = createResultComboBox("Satz 2");
		vlcResult.add(createResultContainer(sfResultGame2Team1, sfResultGame2Team2), new VerticalLayoutData(1, -1, new Margins(0, 0, 4, 0)));

		sfResultGame3Team1 = createResultComboBox("Satz 3");
		sfResultGame3Team2 = createResultComboBox("Satz 3");
		vlcResult.add(createResultContainer(sfResultGame3Team1, sfResultGame3Team2), new VerticalLayoutData(1, -1));

		fsResult.add(vlcResult);

		return fsResult;
	}

	/**
	 * Erzeugt einen horizontal ausgerichteten Container mit zwei ComboBoxen für
	 * das Ergebnis und einem Label.
	 * 
	 * @param cbResultTeam1 Die ComboBox für das erste Team.
	 * @param cbResultTeam2 Die ComboBox für das zweite Team.
	 * @return Der erzeugte Container.
	 */
	private HBoxLayoutContainer createFieldSetResultContainer(AppSpinnerField<Integer> cbResultTeam1, AppSpinnerField<Integer> cbResultTeam2) {
		HBoxLayoutContainer hblcResultInput = new HBoxLayoutContainer();
		hblcResultInput.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		hblcResultInput.setPack(BoxLayoutPack.CENTER);

		FieldLabel fieldLabel1 = new FieldLabel(cbResultTeam1, "Team 1");
		fieldLabel1.setLabelAlign(LabelAlign.TOP);

		FieldLabel fieldLabel2 = new FieldLabel(cbResultTeam2, "Team 2");
		fieldLabel2.setLabelAlign(LabelAlign.TOP);

		hblcResultInput.add(fieldLabel1, new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblcResultInput.add(new Label(":"), new BoxLayoutData(new Margins(15, 5, 0, 0)));
		hblcResultInput.add(fieldLabel2, new BoxLayoutData());

		return hblcResultInput;
	}

	/**
	 * Erzeugt einen horizontal ausgerichteten Container mit zwei ComboBoxen für
	 * das Ergebnis und einem Label.
	 * 
	 * @param cbResultTeam1 Die ComboBox für das erste Team.
	 * @param cbResultTeam2 Die ComboBox für das zweite Team.
	 * @return Der erzeugte Container.
	 */
	private HBoxLayoutContainer createResultContainer(AppSpinnerField<Integer> cbResultTeam1, AppSpinnerField<Integer> cbResultTeam2) {
		HBoxLayoutContainer hblcResultInput = new HBoxLayoutContainer();
		hblcResultInput.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		hblcResultInput.setPack(BoxLayoutPack.CENTER);

		hblcResultInput.add(cbResultTeam1, new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblcResultInput.add(new Label(":"), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblcResultInput.add(cbResultTeam2, new BoxLayoutData());

		return hblcResultInput;
	}

	/**
	 * Erzeugt eine ComboBox zur Eingabe des Ergebnisses.
	 * 
	 * @param emptyText Der Text, welcher angezeigt wird, wenn das Feld noch
	 *            leer ist als {@link String}.
	 * @return Die erzeugte ComboBox.
	 */
	private AppSpinnerField<Integer> createResultComboBox(String emptyText) {
		AppSpinnerField<Integer> spResult = new AppSpinnerField<Integer>(new IntegerPropertyEditor());
		spResult.setEmptyText(emptyText);
		spResult.setIncrement(1);
		spResult.setMinValue(0);
		spResult.setMaxValue(6);
		spResult.setAllowBlank(false);

		return spResult;
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

		vlcPlayers.add(createRadioButtons(), new VerticalLayoutData(-1, -1, new Margins(0, 0, 5, 0)));

		tfPlayer1Team1 = new AppTextField("Spieler 1");
		tfPlayer1Team2 = new AppTextField("Spieler 1");
		vlcPlayers.add(createFieldLabelPlayerContainer(tfPlayer1Team1, tfPlayer1Team2), new VerticalLayoutData(1, -1));

		tfPlayer2Team1 = new AppTextField("Spieler 2");
		tfPlayer2Team1.setEnabled(false);
		tfPlayer2Team2 = new AppTextField("Spieler 2");
		tfPlayer2Team2.setEnabled(false);
		vlcPlayers.add(createPlayerContainer(tfPlayer2Team1, tfPlayer2Team2), new VerticalLayoutData(1, -1));

		fsPlayers.add(vlcPlayers);

		return fsPlayers;
	}

	private HorizontalPanel createRadioButtons() {
		Radio radio = new Radio();
		radio.setBoxLabel("Einzel");
		radio.setId("single");
		radio.setValue(true);

		Radio radio2 = new Radio();
		radio2.setBoxLabel("Doppel");
		radio2.setId("double");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(radio);
		horizontalPanel.add(radio2);

		ToggleGroup toggle = new ToggleGroup();
		toggle.add(radio);
		toggle.add(radio2);
		toggle.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>() {
			@Override
			public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
				ToggleGroup group = (ToggleGroup) event.getSource();
				Radio radio = (Radio) group.getValue();

				tfPlayer2Team1.setValue(null);
				tfPlayer2Team1.setEnabled(false);
				tfPlayer2Team2.setValue(null);
				tfPlayer2Team2.setEnabled(false);
				if (radio.getId().equals("double")) {
					tfPlayer2Team1.setEnabled(true);
					tfPlayer2Team2.setEnabled(true);
				}
			}
		});
		return horizontalPanel;
	}

	/**
	 * Erzeugt einen horizontal ausgerichteten Container mit zwei Textfeldern
	 * für die Spieler.
	 * 
	 * @param tfPlayer1 Das Textfeld für den ersten Spieler.
	 * @param tfPlayer2 Das Textfeld für den zweiten Spieler.
	 * @return Der erzeugte Container.
	 */
	private HBoxLayoutContainer createFieldLabelPlayerContainer(AppTextField tfPlayer1, AppTextField tfPlayer2) {
		HBoxLayoutContainer hblcResultInput = new HBoxLayoutContainer();
		hblcResultInput.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);

		BoxLayoutData flex = new BoxLayoutData(new Margins(0, 5, 0, 0));
		flex.setFlex(1);

		BoxLayoutData flex2 = new BoxLayoutData();
		flex2.setFlex(1);

		FieldLabel fieldLabel1 = new FieldLabel(tfPlayer1, "Team 1");
		fieldLabel1.setLabelAlign(LabelAlign.TOP);

		hblcResultInput.add(fieldLabel1, flex);

		FieldLabel fieldLabel2 = new FieldLabel(tfPlayer2, "Team 2");
		fieldLabel2.setLabelAlign(LabelAlign.TOP);

		hblcResultInput.add(fieldLabel2, flex2);

		return hblcResultInput;
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
	protected void initPanelButtons(Portlet portletResult) {
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
		portletResult.addButton(bReset);
		portletResult.addButton(bReport);
	}

	private void createMatch() {
		MatchData match = new MatchData();
		CursorDefs.showWaitCursor();
		KickerServices.MATCH_SERVICE.createMatch(match, new AsyncCallback<MatchData>() {
			@Override
			public void onSuccess(MatchData result) {
				CursorDefs.showDefaultCursor();
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

}
