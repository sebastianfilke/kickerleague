package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.match.PlayerDto;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface PlayerServiceAsync {

	public void createPlayer(PlayerDto player, AsyncCallback<PlayerDto> callback) throws IllegalArgumentException;
}
