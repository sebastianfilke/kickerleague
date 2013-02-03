package de.kickerapp.shared.common;

public enum MatchType {

	Single("single"),

	Double("double"),

	None("none");

	private String matchType;

	private MatchType(String matchType) {
		this.matchType = matchType;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

}
