package de.kickerapp.client.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
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
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.event.UpdatePanelEventHandler;
import de.kickerapp.client.exception.AppExceptionHandler;
import de.kickerapp.client.properties.ChartProperty;
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.properties.PlayerProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.images.KickerIcons;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppComboBox;
import de.kickerapp.client.widgets.AppContentPanel;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.ChartDto;
import de.kickerapp.shared.dto.PlayerDto;

public class SinglePlayerChartPanel extends BaseContainer implements UpdatePanelEventHandler {

	private AppComboBox<PlayerDto> cbPlayer;

	private ListStore<PlayerDto> storePlayer;

	private ListStore<ChartDto> storeWin;

	private ListStore<ChartDto> storeGoal;

	private Chart<ChartDto> chartGoal;

	private boolean doUpdatePlayerList, doUpdateGoalChart;

	private BarSeries<ChartDto> bar;

	private ToggleGroup tgChart;

	public SinglePlayerChartPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		doUpdatePlayerList = true;
		doUpdateGoalChart = true;

		storeGoal = new ListStore<ChartDto>(KickerProperties.CHART_PROPERTY.id());
		storeWin = new ListStore<ChartDto>(KickerProperties.CHART_PROPERTY.id());

		add(createBlcSingleStats());
	}

	private BorderLayoutContainer createBlcSingleStats() {
		final BorderLayoutContainer blcSinglePlayer = new BorderLayoutContainer();

		final BorderLayoutData northData = new BorderLayoutData(0.2);
		northData.setSplit(true);

		final AppContentPanel panel = new AppContentPanel();
		panel.setHeaderVisible(false);
		panel.setBodyBorder(false);
		panel.setBorders(false);

		final VerticalLayoutContainer vlcPlayerInfo = new VerticalLayoutContainer();
		vlcPlayerInfo.add(createToolBarPlayerInfo(), new VerticalLayoutData(1, -1));
		vlcPlayerInfo.add(panel, new VerticalLayoutData(1, 1));

		final VerticalLayoutContainer vlcCharts = new VerticalLayoutContainer();
		vlcCharts.add(createToolBarCharts(), new VerticalLayoutData(1, -1));
		vlcCharts.add(createGroupBarChartForSingleGoals(), new VerticalLayoutData(1, 1));

		blcSinglePlayer.setNorthWidget(vlcPlayerInfo, northData);
		blcSinglePlayer.setCenterWidget(vlcCharts);

		return blcSinglePlayer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initHandlers() {
		super.initHandlers();

		AppEventBus.addHandler(UpdatePanelEvent.ALL, this);
		AppEventBus.addHandler(UpdatePanelEvent.TABLES, this);
		AppEventBus.addHandler(UpdatePanelEvent.CHARTS, this);
	}

	private ToolBar createToolBarPlayerInfo() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		toolBar.add(createBtnUpdate());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(createPlayerComboBox());

		return toolBar;
	}

	private AppButton createBtnUpdate() {
		final AppButton btnListUpdate = new AppButton("Aktualisieren", KickerIcons.ICON.tableRefresh());
		btnListUpdate.setToolTip("Aktualisiert die Liste der eingetragenen Spieler in der Datenbank");
		btnListUpdate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				setDoUpdate();
				getPlayerList();
			}

			private void setDoUpdate() {
				doUpdatePlayerList = true;
			}
		});
		return btnListUpdate;
	}

	private AppComboBox<PlayerDto> createPlayerComboBox() {
		final PlayerProperty props = GWT.create(PlayerProperty.class);

		storePlayer = new ListStore<PlayerDto>(props.id());

		cbPlayer = new AppComboBox<PlayerDto>(storePlayer, PlayerProperty.label);
		cbPlayer.setTriggerAction(TriggerAction.ALL);
		cbPlayer.setEmptyText("Spieler wählen...");
		cbPlayer.setWidth(250);
		cbPlayer.addSelectionHandler(new SelectionHandler<PlayerDto>() {
			@Override
			public void onSelection(SelectionEvent<PlayerDto> event) {
				final PlayerDto selectedPlayer = event.getSelectedItem();
				if (selectedPlayer != null) {
					final ToggleButton tbtn = (ToggleButton) tgChart.getValue();
					if (tbtn.getId().equals("singleGoalsChart")) {
						setDoUpdate();
						loadGoalChart(selectedPlayer);
					} else if (tbtn.getId().equals("singleWinsChart")) {

					}
				}
			}

			private void setDoUpdate() {
				doUpdateGoalChart = true;
			}
		});

		return cbPlayer;
	}

	private ToolBar createToolBarCharts() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		final ToggleButton btnGoalChart = createBtnGoalChart();
		final ToggleButton btnWinsChart = createBtnWinsChart();

		tgChart = new ToggleGroup();
		tgChart.add(btnGoalChart);
		tgChart.add(btnWinsChart);
		tgChart.setValue(btnGoalChart);

		toolBar.add(createBtnUpdateChart());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(btnGoalChart);
		toolBar.add(btnWinsChart);

		return toolBar;
	}

	private AppButton createBtnUpdateChart() {
		final AppButton btnUpdate = new AppButton("Aktualisieren", KickerIcons.ICON.tableRefresh());
		btnUpdate.setToolTip("Aktualisiert die momentan gewählte Statistik");
		btnUpdate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				final PlayerDto selectedPlayer = cbPlayer.getValue();
				if (selectedPlayer != null) {
					final ToggleButton tbtn = (ToggleButton) tgChart.getValue();
					if (tbtn.getId().equals("singleGoalsChart")) {
						setDoUpdate();
						loadGoalChart(selectedPlayer);
					} else if (tbtn.getId().equals("singleWinsChart")) {

					}
				}
			}

			private void setDoUpdate() {
				doUpdateGoalChart = true;
			}
		});
		return btnUpdate;
	}

	private ToggleButton createBtnGoalChart() {
		final ToggleButton btnGoalChart = new ToggleButton("Torstatistik");
		btnGoalChart.setToolTip("Zeigt die Torstatistik für den aktuell gewählten Spieler");
		btnGoalChart.setIcon(KickerIcons.ICON.chartBar());
		btnGoalChart.setId("singleGoalsChart");
		btnGoalChart.setAllowDepress(false);
		btnGoalChart.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					final PlayerDto selectedPlayer = cbPlayer.getValue();
					if (selectedPlayer != null) {
						setDoUpdate();
						loadGoalChart(selectedPlayer);
					}
				}
			}

			private void setDoUpdate() {
				doUpdateGoalChart = true;
			}
		});
		return btnGoalChart;
	}

	private ToggleButton createBtnWinsChart() {
		final ToggleButton btnWinsChart = new ToggleButton("Siegstatistik");
		btnWinsChart.setToolTip("Zeigt die Siegstatistik für den aktuell gewählten Spieler");
		btnWinsChart.setIcon(KickerIcons.ICON.chartBar());
		btnWinsChart.setId("singleWinsChart");
		btnWinsChart.setAllowDepress(false);
		btnWinsChart.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					final PlayerDto selectedPlayer = cbPlayer.getValue();
					if (selectedPlayer != null) {
						setDoUpdate();
						// loadGoalChart(selectedPlayer);
					}
				}
			}

			private void setDoUpdate() {
				doUpdateGoalChart = true;
			}
		});
		return btnWinsChart;
	}

	private Chart<ChartDto> createGroupBarChartForSingleGoals() {
		chartGoal = new Chart<ChartDto>();
		chartGoal.setShadowChart(true);
		chartGoal.setStore(storeGoal);
		chartGoal.setAnimated(true);

		chartGoal.addAxis(createNumericAxisForSingleGoals());
		chartGoal.addAxis(createCategoryAxis());
		chartGoal.addSeries(createBarSeriesForSingleGoals());
		chartGoal.setLegend(createLegend());

		return chartGoal;
	}

	private NumericAxis<ChartDto> createNumericAxisForSingleGoals() {
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

	private BarSeries<ChartDto> createBarSeriesForSingleGoals() {
		bar = new BarSeries<ChartDto>();
		bar.setYAxisPosition(Position.LEFT);
		bar.addYField(ChartProperty.goalDifference);
		bar.addYField(KickerProperties.CHART_PROPERTY.shotGoals());
		bar.addYField(KickerProperties.CHART_PROPERTY.getGoals());
		bar.addColor(new RGB(17, 95, 166));
		bar.addColor(new RGB(148, 174, 10));
		bar.addColor(new RGB(166, 17, 32));
		bar.setColumn(true);

		final TextSprite sprite = new TextSprite();
		sprite.setFill(RGB.WHITE);
		sprite.setFontSize(12);
		sprite.setTextAnchor(TextAnchor.MIDDLE);

		final SeriesLabelConfig<ChartDto> labelConfig = new SeriesLabelConfig<ChartDto>();
		labelConfig.setSpriteConfig(sprite);

		bar.setLabelConfig(labelConfig);

		final ArrayList<String> legendTitles = new ArrayList<String>();
		legendTitles.add("Tordifferenz");
		legendTitles.add("Tore geschossen");
		legendTitles.add("Tore kassiert");
		bar.setLegendTitles(legendTitles);
		bar.setShownInLegend(false);
		bar.setHighlighting(true);
		bar.setHighlighter(new SeriesHighlighter() {
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
		return bar;
	}

	private Legend<ChartDto> createLegend() {
		final Legend<ChartDto> legend = new Legend<ChartDto>();
		legend.setPosition(Position.RIGHT);
		legend.setItemHighlighting(true);
		legend.setItemHiding(true);

		return legend;
	}

	private Chart<ChartDto> createGroupBarChartForSingleWins() {
		chartGoal = new Chart<ChartDto>();
		chartGoal.setShadowChart(true);
		chartGoal.setStore(storeWin);
		chartGoal.setAnimated(true);

		chartGoal.addAxis(createNumericAxisForSingleWins());
		chartGoal.addAxis(createCategoryAxis());
		chartGoal.addSeries(createBarSeriesForSingleWins());
		chartGoal.setLegend(createLegend());

		return chartGoal;
	}

	private NumericAxis<ChartDto> createNumericAxisForSingleWins() {
		final NumericAxis<ChartDto> numAxis = new NumericAxis<ChartDto>();
		numAxis.setPosition(Position.LEFT);
		numAxis.addField(ChartProperty.winsDifference);
		numAxis.addField(KickerProperties.CHART_PROPERTY.wins());
		numAxis.addField(KickerProperties.CHART_PROPERTY.losses());

		final TextSprite title = new TextSprite("Siegbilanz");
		title.setFontSize(20);
		title.setFont("Tahoma");
		numAxis.setTitleConfig(title);

		numAxis.setDisplayGrid(true);

		return numAxis;
	}

	private BarSeries<ChartDto> createBarSeriesForSingleWins() {
		final BarSeries<ChartDto> bar = new BarSeries<ChartDto>();
		bar.setYAxisPosition(Position.LEFT);
		bar.addYField(ChartProperty.winsDifference);
		bar.addYField(KickerProperties.CHART_PROPERTY.wins());
		bar.addYField(KickerProperties.CHART_PROPERTY.losses());
		bar.addColor(new RGB(17, 95, 166));
		bar.addColor(new RGB(148, 174, 10));
		bar.addColor(new RGB(166, 17, 32));
		bar.setColumn(true);

		final TextSprite sprite = new TextSprite();
		sprite.setFill(RGB.WHITE);
		sprite.setFontSize(12);
		sprite.setTextAnchor(TextAnchor.MIDDLE);

		final SeriesLabelConfig<ChartDto> labelConfig = new SeriesLabelConfig<ChartDto>();
		labelConfig.setSpriteConfig(sprite);

		bar.setLabelConfig(labelConfig);

		final ArrayList<String> legendTitles = new ArrayList<String>();
		legendTitles.add("Siegdifferenz");
		legendTitles.add("Siege");
		legendTitles.add("Niederlagen");
		bar.setLegendTitles(legendTitles);
		bar.setShownInLegend(false);
		bar.setHighlighting(true);
		bar.setHighlighter(new SeriesHighlighter() {
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
		return bar;
	}

	protected void getPlayerList() {
		if (doUpdatePlayerList) {
			mask("Aktualisiere...");
			KickerServices.PLAYER_SERVICE.getAllPlayers(MatchType.NONE, new AsyncCallback<ArrayList<PlayerDto>>() {
				@Override
				public void onSuccess(ArrayList<PlayerDto> result) {
					storePlayer.replaceAll(result);
					doUpdatePlayerList = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdatePlayerList = false;
					unmask();
					AppExceptionHandler.handleException(caught);
				}
			});
		}
	}

	protected void loadGoalChart(PlayerDto selectedPlayer) {
		if (doUpdateGoalChart) {
			mask("Tor-Statistik wird geladen...");
			KickerServices.CHART_SERVICE.getGoalChart(selectedPlayer, new AsyncCallback<ArrayList<ChartDto>>() {
				@Override
				public void onSuccess(ArrayList<ChartDto> result) {
					bar.setShownInLegend(true);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePanel(UpdatePanelEvent event) {
		doUpdatePlayerList = true;
		doUpdateGoalChart = true;
	}

}
