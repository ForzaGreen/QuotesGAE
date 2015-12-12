package fr.eurecom.quotes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.labs.repackaged.org.json.*;

public class QuotesListServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		// Take the list of quotes ordered by name
		Query query = new Query("Quote").addSort("author",
				Query.SortDirection.ASCENDING);
		List<Entity> quotes = datastore.prepare(query).asList(
				FetchOptions.Builder.withDefaults());

		// Let's output the basic HTML headers
		PrintWriter out = resp.getWriter();
		
		/** Different response type? */
		String responseType = req.getParameter("respType");
		if (responseType != null) {
			if (responseType.equals("json")) {
				// Set header to JSON output
				resp.setContentType("application/json");
				out.println(getJSON(quotes, req, resp));
				return;
			} else if (responseType.equals("xml")) {
				resp.setContentType("application/json");
				out.println(getXML(quotes, req, resp));
				return;
			}
		}
	}

	
	private String getJSON(List<Entity> quotes, HttpServletRequest req,
			HttpServletResponse resp) {
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
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			results.put(quoteJSON);
		}
		return results.toString();
	}
	
	private String getXML(List<Entity> quotes, HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		return null;
	}

}
