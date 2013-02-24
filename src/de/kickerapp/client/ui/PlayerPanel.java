package de.kickerapp.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.TabPanelEvent;
import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.exception.AppExceptionHandler;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.ui.util.AppInfo;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppTextField;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Controller-Klasse zum Eintragen neuer Spieler für die Applikation.
 * 
 * @author Sebastian Filke
 */
public class PlayerPanel extends BasePanel {

	private AppTextField tfNickname;
	private AppTextField tfFirstname;
	private AppTextField tfLastname;
	private AppTextField tfEMail;

	/**
	 * Erzeugt einen neuen Controller zum Eintragen neuer Spieler für die
	 * Applikation.
	 */
	public PlayerPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		super.initLayout();
		setHeadingHtml("<span id='panelHeading'>Spieler</span>");

		final VerticalLayoutContainer vlcMain = new VerticalLayoutContainer();

		final ToolBar toolBar = createToolBar();
		vlcMain.add(toolBar, new VerticalLayoutData(1, -1));

		final FieldSet fieldSetResult = createPlayerFieldSet();
		vlcMain.add(fieldSetResult, new VerticalLayoutData(1, -1, new Margins(10)));

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
	private FieldSet createPlayerFieldSet() {
		final FieldSet fsPlayer = new FieldSet();
		fsPlayer.setHeadingText("Neuer Spieler");

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
		final AppButton btnReport = new AppButton("Spieler eintragen", KickerIcons.ICON.table_save());
		btnReport.setToolTip("Speichert den Spieler mit den einegegeben Daten in der Datebank");
		btnReport.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				createNewPlayer();
			}
		});
		return btnReport;
	}

	private AppButton createBtnReset() {
		final AppButton btnReset = new AppButton("Eingaben zurücksetzen", KickerIcons.ICON.table());
		btnReset.setToolTip("Setzt alle eingegebenen Daten zurück");
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

	private void createNewPlayer() {
		if (inputValid()) {
			mask("Spieler wird eingetragen...");
			final PlayerDto newPlayer = createPlayer();
			KickerServices.PLAYER_SERVICE.createPlayer(newPlayer, new AsyncCallback<PlayerDto>() {
				@Override
				public void onSuccess(PlayerDto result) {
					AppInfo.showInfo("Hinweis", "Neuer Spieler " + newPlayer.getLastName() + ", " + newPlayer.getFirstName() + " wurde erfolgreich eingetragen");
					final TabPanelEvent tabPanelEvent = new TabPanelEvent();
					tabPanelEvent.setActiveWidget(0);
					AppEventBus.fireEvent(tabPanelEvent);
					final UpdatePanelEvent updatePanelEvent = new UpdatePanelEvent(UpdatePanelEvent.ALL);
					updatePanelEvent.setActiveWidget(0);
					AppEventBus.fireEvent(updatePanelEvent);
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
		newPlayer.setEMail(tfEMail.getText());

		return newPlayer;
	}

}
