package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import de.kickerapp.shared.dto.PlayerDto;

public interface PagingServiceAsync {

	/**
	 * Liefert eine gepufferte Liste, welche anhand des
	 * <code>OrderPropertyIdentifier</code>s angegeben wird.
	 * 
	 * @param identifier Die Bezeichnungen einer Eigenschaft der Auftragsdaten.
	 * @param config Die Ladekonfiguration der gepufferten Liste.
	 * @param callback Der Callback-Handler.
	 */
	public void getPagedPlayers(String query, PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<PlayerDto>> callback) throws IllegalArgumentException;

}
