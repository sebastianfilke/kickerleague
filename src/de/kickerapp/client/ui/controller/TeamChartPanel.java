package de.kickerapp.client.ui.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
import de.kickerapp.client.properties.KickerProperties;
import de.kickerapp.client.properties.TeamProperty;
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
import de.kickerapp.shared.dto.TeamDto;

/**
 * Controller-Klasse für die Ansicht der Teamstatistiken.
 * 
 * @author Sebastian Filke
 */
public class TeamChartPanel extends BaseContainer implements UpdatePanelEventHandler {

	private SimpleComboBox<Integer> cbYear;

	private AppComboBox<TeamDto> cbTeam;

	private ListStore<TeamDto> storeTeam;

	private CardLayoutContainer clcTeamChart;

	private InfoPanel infoPanel;

	private GameChartPanel gameChartPanel;

	private GoalChartPanel goalChartPanel;

	private PointChartPanel pointChartPanel;

	private OpponentChartPanel opponentChartPanel;

	private ChartContainer chartContainer;

	private ToggleGroup tgChart;

	private HashMap<Integer, ArrayList<TeamDto>> teamAggregation;

	private ToggleButton tbtnGoalChart, tbtnGameChart, tbtnOpponentChart, tbtnPointChart;

	private boolean doUpdateTeamList, doUpdateTeamInfo, doUpdateTeamChart;

	private AppButton btnUpdate;

	public TeamChartPanel() {
		super();
		initLayout();
		initHandlers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initLayout() {
		clcTeamChart = new CardLayoutContainer();

		infoPanel = new InfoPanel();
		goalChartPanel = new GoalChartPanel();
		gameChartPanel = new GameChartPanel();
		opponentChartPanel = new OpponentChartPanel();
		pointChartPanel = new PointChartPanel();

		clcTeamChart.add(gameChartPanel);
		clcTeamChart.add(goalChartPanel);
		clcTeamChart.add(pointChartPanel);
		clcTeamChart.add(opponentChartPanel);

		doUpdateTeamList = true;
		doUpdateTeamInfo = true;
		doUpdateTeamChart = true;

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

		final VerticalLayoutContainer vlcTeamInfo = new VerticalLayoutContainer();
		vlcTeamInfo.getElement().getStyle().setBackgroundColor("#FFFFFF");
		vlcTeamInfo.add(createToolBarTeamInfo(), new VerticalLayoutData(1, -1));
		vlcTeamInfo.add(infoPanel, new VerticalLayoutData(1, 1));

		final VerticalLayoutContainer vlcCharts = new VerticalLayoutContainer();
		vlcCharts.add(createToolBarCharts(), new VerticalLayoutData(1, -1));
		vlcCharts.add(clcTeamChart, new VerticalLayoutData(1, 1));

		vlcDoubleStats.add(vlcTeamInfo, new VerticalLayoutData(1, 0.18));
		vlcDoubleStats.add(vlcCharts, new VerticalLayoutData(1, 0.82));

		return vlcDoubleStats;
	}

	private ToolBar createToolBarTeamInfo() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		toolBar.add(createBtnUpdate());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(new LabelToolItem("Statistik für:"));
		toolBar.add(createYearComboBox());
		toolBar.add(new LabelToolItem("von:"));
		toolBar.add(createTeamComboBox());

		return toolBar;
	}

	private AppButton createBtnUpdate() {
		final AppButton btnListUpdate = new AppButton("Aktualisieren", IconProvider.get().update());
		btnListUpdate.setToolTip("Aktualisiert die Liste der eingetragenen Teams in der Datenbank");
		btnListUpdate.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				setDoUpdate();
				getTeamList();
			}

