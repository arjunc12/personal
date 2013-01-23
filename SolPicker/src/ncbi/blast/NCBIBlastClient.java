package ncbi.blast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import solPicker.ErrorReporter;
import solPicker.job.JobFactory;

/**
 * The blast client for SolPicker which uses the NCBI <a href =
 * "http://www.ncbi.nlm.nih.gov/staff/tao/URLAPI/netblast.html">netblast
 * program</a> which can be downloaded <a href="ftp://ftp.ncbi.nlm.nih.gov/blast/executables/LATEST/"> here</a> 
 * To use this client you must have an Internet connection.
 * Important Note: If running this client with SolPicker, netblast must be downloaded and put into
 * the same directory where SolPicker executable is.
 * 
 * 
 * @version $Id: NCBIBlastClient.java,v 1.13 2009/12/15 21:14:05 tizatt Exp $
 * @see <a href =
 *      "http://www.ncbi.nlm.nih.gov/staff/tao/URLAPI/netblast.html">blastcl3</a>
 * @author tizatt
 * 
 */
public class NCBIBlastClient {

	/**
	 * Constructor for NCBIBlastClient
	 */
	public NCBIBlastClient(String inputFile, String outputFile) {
		params = new HashMap<String, String>();
		clientOptions = "";
		this.inputQueryFile = inputFile;
		this.outputFile = outputFile;
	}

	/**
	 * Specifies which program to run. Default is none, MANDATORY. Options:
	 * blastn, blastp, blastx, tblastn, tblastx.
	 * 
	 * @param program
	 */
	public void setProgram(String program) {
		params.put("p", program);
	}

	/**
	 * Specifies target database to search against. Default is nr, MANDATORY. To
	 * search against multiple databases separate database names by a space
	 * Example: -d "db1 db2".
	 * 
	 * @param database
	 */
	public void setDatabase(String database) {
		params.put("d", database);
	}

	/**
	 * Specifies input query file. Include complete filename with extension.
	 * Default is stdin.
	 * 
	 * @param file
	 *            - a file containing query sequences. Typical format is
	 *            multi-fasta.
	 */
	public void setInputQueryFile(String file) {
		params.put("i", file);
	}

	/**
	 * Specifies expect value cutoff. Accepted formats are integer, fraction,
	 * decimal, exponential, and scientific notation. To set the cutoff to
	 * 2*10-20, use -e 2e-20
	 * 
	 * @param value
	 *            -
	 */
	public void setExpectValueCutOff(int value) {
		params.put("e", value + "");
	}

	/**
	 * Options: 0 Pairwise 1 query-anchored showing identities 2 query-anchored
	 * no identities 3 flat query-anchored, show identities 4 flat
	 * query-anchored, no identities 5 query-anchored no identities and blunt
	 * ends 6 flat query-anchored, no identities and blunt ends 7 XML Blast
	 * output 8 tabular (not post processing) 9 tabular with comment lines
	 * (post-processed, sorted) 10 ASN, text 11 ASN, binary
	 * 
	 * @param alignmentView
	 *            - the output format of the alignment
	 */
	public void setAlignmentViewOption(int alignmentView) {
		params.put("m", alignmentView + "");
	}

	/**
	 * Specifies the output file. Default is stdout.
	 * 
	 * @param outputFile
	 *            - file where blast output is stored
	 */
	public void setOutputFile(String outputFile) {
		params.put("o", outputFile);
	}

	/**
	 * Specifies which filter(s) to use to mask query sequence. Default is T
	 * (DUST for nucleotide, SEG for protein). Options: T,F,D,L,R,V,S,C, and m.
	 * 
	 * @param filter
	 *            - a filter optoin (T,F,D,L,R,V,S,C, or m).
	 */
	public void setFilter(String filter) {
		params.put("F", filter);
	}

	/**
	 * Cost to open a gap. Default 0. Default invokes 5 for blastn
	 * 
	 * @param gapOpen
	 *            - integer representing the cost to open a gap.
	 */
	public void setGapOpenPenalty(int gapOpen) {
		params.put("G", gapOpen + "");
	}

	/**
	 * Cost to extend a gap. Default 0. Default invokes 2 for blastn.
	 * 
	 * @param gapExtend
	 *            - integer representing the cost to extend a gap.
	 */
	public void setGapExtendPenalty(int gapExtend) {
		params.put("E", gapExtend + "");
	}

