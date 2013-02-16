package de.kickerapp.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.NavigationEvent;
import de.kickerapp.client.event.NavigationEventHandler;
import de.kickerapp.client.event.ShowDataEvent;
import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.event.UpdatePanelEventHandler;
import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.ui.resources.KickerMessages;

/**
 * Basis-Controller zur Darstellung und Verarbeitung der Applikation.
 * 
 * @author Sebastian Filke
 */
public class MainPanel extends VerticalLayoutContainer implements NavigationEventHandler, UpdatePanelEventHandler {

	private TablePanel tablePanel;

	private MatchesPanel matchesPanel;

	private ResultPanel resultPanel;

	private PlayerPanel playerPanel;

	private CardLayoutContainer clcContent;

	private Label label1;
	private Label label2;
	private Label label3;
	private Label label4;

	private Label current;

	public MainPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	public void initLayout() {
		setId("transparentBackground");
		setBorders(false);

		tablePanel = new TablePanel();
		matchesPanel = new MatchesPanel();
		resultPanel = new ResultPanel();
		playerPanel = new PlayerPanel();

		clcContent = new CardLayoutContainer();
		clcContent.add(tablePanel);
		clcContent.add(matchesPanel);
		clcContent.add(resultPanel);
		clcContent.add(playerPanel);

		AppEventBus.fireEvent(new ShowDataEvent(ShowDataEvent.TABLE_PANEL));

		AppEventBus.addHandler(NavigationEvent.TABLE, this);
		AppEventBus.addHandler(NavigationEvent.RESULT, this);
		AppEventBus.addHandler(NavigationEvent.INPUT, this);
		AppEventBus.addHandler(NavigationEvent.PLAYER, this);
		AppEventBus.addHandler(UpdatePanelEvent.TYPE, this);

		add(createPageHeader(), new VerticalLayoutData(1, 92));
		add(createPageTitle(), new VerticalLayoutData(1, 0));
		add(createHeader(), new VerticalLayoutData(1, 42));
		add(new Label(), new VerticalLayoutData(1, 20));
		add(createContentLayout(), new VerticalLayoutData(1, 1000));
		add(createFooter(), new VerticalLayoutData(1, 80, new Margins(60, 0, 0, 0)));
	}

	private HtmlLayoutContainer createPageHeader() {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div></div>");

		final HtmlLayoutContainer htmlLcHeader = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcHeader.setId("pageHeaderBackground");
		htmlLcHeader.setStateful(false);

		return htmlLcHeader;
	}

	private HtmlLayoutContainer createHeader() {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div id='tabs26'>" + "<ul><li><div class='label1'></div></li>" + "<li><div class='label2'></div></li> "
				+ "<li><div class='label3'></div></li>" + "<li><div class='label4'></div></li>" + "</ul></div>");

		final HtmlLayoutContainer htmlLcHeader = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcHeader.setStateful(false);

