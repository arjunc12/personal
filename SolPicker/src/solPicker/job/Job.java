package solPicker.job;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Each solPicker.job object calls upon a different query and a different 
 * configuration to create oligos and filter them accordingly.
 * $Id: Job.java,v 1.24 2009/12/15 21:14:05 tizatt Exp $
 * @author Arjun Chandrasekhar
 */

import server.Domain;
import server.ensembl.GetEnsembleDna;
import server.ucsc.GetUCSCDna;
import solPicker.filters.Filter;

/**
 * Each solPicker.job object calls upon a different query and a different
 * configuration to create oligos and filter them accordingly. $Id: Job.java,v
 * 1.21 2009/10/28 15:41:51 tizatt Exp $
 * 
 * @author Arjun Chandrasekhar
 */
public class Job {
	/**
	 * Creates a Job Object with the Configuration field set equal to the first
	 * parameter and the Query Object set equal to the second parameter
	 * 
	 * @param c
	 *            the Job's Configuration
	 * @param q
	 *            the Job's Query
	 */
	public Job(Configuration c, Query q, int id) {
		config = c;
		this.q = q;
		this.id = id;
		queryHeader = "|c||ID|chr=" + q.getChromosomeIdentifier() + ";s="
				+ q.getStart() + ";e=" + q.getEnd() + ";\n";
		querySequences = "";
	}

	/**
	 * Retrieves all the appropriate sequence information, applies the
	 * appropriate filters. There are 4 files which are created - valid,
	 * invalid, all oligos, and query sequences. These are stored as separate
	 * strings in the ArrayList that is returned
	 * 
	 * @return fastaList - a list containing the 4 strings to be printed.
	 */
	public void performJob(ArrayList<PrintWriter> writers) {
		querySequences = retrieveSequences();
		ParseSequence parser = new ParseSequence(querySequences, q
				.getOligoLength(), q.getMinDistance(), q.getID());
		ArrayList<Oligo> oligos = parser.parse();
		filterOligos(oligos,writers);
	}

	/**
	 * Calls the filter methods for this Job. Loops through each oligo and
	 * filters them. Prints each oligo file to its respective writer.
	 * 
	 * @param oligos
	 * @param fastaList
	 * @return fastaList
	 */
	private void filterOligos(ArrayList<Oligo> oligos,
			ArrayList<PrintWriter> writers) {
		
		PrintWriter queryWriter = writers.get(0);
		PrintWriter oligoWriter = writers.get(1);
		PrintWriter invalidWriter = writers.get(2);
		PrintWriter validWriter = writers.get(3);
		
		queryWriter.write(getHeader());
		queryWriter.write(queryHeader);
		queryWriter.write(querySequences); // Add the query sequences String
		oligoWriter.write(getHeader());
		oligoWriter = printOligosFasta(oligos, oligoWriter); // Add the all oligos String
		
		invalidWriter.write(getHeader());
		int originalSize = oligos.size();
		
		for (Filter f : config.getFilters()) {
			System.out.println("filtering...");
			int prevSize = oligos.size();
			System.out.println("previous size: "+prevSize);
			oligos = f.filter(oligos); // filter statement
			invalidWriter = getFilterStatistics(f,oligos, prevSize, originalSize,invalidWriter);
		}
		
		validWriter.write(getHeader());
		validWriter = printOligosFasta(oligos, validWriter);
		

		Filter.resetInvalids();
	}
	
	
	/**
	 * Prints oligos in fasta format using a PrintWriter.
	 * @param oligos
	 * @param printWriter
	 * @return
	 */
	private PrintWriter printOligosFasta(ArrayList<Oligo> oligos, PrintWriter printWriter) {
		
		for (Oligo o : oligos) {
			printWriter.write(getOligoFasta(o));
		}
		return printWriter;
		
	}

	
	/**
	 * Calculates *invalid oligo* statistics for this filter and writes them to a file.
	 * @param f
	 * @param oligos
	 * @param previousSize
	 * @param originalSize
	 * @param writer
	 * @return
	 */
	private PrintWriter getFilterStatistics(Filter f, ArrayList<Oligo> oligos, int previousSize, int originalSize, PrintWriter writer){
		int numRejected = previousSize - oligos.size();
		double percentRejected = roundTwoDecimals((double)numRejected/(double)previousSize);
		int invalidSize = Filter.getInvalidOligos().size();
		double percentInvalid = roundTwoDecimals((double)invalidSize/(double)originalSize);
		writer.write("\n");
		writer.write("Before the " + f.getFilterType()
				+ " filter was applied there were " + previousSize
				+ " valid oligos remaining for " + getJobHeader()
				+ "\nNow there are " + oligos.size()
				+ " valid oligos remaining for " + getJobHeader() + "\n"
				+ "Of those " + previousSize + " oligos, " + numRejected + " ("
				+ percentRejected + "%) were rejected by this filter. \n"
				+ "The total number of oligos that have been rejected for "
				+ getJobHeader() + " is "
				+ Filter.getInvalidOligos().size() + " out of "
				+ originalSize + " (" + percentInvalid + "%)\n"
				+ "The following oligos were rejected by the "
				+ f.getFilterType() + "\n");
		for (Oligo o : f.getRejectedOligos()) {
			writer.write(getOligoFasta(o));
		}
		writer.write("\n\n");
		
		return writer;
	}

