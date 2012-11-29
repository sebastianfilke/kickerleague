package de.kickerapp.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("userService")
public interface UserService extends RemoteService {
	String getUser(String name) throws IllegalArgumentException;
}
