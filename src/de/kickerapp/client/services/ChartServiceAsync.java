package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.container.ChartContainer;
import de.kickerapp.shared.dto.InfoDto;
import de.kickerapp.shared.dto.PlayerDto;

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
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getSinglePlayerInfo(PlayerDto playerDto, Integer year, AsyncCallback<InfoDto> callback) throws IllegalArgumentException;

	/**
	 * Liefert die Einzelspieler-Statistiken für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Einzelspieler-Statistiken angezeigt werden sollen.
	 * @param year Das Jahr für welches die Einzelspieler-Statistiken angezeigt werden sollen.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getSinglePlayerChart(PlayerDto playerDto, Integer year, AsyncCallback<ChartContainer> callback) throws IllegalArgumentException;

	/**
	 * Liefert die Doppelspieler-Informationen für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Informationen angezeigt werden sollen.
	 * @param year Das Jahr für welches die Informationen angezeigt werden sollen.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getDoublePlayerInfo(PlayerDto playerDto, Integer year, AsyncCallback<InfoDto> callback) throws IllegalArgumentException;

	/**
	 * Liefert die Doppelspieler-Statistiken für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Doppelspieler-Statistiken angezeigt werden sollen.
	 * @param year Das Jahr für welches die Doppelspieler-Statistiken angezeigt werden sollen.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getDoublePlayerChart(PlayerDto playerDto, Integer year, AsyncCallback<ChartContainer> callback) throws IllegalArgumentException;

}
