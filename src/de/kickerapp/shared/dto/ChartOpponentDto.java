package de.kickerapp.shared.dto;

/**
 * Client-Datenklasse zum Halten der Gegnerstatistiken für ein Team bzw. Spieler.
 * 
 * @author Sebastian Filke
 */
public class ChartOpponentDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Der Name des Gegners bzw. des gegnerischen Teams. */
	private String opponentName;
	/** Der prozentuale Anteil der gespielten Spiele gegen den Gegner bzw. das gegnerische Team. */
	private String percentageMatches;
	/** Der prozentuale Anteil der Siege des Teams bzw. Spielers. */
	private String percentageWins;
	/** Der prozentuale Anteil der Niederlagen des Teams bzw. Spielers. */
	private String percentageDefeats;
	/** Die gewonnen Spiele des Teams bzw. Spielers. */
	private Integer wins;
	/** Die verlorenen Spiele des Teams bzw. Spielers. */
	private Integer defeats;

	/**
	 * Erzeugt eine leere Gegnerstatistik ohne Angaben.
	 */
	public ChartOpponentDto() {
		super();

		opponentName = "";
		percentageMatches = "";
		percentageWins = "";
		percentageDefeats = "";
		wins = 0;
		defeats = 0;
	}

	/**
	 * Liefert den Namen des Gegners bzw. des gegnerischen Teams.
	 * 
	 * @return Der Name des Gegners bzw. des gegnerischen Teams als {@link String}.
	 */
	public String getOpponentName() {
		return opponentName;
	}

	/**
	 * Setzt den Namen des Gegners bzw. des gegnerischen Teams.
	 * 
	 * @param opponentName Der Name des Gegners bzw. des gegnerischen Teams als {@link String}.
	 */
	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	/**
	 * Liefert den prozentuale Anteil der gespielten Spiele gegen den Gegner bzw. das gegnerische Team.
	 * 
	 * @return Der prozentuale Anteil der gespielten Spiele gegen den Gegner bzw. das gegnerische Team als {@link String}.
	 */
	public String getPercentageMatches() {
		return percentageMatches;
	}

	/**
	 * Setzt den prozentuale Anteil der gespielten Spiele gegen den Gegner bzw. das gegnerische Team.
	 * 
	 * @param percentageMatches Der prozentuale Anteil der gespielten Spiele gegen den Gegner bzw. das gegnerische Team als {@link String}.
	 */
	public void setPercentageMatches(String percentageMatches) {
		this.percentageMatches = percentageMatches;
	}

	/**
	 * Liefert den prozentuale Anteil der Siege des Teams bzw. Spielers.
	 * 
	 * @return Der prozentuale Anteil der Siege des Teams bzw. Spielers als {@link String}.
	 */
	public String getPercentageWins() {
		return percentageWins;
	}

	/**
	 * Setzt den prozentuale Anteil der Siege des Teams bzw. Spielers.
	 * 
	 * @param percentageWins Der prozentuale Anteil der Siege des Teams bzw. Spielers als {@link String}.
	 */
	public void setPercentageWins(String percentageWins) {
		this.percentageWins = percentageWins;
	}

	/**
	 * Liefert den prozentuale Anteil der Niederlagen des Teams bzw. Spielers.
	 * 
	 * @return Der prozentuale Anteil der Niederlagen des Teams bzw. Spielers als {@link String}.
	 */
	public String getPercentageDefeats() {
		return percentageDefeats;
	}

	/**
	 * Setzt den prozentuale Anteil der Niederlagen des Teams bzw. Spielers.
	 * 
	 * @param percentageDefeats Der prozentuale Anteil der Niederlagen des Teams bzw. Spielers als {@link String}.
	 */
	public void setPercentageDefeats(String percentageDefeats) {
		this.percentageDefeats = percentageDefeats;
	}

	/**
	 * Liefert die gewonnen Spiele des Teams bzw. Spielers.
	 * 
	 * @return Die gewonnen Spiele des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getWins() {
		return wins;
	}

	/**
	 * Setzt die gewonnen Spiele des Teams bzw. Spielers.
	 * 
	 * @param wins Die gewonnen Spiele des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setWins(Integer wins) {
		this.wins = wins;
	}

	/**
	 * Liefert die verlorenen Spiele des Teams bzw. Spielers.
	 * 
	 * @return Die verlorenen Spiele des Teams bzw. Spielers als {@link Integer}.
	 */
	public Integer getDefeats() {
		return defeats;
	}

	/**
	 * Setzt die verlorenen Spiele des Teams bzw. Spielers.
	 * 
	 * @param defeats Die verlorenen Spiele des Teams bzw. Spielers als {@link Integer}.
	 */
	public void setDefeats(Integer defeats) {
		this.defeats = defeats;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(getId()).append(", ");
		sb.append("opponentName=").append(opponentName).append(", ");
		sb.append("percentageMatches=").append(percentageMatches).append(", ");
		sb.append("percentageWins=").append(percentageWins).append(", ");
		sb.append("percentageDefeats=").append(percentageDefeats).append(", ");
		sb.append("wins=").append(wins).append(", ");
		sb.append("defeats=").append(defeats);
		sb.append("]");

		return sb.toString();
	}

}
