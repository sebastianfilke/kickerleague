package de.kickerapp.shared.common;

public class BaseData implements IBase {

	private int id = 0;

	public BaseData() {
		this.id++;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

}
