package de.kickerapp.client.ui;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.NavigationEvent;
import de.kickerapp.client.event.TabPanelEvent;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.client.ui.resources.TemplateProvider;

/**
 * Controller-Klasse für die Navigationsleiste der Applikation.
 * 
 * @author Sebastian Filke
 */
public class NavigationPanel extends BaseContainer {

	/** Der aktuell ausgewählte Navigationspunkt für das Hauptmenü. */
	private Element selectedElement;
	/** Der aktuell ausgewählte Navigationspunkt für ein Untermenü. */
	private Element selectedSubElement;

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
		final HtmlLayoutContainer htmlLcNavigation = new HtmlLayoutContainer(TemplateProvider.get().renderNavigation());
		htmlLcNavigation.setId("navigationBorder");
		htmlLcNavigation.addAttachHandler(new Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				selectedElement = DOM.getElementById("tables");
			}
		});
		htmlLcNavigation.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final Element clickedElement = DOM.eventGetTarget(Event.as(event.getNativeEvent()));
				if (clickedElement != null) {
					handleClickEvent(clickedElement);
					fireNavigationEvent(clickedElement);
				}
			}
		}, ClickEvent.getType());

		return htmlLcNavigation;
	}

	/**
	 * Führt das entsprechende NavigationsEvent aus.
	 * 
	 * @param clickedElement Das selektierte Element in der Navigationsleiste.
	 */
	private void fireNavigationEvent(final Element clickedElement) {
		final Element element = getElement(clickedElement);
		final Element subElement = getSubElement(clickedElement);

		String elementId = element.getAttribute("id");
		if (subElement != null) {
			elementId = subElement.getAttribute("id");
		}

		if (elementId.equals("tables")) {
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.TABLES));
		} else if (elementId.equals("singleTables")) {
			AppEventBus.fireEvent(new TabPanelEvent(TabPanelEvent.TABLES, 0));
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.TABLES));
		} else if (elementId.equals("doubleTables")) {
			AppEventBus.fireEvent(new TabPanelEvent(TabPanelEvent.TABLES, 1));
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.TABLES));
		} else if (elementId.equals("teamTables")) {
			AppEventBus.fireEvent(new TabPanelEvent(TabPanelEvent.TABLES, 2));
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.TABLES));
		} else if (elementId.equals("results")) {
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.MATCHES));
		} else if (elementId.equals("insert")) {
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.INSERT));
		} else if (elementId.equals("charts")) {
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.CHARTS));
		} else if (elementId.equals("singleChart")) {
			AppEventBus.fireEvent(new TabPanelEvent(TabPanelEvent.CHARTS, 0));
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.CHARTS));
		} else if (elementId.equals("doubleChart")) {
			AppEventBus.fireEvent(new TabPanelEvent(TabPanelEvent.CHARTS, 1));
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.CHARTS));
		} else if (elementId.equals("teamChart")) {
			AppEventBus.fireEvent(new TabPanelEvent(TabPanelEvent.CHARTS, 2));
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.CHARTS));
		} else if (elementId.equals("player")) {
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.PLAYER));
		}
	}

	/**
	 * Methode zum Verarbeiten des ClickEvents.
	 * 
	 * @param clickedElement Das selektierte Element in der Navigationsleiste.
	 */
	private void handleClickEvent(final Element clickedElement) {
		final Element element = getElement(clickedElement);
		final Element subElement = getSubElement(clickedElement);
		final String elementId = element.getAttribute("id");

		if (!elementId.equals("cssmenu") && !elementId.equals("navigationBorder")) {
			selectedElement.removeClassName("active");
			if (selectedSubElement != null) {
				selectedSubElement.removeClassName("active");
			}

			selectedElement = element;
			selectedElement.addClassName("active");
			if (subElement != null) {
				selectedSubElement = subElement;
				selectedSubElement.addClassName("active");
			}
		}
	}

	/**
	 * Liefert das Element aus dem Hauptmenü.
	 * 
	 * @param clickedElement Das selektierte Element in der Navigationsleiste.
	 * @return Das Element aus dem Hauptmenü.
	 */
	private Element getElement(final Element clickedElement) {
		if (clickedElement.hasClassName("sub")) {
			if (clickedElement.hasTagName(AnchorElement.TAG)) {
				return getParent(clickedElement, 3);
			} else {
				return getParent(clickedElement, 4);
			}
		} else {
			if (clickedElement.hasTagName(AnchorElement.TAG)) {
				return getParent(clickedElement, 1);
			} else {
				return getParent(clickedElement, 2);
			}
		}
	}

	/**
	 * Liefert das Element aus dem Untermenü.
	 * 
	 * @param clickedElement Das selektierte Element in der Navigationsleiste.
	 * @return Das Element aus dem Untermenü oder <code>null</code>.
	 */
	private Element getSubElement(final Element clickedElement) {
		if (clickedElement.hasClassName("sub")) {
			if (clickedElement.hasTagName(AnchorElement.TAG)) {
				return getParent(clickedElement, 1);
			} else {
				return getParent(clickedElement, 2);
			}
		}
		return null;
	}

	/**
	 * Liefert das Vaterlement des selektierten Elements.
	 * 
	 * @param clickedElement Das selektierte Element in der Navigationsleiste.
	 * @param number Nach welchem Vater gesucht werden soll.
	 * @return Das Vaterlement.
	 */
	private Element getParent(Element clickedElement, int number) {
		Element parentElement = clickedElement;
		for (int i = 0; i < number; i++) {
			parentElement = parentElement.getParentElement();
		}
		return parentElement;
	}

}
