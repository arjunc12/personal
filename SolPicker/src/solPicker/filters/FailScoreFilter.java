package solPicker.filters;

import java.util.ArrayList;

import solPicker.CDF_Normal;
import solPicker.ErrorReporter;
import solPicker.job.Oligo;

/**
 * This filter performs a series of operations on the oligos and assigns each a
 * score based on these computations. Oligos with too high a fail score will be
 * removed from the set. The user has the opportunity to designate a maximum
 * fail score, but the programmed threshold is 10.
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: FailScoreFilter.java,v 1.6 2009/10/28 21:14:05 tizatt Exp $
 */

public class FailScoreFilter extends Filter {
	/**
	 * Creates a Fail Score Filter with a maximum allowable fail score equal to
	 * the parameter
	 * 
	 * @param max
	 *            the maximum fail score that the oligos are allowed to have
	 */
	public FailScoreFilter(int max) {
		maxFail = max;
		rejectedOligos = new ArrayList<Oligo>();
		checkValues();
	}

	/**
	 * Takes an ArrayList of Oligos as a parameter and filters out all Oligos
	 * with a fail score that is greater than the specified maxmimum
	 * 
	 * @param oligos
	 *            the ArrayList of Oligos to be filtered
	 * @return an ArrayList of Oligos whose fail scores are all below a certain
	 *         number
	 */
	public ArrayList<Oligo> filter(ArrayList<Oligo> oligos) {
		for (int i = 0; i < oligos.size(); i++) {
			Oligo o = oligos.get(i);
			int failScore = getTMScore(o) + getHomoPolymerScore(o) + getBaseFractionScore(o);
			// int failScore = o.getFailScore();
			o.addFilterValue(this.getFilterType(), failScore);
			if (failScore > maxFail) {
				o.reject("fail score is too high.  The maximum acceptable fail score was "+ maxFail + " but the actual fail score was " + failScore);
				oligos.remove(i);
				invalidOligos.add(o);
				rejectedOligos.add(o);
				i--;
			}
		}
		return oligos;
	}

	/**
	 * Counts the occurrences of G and C nucleotides in the oligo's sequence.
	 * Uses the GC Count to calculate the TM score, adds 4 to the Fail Score if
	 * the TM Score is not between 81 and 77 inclusive.
	 * 
	 * @param o
	 *            The oligo from which to calculate the TM score
	 */
	private int getTMScore(Oligo o) {
		String sequence = o.getSequence();
		int gc = 0;
		for (int i = 0; i < sequence.length(); i++) {
			String letter = sequence.charAt(i) + "";
			if (letter.equalsIgnoreCase("c") || letter.equalsIgnoreCase("g"))
				gc++;
		}
		gc -= 16.4;
		gc *= 41;
		gc /= sequence.length();
		gc += 64.9;
		if (gc > 81 || gc < 77)
			return 4;
		return 0;
	}

	/**
	 * Checks for the presence of any homopolymers (subsequences of 5 or more
	 * consecutive identical nucleotides). Adds 11 to the fail score if there
	 * are any homopolymers that are 7 bases long or more; adds 2 to the Fail
	 * Score for every homopolymer that is either 5 or 6 bases long
	 * 
	 * @param o
	 *            the Oligo from which to calculate the Homo Polymer Score
	 */
	protected int getHomoPolymerScore(Oligo o) {
		String sequence = o.getSequence();
		int hmScore = 0;
		for (int i = 0; i < sequence.length() - 4; i++) {
			String poly = sequence.substring(i, i + 5);
			char ch = sequence.charAt(i);
			if (fiveInARow(poly)) 
			{
				hmScore += 2;
				if(i < sequence.length() - 6)
				{
					String poly2 = sequence.substring(i, i + 7);
					if (sevenConsecutive(poly2)) 
					{
						hmScore += 9;
					}
				}
				boolean done = false;
				while (i < sequence.length() - 1 && !done) 
				{
					i++;
					char ch2 = sequence.charAt(i);
					String str1 = ch + "";
					String str2 = ch2 + "";
					if (!str1.equalsIgnoreCase(str2))
						done = true;
				}
				i--;
			}
		}
		return hmScore;
	}

