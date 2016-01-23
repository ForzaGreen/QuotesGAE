package fr.eurecom.quotes;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class LogIn extends HttpServlet  {
	/**
	 * Check if login is successful
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// Retrieve informations from the request
		// Parameters: username password
		
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		// Take a reference of the datastore
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		
		Entity user = null;
		
		try {
//			user = datastore.get(KeyFactory.stringToKey(username));
			Key k = KeyFactory.createKey("User", username);
			user = datastore.get(k);
			
			if( password.equals(user.getProperty("password").toString()) ) {
				resp.getWriter().println("login success");				
			} else {
				resp.getWriter().println("login failed (bad password)");
			}
		} catch(EntityNotFoundException e) {
			resp.getWriter().println("login failed (no such username)");
		}
		
	}
}
