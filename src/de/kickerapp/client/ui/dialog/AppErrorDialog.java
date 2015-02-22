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

	private boolean detailsView;

	private String errorMessage;

	private String detailedErrorMessage;

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

		detailsView = false;

		final AppContentPanel cpError = new AppContentPanel();
		cpError.setHeaderVisible(false);

		final FlowLayoutContainer vlcError = new FlowLayoutContainer();
		vlcError.setScrollMode(ScrollMode.AUTO);

		message = new HTML();
		message.setWordWrap(false);

		vlcError.add(message, new MarginData(5));
		cpError.add(vlcError);

		add(cpError);

		getButtonBar().clear();

		addButton(createCloseButton());
		addButton(createDetailsButton());
	}

	@Override
	protected void onShow() {
		detailsView = true;
		updateErrorMessage();

		super.onShow();
	}

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

	private void updateErrorMessage() {
		if (!detailsView) {
			detailsView = true;
			setPixelSize(400, 400);
			message.setHTML(detailedErrorMessage);
		} else {
			detailsView = false;
			setPixelSize(400, 150);
			message.setHTML(errorMessage);
		}
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setDetailedErrorMessage(String detailedErrorMessage) {
		this.detailedErrorMessage = detailedErrorMessage;
	}

}
