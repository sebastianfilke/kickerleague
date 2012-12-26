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

	/** Das Portal für die Spielergebnisse. */
	private Portlet portletResult;
	/** Das Portal für die zuletzt gespielten Spiele. */
	private Portlet portletMatches;
	/** Das Portal zum Anzeigen der aktuellen Tabelle. */
	private Portlet portletTable;
	/** Das Portal für die Verwendung der Stoppuhr. */
	private Portlet portletTimer;
	/** Das Portal zum Eintragen von Spieler. */
	private Portlet portletPlayer;

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

		portletPlayer = createPortletPlayer();
		plcMain.add(portletPlayer, 2);

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
		portletResult.getHeader().addTool(new ToolButton(ToolButton.CLOSE));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.COLLAPSE));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.DOUBLEDOWN));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.DOUBLELEFT));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.DOUBLERIGHT));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.DOUBLEUP));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.DOWN));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.EXPAND));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.GEAR));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.LEFT));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.MAXIMIZE));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.MINIMIZE));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.MINUS));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.PIN));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.PLUS));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.PRINT));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.QUESTION));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.RESTORE));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.RIGHT));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.SAVE));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.SEARCH));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.UNPIN));
		portletResult.getHeader().addTool(new ToolButton(ToolButton.UP));
		portletResult.setCollapsible(true);
		portletResult.setPixelSize(350, 440);

		return portletResult;
	}

	private Portlet createPortletMatches() {
		final Portlet portletMatches = new Portlet();
		portletMatches.setHeadingHtml("<span style='font-size:14px;'>Zuletzt Gespielt</i>");
		portletMatches.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletMatches.setCollapsible(true);
		portletMatches.setPixelSize(400, 250);

		return portletMatches;
	}

	private Portlet createPortletTable() {
		final Portlet portletTable = new Portlet();
		portletTable.setHeadingHtml("<span style='font-size:14px;'>Aktuelle Tabelle</i>");
		portletTable.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletTable.setCollapsible(true);
		portletTable.setPixelSize(400, 465);

		return portletTable;
	}

	private Portlet createPortletTimer() {
		final Portlet portletTimer = new Portlet();
		portletTimer.setHeadingHtml("<span style='font-size:14px;'>Stoppuhr</i>");
		portletTimer.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletTimer.setCollapsible(true);
		portletTimer.setPixelSize(300, 150);

		return portletTimer;
	}

	private Portlet createPortletPlayer() {
		final Portlet portletPlayer = new Portlet();
		portletPlayer.setHeadingHtml("<span style='font-size:14px;'>Spieler</i>");
		portletPlayer.getHeader().addTool(new ToolButton(ToolButton.REFRESH));
		portletPlayer.setCollapsible(true);
		portletPlayer.setPixelSize(300, 245);

		return portletPlayer;
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

	public Portlet getPortletPlayer() {
		return portletPlayer;
	}

}
