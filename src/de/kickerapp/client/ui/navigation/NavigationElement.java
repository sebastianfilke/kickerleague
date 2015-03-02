package de.kickerapp.client.ui.navigation;

/**
 * Aufzählung für die unterschiedlichen Elemente der Navigationsleiste.
 * 
 * @author Sebastian Filke
 */
public enum NavigationElement {

	/** Das Element für die Tabellen. */
	TABLES(NavigationType.MAIN, NavigationIdentificator.TABLES),
	/** Das Element für die Einzeltabelle. */
	SINGLETABLE(NavigationType.SUB, NavigationIdentificator.SINGLE_TABLE, TABLES, 0),
	/** Das Element für die Doppeltabelle. */
	DOUBLETABLE(NavigationType.SUB, NavigationIdentificator.DOUBLE_TABLE, TABLES, 1),
	/** Das Element für die Teamtabelle. */
	TEAMTABLE(NavigationType.SUB, NavigationIdentificator.TEAM_TABLE, TABLES, 2),
	/** Das Element für Ergebnisse. */
	RESULTS(NavigationType.MAIN, NavigationIdentificator.RESULTS),
	/** Das Element für das Eintragen von Spielen. */
	INSERT(NavigationType.MAIN, NavigationIdentificator.INSERT),
	/** Das Element für die Statistiken. */
	CHARTS(NavigationType.MAIN, NavigationIdentificator.CHARTS),
	/** Das Element für die Einzelstatistiken. */
	SINGLECHART(NavigationType.SUB, NavigationIdentificator.SINGLE_CHART, CHARTS, 0),
	/** Das Element für die Doppelstatistiken. */
	DOUBLECHART(NavigationType.SUB, NavigationIdentificator.DOUBLE_CHART, CHARTS, 1),
	/** Das Element für die Teamstatistiken. */
	TEAMCHART(NavigationType.SUB, NavigationIdentificator.TEAM_CHART, CHARTS, 2),
	/** Das Element für die Spieler. */
	PLAYER(NavigationType.MAIN, NavigationIdentificator.PLAYER),
	/** Element unbekannt. */
	UNKOWN(NavigationType.UNKNOWN, NavigationIdentificator.UNKNOWN);

	/** Der Typ des Navigationselements. */
	private String type;
	/** Der Schlüssel des Navigationselements. */
	private String identificator;
	/** Das Vater-Navigationselement. */
	private NavigationElement parent;
	/** Die Nummer des Tabs des Navigationselement. */
	private int tabIndex;

	/**
	 * Erzeugt ein neues Navigatonselement mit übergebenem Typ und Schlüssel.
	 * 
	 * @param type Der Typ des Navigationselements als {@link String}.
	 * @param identificator Der Schlüssel des Navigationselements als {@link String}.
	 */
	private NavigationElement(String type, String identificator) {
		this.type = type;
		this.identificator = identificator;
		this.parent = null;
		this.tabIndex = -1;
	}

	/**
	 * Erzeugt ein neues Navigatonselement mit übergebenem Typ und Schlüssel.
	 * 
	 * @param type Der Typ des Navigationselements als {@link String}.
	 * @param identificator Der Schlüssel des Navigationselements als {@link String}.
	 * @param parent Das Vater-Navigationselement.
	 * @param tabIndex Die Nummer des Tabs des Navigationselement.
	 */
	private NavigationElement(String type, String identificator, NavigationElement parent, int tabIndex) {
		this(type, identificator);
		this.parent = parent;
		this.tabIndex = tabIndex;
	}

	/**
	 * Liefert den Typ des Navigationselements.
	 * 
	 * @return Der Typ des Navigationselements als {@link String}.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Liefert den Schlüssel des Navigationselements.
	 * 
	 * @return Der Schlüssel des Navigationselements als {@link String}.
	 */
	public String getIdentificator() {
		return identificator;
	}

	/**
	 * Liefert das Vater-Navigationselement.
	 * 
	 * @return Das Vater-Navigationselement {@link NavigationElement} oder <code>null</code>.
	 */
	public NavigationElement getParent() {
		return parent;
	}

	/**
	 * Liefert die Nummer des Tabs des Navigationselement.
	 * 
	 * @return Die Nummer des Tabs des Navigationselement {@link Integer}.
	 */
	public int getTabIndex() {
		return tabIndex;
	}

}
