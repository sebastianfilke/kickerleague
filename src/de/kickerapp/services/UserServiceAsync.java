package de.kickerapp.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface UserServiceAsync {
	void getUser(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
