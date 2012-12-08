package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.match.MatchData;

public interface MatchServiceAsync {

	public void createMatch(MatchData match, AsyncCallback<MatchData> callback) throws IllegalArgumentException;

	public void getAllMatches(AsyncCallback<ArrayList<MatchData>> callback) throws IllegalArgumentException;

}
