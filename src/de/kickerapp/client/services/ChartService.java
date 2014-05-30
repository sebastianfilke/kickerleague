package de.kickerapp.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.kickerapp.shared.dto.ChartDto;
import de.kickerapp.shared.dto.PlayerDto;

@RemoteServiceRelativePath("chartService")
public interface ChartService extends RemoteService {

	public ChartDto getGoalChart(PlayerDto playerDto) throws IllegalArgumentException;

}
