package de.kickerapp.client.ui.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface KickerIcons extends ClientBundle {

	public final KickerIcons GET = GWT.create(KickerIcons.class);

	@Source("soccer_ball.png")
	ImageResource socerBall();

}