	/**
	 * Retrieves Query Sequences based on which domain was provided.
	 * 
	 * @return result
	 */
	private String retrieveSequences() {
		String domain = q.getDomain();
		Domain d;

		if (domain.equalsIgnoreCase("ensemble")
				|| domain.equalsIgnoreCase("ensembl"))
			d = new GetEnsembleDna(q);
		else
			d = new GetUCSCDna(q);
		String result = d.getDnaFor();
		return result;
	}

	/**
	 * Retrieves the sequence data corresponding to the Job's Query, Parses the
	 * sequence into an ArrayList of Oligos, filters the oligos. It then returns
	 * an ArrayList of Strings. The first string is the Query Sequence. The
	 * second String contains all the oligos that have been filtered. They are
	 * grouped based on what filter they were rejected by, and the file contains
	 * statistics on how many oligos were rejected by each filter. the 3rd
	 * string contains all the remaining valid oligos, grouped by what Query
	 * sequence they came from.
	 * @deprecated
	 * @return - An ArrayList of String containing the Query Sequence, the
	 *         invalid oligos, and the valid oligos
	 */
	public ArrayList<String> completeJob() {
		ArrayList<String> strList = new ArrayList<String>();
		String domain = q.getDomain();
		Domain d;

		if (domain.equalsIgnoreCase("ensemble")
				|| domain.equalsIgnoreCase("ensembl"))
			d = new GetEnsembleDna(q);
		else
			d = new GetUCSCDna(q);
		GetRequest request = new GetRequest(d.getURL());
		request.sendGetRequest();
		String fasta = request.getResult();
		String result = d.cutHeader(fasta);
		ParseSequence parser = new ParseSequence(result, q.getOligoLength(), q
				.getMinDistance());
		String sequenceHeader = "Sequence for " + getJobHeader() + "\n";
		sequenceHeader += result;
		sequenceHeader += "\n";
		sequenceHeader += "\n";
		strList.add(sequenceHeader);
		ArrayList<Oligo> oligos = parser.parse();
		ArrayList<Filter> filters = config.getFilters();
		int originalSize = oligos.size();
		String invalidOligos = "";

		for (int i = 0; i < filters.size(); i++) {
			Filter f = filters.get(i);
			String fType = f.getFilterType();
			int previousSize = oligos.size();
			oligos = f.filter(oligos);
			int laterSize = oligos.size();
			ArrayList<Oligo> rejectedOligos = f.getRejectedOligos();
			double rejectedSize = rejectedOligos.size();
			invalidOligos += "Before the " + fType
					+ " filter was applied there were " + previousSize
					+ " valid oligos remaining for " + getJobHeader() + "\n";
			invalidOligos += "Now there are " + laterSize
					+ " valid oligos remaining for " + getJobHeader() + "\n";
			double percentRejected = rejectedSize / previousSize;
			invalidOligos += "Of those " + previousSize + " oligos, "
					+ rejectedSize + " (" + percentRejected
					+ "%) were rejected by this \n";
			int invalidSize = Filter.getInvalidOligos().size();
			double origSize = (double) originalSize;
			double percentInvalid = invalidSize / origSize;
			invalidOligos += "The total number of oligos that have been rejected for "
					+ getJobHeader()
					+ " is "
					+ invalidSize
					+ " out of "
					+ originalSize + " (" + percentInvalid + "%)\n";
			invalidOligos += "The Following Oligos were rejected by the "
					+ fType + " of " + getJobHeader() + ":\n";
			invalidOligos += oligosToString(rejectedOligos, true);
			invalidOligos += "\n";
			invalidOligos += "\n";
		}
		strList.add(invalidOligos);
		String validOligos = "The Following are the remaining valid oligos for "
				+ getJobHeader() + ":\n";
		validOligos += oligosToString(oligos, false);
		strList.add(validOligos);
		Filter.resetInvalids();
		return strList;
	}

