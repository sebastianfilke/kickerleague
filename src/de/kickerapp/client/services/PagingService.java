package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.exception.KickerLeagueException;

/**
 * Die Schnittstelle zur Verarbeitung von paginierten-Listen im Klienten.
 * 
 * @author Sebastian Filke
 */
@RemoteServiceRelativePath("pagingService")
public interface PagingService extends RemoteService {

	/**
	 * Liefert eine paginierte Spielerliste.
	 * 
	 * @param query Die Anfrage.
	 * @param triggerClick Die Angabe, ob der Trigger geklickt wurde.
	 * @param selectedPlayers Die Liste der bereits gewählten Spieler.
	 * @param loadConfig Die Ladekonfiguration der paginierten Spielerliste.
	 * @return Die paginierte Spielerliste.
	 * @throws KickerLeagueException Falls ein Fehler aufgetreten ist.
	 */
	PagingLoadResult<PlayerDto> getPagedPlayers(String query, boolean triggerClick, ArrayList<PlayerDto> selectedPlayers, PagingLoadConfig loadConfig)
			throws KickerLeagueException;

}
