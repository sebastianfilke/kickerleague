package de.kickerapp.server.services;

import de.kickerapp.server.dao.MatchComment;
import de.kickerapp.shared.dto.MatchCommentDto;

/**
 * Hilfsklasse für den Dienst zur Verarbeitung von Kommentare zum Spiel im Klienten.
 * 
 * @author Sebastian Filke
 */
public class MatchCommentHelper {

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbMatchComment Die Objekt-Datenklasse.
	 * @return Die Client-Datenklasse.
	 */
	protected static MatchCommentDto createMatchCommentDto(MatchComment dbMatchComment) {
		final MatchCommentDto matchCommentDto = new MatchCommentDto(dbMatchComment.getComment());
		matchCommentDto.setId(dbMatchComment.getKey().getId());

		return matchCommentDto;
	}

}
