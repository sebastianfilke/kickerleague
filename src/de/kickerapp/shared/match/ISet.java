package de.kickerapp.shared.match;

import java.util.ArrayList;

import de.kickerapp.shared.common.IBase;

public interface ISet extends IBase {

	public ArrayList<Integer> getSetsTeam1();

	public void setSetsTeam1(ArrayList<Integer> setsTeam1);

	public ArrayList<Integer> getSetsTeam2();

	public void setSetsTeam2(ArrayList<Integer> setsTeam2);

}
