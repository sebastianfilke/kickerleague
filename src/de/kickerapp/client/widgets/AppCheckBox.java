package de.kickerapp.client.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.sencha.gxt.widget.core.client.form.CheckBox;

/**
 * Erweiterung der {@link CheckBox} für die Applikation.
 * 
 * @author Sebastian Filke
 */
public class AppCheckBox extends CheckBox {

	/**
	 * Erzeugt eine neue CheckBox.
	 */
	public AppCheckBox() {
		super();
		addAttachHandler(new Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				final AppCheckBox checkBox = (AppCheckBox) event.getSource();
				checkBox.addStyleName("checkBoxCon");
				final NodeList<Element> elementsByTagName = checkBox.getElement().getElementsByTagName(InputElement.TAG);
				for (int i = 0; i < elementsByTagName.getLength(); i++) {
					final Element element = elementsByTagName.getItem(i);
					element.addClassName("checkBox");
				}
			}
		});
	}

}