	/**
	 * X dropoff value for gapped alignment (in bits). Default is 0. Default
	 * settings: blastn - 30 megablast - 20 tblastx - 0 others - 15
	 * 
	 * @param gappedAlignDropoff
	 */
	public void setGappedAlignmentDropoff(int gappedAlignDropoff) {
		params.put("X", gappedAlignDropoff + "");
	}

	/**
	 * Show GI in definition line
	 * 
	 * @param gi
	 *            - if true, definition line will be shown
	 */
	public void setShowGIInDefline(boolean gi) {
		if (gi)
			params.put("I", "T");
		else
			params.put("I", "F");
	}

	/**
	 * Specifies penalty for a nucleotide mismatch. Default is -3. For blastn
	 * only. Different -r/-q rtios are optimal for aligning sequences with
	 * different percentage of similarities.
	 * 
	 * @param mismatchPenalty
	 */
	public void setNucleotideMismatchPenalty(int mismatchPenalty) {
		params.put("q", mismatchPenalty + "");
	}

	/**
	 * Specifies reward for a nucleotide match. Default is 1. For blastn only.
	 * Others use external scoring matrix to determine this.
	 * 
	 * @see #setProteinScoringMatrix(String)
	 * @param reward
	 */
	public void setNucleotideMatchReward(int reward) {
		params.put("r", reward + "");
	}

	/**
	 * Specifies the upper limit of database sequences to show descriptions for.
	 * Actual number may be lower due to lack of hits above -e cutoff. Default
	 * is 500.
	 * 
	 * @param limit
	 *            - upper limit of database sequences to show description for.
	 */
	public void setDescriptionsUpperLimit(int limit) {
		params.put("v", limit + "");
	}

	/**
	 * Specifies the upper limit of database sequences to show alignment for.
	 * Physical limit is 200000. Web counterpart: "Alignments". This is NOT the
	 * total number of alignment segments or high scoring pairs(HSPs). Rather it
	 * is the number of database sequences with HSP(s) to the query. Default is
	 * 250.
	 * 
	 * @param limit
	 */
	public void setAlignmentsUpperLimit(int limit) {
		params.put("b", limit + "");
	}

	/**
	 * Specifies the threshold for extending hits. Default is 0. Default
	 * Setting: blastp - 11 blastn - 0 blastx - 12 tblastn - 13 tblastx - 13
	 * megablast - 0
	 * 
	 * @param threshold
	 */
	public void setThresholdForExtendingHits(int threshold) {
		params.put("f", threshold + "");
	}

	/**
	 * Specifies whether or not to perform a gapped alignment. Default is gapped
	 * alignment (true), not available with tblastx.
	 * 
	 * @param performGappedAlignment
	 */
	public void setPerformGappedAlignment(boolean performGappedAlignment) {
		if (performGappedAlignment)
			params.put("g", "T");
		else
			params.put("g", "F");
	}

	/**
	 * Specifies query genetic code to use. Default is 1. This specifies the <a
	 * href
	 * ="www.ncbi.nlm.nih.gov/Taxonomy/Utils/wprintgc.cgi?mode=c">translation
	 * table</a> used in query translation during blastx and tblastx searches.
	 * The default is universal codon.
	 * 
	 * @see <a href =
	 *      "www.ncbi.nlm.nih.gov/Taxonomy/Utils/wprintgc.cgi?mode=c">translation
	 *      table</a>
	 * @param geneticCode
	 */
	public void setQueryGeneticCode(int geneticCode) {
		params.put("Q", geneticCode + "");
	}

	/**
	 * Specifies database genetic code to use. Default is 1. This specifies the
	 * <a href = "www.ncbi.nlm.nih.gov/Taxonomy/Utils/wprintgc.cgi?mode=c">
	 * translation table</a> used in query translation during blastx and tblastx
	 * searches. The default is universal codon.
	 * 
	 * @see <a href =
	 *      "www.ncbi.nlm.nih.gov/Taxonomy/Utils/wprintgc.cgi?mode=c">translation
	 *      table</a>
	 * @param geneticCode
	 */
	public void setDBGeneticCode(int geneticCode) {
		params.put("D", geneticCode + "");
	}

	/**
	 * Specifies the number of processors to use. This method may not be
	 * relevant after the splitd implementation. Default is 1.
	 * 
	 * @param numOfProcessors
	 */
	public void setNumOfProcessors(int numOfProcessors) {
		params.put("a", numOfProcessors + "");
	}

