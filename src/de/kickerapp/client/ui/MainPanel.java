package de.kickerapp.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.ShowDataEvent;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.client.ui.controller.InsertPanel;
import de.kickerapp.client.ui.controller.MatchesPanel;
import de.kickerapp.client.ui.controller.TablesPanel;
import de.kickerapp.client.ui.controller.tab.ChartTabPanel;
import de.kickerapp.client.ui.controller.tab.PlayerTabPanel;
import de.kickerapp.client.ui.navigation.NavigationElement;
import de.kickerapp.client.ui.navigation.NavigationPanel;
import de.kickerapp.client.ui.resources.IconProvider;
import de.kickerapp.client.ui.resources.MessageProvider;
import de.kickerapp.client.ui.token.Tokenizer;
import de.kickerapp.client.ui.util.AppInfo;

/**
 * Basis-Layout zur Darstellung und Verarbeitung der Applikation.
 * 
 * @author Sebastian Filke
 */
public class MainPanel extends BaseContainer {

	/** Der CardLayoutContainer für die Hauptansicht. */
	private CardLayoutContainer clcContent;
	/** Controller-Klasse für die Ansicht der Team- bzw. Spielertabellen. */
	private TablesPanel tablesPanel;
	/** Controller-Klasse für die Ansicht der Spielergebnisse. */
	private MatchesPanel matchesPanel;
	/** Controller-Klasse zum Eintragen der Ergebnisse und Spieler eines Spiels. */
	private InsertPanel insertPanel;
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
	protected void initLayout() {
		tablesPanel = new TablesPanel();
		matchesPanel = new MatchesPanel();
		insertPanel = new InsertPanel();
		chartTabPanel = new ChartTabPanel();
		playerPanel = new PlayerTabPanel();

		final VerticalLayoutContainer vlcMain = new VerticalLayoutContainer();
		vlcMain.add(createHeader(), new VerticalLayoutData(1, 92));
		vlcMain.add(new NavigationPanel(), new VerticalLayoutData(1, 54));
		vlcMain.add(createContent(), new VerticalLayoutData(1, 1));

		add(vlcMain);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				handleHistoryChange(event);
			}
		});
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
		clcContent.add(tablesPanel);
		clcContent.add(matchesPanel);
		clcContent.add(insertPanel);
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
		sb.appendHtmlConstant("<div id='footerLeft'>Impressum | Kontakt</div>");
		sb.appendHtmlConstant("<div id='footerText'>Design und Idee &#064; Sebastian Filke, 2013-15 | Version: 1.0.2</div>");
		sb.appendHtmlConstant("</div>");

		final HtmlLayoutContainer htmlLcTitle = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcTitle.setId("footerBackground");

		return htmlLcTitle;
	}

	/**
	 * Überprüft welches Navigationselement selektiert wurde und wirft das entsprechende Event.
	 * 
	 * @param event Das {@link ValueChangeEvent} mit veränderter URL.
	 */
	private void handleHistoryChange(ValueChangeEvent<String> event) {
		final Tokenizer tokenizer = new Tokenizer(event.getValue());

		switch (tokenizer.getNavigationElement()) {
		case TABLES:
			clcContent.setActiveWidget(tablesPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.TABLES));
			break;
		case SINGLETABLE:
			clcContent.setActiveWidget(tablesPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.TABLES, NavigationElement.SINGLETABLE));
			break;
		case DOUBLETABLE:
			clcContent.setActiveWidget(tablesPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.TABLES, NavigationElement.DOUBLETABLE));
			break;
		case TEAMTABLE:
			clcContent.setActiveWidget(tablesPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.TABLES, NavigationElement.TEAMTABLE));
			break;
		case RESULTS:
			clcContent.setActiveWidget(matchesPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.RESULTS));
			break;
		case INSERT:
			clcContent.setActiveWidget(insertPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.INSERT));
			break;
		case CHARTS:
			clcContent.setActiveWidget(chartTabPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.CHARTS));
			break;
		case SINGLECHART:
			clcContent.setActiveWidget(chartTabPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.CHARTS, NavigationElement.SINGLECHART));
			break;
		case DOUBLECHART:
			clcContent.setActiveWidget(chartTabPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.CHARTS, NavigationElement.DOUBLECHART));
			break;
		case TEAMCHART:
			clcContent.setActiveWidget(chartTabPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.CHARTS, NavigationElement.TEAMCHART));
			break;
		case PLAYER:
			clcContent.setActiveWidget(playerPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.PLAYER));
			break;
		case UNKOWN:
			AppInfo.showInfo("Ups...", "Die eingegebene URL kenn ich nicht!");
			break;
		default:
			clcContent.setActiveWidget(tablesPanel);
			AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.TABLES));
			break;
		}
	}

}
