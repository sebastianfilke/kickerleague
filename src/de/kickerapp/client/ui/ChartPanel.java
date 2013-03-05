package de.kickerapp.client.ui;

import java.util.ArrayList;

import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.shared.dto.ChartDto;

public class ChartPanel extends SimpleContainer {

	private ListStore<ChartDto> storeGroupBarChart;

	private static final String[] MONTHS = new String[] { "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober",
			"November", "Dezember" };

	public ChartPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	public void initLayout() {
		// setBorders(false);

		storeGroupBarChart = new ListStore<ChartDto>(KickerProperties.CHART_PROPERTY.id());
		storeGroupBarChart.addAll(getTestData());

		final HorizontalLayoutContainer hlcSingleTable = new HorizontalLayoutContainer();
		hlcSingleTable.add(createGroupBarChart(), new HorizontalLayoutData(0.5, 1, new Margins(5, 0, 0, 0)));
		hlcSingleTable.add(createGroupBarChart(), new HorizontalLayoutData(0.5, 1, new Margins(5, 0, 0, 5)));

		final VerticalLayoutContainer vlcSingleTable = new VerticalLayoutContainer();
		vlcSingleTable.getElement().getStyle().setBackgroundColor("#F1F1F1");
		vlcSingleTable.add(createGroupBarChart(), new VerticalLayoutData(1, 0.5));
		vlcSingleTable.add(hlcSingleTable, new VerticalLayoutData(1, 0.5));

		add(vlcSingleTable);
	}

	private ArrayList<ChartDto> getTestData() {
		final ArrayList<ChartDto> chart = new ArrayList<ChartDto>();

		for (int i = 1; i <= 12; i++) {
			final ChartDto chartDto = new ChartDto(MONTHS[i - 1]);
			chartDto.setShotGoals(myRandom(0, 100));
			chartDto.setGetGoals(myRandom(0, 100));
			chartDto.setGoalDifference(chartDto.getShotGoals() - chartDto.getGetGoals());

			chart.add(chartDto);
		}

		return chart;
	}

	public int myRandom(int low, int high) {
		return (int) (Math.random() * (high - low) + low);
	}

	private Chart<ChartDto> createGroupBarChart() {
		final Chart<ChartDto> chart = new Chart<ChartDto>();
		chart.setStore(storeGroupBarChart);
		chart.setAnimated(true);
		chart.setShadowChart(true);

		chart.addAxis(createNumericAxis());
		chart.addAxis(createCategoryAxis());
		chart.addSeries(createBarSeries());
		chart.setLegend(createLegend());

		return chart;
	}

	private NumericAxis<ChartDto> createNumericAxis() {
		final NumericAxis<ChartDto> numAxis = new NumericAxis<ChartDto>();
		numAxis.setPosition(Position.LEFT);
		numAxis.addField(KickerProperties.CHART_PROPERTY.goalDifference());
		numAxis.addField(KickerProperties.CHART_PROPERTY.shotGoals());
		numAxis.addField(KickerProperties.CHART_PROPERTY.getGoals());

		final TextSprite title = new TextSprite("Torbilanz");
		title.setFontSize(20);
		title.setFont("Tahoma");
		numAxis.setTitleConfig(title);

		numAxis.setDisplayGrid(true);

		return numAxis;
	}

	private CategoryAxis<ChartDto, String> createCategoryAxis() {
		final CategoryAxis<ChartDto, String> catAxis = new CategoryAxis<ChartDto, String>();
		catAxis.setPosition(Position.BOTTOM);
		catAxis.setField(KickerProperties.CHART_PROPERTY.month());

		final TextSprite title = new TextSprite("Monat");
		title.setFontSize(20);
		title.setFont("Tahoma");
		catAxis.setTitleConfig(title);

		return catAxis;
	}

	private BarSeries<ChartDto> createBarSeries() {
		final BarSeries<ChartDto> bar = new BarSeries<ChartDto>();
		bar.setYAxisPosition(Position.LEFT);
		bar.addYField(KickerProperties.CHART_PROPERTY.goalDifference());
		bar.addYField(KickerProperties.CHART_PROPERTY.shotGoals());
		bar.addYField(KickerProperties.CHART_PROPERTY.getGoals());
		bar.addColor(new RGB(17, 95, 166));
		bar.addColor(new RGB(148, 174, 10));
		bar.addColor(new RGB(166, 17, 32));
		bar.setColumn(true);

		final TextSprite sprite = new TextSprite();
		sprite.setFill(RGB.WHITE);
		sprite.setFontSize(12);
		sprite.setTextAnchor(TextAnchor.MIDDLE);

		final SeriesLabelConfig<ChartDto> labelConfig = new SeriesLabelConfig<ChartDto>();
		labelConfig.setSpriteConfig(sprite);

		bar.setLabelConfig(labelConfig);

		final ArrayList<String> legendTitles = new ArrayList<String>();
		legendTitles.add("Tordifferenz");
		legendTitles.add("Tore geschossen");
		legendTitles.add("Tore kassiert");
		bar.setLegendTitles(legendTitles);

		return bar;
	}

	private Legend<ChartDto> createLegend() {
		final Legend<ChartDto> legend = new Legend<ChartDto>();
		legend.setPosition(Position.RIGHT);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);

		return legend;
	}

}
