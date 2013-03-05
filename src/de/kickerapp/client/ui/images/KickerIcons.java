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

	@Source("table.png")
	ImageResource table();
	
	@Source("table_sort.png")
	ImageResource table_sort();

	@Source("table_edit.png")
	ImageResource table_edit();

	@Source("table_save.png")
	ImageResource table_save();

	@Source("table_refresh.png")
	ImageResource table_refresh();

	@Source("soccer_ball.png")
	ImageResource soccerBall();

	@Source("tendency_up.png")
	ImageResource tendencyUp();

	@Source("tendency_down.png")
	ImageResource tendencyDown();

	@Source("tendency_constant.png")
	ImageResource tendencyConstant();

}
