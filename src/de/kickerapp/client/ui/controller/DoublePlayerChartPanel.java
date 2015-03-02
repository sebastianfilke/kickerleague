package de.kickerapp.client.ui.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import de.kickerapp.client.event.AppEventBus;
import de.kickerapp.client.event.UpdatePanelEvent;
import de.kickerapp.client.event.UpdatePanelEventHandler;
import de.kickerapp.client.exception.AppExceptionHandler;
import de.kickerapp.client.properties.PlayerProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.client.ui.controller.chart.GameChartPanel;
import de.kickerapp.client.ui.controller.chart.GoalChartPanel;
import de.kickerapp.client.ui.controller.chart.InfoPanel;
import de.kickerapp.client.ui.controller.chart.OpponentChartPanel;
import de.kickerapp.client.ui.controller.chart.PointChartPanel;
import de.kickerapp.client.ui.resources.IconProvider;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppComboBox;
import de.kickerapp.shared.container.ChartContainer;
import de.kickerapp.shared.dto.InfoDto;
import de.kickerapp.shared.dto.PlayerDto;

/**
 * Controller-Klasse für die Ansicht der Doppelstatistiken.
 * 
 * @author Sebastian Filke
 */
public class DoublePlayerChartPanel extends BaseContainer implements UpdatePanelEventHandler {

	private SimpleComboBox<Integer> cbYear;

	private AppComboBox<PlayerDto> cbPlayer;

	private ListStore<PlayerDto> storePlayer;

	private CardLayoutContainer clcDoubleChart;

	private InfoPanel infoPanel;

	private GameChartPanel gameChartPanel;

	private GoalChartPanel goalChartPanel;

	private PointChartPanel pointChartPanel;

	private OpponentChartPanel opponentChartPanel;

	private ChartContainer chartContainer;

	private ToggleGroup tgChart;

	private HashMap<Integer, ArrayList<PlayerDto>> playerAggregation;

	private ToggleButton tbtnGoalChart, tbtnGameChart, tbtnOpponentChart, tbtnPointChart;

	private boolean doUpdatePlayerList, doUpdatePlayerInfo, doUpdateDoublePlayerChart;

	private AppButton btnUpdate;

	public DoublePlayerChartPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initLayout() {
		clcDoubleChart = new CardLayoutContainer();

		infoPanel = new InfoPanel();
		goalChartPanel = new GoalChartPanel();
		gameChartPanel = new GameChartPanel();
		opponentChartPanel = new OpponentChartPanel();
		pointChartPanel = new PointChartPanel();

		clcDoubleChart.add(gameChartPanel);
		clcDoubleChart.add(goalChartPanel);
		clcDoubleChart.add(pointChartPanel);
		clcDoubleChart.add(opponentChartPanel);

		doUpdatePlayerList = true;
		doUpdatePlayerInfo = true;
		doUpdateDoublePlayerChart = true;

		add(createVlcDoubleStats());
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

	private VerticalLayoutContainer createVlcDoubleStats() {
		final VerticalLayoutContainer vlcDoubleStats = new VerticalLayoutContainer();

		final VerticalLayoutContainer vlcPlayerInfo = new VerticalLayoutContainer();
		vlcPlayerInfo.getElement().getStyle().setBackgroundColor("#FFFFFF");
		vlcPlayerInfo.add(createToolBarPlayerInfo(), new VerticalLayoutData(1, -1));
		vlcPlayerInfo.add(infoPanel, new VerticalLayoutData(1, 1));

		final VerticalLayoutContainer vlcCharts = new VerticalLayoutContainer();
		vlcCharts.add(createToolBarCharts(), new VerticalLayoutData(1, -1));
		vlcCharts.add(clcDoubleChart, new VerticalLayoutData(1, 1));

		vlcDoubleStats.add(vlcPlayerInfo, new VerticalLayoutData(1, 0.18));
		vlcDoubleStats.add(vlcCharts, new VerticalLayoutData(1, 0.82));

		return vlcDoubleStats;
	}

	private ToolBar createToolBarPlayerInfo() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		toolBar.add(createBtnUpdate());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(new LabelToolItem("Statistik für:"));
		toolBar.add(createYearComboBox());
		toolBar.add(new LabelToolItem("von:"));
		toolBar.add(createPlayerComboBox());

		return toolBar;
	}

