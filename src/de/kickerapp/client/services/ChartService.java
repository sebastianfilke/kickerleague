package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.dto.ChartDto;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die Schnittstelle zur Verarbeitung von Diagrammen im Klienten.
 * 
 * @author Sebastian Filke
 */
@RemoteServiceRelativePath("chartService")
public interface ChartService extends RemoteService {

	/**
	 * @param playerDto Der Spieler für welchen das Diagramm angezeigt werden soll.
	 * @return
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public ChartDto getSinglePlayerGoalChart(PlayerDto playerDto) throws IllegalArgumentException;

}
