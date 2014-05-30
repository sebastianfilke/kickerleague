package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.dto.TeamDto;

/**
 * Die asynchrone Schnittstelle zur Verarbeitung der Teams im Clienten.
 * 
 * @author Sebastian Filke
 */
public interface TeamServiceAsync {

	/**
	 * Liefert alle Teams inklusive Statistiken.
	 * 
	 * @param callback Der Callback-Handler.
	 * @throws IllegalArgumentException Falls ein illegales Argument übergeben wurde.
	 */
	public void getAllTeams(AsyncCallback<ArrayList<TeamDto>> callback) throws IllegalArgumentException;

}
