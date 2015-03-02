package de.kickerapp.client.ui.navigation;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.SelectNavigationElementEvent;
import de.kickerapp.client.event.SelectNavigationElementEventHandler;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.client.ui.resources.TemplateProvider;
import de.kickerapp.client.ui.token.Tokenizer;

/**
 * Controller-Klasse für die Navigationsleiste der Applikation.
 * 
 * @author Sebastian Filke
 */
public class NavigationPanel extends BaseContainer implements SelectNavigationElementEventHandler {

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
		addKeyDownHandler();
		add(createHlcNavigation());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();

		AppEventBus.addHandler(SelectNavigationElementEvent.TABLES, this);
		AppEventBus.addHandler(SelectNavigationElementEvent.CHARTS, this);

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				handleHistoryChange(event);
			}
		});
	}

	/**
	 * Fügt einen KeyDownListener hinzu.
	 */
	private void addKeyDownHandler() {
		RootPanel.get().addDomHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeEvent().getCtrlKey()) {
					final int keyCode = event.getNativeKeyCode();
					if (keyCode == KeyCodes.KEY_T) {
						handleKeyCode(event, NavigationElement.TABLES);
					} else if (keyCode == KeyCodes.KEY_R) {
						handleKeyCode(event, NavigationElement.RESULTS);
					} else if (keyCode == KeyCodes.KEY_I) {
						handleKeyCode(event, NavigationElement.INSERT);
					} else if (keyCode == KeyCodes.KEY_S) {
						handleKeyCode(event, NavigationElement.CHARTS);
					} else if (keyCode == KeyCodes.KEY_P) {
						handleKeyCode(event, NavigationElement.PLAYER);
					}
				}
			}
		}, KeyDownEvent.getType());
	}

	/**
	 * Führt die Methode aus falls Shift und eine entsprechende Taste gedrückt wurde.
	 * 
	 * @param event Das {@link KeyDownEvent}.
	 * @param navigationElement Das zu selektierende Element der Navigationsleiste.
	 */
	private void handleKeyCode(KeyDownEvent event, NavigationElement navigationElement) {
		event.stopPropagation();
		event.preventDefault();

		selectMainElement(navigationElement.getIdentificator());
		History.newItem(navigationElement.getIdentificator());
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
				selectedElement = DOM.getElementById(NavigationElement.TABLES.getIdentificator());
				selectedSubElement = DOM.getElementById(NavigationElement.SINGLETABLE.getIdentificator());
			}
		});
		htmlLcNavigation.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final Element clickedElement = DOM.eventGetTarget(Event.as(event.getNativeEvent()));
				if (clickedElement != null) {
					handleClickEvent(clickedElement);
					handleHistoryEvent(clickedElement);
				}
			}
		}, ClickEvent.getType());
		new QuickTip(htmlLcNavigation);

		return htmlLcNavigation;
	}

	/**
	 * Führt das entsprechende NavigationsEvent aus.
	 * 
	 * @param clickedElement Das selektierte Element in der Navigationsleiste.
	 */
	private void handleHistoryEvent(final Element clickedElement) {
		final Element element = getElement(clickedElement);
		final Element subElement = getSubElement(clickedElement);

		String elementId = element.getAttribute("id");
		if (subElement != null) {
			elementId = subElement.getAttribute("id");
		}

		final Tokenizer tokenizer = new Tokenizer(elementId);
		final NavigationElement navigationElement = tokenizer.getNavigationElement();

		History.newItem(navigationElement.getIdentificator());
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

		if (!"cssmenu".equals(elementId) && !"navigationBorder".equals(elementId)) {
			final Element selectedMediaElement = DOM.getElementById("ul-menu-button");
			if ("menu-button".equals(elementId)) {
				if (selectedMediaElement.hasClassName("open")) {
					DOM.getElementById("ul-menu-button").removeClassName("open");
				} else {
					DOM.getElementById("ul-menu-button").addClassName("open");
				}
			} else {
				selectedMediaElement.removeClassName("open");
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
	}

	/**
	 * Liefert das Element aus dem Hauptmenü.
	 * 
	 * @param clickedElement Das selektierte Element in der Navigationsleiste.
	 * @return Das Element aus dem Hauptmenü.
	 */
	private Element getElement(final Element clickedElement) {
		if (clickedElement.hasClassName("sub")) {
			return getParent(clickedElement, 3);
		} else {
			if ("menu-button".equals(clickedElement.getId())) {
				return clickedElement;
			} else {
				return getParent(clickedElement, 1);
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
			return getParent(clickedElement, 1);
		}
		return null;
	}

	/**
	 * Liefert das Vaterelement des selektierten Elements.
	 * 
	 * @param clickedElement Das selektierte Element in der Navigationsleiste.
	 * @param number Nach welchem Vater gesucht werden soll.
	 * @return Das Vaterelement.
	 */
	private Element getParent(Element clickedElement, int number) {
		Element parentElement = clickedElement;
		for (int i = 0; i < number; i++) {
			parentElement = parentElement.getParentElement();
		}
		return parentElement;
	}

	/**
	 * Überprüft welches Navigationselement selektiert wurde und selektiert es.
	 * 
	 * @param event Das {@link ValueChangeEvent} mit veränderter URL.
	 */
	private void handleHistoryChange(ValueChangeEvent<String> event) {
		final Tokenizer tokenizer = new Tokenizer(event.getValue());
		final NavigationElement navigationElement = tokenizer.getNavigationElement();

		if (navigationElement.getType() == NavigationType.MAIN) {
			selectMainElement(navigationElement.getIdentificator());
		}

		if (navigationElement.getType() == NavigationType.SUB) {
			selectMainElement(navigationElement.getParent().getIdentificator());
			selectSubElement(navigationElement.getIdentificator());
		}
	}

	/**
	 * Selektiert den Menü-Hauptpunkt.
	 * 
	 * @param identificator Der Identifikator für den Menü-Hauptpunkt, welcher zu selektieren ist.
	 */
	private void selectMainElement(final String identificator) {
		if (selectedElement != null) {
			selectedElement.removeClassName("active");
		}
		selectedElement = DOM.getElementById(identificator);
		selectedElement.addClassName("active");
	}

	/**
	 * Selektiert den Menü-Unterpunkt.
	 * 
	 * @param identificator Der Identifikator für den Menü-Unterpunkt, welcher zu selektieren ist.
	 */
	private void selectSubElement(final String identificator) {
		if (selectedSubElement != null) {
			selectedSubElement.removeClassName("active");
		}
		selectedSubElement = DOM.getElementById(identificator);
		selectedSubElement.addClassName("active");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selectElement(SelectNavigationElementEvent event) {
		final NavigationElement navigationElement = event.getNavigationElement();

		selectSubElement(navigationElement.getIdentificator());
	}

}
