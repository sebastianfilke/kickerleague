package de.kickerapp.client.ui;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.core.client.util.DateWrapper.Unit;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.TimeField;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.ShowDataEvent;
import de.kickerapp.client.event.ShowDataEventHandler;
import de.kickerapp.client.event.TabPanelEvent;
import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.exception.AppExceptionHandler;
import de.kickerapp.client.properties.PlayerProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.ui.resources.KickerTemplates;
import de.kickerapp.client.ui.util.AppInfo;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppComboBox;
import de.kickerapp.client.widgets.AppContentPanel;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.MatchDto;
import de.kickerapp.shared.dto.MatchSetDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;

/**
 * Controller-Klasse zum Eintragen der Ergebnisse und Spieler eines Spiels.
 * 
 * @author Sebastian Filke
 */
public class InsertPanel extends BasePanel implements ShowDataEventHandler {

	/** Die Ergebnisse für den ersten Satz. */
	private AppComboBox<Integer> cbSet1Team1, cbSet1Team2;
	/** Die Ergebnisse für den zweiten Satz. */
	private AppComboBox<Integer> cbSet2Team1, cbSet2Team2;
	/** Die Ergebnisse für den dritten Satz. */
	private AppComboBox<Integer> cbSet3Team1, cbSet3Team2;
	/** Die Spieler des ersten Teams. */
	private AppComboBox<PlayerDto> cbTeam1Player1, cbTeam1Player2;
	/** Die Spieler des zweiten Teams. */
	private AppComboBox<PlayerDto> cbTeam2Player1, cbTeam2Player2;

	private ToggleGroup tgPlayType;

	private CheckBox cbCurrentTime;

	private DateField dfMatchDate;

	private TimeField tfMatchTime;

	private Label resultLabelTeam1;

	private Label resultLabelTeam2;

	/**
	 * Erzeugt einen neuen Controller zum Eintragen der Ergebnisse und Spieler eines Spiels.
	 */
	public InsertPanel() {
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
		setHeadingHtml("<span id='panelHeading'>Spiel eintragen</span>");

		final VerticalLayoutContainer vlcMain = new VerticalLayoutContainer();
		vlcMain.getElement().getStyle().setBackgroundColor("#F1F1F1");

		final ToolBar toolBar = createToolBar();
		vlcMain.add(toolBar, new VerticalLayoutData(1, -1));

		final FieldSet fieldSetDateTime = createDateTimeFieldSet();
		vlcMain.add(fieldSetDateTime, new VerticalLayoutData(1, -1, new Margins(10, 10, 5, 10)));

		final FieldSet fieldSetResult = createResultFieldSet();
		vlcMain.add(fieldSetResult, new VerticalLayoutData(1, -1, new Margins(0, 10, 5, 10)));

		final FieldSet fieldSetSetPlayers = createPlayersFieldSet();
		vlcMain.add(fieldSetSetPlayers, new VerticalLayoutData(1, -1, new Margins(0, 10, 10, 10)));

		add(vlcMain);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();

		AppEventBus.addHandler(ShowDataEvent.INSERT, this);
	}