	/**
	 * Specifies file to save SeqAlign Object to. Default N/A.
	 * 
	 * @see <a href =
	 *      "ftp://ftp.ncbi.nih.gov/blast/demo/">ftp://ftp.ncbi.nih.gov/blast/demo/</a>
	 * @param file
	 */
	public void setSeqAlignOutputFile(String file) {
		params.put("O", file);
	}

	/**
	 * Specifies whether to believe the query definition line or not. Default is
	 * false. Since most query deflines do not follow NCBI convention the
	 * default is false.
	 * 
	 * @param queryDefline
	 */
	public void setBelieveQueryDefline(boolean queryDefline) {
		if (queryDefline)
			params.put("J", "T");
		else
			params.put("J", "F");
	}

	/**
	 * Specifies the protein scoring matrix to use. Default: BLOSUM62. Options:
	 * BLOSUM45, BLOSUM62, BLOSUM80, PAM30, OR PAM70.
	 * 
	 * @param matrix
	 */
	public void setProteinScoringMatrix(String matrix) {
		params.put("M", matrix);
	}

	/**
	 * Specifies the word size for different programs. Default is 0. Default
	 * Settings: blastn - 11 megablast - 28 all others - 3
	 * 
	 * @param wordSize
	 */
	public void setWordSize(int wordSize) {
		params.put("W", wordSize + "");
	}

	/**
	 * Specifies the effective length of the database. Default is 0. Default
	 * setting: Leaving out this parameter or setting it to zero (0), BLAST will
	 * use the actual database size.
	 * 
	 * @param databaseLength
	 */
	public void setDatabaseLength(double databaseLength) {
		params.put("z", databaseLength + "");
	}

	/**
	 * Specifies the number of best hits from a region to keep. Default is 0.
	 * This selects the specified number of best hits for a given region of the
	 * query for further evaluation. Off by default, 100 is recommended if used.
	 * 
	 * @param numOfHits
	 */
	public void setNumOfBestHitsToKeepFromRegion(int numOfHits) {
		params.put("K", numOfHits + "");
	}

	/**
	 * Specifies whether to use multiple hit or not. Default is 0. Zero is for
	 * multiple hit, 1 for single hit. Not applicable to blastn.
	 * 
	 * @param useMultipleHit
	 */
	public void setUseMultipleHit(int useMultipleHit) {
		params.put("P", useMultipleHit + "");
	}

	/**
	 * Specifies the effective length of the search space. Default is 0. This is
	 * the product of effective query length and effective database length -
	 * actual length corrected for edge effects. Use zero for actual size.
	 * 
	 * @param length
	 */
	public void setLengthOfSearchSpace(double length) {
		params.put("Y", length + "");
	}

	/**
	 * Specifies strands of the nucleotide query to use in the search. Default
	 * is 3. Options: 1 - Input sequence, 2 - Reverse complement, 3 - Both
	 * (meanings for blastn, blastx, and tblastx).
	 * 
	 * @param queryStrands
	 */
	public void setStrandsOfQuery(int queryStrands) {
		params.put("S", queryStrands + "");
	}

	/**
	 * Specifies whether or not to produce HTML output. Default is false. With
	 * true, BLAST will hyperlink the matched subject sequences to their actual
	 * entries in Entrez.
	 * 
	 * @param produceHTMLOutput
	 */
	public void setProduceHTMLOutput(boolean produceHTMLOutput) {
		if (produceHTMLOutput)
			params.put("T", "T");
		else
			params.put("T", "F");
	}

	/**
	 * Restrict search of database to the subset satisfying the query. Default
	 * is N/A. Argument to this parameter is a set of valid Entrez query terms.
	 * BLAST server will use the terms to retrieve a list of GI numbers and use
	 * them to restrict the BLAST search to entries specified by the list. Make
	 * sure valid terms are used. For example, it does not make sense to
	 * restrict a search to genomic sequences while searching against the est
	 * database.
	 * 
	 * @see <a href =
	 *      "www.ncbi.nlm.nih.gov/entrez/query/static/help/helpdoc.html">Entrez
	 *      Help</a>
	 * @param entrezTerms
	 */
	public void setRestrictDatabaseSearchToSubset(String entrezTerms) {
		params.put("u", "\"" + entrezTerms + "\"");
	}

	/**
	 * Specifies whether or not to use lower case filtering of FASTA sequence.
	 * Default is false. Make sure that only the query sequences to be masked
	 * are in UPPERCASE and only the filtered portions are in lowercase.
	 * 
	 * @param lowerCaseFilter
	 */
	public void setLowercaseFilter(boolean lowerCaseFilter) {
		if (lowerCaseFilter)
			params.put("U", "T");
		else
			params.put("U", "F");
	}

