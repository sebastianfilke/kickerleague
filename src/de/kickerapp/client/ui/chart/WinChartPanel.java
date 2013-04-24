package de.kickerapp.client.ui.chart;

import java.util.ArrayList;

import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.chart.series.SeriesHighlighter;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.DrawFx;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.data.shared.ListStore;

import de.kickerapp.client.properties.ChartProperty;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.shared.dto.ChartDataDto;

public class WinChartPanel extends BaseContainer {

	private ListStore<ChartDataDto> storeWin;

	private Chart<ChartDataDto> chartWin;

	private BarSeries<ChartDataDto> barWin;

	public WinChartPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		storeWin = new ListStore<ChartDataDto>(KickerProperties.CHART_PROPERTY.id());

		add(createGroupBarChart());
	}

	private Chart<ChartDataDto> createGroupBarChart() {
		chartWin = new Chart<ChartDataDto>();
		chartWin.setShadowChart(true);
		chartWin.setStore(storeWin);
		chartWin.setAnimated(true);

		chartWin.addAxis(createNumericAxis());
		chartWin.addAxis(createCategoryAxis());
		chartWin.addSeries(createBarSeries());
		chartWin.setLegend(createLegend());

		return chartWin;
	}

	private NumericAxis<ChartDataDto> createNumericAxis() {
		final NumericAxis<ChartDataDto> numAxis = new NumericAxis<ChartDataDto>();
		numAxis.setPosition(Position.LEFT);
		numAxis.addField(ChartProperty.winDifference);
		numAxis.addField(KickerProperties.CHART_PROPERTY.wins());
		numAxis.addField(KickerProperties.CHART_PROPERTY.defeats());
		numAxis.setAdjustMaximumByMajorUnit(true);
		numAxis.setAdjustMinimumByMajorUnit(true);
		numAxis.setDisplayGrid(true);

		final TextSprite title = new TextSprite("Siegbilanz");
		title.setFontSize(20);
		title.setFont("Tahoma");
		numAxis.setTitleConfig(title);

		return numAxis;
	}

	private CategoryAxis<ChartDataDto, String> createCategoryAxis() {
		final CategoryAxis<ChartDataDto, String> catAxis = new CategoryAxis<ChartDataDto, String>();
		catAxis.setPosition(Position.BOTTOM);
		catAxis.setField(KickerProperties.CHART_PROPERTY.month());

		final TextSprite title = new TextSprite("Monat");
		title.setFontSize(20);
		title.setFont("Tahoma");
		catAxis.setTitleConfig(title);

		return catAxis;
	}

	private BarSeries<ChartDataDto> createBarSeries() {
		barWin = new BarSeries<ChartDataDto>();
		barWin.setYAxisPosition(Position.LEFT);
		barWin.addYField(ChartProperty.winDifference);
		barWin.addYField(KickerProperties.CHART_PROPERTY.wins());
		barWin.addYField(KickerProperties.CHART_PROPERTY.defeats());
		barWin.addColor(new RGB(17, 95, 166));
		barWin.addColor(new RGB(148, 174, 10));
		barWin.addColor(new RGB(166, 17, 32));
		barWin.setColumn(true);

		final TextSprite sprite = new TextSprite();
		sprite.setFill(RGB.WHITE);
		sprite.setFontSize(12);
		sprite.setTextAnchor(TextAnchor.MIDDLE);

		final SeriesLabelConfig<ChartDataDto> labelConfig = new SeriesLabelConfig<ChartDataDto>();
		labelConfig.setSpriteConfig(sprite);

		barWin.setLabelConfig(labelConfig);

		final ArrayList<String> legendTitles = new ArrayList<String>();
		legendTitles.add("Siegdifferenz");
		legendTitles.add("Siege");
		legendTitles.add("Niederlagen");
		barWin.setLegendTitles(legendTitles);
		barWin.setShownInLegend(false);
		barWin.setHighlighting(true);
		barWin.setHighlighter(new SeriesHighlighter() {
			@Override
			public void highlight(Sprite sprite) {
				sprite.setStroke(new RGB(0, 0, 0));
				DrawFx.createStrokeWidthAnimator(sprite, 3).run(250);
			}

			@Override
			public void unHighlight(Sprite sprite) {
				sprite.setStroke(Color.NONE);
				DrawFx.createStrokeWidthAnimator(sprite, 0).run(250);
			}
		});
		return barWin;
	}

	private Legend<ChartDataDto> createLegend() {
		final Legend<ChartDataDto> legend = new Legend<ChartDataDto>();
		legend.setPosition(Position.RIGHT);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);

		return legend;
	}

	public void loadGoalChart(ArrayList<ChartDataDto> result) {
		barWin.setShownInLegend(true);
		storeWin.replaceAll(result);
		chartWin.redrawChart();
	}

	public Chart<ChartDataDto> getChartWin() {
		return chartWin;
	}

}
