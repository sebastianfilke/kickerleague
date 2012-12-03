package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.match.Match;

public interface MatchServiceAsync {

	public void createMatch(Match match, AsyncCallback<Match> callback) throws IllegalArgumentException;

	public void getAllMatches(AsyncCallback<ArrayList<Match>> callback) throws IllegalArgumentException;

}
