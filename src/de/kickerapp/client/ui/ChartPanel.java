package de.kickerapp.client.ui;

import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.data.shared.ListStore;

import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.shared.dto.IChart;

public class ChartPanel extends BasePanel {

	private ListStore<IChart> storeGroupBarChart;

	public ChartPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		super.initLayout();
		storeGroupBarChart = new ListStore<IChart>(KickerProperties.CHART_PROPERTY.id());

		add(createGroupBarChart());
	}

	private Chart<IChart> createGroupBarChart() {
		final Chart<IChart> chart = new Chart<IChart>();
		chart.setStore(storeGroupBarChart);
		chart.setShadowChart(true);

		chart.addAxis(createNumericAxis());
		chart.addAxis(createCategoryAxis());
		chart.addSeries(createBarSeries());
		chart.setLegend(createLegend());

		return chart;
	}

	private NumericAxis<IChart> createNumericAxis() {
		final NumericAxis<IChart> numAxis = new NumericAxis<IChart>();
		numAxis.setPosition(Position.BOTTOM);
		// axis.addField(dataAccess.data1());
		// axis.addField(dataAccess.data2());
		// axis.addField(dataAccess.data3());

		final TextSprite title = new TextSprite("Number of Hits");
		title.setFontSize(18);
		numAxis.setTitleConfig(title);

		numAxis.setDisplayGrid(true);
		numAxis.setMinimum(0);
		numAxis.setMaximum(100);

		return numAxis;
	}

	private CategoryAxis<IChart, String> createCategoryAxis() {
		final CategoryAxis<IChart, String> catAxis = new CategoryAxis<IChart, String>();
		catAxis.setPosition(Position.LEFT);
		// catAxis.setField(dataAccess.name());

		final TextSprite title = new TextSprite("Month of the Year");
		title.setFontSize(18);
		catAxis.setTitleConfig(title);

		return catAxis;
	}

	private BarSeries<IChart> createBarSeries() {
		final BarSeries<IChart> bar = new BarSeries<IChart>();
		bar.setYAxisPosition(Position.BOTTOM);
		// bar.addYField(dataAccess.data1());
		// bar.addYField(dataAccess.data2());
		// bar.addYField(dataAccess.data3());
		bar.addColor(new RGB(148, 174, 10));
		bar.addColor(new RGB(17, 95, 166));
		bar.addColor(new RGB(166, 17, 32));

		return bar;
	}

	private Legend<IChart> createLegend() {
		final Legend<IChart> legend = new Legend<IChart>();
		legend.setPosition(Position.RIGHT);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);

		return legend;
	}

}
