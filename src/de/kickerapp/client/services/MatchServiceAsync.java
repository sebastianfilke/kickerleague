package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.dto.MatchDto;

/**
 * Die asynchrone Schnittstelle zur Verarbeitung von Spielen im Clienten.
 * 
 * @author Sebastian Filke
 */
public interface MatchServiceAsync {

	public void createSingleMatch(MatchDto match, AsyncCallback<MatchDto> callback) throws IllegalArgumentException;

	public void getAllMatches(AsyncCallback<ArrayList<MatchDto>> callback) throws IllegalArgumentException;

}