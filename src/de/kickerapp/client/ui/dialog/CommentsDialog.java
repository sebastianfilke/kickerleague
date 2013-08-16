package de.kickerapp.client.ui.dialog;

import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.container.MarginData;

import de.kickerapp.client.ui.base.BaseDialog;
import de.kickerapp.client.ui.images.KickerIcons;

public class CommentsDialog extends BaseDialog {

	public CommentsDialog() {
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initLayout() {
		super.initLayout();
		setMaximizable(true);
		setModal(true);

		getHeader().setIcon(KickerIcons.ICON.comments());
		setHeadingHtml("<span id='panelHeading'>Kommentare zum Spiel</span>");
		setSize("500", "300");

		setHideOnButtonClick(true);
		add(new Label("test"), new MarginData(5));
	}

	@Override
	protected void onShow() {
		super.onShow();

	}

}
