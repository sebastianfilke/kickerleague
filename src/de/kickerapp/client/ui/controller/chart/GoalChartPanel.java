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
	protected void initLayout() {
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
		gridConfig.setFill(new RGB("#f1f1f1"));
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
		barGoal.addColor(new RGB("#115FA6"));
		barGoal.addColor(new RGB("#94AE0A"));
		barGoal.addColor(new RGB("#A61120"));
		barGoal.setShownInLegend(false);
		barGoal.setHighlighting(true);
		barGoal.setColumn(true);

		barGoal.setLabelConfig(crateLabelConfig());
		barGoal.setToolTipConfig(createToolTipConfig());
		barGoal.setHighlighter(crateHighlighter());
		barGoal.setLegendTitles(createLegendTitles());
		barGoal.setRenderer(createSeriesRenderer());

		return barGoal;
	}

	private SeriesLabelConfig<ChartGoalDto> crateLabelConfig() {
		final TextSprite sprite = new TextSprite();
		sprite.setTextAnchor(TextAnchor.MIDDLE);
		sprite.setFill(RGB.WHITE);
		sprite.setFontSize(12);

		final SeriesLabelConfig<ChartGoalDto> labelConfig = new SeriesLabelConfig<ChartGoalDto>();
		labelConfig.setSpriteConfig(sprite);

		return labelConfig;
	}

	private SeriesToolTipConfig<ChartGoalDto> createToolTipConfig() {
		final SeriesToolTipConfig<ChartGoalDto> toolTipConfig = new SeriesToolTipConfig<ChartGoalDto>();
		toolTipConfig.setLabelProvider(new SeriesLabelProvider<ChartGoalDto>() {
			@Override
			public String getLabel(ChartGoalDto item, ValueProvider<? super ChartGoalDto, ? extends Number> valueProvider) {
				final int value = valueProvider.getValue(item).intValue();

				final StringBuilder sb = new StringBuilder(Integer.toString(Math.abs(value)));
				if (valueProvider.getPath().equals("shotGoals")) {
					sb.append(value == 1 ? " Tor geschossen" : " Tore geschossen");
				} else if (valueProvider.getPath().equals("getGoals")) {
					sb.append(value == 1 ? " Tor kassiert" : " Tore kassiert");
				} else if (valueProvider.getPath().equals("goalDifference")) {
					sb.append(value == 1 || value == -1 ? " Tor " : " Tore ");
					if (value <= 0) {
						sb.append("mehr kassiert");
					} else {
						sb.append("mehr geschossen");
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

	private SeriesHighlighter crateHighlighter() {
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

		legendTitles.add("Tordifferenz");
		legendTitles.add("Tore geschossen");
		legendTitles.add("Tore kassiert");

		return legendTitles;
	}

	private Legend<ChartGoalDto> createLegend() {
		final Legend<ChartGoalDto> legend = new Legend<ChartGoalDto>();
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

	private SeriesRenderer<ChartGoalDto> createSeriesRenderer() {
		return new SeriesRenderer<ChartGoalDto>() {
			@Override
			public void spriteRenderer(Sprite sprite, int index, ListStore<ChartGoalDto> store) {
				sprite.setStroke(sprite.getFill());
				sprite.setOpacity(0.7);
			}
		};
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
