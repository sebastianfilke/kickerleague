package de.kickerapp.client.ui;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldSet;

import de.kickerapp.client.ui.model.ResultData;
import de.kickerapp.client.ui.model.data.Result;
import de.kickerapp.client.ui.model.properties.ResultProperty;
import de.kickerapp.client.widgets.AppButton;
import de.kickerapp.client.widgets.AppComboBox;
import de.kickerapp.client.widgets.AppContentPanel;
import de.kickerapp.client.widgets.AppTextField;

public class MainPanel extends AppContentPanel {

	public MainPanel() {
		initLayout();
	}

	public void initLayout() {
		setHeadingHtml("<i>Spiel eintragen</i>");

		VerticalLayoutContainer vlcMain = new VerticalLayoutContainer();

		HorizontalLayoutContainer hlcTeams = new HorizontalLayoutContainer();
		hlcTeams.add(new HTML("Team 1"), new HorizontalLayoutData(-1, -1, new Margins(4)));
		hlcTeams.add(new HTML("Team 2"), new HorizontalLayoutData(-1, -1, new Margins(4)));
		// vlcMain.add(new HTML("Team 1"), new VerticalLayoutData(-1, -1, new
		// Margins(0, 0, 20, 0)));

		ResultProperty resultProp = GWT.create(ResultProperty.class);
		ListStore<ResultData> storeResult = new ListStore<ResultData>(resultProp.id());
		storeResult.addAll(createResultData());
		AppComboBox<ResultData> cbResultTeam1 = new AppComboBox<ResultData>(storeResult, resultProp.label());
		cbResultTeam1.setTriggerAction(TriggerAction.ALL);
		cbResultTeam1.setWidth(80);

		ResultProperty resultPropTeam2 = GWT.create(ResultProperty.class);
		ListStore<ResultData> storeResultTeam2 = new ListStore<ResultData>(resultPropTeam2.id());
		storeResultTeam2.addAll(createResultData());
		AppComboBox<ResultData> cbResultTeam2 = new AppComboBox<ResultData>(storeResultTeam2, resultPropTeam2.label());
		cbResultTeam2.setTriggerAction(TriggerAction.ALL);
		cbResultTeam2.setWidth(80);

		FieldSet fieldSet = new FieldSet();
		fieldSet.setWidth(300);
		fieldSet.setHeadingText("Ergebnis");

		HBoxLayoutContainer c = new HBoxLayoutContainer();
		c.setPadding(new Padding(5));
		c.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
		c.setPack(BoxLayoutPack.CENTER);
		c.add(cbResultTeam1, new BoxLayoutData(new Margins(0, 5, 0, 0)));
		c.add(new Label(":"), new BoxLayoutData(new Margins(0, 5, 0, 0)));
		c.add(cbResultTeam2, new BoxLayoutData(new Margins(0, 5, 0, 0)));
		fieldSet.add(c);
		vlcMain.add(fieldSet);

		AppTextField tfPlayer1 = new AppTextField("Spieler 1");
		tfPlayer1.setWidth(200);
		vlcMain.add(tfPlayer1);

		AppTextField tfPlayer2 = new AppTextField("Spieler 2");
		tfPlayer2.setWidth(200);
		// vlcMain.add(tfPlayer2, new VerticalLayoutData(1, -1));

		AppTextField tfPlayer3 = new AppTextField("Spieler 3");
		tfPlayer3.setWidth(200);
		// vlcMain.add(tfPlayer3, new VerticalLayoutData(1, -1));

		AppTextField tfPlayer4 = new AppTextField("Spieler 4");
		tfPlayer4.setWidth(200);
		// vlcMain.add(tfPlayer4, new VerticalLayoutData(1, -1));

		AppButton bReport = new AppButton("Ergebniss eintragen");

		add(vlcMain, new VerticalLayoutData(1, -1, new Margins(10)));
		addButton(bReport);
	}

	private ArrayList<ResultData> createResultData() {
		ArrayList<ResultData> resultList = new ArrayList<ResultData>();

		for (int i = 1; i <= 6; i++) {
			Result result = new Result();
			result.setId(i);
			result.setName(Integer.toString(i));
			resultList.add(result);
		}
		return resultList;
	}

}
