package solPicker.filters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import solPicker.job.Oligo;



/**
 * Implements the BinFilter
 * @author tizatt
 *
 */
public class BinFilter {
	
	
	public BinFilter(ArrayList<Oligo> oligos, int sequenceLength){
		this.oligos = oligos;
		this.binWidth = oligos.get(0).getSequence().length();
		numberOfBins();
		initializeBins();
	}
	
	
	
	public void numberOfBins(){
		double numOfOligos = (double) oligos.size();
		numOfBins = (int) (numOfOligos/binWidth);
		bin = (ArrayList<Oligo>[])new ArrayList[numOfBins];
	}
	
	
	public void initializeBins(){
		int oligoCounter = 0;
		for(int i = 0; i<numOfBins; i++){
			ArrayList<Oligo> row = new ArrayList<Oligo>();
			for(int j =0; j<binWidth; j++){
				row.add(oligos.get(oligoCounter));
				oligoCounter++;
			}
			bin[i] = row;
		}
	}
	
	
	/**
	 * Note - this filter must already be initialized!
	 * @param filter
	 */
	public void sortBins(Filter filter){
		this.filter = filter;
		for(int i = 0; i < bin.length; i++){
			Collections.sort(bin[i], new compareFilterValues());
		}
	}
	
	
	class compareFilterValues implements Comparator<Oligo>{
		public int compare(Oligo o1, Oligo o2) {
			// TODO Auto-generated method stub
			String filterType = filter.getFilterType();
			double val1 = (Double) o1.getFilterValue(filterType);
			double val2 = (Double) o2.getFilterValue(filterType);
			return Double.compare(val1, val2);
		}

	}
	
	private Filter filter;
	private ArrayList<Oligo>[] bin;
	private double binWidth;
	private int numOfBins;
	private ArrayList<Oligo> oligos;
}
