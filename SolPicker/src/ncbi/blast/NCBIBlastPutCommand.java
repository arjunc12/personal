package ncbi.blast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 * This class specifies the parameters for a wswu.blast "put" command directed using
 * the BLAST API. The only required parameter is a nucleotide or protein
 * sequence.
 * @deprecated
 * @author Tyler Izatt
 * @version $Id: NCBIBlastPutCommand.java,v 1.3 2009/12/15 21:14:05 tizatt Exp $
 */

public class NCBIBlastPutCommand {

	public NCBIBlastPutCommand(String query) {
		parameters = new HashMap<String, String>();
		this.querySeq = query;
		this.setDefaults();
		this.createPutCommand();
		
	}


	public void setQuery(String query) {
		parameters.put("QUERY", query);
	}

	public void setCmd(String cmd) {
		parameters.put("CMD", cmd);
	}

	public void setDatabase(String database) {
		parameters.put("DATABASE", database);
	}

	public void setProgram(String program) {
		parameters.put("PROGRAM", program);
	}

	public void setFilter(String filter) {
		parameters.put("FILTER", filter);
	}

	public void setHitListSize(String size) {
		parameters.put("HITLIST_SIZE", size);
	}

	public void setMegaBlast(String megablast) {
		parameters.put("MEGABLAST", megablast);
	}

	public void setService(String service) {
		parameters.put("SERVICE", service);
	}

	public void setExpect(String expect) {
		parameters.put("EXPECT", expect);
	}

	public void setFormatType(String formatType) {
		parameters.put("FORMAT_TYPE", formatType);
	}

	private void setPage(String page) {
		parameters.put("PAGE", page);
	}

	private void setClient(String client) {
		parameters.put("CLIENT", client);
	}

	private void setNewView(String newView) {
		parameters.put("NEW_VIEW", newView);
	}

	protected void setDefaults() {
		this.setCmd(cmd);
		this.setQuery(querySeq);
		//this.setQueryFileName(querySeq);
		this.setClient(client);
		this.setDatabase(database);
		this.setExpect(expect);
		this.setFilter(filter);
		this.setFormatType(formatType);
		this.setHitListSize(hitListSize);
		this.setMegaBlast(megablast);
		this.setPage(page);
		this.setProgram(program);
		this.setCmd(cmd);
		this.setNewView(newView);
	}

	protected void createPutCommand() {
		putCommand = "";
		Iterator<String> iter = parameters.keySet().iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			String value = parameters.get(name);
			putCommand += "&" + name + "=" + value;
		}
	}

	/**
	 * Sends a put request
	 * @return requestId
	 */
	public String sendRequest(){
		URL url;
		int rtoe;
		try {
			URLConnection urlConn = null;
			url = new URL(REQUEST+putCommand);
			urlConn = url.openConnection();
			urlConn.setDoOutput(true);
			
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(urlConn
					.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				
				if (line.indexOf("RID =") > -1) {
					String[] ridArr = line.split("=");
					requestId = ridArr[1].trim();
				} else if (line.indexOf("RTOE =") > -1) {
					String[] rtoeArr = line.split("=");
					rtoe = Integer.parseInt(rtoeArr[1].trim());
					wait(rtoe);
				}
			}
			rd.close();
			
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return requestId;
	}

	/**
	 * 
	 * @return command
	 */
	public String getPutCommand() {
		return putCommand;
	}

	/**
	 * 
	 * @param n
	 */
	public void wait(int n) {

		long w, w1;
		w = System.currentTimeMillis();
		do {
			w1 = System.currentTimeMillis();
		} while ((w1 - w) < (n * 1000));
	}

	//protected String querySeq = "%2FUsers%2Ftizatt%2FDocuments%2Fworkspace%2FSolPicker%2Fbin%2FblastTest%2Ftest1.txt";
	protected String querySeq = "";
	protected String newView = "true";
	protected String cmd = "Put";
	protected String database = "nr";
	protected String program = "blastn";
	protected String filter = "L";
	protected String hitListSize = "2";
	protected String service = "megablast";
	protected String formatType = "HTML";
	protected String client = "web";
	protected String page = "Nucleotides";
	protected String expect = "10";
	protected String megablast = "true";
	protected HashMap<String, String> parameters;
	protected String putCommand;
	protected String requestId;
	public final String REQUEST = "http://www.ncbi.nlm.nih.gov/wswu.blast/Blast.cgi?";

}
