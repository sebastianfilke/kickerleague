package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.dto.MatchDto;

/**
 * Die asynchrone Schnittstelle zur Verarbeitung von Spielen im Clienten.
 * 
 * @author Sebastian Filke
 */
public interface MatchServiceAsync {

	/**
	 * Erzeugt ein neues Spiel.
	 * 
	 * @param matchDto Das zu erstellende Spiel.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void createMatch(MatchDto matchDto, AsyncCallback<MatchDto> callback) throws IllegalArgumentException;

	/**
	 * Liefert alle Spiele.
	 * 
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getAllMatches(AsyncCallback<ArrayList<MatchDto>> callback) throws IllegalArgumentException;

}
