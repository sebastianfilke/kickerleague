package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.dto.ChartDto;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Die asynchrone Schnittstelle zur Verarbeitung von Diagrammen im Klienten.
 * 
 * @author Sebastian Filke
 */
public interface ChartServiceAsync {

	public void getSinglePlayerGoalChart(PlayerDto playerDto, AsyncCallback<ChartDto> callback) throws IllegalArgumentException;

}
