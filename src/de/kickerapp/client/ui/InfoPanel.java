package de.kickerapp.client.ui;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

import de.kickerapp.client.ui.base.BaseContainer;

public class InfoPanel extends BaseContainer {

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

		final FlexTable table = new FlexTable();
		table.getElement().getStyle().setProperty("margin", "6px");
		table.getFlexCellFormatter().setWidth(0, 0, "280");
		table.getFlexCellFormatter().setWidth(0, 1, "40");
		table.getFlexCellFormatter().setWidth(0, 2, "280");
		table.getFlexCellFormatter().setWidth(0, 3, "40");

		table.setCellSpacing(6);
		table.setCellPadding(4);

		table.setHTML(0, 0, "<span style='font-weight:bold'>Höchste Gewinnserie:</span>");
		table.setWidget(0, 1, new Label("2"));
		table.setHTML(0, 2, "<span style='font-weight:bold'>Höchste Niederlagenserie:</span>");
		table.setWidget(0, 3, new Label("2"));

		table.setHTML(1, 0, "<span style='font-weight:bold'>Höchster Punktegewinn für Sieg:</span>");
		table.setWidget(1, 1, new Label("30"));
		table.setHTML(1, 2, "<span style='font-weight:bold'>Höchster Punkteverlust für Niederlage:</span>");
		table.setWidget(1, 3, new Label("-22"));

		table.setHTML(2, 0, "<span style='font-weight:bold'>Höchste Punktzahl:</span>");
		table.setWidget(2, 1, new Label("1234"));
		table.setHTML(2, 2, "<span style='font-weight:bold'>Niedrigste Punktzahl:</span>");
		table.setWidget(2, 3, new Label("888"));

		table.setHTML(3, 0, "<span style='font-weight:bold'>Siege (Prozentual):</span>");
		table.setWidget(3, 1, new Label("88%"));
		table.setHTML(3, 2, "<span style='font-weight:bold'>Punktedurchschnitt:</span>");
		table.setWidget(3, 3, new Label("1120"));

		add(table);
	}

}
