package fr.eurecom.quotes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.labs.repackaged.org.json.*;

public class QuotesListServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Take the list of quotes ordered by author
		Query query = new Query("Quote"); //.addSort("author", Query.SortDirection.ASCENDING);
		List<Entity> quotes = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());;

		// Let's output the basic HTML headers
		PrintWriter out = resp.getWriter();
		
		/** Different response type? */
		String responseType = req.getParameter("respType");
		if (responseType != null) {
			if (responseType.equals("json")) {
				// Set header to JSON output
				resp.setContentType("application/json");
								
				String origin = req.getParameter("origin"); //W: 23-1-2016
				String language = req.getParameter("lang");
				String type = req.getParameter("type");	
				String author = req.getParameter("author"); //W: 23-1-2016
				String book = req.getParameter("book"); //W: 23-1-2016
				
				Filter originFilter = new FilterPredicate("origin", FilterOperator.EQUAL, origin); //W: 23-1-2016
				Filter languageFilter = new FilterPredicate("language", FilterOperator.EQUAL, language);
				Filter typeFilter = new FilterPredicate("typeBook", FilterOperator.EQUAL, type);
				Filter authorFilter = new FilterPredicate("author", FilterOperator.EQUAL, author); //W: 23-1-2016
				Filter bookFilter = new FilterPredicate("titleBook", FilterOperator.EQUAL, book); //W: 23-1-2016
				
//				Filter languageAndTypeFilter = CompositeFilterOperator.and(languageFilter, typeFilter);
//				
//				if ((language != null) && (type != null)) {
//					query.setFilter(languageAndTypeFilter);
//				} else if ((language != null) && (type == null)) {
//					query.setFilter(languageFilter);
//				} else if ((language == null) && (type != null)) {
//					query.setFilter(typeFilter);
//				} else if ((language == null) && (type == null)) {
//					//nothing
//				}
				
				
				// ****** Handling 5 parameters: origin, language, type, author, book ****** 
				// W: 23-1-2016
				// 0 <=> null; 1 <=> not null
				// format: origin language type author book
				if (origin == null) { // 0 * * * *
					if (language == null) { 
						if (type == null) {
							if (author == null) {
								if (book == null) { // 0 0 0 0 0
									//nothing
								} else { // 0 0 0 0 1
									query.setFilter(bookFilter);
								}
							} else {
								if (book == null) { // 0 0 0 1 0
									query.setFilter(authorFilter);
								} else { // 0 0 0 1 1
									Filter f = CompositeFilterOperator.and(authorFilter, bookFilter);
									query.setFilter(f);
								}
							}
						} else {
							if (author == null) {
								if (book == null) { // 0 0 1 0 0
									query.setFilter(typeFilter);
								} else { // 0 0 1 0 1
									Filter f = CompositeFilterOperator.and(typeFilter, bookFilter);
									query.setFilter(f);
								}
							} else {
								if (book == null) { // 0 0 1 1 0
									Filter f = CompositeFilterOperator.and(typeFilter, authorFilter);
									query.setFilter(f);
								} else { // 0 0 1 1 1
									Filter f = CompositeFilterOperator.and(typeFilter, authorFilter, bookFilter);
									query.setFilter(f);
								}
							}
						}
					} else { 
						if (type == null) {
							if (author == null) {
								if (book == null) { // 0 1 0 0 0
									query.setFilter(languageFilter);
								} else { // 0 1 0 0 1
									Filter f = CompositeFilterOperator.and(languageFilter, bookFilter);
									query.setFilter(f);
								}
							} else {
								if (book == null) { // 0 1 0 1 0
									Filter f = CompositeFilterOperator.and(languageFilter, authorFilter);
									query.setFilter(f);
								} else { // 0 1 0 1 1
									Filter f = CompositeFilterOperator.and(languageFilter, authorFilter, bookFilter);
									query.setFilter(f);
								}
							}
						} else {
							if (author == null) {
								if (book == null) { // 0 1 1 0 0
									Filter f = CompositeFilterOperator.and(languageFilter, typeFilter);
									query.setFilter(f);
								} else { // 0 1 1 0 1
									Filter f = CompositeFilterOperator.and(languageFilter, typeFilter, bookFilter);
									query.setFilter(f);
								}
							} else {
								if (book == null) { // 0 1 1 1 0
									Filter f = CompositeFilterOperator.and(languageFilter, typeFilter, authorFilter);
									query.setFilter(f);
								} else { // 0 1 1 1 1
									Filter f = CompositeFilterOperator.and(languageFilter, typeFilter, authorFilter, bookFilter);
									query.setFilter(f);
								}
							}
						}
					}
					
				//************************************************************************
				} else { // 1 * * * *
					if (language == null) { 
						if (type == null) {
							if (author == null) {
								if (book == null) { // 1 0 0 0 0
									query.setFilter(originFilter);
								} else { // 1 0 0 0 1
									Filter f = CompositeFilterOperator.and(originFilter, bookFilter);
				                    query.setFilter(f);
								}
							} else {
								if (book == null) { // 1 0 0 1 0
									query.setFilter(authorFilter);
								} else { // 1 0 0 1 1
									Filter f = CompositeFilterOperator.and(originFilter, authorFilter, bookFilter);
									query.setFilter(f);
								}
							}
						} else {
							if (author == null) {
								if (book == null) { // 1 0 1 0 0
									Filter f = CompositeFilterOperator.and(originFilter, typeFilter);
				                    query.setFilter(f);
								} else { // 1 0 1 0 1
									Filter f = CompositeFilterOperator.and(originFilter, typeFilter, bookFilter);
									query.setFilter(f);
								}
							} else {
								if (book == null) { // 1 0 1 1 0
									Filter f = CompositeFilterOperator.and(originFilter, typeFilter, authorFilter);
									query.setFilter(f);
								} else { // 1 0 1 1 1
									Filter f = CompositeFilterOperator.and(originFilter, typeFilter, authorFilter, bookFilter);
									query.setFilter(f);
								}
							}
						}
					} else { 
						if (type == null) {
							if (author == null) {
								if (book == null) { // 1 1 0 0 0
									Filter f = CompositeFilterOperator.and(originFilter, languageFilter);
				                    query.setFilter(f);
								} else { // 1 1 0 0 1
									Filter f = CompositeFilterOperator.and(originFilter, languageFilter, bookFilter);
									query.setFilter(f);
								}
							} else {
								if (book == null) { // 1 1 0 1 0
									Filter f = CompositeFilterOperator.and(originFilter, languageFilter, authorFilter);
									query.setFilter(f);
								} else { // 1 1 0 1 1
									Filter f = CompositeFilterOperator.and(originFilter, languageFilter, authorFilter, bookFilter);
									query.setFilter(f);
								}
							}
						} else {
							if (author == null) {
								if (book == null) { // 1 1 1 0 0
									Filter f = CompositeFilterOperator.and(originFilter, languageFilter, typeFilter);
									query.setFilter(f);
								} else { // 1 1 1 0 1
									Filter f = CompositeFilterOperator.and(originFilter, languageFilter, typeFilter, bookFilter);
									query.setFilter(f);
								}
							} else {
								if (book == null) { // 1 1 1 1 0
									Filter f = CompositeFilterOperator.and(originFilter, languageFilter, typeFilter, authorFilter);
									query.setFilter(f);
								} else { // 1 1 1 1 1
									Filter f = CompositeFilterOperator.and(originFilter, languageFilter, typeFilter, authorFilter, bookFilter);
									query.setFilter(f);
								}
							}
						}
					}
				}
				
				
				
				
				
				quotes = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
				
				String limit = req.getParameter("limit");
				if (limit != null) {
					quotes = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(new Integer(limit)));
				}
				
				out.println(getJSON(quotes, req, resp));
				return;
				
			} 
		}
	}

	
	private String getJSON(List<Entity> quotes, HttpServletRequest req, HttpServletResponse resp) {
		// Create a JSON array that will contain all the entities converted in a
		// JSON version
		JSONArray results = new JSONArray();
		for (Entity quote : quotes) {
			JSONObject quoteJSON = new JSONObject();
			// language author titleBook typeBook textQuote year
			try {
				quoteJSON.put("id", KeyFactory.keyToString(quote.getKey()));
				quoteJSON.put("language", quote.getProperty("language"));
				quoteJSON.put("author", quote.getProperty("author"));
				quoteJSON.put("titleBook", quote.getProperty("titleBook"));
				quoteJSON.put("typeBook", quote.getProperty("typeBook"));
				quoteJSON.put("textQuote", quote.getProperty("textQuote"));
				quoteJSON.put("year", quote.getProperty("year"));
				quoteJSON.put("origin", quote.getProperty("origin")); // W: 23-1-2016
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			results.put(quoteJSON);
		}
		return results.toString();
	}
	

}
