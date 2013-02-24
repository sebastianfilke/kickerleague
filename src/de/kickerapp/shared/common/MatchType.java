package de.kickerapp.shared.common;

/**
 * Aufzählung für die unterschiedlichen Typen von Spielen.
 * 
 * @author Sebastian Filke
 */
public enum MatchType {

	/**
	 * Einzelspiel.
	 */
	SINGLE("single"),

	/**
	 * Doppelspiel.
	 */
	DOUBLE("double"),

	/**
	 * Unbekannt.
	 */
	UNKNOWN("none");

	/** Der Spieltyp. */
	private String matchType;

	/**
	 * @param matchType
	 */
	private MatchType(String matchType) {
		this.matchType = matchType;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

}
