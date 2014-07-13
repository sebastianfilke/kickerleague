package de.kickerapp.client.ui.controller;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
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
import de.kickerapp.client.properties.PlayerProperty;
import de.kickerapp.client.services.KickerServices;
import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.client.ui.controller.chart.GameChartPanel;
import de.kickerapp.client.ui.controller.chart.GoalChartPanel;
import de.kickerapp.client.ui.controller.chart.InfoPanel;
import de.kickerapp.client.ui.resources.IconProvider;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppComboBox;
import de.kickerapp.client.widgets.AppContentPanel;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.InfoDto;
import de.kickerapp.shared.dto.PlayerDto;

public class DoublePlayerChartPanel extends BaseContainer implements UpdatePanelEventHandler {

	private AppComboBox<PlayerDto> cbPlayer;

	private ListStore<PlayerDto> storePlayer;

	private CardLayoutContainer clcDoubleChart;

	private GoalChartPanel goalChartPanel;

	private GameChartPanel gameChartPanel;

	private InfoPanel infoPanel;

	private ToggleGroup tgChart;

	private ToggleButton tbtnGoalChart, tbtnWinChart;

	private boolean doUpdatePlayerList, doUpdateSinglePlayerChart;

	private InfoDto chartDto;

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

		clcDoubleChart.add(goalChartPanel);
		clcDoubleChart.add(gameChartPanel);

		doUpdatePlayerList = true;
		doUpdateSinglePlayerChart = true;

		add(createBlcSingleStats());
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

	private BorderLayoutContainer createBlcSingleStats() {
		final BorderLayoutContainer blcSinglePlayer = new BorderLayoutContainer();

		final BorderLayoutData northData = new BorderLayoutData(0.2);
		northData.setSplit(true);

		final AppContentPanel panel = new AppContentPanel();
		panel.setHeaderVisible(false);
		panel.setBodyBorder(false);
		panel.setBorders(false);
		panel.add(infoPanel);

		final VerticalLayoutContainer vlcPlayerInfo = new VerticalLayoutContainer();
		vlcPlayerInfo.getElement().getStyle().setBackgroundColor("#FFFFFF");
		vlcPlayerInfo.add(createToolBarPlayerInfo(), new VerticalLayoutData(1, -1));
		vlcPlayerInfo.add(panel, new VerticalLayoutData(1, 1));

		final VerticalLayoutContainer vlcCharts = new VerticalLayoutContainer();
		vlcCharts.add(createToolBarCharts(), new VerticalLayoutData(1, -1));
		vlcCharts.add(clcDoubleChart, new VerticalLayoutData(1, 1));

		blcSinglePlayer.setNorthWidget(vlcPlayerInfo, northData);
		blcSinglePlayer.setCenterWidget(vlcCharts);

		return blcSinglePlayer;
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
					setEnabledButtons();
					loadSinglePlayerChart(selectedPlayer);
				}
			}

			private void setDoUpdate() {
				doUpdateSinglePlayerChart = true;
			}
		});
		return cbPlayer;
	}

	protected void setEnabledButtons() {
		tbtnGoalChart.setEnabled(true);
		btnUpdate.setEnabled(true);
		tbtnWinChart.setEnabled(true);
	}

	private ToolBar createToolBarCharts() {
		final ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		final ToggleButton btnGoalChart = createTbtnGoalChart();
		final ToggleButton btnWinsChart = createTbtnWinChart();

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
		tgChart.add(btnGoalChart);
		tgChart.add(btnWinsChart);
		tgChart.setValue(btnGoalChart);

		toolBar.add(createBtnUpdateChart());
		toolBar.add(new SeparatorToolItem());
		toolBar.add(btnGoalChart);
		toolBar.add(btnWinsChart);

		return toolBar;
	}

	public void buttonPressed(HasValue<Boolean> value, PlayerDto selectedPlayer) {
		if (value == tbtnGoalChart) {
			clcDoubleChart.setActiveWidget(goalChartPanel);
			// goalChartPanel.loadGoalChart(chartDto);
		} else if (value == tbtnWinChart) {
			clcDoubleChart.setActiveWidget(gameChartPanel);
			// gameChartPanel.loadGameChart(chartDto);
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
					loadSinglePlayerChart(selectedPlayer);
				}
			}

			private void setDoUpdate() {
				doUpdateSinglePlayerChart = true;
			}
		});
		return btnUpdate;
	}

	private ToggleButton createTbtnGoalChart() {
		tbtnGoalChart = new ToggleButton("Torstatistik");
		tbtnGoalChart.setToolTip("Zeigt die Torstatistik für den aktuell gewählten Spieler");
		tbtnGoalChart.setIcon(IconProvider.get().chart_bar());
		tbtnGoalChart.setId("singleGoalsChart");
		tbtnGoalChart.setAllowDepress(false);
		tbtnGoalChart.setEnabled(false);

		return tbtnGoalChart;
	}

	private ToggleButton createTbtnWinChart() {
		tbtnWinChart = new ToggleButton("Spielstatistik");
		tbtnWinChart.setToolTip("Zeigt die Spielstatistik für den aktuell gewählten Spieler");
		tbtnWinChart.setIcon(IconProvider.get().chart_bar());
		tbtnWinChart.setId("singleWinsChart");
		tbtnWinChart.setAllowDepress(false);
		tbtnWinChart.setEnabled(false);

		return tbtnWinChart;
	}

	public void getPlayerList() {
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
					AppExceptionHandler.getInstance().handleException(caught);
				}
			});
		}
	}

	private void loadSinglePlayerChart(PlayerDto selectedPlayer) {
		if (doUpdateSinglePlayerChart) {
			mask("Statistik wird geladen...");
			// KickerServices.CHART_SERVICE.getSinglePlayerGoalChart(selectedPlayer, new AsyncCallback<InfoDto>() {
			// @Override
			// public void onSuccess(InfoDto result) {
			// unmask();
			// doUpdateSinglePlayerChart = false;
			// chartDto = result;
			// if (tgChart.getValue() == tbtnGoalChart) {
			// goalChartPanel.loadGoalChart(result.getChartDataDto());
			// } else if (tgChart.getValue() == tbtnWinChart) {
			// gameChartPanel.loadGameChart(result.getChartDataDto());
			// }
			// infoPanel.setInfos(result);
			// }
			//
			// @Override
			// public void onFailure(Throwable caught) {
			// unmask();
			// doUpdateSinglePlayerChart = false;
			// AppExceptionHandler.getInstance().handleException(caught);
			// }
			// });
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePanel(UpdatePanelEvent event) {
		doUpdatePlayerList = true;
		doUpdateSinglePlayerChart = true;
	}

}
