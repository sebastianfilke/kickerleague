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
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.TabPanelEvent;
import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.properties.PlayerProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.ui.resources.KickerTemplates;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppComboBox;
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
public class InsertPanel extends BasePanel {

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

	/**
	 * Erzeugt einen neuen Controller zum Eintragen der Ergebnisse und Spieler
	 * eines Spiels.
	 */
	public InsertPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		super.initLayout();
		setHeadingHtml("<span id='panelHeading'>Spiel eintragen</span>");

		final VerticalLayoutContainer vlcMain = new VerticalLayoutContainer();

		final ToolBar toolBar = createToolBar();
		vlcMain.add(toolBar, new VerticalLayoutData(1, -1));

		final FieldSet fieldSetResult = createResultFieldSet();
		vlcMain.add(fieldSetResult, new VerticalLayoutData(1, -1, new Margins(10, 10, 5, 10)));

		final FieldSet fieldSetSetPlayers = createPlayersFieldSet();
		vlcMain.add(fieldSetSetPlayers, new VerticalLayoutData(1, -1, new Margins(0, 10, 10, 10)));

		add(vlcMain);
	}

	private ToolBar createToolBar() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		toolBar.add(createBtnInsert());
		toolBar.add(createBtnReset());
		return toolBar;
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

		fsResult.add(vlcResult);

		return fsResult;
	}

	private void addValueChangedHandler() {
		cbSet1Team1.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateRelatedSet(event.getValue(), cbSet1Team2);
				enableStatusForLastSet(event.getValue(), cbSet2Team1.getValue(), cbSet1Team2.getValue(), cbSet2Team2.getValue());
			}
		});
		cbSet1Team2.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateRelatedSet(event.getValue(), cbSet1Team1);
				enableStatusForLastSet(cbSet1Team1.getValue(), cbSet2Team1.getValue(), event.getValue(), cbSet2Team2.getValue());
			}
		});
		cbSet2Team1.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateRelatedSet(event.getValue(), cbSet2Team2);
				enableStatusForLastSet(cbSet1Team1.getValue(), event.getValue(), cbSet1Team2.getValue(), cbSet2Team2.getValue());
			}
		});
		cbSet2Team2.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateRelatedSet(event.getValue(), cbSet2Team1);
				enableStatusForLastSet(cbSet1Team1.getValue(), cbSet2Team1.getValue(), cbSet1Team2.getValue(), event.getValue());
			}
		});
		cbSet3Team1.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateRelatedSet(event.getValue(), cbSet3Team2);
			}
		});
		cbSet3Team2.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				updateRelatedSet(event.getValue(), cbSet3Team1);
			}
		});
	}

	private void addSelectionHandler() {
		cbSet1Team1.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateRelatedSet(event.getSelectedItem(), cbSet1Team2);
				enableStatusForLastSet(event.getSelectedItem(), cbSet2Team1.getValue(), cbSet1Team2.getValue(), cbSet2Team2.getValue());
			}
		});
		cbSet1Team2.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateRelatedSet(event.getSelectedItem(), cbSet1Team1);
				enableStatusForLastSet(cbSet1Team1.getValue(), cbSet2Team1.getValue(), event.getSelectedItem(), cbSet2Team2.getValue());
			}
		});
		cbSet2Team1.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateRelatedSet(event.getSelectedItem(), cbSet2Team2);
				enableStatusForLastSet(cbSet1Team1.getValue(), event.getSelectedItem(), cbSet1Team2.getValue(), cbSet2Team2.getValue());
			}
		});
		cbSet2Team2.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateRelatedSet(event.getSelectedItem(), cbSet2Team1);
				enableStatusForLastSet(cbSet1Team1.getValue(), cbSet2Team1.getValue(), cbSet1Team2.getValue(), event.getSelectedItem());
			}
		});
		cbSet3Team1.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateRelatedSet(event.getSelectedItem(), cbSet3Team2);
			}
		});
		cbSet3Team2.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				updateRelatedSet(event.getSelectedItem(), cbSet3Team1);
			}
		});
	}

	protected void enableStatusForLastSet(Integer cbSet1Team1, Integer cbSet2Team1, Integer cbSet1Team2, Integer cbSet2Team2) {
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
				cbSet.setValue(6);
			}
		}
	}

	/**
	 * Erzeugt einen horizontal ausgerichteten Container mit zwei ComboBoxen für
	 * das Ergebnis und einem Label.
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
	 * Erzeugt einen horizontal ausgerichteten Container mit zwei ComboBoxen für
	 * das Ergebnis und einem Label.
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
	 * @param emptyText Der Text, welcher angezeigt wird, wenn das Feld noch
	 *            leer ist als {@link String}.
	 * @return Die erzeugte ComboBox.
	 */
	private AppComboBox<Integer> createSetComboBox(String emptyText) {
		final ListStore<Integer> store = new ListStore<Integer>(new ModelKeyProvider<Integer>() {
			@Override
			public String getKey(Integer item) {
				return Integer.toString(item);
			}
		});
		ArrayList<Integer> matchSet = new ArrayList<Integer>();
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
		rSingle.setId(MatchType.Single.getMatchType());

		final Radio rDouble = new Radio();
		rDouble.setToolTip("Stellt den Spieltyp auf ein Doppelspiel (2vs2)");
		rDouble.setBoxLabel("Doppel");
		rDouble.setId(MatchType.Double.getMatchType());

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
				if (radio.getId().equals(MatchType.Double.getMatchType())) {
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

		cbPlayer.setLoader(loader);
		cbPlayer.setPageSize(5);
		cbPlayer.setMinChars(2);

		return cbPlayer;
	}

	private AppButton createBtnInsert() {
		final AppButton bReport = new AppButton("Ergebnis eintragen", KickerIcons.ICON.table_save());
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
		mask("Spiel wird eingetragen...");
		final MatchDto newMatch = makeMatch();
		KickerServices.MATCH_SERVICE.createSingleMatch(newMatch, new AsyncCallback<MatchDto>() {
			@Override
			public void onSuccess(MatchDto result) {
				Info.display("Erfolgreich", "Spiel wurde erfolgreich eingetragen");
				clearInput();

				int activeWidget = 0;
				if (result.getMatchType() == MatchType.Double) {
					activeWidget = 1;
				}
				final TabPanelEvent tabPanelEvent = new TabPanelEvent();
				tabPanelEvent.setActiveWidget(activeWidget);
				AppEventBus.fireEvent(tabPanelEvent);
				AppEventBus.fireEvent(new UpdatePanelEvent(UpdatePanelEvent.ALL));

				unmask();
			}

			@Override
			public void onFailure(Throwable caught) {
				unmask();
			}
		});
	}

	private void clearInput() {
		cbSet1Team1.clear();
		cbSet1Team2.clear();
		cbSet2Team1.clear();
		cbSet2Team2.clear();
		cbSet3Team1.clear();
		cbSet3Team2.clear();
		cbSet3Team1.setEnabled(true);
		cbSet3Team2.setEnabled(true);

		cbTeam1Player1.clear();
		cbTeam1Player2.clear();
		cbTeam2Player1.clear();
		cbTeam2Player2.clear();
	}

	private MatchDto makeMatch() {
		final MatchDto newMatch = new MatchDto();
		newMatch.setMatchDate(new Date());

		TeamDto team1 = null;
		TeamDto team2 = null;
		final Radio radio = (Radio) tgPlayType.getValue();
		if (radio.getId().equals(MatchType.Single.getMatchType())) {
			newMatch.setMatchType(MatchType.Single);
			team1 = new TeamDto(cbTeam1Player1.getValue());
			team2 = new TeamDto(cbTeam2Player1.getValue());
		} else {
			newMatch.setMatchType(MatchType.Double);
			team1 = new TeamDto(cbTeam1Player1.getValue(), cbTeam1Player2.getValue());
			team2 = new TeamDto(cbTeam2Player1.getValue(), cbTeam2Player2.getValue());
		}
		newMatch.setTeam1(team1);
		newMatch.setTeam2(team2);

		final MatchSetDto newSets = new MatchSetDto();
		newSets.getSetsTeam1().add(cbSet1Team1.getValue());
		newSets.getSetsTeam1().add(cbSet2Team1.getValue());
		if (cbSet3Team1.getValue() != null) {
			newSets.getSetsTeam1().add(cbSet3Team1.getValue());
		}

		newSets.getSetsTeam2().add(cbSet1Team2.getValue());
		newSets.getSetsTeam2().add(cbSet2Team2.getValue());
		if (cbSet3Team2.getValue() != null) {
			newSets.getSetsTeam2().add(cbSet3Team2.getValue());
		}
		newMatch.setSets(newSets);

		return newMatch;
	}

}
