package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.dto.ChartDto;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die asynchrone Schnittstelle zur Verarbeitung von Diagrammen im Klienten.
 * 
 * @author Sebastian Filke
 */
public interface ChartServiceAsync {

	/**
	 * Liefert die Torstatistik für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen das Diagramm angezeigt werden soll.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getSinglePlayerGoalChart(PlayerDto playerDto, AsyncCallback<ChartDto> callback) throws IllegalArgumentException;

}
