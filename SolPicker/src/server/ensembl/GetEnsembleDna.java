package server.ensembl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import server.Domain;
import solPicker.ErrorReporter;
import solPicker.job.Query;
import solPicker.job.Range;


/**
 * 
 * 
 * This class accesses the Ensembl Genome Browser to retrieve DNA sequences from
 * specified chromosome segments of the genome of the organism.
 * 
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: GetEnsembleDna.java,v 1.3 2009/09/14 21:17:42 tizatt Exp $
 */
public class GetEnsembleDna extends Domain {
	/**
	 * This class constructs an Ensembl Instance of Domain that will retrieve a
	 * sequence from the Ensembl Genome Browser
	 * 
	 * @param chr
	 *            - chromosome number
	 * @param loc1
	 *            - start position
	 * @param loc2
	 *            - end position
	 * @param organism
	 *            - target organism
	 */
	public GetEnsembleDna(String chr, String loc1, String loc2, String organism) {
		super(chr, loc1, loc2, organism);
	}

	public GetEnsembleDna(Query q) {
		super(q);
	}

	public GetEnsembleDna(Range r) {
		super(r);
	}

	/**
	 * Uses the instance fields to construct the url of the website containing
	 * the correct sequence from Ensembl's database
	 */
	public void createURL(){
		if (!isHumanChromosomeValid()) {
			ErrorReporter.reportFatalError(this,"Chromosome is invalid : "+chr);
		}

		String header = "http://www.ensembl.org/" + organism
				+ "/Location/Export?output=fasta;r=" + chr + ":" + first + "-"
				+ second + ";strand=1;time=" + System.currentTimeMillis()
				+ ";genomic=soft_masked;_format=Text";
		// http://www.ensembl.org/Homo_sapiens/Location/Export?output=fasta;r=X:16500-18547;strand=1;time=1247608835.80448;genomic=unmasked;_format=Text
		setURL(header);
		try {
			finalUrl = new URL(header);
		} catch (MalformedURLException e) {
			ErrorReporter.reportFatalError(this,"Ensembl URL could not be formed for retrieving sequence : "+header);
		}
		// int length = header.length();
		// return fasta.substring(length + 1);
	}

	/**
	 * Gets the current time mill in the system to four decimal places. This is
	 * necessary because the the Url for the fasta sequence Ensembl produces
	 * includes the current time mills
	 * 
	 * @return current time mills to four decimal places
	 */

	/*
	 * private String getTime() { String timeMills = System.currentTimeMillis()
	 * + ""; int length = timeMills.length(); int i = length - 4; return
	 * timeMills.substring(0, i) + "." + timeMills.substring(i); }
	 */

	/**
	 * Removes the html header from the ensemble fasta file so that only the
	 * nucleotide sequence remains
	 * 
	 * @param fasta
	 *            - the fasta string initially returned by the GET request
	 */
	public String cutHeader(String fasta) {
		String first = getFirst();
		String second = getSecond();
		String cNum = getChromosome();
		String header = ">" + cNum + "dna:chromosome chromosome:NCBI36:" + cNum
				+ ":" + first + ":" + second + ":1";
		int length = header.length();
		return fasta.substring(length + 1);
	}

	/**
	 * returns the name of the domain
	 * 
	 * @return the name of the domain
	 */
	public String getDomain() {
		return "server.ensembl";
	}

	public String getDnaFor() {
		String result = "";
		result.trim();
		createURL();
		// open the url connection
		URLConnection conn;
		try {
			conn = finalUrl.openConnection();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
			result = sb.toString();
			} catch (IOException e){
				ErrorReporter.reportFatalError(this,"Could not retrieve Ensembl DNA sequence ("+finalUrl.toString()+")");
			}
		
		return (cutHeader(result));
	}
}