	private ToolBar createToolBar() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		toolBar.add(createBtnInsert());
		toolBar.add(createBtnReset());
		return toolBar;
	}

	/**
	 * @return Das erzeugte FieldSet.
	 */
	private FieldSet createDateTimeFieldSet() {
		final FieldSet fsDateTime = new FieldSet();
		fsDateTime.setHeadingText("Datum/Uhrzeit");

		final VerticalLayoutContainer vlcDateTime = new VerticalLayoutContainer();

		vlcDateTime.add(createCheckBox(), new VerticalLayoutData(-1, -1));
		vlcDateTime.add(createFieldSetDateTime(), new VerticalLayoutData(-1, -1));

		fsDateTime.add(vlcDateTime);

		return fsDateTime;
	}

	private CheckBox createCheckBox() {
		cbCurrentTime = new CheckBox();
		cbCurrentTime.setBoxLabel("Aktuelle Uhrzeit");
		cbCurrentTime.setToolTip("Deaktivieren um die Zeit manuell einstellen");
		cbCurrentTime.setValue(true);
		cbCurrentTime.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				final CheckBox check = (CheckBox) event.getSource();

				dfMatchDate.setValue(new Date());
				dfMatchDate.clearInvalid();
				dfMatchDate.setEnabled(false);
				tfMatchTime.setValue(new Date());
				tfMatchTime.clearInvalid();
				tfMatchTime.setEnabled(false);
				if (!check.getValue()) {
					dfMatchDate.setEnabled(true);
					tfMatchTime.setEnabled(true);
				}
			}
		});

		return cbCurrentTime;
	}

	private HBoxLayoutContainer createFieldSetDateTime() {
		final HBoxLayoutContainer hblcResultInput = new HBoxLayoutContainer();
		hblcResultInput.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		hblcResultInput.setPack(BoxLayoutPack.CENTER);

		dfMatchDate = new DateField();
		dfMatchDate.setEnabled(false);

		final FieldLabel fieldLabel1 = new FieldLabel(dfMatchDate, "Datum");
		fieldLabel1.setLabelAlign(LabelAlign.TOP);

		tfMatchTime = new TimeField();
		tfMatchTime.setTriggerAction(TriggerAction.ALL);
		tfMatchTime.setEnabled(false);
		tfMatchTime.setIncrement(1);

		final FieldLabel fieldLabel2 = new FieldLabel(tfMatchTime, "Zeit");
		fieldLabel2.setLabelAlign(LabelAlign.TOP);

		hblcResultInput.add(fieldLabel1, new BoxLayoutData(new Margins(0, 13, 0, 0)));
		hblcResultInput.add(fieldLabel2, new BoxLayoutData());

		return hblcResultInput;
	}

	/**
	 * Erzeugt das Fieldset mit den ComboBoxen zum Eintragen des Ergebnisses.
	 * 
	 * @return Das erzeugte FieldSet.
	 */
	private FieldSet createResultFieldSet() {
		final FieldSet fsResult = new FieldSet();
		fsResult.setHeadingText("Ergebnis");

		final AppContentPanel panel = new AppContentPanel();
		panel.setHeaderVisible(false);
		panel.setBodyBorder(false);
		panel.setBorders(false);
		panel.setHeight(98);

		final HorizontalLayoutContainer hlcResult = new HorizontalLayoutContainer();

		resultLabelTeam1 = new Label("0");
		resultLabelTeam1.setStyleName("resultLabel resultLabelRight", true);
		final VBoxLayoutContainer vblcResultTeam1 = createLabelResultContainer(resultLabelTeam1);

		resultLabelTeam2 = new Label("0");
		resultLabelTeam2.setStyleName("resultLabel resultLabelLeft", true);
		final VBoxLayoutContainer vblcResultTeam2 = createLabelResultContainer(resultLabelTeam2);

		hlcResult.add(vblcResultTeam1, new HorizontalLayoutData(360, 1));
		hlcResult.add(createResultInsertContainer(), new HorizontalLayoutData(1, 1));
		hlcResult.add(vblcResultTeam2, new HorizontalLayoutData(360, 1));

		panel.add(hlcResult);

		fsResult.add(panel);

		return fsResult;
	}

	private VBoxLayoutContainer createLabelResultContainer(Label labelTeam) {
		final VBoxLayoutContainer vblcResult = new VBoxLayoutContainer();
		vblcResult.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);

		final BoxLayoutData flex = new BoxLayoutData();
		flex.setFlex(1);

		vblcResult.add(labelTeam, flex);

		return vblcResult;
	}

	private VerticalLayoutContainer createResultInsertContainer() {
		final VerticalLayoutContainer vlcResult = new VerticalLayoutContainer();
		vlcResult.getElement().getStyle().setBackgroundColor("#F1F1F1");

		cbSet1Team1 = createSetComboBox("Satz 1");
		cbSet1Team2 = createSetComboBox("Satz 1");
		vlcResult.add(createFieldSetResultContainer(cbSet1Team1, cbSet1Team2), new VerticalLayoutData(1, -1));

		cbSet2Team1 = createSetComboBox("Satz 2");
		cbSet2Team2 = createSetComboBox("Satz 2");
		vlcResult.add(createResultContainer(cbSet2Team1, cbSet2Team2), new VerticalLayoutData(1, -1, new Margins(0, 0, 4, 0)));

		cbSet3Team1 = createSetComboBox("Satz 3");
		cbSet3Team2 = createSetComboBox("Satz 3");
		vlcResult.add(createResultContainer(cbSet3Team1, cbSet3Team2), new VerticalLayoutData(1, -1));

		addValueChangedHandler();
		addSelectionHandler();

		return vlcResult;
	}

	private void addValueChangedHandler() {
		cbSet1Team1.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateLabelForTeam1(event.getValue(), cbSet2Team1.getValue(), cbSet3Team1.getValue());
				updateRelatedSet(event.getValue(), cbSet1Team2);
				enableStatusForLastSet(event.getValue(), cbSet2Team1.getValue(), cbSet1Team2.getValue(), cbSet2Team2.getValue());
			}
		});
		cbSet1Team2.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateLabelForTeam2(event.getValue(), cbSet2Team2.getValue(), cbSet3Team2.getValue());
				updateRelatedSet(event.getValue(), cbSet1Team1);
				enableStatusForLastSet(cbSet1Team1.getValue(), cbSet2Team1.getValue(), event.getValue(), cbSet2Team2.getValue());
			}
		});
		cbSet2Team1.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateLabelForTeam1(cbSet1Team1.getValue(), event.getValue(), cbSet3Team1.getValue());
				updateRelatedSet(event.getValue(), cbSet2Team2);
				enableStatusForLastSet(cbSet1Team1.getValue(), event.getValue(), cbSet1Team2.getValue(), cbSet2Team2.getValue());
			}
		});
		cbSet2Team2.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateLabelForTeam2(cbSet1Team2.getValue(), event.getValue(), cbSet3Team2.getValue());
				updateRelatedSet(event.getValue(), cbSet2Team1);
				enableStatusForLastSet(cbSet1Team1.getValue(), cbSet2Team1.getValue(), cbSet1Team2.getValue(), event.getValue());
			}
		});
		cbSet3Team1.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateLabelForTeam1(cbSet1Team1.getValue(), cbSet2Team1.getValue(), event.getValue());
				updateRelatedSet(event.getValue(), cbSet3Team2);
			}
		});
		cbSet3Team2.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateLabelForTeam2(cbSet1Team2.getValue(), cbSet2Team2.getValue(), event.getValue());
				updateRelatedSet(event.getValue(), cbSet3Team1);
			}
		});
	}

	private void addSelectionHandler() {
		cbSet1Team1.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateLabelForTeam1(event.getSelectedItem(), cbSet2Team1.getValue(), cbSet3Team1.getValue());
				updateRelatedSet(event.getSelectedItem(), cbSet1Team2);
				enableStatusForLastSet(event.getSelectedItem(), cbSet2Team1.getValue(), cbSet1Team2.getValue(), cbSet2Team2.getValue());
			}
		});
		cbSet1Team2.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateLabelForTeam2(event.getSelectedItem(), cbSet2Team2.getValue(), cbSet3Team2.getValue());
				updateRelatedSet(event.getSelectedItem(), cbSet1Team1);
				enableStatusForLastSet(cbSet1Team1.getValue(), cbSet2Team1.getValue(), event.getSelectedItem(), cbSet2Team2.getValue());
			}
		});
		cbSet2Team1.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateLabelForTeam1(cbSet1Team1.getValue(), event.getSelectedItem(), cbSet3Team1.getValue());
				updateRelatedSet(event.getSelectedItem(), cbSet2Team2);
				enableStatusForLastSet(cbSet1Team1.getValue(), event.getSelectedItem(), cbSet1Team2.getValue(), cbSet2Team2.getValue());
			}
		});
		cbSet2Team2.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateLabelForTeam2(cbSet1Team2.getValue(), event.getSelectedItem(), cbSet3Team2.getValue());
				updateRelatedSet(event.getSelectedItem(), cbSet2Team1);
				enableStatusForLastSet(cbSet1Team1.getValue(), cbSet2Team1.getValue(), cbSet1Team2.getValue(), event.getSelectedItem());
			}
		});
		cbSet3Team1.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateLabelForTeam1(cbSet1Team1.getValue(), cbSet2Team1.getValue(), event.getSelectedItem());
				updateRelatedSet(event.getSelectedItem(), cbSet3Team2);
			}
		});
		cbSet3Team2.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateLabelForTeam2(cbSet1Team2.getValue(), cbSet2Team2.getValue(), event.getSelectedItem());
				updateRelatedSet(event.getSelectedItem(), cbSet3Team1);
			}
		});
	}

	protected void updateLabelForTeam1(Integer value, Integer value2, Integer value3) {
		final int result = getResult(value, value2, value3);
		resultLabelTeam1.setText(String.valueOf(result));
	}

	protected void updateLabelForTeam2(Integer value, Integer value2, Integer value3) {
		final int result = getResult(value, value2, value3);
		resultLabelTeam2.setText(String.valueOf(result));
	}

	private int getResult(Integer value, Integer value2, Integer value3) {
		int result = 0;

		if (value != null && value == 6) {
			result++;
		}
		if (value2 != null && value2 == 6) {
			result++;
		}
		if (value3 != null && value3 == 6) {
			result++;
		}
		return result;
	}

	private void enableStatusForLastSet(Integer cbSet1Team1, Integer cbSet2Team1, Integer cbSet1Team2, Integer cbSet2Team2) {
		if ((cbSet1Team1 != null && cbSet1Team1 == 6) && (cbSet2Team1 != null && cbSet2Team1 == 6)) {
			cbSet3Team1.clear();
			cbSet3Team2.clear();
			cbSet3Team1.setEnabled(false);
			cbSet3Team2.setEnabled(false);
		} else if ((cbSet1Team2 != null && cbSet1Team2 == 6) && (cbSet2Team2 != null && cbSet2Team2 == 6)) {
			cbSet3Team1.clear();
			cbSet3Team2.clear();
			cbSet3Team1.setEnabled(false);
			cbSet3Team2.setEnabled(false);
		} else {
			cbSet3Team1.setEnabled(true);
			cbSet3Team2.setEnabled(true);
		}
	}

	private void updateRelatedSet(Integer value, AppComboBox<Integer> cbSet) {
		if (value != null && value != 6) {
			if (cbSet.getValue() == null) {
				cbSet.setValue(6, true);
				cbSet.clearInvalid();
			}
		}
	}

	/**
	 * Erzeugt einen horizontal ausgerichteten Container mit zwei ComboBoxen für das Ergebnis und einem Label.
	 * 
	 * @param cbResultTeam1 Die ComboBox für das erste Team.
	 * @param cbResultTeam2 Die ComboBox für das zweite Team.
	 * @return Der erzeugte Container.
	 */
	private HBoxLayoutContainer createFieldSetResultContainer(AppComboBox<Integer> cbResultTeam1, AppComboBox<Integer> cbResultTeam2) {
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
	 * Erzeugt einen horizontal ausgerichteten Container mit zwei ComboBoxen für das Ergebnis und einem Label.
	 * 
	 * @param cbResultTeam1 Die ComboBox für das erste Team.
	 * @param cbResultTeam2 Die ComboBox für das zweite Team.
	 * @return Der erzeugte Container.
	 */
	private HBoxLayoutContainer createResultContainer(AppComboBox<Integer> cbResultTeam1, AppComboBox<Integer> cbResultTeam2) {
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
	 * @param emptyText Der Text, welcher angezeigt wird, wenn das Feld noch leer ist als {@link String}.
	 * @return Die erzeugte ComboBox.
	 */
	private AppComboBox<Integer> createSetComboBox(String emptyText) {
		final ListStore<Integer> store = new ListStore<Integer>(new ModelKeyProvider<Integer>() {
			@Override
			public String getKey(Integer item) {
				return Integer.toString(item);
			}
		});
		final ArrayList<Integer> matchSet = new ArrayList<Integer>();
		for (int i = 0; i <= 6; i++) {
			matchSet.add(i);
		}
		store.addAll(matchSet);

		final AppComboBox<Integer> cbSet = new AppComboBox<Integer>(store, new LabelProvider<Integer>() {
			@Override
			public String getLabel(Integer item) {
				return Integer.toString(item);
			}
		}, emptyText);
		cbSet.setEmptyText(emptyText);
		cbSet.setTriggerAction(TriggerAction.ALL);
		cbSet.setAllowBlank(false);

		return cbSet;
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

		final FieldLabel fieldLabel1 = new FieldLabel(cbTeam1Player1, "Team 1");
		fieldLabel1.setLabelAlign(LabelAlign.TOP);
		vlcPlayers.add(fieldLabel1, new VerticalLayoutData(1, -1));
		vlcPlayers.add(cbTeam1Player2, new VerticalLayoutData(1, -1, new Margins(0, 0, 8, 0)));

		cbTeam2Player1 = createPlayerComboBox(props, "Nach Spieler 1 suchen");
		cbTeam2Player2 = createPlayerComboBox(props, "Nach Spieler 2 suchen");

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
		rSingle.setId(MatchType.SINGLE.getMatchType());

		final Radio rDouble = new Radio();
		rDouble.setToolTip("Stellt den Spieltyp auf ein Doppelspiel (2vs2)");
		rDouble.setBoxLabel("Doppel");
		rDouble.setId(MatchType.DOUBLE.getMatchType());

		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(rSingle);
		horizontalPanel.add(rDouble);

		tgPlayType = new ToggleGroup();
		tgPlayType.add(rSingle);
		tgPlayType.add(rDouble);
		tgPlayType.setValue(rDouble);
		tgPlayType.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>() {
			@Override
			public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
				final ToggleGroup group = (ToggleGroup) event.getSource();
				final Radio radio = (Radio) group.getValue();

				cbTeam1Player2.setValue(null);
				cbTeam1Player2.setVisible(false);
				cbTeam2Player2.setValue(null);
				cbTeam2Player2.setVisible(false);
				if (radio.getId().equals(MatchType.DOUBLE.getMatchType())) {
					cbTeam1Player2.setVisible(true);
					cbTeam2Player2.setVisible(true);
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

		final ComboBoxCell<PlayerDto> cell = new ComboBoxCell<PlayerDto>(store, PlayerProperty.label, view);
		final AppComboBox<PlayerDto> cbPlayer = new AppComboBox<PlayerDto>(cell, emptyText);

		final RpcProxy<PagingLoadConfig, PagingLoadResult<PlayerDto>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<PlayerDto>>() {
			@Override
			public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<PlayerDto>> callback) {
				KickerServices.PAGING_SERVICE.getPagedPlayers(cbPlayer.getText(), loadConfig, callback);
			}
		};

		final PagingLoader<PagingLoadConfig, PagingLoadResult<PlayerDto>> loader = new PagingLoader<PagingLoadConfig, PagingLoadResult<PlayerDto>>(proxy);
		loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, PlayerDto, PagingLoadResult<PlayerDto>>(store));

		cbPlayer.setTriggerAction(TriggerAction.ALL);
		cbPlayer.setLoader(loader);
		cbPlayer.setPageSize(5);
		cbPlayer.setMinChars(1);

		return cbPlayer;
	}

	private AppButton createBtnInsert() {
		final AppButton bReport = new AppButton("Ergebnis eintragen", KickerIcons.ICON.tableSave());
		bReport.setToolTip("Speichert das Ergebnis und trägt es in die Liste ein");
		bReport.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				createMatch();
			}
		});
		return bReport;
	}

	private AppButton createBtnReset() {
		final AppButton bReset = new AppButton("Eingaben zurücksetzen", KickerIcons.ICON.table());
		bReset.setToolTip("Setzt alle eingegebenen Daten zurück");
		bReset.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				clearInput();
			}
		});
		return bReset;
	}

	private void createMatch() {
		if (inputValid()) {
			mask("Spiel wird eingetragen...");
			final MatchDto newMatch = makeMatch();
			KickerServices.MATCH_SERVICE.createMatch(newMatch, new AsyncCallback<MatchDto>() {
				@Override
				public void onSuccess(MatchDto result) {
					AppInfo.showInfo("Hinweis", "Spiel wurde erfolgreich eingetragen");

					int activeWidget = 0;
					if (result.getMatchType() == MatchType.DOUBLE) {
						activeWidget = 1;
					}
					final TabPanelEvent tabPanelEvent = new TabPanelEvent();
					tabPanelEvent.setActiveWidget(activeWidget);
					AppEventBus.fireEvent(tabPanelEvent);
					AppEventBus.fireEvent(new UpdatePanelEvent(UpdatePanelEvent.ALL));
					clearInput();
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					unmask();
					AppExceptionHandler.handleException(caught);
				}
			});
		} else {
			final MessageBox box = new MessageBox("Hinweis", "Es müssen erst alle notwendigen Angaben gemacht werden!");
			box.setPredefinedButtons(PredefinedButton.OK);
			box.setIcon(MessageBox.ICONS.info());
			box.show();
		}
	}

	private boolean inputValid() {
		boolean valid = false;
		valid = dfMatchDate.isValid() && tfMatchTime.isValid();
		if (valid) {
			valid = checkRelatedSet(cbSet1Team1, cbSet1Team2);
			if (valid) {
				valid = checkRelatedSet(cbSet2Team1, cbSet2Team2);
				if (valid) {
					valid = checkRelatedSet(cbSet3Team1, cbSet3Team2);
					if (valid) {
						final Radio radio = (Radio) tgPlayType.getValue();
						if (radio.getId().equals(MatchType.SINGLE.getMatchType())) {
							valid = cbTeam1Player1.getValue() != null && cbTeam2Player1.getValue() != null
									&& cbTeam1Player1.getValue() != cbTeam2Player1.getValue();
						} else {
							valid = cbTeam1Player1.getValue() != null && cbTeam1Player2.getValue() != null && cbTeam2Player1.getValue() != null
									&& cbTeam2Player2.getValue() != null;
						}
					}
				}
			}
		}
		return valid;
	}

	private boolean checkRelatedSet(AppComboBox<Integer> cb1Team1, AppComboBox<Integer> cb2Team2) {
		boolean valid = false;
		if (cb1Team1.getValue() != null && cb2Team2.getValue() != null) {
			if (cb1Team1.getValue() < 6 && cb2Team2.getValue() == 6) {
				valid = true;
			} else if (cb1Team1.getValue() == 6 && cb2Team2.getValue() < 6) {
				valid = true;
			}
		} else if (cb1Team1.isEnabled() == false && cb2Team2.isEnabled() == false) {
			valid = true;
		}
		return valid;
	}

	private void clearInput() {
		resultLabelTeam1.setText("0");
		resultLabelTeam2.setText("0");

		cbSet1Team1.clear();
		cbSet1Team2.clear();
		cbSet2Team1.clear();
		cbSet2Team2.clear();
		cbSet3Team1.clear();
		cbSet3Team2.clear();
		cbSet3Team1.setEnabled(true);
		cbSet3Team2.setEnabled(true);

		cbCurrentTime.setValue(true, true);

		cbTeam1Player1.clear();
		cbTeam1Player2.clear();
		cbTeam2Player1.clear();
		cbTeam2Player2.clear();
	}

	private MatchDto makeMatch() {
		final MatchDto newMatch = new MatchDto();
		if (cbCurrentTime.getValue()) {
			DateWrapper matchDate = new DateWrapper(new Date());

			final int hours = matchDate.getHours();
			final int minutes = matchDate.getMinutes();
			final int seconds = matchDate.getSeconds();

			matchDate = matchDate.clearTime();

			matchDate = matchDate.add(Unit.HOUR, hours);
			matchDate = matchDate.add(Unit.MINUTE, minutes);
			matchDate = matchDate.add(Unit.SECOND, seconds);

			newMatch.setMatchDate(matchDate.asDate());
		} else {
			DateWrapper matchDate = new DateWrapper(dfMatchDate.getValue()).clearTime();
			final DateWrapper currentDate = new DateWrapper(new Date());

			final int hours = new DateWrapper(tfMatchTime.getValue()).getHours();
			final int minutes = new DateWrapper(tfMatchTime.getValue()).getMinutes();
			final int seconds = currentDate.getSeconds();

			matchDate = matchDate.add(Unit.HOUR, hours);
			matchDate = matchDate.add(Unit.MINUTE, minutes);
			matchDate = matchDate.add(Unit.SECOND, seconds);

			newMatch.setMatchDate(matchDate.asDate());
		}

		TeamDto team1 = null;
		TeamDto team2 = null;
		final Radio radio = (Radio) tgPlayType.getValue();
		if (radio.getId().equals(MatchType.SINGLE.getMatchType())) {
			newMatch.setMatchType(MatchType.SINGLE);
			team1 = new TeamDto(cbTeam1Player1.getValue());
			team2 = new TeamDto(cbTeam2Player1.getValue());
		} else {
			newMatch.setMatchType(MatchType.DOUBLE);
			team1 = new TeamDto(cbTeam1Player1.getValue(), cbTeam1Player2.getValue());
			team2 = new TeamDto(cbTeam2Player1.getValue(), cbTeam2Player2.getValue());
		}
		newMatch.setTeam1Dto(team1);
		newMatch.setTeam2(team2);

		final MatchSetDto newSets = new MatchSetDto();
		newSets.getMatchSetsTeam1().add(cbSet1Team1.getValue());
		newSets.getMatchSetsTeam1().add(cbSet2Team1.getValue());
		if (cbSet3Team1.getValue() != null) {
			newSets.getMatchSetsTeam1().add(cbSet3Team1.getValue());
		}

		newSets.getMatchSetsTeam2().add(cbSet1Team2.getValue());
		newSets.getMatchSetsTeam2().add(cbSet2Team2.getValue());
		if (cbSet3Team2.getValue() != null) {
			newSets.getMatchSetsTeam2().add(cbSet3Team2.getValue());
		}
		newMatch.setMatchSetsDto(newSets);

		return newMatch;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showData(ShowDataEvent event) {
		if (cbCurrentTime.getValue()) {
			dfMatchDate.setValue(new Date());
			tfMatchTime.setValue(new Date());
		}
	}

}
