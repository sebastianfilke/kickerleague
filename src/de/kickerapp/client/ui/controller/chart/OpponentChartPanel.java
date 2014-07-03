package de.kickerapp.client.ui.controller.chart;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.event.SeriesItemOverEvent;
import com.sencha.gxt.chart.client.chart.event.SeriesItemOverEvent.SeriesItemOverHandler;
import com.sencha.gxt.chart.client.chart.series.AreaHighlighter;
import com.sencha.gxt.chart.client.chart.series.PieSeries;
import com.sencha.gxt.chart.client.chart.series.Series;
import com.sencha.gxt.chart.client.chart.series.Series.LabelPosition;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelProvider;
import com.sencha.gxt.chart.client.chart.series.SeriesRenderer;
import com.sencha.gxt.chart.client.chart.series.SeriesToolTipConfig;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.StringLabelProvider;

import de.kickerapp.client.properties.ChartOpponentProperty;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.shared.dto.ChartOpponentDto;

public class OpponentChartPanel extends BaseContainer {

	private ListStore<ChartOpponentDto> storeOpponent;

	private Chart<ChartOpponentDto> chartOpponent;

	private PieSeries<ChartOpponentDto> pieOpponent;

	public OpponentChartPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		storeOpponent = new ListStore<ChartOpponentDto>(KickerProperties.CHART_OPPONENT_PROPERTY.id());

