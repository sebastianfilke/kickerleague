package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.dto.PlayerDto;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface PlayerServiceAsync {

	public void createPlayer(PlayerDto player, AsyncCallback<PlayerDto> callback) throws IllegalArgumentException;

	public void getAllPlayers(AsyncCallback<ArrayList<PlayerDto>> callback) throws IllegalArgumentException;
}
