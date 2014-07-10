package de.kickerapp.client.ui.resources.templates;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

import de.kickerapp.client.ui.resources.css.OpponentStyle;
import de.kickerapp.client.ui.resources.css.PagingStyle;
import de.kickerapp.shared.dto.ChartOpponentDto;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die Templates für die Applikation.
 * 
 * @author Sebastian Filke
 */
public interface KickerTemplates extends XTemplates {

	// CHECKSTYLE:OFF

	@XTemplate(source = "pagingComboBox.html")
	SafeHtml renderPlayerPagingComboBox(PlayerDto playerDto, PagingStyle style);

	@XTemplate(source = "opponentChartToolTip.html")
	SafeHtml renderOpponentToolTip(ChartOpponentDto chartOpponentDto, OpponentStyle style);

}
