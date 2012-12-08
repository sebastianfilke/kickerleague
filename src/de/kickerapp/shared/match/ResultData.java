package de.kickerapp.shared.match;

import de.kickerapp.shared.common.BaseData;

public class ResultData extends BaseData implements IResult {

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
