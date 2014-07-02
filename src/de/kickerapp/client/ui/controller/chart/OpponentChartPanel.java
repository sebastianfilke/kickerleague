package de.kickerapp.client.ui.controller.chart;

import java.util.ArrayList;

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
		pieOpponent.setAngleField(KickerProperties.CHART_OPPONENT_PROPERTY.playedGames());
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
				final StringBuilder sb = new StringBuilder();

				sb.append(item.getOpponentName());
				sb.append("\n");
				sb.append(item.getPlayedGames());
				sb.append(item.getPlayedGames() == 1 ? " Spiel" : " Spiele");

				return sb.toString();
			}
		});
		labelConfig.setSpriteConfig(textConfig);
		pieOpponent.setLabelConfig(labelConfig);

		final SeriesToolTipConfig<ChartOpponentDto> toolTipConfig = new SeriesToolTipConfig<ChartOpponentDto>();
		toolTipConfig.setLabelProvider(null);
		toolTipConfig.setTrackMouse(true);
		toolTipConfig.setHideDelay(200);

		pieOpponent.addSeriesItemOverHandler(new SeriesItemOverHandler<ChartOpponentDto>() {
			@Override
			public void onSeriesOverItem(SeriesItemOverEvent<ChartOpponentDto> event) {
				final ChartOpponentDto chartOpponentDto = event.getItem();
				toolTipConfig.setTitleHtml("Spielbilanz gegen " + chartOpponentDto.getOpponentName());

				final StringBuilder sb = new StringBuilder();
				sb.append("<br>Siege: " + chartOpponentDto.getWins());
				sb.append(" | Niederlagen: " + chartOpponentDto.getDefeats());

				toolTipConfig.setBodyHtml(sb.toString());
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
		addColorsForResult(result);
		storeOpponent.replaceAll(result);
		chartOpponent.redrawChart();
	}

	private void addColorsForResult(ArrayList<ChartOpponentDto> result) {
		for (int i = 0; i < result.size(); i++) {
			final Color color = new RGB((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
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
