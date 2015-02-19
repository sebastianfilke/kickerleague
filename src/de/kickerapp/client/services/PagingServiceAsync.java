package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die asynchrone Schnittstelle zur Verarbeitung von paginierten-Listen.
 * 
 * @author Sebastian Filke
 */
public interface PagingServiceAsync {

	/**
	 * Liefert eine paginierte Spielerliste.
	 * 
	 * @param query Die Anfrage.
	 * @param triggerClick Die Angabe, ob der Trigger geklickt wurde.
	 * @param selectedPlayers Die Liste der bereits gewählten Spieler.
	 * @param loadConfig Die Ladekonfiguration der paginierten Spielerliste.
	 * @param callback Der Callback-Handler.
	 */
	void getPagedPlayers(String query, boolean triggerClick, ArrayList<PlayerDto> selectedPlayers, PagingLoadConfig loadConfig,
			AsyncCallback<PagingLoadResult<PlayerDto>> callback);

}
