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
	 * Beide Typen.
	 */
	BOTH("both"),

	/**
	 * Kein Typ.
	 */
	NONE("none");

	/** Der Spieltyp. */
	private String matchType;

	/**
	 * Erzeugt einen neuen Spieltyp.
	 * 
	 * @param matchType Der Spieltyp.
	 */
	private MatchType(String matchType) {
		this.matchType = matchType;
	}

	/**
	 * Liefert den Spieltyp.
	 * 
	 * @return Der Spieltyp.
	 */
	public String getMatchType() {
		return matchType;
	}

	/**
	 * Setzt den Spieltyp.
	 * 
	 * @param matchType Der Spieltyp.
	 */
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

}
