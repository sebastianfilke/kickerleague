package de.kickerapp.client.services;

import java.util.Date;

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
	 * @param date Das Jahr für welches die Informationen angezeigt werden sollen.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getSinglePlayerInfo(PlayerDto playerDto, Date date, AsyncCallback<InfoDto> callback) throws IllegalArgumentException;

	/**
	 * Liefert die Einzelspieler-Statistiken für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Einzelspieler-Statistiken angezeigt werden sollen.
	 * @param date Das Jahr für welches die Einzelspieler-Statistiken angezeigt werden sollen.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getSinglePlayerChart(PlayerDto playerDto, Date date, AsyncCallback<ChartContainer> callback) throws IllegalArgumentException;

}
