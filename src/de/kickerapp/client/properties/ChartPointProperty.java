package de.kickerapp.client.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.kickerapp.shared.dto.ChartPointDto;

public interface ChartPointProperty extends BaseProperty<ChartPointDto> {

	ValueProvider<ChartPointDto, Integer> matchNumber();

	ValueProvider<ChartPointDto, Integer> points();

}
