package de.kickerapp.client.services;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die Schnittstelle zur Verarbeitung der Spieler im Klienten.
 * 
 * @author Sebastian Filke
 */
@RemoteServiceRelativePath("playerService")
public interface PlayerService extends RemoteService {

	/**
	 * Erzeugt einen neuen Spieler.
	 * 
	 * @param playerDto Der zu erstellende Spieler.
	 * @return Der neu erstellte Spieler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public PlayerDto createPlayer(PlayerDto playerDto) throws IllegalArgumentException;

	/**
	 * Aktualisiert den Spieler.
	 * 
	 * @param playerDto Der zu aktualisierende Spieler.
	 * @return Der aktualisierte Spieler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public PlayerDto updatePlayer(PlayerDto playerDto) throws IllegalArgumentException;

	/**
	 * Liefert alle Spieler inklusive Statistiken.
	 * 
	 * @param matchType Der Typ des Spiels zum Laden der Statistiken.
	 * @return Alle Spieler inklusive Statistiken.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public ArrayList<PlayerDto> getAllPlayers(MatchType matchType) throws IllegalArgumentException;

	/**
	 * Liefert alle Spieler mit mindestens einem Spiel.
	 * 
	 * @return Alle Spieler mit mindestens einem Spiel.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public HashMap<Integer, ArrayList<PlayerDto>> getPlayerYearAggregation() throws IllegalArgumentException;

}
