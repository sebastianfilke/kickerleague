package de.kickerapp.client.ui.dialog;

import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.container.MarginData;

import de.kickerapp.client.ui.base.BaseDialog;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppContentPanel;

/**
 * Allgemeiner Fehlerdialog, der auch einen Stacktrace anzeigen kann.
 * 
 * @author Sebastian Filke
 */
public class AppErrorDialog extends BaseDialog {

	private AppButton btnOk;

	private AppButton btnDetails;

	private HTML html;

	/**
	 * 
	 */
	public AppErrorDialog() {
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initLayout() {
		super.initLayout();
		setModal(true);

		setHeadingHtml("<span id='panelHeading'>Fehler</span>");
		setSize("400", "150");

		final AppContentPanel contentPanel = new AppContentPanel();
		contentPanel.setHeaderVisible(false);

		html = new HTML();

		contentPanel.add(html, new MarginData(5));

		setHideOnButtonClick(true);
		add(contentPanel);
	}

	public void setErrorMsg(String text) {
		html.setText(text);
	}

}
