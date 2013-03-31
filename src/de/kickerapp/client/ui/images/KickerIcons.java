package de.kickerapp.client.ui.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Die Icons für die Applikation.
 * 
 * @author Sebastian Filke
 */
public interface KickerIcons extends ClientBundle {

	/** Die Icons. */
	public final KickerIcons ICON = GWT.create(KickerIcons.class);

	// CHECKSTYLE:OFF

	@Source("chart_bar.png")
	ImageResource chartBar();

	@Source("group.png")
	ImageResource group();

	@Source("group_link.png")
	ImageResource groupLink();

	@Source("soccer_ball.png")
	ImageResource soccerBall();

	@Source("table.png")
	ImageResource table();

	@Source("table_edit.png")
	ImageResource tableEdit();

	@Source("table_refresh.png")
	ImageResource tableRefresh();

	@Source("table_save.png")
	ImageResource tableSave();

	@Source("table_sort.png")
	ImageResource tableSort();

	@Source("tendency_constant.png")
	ImageResource tendencyConstant();

	@Source("tendency_down.png")
	ImageResource tendencyDown();

	@Source("tendency_up.png")
	ImageResource tendencyUp();

	@Source("user.png")
	ImageResource user();

	@Source("user_edit.png")
	ImageResource userEdit();

}
