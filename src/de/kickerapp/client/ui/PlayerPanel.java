package de.kickerapp.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.info.Info;

import de.kickerapp.client.exception.AppExceptionHandler;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.util.CursorDefs;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppTextField;
import de.kickerapp.shared.match.PlayerDto;

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

	private AppButton btnReset;
	private AppButton btnReport;

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

		add(createResultFieldSet(), new MarginData(10));
	}

	/**
	 * Erzeugt das Fieldset mit den ComboBoxen zum Eintragen des Ergebnisses.
	 * 
	 * @return Das erzeugte FieldSet.
	 */
	private FieldSet createResultFieldSet() {
		final FieldSet fsPlayer = new FieldSet();
		fsPlayer.setHeadingText("Neuer Spieler");

		final VerticalLayoutContainer vlcPlayer = new VerticalLayoutContainer();

		tfNickname = createTextField("Nicknamen eintragen");
		vlcPlayer.add(new FieldLabel(tfNickname, "Nickname"), new VerticalLayoutData(1, -1));

		tfFirstname = createTextField("Vornamen eintragen");
		vlcPlayer.add(new FieldLabel(tfFirstname, "Vorname"), new VerticalLayoutData(1, -1));

		tfLastname = createTextField("Nachnamen eintragen");
		vlcPlayer.add(new FieldLabel(tfLastname, "Nachname"), new VerticalLayoutData(1, -1));

		tfEMail = createTextField("E-Mail Adresse eintragen");
		vlcPlayer.add(new FieldLabel(tfEMail, "E-Mail"), new VerticalLayoutData(1, -1));

		fsPlayer.add(vlcPlayer);

		return fsPlayer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initPanelButtons(Portlet portletResult) {
		btnReset = new AppButton("Eingaben zurücksetzen");
		btnReset.setToolTip("Setzt alle eingegebenen Daten zurück");
		btnReset.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				// TODO
			}
		});
		btnReport = new AppButton("Spieler eintragen");
		btnReport.setToolTip("Speichert den Spieler mit den einegegeben Daten in der Datebank");
		btnReport.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				createNewPlayer();
			}
		});
		portletResult.addButton(btnReset);
		portletResult.addButton(btnReport);
	}

	/**
	 * @param emptyText
	 * @return
	 */
	private AppTextField createTextField(String emptyText) {
		final AppTextField tfPlayer = new AppTextField(emptyText);
		tfPlayer.setAllowBlank(false);

		return tfPlayer;
	}

	private void createNewPlayer() {
		mask("Spieler wird eingetragen...");
		CursorDefs.showWaitCursor();
		btnReport.setEnabled(false);

		final PlayerDto newPlayer = createPlayer();

		KickerServices.PLAYER_SERVICE.createPlayer(newPlayer, new AsyncCallback<PlayerDto>() {
			@Override
			public void onSuccess(PlayerDto result) {
				Info.display("Erfolgreich", "Neuer Spieler wurde erfolgreich erstellt");
				btnReport.setEnabled(true);
				CursorDefs.showDefaultCursor();
				unmask();
			}

			@Override
			public void onFailure(Throwable caught) {
				btnReport.setEnabled(true);
				CursorDefs.showDefaultCursor();
				unmask();
				AppExceptionHandler.handleException(caught);
			}
		});
	}

	private PlayerDto createPlayer() {
		PlayerDto newPlayer = new PlayerDto(tfFirstname.getText(), tfLastname.getText());
		newPlayer.setNickName(tfNickname.getText());
		newPlayer.setEMail(tfEMail.getText());

		return newPlayer;
	}

}
