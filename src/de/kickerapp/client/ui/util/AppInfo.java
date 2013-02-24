package de.kickerapp.client.ui.util;

import com.sencha.gxt.widget.core.client.info.DefaultInfoConfig;
import com.sencha.gxt.widget.core.client.info.Info;

public class AppInfo {

	private AppInfo() {
	}

	public static void showInfo(String title, String message) {
		Info.display(new AppInfoConfig(title, message));
	}

	private static class AppInfoConfig extends DefaultInfoConfig {

		public AppInfoConfig(String title, String message) {
			super(title, message);
			setDisplay(8000);
		}
	}

}
