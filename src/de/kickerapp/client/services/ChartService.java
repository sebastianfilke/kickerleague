package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.container.ChartContainer;
import de.kickerapp.shared.dto.InfoDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;
import de.kickerapp.shared.exception.KickerLeagueException;

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
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	InfoDto getSinglePlayerInfo(PlayerDto playerDto, Integer year) throws KickerLeagueException;

	/**
	 * Liefert die Einzelspieler-Statistiken für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Einzelspieler-Statistiken angezeigt werden sollen.
	 * @param year Das Jahr für welches die Einzelspieler-Statistiken angezeigt werden sollen.
	 * @return Die Einzelspieler-Statistiken.
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	ChartContainer getSinglePlayerChart(PlayerDto playerDto, Integer year) throws KickerLeagueException;

	/**
	 * Liefert die Doppelspieler-Informationen für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Informationen angezeigt werden sollen.
	 * @param year Das Jahr für welches die Informationen angezeigt werden sollen.
	 * @return Die Informationen.
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	InfoDto getDoublePlayerInfo(PlayerDto playerDto, Integer year) throws KickerLeagueException;

	/**
	 * Liefert die Doppelspieler-Statistiken für den gewählten Spieler.
	 * 
	 * @param playerDto Der Spieler für welchen die Doppelspieler-Statistiken angezeigt werden sollen.
	 * @param year Das Jahr für welches die Doppelspieler-Statistiken angezeigt werden sollen.
	 * @return Die Einzelspieler-Statistiken.
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	ChartContainer getDoublePlayerChart(PlayerDto playerDto, Integer year) throws KickerLeagueException;

	/**
	 * Liefert die Teamspieler-Informationen für das gewählte Team.
	 * 
	 * @param teamDto Das Team für welchen die Informationen angezeigt werden sollen.
	 * @param year Das Jahr für welches die Informationen angezeigt werden sollen.
	 * @return Die Informationen.
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	InfoDto getTeamPlayerInfo(TeamDto teamDto, Integer year) throws KickerLeagueException;

	/**
	 * Liefert die Teamspieler-Statistiken für das gewählte Team.
	 * 
	 * @param teamDto Das Team für welches die Teamspieler-Statistiken angezeigt werden sollen.
	 * @param year Das Jahr für welches die Teamspieler-Statistiken angezeigt werden sollen.
	 * @return Die Teamspieler-Statistiken.
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	ChartContainer getTeamPlayerChart(TeamDto teamDto, Integer year) throws KickerLeagueException;

}
