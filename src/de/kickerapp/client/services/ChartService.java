package de.kickerapp.client.services;

import java.util.Date;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.container.ChartContainer;
import de.kickerapp.shared.dto.InfoDto;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die Schnittstelle zur Verarbeitung von Diagrammen im Klienten.
 * 
 * @author Sebastian Filke
 */
@RemoteServiceRelativePath("chartService")
public interface ChartService extends RemoteService {

	/**
	 * Liefert die Einzelspieler-Informationen für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Informationen angezeigt werden sollen.
	 * @param year Das Jahr für welches die Informationen angezeigt werden sollen.
	 * @return Die Informationen.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public InfoDto getSinglePlayerInfo(PlayerDto playerDto, Integer year) throws IllegalArgumentException;

	/**
	 * Liefert die Einzelspieler-Statistiken für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Einzelspieler-Statistiken angezeigt werden sollen.
	 * @param year Das Jahr für welches die Einzelspieler-Statistiken angezeigt werden sollen.
	 * @return Die Einzelspieler-Statistiken.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public ChartContainer getSinglePlayerChart(PlayerDto playerDto, Integer year) throws IllegalArgumentException;

	/**
	 * Liefert die Doppelspieler-Informationen für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Informationen angezeigt werden sollen.
	 * @param year Das Jahr für welches die Informationen angezeigt werden sollen.
	 * @return Die Informationen.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public InfoDto getDoublePlayerInfo(PlayerDto playerDto, Integer year) throws IllegalArgumentException;

	/**
	 * Liefert die Doppelspieler-Statistiken für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Doppelspieler-Statistiken angezeigt werden sollen.
	 * @param date Das Jahr für welches die Doppelspieler-Statistiken angezeigt werden sollen.
	 * @return Die Einzelspieler-Statistiken.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public ChartContainer getDoublePlayerChart(PlayerDto playerDto, Date date) throws IllegalArgumentException;

}