		label1 = new Label("Tabelle");
		label1.setStyleName("label", true);
		label1.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				current.setStyleName("current", false);
				current = label1;
				current.setStyleName("current", true);
			}
		});
		current = label1;
		current.setStyleName("current", true);
		label2 = new Label("Ergebnisse");
		label2.setStyleName("label", true);
		label2.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				current.setStyleName("current", false);
				current = label2;
				current.setStyleName("current", true);
			}
		});

		label3 = new Label("Spiel eintragen");
		label3.setStyleName("label", true);
		label3.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				current.setStyleName("current", false);
				current = label3;
				current.setStyleName("current", true);
			}
		});
		label4 = new Label("Spieler");
		label4.setStyleName("label", true);
		label4.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				current.setStyleName("current", false);
				current = label4;
				current.setStyleName("current", true);
			}
		});

		htmlLcHeader.add(label1, new HtmlData(".label1"));
		htmlLcHeader.add(label2, new HtmlData(".label2"));
		htmlLcHeader.add(label3, new HtmlData(".label3"));
		htmlLcHeader.add(label4, new HtmlData(".label4"));

		return htmlLcHeader;
	}

	private CenterLayoutContainer createPageTitle() {
		final CenterLayoutContainer clcPageTitle = new CenterLayoutContainer();

		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div id='header'><div id='headerIcon'>");
		sb.append(AbstractImagePrototype.create(KickerIcons.ICON.soccerBall()).getSafeHtml());
		sb.appendHtmlConstant("</div>");
		sb.appendHtmlConstant("<div id='headerTitle'>" + KickerMessages.MAIN_PANEL.mainPanelTitle() + "</div><div>");

		final HtmlLayoutContainer htmlLcTitle = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcTitle.setId("transparentBackground");
		htmlLcTitle.setStateful(false);

		final SimpleContainer panel = new SimpleContainer();
		panel.setSize("1200px", "200px");

		panel.add(htmlLcTitle);

		clcPageTitle.add(panel);

		return clcPageTitle;
	}

	private CenterLayoutContainer createContentLayout() {
		final CenterLayoutContainer clcContentMain = new CenterLayoutContainer();

		final SimpleContainer panel = new SimpleContainer();
		panel.setSize("1200px", "1000px");

		final BorderLayoutContainer blcContent = new BorderLayoutContainer();
		blcContent.setId("contentBackground");
		blcContent.setBorders(true);

		final NavigationPanel cpNavigation = new NavigationPanel();

		final BorderLayoutData westData = new BorderLayoutData(280);
		westData.setCollapsible(true);
		westData.setMargins(new Margins(5, 0, 5, 5));
		final MarginData centerData = new MarginData(5);

		blcContent.setWestWidget(cpNavigation, westData);
		blcContent.setCenterWidget(clcContent, centerData);

		panel.add(blcContent);
		clcContentMain.add(panel);

		return clcContentMain;
	}

	private CenterLayoutContainer createFooter() {
		final CenterLayoutContainer clcPageFooter = new CenterLayoutContainer();

		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div id='footer'>Copyright &copy; 2013 Sebastian Filke, All Rights Reserved</div>");

		final HtmlLayoutContainer htmlLcTitle = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcTitle.setId("transparentBackground");
		htmlLcTitle.setStateful(false);

		final SimpleContainer panel = new SimpleContainer();

		panel.add(htmlLcTitle);

		clcPageFooter.add(panel);

		return clcPageFooter;
	}

	@Override
	public void navigationPressed(final NavigationEvent navEvent) {
		// final Fx fx = new Fx();
		// fx.addAfterAnimateHandler(new AfterAnimateHandler() {
		// @Override
		// public void onAfterAnimate(AfterAnimateEvent event) {
		// blcContent.getCenterWidget().removeFromParent();
		// if (navEvent.getAssociatedType() == NavigationEvent.TABLE) {
		// lastSelectedPanel = tablePanel;
		// blcContent.setCenterWidget(tablePanel, centerData);
		// } else if (navEvent.getAssociatedType() == NavigationEvent.RESULT) {
		// lastSelectedPanel = matchesPanel;
		// blcContent.setCenterWidget(matchesPanel, centerData);
		// } else if (navEvent.getAssociatedType() == NavigationEvent.INPUT) {
		// lastSelectedPanel = resultPanel;
		// blcContent.setCenterWidget(resultPanel, centerData);
		// } else if (navEvent.getAssociatedType() == NavigationEvent.PLAYER) {
		// lastSelectedPanel = playerPanel;
		// blcContent.setCenterWidget(playerPanel, centerData);
		// }
		// blcContent.forceLayout();
		// //fx.run(new FadeIn(lastSelectedPanel.getElement().<FxElement>
		// cast()));
		// }
		// });
		if (navEvent.getAssociatedType() == NavigationEvent.TABLE) {
			clcContent.setActiveWidget(tablePanel);
		} else if (navEvent.getAssociatedType() == NavigationEvent.RESULT) {
			clcContent.setActiveWidget(matchesPanel);
		} else if (navEvent.getAssociatedType() == NavigationEvent.INPUT) {
			clcContent.setActiveWidget(resultPanel);
		} else if (navEvent.getAssociatedType() == NavigationEvent.PLAYER) {
			clcContent.setActiveWidget(playerPanel);
		}
		// lastSelectedPanel.getElement().<FxElement> cast().fadeToggle(fx);
	}

	@Override
	public void updatePanel(UpdatePanelEvent event) {
		if (event.getActiveWidget() == 0) {
			tablePanel.setHeadingHtml("<span id='portletHeading'>Aktuelle Spielertabelle (Einzelansicht)</span>");
		} else if (event.getActiveWidget() == 1) {
			tablePanel.setHeadingHtml("<span id='portletHeading'>Aktuelle Spielertabelle (Teamansicht)</span>");
		} else {
			tablePanel.setHeadingHtml("<span id='portletHeading'>Aktuelle Teamtabelle</span>");
		}
	}

}