		add(createPieChart());
	}

	private Chart<ChartOpponentDto> createPieChart() {
		chartOpponent = new Chart<ChartOpponentDto>();
		chartOpponent.setStore(storeOpponent);
		chartOpponent.setDefaultInsets(50);
		chartOpponent.setAnimated(true);

		chartOpponent.addSeries(createPieSeries());
		chartOpponent.setLegend(createLegend());

		return chartOpponent;
	}

	private Series<ChartOpponentDto> createPieSeries() {
		pieOpponent = new PieSeries<ChartOpponentDto>();
		pieOpponent.setAngleField(ChartOpponentProperty.playedMatches);
		pieOpponent.setHighlighter(new AreaHighlighter());
		pieOpponent.setHighlighting(true);
		pieOpponent.setPopOutMargin(0);
		pieOpponent.addColor(new RGB((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
		pieOpponent.addColor(new RGB((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));

		final TextSprite textConfig = new TextSprite();
		textConfig.setFont("Arial");
		textConfig.setFontSize(15);
		textConfig.setTextBaseline(TextBaseline.MIDDLE);
		textConfig.setTextAnchor(TextAnchor.MIDDLE);
		textConfig.setFill(new RGB("#333"));

		final SeriesLabelConfig<ChartOpponentDto> labelConfig = new SeriesLabelConfig<ChartOpponentDto>();
		labelConfig.setLabelPosition(LabelPosition.START);
		labelConfig.setLabelContrast(true);
		labelConfig.setLabelProvider(new SeriesLabelProvider<ChartOpponentDto>() {
			@Override
			public String getLabel(ChartOpponentDto item, ValueProvider<? super ChartOpponentDto, ? extends Number> valueProvider) {
				final int matches = item.getWins() + item.getDefeats();

				final StringBuilder sb = new StringBuilder();

				sb.append(item.getOpponentName());
				sb.append("\n");
				sb.append(matches);
				sb.append(matches == 1 ? " Spiel" : " Spiele");
				sb.append(" (").append(item.getPercentageMatches()).append("%)");

				return sb.toString();
			}
		});
		labelConfig.setSpriteConfig(textConfig);
		pieOpponent.setLabelConfig(labelConfig);

		final SeriesToolTipConfig<ChartOpponentDto> toolTipConfig = new SeriesToolTipConfig<ChartOpponentDto>();
		toolTipConfig.setLabelProvider(null);
		toolTipConfig.setTrackMouse(true);
		toolTipConfig.setHideDelay(200);
		toolTipConfig.setMinWidth(400);

		pieOpponent.addSeriesItemOverHandler(new SeriesItemOverHandler<ChartOpponentDto>() {
			@Override
			public void onSeriesOverItem(SeriesItemOverEvent<ChartOpponentDto> event) {
				final ChartOpponentDto chartOpponentDto = event.getItem();
				toolTipConfig.setTitleHtml("Spielbilanz gegen " + chartOpponentDto.getOpponentName());

				final StringBuilder sbWins = new StringBuilder();
				sbWins.append(chartOpponentDto.getWins()).append(" (").append(chartOpponentDto.getPercentageWins()).append("%)");

				final StringBuilder sbDefeats = new StringBuilder();
				sbDefeats.append(chartOpponentDto.getDefeats()).append(" (").append(chartOpponentDto.getPercentageDefeats()).append("%)");

				toolTipConfig.setBodyHtml(createFlexTable(sbWins.toString(), sbDefeats.toString()).getElement().getInnerHTML());
				pieOpponent.setToolTipConfig(toolTipConfig);
			}
		});
		pieOpponent.setLegendValueProvider(KickerProperties.CHART_OPPONENT_PROPERTY.opponentName(), new StringLabelProvider<String>());
		pieOpponent.setRenderer(new SeriesRenderer<ChartOpponentDto>() {
			@Override
			public void spriteRenderer(Sprite sprite, int index, ListStore<ChartOpponentDto> store) {
				sprite.setStroke(RGB.WHITE);
				sprite.setStrokeWidth(2);
				sprite.redraw();
			}
		});
		return pieOpponent;
	}

	private FlexTable createFlexTable(String wins, String defeats) {
		final FlexTable ftInfo = new FlexTable();
		ftInfo.setHeight("300px");
		ftInfo.setWidth("300px");
		ftInfo.setBorderWidth(2);
		ftInfo.setCellSpacing(20);
		ftInfo.setCellPadding(20);

		ftInfo.setHTML(0, 0, "Siege");
		ftInfo.setWidget(1, 0, new HTML(createHtml(wins)));
		// ftInfo.setHTML(0, 1, "Niederlagen");
		// ftInfo.setHTML(1, 1, createHtml(defeats));

		final FlexCellFormatter formatter = ftInfo.getFlexCellFormatter();
		formatter.setWidth(0, 0, "120px");
		formatter.setHeight(0, 0, "40px");
		formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);

		formatter.setWidth(1, 0, "120px");
		formatter.setHeight(1, 0, "60px");
		formatter.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_MIDDLE);

		// formatter.setWidth(0, 1, "120");
		// formatter.setHeight(0, 1, "40");
		// formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		// formatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		//
		// formatter.setWidth(1, 1, "120");
		// formatter.setHeight(1, 1, "60");
		// formatter.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
		// formatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_MIDDLE);

		return ftInfo;
	}

	private String createHtml(String text) {
		return "<span style='font-size:20px; font-family:Arial; font-weight:bold;'>" + text + "</span>";
	}

	private Legend<ChartOpponentDto> createLegend() {
		final Legend<ChartOpponentDto> legend = new Legend<ChartOpponentDto>();
		legend.setPosition(Position.RIGHT);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);

		return legend;
	}

	public void loadOpponentChart(ArrayList<ChartOpponentDto> result) {
		if (!result.isEmpty()) {
			pieOpponent.setShownInLegend(true);
		} else {
			pieOpponent.setShownInLegend(false);
		}
		// Workaround für die IndexOutOfBoundsException
		pieOpponent.clear();
		//
		addColorsForResult(result);
		storeOpponent.replaceAll(result);
		chartOpponent.redrawChart();
	}

	private void addColorsForResult(ArrayList<ChartOpponentDto> result) {
		for (int i = 0; i < result.size(); i++) {
			final Color color = new RGB((int) (Math.random() * 255), 255, (int) (Math.random() * 255));
			if (pieOpponent.getColors().size() <= result.size()) {
				pieOpponent.addColor(color);
			} else {
				break;
			}
		}
	}

	public Chart<ChartOpponentDto> getChartGoal() {
		return chartOpponent;
	}

}
