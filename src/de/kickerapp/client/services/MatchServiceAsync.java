package de.kickerapp.client.services;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.dto.MatchDto;

/**
 * Die asynchrone Schnittstelle zur Verarbeitung von Spielen im Klienten.
 * 
 * @author Sebastian Filke
 */
public interface MatchServiceAsync {

	/**
	 * Erzeugt ein neues Einzelspiel.
	 * 
	 * @param matchDto Das zu erstellende Spiel.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void createSingleMatch(MatchDto matchDto, AsyncCallback<MatchDto> callback) throws IllegalArgumentException;

	/**
	 * Erzeugt ein neues Doppelspiel.
	 * 
	 * @param matchDto Das zu erstellende Spiel.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void createDoubleMatch(MatchDto matchDto, AsyncCallback<MatchDto> callback) throws IllegalArgumentException;

	/**
	 * Liefert alle Spiele.
	 * 
	 * @param date Das Datum ab wann die Spiele geliefert werden sollen.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getAllMatchesFrom(Date date, AsyncCallback<ArrayList<MatchDto>> callback) throws IllegalArgumentException;

}