	public ArrayList<String> produceFastas(ArrayList<PrintWriter> writers) {

		ArrayList<String> fastaList = new ArrayList<String>();
		String domain = q.getDomain();
		Domain d;
		if (domain.equalsIgnoreCase("ensemble")
				|| domain.equalsIgnoreCase("ensembl"))
			d = new GetEnsembleDna(q);
		else
			d = new GetUCSCDna(q);
		GetRequest request = new GetRequest(d.getURL());
		request.sendGetRequest();
		String fasta = request.getResult();
		String result = d.cutHeader(fasta);
		String fasta1 = "|c||ID|chr=" + q.getChromosomeIdentifier() + ";s="
				+ q.getStart() + ";e=" + q.getEnd() + ";\n" + result;
		fastaList.add(addHeader(fasta1));
		ParseSequence seqParser = new ParseSequence(result, q.getOligoLength(),
				q.getMinDistance(), q.getID());
		ArrayList<Oligo> validOligos = seqParser.parse();
		String allOligosFasta = "";
		for (int i = 0; i < validOligos.size(); i++) {
			Oligo o = validOligos.get(i);
			allOligosFasta += getOligoFasta(o);
		}
		fastaList.add(addHeader(allOligosFasta));
		ArrayList<Filter> filters = config.getFilters();

		StringBuffer invalidOligos = new StringBuffer();
		int originalSize = validOligos.size();
		for (Filter f : filters) {
			String fType = f.getFilterType();
			int previousSize = validOligos.size();
			validOligos = f.filter(validOligos);
			int laterSize = validOligos.size();
			ArrayList<Oligo> rejectedOligos = f.getRejectedOligos();
			double rejectedSize = rejectedOligos.size();

			invalidOligos.append("Before the " + fType
					+ " filter was applied there were " + previousSize
					+ " valid oligos remaining for " + getJobHeader()
					+ "\nNow there are " + laterSize
					+ " valid oligos remaining for " + getJobHeader() + "\n");

			double percentRejected = 100 * rejectedSize / previousSize;
			invalidOligos.append("Of those " + previousSize + " oligos, "
					+ rejectedSize + " (" + percentRejected
					+ "%) were rejected by this \n");

			int invalidSize = Filter.getInvalidOligos().size();
			double origSize = (double) originalSize;
			double percentInvalid = invalidSize / origSize;
			invalidOligos
					.append("The total number of oligos that have been rejected for "
							+ getJobHeader()
							+ " is "
							+ invalidSize
							+ " out of "
							+ originalSize
							+ " ("
							+ percentInvalid
							+ "%)\nThe Following Oligos were rejected by the "
							+ fType + " of " + getJobHeader() + ":\n");
			for (Oligo o : f.getRejectedOligos()) {
				invalidOligos.append(getOligoFasta(o));
			}
			invalidOligos.append("\n\n\n\n\n\n");
		}
		fastaList.add(addHeader(invalidOligos.toString()));
		fastaList.add(addHeader(getOligosFasta(validOligos)));
		return fastaList;
	}

	/**
	 * Creates fasta formatted String representations of the query sequences,
	 * valid oligos, invalid oligos and all oligos
	 * 
	 * @return fastas
	 */
	@SuppressWarnings("unused")
	private HashMap<String, String> toFasta() {
		HashMap<String, String> fastas = new HashMap<String, String>();
		fastas.put("Query Sequences", q.toFasta());
		// fastas.put("All Oligos", oligosToFasta(allOligos, false));

		return fastas;

	}

