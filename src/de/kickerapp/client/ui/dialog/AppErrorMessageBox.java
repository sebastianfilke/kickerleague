package de.kickerapp.client.ui.dialog;

import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.kickerapp.client.widgets.AppContentPanel;

/**
 * Allgemeiner Fehlerdialog, der auch einen Stacktrace anzeigen kann.
 * 
 * @author Sebastian Filke
 */
public class AppErrorMessageBox extends BaseDialog {

	private HTML html;

	/**
	 * 
	 */
	public AppErrorMessageBox() {
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initLayout() {
		super.initLayout();

		setHeadingHtml("<span id='panelHeading'>Fehler</span>");
		setSize("400", "150");

		final AppContentPanel contentPanel = new AppContentPanel();
		contentPanel.setHeaderVisible(false);

		html = new HTML();

		contentPanel.add(html, new MarginData(5));

		getButtonById(PredefinedButton.OK.name()).addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				hide();
			}
		});
		add(contentPanel);
	}

	public void setErrorMsg(String text) {
		html.setText(text);
	}

}
