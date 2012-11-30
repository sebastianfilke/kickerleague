package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.player.Player;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface PlayerServiceAsync {

	public void createPlayer(Player player, AsyncCallback<Player> callback)
			throws IllegalArgumentException;
}
