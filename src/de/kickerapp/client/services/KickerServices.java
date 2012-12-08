package de.kickerapp.client.services;

import com.google.gwt.core.client.GWT;

public interface KickerServices {

	public final MatchServiceAsync MATCH_SERVICE = GWT.create(MatchService.class);

}
