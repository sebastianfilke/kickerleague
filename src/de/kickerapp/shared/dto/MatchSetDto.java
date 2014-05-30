package de.kickerapp.shared.dto;

import java.util.ArrayList;

import de.kickerapp.shared.common.BaseDto;

/**
 * Client-Datenklasse zum Halten der Informationen für die Spielsätze eines Spiels.
 * 
 * @author Sebastian Filke
 */
public class MatchSetDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -2054250321124768378L;

	/** Die Sätze des ersten Teams bzw. Spielers. */
	private ArrayList<Integer> matchSetsTeam1;
	/** Die Sätze des zweiten Teams bzw. Spielers. */
	private ArrayList<Integer> matchSetsTeam2;

	/**
	 * Erzeugt Spielsätze mit leeren Sätzen.
	 */
	public MatchSetDto() {
		super();

		matchSetsTeam1 = new ArrayList<Integer>();
		matchSetsTeam2 = new ArrayList<Integer>();
	}

	/**
	 * Liefert die Sätze des ersten Teams bzw. Spielers.
	 * 
	 * @return Die Sätze des ersten Teams bzw. Spielers als {@link ArrayList}.
	 */
	public ArrayList<Integer> getMatchSetsTeam1() {
		return matchSetsTeam1;
	}

	/**
	 * Setzt die Sätze des ersten Teams bzw. Spielers.
	 * 
	 * @param matchSetsTeam1 Die Sätze des ersten Teams bzw. Spielers als {@link ArrayList}.
	 */
	public void setMatchSetsTeam1(ArrayList<Integer> matchSetsTeam1) {
		this.matchSetsTeam1 = matchSetsTeam1;
	}

	/**
	 * Liefert die Sätze des zweiten Teams bzw. Spielers.
	 * 
	 * @return Die Sätze des zweiten Teams bzw. Spielers als {@link ArrayList}.
	 */
	public ArrayList<Integer> getMatchSetsTeam2() {
		return matchSetsTeam2;
	}

	/**
	 * Setzt die Sätze des zweiten Teams bzw. Spielers.
	 * 
	 * @param matchSetsTeam2 Die Sätze des zweiten Teams bzw. Spielers als {@link ArrayList}.
	 */
	public void setMatchSetsTeam2(ArrayList<Integer> matchSetsTeam2) {
		this.matchSetsTeam2 = matchSetsTeam2;
	}

}
