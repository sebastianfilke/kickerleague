package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.match.MatchData;

@RemoteServiceRelativePath("matchService")
public interface MatchService extends RemoteService {

	public MatchData createMatch(MatchData match) throws IllegalArgumentException;

	public ArrayList<MatchData> getAllMatches() throws IllegalArgumentException;

}
