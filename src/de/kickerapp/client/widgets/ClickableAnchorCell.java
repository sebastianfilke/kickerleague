package de.kickerapp.client.widgets;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.ResizeCell;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;

/**
 * Zelle, welche angeklickt werden kann.
 * 
 * @author Sebastian Filke
 */
public class ClickableAnchorCell extends ResizeCell<String> implements HasSelectHandlers {

	/**
	 * Erzeugt eine neue anklickbare Zelle.
	 */
	public ClickableAnchorCell() {
		super("click", "mouseover", "mouseout");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HandlerRegistration addSelectHandler(SelectEvent.SelectHandler handler) {
		return addHandler(handler, SelectEvent.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
		if (!isDisableEvents()) {
			final String eventType = event.getType();
			if ("click".equals(eventType)) {
				final XElement target = event.getEventTarget().cast();
				if (target.hasClassName("anchor")) {
					onClick(context);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		if (value != null && !value.equals("0")) {
			sb.appendHtmlConstant("<a class='anchor' style='cursor: pointer; font-weight: bold; text-decoration: underline;'>" + value + "</a>");
		} else {
			sb.appendHtmlConstant("<a class='anchor'>" + value + "</a>");
		}
	}

	/**
	 * Methode welche bei einem Klick auf das Element in der Zelle ausgeführt wird.
	 * 
	 * @param context Der Kontext der Zelle.
	 */
	protected void onClick(Context context) {
		if (!isDisableEvents() && fireCancellableEvent(context, new BeforeSelectEvent(context))) {
			fireEvent(context, new SelectEvent(context));
		}
	}

}
