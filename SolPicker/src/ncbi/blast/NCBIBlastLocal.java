package ncbi.blast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ncbi.blast.ClientWrapper.OsUtils;

import solPicker.ErrorReporter;
import solPicker.job.JobFactory;

/**
 * This class runs a local blast using the ncbi genome database.
 * 
 * ** Special Instructions**
 * To use this class follow these steps:
 * 
 * <li>Download the latest ncbi blast executable folder which can be downloaded 
 * <a href="ftp://ftp.ncbi.nih.gov/blast/executables/LATEST/">here</a>.
 * Unzip/unpack this folder and place somewhere on your computer.  
 * It is this folder ("ncbi-blast...") that should be specified as the blast directory using the -l|-local option 
 * ({@link solPicker.CommandLineParser}) and when using the window version of SolPicker.  
 * </li>
 * <li>Download the genome database files which can be found <a href="ftp://ftp.ncbi.nih.gov/blast/executables/blast+/LATEST/">here</a> 
 * The database files must be unpacked and put into the same directory. The directory for the folder containing 
 * the genome database files and blast executable must be specified 
 * using the -db argument (see {@link solPicker.CommandLineParser}) followed by a reference to the alias file (see 3). 
 * </li>
 * <li>Create an alias file for the genome database.  It should specify the names of all the chromosomes.
 *    <a href="ftp://ftp.ncbi.nih.gov/blast/db/README">Read me file</a>. 
 * </li>	
 * @see solPicker.CommandLineParser
 * @author tizatt
 * $Id: NCBIBlastLocal.java,v 1.10 2009/12/15 21:14:05 tizatt Exp $
 */
