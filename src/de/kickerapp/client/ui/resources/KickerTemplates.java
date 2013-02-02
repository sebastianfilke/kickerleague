package de.kickerapp.client.ui.resources;

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
	 * Das Template zum Anzeigen eines Spielers mitsamt seinen Informationen bei
	 * der Spielersuche.
	 * 
	 * @param player Der gefundene Spieler.
	 * @return Das Template.
	 */
	@XTemplate("<div class='templatePagingComboBox'><h3><span>Letztes Spiel:</br>{player.lastMatchDate:date(\"dd.MM.yyyy HH:mm\")}</span>{player.firstName}, {player.lastName} ({player.nickName})</h3>"
			+ "<span>Siege: <font color='green'>{player.singleWins}</font> &#183; Niederlagen: <font color='red'>{player.singleLosses}</font></span></div>")
	SafeHtml renderPlayerPagingComboBox(PlayerDto player);

}
