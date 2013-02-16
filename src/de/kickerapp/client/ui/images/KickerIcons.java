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

	@Source("soccer_ball.png")
	ImageResource soccerBall();

	@Source("tendency_up.png")
	ImageResource tendencyUp();

	@Source("tendency_down.png")
	ImageResource tendencyDown();

	@Source("tendency_constant.png")
	ImageResource tendencyConstant();

}
