package ncbi.blast;


import java.util.HashMap;
import java.util.Iterator;

/**
 * @deprecated
 * @author Tyler Izatt
 * @version $Id: NCBIBlastGetCommand.java,v 1.2 2009/08/19 21:54:50 tizatt Exp $
 */
public class NCBIBlastGetCommand {
	

	public NCBIBlastGetCommand(String requestId) {
		this.requestId = requestId;
		parameters = new HashMap<String, String>();
		this.setDefaults();
		this.createGetCommand();
	}
	
	private void setRID(String requestId2){
		parameters.put("RID", requestId2);
	}
	
	private void setOverview(String overview2) {
		parameters.put("SHOW_OVERVIEW", overview2);
		
	}



	private void setAlignmentType(String alignmentType2) {
		parameters.put("ALIGNMENT_TYPE",alignmentType2);
		
	}



	private void setAlignments(String alignments2) {
		parameters.put("ALIGNMENTS",alignments2);
		
	}



	private void setDescriptions(String descriptions2) {
		parameters.put("DESCRIPTIONS",descriptions2);
		
	}



	private void setFormatType(String formatType2) {
		parameters.put("FORMAT_TYPE",formatType2);
		
	}



	private void setFormatObject(String formatObject2) {
		parameters.put("FORMAT_OBJECT",formatObject2);
	}



	private void setCMD(String cmd2) {
		parameters.put("CMD",cmd2);
	}

	/**
	 * Sets the defaults for the request
	 */
	protected void setDefaults(){
		this.setRID(requestId);
		this.setCMD(cmd);
		this.setFormatObject(formatObject);
		this.setFormatType(formatType);
		this.setDescriptions(descriptions);
		this.setAlignments(alignments);
		this.setAlignmentType(alignmentType);
		this.setOverview(overview);
	}

	
	/**
	 * 
	 */
	protected void createGetCommand(){
		getCommand = REQUEST;
		Iterator<String> iter = parameters.keySet().iterator();
		while(iter.hasNext()){
			String name = iter.next();
			String value = parameters.get(name);
			getCommand += name + "=" + value+"&";
		}
	}
	
	
	/**
	 * 
	 * @return the get command for this object
	 */
	public String getCommand(){
		return getCommand;
	}

	protected String cmd = "get";
	protected String formatObject = "Alignment";
	protected String formatType = "HTML";
	protected String descriptions = "5";
	protected String alignments = "100";
	protected String alignmentType = "Pairwise";
	protected String overview = "yes";
	protected String requestId ="";
	protected HashMap<String,String> parameters;
	protected String getCommand;
	private final String REQUEST = "http://www.ncbi.nlm.nih.gov/wswu.blast/Blast.cgi?";
}
