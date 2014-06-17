package de.kickerapp.client.ui.resources.templates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die Templates für die Applikation.
 * 
 * @author Sebastian Filke
 */
public interface KickerTemplates extends XTemplates {

	/** Die Templates. */
	public final KickerTemplates TEMPLATE = GWT.create(KickerTemplates.class);

	/**
	 * Das Template zum Anzeigen eines Spielers mitsamt seinen Informationen bei der Spielersuche.
	 * 
	 * @param playerDto Der gefundene Spieler.
	 * @return Das Template.
	 */
	@XTemplate(source = "pagingComboBox.html")
	SafeHtml renderPlayerPagingComboBox(PlayerDto playerDto);

}
