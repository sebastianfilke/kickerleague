package de.kickerapp.shared.match;

import java.util.ArrayList;

import de.kickerapp.shared.common.BaseData;

public class SetDto extends BaseData implements ISet {

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -2054250321124768378L;

	private ArrayList<Integer> setsTeam1;

	private ArrayList<Integer> setsTeam2;

	public SetDto() {
		super();
		setsTeam1 = new ArrayList<Integer>();
		setsTeam2 = new ArrayList<Integer>();
	}

	public ArrayList<Integer> getSetsTeam1() {
		return setsTeam1;
	}

	public void setSetsTeam1(ArrayList<Integer> setsTeam1) {
		this.setsTeam1 = setsTeam1;
	}

	public ArrayList<Integer> getSetsTeam2() {
		return setsTeam2;
	}

	public void setSetsTeam2(ArrayList<Integer> setsTeam2) {
		this.setsTeam2 = setsTeam2;
	}

}
