package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.dto.MatchCommentDto;

/**
 * Die Schnittstelle zur Verarbeitung von Kommentaren zu Spielen im Klienten.
 * 
 * @author Sebastian Filke
 */
@RemoteServiceRelativePath("matchCommentService")
public interface MatchCommentService {

	public void insertComment(MatchCommentDto matchCommentDto) throws IllegalArgumentException;

}
