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
import de.kickerapp.client.event.TabPanelEventHandler;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.client.ui.resources.TemplateProvider;

/**
 * Controller-Klasse für die Navigationsleiste der Applikation.
 * 
 * @author Sebastian Filke
 */
public class NavigationPanel extends BaseContainer implements TabPanelEventHandler {

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
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initLayout() {
		add(createHlcNavigation());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();

		AppEventBus.addHandler(TabPanelEvent.TABLES_NAV, this);
		AppEventBus.addHandler(TabPanelEvent.CHARTS_NAV, this);
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
				selectedSubElement = DOM.getElementById("singleTable");
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
		} else if (elementId.equals("singleTable")) {
			AppEventBus.fireEvent(new TabPanelEvent(TabPanelEvent.TABLES, 0));
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.TABLES));
		} else if (elementId.equals("doubleTable")) {
			AppEventBus.fireEvent(new TabPanelEvent(TabPanelEvent.TABLES, 1));
			AppEventBus.fireEvent(new NavigationEvent(NavigationEvent.TABLES));
		} else if (elementId.equals("teamTable")) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActiveWidget(TabPanelEvent event) {
		if (selectedSubElement != null) {
			selectedSubElement.removeClassName("active");
		}
		if (event.getAssociatedType() == TabPanelEvent.TABLES_NAV) {
			if (event.getActiveTab() == 0) {
				selectedSubElement = DOM.getElementById("singleTable");
			} else if (event.getActiveTab() == 1) {
				selectedSubElement = DOM.getElementById("doubleTable");
			} else if (event.getActiveTab() == 2) {
				selectedSubElement = DOM.getElementById("teamTable");
			}
		} else if (event.getAssociatedType() == TabPanelEvent.CHARTS_NAV) {
			if (event.getActiveTab() == 0) {
				selectedSubElement = DOM.getElementById("singleChart");
			} else if (event.getActiveTab() == 1) {
				selectedSubElement = DOM.getElementById("doubleChart");
			} else if (event.getActiveTab() == 2) {
				selectedSubElement = DOM.getElementById("teamChart");
			}
		}
		selectedSubElement.addClassName("active");
	}

}
