package de.kickerapp.client.ui;

import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.event.UpdatePanelEventHandler;

/**
 * Basis-Controller zur Darstellung und Verarbeitung der Panels.
 * 
 * @author Sebastian Filke
 */
public class PortalPanel extends BasePanel implements UpdatePanelEventHandler {

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
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		setHeaderVisible(false);

		final PortalLayoutContainer plcMain = createPortalLayoutContainer();

		portletResult = createPortletResult();
		plcMain.add(portletResult, 0);

		portletPlayer = createPortletPlayer();
		plcMain.add(portletPlayer, 0);

		portletMatches = createPortletMatches();
		plcMain.add(portletMatches, 1);

		portletTable = createPortletTable();
		plcMain.add(portletTable, 1);

		portletTimer = createPortletTimer();

		add(plcMain);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();

		AppEventBus.addHandler(UpdatePanelEvent.TYPE, this);
	}

	/**
	 * @return
	 */
	private PortalLayoutContainer createPortalLayoutContainer() {
		final PortalLayoutContainer plcMain = new PortalLayoutContainer(2);
		plcMain.setColumnWidth(0, .40);
		plcMain.setColumnWidth(1, .60);

		return plcMain;
	}

	private Portlet createPortletResult() {
		final Portlet portletResult = new Portlet();
		portletResult.setHeadingHtml("<span id='portletHeading'>Ergebnis eintragen</span>");
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.CLOSE));
		// portletResult.getHeader().addTool(new
		// ToolButton(ToolButton.COLLAPSE));
		// portletResult.getHeader().addTool(new
		// ToolButton(ToolButton.DOUBLEDOWN));
		// portletResult.getHeader().addTool(new
		// ToolButton(ToolButton.DOUBLELEFT));
		// portletResult.getHeader().addTool(new
		// ToolButton(ToolButton.DOUBLERIGHT));
		// portletResult.getHeader().addTool(new
		// ToolButton(ToolButton.DOUBLEUP));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.DOWN));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.EXPAND));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.GEAR));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.LEFT));
		// portletResult.getHeader().addTool(new
		// ToolButton(ToolButton.MAXIMIZE));
		// portletResult.getHeader().addTool(new
		// ToolButton(ToolButton.MINIMIZE));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.MINUS));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.PIN));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.PLUS));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.PRINT));
		// portletResult.getHeader().addTool(new
		// ToolButton(ToolButton.QUESTION));
		// portletResult.getHeader().addTool(new
		// ToolButton(ToolButton.REFRESH));
		// portletResult.getHeader().addTool(new
		// ToolButton(ToolButton.RESTORE));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.RIGHT));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.SAVE));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.SEARCH));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.UNPIN));
		// portletResult.getHeader().addTool(new ToolButton(ToolButton.UP));
		portletResult.setId("portletBackground");
		portletResult.setPixelSize(350, 440);
		portletResult.setCollapsible(true);

		return portletResult;
	}

	private Portlet createPortletMatches() {
		final Portlet portletMatches = new Portlet();
		portletMatches.setHeadingHtml("<span id='portletHeading'>Zuletzt Gespielt</span>");
		portletMatches.setId("portletBackground");
		portletMatches.setPixelSize(400, 350);
		portletMatches.setCollapsible(true);

		return portletMatches;
	}

	private Portlet createPortletTable() {
		final Portlet portletTable = new Portlet();
		portletTable.setHeadingHtml("<span id='portletHeading'>Aktuelle Spielertabelle (Einzelansicht)</span>");
		portletTable.setId("portletBackground");
		portletTable.setPixelSize(400, 350);
		portletTable.setCollapsible(true);

		return portletTable;
	}

	private Portlet createPortletTimer() {
		final Portlet portletTimer = new Portlet();
		portletTimer.setHeadingHtml("<span id='portletHeading'>Stoppuhr</span>");
		portletTimer.setId("portletBackground");
		portletTimer.setPixelSize(300, 150);
		portletTimer.setCollapsible(true);

		return portletTimer;
	}

	private Portlet createPortletPlayer() {
		final Portlet portletPlayer = new Portlet();
		portletPlayer.setHeadingHtml("<span id='portletHeading'>Spieler</span>");
		portletPlayer.setId("portletBackground");
		portletPlayer.setPixelSize(300, 245);
		portletPlayer.setCollapsible(true);

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

	@Override
	public void updatePanel(UpdatePanelEvent event) {
		if (event.getActiveWidget() == 0) {
			portletTable.setHeadingHtml("<span id='portletHeading'>Aktuelle Spielertabelle (Einzelansicht)</span>");
		} else if (event.getActiveWidget() == 1) {
			portletTable.setHeadingHtml("<span id='portletHeading'>Aktuelle Spielertabelle (Teamansicht)</span>");
		} else {
			portletTable.setHeadingHtml("<span id='portletHeading'>Aktuelle Teamtabelle</span>");
		}
	}

}
