package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.player.Player;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("playerService")
public interface PlayerService extends RemoteService {

	public Player createPlayer(Player player) throws IllegalArgumentException;

}
