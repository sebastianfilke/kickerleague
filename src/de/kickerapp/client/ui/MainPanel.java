package de.kickerapp.client.ui;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.NavigationEvent;
import de.kickerapp.client.event.NavigationEventHandler;
import de.kickerapp.client.event.ShowDataEvent;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.client.ui.controller.InsertPanel;
import de.kickerapp.client.ui.controller.MatchesPanel;
import de.kickerapp.client.ui.controller.TablesPanel;
import de.kickerapp.client.ui.controller.tab.ChartTabPanel;
import de.kickerapp.client.ui.controller.tab.PlayerTabPanel;
import de.kickerapp.client.ui.resources.IconProvider;
import de.kickerapp.client.ui.resources.MessageProvider;

/**
 * Basis-Layout zur Darstellung und Verarbeitung der Applikation.
 * 
 * @author Sebastian Filke
 */
public class MainPanel extends BaseContainer implements NavigationEventHandler {

	/** Der CardLayoutContainer für die Hauptansicht. */
	private CardLayoutContainer clcContent;
	/** Controller-Klasse für die Ansicht der Team- bzw. Spielertabellen. */
	private TablesPanel tablePanel;
	/** Controller-Klasse für die Ansicht der Spielergebnisse. */
	private MatchesPanel matchesPanel;
	/** Controller-Klasse zum Eintragen der Ergebnisse und Spieler eines Spiels. */
	private InsertPanel resultPanel;
	/** Controller-Klasse zum Anzeigen der Team- bzw. Spielerstatistiken. */
	private ChartTabPanel chartTabPanel;
	/** Controller-Klasse zum Eintragen neuer Spieler für die Applikation. */
	private PlayerTabPanel playerPanel;

	/**
	 * Erzeugt ein neues Basis-Layout zur Darstellung und Verarbeitung der Applikation.
	 */
	public MainPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		tablePanel = new TablesPanel();
		matchesPanel = new MatchesPanel();
		resultPanel = new InsertPanel();
		chartTabPanel = new ChartTabPanel();
		playerPanel = new PlayerTabPanel();

		final VerticalLayoutContainer vlcMain = new VerticalLayoutContainer();
		vlcMain.add(createHeader(), new VerticalLayoutData(1, 92));
		vlcMain.add(new NavigationPanel(), new VerticalLayoutData(1, 42));
		vlcMain.add(createContent(), new VerticalLayoutData(1, 1));

		add(vlcMain);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initHandlers() {
		AppEventBus.addHandler(NavigationEvent.TABLES, this);
		AppEventBus.addHandler(NavigationEvent.MATCHES, this);
		AppEventBus.addHandler(NavigationEvent.INSERT, this);
		AppEventBus.addHandler(NavigationEvent.CHART, this);
		AppEventBus.addHandler(NavigationEvent.PLAYER, this);
	}

	/**
	 * Erzeugt den Container für die Kopfleiste der Applikation.
	 * 
	 * @return Der erzeugte Container.
	 */
	private HtmlLayoutContainer createHeader() {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div id='header'><div id='headerIcon'>");
		sb.append(AbstractImagePrototype.create(IconProvider.get().soccer_ball()).getSafeHtml());
		sb.appendHtmlConstant("</div>");
		sb.appendHtmlConstant("<div id='headerText'>");
		sb.appendHtmlConstant(MessageProvider.get().applicationTitle());
		sb.appendHtmlConstant("</div><div>");

		final HtmlLayoutContainer htmlLcHeader = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcHeader.setId("headerBackground");

		return htmlLcHeader;
	}

	/**
	 * Erzeugt den Container für den Inhalt der Applikation.
	 * 
	 * @return Der erzeugte Container.
	 */
	private VerticalLayoutContainer createContent() {
		final VerticalLayoutContainer vlcContent = new VerticalLayoutContainer();
		vlcContent.setScrollMode(ScrollMode.AUTO);
		vlcContent.setId("contentBackground");

		final BorderLayoutContainer blcContent = new BorderLayoutContainer();
		blcContent.setId("content");

		clcContent = new CardLayoutContainer();
		clcContent.add(tablePanel);
		clcContent.add(matchesPanel);
		clcContent.add(resultPanel);
		clcContent.add(chartTabPanel);
		clcContent.add(playerPanel);

		blcContent.setCenterWidget(clcContent, new MarginData(5));

		vlcContent.add(blcContent, new VerticalLayoutData(1200, 1000));
		vlcContent.add(createFooter(), new VerticalLayoutData(1200, 50));

		return vlcContent;
	}

	/**
	 * Erzeugt den Container für die Fußleiste der Applikation.
	 * 
	 * @return Der erzeugte Container.
	 */
	private HtmlLayoutContainer createFooter() {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div id='footer'>");
		sb.appendHtmlConstant("<div id='footerLinks'>Impressum | Kontakt</div>");
		sb.appendHtmlConstant("<div id='footerText'>Design und Idee &#064; Sebastian Filke, 2014 | Version: 0.9.2</div>");
		sb.appendHtmlConstant("</div>");

		final HtmlLayoutContainer htmlLcTitle = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcTitle.setId("footerBackground");

		return htmlLcTitle;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void navigationPressed(final NavigationEvent navEvent) {
		if (navEvent.getAssociatedType() == NavigationEvent.TABLES) {
			clcContent.setActiveWidget(tablePanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.TABLES));
		} else if (navEvent.getAssociatedType() == NavigationEvent.MATCHES) {
			clcContent.setActiveWidget(matchesPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.MATCHES));
		} else if (navEvent.getAssociatedType() == NavigationEvent.INSERT) {
			clcContent.setActiveWidget(resultPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.INSERT));
		} else if (navEvent.getAssociatedType() == NavigationEvent.CHART) {
			clcContent.setActiveWidget(chartTabPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.CHART));
		} else if (navEvent.getAssociatedType() == NavigationEvent.PLAYER) {
			clcContent.setActiveWidget(playerPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.PLAYER));
		}
	}

}
