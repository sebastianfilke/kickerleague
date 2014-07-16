package de.kickerapp.client.ui.controller;

import java.util.ArrayList;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.CssFloatLayoutContainer;
import com.sencha.gxt.widget.core.client.container.CssFloatLayoutContainer.CssFloatData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent.BeforeSelectHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.TabPanelEvent;
import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.exception.AppExceptionHandler;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.properties.PlayerProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.client.ui.dialog.AppInfoDialog;
import de.kickerapp.client.ui.resources.IconProvider;
import de.kickerapp.client.ui.util.AppInfo;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppCheckBox;
import de.kickerapp.client.widgets.AppFieldLabel;
import de.kickerapp.client.widgets.AppTextField;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Controller-Klasse zum Eintragen und Bearbeiten der Spieler für die Applikation.
 * 
 * @author Sebastian Filke
 */
public class PlayerAdminPanel extends BaseContainer {

	private ListStore<PlayerDto> storePlayer;

	private ListView<PlayerDto, String> lvPlayer;

	private StoreFilterField<PlayerDto> sffPlayer;

	private AppTextField tfNickname;

	private AppTextField tfFirstname;

	private AppTextField tfLastname;

	private AppTextField tfEMail;

	private AppCheckBox cbLocked;

	private AppButton btnListUpdate;

	private ToggleButton btnPlayerUpdate;

	private boolean doUpdatePlayerList;

	private FieldSet fsPlayer;

	/**
	 * Erzeugt einen neuen Controller zum Eintragen neuer Spieler für die Applikation.
	 */
	public PlayerAdminPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initLayout() {
		setBorders(false);

		doUpdatePlayerList = true;

		storePlayer = new ListStore<PlayerDto>(KickerProperties.PLAYER_PROPERTY.id());

		add(createAdministrationContainer());
	}

	private BorderLayoutContainer createAdministrationContainer() {
		final BorderLayoutContainer blcAdmin = new BorderLayoutContainer();
		blcAdmin.setBorders(false);

		final VerticalLayoutContainer vlcPlayerInfo = new VerticalLayoutContainer();
		vlcPlayerInfo.getElement().getStyle().setBackgroundColor("#F1F1F1");
		vlcPlayerInfo.addStyleName("playerAdminInfo");
		vlcPlayerInfo.add(createToolBarPlayerInfo(), new VerticalLayoutData(1, -1));
		vlcPlayerInfo.add(createPlayerInfoFieldSet(), new VerticalLayoutData(1, -1, new Margins(10)));

		final VerticalLayoutContainer vlcPlayerList = new VerticalLayoutContainer();
		vlcPlayerList.addStyleName("playerAdminPlayerList");
		vlcPlayerList.getElement().getStyle().setBackgroundColor("white");
		vlcPlayerList.add(createToolBarPlayerList(), new VerticalLayoutData(1, -1));
		vlcPlayerList.add(createPlayerListListView(), new VerticalLayoutData(1, -1));

		final BorderLayoutData eastData = new BorderLayoutData(520);
		eastData.setMargins(new Margins(0, 0, 0, 5));

		blcAdmin.setCenterWidget(vlcPlayerInfo);
		blcAdmin.setEastWidget(vlcPlayerList, eastData);

		return blcAdmin;
	}

