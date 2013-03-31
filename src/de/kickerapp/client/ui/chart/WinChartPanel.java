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

public class WinChartPanel extends BaseContainer {

	private ListStore<ChartDto> storeWin;

	private Chart<ChartDto> chartWin;

	private BarSeries<ChartDto> barWin;

	private boolean doUpdateWinChart;

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
		doUpdateWinChart = true;

		storeWin = new ListStore<ChartDto>(KickerProperties.CHART_PROPERTY.id());

		add(createGroupBarChart());
	}

	private Chart<ChartDto> createGroupBarChart() {
		chartWin = new Chart<ChartDto>();
		chartWin.setShadowChart(true);
		chartWin.setStore(storeWin);
		chartWin.setAnimated(true);

		chartWin.addAxis(createNumericAxis());
		chartWin.addAxis(createCategoryAxis());
		chartWin.addSeries(createBarSeries());
		chartWin.setLegend(createLegend());

		return chartWin;
	}

	private NumericAxis<ChartDto> createNumericAxis() {
		final NumericAxis<ChartDto> numAxis = new NumericAxis<ChartDto>();
		numAxis.setPosition(Position.LEFT);
		numAxis.addField(ChartProperty.winDifference);
		numAxis.addField(KickerProperties.CHART_PROPERTY.wins());
		numAxis.addField(KickerProperties.CHART_PROPERTY.losses());

		final TextSprite title = new TextSprite("Siegbilanz");
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
		barWin = new BarSeries<ChartDto>();
		barWin.setYAxisPosition(Position.LEFT);
		barWin.addYField(ChartProperty.winDifference);
		barWin.addYField(KickerProperties.CHART_PROPERTY.wins());
		barWin.addYField(KickerProperties.CHART_PROPERTY.losses());
		barWin.addColor(new RGB(17, 95, 166));
		barWin.addColor(new RGB(148, 174, 10));
		barWin.addColor(new RGB(166, 17, 32));
		barWin.setColumn(true);

		final TextSprite sprite = new TextSprite();
		sprite.setFill(RGB.WHITE);
		sprite.setFontSize(12);
		sprite.setTextAnchor(TextAnchor.MIDDLE);

		final SeriesLabelConfig<ChartDto> labelConfig = new SeriesLabelConfig<ChartDto>();
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

	private Legend<ChartDto> createLegend() {
		final Legend<ChartDto> legend = new Legend<ChartDto>();
		legend.setPosition(Position.RIGHT);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);

		return legend;
	}

	public void loadGoalChart(PlayerDto selectedPlayer) {
		if (doUpdateWinChart) {
			mask("Sieg-Statistik wird geladen...");
			KickerServices.CHART_SERVICE.getWinChart(selectedPlayer, new AsyncCallback<ArrayList<ChartDto>>() {
				@Override
				public void onSuccess(ArrayList<ChartDto> result) {
					barWin.setShownInLegend(true);
					storeWin.replaceAll(result);
					chartWin.redrawChart();
					doUpdateWinChart = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdateWinChart = false;
					unmask();
					AppExceptionHandler.handleException(caught);
				}
			});
		}
	}

	public boolean isDoUpdateWinChart() {
		return doUpdateWinChart;
	}

	public void setDoUpdateWinChart(boolean doUpdateWinChart) {
		this.doUpdateWinChart = doUpdateWinChart;
	}

}