	private AppButton createBtnUpdate() {
		final AppButton btnListUpdate = new AppButton("Aktualisieren", IconProvider.get().update());
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
					setDoUpdate();
					loadDoublePlayerChart(selectedPlayer);
					loadDoubleInfo(selectedPlayer);
				}
			}

			private void setDoUpdate() {
				doUpdatePlayerInfo = true;
				doUpdateDoublePlayerChart = true;
			}
		});
		return cbPlayer;
	}

	private SimpleComboBox<Integer> createYearComboBox() {
		cbYear = new SimpleComboBox<Integer>(new LabelProvider<Integer>() {
			@Override
			public String getLabel(Integer item) {
				return Integer.toString(item);
			}
		});
		cbYear.setTriggerAction(TriggerAction.ALL);
		cbYear.setEmptyText("Jahr wählen...");
		cbYear.setEditable(false);
		cbYear.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				cbPlayer.reset();
				storePlayer.clear();

				final ArrayList<PlayerDto> playersDto = playerAggregation.get(event.getSelectedItem());
				if (playersDto != null) {
					storePlayer.replaceAll(playersDto);
				}
			}
		});
		return cbYear;
	}

	private ToolBar createToolBarCharts() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		final ToggleButton btnGameChart = createTbtnGameChart();
		final ToggleButton btnGoalChart = createTbtnGoalChart();
		final ToggleButton btnOpponentChart = createTbtnOpponentChart();
		final ToggleButton btnPointChart = createTbtnPointChart();

		tgChart = new ToggleGroup();
		tgChart.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>() {
			@Override
			public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
				final PlayerDto selectedPlayer = cbPlayer.getValue();
				if (selectedPlayer != null) {
					buttonPressed(event.getValue(), selectedPlayer);
				}
			}
		});
		tgChart.add(btnGameChart);
		tgChart.add(btnGoalChart);
		tgChart.add(btnPointChart);
		tgChart.add(btnOpponentChart);
		tgChart.setValue(btnGameChart);

		toolBar.add(createBtnUpdateChart());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(btnGameChart);
		toolBar.add(btnGoalChart);
		toolBar.add(btnPointChart);
		toolBar.add(btnOpponentChart);

		return toolBar;
	}

	private ToggleButton createTbtnGameChart() {
		tbtnGameChart = new ToggleButton("Spielstatistik");
		tbtnGameChart.setToolTip("Zeigt die Spielstatistik für den aktuell gewählten Spieler");
		tbtnGameChart.setIcon(IconProvider.get().chart_bar());
		tbtnGameChart.setId("doubleWinsChart");
		tbtnGameChart.setAllowDepress(false);
		tbtnGameChart.setEnabled(false);

		return tbtnGameChart;
	}

	private ToggleButton createTbtnGoalChart() {
		tbtnGoalChart = new ToggleButton("Torstatistik");
		tbtnGoalChart.setToolTip("Zeigt die Torstatistik für den aktuell gewählten Spieler");
		tbtnGoalChart.setIcon(IconProvider.get().chart_bar());
		tbtnGoalChart.setId("doubleGoalsChart");
		tbtnGoalChart.setAllowDepress(false);
		tbtnGoalChart.setEnabled(false);

		return tbtnGoalChart;
	}

	private ToggleButton createTbtnPointChart() {
		tbtnPointChart = new ToggleButton("Punktestatistik");
		tbtnPointChart.setToolTip("Zeigt die Punktestatistik für den aktuell gewählten Spieler");
		tbtnPointChart.setIcon(IconProvider.get().chart_line());
		tbtnPointChart.setId("doublePointChart");
		tbtnPointChart.setAllowDepress(false);
		tbtnPointChart.setEnabled(false);

		return tbtnPointChart;
	}

	private ToggleButton createTbtnOpponentChart() {
		tbtnOpponentChart = new ToggleButton("Gegnerstatistik");
		tbtnOpponentChart.setToolTip("Zeigt die Gegnerstatistik für den aktuell gewählten Spieler");
		tbtnOpponentChart.setIcon(IconProvider.get().chart_pie());
		tbtnOpponentChart.setId("doubleOpponentChart");
		tbtnOpponentChart.setAllowDepress(false);
		tbtnOpponentChart.setEnabled(false);

		return tbtnOpponentChart;
	}

	private void buttonPressed(HasValue<Boolean> value, PlayerDto selectedPlayer) {
		if (value == tbtnGameChart) {
			clcDoubleChart.setActiveWidget(gameChartPanel);
			gameChartPanel.loadGameChart(chartContainer.getChartGameDtos());
		} else if (value == tbtnGoalChart) {
			clcDoubleChart.setActiveWidget(goalChartPanel);
			goalChartPanel.loadGoalChart(chartContainer.getChartGoalDtos());
		} else if (value == tbtnPointChart) {
			clcDoubleChart.setActiveWidget(pointChartPanel);
			pointChartPanel.loadPointChart(chartContainer.getChartPointDtos());
		} else if (value == tbtnOpponentChart) {
			clcDoubleChart.setActiveWidget(opponentChartPanel);
			opponentChartPanel.loadOpponentChart(chartContainer.getChartOpponentDtos());
		}
	}

	private AppButton createBtnUpdateChart() {
		btnUpdate = new AppButton("Aktualisieren", IconProvider.get().update());
		btnUpdate.setToolTip("Aktualisiert die momentan gewählte Statistik");
		btnUpdate.setEnabled(false);
		btnUpdate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				final PlayerDto selectedPlayer = cbPlayer.getValue();
				if (selectedPlayer != null) {
					setDoUpdate();
					loadDoubleInfo(selectedPlayer);
					loadDoublePlayerChart(selectedPlayer);
				}
			}

			private void setDoUpdate() {
				doUpdatePlayerInfo = true;
				doUpdateDoublePlayerChart = true;
			}
		});
		return btnUpdate;
	}

	public void updateDoublePlayer() {
		final PlayerDto selectedPlayer = cbPlayer.getValue();
		if (selectedPlayer != null && doUpdatePlayerInfo && doUpdateDoublePlayerChart) {
			loadDoubleInfo(selectedPlayer);
			loadDoublePlayerChart(selectedPlayer);
		}
	}

	public void getPlayerList() {
		if (doUpdatePlayerList) {
			mask("Aktualisiere...");
			KickerServices.PLAYER_SERVICE.getDoubleMatchYearAggregation(new AsyncCallback<HashMap<Integer, ArrayList<PlayerDto>>>() {
				@Override
				public void onSuccess(HashMap<Integer, ArrayList<PlayerDto>> result) {
					playerAggregation = result;

					cbYear.getStore().replaceAll(new ArrayList<>(result.keySet()));
					if (!result.isEmpty()) {
						cbYear.setValue(getCurrentYear());
					}

					final ArrayList<PlayerDto> playersDto = result.get(getCurrentYear());
					if (cbYear.getValue() != null && playersDto != null) {
						storePlayer.replaceAll(playersDto);
					}

					doUpdatePlayerList = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdatePlayerList = false;
					unmask();
					AppExceptionHandler.getInstance().handleException(caught, false);
				}
			});
		}
	}

	/**
	 * Liefert das aktuelle Jahr.
	 * 
	 * @return Das aktuelle Jahr.
	 */
	private static int getCurrentYear() {
		final DateWrapper matchDate = new DateWrapper(new Date());

		return matchDate.getFullYear();
	}

	private void loadDoubleInfo(PlayerDto selectedPlayer) {
		if (doUpdatePlayerInfo) {
			mask("Statistik wird geladen...");
			KickerServices.CHART_SERVICE.getDoublePlayerInfo(selectedPlayer, cbYear.getValue(), new AsyncCallback<InfoDto>() {
				@Override
				public void onSuccess(InfoDto result) {
					infoPanel.setInfos(result);
					doUpdatePlayerInfo = false;
					unmaskPanel();
				}

				@Override
				public void onFailure(Throwable caught) {
					unmask();
					doUpdatePlayerInfo = false;
					AppExceptionHandler.getInstance().handleException(caught, false);
				}
			});
		}
	}

	private void loadDoublePlayerChart(PlayerDto selectedPlayer) {
		if (doUpdateDoublePlayerChart) {
			mask("Statistik wird geladen...");
			KickerServices.CHART_SERVICE.getDoublePlayerChart(selectedPlayer, cbYear.getValue(), new AsyncCallback<ChartContainer>() {
				@Override
				public void onSuccess(ChartContainer result) {
					chartContainer = result;
					if (tgChart.getValue() == tbtnGameChart) {
						gameChartPanel.loadGameChart(result.getChartGameDtos());
					} else if (tgChart.getValue() == tbtnGoalChart) {
						goalChartPanel.loadGoalChart(result.getChartGoalDtos());
					} else if (tgChart.getValue() == tbtnPointChart) {
						if (result.getChartPointDtos().size() > 1) {
							pointChartPanel.loadPointChart(result.getChartPointDtos());
						} else {
							tgChart.setValue(tbtnOpponentChart);
							clcDoubleChart.setActiveWidget(opponentChartPanel);
							opponentChartPanel.loadOpponentChart(result.getChartOpponentDtos());
						}
					} else if (tgChart.getValue() == tbtnOpponentChart) {
						opponentChartPanel.loadOpponentChart(result.getChartOpponentDtos());
					}
					setButtonsEnabled();
					doUpdateDoublePlayerChart = false;
					unmaskPanel();
				}

				@Override
				public void onFailure(Throwable caught) {
					unmask();
					doUpdateDoublePlayerChart = false;
					AppExceptionHandler.getInstance().handleException(caught, false);
				}
			});
		}
	}

	private void unmaskPanel() {
		if (!doUpdateDoublePlayerChart && !doUpdatePlayerInfo) {
			unmask();
		}
	}

	private void setButtonsEnabled() {
		btnUpdate.setEnabled(true);
		tbtnGoalChart.setEnabled(true);
		tbtnGameChart.setEnabled(true);
		if (chartContainer.getChartPointDtos().size() > 1) {
			tbtnPointChart.setEnabled(true);
		} else {
			tbtnPointChart.setEnabled(false);
		}
		tbtnOpponentChart.setEnabled(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePanel(UpdatePanelEvent event) {
		doUpdatePlayerList = true;
		doUpdatePlayerInfo = true;
		doUpdateDoublePlayerChart = true;
	}
}
