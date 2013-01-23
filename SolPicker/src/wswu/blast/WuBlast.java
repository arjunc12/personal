package wswu.blast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import java.util.Scanner;

import javax.xml.rpc.ServiceException;

import uk.ac.ebi.webservices.FileUtil;
import uk.ac.ebi.webservices.WSWUBlastClient;
import uk.ac.ebi.webservices.wswublast.Data;
import uk.ac.ebi.webservices.wswublast.InputParams;
import uk.ac.ebi.webservices.wswublast.WSFile;
import uk.ac.ebi.webservices.wswublast.WSWUBlastService;
import uk.ac.ebi.webservices.wswublast.WSWUBlastServiceLocator;
import uk.ac.ebi.webservices.wswublast.WSWUBlast_PortType;



public class WuBlast 
{
	private WSWUBlast_PortType srvProxy = null;
	
	@SuppressWarnings("unused")
	private WSWUBlastClient wclient;
	private InputParams inputParams;
	

	
	public WuBlast(){
		inputParams = new InputParams();
	}
	
	/**
	 * Runs the WuBlast program.
	 */
	public void blast(){
		try {
			srvProxyConnect();
			wclient = new WSWUBlastClient();
			Data[] sequences = null;
			sequences = readSequences();
			System.out.println();
			String jobId = srvProxy.runWUBlast(inputParams, sequences);
			System.out.println("Server id: "+jobId);
			System.out.println(srvProxy.checkStatus(jobId));
			WSFile[] wsFiles = srvProxy.getResults(jobId);
			for(int i = 0; i< wsFiles.length;i++){
				WSFile file = wsFiles[i];
				byte[] b = this.srvProxy.poll(jobId,file.getType());
				String value = new String(b);
				String filename = "wu" +i+ "." + file.getExt();
				FileUtil.writeFile(new File(filename), value);
				System.out.println(value);
			}
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * The wswu.blast program to use.  Options include BLASTP, BLASTN, BLASTX, TBLASTN, TBLASTX
	 * @see <a href="http://www.ebi.ac.uk/Tools/blast2/help.html#program">WuBlast program list<a>
	 * @param program - a string representing the name of the wswu.blast program
	 */
	public void setProgram(String program){
		inputParams.setProgram(program);
	}
	
	/**
	 * 
	 * @param database
	 */
	public void setDatabase(String database){
		inputParams.setDatabase(database);
	}
	
	public void setSort(String sort){
		inputParams.setSort(sort);
	}
	
	public void setFilter(String filter){
		inputParams.setFilter(filter);
	}
	
	
	public void setMatrix(String matrix){
		inputParams.setMatrix(matrix);
	}
	
	
	public void setNumal(int numal){
		inputParams.setNumal(numal);
	}
	
	
	public void setScores(int scores){
		inputParams.setScores(scores);
	}
	
	
	public void setTopcombon(int topcombon){
		inputParams.setTopcombon(topcombon);
	}
	
	
	public void setExp(float exp){
		inputParams.setExp(exp);
	}
	
	
	
	public void setEchofilter(boolean echofilter){
		inputParams.setEchofilter(echofilter);
	}
	
	
	public void setStats(String stats){
		inputParams.setStats(stats);
	}
	
	
	public void setStrand(String strand){
		inputParams.setStrand(strand);
	}
	
	
	public void setSensitivity(String sensitivity){
		inputParams.setSensitivity(sensitivity);
	}
	
	
	public void setAppxml(String appxml){
		inputParams.setAppxml(appxml);
	}
	
	
	public void setAsync(boolean async){
		inputParams.setAsync(async);
	}
	
	
	public void setEmail(String email){
		inputParams.setEmail(email);
	}
	
	
	public void setDefaultInputParameters(){
		this.setProgram("BLASTn");
		this.setDatabase("em_rel");
		//this.setMatrix("Identity");
		//this.setNumal(2);
		//this.setScores(2);
		this.setAsync(false);
		this.setStrand("both");
		this.setEmail("tizatt@tgen.org");
	}
	
	
	
	/** Get an instance of the service proxy to use with other methods.
	 * 
	 * @throws javax.xml.rpc.ServiceException
	 */
	private void srvProxyConnect() throws javax.xml.rpc.ServiceException {
		System.out.println("Connecting to server proxy");
		
		if(this.srvProxy == null) {
			WSWUBlastService service =  new WSWUBlastServiceLocator();
			this.srvProxy = service.getWSWUBlast();
		}
	}
	
	/**
	 * Reads the fasta sequences from a file.
	 * @return - data structure containing settings for the Blast search
	 */
	private Data[] readSequences(){
		int counter = 0;
		Data[] sequences = new Data[numOfSequences];
		 try {
			 Scanner scanner = new Scanner(new File(sequenceInputFile));
		     scanner.useDelimiter(System.getProperty("line.separator")); 
		     String header = "";
		     String sequence = "";
		     
		     while (scanner.hasNext()) {
		    	 String line = scanner.next();
		    	 System.out.println("line: "+line);
		    	 if(line.startsWith(">") && header.equals("")){
		    		 header = line;
		    		 System.out.println("header: "+line);
		    	 }
		    	 else if(line.startsWith(">")){
		    		 System.out.println("adding sequence: "+header);
		    		 header = header.trim();
		    		 Data data = new Data();
		    		 data.setType("sequence");
		    		 System.out.println("header: "+header+"     sequence: "+sequence);
		    		 data.setContent(header+"\n"+sequence+"\n");
		    		 sequences[counter++] = data;
		    		 header = line;
		    	 }
		    	 else{
		    		 System.out.println("sequence: "+sequence);
		    		 sequence += line.trim();
		    	 }
		     }
		     scanner.close();
		 } catch (FileNotFoundException e) {
		     e.printStackTrace();
		 }
		return sequences;
	}
	
	
	
	
	
	private final String sequenceInputFile = "bin/blastTest/test1.fasta";
	private int numOfSequences = 1000;

}