	/**
	 * Specifies the X dropoff value for ungapped extensions (in bits). Default
	 * is 0. Default settings for ungapped extension X dropoff (in bits): blastn
	 * - 20, megablast - 10, others - 7.
	 * 
	 * @param dropoffValue
	 */
	public void setDropoffValueForUngappedExtensions(double dropoffValue) {
		params.put("y", dropoffValue + "");
	}

	/**
	 * Specifies the X dropoff value for final gapped alignment (in bits).
	 * Default is 0. Large dropoff value settings may help generate longer
	 * alignment. Default setting for ungapped alignment X dropoff (in bits):
	 * blastn - 50, megablast - 50, tblastx - 25, all others - 0.
	 * 
	 * @param dropoffValue
	 */
	public void setDropoffValueForFinalGappedAlignment(int dropoffValue) {
		params.put("Z", dropoffValue + "");
	}

	/**
	 * Specifies whether or not to run the rpsblastSearch. Default is false.
	 * Setting this to "T" will perform rpsblast search against CDD database. It
	 * requires an appropriate database input. Refer to
	 * {@link #setDatabase(String)}
	 * 
	 * @see #setDatabase(String)
	 * @param runRpsblastSearch
	 */
	public void setRunRpsblastSearch(boolean runRpsblastSearch) {
		if (runRpsblastSearch)
			params.put("R", "T");
		else
			params.put("R", "T");
	}

	/**
	 * Specifies whether or not to enable megablast. Default is false. Setting
	 * this to true invokes megablast algorithm. {@link #setWordSize(int)} will
	 * default to 28 and queries will be concatenated. This will help speed up
	 * the search at the expense of search sensitivities.
	 * 
	 * @param enableMegablast
	 */
	public void setEnableMegablast(boolean enableMegablast) {
		if (enableMegablast)
			params.put("n", "T");
		else
			params.put("n", "F");
	}

	/**
	 * Specifies the location to use on a query sequence. Example - "100,400",
	 * 100 is the start and 400 the end.
	 * 
	 * @param locationOnQuerySeq
	 */
	public void setLocationOnQuerySeq(String locationOnQuerySeq) {
		params.put("L", locationOnQuerySeq);
	}

	/**
	 * Specifies the window size for multiple hits. Default is 0. Default
	 * Settings for different programs: blastn - 0, megablast - 0, all others -
	 * 40
	 * 
	 * @param windowSize
	 */
	public void setMultipleHitsWindowSize(int windowSize) {
		params.put("A", windowSize + "");
	}

	/**
	 * Specifies the frame shift penalty. Default is 0 (no penalty). Non-zero
	 * setting invokes OOF (Out of Frame) algorithm for blastx.
	 * 
	 * @param frameShiftPenalty
	 */
	public void setFrameShiftPenalty(int frameShiftPenalty) {
		params.put("w", frameShiftPenalty + "");
	}

	/**
	 * Specifies the length of the largest intron allowed in tblastn for linking
	 * HSPs. Default is 0. Zero disables linking. Otherwise the value specified
	 * will be used.
	 * 
	 * @param length
	 */
	public void setLengthOfLargestIntron(int length) {
		params.put("t", length + "");
	}

	/**
	 * Retrieves the clientOptions for this BlastClient. The options are
	 * formatted for the <a href =
	 * "http://www.ncbi.nlm.nih.gov/staff/tao/URLAPI/netblast.html">blastcl3
	 * program</a>.
	 * 
	 * @see #formatClientOptions()
	 * @return clientOptions
	 */
	public String getClientOptions() {
		return clientOptions;
	}

