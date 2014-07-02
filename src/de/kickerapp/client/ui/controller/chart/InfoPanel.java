package de.kickerapp.client.ui.controller.chart;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;

import de.kickerapp.client.ui.base.BaseContainer;
import de.kickerapp.shared.dto.InfoDto;

/**
 * Controller-Klasse für die Ansicht der Statistiken von Team- bzw. Spieler.
 * 
 * @author Sebastian Filke
 */
public class InfoPanel extends BaseContainer {

	private HTML winSeries, defeatSeries;

	private HTML maxWinPoints, maxLostPoints;

	private HTML maxPoints, minPoints;

	private HTML averageWins, averagePoints, averageTablePlace;

	/**
	 * Erzeugt einen neuen Controller für die Ansicht der Statistiken von Team- bzw. Spieler.
	 */
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
		add(createInfoContainer());
	}

	private void initLabels() {
		winSeries = new HTML(createLabelText("0"));
		defeatSeries = new HTML(createLabelText("0"));

		maxWinPoints = new HTML(createLabelText("0"));
		maxLostPoints = new HTML(createLabelText("0"));

		maxPoints = new HTML(createLabelText("0"));
		minPoints = new HTML(createLabelText("0"));

		averageWins = new HTML(createLabelText("0"));
		averagePoints = new HTML(createLabelText("0"));
		averageTablePlace = new HTML(createLabelText("0"));
	}

	private CenterLayoutContainer createInfoContainer() {
		final CenterLayoutContainer clcInfo = new CenterLayoutContainer();

		final HBoxLayoutContainer hblc = new HBoxLayoutContainer();
		hblc.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);

		hblc.add(createFlexTable("LÄNGSTE GEWINNSERIE", winSeries), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblc.add(createFlexTable("LÄNGSTE NIEDERLAGENSERIE", defeatSeries), new BoxLayoutData(new Margins(0, 20, 0, 0)));
		hblc.add(createFlexTable("HÖCHSTER PUNKTEGEWINN", maxWinPoints), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblc.add(createFlexTable("HÖCHSTER PUNKTEVERLUST", maxLostPoints), new BoxLayoutData(new Margins(0, 20, 0, 0)));
		hblc.add(createFlexTable("HÖCHSTE PUNKTZAHL", maxPoints), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblc.add(createFlexTable("NIEDRIGSTE PUNKTZAHL", minPoints), new BoxLayoutData(new Margins(0, 20, 0, 0)));
		hblc.add(createFlexTable("SIEGE <br>(PROZENTUAL)", averageWins), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblc.add(createFlexTable("PUNKTE-DURCHSCHNITT", averagePoints), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblc.add(createFlexTable("DURCHSCHNITTL. TABELLENPLATZ", averageTablePlace));

		clcInfo.add(hblc);

		return clcInfo;
	}

	private FlexTable createFlexTable(String text, HTML html) {
		final FlexTable ftInfo = new FlexTable();
		ftInfo.getElement().setId("stats");
		ftInfo.setCellSpacing(0);
		ftInfo.setCellPadding(0);

		ftInfo.setHTML(0, 0, createText(text));
		ftInfo.setWidget(1, 0, html);

		final FlexCellFormatter formatter = ftInfo.getFlexCellFormatter();
		formatter.setWidth(0, 0, "120");
		formatter.setHeight(0, 0, "40");
		formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		formatter.setWidth(1, 0, "120");
		formatter.setHeight(1, 0, "60");
		formatter.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_MIDDLE);

		return ftInfo;
	}

	private String createText(String text) {
		return "<span style='font-size:11px; font-family:Arial; font-weight:bold;'>" + text + "</span>";
	}

	private String createLabelText(String text) {
		return "<span style='font-size:34px; font-family:Arial; font-weight:bold;'>" + text + "</span>";
	}

	public void setInfos(InfoDto infoDto) {
		winSeries.setHTML(createLabelText(Integer.toString(infoDto.getWinSeries())));
		defeatSeries.setHTML(createLabelText(Integer.toString(infoDto.getDefeatSeries())));
		maxWinPoints.setHTML(createLabelText("+" + Integer.toString(infoDto.getMaxWinPoints())));
		maxLostPoints.setHTML(createLabelText(Integer.toString(infoDto.getMaxLostPoints())));
		maxPoints.setHTML(createLabelText(Integer.toString(infoDto.getMaxPoints())));
		minPoints.setHTML(createLabelText(Integer.toString(infoDto.getMinPoints())));
		averageWins.setHTML(createLabelText(infoDto.getAverageWins() + "%"));
		averagePoints.setHTML(createLabelText(infoDto.getAveragePoints()));
		averageTablePlace.setHTML(createLabelText(infoDto.getAverageTablePlace()));
	}

}
