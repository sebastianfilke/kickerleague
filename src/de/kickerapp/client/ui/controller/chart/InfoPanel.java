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

	/** Die längste Gewinn-/Niederlagenserie des Team bzw. Spielers. */
	private HTML winSeries, defeatSeries;
	/** Der höchste Punktegewinn/-verlust des Team bzw. Spielers. */
	private HTML maxWinPoints, maxLostPoints;
	/** Die höchste/niedrigste Punktezahl des Team bzw. Spielers. */
	private HTML maxPoints, minPoints;
	/** Die prozentualen Siege/Punktedurchschnitt/durchschnittliche Tabellenplatz des Team bzw. Spielers. */
	private HTML percentageWins, averagePoints, averageTablePlace;

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

		initHtml();
		add(createClcInfoContainer());
	}

	private void initHtml() {
		winSeries = new HTML(createInfoHtml("0"));
		defeatSeries = new HTML(createInfoHtml("0"));

		maxWinPoints = new HTML(createInfoHtml("0"));
		maxLostPoints = new HTML(createInfoHtml("0"));

		maxPoints = new HTML(createInfoHtml("0"));
		minPoints = new HTML(createInfoHtml("0"));

		percentageWins = new HTML(createInfoHtml("0.0"));
		averagePoints = new HTML(createInfoHtml("0.0"));
		averageTablePlace = new HTML(createInfoHtml("0.0"));
	}

	private CenterLayoutContainer createClcInfoContainer() {
		final CenterLayoutContainer clcInfo = new CenterLayoutContainer();

		final HBoxLayoutContainer hblc = new HBoxLayoutContainer();
		hblc.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);

		hblc.add(createFlexTable("LÄNGSTE GEWINNSERIE", winSeries), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblc.add(createFlexTable("LÄNGSTE NIEDERLAGENSERIE", defeatSeries), new BoxLayoutData(new Margins(0, 20, 0, 0)));
		hblc.add(createFlexTable("HÖCHSTER PUNKTEGEWINN", maxWinPoints), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblc.add(createFlexTable("HÖCHSTER PUNKTEVERLUST", maxLostPoints), new BoxLayoutData(new Margins(0, 20, 0, 0)));
		hblc.add(createFlexTable("HÖCHSTE PUNKTZAHL", maxPoints), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblc.add(createFlexTable("NIEDRIGSTE PUNKTZAHL", minPoints), new BoxLayoutData(new Margins(0, 20, 0, 0)));
		hblc.add(createFlexTable("SIEGE <br>(PROZENTUAL)", percentageWins), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblc.add(createFlexTable("DURCHSCHNITTL. PUNKTE", averagePoints), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		hblc.add(createFlexTable("DURCHSCHNITTL. TABELLENPLATZ", averageTablePlace));

		clcInfo.add(hblc);

		return clcInfo;
	}

	private FlexTable createFlexTable(String text, HTML html) {
		final FlexTable ftInfo = new FlexTable();
		ftInfo.getElement().setId("stats");
		ftInfo.setCellSpacing(0);
		ftInfo.setCellPadding(0);

		ftInfo.setHTML(0, 0, createHtml(text));
		ftInfo.setWidget(1, 0, html);

		final FlexCellFormatter formatter = ftInfo.getFlexCellFormatter();
		formatter.setWidth(0, 0, "120px");
		formatter.setHeight(0, 0, "40px");
		formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);

		formatter.setWidth(1, 0, "120px");
		formatter.setHeight(1, 0, "60px");
		formatter.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_MIDDLE);

		return ftInfo;
	}

	private String createHtml(String text) {
		return "<span style='font-size:11px; font-family:Arial; font-weight:bold;'>" + text + "</span>";
	}

	private String createInfoHtml(String text) {
		return "<span style='font-size:34px; font-family:Arial; font-weight:bold;'>" + text + "</span>";
	}

	public void setInfos(InfoDto infoDto) {
		winSeries.setHTML(createInfoHtml(Integer.toString(infoDto.getWinSeries())));
		defeatSeries.setHTML(createInfoHtml(Integer.toString(infoDto.getDefeatSeries())));
		maxWinPoints.setHTML(createInfoHtml("+" + Integer.toString(infoDto.getMaxWinPoints())));
		maxLostPoints.setHTML(createInfoHtml(Integer.toString(infoDto.getMaxLostPoints())));
		maxPoints.setHTML(createInfoHtml(Integer.toString(infoDto.getMaxPoints())));
		minPoints.setHTML(createInfoHtml(Integer.toString(infoDto.getMinPoints())));
		percentageWins.setHTML(createInfoHtml(infoDto.getPercentageWins() + "%"));
		averagePoints.setHTML(createInfoHtml(infoDto.getAveragePoints()));
		averageTablePlace.setHTML(createInfoHtml(infoDto.getAverageTablePlace()));
	}

}
