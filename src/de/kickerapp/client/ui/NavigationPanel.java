package de.kickerapp.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.NavigationEvent;
import de.kickerapp.client.event.NavigationEventHandler;
import de.kickerapp.client.ui.base.BaseContainer;

/**
 * Controller-Klasse für die Navigationsleiste der Applikation.
 * 
 * @author Sebastian Filke
 */
public class NavigationPanel extends BaseContainer {

	/** Der Navigationspunkt für Tabellen. */
	private Label lTables;
	/** Der Navigationspunkt für Ergebnisse. */
	private Label lResults;
	/** Der Navigationspunkt für das Eintragen von Spielen. */
	private Label lInsert;
	/** Der Navigationspunkt für Spieler und Spielerstatistiken. */
	private Label lPlayer;
	/** Der momentan selektierte Navigationspunkt. */
	private Label lSelected;

	/**
	 * Erzeugt einen neuen Controller für die Navigationsleiste der Applikation.
	 */
	public NavigationPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		add(createHlcNavigation());
	}

	/**
	 * Erstellt die Navigationsleiste.
	 * 
	 * @return Die erstellt Navigationsleiste.
	 */
	private HtmlLayoutContainer createHlcNavigation() {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div id='tabs26'><ul>");
		sb.appendHtmlConstant("<li><div class='lTables'></div></li>");
		sb.appendHtmlConstant("<li><div class='lResults'></div></li>");
		sb.appendHtmlConstant("<li><div class='lInsert'></div></li>");
		sb.appendHtmlConstant("<li><div class='lPlayer'></div></li>");
		sb.appendHtmlConstant("</ul></div>");

		final HtmlLayoutContainer htmlLcNavigation = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcNavigation.setStateful(false);

		lTables = createNavigationLabel("Tabellen", NavigationEvent.TABLES);
		lSelected = lTables;
		lSelected.setStyleName("current", true);

		lResults = createNavigationLabel("Ergebnisse", NavigationEvent.MATCHES);
		lInsert = createNavigationLabel("Spiel eintragen", NavigationEvent.INSERT);
		lPlayer = createNavigationLabel("Spieler", NavigationEvent.PLAYER);

		htmlLcNavigation.add(lTables, new HtmlData(".lTables"));
		htmlLcNavigation.add(lResults, new HtmlData(".lResults"));
		htmlLcNavigation.add(lInsert, new HtmlData(".lInsert"));
		htmlLcNavigation.add(lPlayer, new HtmlData(".lPlayer"));

		return htmlLcNavigation;
	}

	/**
	 * Erstellt einen Navigationspunkt für die Navigationsleiste.
	 * 
	 * @param text Der Text des Navigationspunktes als <code>String</code>.
	 * @param navEvent Der Typ des NavigationsEvents welches ausgelöst werden soll.
	 * @return Der erstellte Navigationspunkt.
	 */
	private Label createNavigationLabel(final String text, final Type<NavigationEventHandler> navEvent) {
		final Label lNavigation = new Label(text);
		lNavigation.setStyleName("label", true);
		lNavigation.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppEventBus.fireEvent(new NavigationEvent(navEvent));
				lSelected.setStyleName("current", false);
				lSelected = lNavigation;
				lSelected.setStyleName("current", true);
			}
		});
		return lNavigation;
	}

}