public class NCBIBlastLocal {

	
	public NCBIBlastLocal(String inputFile, String outputFile,String database) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		this.databaseDir = database;
		params = new HashMap<String,String>();
		setDefaults();
	}

	/**
	 * 
	 * @param filename
	 */
	public void setImportSearchStrategy(String filename){
		params.put("import_search_strategy", filename);
	}
	/**
	 * 
	 * @param filename
	 */
	public void setExportSearchStrategy(String filename){
		params.put("export_search_strategy", filename);
	}
	/**
	 * 
	 * @param taskName
	 */
	public void setTask(String taskName){
		params.put("task", taskName);
	}
	/**
	 * The human genome databases can be downloaded <a href="ftp://ftp.ncbi.nih.gov/blast/executables/blast+/LATEST/">here<\a>.
	 * An alias file must be created as well
	 * @param databaseName
	 */
	public void setDB(String databaseName){
		params.put("db",databaseName);
	}
	/**
	 * 
	 * @param numLetters
	 */
	public void setDBSize(int numLetters){
		params.put("dbsize", ""+numLetters);
	}
	/**
	 * 
	 * @param filename
	 */
	public void setGIList(String filename){
		params.put("gilist", filename);
	}
	/**
	 * 
	 * @param filename
	 */
	public void setNegativeGIList(String filename){
		params.put("negative_gilist", filename);
	}
	/**
	 * 
	 * @param filteringAlgorithm
	 */
	public void setDBSoftMask(String filteringAlgorithm){
		params.put("db_soft_mask", filteringAlgorithm);
	}
	/**
	 * 
	 * @param subjectInputFile
	 */
	public void setSubject(String subjectInputFile){
		params.put("subject", subjectInputFile);
	}
	/**
	 * 
	 * @param range
	 */
	public void setSubjectLoc(String range){
		params.put("subject_loc", range);
	}	
	/**
	 * 
	 * @param inputFile
	 */
	public void setQuery(String inputFile){
		params.put("query", inputFile);
	}
	/**
	 * 
	 * @param outputFile
	 */
	public void setOut(String outputFile){
		params.put("out", outputFile);
	}
	/**
	 * 
	 * @param evalue
	 */
	public void setEValue(String evalue){
		params.put("evalue", evalue);
	}
	/**
	 * 
	 * @param wordSize
	 */
	public void setWordSize(String wordSize){
		params.put("word_size", wordSize);
	}
	/**
	 * 
	 * @param openPenalty
	 */
	public void setGapOpen(String openPenalty){
		params.put("gapopen", openPenalty);
	}
	/**
	 * 
	 * @param extendPenalty
	 */
	public void setGapExtend(String extendPenalty){
		params.put("gapextend", extendPenalty);	
	}
	
	/**
	 * 
	 * @param floatValue
	 */
	public void setPercIdentity(float floatValue){
		params.put("perc_identity", ""+floatValue);
	}
	/**
	 * 
	 * @param floatValue
	 */
	public void setXdropUngap (float floatValue){
		params.put("xdrop_ungap", ""+floatValue);
	}
	
	/**
	 * 
	 * @param floatValue
	 */
	public void setXdropGap (float floatValue){
		params.put("xdrop_gap", ""+floatValue);
	}
	
	/**
	 * 
	 * @param floatValue
	 */
	public void setXdropGapFinal(float floatValue){
		params.put("xdrop_gap_final", ""+floatValue);
	}
	
	/**
	 * 
	 * @param intValue
	 */
	public void setSearchsp(int intValue){
		params.put("searchsp", ""+intValue);
	}

	/**
	 * 
	 * @param penalty
	 */
	public void setPenalty(String penalty){
		params.put("penalty", penalty);
	}
	
	/**
	 * 
	 * @param reward
	 */
	public void setReward(String reward){
		params.put("reward", reward);
	}
	
	/**
	 * 
	 */
	public void setNoGreedy(){
		params.put("no_greedy", "");
	}
	
	/**
	 * 
	 * @param intValue
	 */
	public void setMinRawGappedScore(int intValue){
		params.put("min_raw_gapped_score", ""+intValue);
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setTemplateType(String type){
		params.put("template_type", type);
	}
	
	/**
	 * 
	 * @param intValue
	 */
	public void setTemplateLength(int intValue){
		params.put("template_length", ""+intValue);
	}
	
	/**
	 * 
	 * @param dustOptions
	 */
	public void setDust (String dustOptions){
		params.put("dust", dustOptions);
	}
	

	/**
	 * 
	 * @param filteringDatabase
	 */
	public void setFilteringDB(String filteringDatabase){
		params.put("filtering_db", filteringDatabase);
	}
	
	/**
	 * 
	 * @param windowMaskerTaxid
	 */
	public void setWindowMaskerTaxid(String windowMaskerTaxid){
		params.put("window_masker_taxid", windowMaskerTaxid);
	}
	
	/**
	 * 
	 * @param windowMaskerDB
	 */
	public void setWindowMaskerDB(String windowMaskerDB){
		params.put("window_masker_db", windowMaskerDB);
	}
	
	/**
	 * 
	 * @param softMasking
	 */
	public void setSoftMasking(String softMasking){
		params.put("soft_masking", softMasking);
	}
	/**
	 * 
	 */
	public void setUngapped(){
		params.put("ungapped", "");
	}
	
	/**
	 * 
	 * @param intValue
	 */
	public void setCullingLimit(int intValue){
		params.put("culling_limit", ""+intValue);
	}
	/**
	 * 
	 * @param floatValue
	 */
	public void setBestHitOverhang(float floatValue){
		params.put("best_hit_overhang", ""+floatValue);
	}
	/**
	 * 
	 * @param floatValue
	 */
	public void setBestHitScoreEdge(float floatValue){
		params.put("best_hit_score_edge", ""+floatValue);
	}
	/**
	 * 
	 * @param intValue
	 */
	public void setWindowSize(int intValue){
		params.put("window_size", ""+intValue);
	}
	/**
	 * 
	 * @param booleanValue
	 */
	public void setUseIndex(String booleanValue){
		params.put("use_index", booleanValue);
	}
	/**
	 * 
	 * @param string
	 */
	public void setIndexName(String string){
		params.put("index_name", string);
	}
	/**
	 * 
	 */
	public void setLcaseMasking(){
		params.put("lcase_masking", "");
	}
	/**
	 * 
	 * @param range
	 */
	public void setQueryLoc(String range){
		params.put("query_loc", range);
	}
	/**
	 * 
	 * @param strand
	 */
	public void setStrand(String strand){
		params.put("strand", strand);
	}
	/**
	 * 
	 */
	public void setParseDeflines(){
		params.put("parse_deflines","");
	}
	/**
	 * 
	 * @param format
	 */
	public void setOutfmt(String format){
		params.put("outfmt", format);
	}
	/**
	 * 
	 */
	public void setShowGis(){
		params.put("show_gis", "");
	}
	
	/**
	 * 
	 * @param intValue
	 */
	public void setNumDescriptions(int intValue){
		params.put("num_descriptions", intValue+"");
	}
	
	/**
	 * 
	 * @param intValue
	 */
	public void setNumAlignments(int intValue){
		params.put("set_num_alignments", intValue+"");
	}
	
	/**
	 * 
	 */
	public void setHTML(){
		params.put("html", "");
	}
	
	/**
	 * 
	 * @param numSequences
	 */
	public void setMaxTargetSeqs(String numSequences){
		params.put("max_target_seqs", numSequences);
	}
	
	/**
	 * 
	 * @param intValue
	 */
	public void setNumThreads(int intValue){
		params.put("num_threads", intValue+"");
	}
	/**
	 * 
	 */
	public void setRemote(){
		params.put("remote", "");
	}
	
	/**
	 * 
	 */
	public void setDefaults() {
		this.setOut(outputFile);
		this.setDB(databaseDir+"/bin/genome");
		this.setQuery(inputFile);
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void blast(){
		
		int exitStatus = 0;
		String command = "";
		String localBlastDir = JobFactory.localBlastDir.getPath();
		String blastExec = getExecutable();
		localBlastDir = localBlastDir+blastExec;
		if (localBlastDir == null)
			ErrorReporter.reportError(this, "No local blast executable was provided.");
		
		if(params != null){
			Iterator it = params.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry commands = (Map.Entry)it.next();
		        command += " -"+commands.getKey() + " " + commands.getValue();
		    }
		}
		
		command = localBlastDir + command;
		System.out.println("Local Blast command: "+command);
		try {
			Process process = Runtime.getRuntime().exec(command);
			exitStatus = process.waitFor();
		} catch (InterruptedException e) {
			ErrorReporter.reportFatalError(this, "localBlast could not be performed because of an interrupted connection.\nCommand : "+command+"\nexit code : "+exitStatus);
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			ErrorReporter.reportFatalError(this, "localBlast could not be performed because of IOException.\nCommand : "+command+"\nexit code : "+exitStatus);
		}
	}
	
	private String getExecutable() {
		String exec = "/bin/blastn";
		if(OsUtils.isMac() || OsUtils.isUnix())
			exec = "/bin/blastn";
		else if (OsUtils.isWindows())
			exec = "\bin\blastn";
		return exec;
	}

	private HashMap<String,String> params;
	private String inputFile;
	private String outputFile;
	private String databaseDir;
}
