package de.kickerapp.client.ui.dialog;

import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.kickerapp.client.ui.base.BaseDialog;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppContentPanel;

/**
 * Dialog für Fehler, der auch einen StackTrace anzeigen kann.
 * 
 * @author Sebastian Filke
 */
public final class AppErrorDialog extends BaseDialog {

	/** Die Fehlermeldung. */
	private HTML message;
	/** Die Fehlermeldung. */
	private String errorMessage;
	/** Die detaillierte Fehlerbeschreibung. */
	private String detailedErrorMessage;
	/** Die Angabe, ob die detaillierte Fehlerbeschreibung sichtbar ist. */
	private boolean detailsVisible;

	/**
	 * Erzeugt einen neuen Dialog für Fehler.
	 */
	private AppErrorDialog() {
		super();
		initLayout();
	}

	/**
	 * Klasse zum Halten der Instanz von {@link AppErrorDialog}.
	 * 
	 * @author Sebastian Filke
	 */
	private static class LazyHolder {

		/** Die Instanz der Klasse {@link AppErrorDialog}. */
		private static final AppErrorDialog INSTANCE = new AppErrorDialog();
	}

	/**
	 * Statische Methode <code>getInstance()</code> liefert die einzige Instanz der {@link AppErrorDialog}-Klasse.
	 * 
	 * @return Die einzige Instanz der {@link AppErrorDialog}-Klasse.
	 */
	public static AppErrorDialog getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initLayout() {
		super.initLayout();
		setHeadingHtml("<span id='panelHeading'>Fehler</span>");
		setButtonAlign(BoxLayoutPack.CENTER);
		setHideOnButtonClick(true);
		setPixelSize(400, 150);
		setBlinkModal(true);
		setClosable(false);
		setModal(true);

		detailsVisible = false;

		final AppContentPanel cpError = new AppContentPanel();
		cpError.setHeaderVisible(false);

		final FlowLayoutContainer flcError = new FlowLayoutContainer();
		flcError.setScrollMode(ScrollMode.AUTO);

		message = new HTML();
		message.setWordWrap(false);

		flcError.add(message, new MarginData(5));
		cpError.add(flcError);

		add(cpError);

		getButtonBar().clear();
		addButton(createCloseButton());
		addButton(createDetailsButton());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onShow() {
		setErrorMessageAsContent();
		super.onShow();
	}

	/**
	 * Erzeugt den Button zum Schließen des Dialogs.
	 * 
	 * @return Der erzeugte Button.
	 */
	private AppButton createCloseButton() {
		final AppButton detailsButton = new AppButton("Schließen");
		detailsButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				hide();
			}
		});
		return detailsButton;
	}

	/**
	 * Erzeugt den Button zum Anzeigen der detaillierten Fehlermeldung.
	 * 
	 * @return Der erzeugte Button.
	 */
	private AppButton createDetailsButton() {
		final AppButton detailsButton = new AppButton("Details");
		detailsButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				updateErrorMessage();
			}
		});
		return detailsButton;
	}

	/**
	 * Aktualisiert die Fehlermeldung, anhand der Angabe ob die detaillierte Fehlermeldung sichtbar ist.
	 */
	private void updateErrorMessage() {
		if (!detailsVisible) {
			setDetailedErrorMessageAsContent();
		} else {
			setErrorMessageAsContent();
		}
	}

	/**
	 * Zeigt die Fehlermeldung an.
	 */
	private void setErrorMessageAsContent() {
		detailsVisible = false;
		setPixelSize(500, 150);
		message.setHTML(errorMessage);
	}

	/**
	 * Zeigt die detaillierte Fehlermeldung an.
	 */
	private void setDetailedErrorMessageAsContent() {
		detailsVisible = true;
		setPixelSize(500, 400);
		message.setHTML(detailedErrorMessage);
	}

	/**
	 * Die Fehlermeldung.
	 * 
	 * @param errorMessage Die Fehlermeldung als {@link String}.
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Die detaillierte Fehlerbeschreibung.
	 * 
	 * @param detailedErrorMessage Die detaillierte Fehlerbeschreibung als {@link String}.
	 */
	public void setDetailedErrorMessage(String detailedErrorMessage) {
		this.detailedErrorMessage = detailedErrorMessage;
	}

}
