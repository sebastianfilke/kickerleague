package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.dto.TeamDto;

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
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	ArrayList<TeamDto> getAllTeams() throws IllegalArgumentException;

}
