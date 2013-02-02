package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.dto.TeamDto;

@RemoteServiceRelativePath("teamService")
public interface TeamService extends RemoteService {

	public ArrayList<TeamDto> getAllTeams() throws IllegalArgumentException;
	
}
