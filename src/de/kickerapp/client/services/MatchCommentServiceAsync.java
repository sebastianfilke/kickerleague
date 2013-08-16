package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.dto.MatchCommentDto;

/**
 * Die asynchrone Schnittstelle zur Verarbeitung von Kommentaren zu Spielen im Klienten.
 * 
 * @author Sebastian Filke
 */
public interface MatchCommentServiceAsync {

	public void insertComment(MatchCommentDto matchCommentDto, AsyncCallback<Void> callback) throws IllegalArgumentException;

}
