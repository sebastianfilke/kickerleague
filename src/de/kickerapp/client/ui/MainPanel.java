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
 * Basis-Controller zur Darstellung und Verarbeitung der Applikation.
 * 
 * @author Sebastian Filke
 */
public class MainPanel extends VerticalLayoutContainer implements NavigationEventHandler {

	private TablesPanel tablePanel;

	private MatchesPanel matchesPanel;

	private InsertPanel resultPanel;

	private PlayerPanel playerPanel;

	private NavigationPanel navigationPanel;

	private CardLayoutContainer clcContent;

	public MainPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	public void initLayout() {
		setId("transparentBackground");
		setBorders(false);

		tablePanel = new TablesPanel();
		matchesPanel = new MatchesPanel();
		resultPanel = new InsertPanel();
		playerPanel = new PlayerPanel();
		navigationPanel = new NavigationPanel();

		clcContent = new CardLayoutContainer();
		clcContent.add(tablePanel);
		clcContent.add(matchesPanel);
		clcContent.add(resultPanel);
		clcContent.add(playerPanel);

		add(createPageHeader(), new VerticalLayoutData(1, 92));
		add(navigationPanel, new VerticalLayoutData(1, 42));
		add(createContentLayout(), new VerticalLayoutData(1, 1));
	}

	private void initHandlers() {
		AppEventBus.addHandler(NavigationEvent.TABLES, this);
		AppEventBus.addHandler(NavigationEvent.MATCHES, this);
		AppEventBus.addHandler(NavigationEvent.INPUT, this);
		AppEventBus.addHandler(NavigationEvent.PLAYER, this);
	}

	private HtmlLayoutContainer createPageHeader() {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div id='header'><div id='headerIcon'>");
		sb.append(AbstractImagePrototype.create(KickerIcons.ICON.soccerBall()).getSafeHtml());
		sb.appendHtmlConstant("</div>");
		sb.appendHtmlConstant("<div id='headerTitle'>" + KickerMessages.MAIN_PANEL.mainPanelTitle() + "</div><div>");

		final HtmlLayoutContainer htmlLcHeader = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcHeader.setId("pageHeaderBackground");
		htmlLcHeader.setStateful(false);

		return htmlLcHeader;
	}

	private VerticalLayoutContainer createContentLayout() {
		final VerticalLayoutContainer vlcContent = new VerticalLayoutContainer();
		vlcContent.setId("contentBackground");
		vlcContent.setScrollMode(ScrollMode.AUTOY);

		final BorderLayoutContainer blcContent = new BorderLayoutContainer();
		blcContent.setId("content");

		blcContent.setCenterWidget(clcContent, new MarginData(5));

		vlcContent.add(blcContent, new VerticalLayoutData(1200, 1000));
		vlcContent.add(createFooter(), new VerticalLayoutData(1200, 80));

		return vlcContent;
	}

	private HtmlLayoutContainer createFooter() {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div id='footerTitle'>Design und Idee von Sebastian Filke, 2013<br/>Impressum &bull; Kontakt<br/>Version: 0.5.2</div>");

		final HtmlLayoutContainer htmlLcTitle = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcTitle.setId("pageFooterBackground");
		htmlLcTitle.setStateful(false);

		return htmlLcTitle;
	}

	@Override
	public void navigationPressed(final NavigationEvent navEvent) {
		if (navEvent.getAssociatedType() == NavigationEvent.TABLES) {
			clcContent.setActiveWidget(tablePanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.TABLES));
		} else if (navEvent.getAssociatedType() == NavigationEvent.MATCHES) {
			clcContent.setActiveWidget(matchesPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.MATCHES));
		} else if (navEvent.getAssociatedType() == NavigationEvent.INPUT) {
			clcContent.setActiveWidget(resultPanel);
		} else if (navEvent.getAssociatedType() == NavigationEvent.PLAYER) {
			clcContent.setActiveWidget(playerPanel);
		}
	}

}
