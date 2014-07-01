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
import com.sencha.gxt.chart.client.draw.DrawFx;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.data.shared.ListStore;

import de.kickerapp.client.properties.ChartGoalProperty;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.shared.dto.ChartGoalDto;

public class GoalChartPanel extends BaseContainer {

	private ListStore<ChartGoalDto> storeGoal;

	private Chart<ChartGoalDto> chartGoal;

	private BarSeries<ChartGoalDto> barGoal;

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
		storeGoal = new ListStore<ChartGoalDto>(KickerProperties.CHART_GOAL_PROPERTY.id());

		add(createGroupBarChart());
	}

	private Chart<ChartGoalDto> createGroupBarChart() {
		chartGoal = new Chart<ChartGoalDto>();
		chartGoal.setDefaultInsets(20);
		chartGoal.setShadowChart(true);
		chartGoal.setStore(storeGoal);
		chartGoal.setAnimated(true);

		chartGoal.addAxis(createNumericAxis());
		chartGoal.addAxis(createCategoryAxis());
		chartGoal.addSeries(createBarSeries());
		chartGoal.setLegend(createLegend());

		return chartGoal;
	}

	private NumericAxis<ChartGoalDto> createNumericAxis() {
		final NumericAxis<ChartGoalDto> numAxis = new NumericAxis<ChartGoalDto>();
		numAxis.setPosition(Position.LEFT);
		numAxis.addField(ChartGoalProperty.goalDifference);
		numAxis.addField(KickerProperties.CHART_GOAL_PROPERTY.shotGoals());
		numAxis.addField(KickerProperties.CHART_GOAL_PROPERTY.getGoals());
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

	private CategoryAxis<ChartGoalDto, String> createCategoryAxis() {
		final CategoryAxis<ChartGoalDto, String> catAxis = new CategoryAxis<ChartGoalDto, String>();
		catAxis.setField(KickerProperties.CHART_GOAL_PROPERTY.month());
		catAxis.setPosition(Position.BOTTOM);

		final TextSprite title = new TextSprite("Monat");
		title.setFontSize(20);
		title.setFont("Tahoma");
		catAxis.setTitleConfig(title);

		return catAxis;
	}

	private BarSeries<ChartGoalDto> createBarSeries() {
		barGoal = new BarSeries<ChartGoalDto>();
		barGoal.setYAxisPosition(Position.LEFT);
		barGoal.addYField(ChartGoalProperty.goalDifference);
		barGoal.addYField(KickerProperties.CHART_GOAL_PROPERTY.shotGoals());
		barGoal.addYField(KickerProperties.CHART_GOAL_PROPERTY.getGoals());
		barGoal.addColor(new RGB(17, 95, 166));
		barGoal.addColor(new RGB(148, 174, 10));
		barGoal.addColor(new RGB(166, 17, 32));
		barGoal.setColumn(true);

		final TextSprite sprite = new TextSprite();
		sprite.setFill(RGB.WHITE);
		sprite.setFontSize(12);
		sprite.setTextAnchor(TextAnchor.MIDDLE);

		final SeriesLabelConfig<ChartGoalDto> labelConfig = new SeriesLabelConfig<ChartGoalDto>();
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
				final RGB rgb = (RGB) sprite.getFill();
				sprite.setStroke(rgb.getLighter(0.2));
				DrawFx.createStrokeWidthAnimator(sprite, 3).run(250);
			}

			@Override
			public void unHighlight(Sprite sprite) {
				DrawFx.createStrokeWidthAnimator(sprite, 0).run(250);
			}
		});
		return barGoal;
	}

	private Legend<ChartGoalDto> createLegend() {
		final Legend<ChartGoalDto> legend = new Legend<ChartGoalDto>();
		legend.setPosition(Position.TOP);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);

		return legend;
	}

	public void loadGoalChart(ArrayList<ChartGoalDto> result) {
		if (!result.isEmpty()) {
			barGoal.setShownInLegend(true);
		} else {
			barGoal.setShownInLegend(false);
		}
		storeGoal.replaceAll(result);
		chartGoal.redrawChart();
	}

	public Chart<ChartGoalDto> getChartGoal() {
		return chartGoal;
	}

}
