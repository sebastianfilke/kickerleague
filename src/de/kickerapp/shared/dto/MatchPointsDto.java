package de.kickerapp.shared.dto;

import java.util.ArrayList;

import de.kickerapp.shared.common.BaseDto;

public class MatchPointsDto extends BaseDto {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -8595724509087459312L;

	private ArrayList<Integer> pointsTeam1;

	private ArrayList<Integer> pointsTeam2;

	public MatchPointsDto() {
		super();
		pointsTeam1 = new ArrayList<Integer>();
		pointsTeam2 = new ArrayList<Integer>();
	}

	public ArrayList<Integer> getPointsTeam1() {
		return pointsTeam1;
	}

	public void setPointsTeam1(ArrayList<Integer> setsTeam1) {
		this.pointsTeam1 = setsTeam1;
	}

	public ArrayList<Integer> getPointsTeam2() {
		return pointsTeam2;
	}

	public void setPointsTeam2(ArrayList<Integer> setsTeam2) {
		this.pointsTeam2 = setsTeam2;
	}

}
