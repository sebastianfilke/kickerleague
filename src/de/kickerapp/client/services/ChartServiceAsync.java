package de.kickerapp.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.kickerapp.shared.dto.ChartDto;
import de.kickerapp.shared.dto.PlayerDto;

public interface ChartServiceAsync {

	public void getGoalChart(PlayerDto playerDto, AsyncCallback<ArrayList<ChartDto>> callback) throws IllegalArgumentException;

}
