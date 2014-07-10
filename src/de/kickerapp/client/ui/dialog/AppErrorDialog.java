package de.kickerapp.client.ui.dialog;

import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.MarginData;

import de.kickerapp.client.ui.base.BaseDialog;
import de.kickerapp.client.widgets.AppContentPanel;

/**
 * Dialog für Fehler, der auch einen StackTrace anzeigen kann.
 * 
 * @author Sebastian Filke
 */
public final class AppErrorDialog extends BaseDialog {

	/** Die Fehlermeldung. */
	private HTML errorMessage;

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
	 * Erzeugt einen neuen Dialog für Fehler.
	 */
	private AppErrorDialog() {
		super();
		initLayout();
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
		setModal(true);

		final AppContentPanel contentPanel = new AppContentPanel();
		contentPanel.setHeaderVisible(false);

		errorMessage = new HTML();

		contentPanel.add(errorMessage, new MarginData(5));

		add(contentPanel);
	}

	/**
	 * Setzt die Fehlermeldung.
	 * 
	 * @param errorMessage Die Fehlermeldung als {@link String}.
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage.setText(errorMessage);
	}

}
