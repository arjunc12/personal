package solPicker.filters;

import java.util.ArrayList;

import solPicker.job.Oligo;

/**
 * The base class for all Filters. Forces subclasses to implement a method that
 * filters an ArrayLit of Oligos in a certain way, a method that returns the
 * oligos that have been rejected by the specific subclass, and a method that
 * returns the specific type of filter that the subclass is
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: Filter.java,v 1.7 2009/10/28 21:14:05 tizatt Exp $
 */
public abstract class Filter {
	/**
	 * Default filter constructor; initializes the ArrayList that contains all
	 * the oligos that have been rejected by any filter
	 */
	public Filter() {
		invalidOligos = new ArrayList<Oligo>();
	}

	/**
	 * Clears the list of all the invalid oligos. used whenever a new Job is
	 * started.
	 */
	public static void resetInvalids() {
		if (invalidOligos != null)
			invalidOligos.clear();
		else
			invalidOligos = new ArrayList<Oligo>();
	}

	/**
	 * Takes an ArrayList of Oligos as a parameter and filters out those that do
	 * not meet certain criteria
	 * 
	 * @param oligos
	 *            the list of oligos to be filtered
	 * @return an ArrayList of Oligos filtered based on a certain characteristic
	 */
	public abstract ArrayList<Oligo> filter(ArrayList<Oligo> oligos);

	/**
	 * retrieves the oligos that have been rejeced by an individual filter
	 * 
	 * @return the list of oligos that were rejected by an individual filter
	 */
	public abstract ArrayList<Oligo> getRejectedOligos();

	/**
	 * Returns the specific type of filter
	 * 
	 * @return the specific filter type
	 */
	public abstract String getFilterType();

	public abstract boolean checkValues();
	
	public abstract String getValue(Oligo o);
	

	/*
	 * public ArrayList<Oligo> getValidOligos() { return validOligos; }
	 */

	/**
	 * Retrieves the list of all the Oligos that have been rejected by any
	 * filter
	 * 
	 * @return the list of all the rejected oligos
	 */
	public static ArrayList<Oligo> getInvalidOligos() {
		return invalidOligos;
	}

	/**
	 * Creates a String representation of the Filter that consists of the filter
	 * type
	 * 
	 * @return the filter type
	 */
	public String toString() {
		return getFilterType();
	}
	
	
	/**
	 * Creates an xml representation of a filter object.
	 */
	

	public abstract String toXML();
	

	/**
	 * A list containing all valid oligos or oligos that weren't filtered out.
	 *
	protected ArrayList<Oligo> validOligos;
	 */
	
	
	/**
	 * A list containing all invalid oligos that were filtered out.
	 */
	protected static ArrayList<Oligo> invalidOligos;
}
