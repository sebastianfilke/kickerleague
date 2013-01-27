package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die Schnittstelle zur Verarbeitung der Paging-ComboBoxen.
 * 
 * @author Sebastian Filke, GIGATRONIK München GmbH
 */
@RemoteServiceRelativePath("pagingService")
public interface PagingService extends RemoteService {

	/**
	 * Liefert eine gepufferte Liste, welche anhand des
	 * <code>OrderPropertyIdentifier</code>s angegeben wird.
	 * 
	 * @param config Die Ladekonfiguration der gepufferten Liste.
	 * @return Die gepufferte Liste.
	 */
	public PagingLoadResult<PlayerDto> getPagedPlayers(String query, PagingLoadConfig loadConfig) throws IllegalArgumentException;

}
