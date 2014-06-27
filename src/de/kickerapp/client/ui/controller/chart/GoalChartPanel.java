package de.kickerapp.client.ui.controller.chart;

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
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.data.shared.ListStore;

import de.kickerapp.client.properties.ChartProperty;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.shared.dto.ChartDataDto;

public class GoalChartPanel extends BaseContainer {

	private ListStore<ChartDataDto> storeGoal;

	private Chart<ChartDataDto> chartGoal;

	private BarSeries<ChartDataDto> barGoal;

	public GoalChartPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		storeGoal = new ListStore<ChartDataDto>(KickerProperties.CHART_PROPERTY.id());

		add(createGroupBarChart());
	}

	private Chart<ChartDataDto> createGroupBarChart() {
		chartGoal = new Chart<ChartDataDto>();
		chartGoal.setShadowChart(true);
		chartGoal.setStore(storeGoal);
		chartGoal.setAnimated(true);

		chartGoal.addAxis(createNumericAxis());
		chartGoal.addAxis(createCategoryAxis());
		chartGoal.addSeries(createBarSeries());
		chartGoal.setLegend(createLegend());

		return chartGoal;
	}

	private NumericAxis<ChartDataDto> createNumericAxis() {
		final NumericAxis<ChartDataDto> numAxis = new NumericAxis<ChartDataDto>();
		numAxis.setPosition(Position.LEFT);
		numAxis.addField(ChartProperty.goalDifference);
		numAxis.addField(KickerProperties.CHART_PROPERTY.shotGoals());
		numAxis.addField(KickerProperties.CHART_PROPERTY.getGoals());
		numAxis.setAdjustMaximumByMajorUnit(true);
		numAxis.setAdjustMinimumByMajorUnit(true);
		numAxis.setDisplayGrid(true);

		final PathSprite gridConfig = new PathSprite();
		gridConfig.setStroke(new RGB("#dfdfdf"));
		gridConfig.setFill(new RGB("#e3e3e3"));
		gridConfig.setZIndex(1);
		gridConfig.setStrokeWidth(1);
		numAxis.setGridOddConfig(gridConfig);

		final TextSprite title = new TextSprite("Torbilanz");
		title.setFontSize(20);
		title.setFont("Tahoma");
		numAxis.setTitleConfig(title);

		return numAxis;
	}

	private CategoryAxis<ChartDataDto, String> createCategoryAxis() {
		final CategoryAxis<ChartDataDto, String> catAxis = new CategoryAxis<ChartDataDto, String>();
		catAxis.setField(KickerProperties.CHART_PROPERTY.month());
		catAxis.setPosition(Position.BOTTOM);

		final TextSprite title = new TextSprite("Monat");
		title.setFontSize(20);
		title.setFont("Tahoma");
		catAxis.setTitleConfig(title);

		return catAxis;
	}

	private BarSeries<ChartDataDto> createBarSeries() {
		barGoal = new BarSeries<ChartDataDto>();
		barGoal.setYAxisPosition(Position.LEFT);
		barGoal.addYField(ChartProperty.goalDifference);
		barGoal.addYField(KickerProperties.CHART_PROPERTY.shotGoals());
		barGoal.addYField(KickerProperties.CHART_PROPERTY.getGoals());
		barGoal.addColor(new RGB(17, 95, 166));
		barGoal.addColor(new RGB(148, 174, 10));
		barGoal.addColor(new RGB(166, 17, 32));
		barGoal.setColumn(true);

		final TextSprite sprite = new TextSprite();
		sprite.setFill(RGB.WHITE);
		sprite.setFontSize(12);
		sprite.setTextAnchor(TextAnchor.MIDDLE);

		final SeriesLabelConfig<ChartDataDto> labelConfig = new SeriesLabelConfig<ChartDataDto>();
		labelConfig.setSpriteConfig(sprite);

		barGoal.setLabelConfig(labelConfig);

		final ArrayList<String> legendTitles = new ArrayList<String>();
		legendTitles.add("Tordifferenz");
		legendTitles.add("Tore geschossen");
		legendTitles.add("Tore kassiert");
		barGoal.setLegendTitles(legendTitles);
		barGoal.setShownInLegend(false);
		barGoal.setHighlighting(true);
		barGoal.setHighlighter(new SeriesHighlighter() {
			@Override
			public void highlight(Sprite sprite) {
				sprite.setStroke(new RGB("#0b8db3"));
				DrawFx.createStrokeWidthAnimator(sprite, 3).run(250);
			}

			@Override
			public void unHighlight(Sprite sprite) {
				sprite.setStroke(Color.NONE);
				DrawFx.createStrokeWidthAnimator(sprite, 0).run(250);
			}
		});
		return barGoal;
	}

	private Legend<ChartDataDto> createLegend() {
		final Legend<ChartDataDto> legend = new Legend<ChartDataDto>();
		legend.setPosition(Position.TOP);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);

		return legend;
	}

	public void loadGoalChart(ArrayList<ChartDataDto> result) {
		if (!result.isEmpty()) {
			barGoal.setShownInLegend(true);
		} else {
			barGoal.setShownInLegend(false);
		}
		storeGoal.replaceAll(result);
		chartGoal.redrawChart();
	}

	public Chart<ChartDataDto> getChartGoal() {
		return chartGoal;
	}

}
