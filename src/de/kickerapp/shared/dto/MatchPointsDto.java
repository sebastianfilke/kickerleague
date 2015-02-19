package de.kickerapp.shared.dto;

import java.util.ArrayList;

/**
 * Client-Datenklasse zum Halten der Informationen für die Punkte eines Spiels.
 * 
 * @author Sebastian Filke
 */
public class MatchPointsDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Die Punkte des ersten Teams bzw. Spielers. */
	private ArrayList<Integer> matchPointsTeam1;
	/** Die Punkte des ersten Teams bzw. Spielers. */
	private ArrayList<Integer> matchPointsTeam2;

	/**
	 * Erzeugt Spielpunkte mit leeren Punktzahlen.
	 */
	public MatchPointsDto() {
		super();

		matchPointsTeam1 = new ArrayList<Integer>();
		matchPointsTeam2 = new ArrayList<Integer>();
	}

	/**
	 * Liefert die Punkte des ersten Teams bzw. Spielers.
	 * 
	 * @return Die Punkte des ersten Teams bzw. Spielers als {@link ArrayList}.
	 */
	public ArrayList<Integer> getMatchPointsTeam1() {
		return matchPointsTeam1;
	}

	/**
	 * Setzt die Punkte des ersten Teams bzw. Spielers.
	 * 
	 * @param matchPointsTeam1 Die Punkte des ersten Teams bzw. Spielers als {@link ArrayList}.
	 */
	public void setMatchPointsTeam1(ArrayList<Integer> matchPointsTeam1) {
		this.matchPointsTeam1 = matchPointsTeam1;
	}

	/**
	 * Liefert die Punkte des zweiten Teams bzw. Spielers.
	 * 
	 * @return Die Punkte des zweiten Teams bzw. Spielers als {@link ArrayList}.
	 */
	public ArrayList<Integer> getMatchPointsTeam2() {
		return matchPointsTeam2;
	}

	/**
	 * Setzt die Punkte des zweiten Teams bzw. Spielers.
	 * 
	 * @param matchPointsTeam2 Die Punkte des zweiten Teams bzw. Spielers als {@link ArrayList}.
	 */
	public void setMatchPointsTeam2(ArrayList<Integer> matchPointsTeam2) {
		this.matchPointsTeam2 = matchPointsTeam2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("matchPointsTeam1=");
		for (int i = 0; i < matchPointsTeam1.size(); i++) {
			sb.append(matchPointsTeam1.get(i));
			if (i < matchPointsTeam1.size() - 1) {
				sb.append(",");
			}
		}
		sb.append(",").append("matchPointsTeam2=");
		for (int i = 0; i < matchPointsTeam2.size(); i++) {
			sb.append(matchPointsTeam2.get(i));
			if (i < matchPointsTeam2.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");

		return sb.toString();
	}

}
