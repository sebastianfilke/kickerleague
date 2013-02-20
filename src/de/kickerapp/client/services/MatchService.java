package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.dto.MatchDto;

/**
 * Die Schnittstelle zur Verarbeitung von Spielen im Clienten.
 * 
 * @author Sebastian Filke
 */
@RemoteServiceRelativePath("matchService")
public interface MatchService extends RemoteService {

	/**
	 * @param match
	 * @return
	 * @throws IllegalArgumentException
	 */
	public MatchDto createMatch(MatchDto match) throws IllegalArgumentException;

	public ArrayList<MatchDto> getAllMatches() throws IllegalArgumentException;

}
