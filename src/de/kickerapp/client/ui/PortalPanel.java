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

	/** Das Portal f�r den Ergebnispanel. */
	private Portlet portletResult;
	/** Das Portal f�r die akutelle Tabelle. */
	private Portlet portletTable;
	/** Das Portal f�r die Stoppuhr/Zeituhr. */
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
	public void initLayout() {
		setHeaderVisible(false);

		final PortalLayoutContainer plcMain = createPortalLayoutContainer();
		plcMain.setBorders(false);

		portletTable = createPortletTable();
		plcMain.add(portletTable, 0);

		portletResult = createPortletResult();
		plcMain.add(portletResult, 1);

		portletTimer = createPortletTimer();
		plcMain.add(portletTimer, 1);

		add(plcMain);
	}

	/**
	 * @return
	 */
	private PortalLayoutContainer createPortalLayoutContainer() {
		final PortalLayoutContainer plcMain = new PortalLayoutContainer(4);
		plcMain.setColumnWidth(0, .40);
		plcMain.setColumnWidth(1, .20);
		plcMain.setColumnWidth(2, .20);
		plcMain.setColumnWidth(3, .20);

		return plcMain;
	}

	private Portlet createPortletTable() {
		final Portlet portletTabelle = new Portlet();
		portletTabelle.setHeadingHtml("<span style='font-size:14px;'>Aktuelle Tabelle</i>");
		portletTabelle.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletTabelle.setCollapsible(true);
		portletTabelle.setHeight(465);

		return portletTabelle;
	}

	private Portlet createPortletResult() {
		final Portlet portletResult = new Portlet();
		portletResult.setHeadingHtml("<span style='font-size:14px;'>Ergebniss eintragen</i>");
		portletResult.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletResult.setCollapsible(true);
		portletResult.setHeight(310);

		return portletResult;
	}

	private Portlet createPortletTimer() {
		final Portlet portletTimer = new Portlet();
		portletTimer.setHeadingHtml("<span style='font-size:14px;'>Stoppuhr</i>");
		portletTimer.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletTimer.setCollapsible(true);
		portletTimer.setHeight(150);

		return portletTimer;
	}

	public Portlet getPortletResult() {
		return portletResult;
	}

	public Portlet getPortletTable() {
		return portletTable;
	}

	public Portlet getPortletTimer() {
		return portletTimer;
	}

}
