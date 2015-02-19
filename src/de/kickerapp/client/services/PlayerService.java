package de.kickerapp.client.services;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.exception.KickerLeagueException;

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
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	PlayerDto createPlayer(PlayerDto playerDto) throws KickerLeagueException;

	/**
	 * Aktualisiert den Spieler.
	 * 
	 * @param playerDto Der zu aktualisierende Spieler.
	 * @return Der aktualisierte Spieler.
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	PlayerDto updatePlayer(PlayerDto playerDto) throws KickerLeagueException;

	/**
	 * Liefert alle Spieler inklusive Statistiken.
	 * 
	 * @param matchType Der Typ des Spiels zum Laden der Statistiken.
	 * @return Alle Spieler inklusive Statistiken.
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	ArrayList<PlayerDto> getAllPlayers(MatchType matchType) throws KickerLeagueException;

	/**
	 * Liefert Aggregation von Jahr und Spielern mit mindestens einem Spiel für Einzelspiele.
	 * 
	 * @return Die Aggregation von Jahr und Spielern mit mindestens einem Spiel.
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	HashMap<Integer, ArrayList<PlayerDto>> getSingleMatchYearAggregation() throws KickerLeagueException;

	/**
	 * Liefert Aggregation von Jahr und Spielern mit mindestens einem Spiel für Doppelspiele.
	 * 
	 * @return Die Aggregation von Jahr und Spielern mit mindestens einem Spiel.
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	HashMap<Integer, ArrayList<PlayerDto>> getDoubleMatchYearAggregation() throws KickerLeagueException;

}
