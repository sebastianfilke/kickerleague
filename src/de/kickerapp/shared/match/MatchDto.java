package de.kickerapp.shared.match;

import java.util.Date;

import de.kickerapp.shared.common.BaseData;

public class MatchDto extends BaseData implements IMatch {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = 2656034118993930642L;

	private String matchNumber;

	private Date matchDate;

	private String labelTeam1;

	private String labelTeam2;

	private String labelSets;

	private TeamDto team1;

	private TeamDto team2;

	private SetDto sets;

	public MatchDto() {
		super();
		matchNumber = "";
		labelTeam1 = "";
		labelTeam2 = "";
		matchDate = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMatchNumber(String matchNumber) {
		this.matchNumber = matchNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMatchNumber() {
		return matchNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getMatchDate() {
		return matchDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLabelTeam1(String labelTeam1) {
		this.labelTeam1 = labelTeam1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabelTeam1() {
		return labelTeam1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLabelTeam2(String labelTeam2) {
		this.labelTeam2 = labelTeam2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabelTeam2() {
		return labelTeam2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLabelSets(String labelSets) {
		this.labelSets = labelSets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabelSets() {
		return labelSets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTeam1(TeamDto team1) {
		this.team1 = team1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TeamDto getTeam1() {
		return team1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTeam2(TeamDto team2) {
		this.team2 = team2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TeamDto getTeam2() {
		return team2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSets(SetDto sets) {
		this.sets = sets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SetDto getSets() {
		return sets;
	}

}
