package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die asynchrone Schnittstelle zur Verarbeitung der Spieler im Clienten.
 * 
 * @author Sebastian Filke
 */
public interface PlayerServiceAsync {

	/**
	 * Erzeugt einen neuen Spieler.
	 * 
	 * @param playerDto Der zu erstellende Spieler.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void createPlayer(PlayerDto playerDto, AsyncCallback<PlayerDto> callback) throws IllegalArgumentException;

	/**
	 * Aktualisiert den Spieler.
	 * 
	 * @param playerDto Der zu aktualisierende Spieler.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void updatePlayer(PlayerDto playerDto, AsyncCallback<PlayerDto> callback) throws IllegalArgumentException;

	/**
	 * Liefert alle Spieler inklusive Statistiken.
	 * 
	 * @param matchType Der Typ des Spiels zum Laden der Statistiken.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getAllPlayers(MatchType matchType, AsyncCallback<ArrayList<PlayerDto>> callback) throws IllegalArgumentException;

}
