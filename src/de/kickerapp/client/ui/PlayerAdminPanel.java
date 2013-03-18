package de.kickerapp.client.ui;

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
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent.BeforeSelectHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
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
import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.ui.util.AppInfo;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppTextField;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDto;

public class PlayerAdminPanel extends BaseContainer {

	private ListStore<PlayerDto> storePlayer;

	private ListView<PlayerDto, String> lvPlayer;

	private StoreFilterField<PlayerDto> sffPlayer;

	private AppTextField tfNickname;

	private AppTextField tfFirstname;

	private AppTextField tfLastname;

	private AppTextField tfEMail;

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
	public void initLayout() {
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

		final VerticalLayoutContainer vlcPlayer = new VerticalLayoutContainer();

		tfNickname = createTextField("Spitznamen eintragen (notwendig)");
		vlcPlayer.add(new FieldLabel(tfNickname, "Spitzname"), new VerticalLayoutData(1, -1));

		tfFirstname = createTextField("Vornamen eintragen (notwendig)");
		vlcPlayer.add(new FieldLabel(tfFirstname, "Vorname"), new VerticalLayoutData(1, -1));

		tfLastname = createTextField("Nachnamen eintragen (notwendig)");
		vlcPlayer.add(new FieldLabel(tfLastname, "Nachname"), new VerticalLayoutData(1, -1));

		tfEMail = createTextField("E-Mail Adresse eintragen");
		tfEMail.setAllowBlank(true);
		vlcPlayer.add(new FieldLabel(tfEMail, "E-Mail"), new VerticalLayoutData(1, -1));

		fsPlayer.add(vlcPlayer);

		return fsPlayer;
	}

	private AppButton createBtnInsert() {
		final AppButton btnReport = new AppButton("Spieler speichern", KickerIcons.ICON.tableSave());
		btnReport.setToolTip("Speichert oder Aktualisiert den Spieler mit den einegegeben Daten in der Datebank");
		btnReport.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (btnPlayerUpdate.getValue() && lvPlayer.getSelectionModel().getSelectedItem() != null) {
					updatePlayer();
				} else {
					createNewPlayer();
				}
			}
		});
		return btnReport;
	}

	private AppButton createBtnReset() {
		final AppButton btnReset = new AppButton("Eingaben zurücksetzen", KickerIcons.ICON.table());
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
		tfNickname.clear();
		tfFirstname.clear();
		tfLastname.clear();
		tfEMail.clear();
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
		btnListUpdate = new AppButton("Aktualisieren", KickerIcons.ICON.tableRefresh());
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
		btnPlayerUpdate.setIcon(KickerIcons.ICON.tableEdit());
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

	/**
	 * @param emptyText
	 * @return
	 */
	private AppTextField createTextField(String emptyText) {
		final AppTextField tf = new AppTextField(emptyText);
		tf.setAllowBlank(false);

		return tf;
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

	protected void getPlayerList() {
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
					AppExceptionHandler.handleException(caught);
				}
			});
		}
	}

	private void updatePlayer() {
		if (inputValid()) {
			mask("Spieler wird aktualisiert...");
			final PlayerDto updatedPlayer = getPlayer();
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
		return isNotNullAndEmpty(tfNickname) && isNotNullAndEmpty(tfFirstname) && isNotNullAndEmpty(tfLastname);
	}

	private boolean isNotNullAndEmpty(AppTextField tf) {
		return tf.getValue() != null && !tf.getValue().isEmpty();
	}

	private PlayerDto createPlayer() {
		final PlayerDto newPlayer = new PlayerDto(tfLastname.getText(), tfFirstname.getText());
		newPlayer.setNickName(tfNickname.getText());
		if (tfEMail.getValue() != null) {
			newPlayer.setEMail(tfEMail.getText());
		}
		return newPlayer;
	}

	private PlayerDto getPlayer() {
		final PlayerDto selectedPlayer = lvPlayer.getSelectionModel().getSelectedItem();
		if (selectedPlayer != null) {
			selectedPlayer.setLastName(tfLastname.getText());
			selectedPlayer.setFirstName(tfFirstname.getText());
			selectedPlayer.setNickName(tfNickname.getText());
			if (tfEMail.getValue() != null) {
				selectedPlayer.setEMail(tfEMail.getText().toLowerCase());
			}
		}
		return selectedPlayer;
	}

	private void fireEvents() {
		final TabPanelEvent tabPanelEvent = new TabPanelEvent();
		tabPanelEvent.setActiveWidget(0);
		AppEventBus.fireEvent(tabPanelEvent);
		AppEventBus.fireEvent(new UpdatePanelEvent(UpdatePanelEvent.TABLES));
	}

}
