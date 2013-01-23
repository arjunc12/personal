package solPicker.filters;

import java.util.ArrayList;

import solPicker.ErrorReporter;
import solPicker.job.Oligo;

//import solpicker.CDF_Normal;
/**
 * Calculates the percent distribution of each of the nucleotide bases, and
 * filters oligos based on the equality of the distributions. The more uneven
 * the distribution, the higher the base fraction score. For each base whose
 * percent distribution is not between 25% and 35%, this filter adds one point
 * to the base fraction score. Users can indicate a maximum score.
 * 
 * @version $Id: BaseFractionFilter.java,v 1.6 2009/10/28 21:14:05 tizatt Exp $
 */

public class BaseFractionFilter extends Filter {
	
	
	
	public BaseFractionFilter(double min, double max) {
		rejectedOligos = new ArrayList<Oligo>();
		minProp = min;
		maxProp = max;
		checkValues();
	}

	public boolean checkValues() {
		if(minProp>maxProp){
			ErrorReporter.reportError(this,"Filter : "+getFilterType()+" min ("+minProp+") is greater than max("+maxProp+").");
			return false;
		}
		
		if(minProp<0 || minProp>100)
			ErrorReporter.reportError(this, "Filter : "+getFilterType()+" min ("+minProp+") must be between 0 and 100.");
		if(maxProp>0 || maxProp>100)
			ErrorReporter.reportError(this, "Filter : "+getFilterType()+" max ("+maxProp+") must be between 0 and 100.");
		return true;
	}

	/**
	 * Takes an ArrayList of oligos as a parameter and filters out all oligos
	 * with a base fraction score greater than the specified maxmimum.
	 * 
	 * @param oligos
	 *            the ArrayList of oligos to be filtered
	 * @return an ArrayList of oligos whose TM scores meet the parameters
	 */
	public ArrayList<Oligo> filter(ArrayList<Oligo> oligos) {
		String bases = "ACTG";
		for (int i = 0; i < bases.length(); i++) {
			String base = bases.substring(i, i + 1);
			for (int j = 0; j < oligos.size(); j++) {
				Oligo o = oligos.get(j);
				double percent = getBaseFraction(o, base);
				o.addFilterValue(this.getFilterType(),percent);
				if (percent < minProp || percent > maxProp) {
					String reason = "";
					if (percent < minProp)
						reason += "Base Fraction for the letter \""
								+ base
								+ "\" is too low.  The minimum acceptable base fraction was "
								+ minProp;
					else
						reason += "Base Fraction for the letter \""
								+ base
								+ "\" is too high.  The maximum acceptable base fraction was "
								+ maxProp;
					reason += " but the actual base fraction was " + percent;
					o.reject(reason);
					oligos.remove(j--);
					invalidOligos.add(o);
					rejectedOligos.add(o);
				}
				/*
				 * String sequence = o.getSequence(); int length =
				 * sequence.length(); double pValue = runZTest(length, percent);
				 * if(pValue < minPValue) {o.reject(
				 * "P-Value is too low.  The minimum acceptable P-Value was " +
				 * minPValue + " but the actual P-Value was " + pValue);
				 * validOligos.remove(j); invalidOligos.add(o);
				 * rejectedOligos.remove(o); j--; }
				 */
			}
		}
		return oligos;
	}

	private double getBaseFraction(Oligo o, String nucleotide) {
		String sequence = o.getSequence();
		double nucCount = 0;
		int length = sequence.length();
		for (int i = 0; i < length; i++) {
			String nuc = sequence.substring(i, i + 1);
			if (nuc.equalsIgnoreCase(nucleotide))
				nucCount++;
		}
		return nucCount / length;
	}

	/*
	 * private double runZTest(int length, double percent) { double variance =
	 * (.25 * .75) / length; double sDev = Math.sqrt(variance); double zScore =
	 * (percent - .25) / sDev; double negZ = 0 - Math.abs(zScore); double
	 * lowerTail = CDF_Normal2.normp(negZ); double pValue = 2 * lowerTail; return
	 * pValue; }
	 */

	public String getFilterType() {
		return "Base Fraction Filter";
	}

	public ArrayList<Oligo> getRejectedOligos() {
		return rejectedOligos;
	}

	public String getValue(Oligo o)
	{
		return "";
	}
	
	private ArrayList<Oligo> rejectedOligos;
	private double minProp;
	private double maxProp;
	// private double minPValue;
	
	public String toXML() {
		String output = "";
		output += "<Filter Type=\""+getFilterType()+"\">\n";
		output += "<Parameter Constraint=\"min"+"\" Value=\""+minProp+"\"/>\n";
		output += "<Parameter Constraint=\"max"+"\" Value=\""+maxProp+"\"/>\n";
		output += "</Filter>\n";
		return output;
	}
	
}
