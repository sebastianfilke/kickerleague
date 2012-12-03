package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.match.Match;

@RemoteServiceRelativePath("matchService")
public interface MatchService extends RemoteService {

	public Match createMatch(Match match) throws IllegalArgumentException;

	public ArrayList<Match> getAllMatches() throws IllegalArgumentException;

}
