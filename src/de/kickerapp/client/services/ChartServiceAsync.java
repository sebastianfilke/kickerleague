package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.dto.ChartGoalDataDto;
import de.kickerapp.shared.dto.InfoDto;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die asynchrone Schnittstelle zur Verarbeitung von Diagrammen im Klienten.
 * 
 * @author Sebastian Filke
 */
public interface ChartServiceAsync {

	public void getSinglePlayerInfo(PlayerDto playerDto, AsyncCallback<InfoDto> callback) throws IllegalArgumentException;

	/**
	 * Liefert die Torstatistik für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen das Diagramm angezeigt werden soll.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getSinglePlayerGoalChart(PlayerDto playerDto, AsyncCallback<ArrayList<ChartGoalDataDto>> callback) throws IllegalArgumentException;

}
