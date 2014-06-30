package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.dto.ChartGoalDataDto;
import de.kickerapp.shared.dto.InfoDto;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die Schnittstelle zur Verarbeitung von Diagrammen im Klienten.
 * 
 * @author Sebastian Filke
 */
@RemoteServiceRelativePath("chartService")
public interface ChartService extends RemoteService {

	public InfoDto getSinglePlayerInfo(PlayerDto playerDto) throws IllegalArgumentException;

	/**
	 * Liefert die Torstatistik für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen das Diagramm angezeigt werden soll.
	 * @return Die Torstatistik.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public ArrayList<ChartGoalDataDto> getSinglePlayerGoalChart(PlayerDto playerDto) throws IllegalArgumentException;

}
