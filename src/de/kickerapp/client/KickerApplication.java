package de.kickerapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

public class KickerApplication implements IsWidget, EntryPoint {

	@Override
	public Widget asWidget() {
		BorderLayoutContainer blcMain = new BorderLayoutContainer();

		ContentPanel cpMain = new ContentPanel();
		cpMain.setHeadingText("Kicker Application");
		cpMain.setBodyStyle("padding: 6px");
		cpMain.add(new Label("I should be centered"));
		TextButton button = new TextButton("Test");
		button.setEnabled(false);
		button.setToolTip("TestToolTip");
		ToolTipConfig config = new ToolTipConfig();
		config.setShowDelay(5000);
		cpMain.addButton(button);

		blcMain.add(cpMain);
		return blcMain;
	}

	public void onModuleLoad() {
		Viewport viewport = new Viewport();
		viewport.add(asWidget(), new MarginData(10));
		RootPanel.get().add(viewport, 0, 0);
	}
}
