package de.kickerapp.client.services;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.dto.TeamDto;
import de.kickerapp.shared.exception.KickerLeagueException;

/**
 * Die Schnittstelle zur Verarbeitung der Teams im Klienten.
 * 
 * @author Sebastian Filke
 */
@RemoteServiceRelativePath("teamService")
public interface TeamService extends RemoteService {

	/**
	 * Liefert alle Teams inklusive Statistiken.
	 * 
	 * @return Die Teams inklusive Statistiken.
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	ArrayList<TeamDto> getAllTeams() throws KickerLeagueException;

	/**
	 * Liefert Aggregation von Jahr und Teams mit mindestens einem Spiel für Teamspiele.
	 * 
	 * @return Die Aggregation von Jahr und Teams mit mindestens einem Spiel.
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	HashMap<Integer, ArrayList<TeamDto>> getTeamMatchYearAggregation() throws KickerLeagueException;

}
