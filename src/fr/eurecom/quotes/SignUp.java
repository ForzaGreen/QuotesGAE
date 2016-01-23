package fr.eurecom.quotes;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;

public class SignUp extends HttpServlet  {

	/**
	 * Save a user in the DB. 
	 * //TODO: check if user exists
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
		// Generate user with name as username
		Entity user = null;
		
		
		try {
			user = new Entity("User", username);
			Key k = KeyFactory.createKey("User", username);
			//user = datastore.get(KeyFactory.stringToKey("raja"));
			user = datastore.get(k);
			resp.getWriter().println("username already exists");
		} catch(EntityNotFoundException e) {
			user = new Entity("User", username);
			e.printStackTrace();
			user.setProperty("username", username);	
			user.setProperty("password", password);			
			
			// Save in the Datastore
			datastore.put(user);
			resp.getWriter().println(
					"Success: User created with key "
							+ KeyFactory.keyToString(user.getKey()) + " !");
		}
		

	}

	
}
