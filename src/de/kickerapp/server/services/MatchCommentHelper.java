package de.kickerapp.server.services;

import de.kickerapp.server.entity.MatchComment;
import de.kickerapp.shared.dto.MatchCommentDto;
import de.kickerapp.shared.dto.MatchDto;

/**
 * Hilfsklasse für den Dienst zur Verarbeitung von Kommentare zum Spiel im Clienten.
 * 
 * @author Sebastian Filke
 */
public class MatchCommentHelper {

	/**
	 * Erzeugt die Client-Datenklasse anhand der Objekt-Datenklasse.
	 * 
	 * @param dbMatchComment Die Objekt-Datenklasse.
	 * @param matchDto Das Spiel.
	 * @return Die Client-Datenklasse.
	 */
	protected static MatchCommentDto createMatchCommentDto(MatchComment dbMatchComment, MatchDto matchDto) {
		final MatchCommentDto matchCommentDto = new MatchCommentDto(dbMatchComment.getComment(), matchDto);
		matchCommentDto.setId(dbMatchComment.getKey().getId());

		return matchCommentDto;
	}

}
