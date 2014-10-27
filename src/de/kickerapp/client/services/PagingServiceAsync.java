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
	 * @param loadConfig Die Ladekonfiguration der paginierten Spielerliste.
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getPagedPlayers(String query, ArrayList<PlayerDto> selectedPlayers, PagingLoadConfig loadConfig,
			AsyncCallback<PagingLoadResult<PlayerDto>> callback) throws IllegalArgumentException;

}
