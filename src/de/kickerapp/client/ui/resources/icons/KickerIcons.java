package de.kickerapp.client.ui.resources.icons;

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

	@Source("arrow-down10.png")
	ImageResource arrow_down10();

	@Source("arrow-right10.png")
	ImageResource arrow_right10();

	@Source("arrow-up10.png")
	ImageResource arrow_up10();

	@Source("bars.png")
	ImageResource chart_bar();

	@Source("bubbles10.png")
	ImageResource comments10();

	@Source("disk.png")
	ImageResource save();

	@Source("filter.png")
	ImageResource player_filter();

	@Source("info.png")
	ImageResource info();

	@Source("pencil2.png")
	ImageResource player_edit();

	@Source("pie.png")
	ImageResource chart_pie();

	@Source("undo.png")
	ImageResource reset();

	@Source("update.png")
	ImageResource update();

	@Source("soccer_ball.png")
	ImageResource soccer_ball();

	@Source("trophy_bronze.png")
	ImageResource trophy_bronze();

	@Source("trophy_gold.png")
	ImageResource trophy_gold();

	@Source("trophy_silver.png")
	ImageResource trophy_silver();

	@Source("user.png")
	ImageResource player_single();

	@Source("users.png")
	ImageResource player_double();

	@Source("users2.png")
	ImageResource player_team();

}
