package de.kickerapp.shared.dto;

import java.util.ArrayList;

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("matchSetsTeam1=");
		for (int i = 0; i < matchSetsTeam1.size(); i++) {
			sb.append(matchSetsTeam1.get(i));
			if (i < matchSetsTeam1.size() - 1) {
				sb.append(",");
			}
		}
		sb.append(",").append("matchSetsTeam2=");
		for (int i = 0; i < matchSetsTeam2.size(); i++) {
			sb.append(matchSetsTeam2.get(i));
			if (i < matchSetsTeam2.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");

		return sb.toString();
	}

}