	private ToolBar createToolBarPlayerInfo() {
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
	private FieldSet createPlayerInfoFieldSet() {
		fsPlayer = new FieldSet();
		fsPlayer.setHeadingText("Neuen Spieler eintragen");

		final CssFloatLayoutContainer cflcPlayer = new CssFloatLayoutContainer();

		tfNickname = new AppTextField("Spitznamen eintragen (notwendig)");
		tfNickname.setAllowBlank(false);
		cflcPlayer.add(new AppFieldLabel(tfNickname, "Spitzname", true), new CssFloatData(1, new Margins(0, 0, 8, 0)));

		tfFirstname = new AppTextField("Vornamen eintragen (notwendig)");
		tfFirstname.setAllowBlank(false);
		cflcPlayer.add(new AppFieldLabel(tfFirstname, "Vorname", true), new CssFloatData(1, new Margins(0, 0, 8, 0)));

		tfLastname = new AppTextField("Nachnamen eintragen (notwendig)");
		tfLastname.setAllowBlank(false);
		cflcPlayer.add(new AppFieldLabel(tfLastname, "Nachname", true), new CssFloatData(1, new Margins(0, 0, 8, 0)));

		tfEMail = new AppTextField("E-Mail Adresse eintragen");
		cflcPlayer.add(new AppFieldLabel(tfEMail, "E-Mail", false), new CssFloatData(1, new Margins(0, 0, 8, 0)));

		cbLocked = new AppCheckBox();
		cbLocked.setBoxLabel("");
		cbLocked.setToolTip("Falls selektiert, kann mit dem Spieler kein neues Spiel mehr eingetragen werden");
		cflcPlayer.add(new AppFieldLabel(cbLocked, "Gesperrt", false), new CssFloatData(1));

		fsPlayer.add(cflcPlayer);

		return fsPlayer;
	}

	private AppButton createBtnInsert() {
		final AppButton btnReport = new AppButton("Spieler speichern", IconProvider.get().save());
		btnReport.setToolTip("Speichert oder Aktualisiert den Spieler mit den einegegeben Daten in der Datebank");
		btnReport.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (btnPlayerUpdate.getValue() && lvPlayer.getSelectionModel().getSelectedItem() != null) {
					updateExistingPlayer();
				} else {
					createNewPlayer();
				}
			}
		});
		return btnReport;
	}

	private AppButton createBtnReset() {
		final AppButton btnReset = new AppButton("Eingaben zurücksetzen", IconProvider.get().reset());
		btnReset.setToolTip("Setzt alle eingegebenen Daten zurück und beendet, falls aktiv, das Bearbeiten eines Spielers");
		btnReset.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				clearInput();
			}
		});
		return btnReset;
	}

	private void clearInput() {
		tfNickname.reset();
		tfFirstname.reset();
		tfLastname.reset();
		tfEMail.reset();
		cbLocked.setValue(false, true);
		btnPlayerUpdate.setValue(false);
		lvPlayer.getSelectionModel().deselectAll();
		fsPlayer.setHeadingText("Neuen Spieler eintragen");
		sffPlayer.setEnabled(true);
		btnListUpdate.setEnabled(true);
	}

	private ToolBar createToolBarPlayerList() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		sffPlayer = new StoreFilterField<PlayerDto>() {
			@Override
			protected boolean doSelect(Store<PlayerDto> store, PlayerDto parent, PlayerDto item, String filter) {
				if (item.getLastName().toLowerCase().contains(filter.toLowerCase()) || item.getFirstName().toLowerCase().contains(filter.toLowerCase())
						|| item.getNickName().toLowerCase().contains(filter.toLowerCase())) {
					return true;
				}
				return false;
			}
		};
		sffPlayer.bind(storePlayer);
		sffPlayer.setWidth(250);
		sffPlayer.setEmptyText("Nach Spieler suchen...");

		toolBar.add(createBtnUpdate());
		toolBar.add(createBtnEdit());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(sffPlayer);

		return toolBar;
	}

	private AppButton createBtnUpdate() {
		btnListUpdate = new AppButton("Aktualisieren", IconProvider.get().update());
		btnListUpdate.setToolTip("Aktualisiert die Liste der eingetragenen Spieler in der Datenbank");
		btnListUpdate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				setDoUpdate();
				getPlayerList();
			}

			private void setDoUpdate() {
				doUpdatePlayerList = true;
			}
		});
		return btnListUpdate;
	}

	private ToggleButton createBtnEdit() {
		btnPlayerUpdate = new ToggleButton("Spieler bearbeiten");
		btnPlayerUpdate.setIcon(IconProvider.get().player_edit());
		btnPlayerUpdate.setToolTip("Wählt den aktuell selektierten Spieler zum Bearbeiten");
		btnPlayerUpdate.addBeforeSelectHandler(new BeforeSelectHandler() {
			@Override
			public void onBeforeSelect(BeforeSelectEvent event) {
				final PlayerDto selectedPlayer = lvPlayer.getSelectionModel().getSelectedItem();
				if (selectedPlayer == null) {
					event.setCancelled(true);
				}
			}
		});
		btnPlayerUpdate.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					final PlayerDto selectedPlayer = lvPlayer.getSelectionModel().getSelectedItem();
					if (selectedPlayer != null) {
						tfNickname.setValue(selectedPlayer.getNickName());
						tfFirstname.setValue(selectedPlayer.getFirstName());
						tfLastname.setValue(selectedPlayer.getLastName());
						tfEMail.setValue(selectedPlayer.getEMail());
						cbLocked.setValue(selectedPlayer.isLocked(), true);
						fsPlayer.setHeadingText("Spieler bearbeiten");
						sffPlayer.setEnabled(false);
						btnListUpdate.setEnabled(false);
					}
				} else {
					clearInput();
				}
			}
		});
		return btnPlayerUpdate;
	}

	private ListView<PlayerDto, String> createPlayerListListView() {
		lvPlayer = new ListView<PlayerDto, String>(storePlayer, PlayerProperty.playerName);
		lvPlayer.getSelectionModel().addBeforeSelectionHandler(new BeforeSelectionHandler<PlayerDto>() {
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<PlayerDto> event) {
				if (btnPlayerUpdate.getValue()) {
					event.cancel();
				}
			}
		});
		lvPlayer.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		lvPlayer.setBorders(false);

		return lvPlayer;
	}

	public void getPlayerList() {
		if (doUpdatePlayerList) {
			mask("Aktualisiere...");
			KickerServices.PLAYER_SERVICE.getAllPlayers(MatchType.NONE, new AsyncCallback<ArrayList<PlayerDto>>() {
				@Override
				public void onSuccess(ArrayList<PlayerDto> result) {
					storePlayer.replaceAll(result);
					doUpdatePlayerList = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdatePlayerList = false;
					unmask();
					AppExceptionHandler.getInstance().handleException(caught);
				}
			});
		}
	}

	private void createNewPlayer() {
		if (inputValid()) {
			mask("Spieler wird eingetragen...");
			final PlayerDto newPlayer = createPlayer();
			KickerServices.PLAYER_SERVICE.createPlayer(newPlayer, new AsyncCallback<PlayerDto>() {
				@Override
				public void onSuccess(PlayerDto result) {
					AppInfo.showInfo("Hinweis", "Neuer Spieler " + newPlayer.getLastName() + ", " + newPlayer.getFirstName() + " wurde erfolgreich eingetragen");
					doUpdatePlayerList = true;
					getPlayerList();
					fireEvents();
					clearInput();
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					unmask();
					AppExceptionHandler.getInstance().handleException(caught);
				}
			});
		} else {
			AppInfoDialog.getInstance().setInfoMessage("Es müssen erst alle notwendigen Angaben gemacht werden!");
			AppInfoDialog.getInstance().show();
		}
	}

	private boolean inputValid() {
		return tfNickname.getCurrentValue() != null && tfFirstname.getCurrentValue() != null && tfLastname.getCurrentValue() != null;
	}

	private PlayerDto createPlayer() {
		final PlayerDto newPlayer = new PlayerDto(tfLastname.getValue(), tfFirstname.getValue());
		newPlayer.setNickName(tfNickname.getValue());
		if (tfEMail.getValue() != null) {
			newPlayer.setEMail(tfEMail.getValue().toLowerCase());
		}
		newPlayer.setLocked(cbLocked.getValue());

		return newPlayer;
	}

	private void updateExistingPlayer() {
		if (inputValid()) {
			mask("Spieler wird aktualisiert...");
			final PlayerDto updatedPlayer = updatePlayer();
			KickerServices.PLAYER_SERVICE.updatePlayer(updatedPlayer, new AsyncCallback<PlayerDto>() {
				@Override
				public void onSuccess(PlayerDto result) {
					AppInfo.showInfo("Hinweis", "Spieler " + updatedPlayer.getLastName() + ", " + updatedPlayer.getFirstName()
							+ " wurde erfolgreich aktualisiert");
					doUpdatePlayerList = true;
					getPlayerList();
					fireEvents();
					clearInput();
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					unmask();
					AppExceptionHandler.getInstance().handleException(caught);
				}
			});
		} else {
			AppInfoDialog.getInstance().setInfoMessage("Es müssen erst alle notwendigen Angaben gemacht werden!");
			AppInfoDialog.getInstance().show();
		}
	}

	private PlayerDto updatePlayer() {
		final PlayerDto selectedPlayer = lvPlayer.getSelectionModel().getSelectedItem();
		if (selectedPlayer != null) {
			selectedPlayer.setLastName(tfLastname.getValue());
			selectedPlayer.setFirstName(tfFirstname.getValue());
			selectedPlayer.setNickName(tfNickname.getValue());
			if (tfEMail.getValue() != null) {
				selectedPlayer.setEMail(tfEMail.getValue().toLowerCase());
			}
			selectedPlayer.setLocked(cbLocked.getValue());
		}
		return selectedPlayer;
	}

	private void fireEvents() {
		AppEventBus.fireEvent(new TabPanelEvent(TabPanelEvent.TABLES, 0));
		AppEventBus.fireEvent(new UpdatePanelEvent(UpdatePanelEvent.TABLES));
	}

}