	/**
	 * Checks if a nucleotide sequence consists of 5 consecutive identical
	 * nucleotide bases
	 * 
	 * @param poly
	 *            5 base nucleotide to be checked for the presence of a
	 *            homopolymer of length 5
	 * @return whether or not the sequence parameter consists of 5 consecutive
	 *         identical bases
	 */
	private boolean fiveInARow(String poly) {
		return poly.equalsIgnoreCase("AAAAA") || poly.equalsIgnoreCase("CCCCC")
				|| poly.equalsIgnoreCase("GGGGG")
				|| poly.equalsIgnoreCase("TTTTT");
	}

	/**
	 * Checks if a nucleotide sequence consists of 7 consecutive identical
	 * nucleotide bases
	 * 
	 * @param poly
	 *            7 base nucleotide to be checked for the presence of a
	 *            homopolymer of length 7
	 * @return whether or not the sequence parameter consists of 5 consecutive
	 *         identical bases
	 */
	private boolean sevenConsecutive(String poly) {
		return poly.equalsIgnoreCase("AAAAAAA")
				|| poly.equalsIgnoreCase("CCCCCCC")
				|| poly.equalsIgnoreCase("GGGGGGG")
				|| poly.equalsIgnoreCase("TTTTTTT");
	}

	/**
	 * calculates the percentage distribution of each different type of
	 * nucleotide base. If any type of base comprises more than 35% of the
	 * sequence or less than 25% of the sequence then the fail score is
	 * increased by 1
	 */
	protected int getBaseFractionScore(Oligo o) {
		String sequence = o.getSequence();
		int baseFraction = 0;
		String nucleotides = "ACTG";
		for (int i = 0; i < nucleotides.length(); i++) 
		{
			String nuc = nucleotides.substring(i, i + 1);
			double nucCount = 0;
			for (int j = 0; j < sequence.length(); j++) 
			{
				String str = sequence.substring(j, j + 1);
				if (str.equalsIgnoreCase(nuc))
					nucCount++;
			}
			double percent = nucCount / sequence.length();
			double sDev = Math.sqrt((.25 * .75) / sequence.length());
			double zScore = (percent - .25) / sDev;
			double negZ = 0 - Math.abs(zScore);
			double lowerTail = CDF_Normal.normp(negZ);
			double pValue = 2 * lowerTail;
			String str = pValue + " ";
			pValue = Double.parseDouble(str.trim());
			// if(pValue < .1)
			// failScore++;
			if (percent < .25 || percent > .35)
				baseFraction++;

		}
		return baseFraction;
	}

	public boolean checkValues(){
		if(maxFail<0){
			ErrorReporter.reportError(this, "Filter : "+getFilterType()+" max ("+maxFail+") must be greater than 0.");
			return false;
		}
		return true;
	}
	/**
	 * returns the filter type
	 * 
	 * @return the filter type
	 */
	public String getFilterType() {
		return "failscore";
	}

	/**
	 * retrieves the Oligos that have been filtered out because of their fail
	 * score
	 * 
	 * @return the oligos that have been filtered out by fail score
	 */
	public ArrayList<Oligo> getRejectedOligos() {
		return rejectedOligos;
	}

	/**
	 * Creates an XML tag signifying the start of a Fail Score Filter and a
	 * subsequent tag denoting the maximum allowable fail score
	 * 
	 * @return an XML representation of the Fail Score Filter
	 */
	 
	public String getValue(Oligo o)
	{
		int failScore = getTMScore(o) + getHomoPolymerScore(o) + getBaseFractionScore(o);
		return "" + failScore;
 	}
	

	public String toXML() {
		String output = "";
		output += "<Filter Type=\""+getFilterType()+"\">\n";
		output += "<Parameter Constraint=\"failscore"+"\" Value=\""+maxFail+"\"/>\n";
		output += "</Filter>\n";
		return output;
	}

	/**
	 * A list of oligos rejected by this class.
	 */
	private ArrayList<Oligo> rejectedOligos;
	/**
	 * The maximum value that the FailScore algorithm uses to filter out oligos.
	 */
	private int maxFail;
}
