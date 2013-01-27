package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.dto.PlayerDto;

/**
 * 
 */
@RemoteServiceRelativePath("playerService")
public interface PlayerService extends RemoteService {

	public PlayerDto createPlayer(PlayerDto player) throws IllegalArgumentException;
	
	public ArrayList<PlayerDto> getAllPlayers() throws IllegalArgumentException;

}
