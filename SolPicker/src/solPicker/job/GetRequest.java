package solPicker.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import server.Domain;
import solPicker.ErrorReporter;

/**
 * This class sends an HTTP GET request to a website and retrieves all text from
 * the site.
 * 
 * @author Tyler Izatt
 * @version $Id: GetRequest.java,v 1.4 2009/10/14 17:38:14 achandra Exp $
 */
public class GetRequest {

	private String result;
	private URL url;

	/**
	 * 
	 * @param urlStr
	 *            - a string representing a url
	 */
	public GetRequest(String urlStr) {
		result = "";
		try {
			url = new URL(urlStr);
			System.out.println("URL in GetRequest: "+urlStr);
		} catch (MalformedURLException e) {
			ErrorReporter.reportFatalError(this, urlStr+" is not a valid url. "+e.toString());
		}
	}

	/**
	 * Constructs a GetRequest Object with the URL instance field set equal to
	 * the parameter
	 * 
	 * @param u
	 *            the URL object containing the address of the web site that the
	 *            Get Request is being sent to
	 */
	public GetRequest(URL u) {
		url = u;
	}

	/**
	 * 
	 * @param u
	 *            - a domain object
	 */
	public GetRequest(Domain u) {
		url = u.getURL();
		result = "";
	}

	/**
	 * Sends an HTTP GET request using a url, and retrieves all text.
	 */
	public void sendGetRequest() {
		// Send a request to the server
		try {
			// open the url connection
			URLConnection conn = url.openConnection();
			File urlFile = new File(JobFactory.outDir + "\\url.txt");
			PrintWriter pw = new PrintWriter(urlFile);
			pw.write(url.toString());
			pw.close();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
			result = sb.toString();
			
		} catch (Exception e) {
			ErrorReporter.reportFatalError(this, "Could not retrieve results from webpage - "+url.toString()+". "+e.toString());
		}

	}

	/**
	 * Returns the result in string format from sendGetRequest(String urlStr)
	 * 
	 * @return - A string containing all the text retrieved from the urlStr
	 *         webpage.
	 */
	public String getResult() {
		return result;
	}
}
