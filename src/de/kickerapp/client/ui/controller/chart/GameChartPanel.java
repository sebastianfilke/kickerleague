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

import de.kickerapp.client.properties.ChartProperty;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.shared.dto.ChartDataDto;

public class GameChartPanel extends BaseContainer {

	private ListStore<ChartDataDto> storeGame;

	private Chart<ChartDataDto> chartGame;

	private BarSeries<ChartDataDto> barGame;

	public GameChartPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		storeGame = new ListStore<ChartDataDto>(KickerProperties.CHART_PROPERTY.id());

		add(createGroupBarChart());
	}

	private Chart<ChartDataDto> createGroupBarChart() {
		chartGame = new Chart<ChartDataDto>();
		chartGame.setShadowChart(true);
		chartGame.setStore(storeGame);
		chartGame.setAnimated(true);

		chartGame.addAxis(createNumericAxis());
		chartGame.addAxis(createCategoryAxis());
		chartGame.addSeries(createBarSeries());
		chartGame.setLegend(createLegend());

		return chartGame;
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

		final PathSprite gridConfig = new PathSprite();
		gridConfig.setStroke(new RGB("#dfdfdf"));
		gridConfig.setFill(new RGB("#e3e3e3"));
		gridConfig.setZIndex(1);
		gridConfig.setStrokeWidth(1);
		numAxis.setGridOddConfig(gridConfig);

		final TextSprite title = new TextSprite("Spielbilanz");
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
		barGame = new BarSeries<ChartDataDto>();
		barGame.setYAxisPosition(Position.LEFT);
		barGame.addYField(ChartProperty.winDifference);
		barGame.addYField(KickerProperties.CHART_PROPERTY.wins());
		barGame.addYField(KickerProperties.CHART_PROPERTY.defeats());
		barGame.addColor(new RGB(17, 95, 166));
		barGame.addColor(new RGB(148, 174, 10));
		barGame.addColor(new RGB(166, 17, 32));
		barGame.setColumn(true);

		final TextSprite sprite = new TextSprite();
		sprite.setFill(RGB.WHITE);
		sprite.setFontSize(12);
		sprite.setTextAnchor(TextAnchor.MIDDLE);

		final SeriesLabelConfig<ChartDataDto> labelConfig = new SeriesLabelConfig<ChartDataDto>();
		labelConfig.setSpriteConfig(sprite);

		barGame.setLabelConfig(labelConfig);

		final ArrayList<String> legendTitles = new ArrayList<String>();
		legendTitles.add("Spieldifferenz");
		legendTitles.add("Siege");
		legendTitles.add("Niederlagen");
		barGame.setLegendTitles(legendTitles);
		barGame.setShownInLegend(false);
		barGame.setHighlighting(true);
		barGame.setHighlighter(new SeriesHighlighter() {
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
		return barGame;
	}

	private Legend<ChartDataDto> createLegend() {
		final Legend<ChartDataDto> legend = new Legend<ChartDataDto>();
		legend.setPosition(Position.TOP);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);

		return legend;
	}

	public void loadGameChart(ArrayList<ChartDataDto> result) {
		barGame.setShownInLegend(true);
		if (!result.isEmpty()) {
			barGame.setShownInLegend(true);
		} else {
			barGame.setShownInLegend(false);
		}
		storeGame.replaceAll(result);
		chartGame.redrawChart();
	}

	public Chart<ChartDataDto> getChartGame() {
		return chartGame;
	}

}