	/**
	 * Formats the clientOptions for this BlastClient according to <a href =
	 * "http://www.ncbi.nlm.nih.gov/staff/tao/URLAPI/netblast.html">blastcl3</a>
	 * 
	 * @see #getClientOptions()
	 */
	public void formatClientOptions() {
		clientOptions = "";
		Iterator<String> iter = params.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			String value = params.get(name);
			clientOptions += " -" + name + " " + value;
		}
	}

	/**
	 * Executes the blast client program using the default parameters found in
	 * {@link #setDefaultOptions()}. For this method to work the latest version
	 * of <a
	 * href="http://www.ncbi.nlm.nih.gov/staff/tao/URLAPI/netblast.html#2">
	 * netblast</a> must be downloaded. Save the folder (netblast-2.2.21) to the
	 * same directory as the executable for SolPicker.  
	 * 
	 * 
	 * @see #setDefaultOptions()
	 * @see #NETBLAST
	 * @see JobFactory#netBlastDir
	 * 
	 */
	protected void netblast() {
		int exitStatus = 0;
		this.setDefaultOptions();
		this.formatClientOptions();
		String netblast = JobFactory.netBlastDir + NETBLAST;
		
		System.out.println("netblast = " + netblast);
		if (netblast == null)
			ErrorReporter.reportError(this, "Could not find netblast executable ");
		String command = netblast + this.clientOptions;
		System.out.println("netblast command: "+command);

		try {
			Process process = Runtime.getRuntime().exec(command);
			exitStatus = process.waitFor();
		} catch (InterruptedException e) {
			ErrorReporter.reportFatalError(this, "netBlast could not be performed because of an interrupted connection.\nCommand : "+command+"\nexit code : "+exitStatus);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ErrorReporter.reportFatalError(this, "netBlast could not be performed because of input/output connection.\nCommand : "+command+"\nexit code : "+exitStatus);
		}
	}

	/**
	 * Sets the default options for this class {@docRoot} . Default settings are
	 * as follows: {@link #database} - {@value #database}, {@link #program} -
	 * {@value #program}, {@link #descriptionsUpperLimit} -
	 * {@value #descriptionsUpperLimit}, {@link #alignmentsUpperLimit} -
	 * {@value #alignmentsUpperLimit}
	 * 
	 * @see #setInputQueryFile(String)
	 * @see #setDatabase(String)
	 * @see #setProgram(String)
	 * @see #setOutputFile(String)
	 * @see #setDescriptionsUpperLimit(int)
	 * @see #setAlignmentsUpperLimit(int)
	 * @see #database
	 * @see #program
	 * @see #descriptionsUpperLimit
	 * @see #alignmentsUpperLimit
	 */
	public void setDefaultOptions() {
		this.setInputQueryFile(this.inputQueryFile);
		this.setProgram(this.program);
		this.setDatabase(this.database);
		this.setOutputFile(this.outputFile);
		this.setDescriptionsUpperLimit(this.descriptionsUpperLimit);
		this.setAlignmentsUpperLimit(this.alignmentsUpperLimit);
	}


	private final String NETBLAST = "/bin/blastcl3";
	/**
	 * contains all the parameters to run the blastcl3 program.
	 */
	private HashMap<String, String> params;
	/**
	 * A String containing all the client options formatted for the blast client
	 * <a href = "http://www.ncbi.nlm.nih.gov/staff/tao/URLAPI/netblast.html">
	 * blastcl3 program</a>
	 * 
	 * @see #formatClientOptions()
	 * @see #getClientOptions()
	 * @see <a href =
	 *      "http://www.ncbi.nlm.nih.gov/staff/tao/URLAPI/netblast.html">
	 *      blastcl3 </a>
	 */
	private String clientOptions;
	/**
	 * Fasta formatted file. 
	 */
	private String inputQueryFile;
	/**
	 * File where output will be directed to when using
	 * {@link #netblast()}.
	 */
	private String outputFile;
	/**
	 * The blast program to be used by the Blast Client. For available options
	 * consult {@link #setProgram(String)}.  Default is {@link #program}.
	 */
	private final String program = "blastn";
	/**
	 * Database to be used by {@link #netblast()}. Default is {@value #database}. For available options see {@link #setDatabase(String)}
	 */
	private final String database = "nr";
	/**
	 * The maximum number of descriptions to show for the blast client output.
	 * Default is {@value #descriptionsUpperLimit} - because
	 * {@link solPicker.filters.BlastFilter#filter(java.util.ArrayList)} filters
	 * for uniqueness, therefore it only checks if there is greater than one
	 * match. For more information see {@link #setDescriptionsUpperLimit(int)}.
	 */
	private final int descriptionsUpperLimit = 2;
	/**
	 * The maximum number of alignments to show for the blast client output.
	 * Default is {@value #alignmentsUpperLimit} The maximum number of descriptions to show for the blast
	 * client output. Default is {@value #alignmentsUpperLimit} - because
	 * {@link solPicker.filters.BlastFilter#filter(java.util.ArrayList)} filters
	 * for uniqueness and alignment length, therefore it only checks if there is
	 * greater than one match and if there is only one what its align length is.
	 * For more information see {@link #setAlignmentsUpperLimit(int)}.
	 */
	private final int alignmentsUpperLimit = 2;
}
