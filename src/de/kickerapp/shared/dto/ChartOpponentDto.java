package de.kickerapp.shared.dto;


public class ChartOpponentDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 9037519409303287728L;

	private String opponentName;

	private String percentageMatches;

	private String percentageWins;

	private String percentageDefeats;

	private Integer wins;

	private Integer defeats;

	public ChartOpponentDto() {
		super();

		opponentName = "";
		percentageMatches = "";
		percentageWins = "";
		percentageDefeats = "";
		wins = 0;
		defeats = 0;
	}

	public String getPercentageMatches() {
		return percentageMatches;
	}

	public void setPercentageMatches(String percentageMatches) {
		this.percentageMatches = percentageMatches;
	}

	public String getPercentageWins() {
		return percentageWins;
	}

	public void setPercentageWins(String percentageWins) {
		this.percentageWins = percentageWins;
	}

	public String getPercentageDefeats() {
		return percentageDefeats;
	}

	public void setPercentageDefeats(String percentageDefeats) {
		this.percentageDefeats = percentageDefeats;
	}

	public String getOpponentName() {
		return opponentName;
	}

	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	public Integer getWins() {
		return wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Integer getDefeats() {
		return defeats;
	}

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