			private void setDoUpdate() {
				doUpdateTeamList = true;
			}
		});
		return btnListUpdate;
	}

	private AppComboBox<TeamDto> createTeamComboBox() {
		storeTeam = new ListStore<TeamDto>(KickerProperties.TEAM_PROPERTY.id());

		cbTeam = new AppComboBox<TeamDto>(storeTeam, TeamProperty.label);
		cbTeam.setTriggerAction(TriggerAction.ALL);
		cbTeam.setEmptyText("Team wählen...");
		cbTeam.setWidth(250);
		cbTeam.addSelectionHandler(new SelectionHandler<TeamDto>() {
			@Override
			public void onSelection(SelectionEvent<TeamDto> event) {
				final TeamDto selectedTeam = event.getSelectedItem();
				if (selectedTeam != null) {
					setDoUpdate();
					loadTeamChart(selectedTeam);
					loadTeamInfo(selectedTeam);
				}
			}

			private void setDoUpdate() {
				doUpdateTeamInfo = true;
				doUpdateTeamChart = true;
			}
		});
		return cbTeam;
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
				cbTeam.reset();
				storeTeam.clear();

				final ArrayList<TeamDto> teamsDto = teamAggregation.get(event.getSelectedItem());
				if (teamsDto != null) {
					storeTeam.replaceAll(teamsDto);
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
				final TeamDto selectedTeam = cbTeam.getValue();
				if (selectedTeam != null) {
					buttonPressed(event.getValue(), selectedTeam);
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
		tbtnGameChart.setToolTip("Zeigt die Spielstatistik für das aktuell gewählte Team");
		tbtnGameChart.setIcon(IconProvider.get().chart_bar());
		tbtnGameChart.setId("doubleWinsChart");
		tbtnGameChart.setAllowDepress(false);
		tbtnGameChart.setEnabled(false);

		return tbtnGameChart;
	}

	private ToggleButton createTbtnGoalChart() {
		tbtnGoalChart = new ToggleButton("Torstatistik");
		tbtnGoalChart.setToolTip("Zeigt die Torstatistik für das aktuell gewählte Team");
		tbtnGoalChart.setIcon(IconProvider.get().chart_bar());
		tbtnGoalChart.setId("doubleGoalsChart");
		tbtnGoalChart.setAllowDepress(false);
		tbtnGoalChart.setEnabled(false);

		return tbtnGoalChart;
	}

	private ToggleButton createTbtnPointChart() {
		tbtnPointChart = new ToggleButton("Punktestatistik");
		tbtnPointChart.setToolTip("Zeigt die Punktestatistik für das aktuell gewählte Team");
		tbtnPointChart.setIcon(IconProvider.get().chart_line());
		tbtnPointChart.setId("doublePointChart");
		tbtnPointChart.setAllowDepress(false);
		tbtnPointChart.setEnabled(false);

		return tbtnPointChart;
	}

	private ToggleButton createTbtnOpponentChart() {
		tbtnOpponentChart = new ToggleButton("Gegnerstatistik");
		tbtnOpponentChart.setToolTip("Zeigt die Gegnerstatistik für das aktuell gewählte Team");
		tbtnOpponentChart.setIcon(IconProvider.get().chart_pie());
		tbtnOpponentChart.setId("doubleOpponentChart");
		tbtnOpponentChart.setAllowDepress(false);
		tbtnOpponentChart.setEnabled(false);

		return tbtnOpponentChart;
	}

	private void buttonPressed(HasValue<Boolean> value, TeamDto selectedTeam) {
		if (value == tbtnGameChart) {
			clcTeamChart.setActiveWidget(gameChartPanel);
			gameChartPanel.loadGameChart(chartContainer.getChartGameDtos());
		} else if (value == tbtnGoalChart) {
			clcTeamChart.setActiveWidget(goalChartPanel);
			goalChartPanel.loadGoalChart(chartContainer.getChartGoalDtos());
		} else if (value == tbtnPointChart) {
			clcTeamChart.setActiveWidget(pointChartPanel);
			pointChartPanel.loadPointChart(chartContainer.getChartPointDtos());
		} else if (value == tbtnOpponentChart) {
			clcTeamChart.setActiveWidget(opponentChartPanel);
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
				final TeamDto selectedTeam = cbTeam.getValue();
				if (selectedTeam != null) {
					setDoUpdate();
					loadTeamInfo(selectedTeam);
					loadTeamChart(selectedTeam);
				}
			}

			private void setDoUpdate() {
				doUpdateTeamInfo = true;
				doUpdateTeamChart = true;
			}
		});
		return btnUpdate;
	}

	public void updateTeam() {
		final TeamDto selectedTeam = cbTeam.getValue();
		if (selectedTeam != null && doUpdateTeamInfo && doUpdateTeamChart) {
			loadTeamInfo(selectedTeam);
			loadTeamChart(selectedTeam);
		}
	}

	public void getTeamList() {
		if (doUpdateTeamList) {
			mask("Aktualisiere...");
			KickerServices.TEAM_SERVICE.getTeamMatchYearAggregation(new AsyncCallback<HashMap<Integer, ArrayList<TeamDto>>>() {
				@Override
				public void onSuccess(HashMap<Integer, ArrayList<TeamDto>> result) {
					teamAggregation = result;

					cbYear.getStore().replaceAll(new ArrayList<>(result.keySet()));
					if (!result.isEmpty()) {
						cbYear.setValue(getCurrentYear());
					}

					final ArrayList<TeamDto> teamsDto = result.get(getCurrentYear());
					if (cbYear.getValue() != null && teamsDto != null) {
						storeTeam.replaceAll(teamsDto);
					}

					doUpdateTeamList = false;
					unmask();
				}

				@Override
				public void onFailure(Throwable caught) {
					doUpdateTeamList = false;
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
		return new DateWrapper(new Date()).getFullYear();
	}

	private void loadTeamInfo(TeamDto selectedTeam) {
		if (doUpdateTeamInfo) {
			mask("Statistik wird geladen...");
			KickerServices.CHART_SERVICE.getTeamInfo(selectedTeam, cbYear.getValue(), new AsyncCallback<InfoDto>() {
				@Override
				public void onSuccess(InfoDto result) {
					infoPanel.setInfos(result);
					doUpdateTeamInfo = false;
					unmaskPanel();
				}

				@Override
				public void onFailure(Throwable caught) {
					unmask();
					doUpdateTeamInfo = false;
					AppExceptionHandler.getInstance().handleException(caught, false);
				}
			});
		}
	}

	private void loadTeamChart(TeamDto selectedTeam) {
		if (doUpdateTeamChart) {
			mask("Statistik wird geladen...");
			KickerServices.CHART_SERVICE.getTeamChart(selectedTeam, cbYear.getValue(), new AsyncCallback<ChartContainer>() {
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
							clcTeamChart.setActiveWidget(opponentChartPanel);
							opponentChartPanel.loadOpponentChart(result.getChartOpponentDtos());
						}
					} else if (tgChart.getValue() == tbtnOpponentChart) {
						opponentChartPanel.loadOpponentChart(result.getChartOpponentDtos());
					}
					setButtonsEnabled();
					doUpdateTeamChart = false;
					unmaskPanel();
				}

				@Override
				public void onFailure(Throwable caught) {
					unmask();
					doUpdateTeamChart = false;
					AppExceptionHandler.getInstance().handleException(caught, false);
				}
			});
		}
	}

	private void unmaskPanel() {
		if (!doUpdateTeamChart && !doUpdateTeamInfo) {
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
		doUpdateTeamList = true;
		doUpdateTeamInfo = true;
		doUpdateTeamChart = true;
	}
}
