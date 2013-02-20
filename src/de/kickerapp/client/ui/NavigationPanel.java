package de.kickerapp.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.NavigationEvent;

public class NavigationPanel extends SimpleContainer {

	private Label label1;

	private Label label2;

	private Label label3;

	private Label label4;

	private Label current;

	/**
	 * Erzeugt einen neuen Controller zum Eintragen neuer Spieler für die
	 * Applikation.
	 */
	public NavigationPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	public void initLayout() {
		add(createHeader());
	}

	private HtmlLayoutContainer createHeader() {
		final SafeHtmlBuilder sb = new SafeHtmlBuilder();

		sb.appendHtmlConstant("<div id='tabs26'><ul>");
		sb.appendHtmlConstant("<li><div class='label1'></div></li>");
		sb.appendHtmlConstant("<li><div class='label2'></div></li>");
		sb.appendHtmlConstant("<li><div class='label3'></div></li>");
		sb.appendHtmlConstant("<li><div class='label4'></div></li>");
		sb.appendHtmlConstant("</ul></div>");

		final HtmlLayoutContainer htmlLcHeader = new HtmlLayoutContainer(sb.toSafeHtml());
		htmlLcHeader.setStateful(false);

		label1 = new Label("Tabelle");
		label1.setStyleName("label", true);
		label1.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.TABLES));
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
				AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.MATCHES));
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
				AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.INSERT));
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
				AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.PLAYER));
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

}
