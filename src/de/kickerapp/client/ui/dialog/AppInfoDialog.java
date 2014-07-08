package de.kickerapp.client.ui.dialog;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;

import de.kickerapp.client.ui.base.BaseDialog;
import de.kickerapp.client.ui.resources.icons.KickerIcons;

/**
 * Dialog für Infomeldungen.
 * 
 * @author Sebastian Filke
 */
public final class AppInfoDialog extends BaseDialog {

	/** Die Infomeldung. */
	private Label infoMessage;

	/**
	 * Klasse zum Halten der Instanz von {@link AppInfoDialog}.
	 * 
	 * @author Sebastian Filke
	 */
	private static class LazyHolder {

		/** Die Instanz der Klasse {@link AppInfoDialog}. */
		private static final AppInfoDialog INSTANCE = new AppInfoDialog();
	}

	/**
	 * Liefert die einzige Instanz der Klasse {@link AppInfoDialog}.
	 * 
	 * @return Die einzige Instanz der Klasse {@link AppInfoDialog}.
	 */
	public static AppInfoDialog getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * Erzeugt einen neuen Dialog für Fehler.
	 */
	private AppInfoDialog() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initLayout() {
		super.initLayout();
		setHeadingHtml("<span id='panelHeading'>Hinweis</span>");
		setButtonAlign(BoxLayoutPack.CENTER);
		setHideOnButtonClick(true);
		setPixelSize(300, 120);
		setBlinkModal(true);
		setModal(true);

		final HorizontalLayoutContainer hlcInfo = new HorizontalLayoutContainer();
		hlcInfo.add(new Image(KickerIcons.ICON.info()), new HorizontalLayoutData(-1, -1, new Margins(10, 5, 5, 5)));

		infoMessage = new Label();
		hlcInfo.add(infoMessage, new HorizontalLayoutData(1, 1, new Margins(10, 10, 10, 5)));

		add(hlcInfo);
	}

	/**
	 * Setzt die Infomeldung.
	 * 
	 * @param infoMessage Die Infomeldung als {@link String}.
	 */
	public void setInfoMessage(String infoMessage) {
		this.infoMessage.setText(infoMessage);
	}

}
