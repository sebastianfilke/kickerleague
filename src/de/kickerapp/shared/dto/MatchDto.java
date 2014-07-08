package de.kickerapp.shared.dto;

import java.util.Date;

import de.kickerapp.shared.common.MatchType;

/**
 * Client-Basisdatenklasse zum Halten der Informationen für ein Spiel.
 * 
 * @author Sebastian Filke
 */
public class MatchDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 2656034118993930642L;

	/** Die Spielnummer. */
	private Integer matchNumber;
	/** Das Spieldatum. */
	private Date matchDate;
	/** Das Spieldatum zum Gruppieren der Spiele. */
	private String groupDate;
	/** Der Spieltyp. */
	private MatchType matchType;
	/** Das erste Team bzw. Spieler. */
	private TeamDto team1Dto;
	/** Das zweite Team bzw. Spieler. */
	private TeamDto team2Dto;
	/** Die Spielpunkte der Teams bzw. Spieler. */
	private MatchPointsDto matchPointsDto;
	/** Die Spielsätze der Teams bzw. Spieler. */
	private MatchSetDto matchSetsDto;
	/** Der Kommentar zum Spiel. */
	private MatchCommentDto matchCommentDto;

	/**
	 * Erzeugt ein neues Spiel ohne Angaben.
	 */
	public MatchDto() {
		super();

		matchNumber = 0;
		matchDate = null;
		groupDate = "";
		matchType = MatchType.NONE;
		team1Dto = null;
		team2Dto = null;
		matchPointsDto = null;
		matchSetsDto = null;
		matchCommentDto = null;
	}

	/**
	 * Liefert die Spielnummer.
	 * 
	 * @return Die Spielnummer als {@link Integer}.
	 */
	public Integer getMatchNumber() {
		return matchNumber;
	}

	/**
	 * Setzt die Spielnummer.
	 * 
	 * @param matchNumber Die Spielnummer als {@link Integer}.
	 */
	public void setMatchNumber(Integer matchNumber) {
		this.matchNumber = matchNumber;
	}

	/**
	 * Liefert das Spieldatum.
	 * 
	 * @return Das Spieldatum als {@link Date}.
	 */
	public Date getMatchDate() {
		return matchDate;
	}

	/**
	 * Setzt das Spieldatum.
	 * 
	 * @param matchDate Das Spieldatum als {@link Date}.
	 */
	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}

	/**
	 * Liefert das Spieldatum zum Gruppieren der Spiele.
	 * 
	 * @return Das Spieldatum zum Gruppieren der Spiele als {@link Date}.
	 */
	public String getGroupDate() {
		return groupDate;
	}

	/**
	 * Setzt das Spieldatum zum Gruppieren der Spiele.
	 * 
	 * @param groupDate Das Spieldatum zum Gruppieren der Spiele als {@link Date}.
	 */
	public void setGroupDate(String groupDate) {
		this.groupDate = groupDate;
	}

	/**
	 * Liefert den Spieltyp.
	 * 
	 * @return Der Spieltyp als {@link MatchType}.
	 */
	public MatchType getMatchType() {
		return matchType;
	}

	/**
	 * Setzt den Spieltyp.
	 * 
	 * @param matchType Der Spieltyp als {@link MatchType}.
	 */
	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}

	/**
	 * Liefert das erste Team bzw. Spieler.
	 * 
	 * @return Das erste Team bzw. Spieler als {@link TeamDto}.
	 */
	public TeamDto getTeam1Dto() {
		return team1Dto;
	}

	/**
	 * Setzt das erste Team bzw. Spieler.
	 * 
	 * @param team1Dto Das erste Team bzw. Spieler als {@link TeamDto}.
	 */
	public void setTeam1Dto(TeamDto team1Dto) {
		this.team1Dto = team1Dto;
	}

	/**
	 * Liefert das zweite Team bzw. Spieler.
	 * 
	 * @return Das zweite Team bzw. Spieler als {@link TeamDto}.
	 */
	public TeamDto getTeam2Dto() {
		return team2Dto;
	}

	/**
	 * Setzt das zweite Team bzw. Spieler.
	 * 
	 * @param team2Dto Das zweite Team bzw. Spieler als {@link TeamDto}.
	 */
	public void setTeam2Dto(TeamDto team2Dto) {
		this.team2Dto = team2Dto;
	}

	/**
	 * Liefert die Spielpunkte der Teams bzw. Spieler.
	 * 
	 * @return Die Spielpunkte der Teams bzw. Spieler als {@link MatchPointsDto}.
	 */
	public MatchPointsDto getMatchPointsDto() {
		return matchPointsDto;
	}

	/**
	 * Setzt die Spielpunkte der Teams bzw. Spieler.
	 * 
	 * @param matchPointsDto Die Spielpunkte der Teams bzw. Spieler als {@link MatchPointsDto}.
	 */
	public void setMatchPointsDto(MatchPointsDto matchPointsDto) {
		this.matchPointsDto = matchPointsDto;
	}

	/**
	 * Liefert die Spielsätze der Teams bzw. Spieler.
	 * 
	 * @return Die Spielsätze der Teams bzw. Spieler als {@link MatchSetDto}.
	 */
	public MatchSetDto getMatchSetsDto() {
		return matchSetsDto;
	}

	/**
	 * Setzt die Spielsätze der Teams bzw. Spieler.
	 * 
	 * @param matchSetsDto Die Spielsätze der Teams bzw. Spieler als {@link MatchSetDto}.
	 */
	public void setMatchSetsDto(MatchSetDto matchSetsDto) {
		this.matchSetsDto = matchSetsDto;
	}

	/**
	 * Liefert den Kommentar zum Spiel.
	 * 
	 * @return Der Kommentar zum Spiel als {@link MatchCommentDto}.
	 */
	public MatchCommentDto getMatchCommentDto() {
		return matchCommentDto;
	}

	/**
	 * Setzt den Kommentar zum Spiel.
	 * 
	 * @param matchCommentDto Der Kommentar zum Spiel als {@link MatchCommentDto}.
	 */
	public void setMatchCommentDto(MatchCommentDto matchCommentDto) {
		this.matchCommentDto = matchCommentDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(getId()).append(", ");
		sb.append("matchNumber=").append(matchNumber).append(", ");
		sb.append("matchDate=").append(matchDate).append(", ");
		sb.append("groupDate=").append(groupDate).append(", ");
		sb.append("matchType=").append(matchType);
		sb.append("]");

		return sb.toString();
	}

}
