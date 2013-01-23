package solPicker.job;

import java.util.ArrayList;
import java.util.Collections;

import solPicker.job.Query;
import solPicker.job.Range;


/**
 * $Id: QueryFactory.java,v 1.1 2009/08/18 21:31:41 tizatt Exp $ This class
 * creates ChromosomeQuery objects by sorting and joining Query objects. After
 * these Query objects are used to get sequence information for each query,
 * QueryFactory separates the sequence information back into the original query
 * objects.
 * 
 * @author Tyler Izatt
 *@deprecated
 */
public class QueryFactory {

	public int min_distance;
	private ArrayList<Range> ranges;

	public QueryFactory(int min_distance) {
		this.min_distance = min_distance;
	}

	/**
	 * Sorts all query objects according to chromosome number and location on
	 * the chromosome
	 * 
	 * @return - List of sorted Query objects
	 */

	protected ArrayList<Query> sortQueries(ArrayList<Query> q) {
		Collections.sort(q);
		return q;
	}

	/**
	 * Joins queries that are located on similar parts of the chromosome into
	 * Range objects. This action is only performed when there are a minimum
	 * number of Query objects (usually large) specified in the main() class.
	 * This step minimizes the amount of query requests that are sent to a
	 * webserver - reducing program run-time and preventing over-usage of a
	 * website (so that we are not blacklisted!). The joining algorithm accepts
	 * a list of Queries that are sorted by chromosome and locations. If the
	 * chromosome identifiers for the queries are the same and the queries are
	 * located within a minimum distance of each other on this chromosome, they
	 * are combined.
	 * 
	 * @return - ArrayList of Range objects.
	 */
	public ArrayList<Range> joinQueries(ArrayList<Query> queries) {
		queries = sortQueries(queries);
		ranges = new ArrayList<Range>();
		for (int i = 0; i < queries.size(); i++) {
			Range r = new Range(queries.get(i));
			ranges.add(r);
			queries.remove(i);

			for (int j = 0; j < queries.size(); j++) {
				Query q1 = queries.get(j);

				if (q1.getChromosomeIdentifier().equalsIgnoreCase(
						r.getChromosomeNumber())) {
					int dif = compareStringAsInt(r.getEndBase(), q1.getStart());
					if (Math.abs(dif) <= min_distance) {
						r.addQuery(q1);
						queries.remove(j);
						j--;
					}
				}
			}
			i--;
		}
		return ranges;
	}

	/**
	 * Returns an array with <Range> objects
	 * 
	 * @return - an array of <Range> objects
	 */
	public ArrayList<Range> getRangeArray() {
		return ranges;
	}

	/**
	 * Separates a sequence into its individual Query object
	 * 
	 */
	// currently needs to be fixed because query does not store its sequence
	// information
	public void separateSequenceToQuery(Range range, String sequence) {
		ArrayList<Query> queries = range.getQueries();
		int rStartBase = Integer.parseInt(range.getStartBase());

		for (int i = 0; i < queries.size(); i++) {
			int qStartBase = Integer.parseInt(queries.get(i).getStart());
			int qEndBase = Integer.parseInt(queries.get(i).getEnd());
			int newStartBase = qStartBase - rStartBase;
			int newEndBase = qEndBase - rStartBase;
			Query q = queries.get(i);
			Math.abs(newStartBase);
			Math.abs(newEndBase);
			q.getClass();
			// q.(sequence.substring(newStartBase, newEndBase));
		}

	}

	/**
	 * @return - integer representing the minimum distance required to join two
	 *         Query objects.
	 */
	public int getMinDistance() {
		return min_distance;
	}

	/**
	 * Parses two Strings into Integers and returns the difference when the
	 * first integer is subtracted from the second integer
	 * 
	 * @param a
	 *            first String to be compared
	 * @param b
	 *            second String to be compared
	 * @return the difference between the Integer values of the two Strings
	 */
	private int compareStringAsInt(String a, String b) {
		int i = Integer.parseInt(a);
		int j = Integer.parseInt(b);

		return i - j;
	}
}
