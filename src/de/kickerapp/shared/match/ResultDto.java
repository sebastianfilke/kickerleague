package de.kickerapp.shared.match;

import de.kickerapp.shared.common.BaseData;

public class ResultDto extends BaseData implements IResult {


	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -5955861019580575717L;
	
	private String matchResult;

	@Override
	public void setMatchResult(String matchResult) {
		this.matchResult = matchResult;
	}

	@Override
	public String getMatchResult() {
		return matchResult;
	}

}
