package de.kickerapp.client.ui;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;

import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.shared.dto.ChartDto;

public class InfoPanel extends BaseContainer {

	private Label winSeries, lossSeries;

	private Label maxWinPoints, maxLossPoints;

	private Label maxPoints, minPoints;

	private Label averageWins, averagePoints;

	public InfoPanel() {
		super();
		initLayout();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initLayout() {
		super.initLayout();

		initLabels();

		final CenterLayoutContainer clcInfo = new CenterLayoutContainer();
		clcInfo.add(createFlexTable(), new MarginData(5));

		add(clcInfo);
	}

	private FlexTable createFlexTable() {
		final FlexTable ftInfo = new FlexTable();
		ftInfo.setCellSpacing(6);
		ftInfo.setCellPadding(4);

		ftInfo.getFlexCellFormatter().setWidth(0, 0, "280");
		ftInfo.getFlexCellFormatter().setWidth(0, 1, "60");
		ftInfo.getFlexCellFormatter().setWidth(0, 2, "280");
		ftInfo.getFlexCellFormatter().setWidth(0, 3, "60");

		ftInfo.setHTML(0, 0, createText("Längste Gewinnserie:"));
		ftInfo.setWidget(0, 1, winSeries);
		ftInfo.setHTML(0, 2, createText("Längste Niederlagenserie:"));
		ftInfo.setWidget(0, 3, lossSeries);

		ftInfo.setHTML(1, 0, createText("Höchster Punktegewinn für Sieg:"));
		ftInfo.setWidget(1, 1, maxWinPoints);
		ftInfo.setHTML(1, 2, createText("Höchster Punkteverlust für Niederlage:"));
		ftInfo.setWidget(1, 3, maxLossPoints);

		ftInfo.setHTML(2, 0, createText("Höchste Punktzahl:"));
		ftInfo.setWidget(2, 1, maxPoints);
		ftInfo.setHTML(2, 2, createText("Niedrigste Punktzahl:"));
		ftInfo.setWidget(2, 3, minPoints);

		ftInfo.setHTML(3, 0, createText("Siege (Prozentual):"));
		ftInfo.setWidget(3, 1, averageWins);
		ftInfo.setHTML(3, 2, createText("Punktedurchschnitt:"));
		ftInfo.setWidget(3, 3, averagePoints);

		return ftInfo;
	}

	private void initLabels() {
		winSeries = new Label("0");
		lossSeries = new Label("0");

		maxWinPoints = new Label("0");
		maxLossPoints = new Label("0");

		maxPoints = new Label("0");
		minPoints = new Label("0");

		averageWins = new Label("0");
		averagePoints = new Label("0");
	}

	private String createText(String text) {
		return "<span style='font-size:14px; font-family:Tahoma; font-weight:bold'>" + text + "</span>";
	}

	protected void setInfos(ChartDto chartDto) {
		winSeries.setText(Integer.toString(chartDto.getWinSeries()));
		lossSeries.setText(Integer.toString(chartDto.getLossSeries()));
		maxWinPoints.setText("+" + Integer.toString(chartDto.getMaxWinPoints()));
		maxLossPoints.setText(Integer.toString(chartDto.getMaxLossPoints()));
		maxPoints.setText(Integer.toString(chartDto.getMaxPoints()));
		minPoints.setText(Integer.toString(chartDto.getMinPoints()));
		averageWins.setText(chartDto.getAverageWins() + "%");
		averagePoints.setText(chartDto.getAveragePoints());
	}

}
