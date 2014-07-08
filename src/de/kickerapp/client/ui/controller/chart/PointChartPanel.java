package de.kickerapp.client.ui.controller.chart;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.chart.series.Series;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelProvider;
import com.sencha.gxt.chart.client.chart.series.SeriesToolTipConfig;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;

import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.shared.dto.ChartPointDto;

public class PointChartPanel extends BaseContainer {

	private ListStore<ChartPointDto> storePoint;

	private Chart<ChartPointDto> chartPoint;

	private LineSeries<ChartPointDto> linePoint;

	public PointChartPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		storePoint = new ListStore<ChartPointDto>(KickerProperties.CHART_POINT_PROPERTY.id());

		add(createLineChart());
	}

	private Widget createLineChart() {
		chartPoint = new Chart<ChartPointDto>();
		chartPoint.setDefaultInsets(20);
		chartPoint.setStore(storePoint);
		chartPoint.setAnimated(true);

		chartPoint.addAxis(createNumericAxis());
		chartPoint.addAxis(createCategoryAxis());
		chartPoint.addSeries(createLineSeries());
		chartPoint.setLegend(createLegend());

		return chartPoint;
	}

	private NumericAxis<ChartPointDto> createNumericAxis() {
		final NumericAxis<ChartPointDto> numAxis = new NumericAxis<ChartPointDto>();
		numAxis.setPosition(Position.LEFT);
		numAxis.addField(KickerProperties.CHART_POINT_PROPERTY.points());
		numAxis.setAdjustMaximumByMajorUnit(true);
		numAxis.setAdjustMinimumByMajorUnit(true);
		numAxis.setDisplayGrid(true);

		final PathSprite gridConfig = new PathSprite();
		gridConfig.setStroke(new RGB("#dfdfdf"));
		gridConfig.setFill(new RGB("#e3e3e3"));
		gridConfig.setZIndex(1);
		gridConfig.setStrokeWidth(1);
		numAxis.setGridOddConfig(gridConfig);

		final TextSprite title = new TextSprite("Punkte");
		title.setFontSize(20);
		title.setFont("Tahoma");
		numAxis.setTitleConfig(title);

		return numAxis;
	}

	private CategoryAxis<ChartPointDto, Integer> createCategoryAxis() {
		final CategoryAxis<ChartPointDto, Integer> catAxis = new CategoryAxis<ChartPointDto, Integer>();
		catAxis.setField(KickerProperties.CHART_POINT_PROPERTY.matchNumber());
		catAxis.setPosition(Position.BOTTOM);

		final TextSprite title = new TextSprite("Spielnummer");
		title.setFontSize(20);
		title.setFont("Tahoma");
		catAxis.setTitleConfig(title);

		return catAxis;
	}

	private Series<ChartPointDto> createLineSeries() {
		linePoint = new LineSeries<ChartPointDto>();
		linePoint.setYAxisPosition(Position.LEFT);
		linePoint.setYField(KickerProperties.CHART_POINT_PROPERTY.points());
		linePoint.setStroke(new RGB(32, 68, 186));
		linePoint.setFill(new RGB(32, 68, 186));
		linePoint.setShowMarkers(true);
		linePoint.setSmooth(true);

		final Sprite marker = Primitives.diamond(0, 0, 4);
		marker.setFill(new RGB(32, 68, 186));
		linePoint.setMarkerConfig(marker);
		linePoint.setHighlighting(true);
		linePoint.setLegendTitle("Punkte");

		linePoint.setToolTipConfig(createToolTipConfig());

		return linePoint;
	}

	private SeriesToolTipConfig<ChartPointDto> createToolTipConfig() {
		final SeriesToolTipConfig<ChartPointDto> toolTipConfig = new SeriesToolTipConfig<ChartPointDto>();
		toolTipConfig.setLabelProvider(new SeriesLabelProvider<ChartPointDto>() {
			@Override
			public String getLabel(ChartPointDto item, ValueProvider<? super ChartPointDto, ? extends Number> valueProvider) {
				final int value = valueProvider.getValue(item).intValue();

				final StringBuilder sb = new StringBuilder(Integer.toString(value));
				sb.append(" Punkte nach Spiel ").append(item.getMatchNumber());

				return sb.toString();
			}
		});
		toolTipConfig.setTrackMouse(true);
		toolTipConfig.setHideDelay(200);

		return toolTipConfig;
	}

	private Legend<ChartPointDto> createLegend() {
		final Legend<ChartPointDto> legend = new Legend<ChartPointDto>();
		legend.setPosition(Position.TOP);
		legend.setItemHighlighting(true);
		legend.setItemHiding(false);

		final RectangleSprite borderConfig = new RectangleSprite();
		borderConfig.setStroke(RGB.GRAY);
		borderConfig.setFill(Color.NONE);
		borderConfig.setStrokeWidth(1);

		legend.setBorderConfig(borderConfig);

		return legend;
	}

	public void loadPointChart(ArrayList<ChartPointDto> result) {
		if (!result.isEmpty()) {
			linePoint.setShownInLegend(true);
		} else {
			linePoint.setShownInLegend(false);
		}
		storePoint.replaceAll(result);
		chartPoint.redrawChart();
	}

	public Chart<ChartPointDto> getChartPoint() {
		return chartPoint;
	}

}
