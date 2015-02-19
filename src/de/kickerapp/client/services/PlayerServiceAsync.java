package de.kickerapp.client.services;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die asynchrone Schnittstelle zur Verarbeitung der Spieler im Klienten.
 * 
 * @author Sebastian Filke
 */
public interface PlayerServiceAsync {

	/**
	 * Erzeugt einen neuen Spieler.
	 * 
	 * @param playerDto Der zu erstellende Spieler.
	 * @param callback Der Callback-Handler.
	 */
	void createPlayer(PlayerDto playerDto, AsyncCallback<PlayerDto> callback);

	/**
	 * Aktualisiert den Spieler.
	 * 
	 * @param playerDto Der zu aktualisierende Spieler.
	 * @param callback Der Callback-Handler.
	 */
	void updatePlayer(PlayerDto playerDto, AsyncCallback<PlayerDto> callback);

	/**
	 * Liefert alle Spieler inklusive Statistiken.
	 * 
	 * @param matchType Der Typ des Spiels zum Laden der Statistiken.
	 * @param callback Der Callback-Handler.
	 */
	void getAllPlayers(MatchType matchType, AsyncCallback<ArrayList<PlayerDto>> callback);

	/**
	 * Liefert Aggregation von Jahr und Spielern mit mindestens einem Spiel für Einzelspiele.
	 * 
	 * @param callback Der Callback-Handler.
	 */
	void getSingleMatchYearAggregation(AsyncCallback<HashMap<Integer, ArrayList<PlayerDto>>> callback);

	/**
	 * Liefert Aggregation von Jahr und Spielern mit mindestens einem Spiel für Doppelspiele.
	 * 
	 * @param callback Der Callback-Handler.
	 */
	void getDoubleMatchYearAggregation(AsyncCallback<HashMap<Integer, ArrayList<PlayerDto>>> callback);

}
