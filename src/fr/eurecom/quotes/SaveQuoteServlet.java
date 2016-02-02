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

public class SaveQuoteServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println(
				"Save quote servlet. GET method doesn't do anything.");
	}

	/**
	 * Delete a quote.
	 * W: 28-01-2016
	 */
	public void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
		if(req.getParameter("id") != null) {
			String id = req.getParameter("id");
			Key k = KeyFactory.createKey("Quote", id);
			datastore.delete(k);
//			datastore.delete(KeyFactory.stringToKey(id));
		} else {
			resp.getWriter().println(
					"Please give the id of the quote to be deleted");
		}
	}
	
	/**
	 * Save a quote in the DB. The quote can be new or already existent.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// Retrieve informations from the request
		// Parameters: language author titleBook typeBook textQuote year
		
		
		// Take a reference of the datastore
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		// Generate or retrieve the key associated with an existent quote
		// Create or modify the entity associated with the quote
		Entity quote = null;
		
		// W: 28-1-2016
		if(req.getParameter("id") != null) {
			String id = req.getParameter("id");
			try {
//				Key k = KeyFactory.createKey("Quote", id);
//				quote = datastore.get(k);
				quote = datastore.get(KeyFactory.stringToKey(id));
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Your key doesn't exist in the datastore");
			}
		} else {
			quote = new Entity("Quote");
		}
		
		
		if(req.getParameter("language") != null) {
			String quoteLanguage = req.getParameter("language");
			quote.setProperty("language", quoteLanguage);			
		}
		if(req.getParameter("author") != null) {
			String quoteAuthor = req.getParameter("author");
			quote.setProperty("author", quoteAuthor);
		}
		if(req.getParameter("titleBook") != null) {
			String quoteTitleBook = req.getParameter("titleBook");			
			quote.setProperty("titleBook", quoteTitleBook);
		}
		if(req.getParameter("typeBook") != null) {
			String quoteTypeBook = req.getParameter("typeBook");
			quote.setProperty("typeBook", quoteTypeBook);			
		}
		if(req.getParameter("textQuote") != null) {
			String quoteTextQuote = req.getParameter("textQuote");
			quote.setProperty("textQuote", quoteTextQuote);			
		}
		if(req.getParameter("year") != null) {
			String quoteYear = req.getParameter("year");
			quote.setProperty("year", quoteYear);			
		}
		// W: 23-1-2016
		if(req.getParameter("origin") != null) {
			String quoteOrigin = req.getParameter("origin");
			quote.setProperty("origin", quoteOrigin);			
		}
		// W: 28-1-2016
		if(req.getParameter("likes") != null) {
			String quoteLikes = req.getParameter("likes");
			quote.setProperty("likes", quoteLikes);			
		}
		
		// Save in the Datastore
		datastore.put(quote);
		resp.getWriter().println(
				"Quote saved with key "
						+ KeyFactory.keyToString(quote.getKey()) + " !");
	}

}
