package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.dto.TeamDto;

public interface TeamServiceAsync {

	public void getAllTeams(AsyncCallback<ArrayList<TeamDto>> callback) throws IllegalArgumentException;
}
