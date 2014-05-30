package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die Schnittstelle zur Verarbeitung von paginierten-Listen.
 * 
 * @author Sebastian Filke
 */
@RemoteServiceRelativePath("pagingService")
public interface PagingService extends RemoteService {

	/**
	 * Liefert eine paginierte Spielerliste.
	 * 
	 * @param query Die Anfrage.
	 * @param loadConfig Die Ladekonfiguration der paginierten Spielerliste.
	 * @return Die paginierte Spielerliste.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public PagingLoadResult<PlayerDto> getPagedPlayers(String query, PagingLoadConfig loadConfig) throws IllegalArgumentException;

}
