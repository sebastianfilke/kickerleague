package de.kickerapp.client.ui;

import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;

public class PortalPanel extends BasePanel {

	public PortalPanel() {
		super();
	}

	@Override
	public void initLayout() {
		super.initLayout();

		final PortalLayoutContainer plcMain = createPortalLayoutContainer();

		final Portlet portletResult = createPortletResult();
		plcMain.add(portletResult, 0);

		final Portlet portletTable = createPortletTable();
		plcMain.add(portletTable, 1);

		final Portlet portletTimer = createPortletTimer();
		plcMain.add(portletTimer, 1);
	}

	private PortalLayoutContainer createPortalLayoutContainer() {
		final PortalLayoutContainer plcMain = new PortalLayoutContainer(3);
		plcMain.setColumnWidth(0, .40);
		plcMain.setColumnWidth(1, .30);
		plcMain.setColumnWidth(2, .30);

		return plcMain;
	}

	private Portlet createPortletResult() {
		final Portlet portletResult = new Portlet();
		portletResult.setHeadingHtml("<span style='font-size:18px;'>Ergebniss eintragen</i>");
		portletResult.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletResult.setCollapsible(true);
		portletResult.setHeight(250);

		return portletResult;
	}

	private Portlet createPortletTable() {
		final Portlet portletTabelle = new Portlet();
		portletTabelle.setHeadingHtml("<span style='font-size:18px;'>Aktuelle Tabelle</i>");
		portletTabelle.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletTabelle.setCollapsible(true);
		portletTabelle.setHeight(150);

		return portletTabelle;
	}

	private Portlet createPortletTimer() {
		final Portlet portletTimer = new Portlet();
		portletTimer.setHeadingHtml("<span style='font-size:18px;'>Stoppuhr</i>");
		portletTimer.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletTimer.setCollapsible(true);
		portletTimer.setHeight(150);

		return portletTimer;
	}

}
