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
	 * Das Template zum Anzeigen eines Spielers mitsamt seinen Informationen bei der Spielersuche.
	 * 
	 * @param playerDto Der gefundene Spieler.
	 * @return Das Template.
	 */
	@XTemplate("<div class='templatePagingComboBox'><h3><span>Letztes Spiel:</br>{playerDto.lastMatchDate:date(\"dd.MM.yyyy HH:mm\")}</span>{playerDto.firstName}, {playerDto.lastName} ({playerDto.nickName})</h3>"
			+ "<span>Einzel - Siege: <font color='green'>{playerDto.playerSingleStatsDto.wins}</font> &#183; Niederlagen: <font color='red'>{playerDto.playerSingleStatsDto.losses}</font></span> | "
			+ "<span>Doppel - Siege: <font color='green'>{playerDto.playerDoubleStatsDto.wins}</font> &#183; Niederlagen: <font color='red'>{playerDto.playerDoubleStatsDto.losses}</font></span></div>")
	SafeHtml renderPlayerPagingComboBox(PlayerDto playerDto);

}
