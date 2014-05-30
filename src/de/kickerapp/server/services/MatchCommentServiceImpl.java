package de.kickerapp.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.client.services.MatchCommentService;
import de.kickerapp.shared.dto.MatchCommentDto;

/**
 * Dienst zur Verarbeitung von Kommentaren zu Spielen im Klienten.
 * 
 * @author Sebastian Filke
 */
public class MatchCommentServiceImpl extends RemoteServiceServlet implements MatchCommentService {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1723263815068102491L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insertComment(MatchCommentDto matchCommentDto) throws IllegalArgumentException {
		// TODO Auto-generated method stub
	}

}
