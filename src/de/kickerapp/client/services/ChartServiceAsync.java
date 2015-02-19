package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.container.ChartContainer;
import de.kickerapp.shared.dto.InfoDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;

/**
 * Die asynchrone Schnittstelle zur Verarbeitung von Diagrammen im Klienten.
 * 
 * @author Sebastian Filke
 */
public interface ChartServiceAsync {

	/**
	 * Liefert die Einzelspieler-Informationen für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Informationen angezeigt werden sollen.
	 * @param year Das Jahr für welches die Informationen angezeigt werden sollen.
	 * @param callback Der Callback-Handler.
	 */
	void getSinglePlayerInfo(PlayerDto playerDto, Integer year, AsyncCallback<InfoDto> callback);

	/**
	 * Liefert die Einzelspieler-Statistiken für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Einzelspieler-Statistiken angezeigt werden sollen.
	 * @param year Das Jahr für welches die Einzelspieler-Statistiken angezeigt werden sollen.
	 * @param callback Der Callback-Handler.
	 */
	void getSinglePlayerChart(PlayerDto playerDto, Integer year, AsyncCallback<ChartContainer> callback);

	/**
	 * Liefert die Doppelspieler-Informationen für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Informationen angezeigt werden sollen.
	 * @param year Das Jahr für welches die Informationen angezeigt werden sollen.
	 * @param callback Der Callback-Handler.
	 */
	void getDoublePlayerInfo(PlayerDto playerDto, Integer year, AsyncCallback<InfoDto> callback);

	/**
	 * Liefert die Doppelspieler-Statistiken für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Doppelspieler-Statistiken angezeigt werden sollen.
	 * @param year Das Jahr für welches die Doppelspieler-Statistiken angezeigt werden sollen.
	 * @param callback Der Callback-Handler.
	 */
	void getDoublePlayerChart(PlayerDto playerDto, Integer year, AsyncCallback<ChartContainer> callback);

	/**
	 * Liefert die Teamspieler-Informationen für das gewählte Team.
	 * 
	 * @param teamDto Das Team für welches die Informationen angezeigt werden sollen.
	 * @param year Das Jahr für welches die Informationen angezeigt werden sollen.
	 * @param callback Der Callback-Handler.
	 */
	void getTeamPlayerInfo(TeamDto teamDto, Integer year, AsyncCallback<InfoDto> callback);

	/**
	 * Liefert die Teamspieler-Statistiken für das gewählte Team.
	 * 
	 * @param teamDto Das Team für welches die Teamspieler-Statistiken angezeigt werden sollen.
	 * @param year Das Jahr für welches die Teamspieler-Statistiken angezeigt werden sollen.
	 * @param callback Der Callback-Handler.
	 */
	void getTeamPlayerChart(TeamDto teamDto, Integer year, AsyncCallback<ChartContainer> callback);

}
