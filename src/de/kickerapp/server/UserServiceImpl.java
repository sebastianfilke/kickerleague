package de.kickerapp.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.kickerapp.services.UserService;

/**
 * The server side implementation of the RPC service.
 */
public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {

	private static final long serialVersionUID = -2286450096364102814L;

	public String getUser(String input) throws IllegalArgumentException {

		return "";
	}

}
