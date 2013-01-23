package solPicker.filters;

import java.util.ArrayList;

import solPicker.job.Oligo;

/**
 * Often, enzymes are used to cleave the DNA sequences before applying them to
 * the microarray. However, if there were an oligo present that complemented the
 * enzyme used, the oligo would be useless. The user is able to specify a list
 * of enzymes, and this filter will search for oligos that compliment it in
 * either the forward or reverse direction. These oligos will be discarded.
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: EnzymeFilter.java,v 1.5 2009/10/28 21:14:05 tizatt Exp $
 */

public class EnzymeFilter extends Filter 
{
	/**
	 * Creates an Enzyme Filter with an ArrayList of enzymes set equal to the
	 * parameter
	 * 
	 * @param eList
	 *            the list of enzymes that Oligos are not allowed to complement
	 *            in either the forward or reverse direction
	 */

	public EnzymeFilter(ArrayList<String> eList) 
	{
		enzymes = eList;
		rejectedOligos = new ArrayList<Oligo>();
	}

	/**
	 * Takes an ArrayList of Oligos as a parameter and Filters out all the
	 * Oligos that complement certain enzymes in either the forward or reverse
	 * directions
	 * 
	 * @return an ArrayList of Oligos that do not complement a certain group of
	 *         enzymer
	 */
	public ArrayList<Oligo> filter(ArrayList<Oligo> validOligos) 
	{
		for (int i = 0; i < enzymes.size(); i++) 
		{
			for (int j = 0; j < validOligos.size(); j++) 
			{
				String enzyme = enzymes.get(i);
				Oligo o = validOligos.get(j);
				String sequence = o.getSequence();
				if (searchForMatch(sequence, enzyme)) 
				{
					o.reject("complemented the enzyme " + enzyme, enzyme);
					validOligos.remove(j--);
					invalidOligos.add(o);
					rejectedOligos.add(o);
				}
			}
		}
		return validOligos;
	}

	/**
	 * Creates a sequence that compliments the enzyme by making the A's become
	 * T's, the G's become C's, et cetera.
	 * 
	 * @param sequence
	 *            a sequence of nucleotides
	 * @return the nucleotide sequence that would complement the sequence that
	 *         was given as a parameter
	 */
	private String getComplement(String sequence) {
		String seq = "";
		for (int i = 0; i < sequence.length(); i++) {
			char ch = sequence.charAt(i);
			if (ch == 'A') {
				seq += "T";
			}
			if (ch == 'C') {
				seq += "G";
			}
			if (ch == 'G') {
				seq += "C";
			}
			if (ch == 'T') {
				seq += "A";
			}
		}
		return seq;
	}

	/**
	 * Reverses the complimentary sequence to be able to screen for the enzyme
	 * compliment in both the forward and reverse directions
	 * 
	 * @param sequence
	 *            a sequence of nucleotides
	 * @return the original sequence with the order of letters reversed
	 */
	private String getReverse(String sequence) {
		String reverse = "";
		for (int i = sequence.length() - 1; i >= 0; i--) {
			reverse += sequence.charAt(i);
		}
		return reverse;
	}

	/**
	 * Tests to see if the complimentary enzyme sequence (forward or reverse)
	 * matches the oligo. If it does, the oligo is moved to the list of rejected
	 * oligos.
	 * 
	 * @param oligo
	 *            the oligo sequence that is being tested
	 * @param enzyme
	 *            the sequence of the enzyme being tested
	 * @return returns true if any segment of the oligo complements the enzyme
	 *         in either direction, false if not
	 */
	private boolean searchForMatch(String oligo, String enzyme) {
		String complement = getComplement(enzyme);
		String reverseComplement = getReverse(complement);
		for (int i = 0; i < oligo.length() - complement.length() + 1; i++) {
			if (complement.equals(oligo.substring(i, i + complement.length()))) {
				return true;
			}
		}

		for (int i = 0; i < oligo.length() - reverseComplement.length() + 1; i++) {
			if (reverseComplement.equals(oligo.substring(i, i
					+ reverseComplement.length()))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * retrieves all the oligos that have been filtered out because they
	 * complemented a certain enzyme
	 * 
	 * @return the list of oligos that have been rejected by the Enzyme Filter
	 */
	public ArrayList<Oligo> getRejectedOligos() {
		return rejectedOligos;
	}

	/**
	 * returns the filter type
	 * 
	 * @return the filter type
	 */
	public String getFilterType() {
		return "enzyme";
	}

	public boolean checkValues(){
		return true;
	}


	public String getValue(Oligo o)
	{
		return o.getComplementaryEnzyme();
	}
	/**
	 * a list containing the enzymes used by this class.
	 */
	private ArrayList<String> enzymes;
	private ArrayList<Oligo> rejectedOligos;
	
	
	
	public String toXML() {
		String output = "";
		output += "<Filter Type=\""+getFilterType()+"\">\n";
		for(String e:enzymes){
			output += "<Parameter Constraint=\"enzyme"+"\" Value=\""+e+"\"/>\n";
		}
		output += "</Filter>\n";
		return output;
	}
	
}
