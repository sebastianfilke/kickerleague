package de.kickerapp.client.ui.chart;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
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

import de.kickerapp.client.exception.AppExceptionHandler;
import de.kickerapp.client.properties.ChartProperty;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.shared.dto.ChartDto;
import de.kickerapp.shared.dto.PlayerDto;

public class GoalChartPanel extends BaseContainer {

	private ListStore<ChartDto> storeGoal;

	private Chart<ChartDto> chartGoal;

	private BarSeries<ChartDto> barGoal;

	private boolean doUpdateGoalChart;

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
		doUpdateGoalChart = true;

		storeGoal = new ListStore<ChartDto>(KickerProperties.CHART_PROPERTY.id());

		add(createGroupBarChart());
	}

	private Chart<ChartDto> createGroupBarChart() {
		chartGoal = new Chart<ChartDto>();
		chartGoal.setShadowChart(true);
		chartGoal.setStore(storeGoal);
		chartGoal.setAnimated(true);

		chartGoal.addAxis(createNumericAxis());
		chartGoal.addAxis(createCategoryAxis());
		chartGoal.addSeries(createBarSeries());
		chartGoal.setLegend(createLegend());

		return chartGoal;
	}

	private NumericAxis<ChartDto> createNumericAxis() {
		final NumericAxis<ChartDto> numAxis = new NumericAxis<ChartDto>();
		numAxis.setPosition(Position.LEFT);
		numAxis.addField(ChartProperty.goalDifference);
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
		barGoal = new BarSeries<ChartDto>();
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

		final SeriesLabelConfig<ChartDto> labelConfig = new SeriesLabelConfig<ChartDto>();
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
				sprite.setStroke(new RGB(0, 0, 0));
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

	private Legend<ChartDto> createLegend() {
		final Legend<ChartDto> legend = new Legend<ChartDto>();
		legend.setPosition(Position.RIGHT);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);

		return legend;
	}

	public void loadGoalChart(PlayerDto selectedPlayer) {
		if (doUpdateGoalChart) {
			mask("Tor-Statistik wird geladen...");
			KickerServices.CHART_SERVICE.getGoalChart(selectedPlayer, new AsyncCallback<ArrayList<ChartDto>>() {
				@Override
				public void onSuccess(ArrayList<ChartDto> result) {
					barGoal.setShownInLegend(true);
					storeGoal.replaceAll(result);
					chartGoal.redrawChart();
					doUpdateGoalChart = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdateGoalChart = false;
					unmask();
					AppExceptionHandler.handleException(caught);
				}
			});
		}
	}

	public boolean isDoUpdateGoalChart() {
		return doUpdateGoalChart;
	}

	public void setDoUpdateGoalChart(boolean doUpdateGoalChart) {
		this.doUpdateGoalChart = doUpdateGoalChart;
	}

}
