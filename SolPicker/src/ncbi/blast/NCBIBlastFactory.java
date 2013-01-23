package ncbi.blast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import solPicker.ErrorReporter;
import solPicker.Log;
import solPicker.job.GetRequest;
import solPicker.job.JobFactory;
import solPicker.job.Oligo;

/**
 * This is the factory class for using the NCBI Blast algorithm. The type of blast to perform (i.e. local or remote)
 * can be specified by the user.  It is advisable to use the local blast for most cases since it is quicker and can handle
 * longer sequences better than the remote blast.  
 * <li>Instructions on using the local blast can be found in {@link NCBIBlastLocal}.</li>
 * <li>Instructions on using the remote blast can be found in {@link NCBIBlastClient}</li>
 * 
 * The results are processed by searching for alignment lengths
 * between target & query(Oligo) sequences, and by retrieving a uniqueness value
 * for each query sequence. Based off these values an oligo sequence either
 * passes or fails. The alignment length with no gaps required is {@link this#ALIGN_LENGTH}. An Oligo
 * must be unique (i.e. have no more than 1 hit).
 * 
 * @author Tyler Izatt
 * @version $Id: NCBIBlastFactory.java,v 1.23 2009/12/15 21:14:05 tizatt Exp $
 * 
 */
public class NCBIBlastFactory {

	/**
	 * Constructor. The directory used is provided by the user. The directory
	 * must be the same directory where netblast is stored!
	 * 
	 * @param oligos
	 */
	public NCBIBlastFactory(ArrayList<Oligo> oligos) {
		this.oligos = oligos;
	}

