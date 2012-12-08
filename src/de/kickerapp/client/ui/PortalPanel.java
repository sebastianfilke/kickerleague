package de.kickerapp.client.ui;

import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;

/**
 * Basis-Controller zur Darstellung und Verarbeitung der Panels.
 * 
 * @author Sebastian Filke
 */
public class PortalPanel extends BasePanel {

	/** Das Portal für den Ergebnispanel. */
	private Portlet portletResult;
	/** Das Portal für die zuletzt gespielten Spiele. */
	private Portlet portletMatches;
	/** Das Portal für die akutelle Tabelle. */
	private Portlet portletTable;
	/** Das Portal für die Stoppuhr. */
	private Portlet portletTimer;

	/**
	 * Erstellt einen neuen Basis-Controller.
	 */
	public PortalPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		setHeaderVisible(false);

		final PortalLayoutContainer plcMain = createPortalLayoutContainer();
		// plcMain.setBorders(false);

		portletResult = createPortletResult();
		plcMain.add(portletResult, 0);

		portletMatches = createPortletMatches();
		plcMain.add(portletMatches, 0);

		portletTable = createPortletTable();
		plcMain.add(portletTable, 1);

		portletTimer = createPortletTimer();
		plcMain.add(portletTimer, 2);

		add(plcMain);
	}

	/**
	 * @return
	 */
	private PortalLayoutContainer createPortalLayoutContainer() {
		final PortalLayoutContainer plcMain = new PortalLayoutContainer(3);
		plcMain.setColumnWidth(0, .30);
		plcMain.setColumnWidth(1, .45);
		plcMain.setColumnWidth(2, .25);

		return plcMain;
	}

	private Portlet createPortletResult() {
		final Portlet portletResult = new Portlet();
		portletResult.setHeadingHtml("<span style='font-size:14px;'>Ergebnis eintragen</i>");
		portletResult.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletResult.setCollapsible(true);
		portletResult.add(new ResultPanel());
		portletResult.setSize("350px", "370px");

		return portletResult;
	}

	private Portlet createPortletMatches() {
		final Portlet portletMatches = new Portlet();
		portletMatches.setHeadingHtml("<span style='font-size:14px;'>Zuletzt Gespielt</i>");
		portletMatches.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletMatches.setCollapsible(true);
		portletMatches.setSize("400px", "250px");

		return portletMatches;
	}

	private Portlet createPortletTable() {
		final Portlet portletTable = new Portlet();
		portletTable.setHeadingHtml("<span style='font-size:14px;'>Aktuelle Tabelle</i>");
		portletTable.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletTable.setCollapsible(true);
		portletTable.setSize("400px", "465px");

		return portletTable;
	}

	private Portlet createPortletTimer() {
		final Portlet portletTimer = new Portlet();
		portletTimer.setHeadingHtml("<span style='font-size:14px;'>Stoppuhr</i>");
		portletTimer.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletTimer.setCollapsible(true);
		portletTimer.setSize("300px", "150px");

		return portletTimer;
	}

	public Portlet getPortletResult() {
		return portletResult;
	}

	public Portlet getPortletMatches() {
		return portletMatches;
	}

	public Portlet getPortletTable() {
		return portletTable;
	}

	public Portlet getPortletTimer() {
		return portletTimer;
	}

}
