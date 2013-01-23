package solPicker.job;

import java.util.ArrayList;

import solPicker.filters.Filter;


/**
 * The configuration object keeps track of different configurations of filters
 * to be applied to the oligos. Each configuration has a unique identification
 * number, which is how it is later accessed by the solPicker.job, as well as filter
 * objects.
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: Configuration.java,v 1.2 2009/09/23 18:59:21 tizatt Exp $
 */
public class Configuration {
	/**
	 * Default Configuration Constructor. Initializes the ArrayList of Filters
	 * and assigns a number to the ID
	 */
	public Configuration() {
		filters = new ArrayList<Filter>();
		identity = Identities;
		id = identity + "";
		Identities++;
	}

	/**
	 * Initializes the ArrayList of Filters and sets the ID equal to the
	 * parameter
	 * 
	 * @param i
	 *            the Configuration's unique ID
	 */
	public Configuration(String i) {
		filters = new ArrayList<Filter>();
		id = i;
		identity = Identities;
		Identities++;
	}

	/**
	 * Sets the ArrayList of Filters equal to the parameter fList and sets the
	 * ID equal to the parameter i
	 * 
	 * @param fList
	 *            the ArrayList of Filters
	 * @param i
	 *            the Configuration ID
	 */
	public Configuration(ArrayList<Filter> fList, String i) {
		filters = fList;
		id = i;
		identity = Identities;
		Identities++;
	}

	/**
	 * Adds a filter to the Configuration's list of Filters
	 * 
	 * @param f
	 *            a Filter Object
	 */
	public void addFilter(Filter f) {
			filters.add(f);
	}

	/**
	 * Retrieves the Configuration's list of Filters
	 * 
	 * @return the Configuration's list of Filters
	 */
	public ArrayList<Filter> getFilters() {
		return filters;
	}

	/**
	 * retrieves the configuration's unique ID
	 * 
	 * @return the configuration's ID
	 */
	public String getID() {
		return id;
	}

	/**
	 * Resets the conguration's ID to the parameter
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setID(String id) {
		this.id = id;
	}

	/**
	 * Creates an XML tag signifying the start of a configuration and subsequent
	 * tags for each of the Filters
	 * 
	 * @return an XML Representation of the Configuration
	 */
	public String toXML() {
		String output = "";
		output += "<Configuration ConfigID=\"" + id + "\">\n";
		for (int i = 0; i < filters.size(); i++) {
			output += filters.get(i).toXML();
		}
		output += "</Configuration>\n";
		return output;
	}

	/**
	 * Retrieves the Configuration's unique ID
	 * 
	 * @return the configuration ID
	 */
	public int getIdentity() {
		return identity;
	}

	private ArrayList<Filter> filters;
	private String id;
	private int identity;
	private static int Identities = 1;
}
