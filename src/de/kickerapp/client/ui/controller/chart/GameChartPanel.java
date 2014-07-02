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
import com.sencha.gxt.chart.client.chart.series.SeriesLabelProvider;
import com.sencha.gxt.chart.client.chart.series.SeriesToolTipConfig;
import com.sencha.gxt.chart.client.draw.DrawFx;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;

import de.kickerapp.client.properties.ChartGameProperty;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.shared.dto.ChartGameDto;

public class GameChartPanel extends BaseContainer {

	private ListStore<ChartGameDto> storeGame;

	private Chart<ChartGameDto> chartGame;

	private BarSeries<ChartGameDto> barGame;

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
		storeGame = new ListStore<ChartGameDto>(KickerProperties.CHART_GAME_PROPERTY.id());

		add(createGroupBarChart());
	}

	private Chart<ChartGameDto> createGroupBarChart() {
		chartGame = new Chart<ChartGameDto>();
		chartGame.setDefaultInsets(20);
		chartGame.setShadowChart(true);
		chartGame.setStore(storeGame);
		chartGame.setAnimated(true);

		chartGame.addAxis(createNumericAxis());
		chartGame.addAxis(createCategoryAxis());
		chartGame.addSeries(createBarSeries());
		chartGame.setLegend(createLegend());

		return chartGame;
	}

	private NumericAxis<ChartGameDto> createNumericAxis() {
		final NumericAxis<ChartGameDto> numAxis = new NumericAxis<ChartGameDto>();
		numAxis.setPosition(Position.LEFT);
		numAxis.addField(ChartGameProperty.winDifference);
		numAxis.addField(KickerProperties.CHART_GAME_PROPERTY.wins());
		numAxis.addField(KickerProperties.CHART_GAME_PROPERTY.defeats());
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

	private CategoryAxis<ChartGameDto, String> createCategoryAxis() {
		final CategoryAxis<ChartGameDto, String> catAxis = new CategoryAxis<ChartGameDto, String>();
		catAxis.setField(KickerProperties.CHART_GAME_PROPERTY.month());
		catAxis.setPosition(Position.BOTTOM);

		final TextSprite title = new TextSprite("Monat");
		title.setFontSize(20);
		title.setFont("Tahoma");
		catAxis.setTitleConfig(title);

		return catAxis;
	}

	private BarSeries<ChartGameDto> createBarSeries() {
		barGame = new BarSeries<ChartGameDto>();
		barGame.setYAxisPosition(Position.LEFT);
		barGame.addYField(ChartGameProperty.winDifference);
		barGame.addYField(KickerProperties.CHART_GAME_PROPERTY.wins());
		barGame.addYField(KickerProperties.CHART_GAME_PROPERTY.defeats());
		barGame.addColor(new RGB(17, 95, 166));
		barGame.addColor(new RGB(148, 174, 10));
		barGame.addColor(new RGB(166, 17, 32));
		barGame.setShownInLegend(false);
		barGame.setHighlighting(true);
		barGame.setColumn(true);

		barGame.setLabelConfig(createLabelConfig());
		barGame.setToolTipConfig(createToolTipConfig());
		barGame.setHighlighter(createHighlighter());
		barGame.setLegendTitles(createLegendTitles());

		return barGame;
	}

	private SeriesLabelConfig<ChartGameDto> createLabelConfig() {
		final TextSprite sprite = new TextSprite();
		sprite.setFill(RGB.WHITE);
		sprite.setFontSize(12);
		sprite.setTextAnchor(TextAnchor.MIDDLE);

		final SeriesLabelConfig<ChartGameDto> labelConfig = new SeriesLabelConfig<ChartGameDto>();
		labelConfig.setSpriteConfig(sprite);
		return labelConfig;
	}

	private SeriesToolTipConfig<ChartGameDto> createToolTipConfig() {
		final SeriesToolTipConfig<ChartGameDto> toolTipConfig = new SeriesToolTipConfig<ChartGameDto>();
		toolTipConfig.setLabelProvider(new SeriesLabelProvider<ChartGameDto>() {
			@Override
			public String getLabel(ChartGameDto item, ValueProvider<? super ChartGameDto, ? extends Number> valueProvider) {
				final int value = valueProvider.getValue(item).intValue();

				final StringBuilder sb = new StringBuilder(Integer.toString(Math.abs(value)));
				if (valueProvider.getPath().equals("wins")) {
					sb.append(value == 1 ? " Spiel gewonnen" : " Spiele gewonnen");
				} else if (valueProvider.getPath().equals("defeats")) {
					sb.append(value == 1 ? " Spiel verloren" : " Spiele verloren");
				} else if (valueProvider.getPath().equals("winDifference")) {
					sb.append(value == 1 ? " Spiel " : " Spiele ");
					if (value <= 0) {
						sb.append("mehr verloren");
					} else {
						sb.append("mehr gewonnen");
					}
				}
				sb.append(" im ").append(item.getMonth());

				return sb.toString();
			}
		});
		toolTipConfig.setTrackMouse(true);
		toolTipConfig.setHideDelay(200);

		return toolTipConfig;
	}

	private SeriesHighlighter createHighlighter() {
		final SeriesHighlighter highlighter = new SeriesHighlighter() {
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
		};
		return highlighter;
	}

	private ArrayList<String> createLegendTitles() {
		final ArrayList<String> legendTitles = new ArrayList<String>();

		legendTitles.add("Spieldifferenz");
		legendTitles.add("Siege");
		legendTitles.add("Niederlagen");

		return legendTitles;
	}

	private Legend<ChartGameDto> createLegend() {
		final Legend<ChartGameDto> legend = new Legend<ChartGameDto>();
		legend.setPosition(Position.TOP);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);

		return legend;
	}

	public void loadGameChart(ArrayList<ChartGameDto> result) {
		barGame.setShownInLegend(true);
		if (!result.isEmpty()) {
			barGame.setShownInLegend(true);
		} else {
			barGame.setShownInLegend(false);
		}
		storeGame.replaceAll(result);
		chartGame.redrawChart();
	}

	public Chart<ChartGameDto> getChartGame() {
		return chartGame;
	}

}