	/**
	 * Calls a blast type depending on {@link solPicker.job.JobFactory#blastType}.
	 * @return oligos - a list containing the oligos that have been tagged for
	 *         filtering.
	 */
	public ArrayList<Oligo> blast() {
		boolean keepBlasting = true;
		int start = 0;
		int end = BATCH_SIZE;
		
		while (keepBlasting){
			if(oligos.size()<= end){
				keepBlasting = false;
				end = oligos.size();
			}
			writeOligosToFasta(start, end);
			outputFile = new File(JobFactory.outDir,outputFileName);
			
			System.out.println("blast write permission: "+outputFile.canWrite());
			if(!outputFile.exists()){
				try {
					outputFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ErrorReporter.reportWarning(this, "can't create blast_result.blast, attempting a recreate");
			}
			System.out.println("Blast file : "+outputFile.getPath());
			try {
				Process process = Runtime.getRuntime().exec("chmod 777 "+outputFile.getPath());
				int wait = process.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(JobFactory.blastType.equals("remote")){
				NCBIBlastClient client = new NCBIBlastClient(inputFile.getPath(), outputFile.getPath());
				client.netblast();
			}
			else if(JobFactory.blastType.equals("local")){
				NCBIBlastLocal localBlast= new NCBIBlastLocal(inputFile.getPath(), outputFile.getPath(), JobFactory.databaseDir.getPath());
				localBlast.blast();
			}
			else{
				ErrorReporter.reportFatalError(this, "A blastType was not specified in JobFactory, therefore the BLAST algorithm cannot be run.");
			}
			String result = readFile(outputFile);
			parseBlast(result,start,end);
			
			end = end+BATCH_SIZE;
			start = start+BATCH_SIZE;
		}
		
		return oligos;
	}
	
	

	/**
	 * Writes the valid Oligo objects to a text file. This text file is used by
	 * {@link NCBIBlastClient#netblast()} to blast all the oligos against the
	 * NCBI webserver.
	 */
	protected void writeOligosToFasta(int startIndex, int endIndex) {
		inputFile = new File(JobFactory.outDir,inputFileName);

		try {
			PrintWriter writer = new PrintWriter(inputFile);

			for (int i = startIndex; i < endIndex; i++) {
				writer.write(">" + oligos.get(i).getID() + "\n");
				writer.write(oligos.get(i).getSequence() + "\n");
			}
		writer.close();

		} catch (FileNotFoundException e) {
			ErrorReporter.reportError(this, "Could not find file ("+inputFile.getAbsolutePath()+") for writing oligos to fasta");
		}
	}
	
	
	

	/**
	 * Reads a file into a string.
	 * @param file
	 * @return result - a string containing the file contents
	 */
	private String readFile(File file) {
		String result = "";
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				result += scanner.nextLine() + "\n";
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			ErrorReporter.reportFatalError(this, "Could not find file : "+file.getPath());
		} 
		return result;
	}

	
	
	/**
	 * Parses a String representing the NCBI blast output. Splits the String
	 * into separate strings by locating each query, and then cycles through
	 * each query looking for the number of hits present and the alignment
	 * length. This algorithm uses pattern matching to determine the align
	 * length for an oligo query with a target query by counting the number of
	 * pipe symbols ("|") in a row, each of which represent a single alignment
	 * between nucleotides. Each oligo that does not meet the filter
	 * requirements is "rejected" and the entire list of oligos is returned.
	 * 
	 * @param result
	 *            - output from the NCBI Blast server.
	 */
	private void parseBlast(String result, int start, int end) {
		boolean unique, align;
		String[] results = result.split("Query= ");
		//Log.logMessage("parsing the blast output");
		
		for (int i = 1; i < results.length; i++) {
			//String[] query = results[i].split("\\t|\\r|\\n");
			//int oligoNum = Integer.parseInt(query[0].trim());
			String[] identities = results[i].split("Identities");
			if (identities.length > 2)
				unique = false;
			else
				unique = true;
			
			if(results[i].indexOf(getAlign()) >= 0)
				align = true;
			else
				align = false;

			if(start<=end){
				oligos.get(start).blasted();
				if(!(unique && align))
					oligos.get(start).reject("BLAST");
			}
			else
				ErrorReporter.reportFatalError(this, "The number of oligos returned from the Blast was greater than the number before they were blasted.");
			start++;
		}
	}
	
	
	private String getAlign(){
		String align = "";
		for(int i = 0; i<ALIGN_LENGTH; i++){
			align+="|";
		}
		return align;
	}

	
	/**
	 * Sends a batch of queries to the blast server by using the
	 * NCBIBlastPutCommand and NCBIBlastGetCommand classes
	 * 
	 * @deprecated
	 * @param queryBatch
	 *            - holds a multi fasta batch file.
	 * @return result - holds the results from the Blast server.
	 */
	private String sendRequest(String queryBatch) {
		NCBIBlastPutCommand bpc = new NCBIBlastPutCommand(queryBatch);
		requestId = bpc.sendRequest();
		NCBIBlastGetCommand nbr = new NCBIBlastGetCommand(requestId);
		System.out.println(nbr.getCommand());

		GetRequest gr = new GetRequest(nbr.getCommand());
		gr.sendGetRequest();
		String result = gr.getResult().trim();
		/*
		 * FileWriter fstream; try { fstream = new FileWriter(outFile, true);
		 * BufferedWriter out = new BufferedWriter(fstream); out.write(result);
		 * out.close(); } catch (IOException e) { e.printStackTrace(); }
		 */
		return result;
	}

	/**
	 * Combines query strings into batches to minimize the number of wswu.blast
	 * requests to the server. Every batch is equals in size and is determined
	 * by the limits of the wswu.blast server.
	 * 
	 * @deprecated
	 * @return - a list containing "batches" of query sequences.
	 */
	protected ArrayList<String> batchQueries() {
		ArrayList<String> queryBatches = new ArrayList<String>();
		int size = oligos.size() / BATCH_SIZE;
		int mod = oligos.size() % BATCH_SIZE;
		for (int i = 0; i < size; i++) {
			queryBatches.add(addBatch(i * BATCH_SIZE, BATCH_SIZE));
		}

		if (mod > 0) {
			queryBatches.add(addBatch(oligos.size() - mod, oligos.size()));
		}

		return queryBatches;
	}

	/**
	 * Creates a batch of oligos from a start and end index. Each Oligo object
	 * is identified by its location in validOligos; this location is stored in
	 * the Fasta header for the sequence. The ordering that each sequence is
	 * added is extremely important because it determines later which Oligos are
	 * filtered out by the BlastFilter.
	 * 
	 * @deprecated
	 * @param start
	 *            - index that represents the location to start this batch of
	 *            oligos of the oligo list.
	 * @param end
	 * @return batch - a batch of multifasta sequences.
	 */
	protected String addBatch(int start, int end) {
		String batch = "";
		for (int i = start; i < end; i++) {
			batch += "%3E" + i + "%0D%0A" + oligos.get(i).getSequence()
					+ "%0D%0A";
		}
		return batch;
	}
	
	public int getAlignLength(){
		return ALIGN_LENGTH;
	}
	/**
	 * The base url from which ncbi.blast queries are constructed.
	 * 
	 * @deprecated
	 */
	public final String REQUEST = "http://www.ncbi.nlm.nih.gov/wswu.blast/Blast.cgi?";
	/**
	 * 
	 */
	private String inputFileName = "pre_blast_oligos";
	/**
	 * 
	 */
	private String outputFileName = "blast_result.blast";
	/**
	 * 
	 */
	private File inputFile;
	/**
	 * 
	 */
	private File outputFile;
	/**
	 * 
	 */
	private String requestId = "";
	/**
	 * 
	 */
	private final int ALIGN_LENGTH = 20;
	/**
	 * 
	 */
	protected ArrayList<Oligo> oligos;
	/**
	 * 
	 */
	private final int BATCH_SIZE = 1000;

}
