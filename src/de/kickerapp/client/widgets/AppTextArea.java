package de.kickerapp.client.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.sencha.gxt.widget.core.client.form.TextArea;

/**
 * Erweiterung der {@link TextArea} für die Applikation.
 * 
 * @author Sebastian Filke
 */
public class AppTextArea extends TextArea {

	/**
	 * Erzeugt eine neue TextArea.
	 */
	public AppTextArea() {
		super();
		addAttachHandler(new Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				final AppTextArea textArea = (AppTextArea) event.getSource();
				final NodeList<Element> elementsByTagName = textArea.getElement().getElementsByTagName(TextAreaElement.TAG);
				for (int i = 0; i < elementsByTagName.getLength(); i++) {
					final Element element = elementsByTagName.getItem(i);
					element.addClassName("textArea");
				}
			}
		});
	}

}
