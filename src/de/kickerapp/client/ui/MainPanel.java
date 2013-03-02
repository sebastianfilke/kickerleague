package de.kickerapp.client.ui;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.NavigationEvent;
import de.kickerapp.client.event.NavigationEventHandler;
import de.kickerapp.client.event.ShowDataEvent;
import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.ui.resources.KickerMessages;

/**
 * Basis-Layout zur Darstellung und Verarbeitung der Applikation.
 * 
 * @author Sebastian Filke
 */
public class MainPanel extends VerticalLayoutContainer implements NavigationEventHandler {

	private CardLayoutContainer clcContent;

	private TablesPanel tablePanel;

	private MatchesPanel matchesPanel;

	private InsertPanel resultPanel;

	private PlayerTabPanel playerPanel;

	public MainPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * 
	 */
	public void initLayout() {
		tablePanel = new TablesPanel();
		matchesPanel = new MatchesPanel();
		resultPanel = new InsertPanel();
		playerPanel = new PlayerTabPanel();

		add(createHeader(), new VerticalLayoutData(1, 92));
		add(new NavigationPanel(), new VerticalLayoutData(1, 42));
		add(createContent(), new VerticalLayoutData(1, 1));
	}

	/**
	 * 
	 */
	private void initHandlers() {
		AppEventBus.addHandler(NavigationEvent.TABLES, this);
		AppEventBus.addHandler(NavigationEvent.MATCHES, this);
		AppEventBus.addHandler(NavigationEvent.INSERT, this);
		AppEventBus.addHandler(NavigationEvent.PLAYER, this);
	}

	private HtmlLayoutContainer createHeader() {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div id='header'><div id='headerIcon'>");
		sb.append(AbstractImagePrototype.create(KickerIcons.ICON.soccerBall()).getSafeHtml());
		sb.appendHtmlConstant("</div>");
		sb.appendHtmlConstant("<div id='headerText'>");
		sb.appendHtmlConstant(KickerMessages.MAIN_PANEL.mainPanelTitle());
		sb.appendHtmlConstant("</div><div>");

		final HtmlLayoutContainer htmlLcHeader = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcHeader.setId("headerBackground");
		htmlLcHeader.setStateful(false);

		return htmlLcHeader;
	}

	private VerticalLayoutContainer createContent() {
		final VerticalLayoutContainer vlcContent = new VerticalLayoutContainer();
		vlcContent.setId("contentBackground");
		vlcContent.setScrollMode(ScrollMode.AUTO);

		final BorderLayoutContainer blcContent = new BorderLayoutContainer();
		blcContent.setId("content");

		clcContent = new CardLayoutContainer();
		clcContent.add(tablePanel);
		clcContent.add(matchesPanel);
		clcContent.add(resultPanel);
		clcContent.add(playerPanel);

		blcContent.setCenterWidget(clcContent, new MarginData(5));

		vlcContent.add(blcContent, new VerticalLayoutData(1200, 1000));
		vlcContent.add(createFooter(), new VerticalLayoutData(1200, 50));

		return vlcContent;
	}

	private HtmlLayoutContainer createFooter() {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div id='footer'>");
		sb.appendHtmlConstant("<div id='footerLinks'>Impressum | Kontakt</div>");
		sb.appendHtmlConstant("<div id='footerText'>Design und Idee &#064; Sebastian Filke, 2013 | Version: 0.7.8</div>");
		sb.appendHtmlConstant("</div>");

		final HtmlLayoutContainer htmlLcTitle = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcTitle.setId("footerBackground");
		htmlLcTitle.setStateful(false);

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
		} else if (navEvent.getAssociatedType() == NavigationEvent.PLAYER) {
			clcContent.setActiveWidget(playerPanel);
		}
		clcContent.forceLayout();
	}

}
