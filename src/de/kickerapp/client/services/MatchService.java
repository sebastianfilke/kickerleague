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
	 * Erzeugt ein neues Spiel.
	 * 
	 * @param matchDto Das zu erstellende Spiel.
	 * @return Das neu erstellte Spiel.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public MatchDto createMatch(MatchDto matchDto) throws IllegalArgumentException;

	/**
	 * Liefert alle Spiele.
	 * 
	 * @return Alle Spiel.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public ArrayList<MatchDto> getAllMatches() throws IllegalArgumentException;

}
