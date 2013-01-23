package solPicker.filters;

import java.util.ArrayList;

import solPicker.job.Oligo;


/**
 * This filter calculates the homopolymer score of each oligo. The user may
 * specify a maximum score, and if the oligo exceeds this, it is discarded. The
 * filter checks to see if there are any homopolymers (subsequences of 5 or more
 * consecutive identical nucleotides). Adds 11 to the filter score for are any
 * homopolymers that are 7 bases long or more; adds 2 to the score for every
 * homopolymer that is 5 or 6 bases long.
 * 
 * @version $Id: HomoPolymerFilter.java,v 1.4 2009/10/14 17:38:14 achandra Exp $
 * @author Arjun Chandrasekhar
 */

public class HomoPolymerFilter extends FailScoreFilter {
	public HomoPolymerFilter(int max) {
		super(0);
		maxHomoPolymerScore = max;
		rejectedOligos = new ArrayList<Oligo>();
	}

	/**
	 * Takes an ArrayList of oligos as a parameter and filters out all oligos
	 * with a homopolymer score greater than the specified maxmimum
	 * 
	 * @param oligos
	 *            the ArrayList of oligos to be filtered
	 * @return an ArrayList of oligos whose homopolymer scores meet the
	 *         parameters
	 */

	public ArrayList<Oligo> filter(ArrayList<Oligo> oligos) {
		for (int i = 0; i < oligos.size(); i++) {

			Oligo o = oligos.get(i);
			int homoPolymerScore = getHomoPolymerScore(o);
			o.addFilterValue(this.getFilterType(),homoPolymerScore);

			if (homoPolymerScore > maxHomoPolymerScore) {
				o
						.reject("Homopolymer score is too high.  The maximum acceptable score was "
								+ maxHomoPolymerScore
								+ " but the actual Homopolymer score was "
								+ homoPolymerScore);
				oligos.remove(i--);
				rejectedOligos.add(o);
				invalidOligos.add(o);
			}
		}
		return oligos;
	}

	/**
	 * returns the filter type
	 * 
	 * @return filter type
	 */
	public String getFilterType() {
		return "Homopolymer Score Filter";
	}

	/**
	 * returns oligos that were filtered out because of their homopolymer score
	 * 
	 * @return oligos filtered out due to homopolymer score
	 */
	public ArrayList<Oligo> getRejectedOligos() {
		return rejectedOligos;
	}


	public String getValue(Oligo o)
	{
		return "" + getHomoPolymerScore(o);
	}

	public String toXML(){
		String output = "";
		output += "<Filter Type=\""+getFilterType()+"\">\n";
		output += "<Parameter Constraint=\"max"+"\" Value=\""+maxHomoPolymerScore+"\"/>\n";
		output += "</Filter>\n";
		return output;
	}
	/**
	 * The maximum homopolymer score allowed by the filter
	 */
	private int maxHomoPolymerScore;

	/**
	 * a list of oligos rejected by the homopolymer filter
	 */
	private ArrayList<Oligo> rejectedOligos;
}
