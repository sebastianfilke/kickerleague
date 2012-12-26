package de.kickerapp.client.ui;

import java.util.Date;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ListView;
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

import de.kickerapp.client.model.PlayerProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.resources.KickerTemplates;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppComboBox;
import de.kickerapp.client.widgets.AppSpinnerField;
import de.kickerapp.shared.match.MatchDto;
import de.kickerapp.shared.match.PlayerDto;
import de.kickerapp.shared.match.TeamDto;

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
	private AppComboBox<PlayerDto> cbTeam1Player1, cbTeam1Player2;
	/** Die Spieler des zweiten Teams. */
	private AppComboBox<PlayerDto> cbTeam2Player1, cbTeam2Player2;

	private ToggleGroup tgPlayType;

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
		final FieldSet fsResult = new FieldSet();
		fsResult.setHeadingText("Ergebnis");

		final VerticalLayoutContainer vlcResult = new VerticalLayoutContainer();

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
		final HBoxLayoutContainer hblcResultInput = new HBoxLayoutContainer();
		hblcResultInput.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		hblcResultInput.setPack(BoxLayoutPack.CENTER);

		final FieldLabel fieldLabel1 = new FieldLabel(cbResultTeam1, "Team 1");
		fieldLabel1.setLabelAlign(LabelAlign.TOP);

		final FieldLabel fieldLabel2 = new FieldLabel(cbResultTeam2, "Team 2");
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
		final HBoxLayoutContainer hblcResultInput = new HBoxLayoutContainer();
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
		final AppSpinnerField<Integer> spResult = new AppSpinnerField<Integer>(new IntegerPropertyEditor());
		spResult.setEmptyText(emptyText);
		spResult.setAllowBlank(false);
		// spResult.setIncrement(1);
		spResult.setMinValue(0);
		spResult.setMaxValue(6);

		return spResult;
	}

	/**
	 * Erzeugt das Fieldset mit vier Textfeldern zum Eintragen der Spieler.
	 * 
	 * @return Das erzeugte Fieldset.
	 */
	private FieldSet createPlayersFieldSet() {
		final FieldSet fsPlayers = new FieldSet();
		fsPlayers.setHeadingText("Spieler");

		final PlayerProperty props = GWT.create(PlayerProperty.class);

		final VerticalLayoutContainer vlcPlayers = new VerticalLayoutContainer();

		vlcPlayers.add(createRadioButtons(), new VerticalLayoutData(-1, -1, new Margins(0, 0, 5, 0)));

		cbTeam1Player1 = createPlayerComboBox(props, "Nach Spieler 1 suchen");
		cbTeam1Player2 = createPlayerComboBox(props, "Nach Spieler 2 suchen");
		cbTeam1Player2.setEnabled(false);

		final FieldLabel fieldLabel1 = new FieldLabel(cbTeam1Player1, "Team 1");
		fieldLabel1.setLabelAlign(LabelAlign.TOP);
		vlcPlayers.add(fieldLabel1, new VerticalLayoutData(1, -1));
		vlcPlayers.add(cbTeam1Player2, new VerticalLayoutData(1, -1, new Margins(0, 0, 8, 0)));

		cbTeam2Player1 = createPlayerComboBox(props, "Nach Spieler 1 suchen");
		cbTeam2Player2 = createPlayerComboBox(props, "Nach Spieler 2 suchen");
		cbTeam2Player2.setEnabled(false);

		final FieldLabel fieldLabel2 = new FieldLabel(cbTeam2Player1, "Team 2");
		fieldLabel2.setLabelAlign(LabelAlign.TOP);
		vlcPlayers.add(fieldLabel2, new VerticalLayoutData(1, -1));
		vlcPlayers.add(cbTeam2Player2, new VerticalLayoutData(1, -1));

		fsPlayers.add(vlcPlayers);

		return fsPlayers;
	}

	private HorizontalPanel createRadioButtons() {
		final Radio rSingle = new Radio();
		rSingle.setToolTip("Stellt den Spieltyp auf ein Einzelspiel (1vs1)");
		rSingle.setBoxLabel("Einzel");
		rSingle.setId("single");

		final Radio rDouble = new Radio();
		rDouble.setToolTip("Stellt den Spieltyp auf ein Doppelspiel (2vs2)");
		rDouble.setBoxLabel("Doppel");
		rDouble.setId("double");

		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(rSingle);
		horizontalPanel.add(rDouble);

		tgPlayType = new ToggleGroup();
		tgPlayType.add(rSingle);
		tgPlayType.add(rDouble);
		tgPlayType.setValue(rSingle);
		tgPlayType.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>() {
			@Override
			public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
				final ToggleGroup group = (ToggleGroup) event.getSource();
				final Radio radio = (Radio) group.getValue();

				cbTeam1Player2.setValue(null);
				cbTeam1Player2.setEnabled(false);
				cbTeam2Player2.setValue(null);
				cbTeam2Player2.setEnabled(false);
				if (radio.getId().equals("double")) {
					cbTeam1Player2.setEnabled(true);
					cbTeam2Player2.setEnabled(true);
				}
			}
		});
		return horizontalPanel;
	}

	private AppComboBox<PlayerDto> createPlayerComboBox(PlayerProperty props, String emptyText) {
		final ListStore<PlayerDto> store = new ListStore<PlayerDto>(props.id());

		final ListView<PlayerDto, PlayerDto> view = new ListView<PlayerDto, PlayerDto>(store, new IdentityValueProvider<PlayerDto>());
		view.setCell(new AbstractCell<PlayerDto>() {
			@Override
			public void render(Context context, PlayerDto value, SafeHtmlBuilder sb) {
				sb.append(KickerTemplates.TEMPLATE.renderPlayerPagingComboBox(value));
			}
		});

		final ComboBoxCell<PlayerDto> cell = new ComboBoxCell<PlayerDto>(store, props.label(), view);
		final AppComboBox<PlayerDto> cbPlayer = new AppComboBox<PlayerDto>(cell, emptyText);

		final RpcProxy<PagingLoadConfig, PagingLoadResult<PlayerDto>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<PlayerDto>>() {
			@Override
			public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<PlayerDto>> callback) {
				KickerServices.PAGING_SERVICE.getPagedPlayers(cbPlayer.getText(), loadConfig, callback);
			}
		};

		final PagingLoader<PagingLoadConfig, PagingLoadResult<PlayerDto>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<PlayerDto>>(proxy);
		loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, PlayerDto, PagingLoadResult<PlayerDto>>(store));

		cbPlayer.setHideTrigger(true);
		cbPlayer.setLoader(loader);
		cbPlayer.setPageSize(5);
		cbPlayer.setMinChars(2);

		return cbPlayer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initPanelButtons(Portlet portletResult) {
		final AppButton bReset = new AppButton("Eingaben zurücksetzen");
		bReset.setToolTip("Setzt alle eingegebenen Daten zurück");
		bReset.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
			}
		});
		final AppButton bReport = new AppButton("Ergebnis eintragen");
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
		final MatchDto newMatch = new MatchDto();
		newMatch.setMatchDate(new Date());
		newMatch.setMatchNumber("1");

		TeamDto team1 = null;
		TeamDto team2 = null;
		final Radio radio = (Radio) tgPlayType.getValue();
		if (radio.getId().equals("single")) {
			team1 = new TeamDto(cbTeam1Player1.getValue());
			team2 = new TeamDto(cbTeam2Player1.getValue());
		} else {
			team1 = new TeamDto(cbTeam1Player1.getValue(), cbTeam1Player2.getValue());
			team2 = new TeamDto(cbTeam2Player1.getValue(), cbTeam2Player2.getValue());
		}
		newMatch.setTeam1(team1);
		newMatch.setTeam2(team2);

		KickerServices.MATCH_SERVICE.createMatch(newMatch, new AsyncCallback<MatchDto>() {
			@Override
			public void onSuccess(MatchDto result) {

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}

}
