package solPicker.filters;


import java.util.ArrayList;
import ncbi.blast.NCBIBlastFactory;
import solPicker.Log;
import solPicker.job.Oligo;


/**
 * This class filters out oligos that are not unique and that do not have a good align length.
 * It accepts a list of oligos and runs a blast filter - specified in {@link ncbi.blast.NCBIBlastFactory}
 * @author Tyler Izatt and Arjun Chandrasekhar
 * @version $Id: BlastFilter.java,v 1.12 2009/10/28 21:14:05 tizatt Exp $
 */
public class BlastFilter extends Filter {


	/**
	 * Default BLAST Filter Constructor that initializes the ArrayList of oligos
	 * that have been rejected by the BLAST Filter
	 */
	public BlastFilter() {
		rejectedOligos = new ArrayList<Oligo>();
		checkValues();
	}
	

	/**
	 * Takes an ArrayList of Oligos as a parameter and filters out all oligos
	 * that have more than one BLAST hit or that have too short of an align
	 * length
	 * 
	 * @return an ArrayList of Oligos that is filtered for Unique Hits and Align
	 *         Length
	 */
	public ArrayList<Oligo> filter(ArrayList<Oligo> oligos) {
		Log.logMessage("Performing Blast filter");
		Log.logMessage("# of oligos before blast = "+oligos.size());
		ncbi = new NCBIBlastFactory(oligos);
		oligos = ncbi.blast();
		for(int i = 0; i<oligos.size(); i++){
			Oligo o = oligos.get(i);
			if(o.hasBeenRejected()){
				invalidOligos.add(o);
				rejectedOligos.add(o);
				oligos.remove(i--);
			}
		}
		
		return oligos;
	}

	/**
	 * Returns the filter type
	 * 
	 * @return the filter type
	 */
	public String getFilterType() {
		return "BLAST Filter";
	}
	


	/**
	 * retrieves the list of Oligos that were filtered out due to non unique
	 * hits or short align lengths
	 * 
	 * @return the list of Oligos that have been rejected by the BLAST Filter
	 */
	public ArrayList<Oligo> getRejectedOligos() {
		return rejectedOligos;
	}


	public boolean checkValues(){
		return true;
	}

	/**
	 * a list containing all oligos that were filtered out by the Blast filter.
	 */
	protected ArrayList<Oligo> rejectedOligos;
	public String getValue(Oligo o) {
		return "";
	}


	public String toXML() {
		if(ncbi == null)
			return "";
		String output = "";
		output += "<Filter Type=\""+getFilterType()+"\">\n";
		output += "<Parameter Constraint=\"alignLength"+"\" Value=\""+ncbi.getAlignLength()+"\"/>\n";
		output += "</Filter>\n";
		return output;
	}

	
	public NCBIBlastFactory ncbi; 
	
}
