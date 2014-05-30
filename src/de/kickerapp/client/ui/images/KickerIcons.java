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

	@Source("arrow-down.png")
	ImageResource arrow_down();

	@Source("arrow-right.png")
	ImageResource arrow_right();

	@Source("arrow-up.png")
	ImageResource arrow_up();

	@Source("bars.png")
	ImageResource chart_bar();

	@Source("bubbles.png")
	ImageResource comments();

	@Source("disk.png")
	ImageResource save();

	@Source("filter.png")
	ImageResource player_filter();

	@Source("pencil2.png")
	ImageResource player_edit();

	@Source("undo.png")
	ImageResource reset();

	@Source("update.png")
	ImageResource update();

	@Source("soccer_ball.png")
	ImageResource soccer_ball();

	@Source("user.png")
	ImageResource player_single();

	@Source("users.png")
	ImageResource player_double();

	@Source("users2.png")
	ImageResource player_team();

}