	/**
	 * Retrieves the Configuration' unique ID Number
	 * 
	 * @return the configuration ID Number
	 */
	public int getID() {
		return id;
	}

	/**
	 * Retrieves the Configuration's Unique ID
	 * 
	 * @return the Configuration ID
	 */
	public String getConfigurationID() {
		return configurationID;
	}

	/**
	 * Retrieves the Unique ID of the Query Object contained in the Job
	 * 
	 * @return the Query ID
	 */
	public String getQueryID() {
		return queryID;
	}

	/**
	 * Creates an XML tag signifying the start of a Job Object and SubSequent
	 * Tags denoting the ID's of the Query and Configuration contained in the
	 * Job Object
	 * 
	 * @return an XML representation of the Job Object
	 */
	public String toXML() {
		String output = "";
		output += "<Job ConfigID=\"" + config.getID() + "\" JobID=\"" + id
				+ "\" QueryName=\"" + q.getID() + "\"/>\n";
		return output;
	}

	/**
	 * Creates a string representation of an ArrayList of oligos. For each oligo
	 * in the list, the method creates a new line of text that contains that
	 * Oligo's sequence followed by its ID.
	 * 
	 * @param oligos
	 *            the ArrayList of Oligos that is to be put into a String form
	 * @return a String representation of the list of Oligos
	 */
	private String oligosToString(ArrayList<Oligo> oligos, boolean rejected) {
		if (oligos.size() == 0)
			return "[]";
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < oligos.size(); i++) {
			Oligo o = oligos.get(i);
			str.append("Oligo ID: " + o.getID() + " Oligo Sequence: "
					+ o.getSequence() + "\n");
			if (rejected)
				str.append(o.getReasonForRejection() + "\n");
		}
		str.append("\n\n");
		return str.toString();
	}

	
	/**
	 * 
	 * @param d
	 * @return
	 */
	double roundTwoDecimals(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
    	return Double.valueOf(twoDForm.format(d));
	}
	
	
	
	
	
	
	/**
	 * Creates a header for each individual Job Object that specifies the
	 * solPicker.job ID and the ID's of each of the Query and Configuration
	 * instance fields
	 * 
	 * @return a header for the individual Job Object that calls the method
	 */
	private String getJobHeader() {
		return "Job " + id + " (Query ID = " + q.getID()
				+ " Configuration ID = " + config.getID() + ")";
	}

	/**
	 * 
	 * @param o
	 * @return
	 */
	private String getOligoFasta(Oligo o) {
		String def = ": lcl|ID|query=" + o.getQID() + ";query_offset="
				+ o.getID();
		ArrayList<Filter> filters = config.getFilters();
		for (int i = 0; i < filters.size(); i++) {
			Filter f = filters.get(i);
			def += f.getFilterType() + " value=" + f.getValue(o) + ";";
		}
		def += "\n" + o.getSequence() + "\n";
		return def;
	}

	/**
	 * 
	 * @param oligos
	 * @return
	 */
	private String getOligosFasta(ArrayList<Oligo> oligos) {
		StringBuffer fasta = new StringBuffer();
		for (Oligo o : oligos) {
			fasta.append(getOligoFasta(o));
		}
		return fasta.toString();
	}

	
	private String getHeader(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		java.util.Date date = new java.util.Date();
		dateFormat.format(date);
		String header = "SolPicker_";
		header += dateFormat.format(date);
		return header;
	}
	/**
	 * Adds a header to the fasta file with the data/time and SolPicker
	 * 
	 * @param fasta
	 * @return
	 */
	private String addHeader(String fasta) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		java.util.Date date = new java.util.Date();
		dateFormat.format(date);
		String header = "SolPicker_";
		header += dateFormat.format(date);
		return header + "\n" + fasta;
	}

	/**
	 * The list of query sequences
	 * 
	 * @return
	 */
	public String getQuerySequences() {
		return querySequences;
	}

	/**
	 * Returns the current data and time in the format - yyyy-MM-dd HH:mm
	 * 
	 * @return
	 */
	private String getDateTime() {
		final String DATE = "yyyy-MM-dd HH:mm";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE);

		return sdf.format(cal.getTime());
	}

	private String queryHeader;
	private String querySequences;
	private String configurationID;
	private String queryID;
	private Configuration config;
	private Query q;
	private int id;
	// private static int ID = 0;
}
