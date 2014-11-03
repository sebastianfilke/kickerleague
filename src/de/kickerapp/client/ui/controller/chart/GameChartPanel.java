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
import com.sencha.gxt.chart.client.chart.series.SeriesRenderer;
import com.sencha.gxt.chart.client.chart.series.SeriesToolTipConfig;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;

import de.kickerapp.client.properties.ChartGameProperty;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.shared.dto.ChartGameDto;

/**
 * Controller-Klasse zum Anzeigen von Spielstatistiken für ein Team bzw. Spieler.
 * 
 * @author Sebastian Filke
 */
public class GameChartPanel extends BaseContainer {

	/** Die Spielstatistiken für ein Team bzw. Spieler. */
	private ListStore<ChartGameDto> storeGame;
	/** Das Diagramm. */
	private Chart<ChartGameDto> chartGame;
	/** Das Balkendiagramm. */
	private BarSeries<ChartGameDto> barGame;

	/**
	 * Erzeugt einen neuen Controller Anzeigen von Spielstatistiken.
	 */
	public GameChartPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initLayout() {
		storeGame = new ListStore<ChartGameDto>(KickerProperties.CHART_GAME_PROPERTY.id());

		add(createGroupBarChart());
	}

	/**
	 * Erzeugt das Diagramm zum Anzeigen der Spielstatistiken.
	 * 
	 * @return Das erzeugte Diagramm.
	 */
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

	/**
	 * Erzeugt die Y-Achse des Diagramms.
	 * 
	 * @return Die erzeugte Y-Achse.
	 */
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
		gridConfig.setFill(new RGB("#f1f1f1"));
		gridConfig.setZIndex(1);
		gridConfig.setStrokeWidth(1);
		numAxis.setGridOddConfig(gridConfig);

		final TextSprite title = new TextSprite("Spielbilanz");
		title.setFontSize(20);
		title.setFont("Tahoma");
		numAxis.setTitleConfig(title);

		return numAxis;
	}

	/**
	 * Erzeugt die X-Achse des Diagramms.
	 * 
	 * @return Die erzeugte X-Achse.
	 */
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

	/**
	 * Erzeugt die das Balkendiagramm.
	 * 
	 * @return Das erzeugte Balkendiagramm.
	 */
	private BarSeries<ChartGameDto> createBarSeries() {
		barGame = new BarSeries<ChartGameDto>();
		barGame.setYAxisPosition(Position.LEFT);
		barGame.addYField(ChartGameProperty.winDifference);
		barGame.addYField(KickerProperties.CHART_GAME_PROPERTY.wins());
		barGame.addYField(KickerProperties.CHART_GAME_PROPERTY.defeats());
		barGame.addColor(new RGB("#115FA6"));
		barGame.addColor(new RGB("#94AE0A"));
		barGame.addColor(new RGB("#A61120"));
		barGame.setShownInLegend(false);
		barGame.setHighlighting(true);
		barGame.setColumn(true);

		barGame.setLabelConfig(createLabelConfig());
		barGame.setToolTipConfig(createToolTipConfig());
		barGame.setHighlighter(createHighlighter());
		barGame.setLegendTitles(createLegendTitles());
		barGame.setRenderer(createSeriesRenderer());

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
					sb.append(value == 1 || value == -1 ? " Spiel " : " Spiele ");
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
		toolTipConfig.setMinWidth(180);

		return toolTipConfig;
	}

	private SeriesHighlighter createHighlighter() {
		return new SeriesHighlighter() {
			@Override
			public void highlight(Sprite sprite) {
				sprite.setFill(new RGB("#333333"));
				sprite.redraw();
			}

			@Override
			public void unHighlight(Sprite sprite) {
				sprite.setFill(sprite.getStroke());
				sprite.redraw();
			}
		};
	}

	private ArrayList<String> createLegendTitles() {
		final ArrayList<String> legendTitles = new ArrayList<String>();

		legendTitles.add("Spieldifferenz");
		legendTitles.add("Siege");
		legendTitles.add("Niederlagen");

		return legendTitles;
	}

	private SeriesRenderer<ChartGameDto> createSeriesRenderer() {
		return new SeriesRenderer<ChartGameDto>() {
			@Override
			public void spriteRenderer(Sprite sprite, int index, ListStore<ChartGameDto> store) {
				sprite.setStroke(sprite.getFill());
				sprite.setOpacity(0.7);
			}
		};
	}

	private Legend<ChartGameDto> createLegend() {
		final Legend<ChartGameDto> legend = new Legend<ChartGameDto>();
		legend.setPosition(Position.TOP);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);

		final RectangleSprite borderConfig = new RectangleSprite();
		borderConfig.setStroke(RGB.GRAY);
		borderConfig.setFill(Color.NONE);
		borderConfig.setStrokeWidth(1);

		legend.setBorderConfig(borderConfig);

		return legend;
	}

	public void loadGameChart(ArrayList<ChartGameDto> result) {
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
