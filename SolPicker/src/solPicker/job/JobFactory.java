package solPicker.job;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


import solPicker.ErrorReporter;
import solPicker.XMLParser;

/**
 * A Job Factory is used to run SolPicker and complete all the Jobs assigned by the user.  The class takes an XML file as a paremeter.  It validates the XML file against the schema, parses the file,
 * calls on the newly createdJob objects to produce the appropriate fastas, and prints all the different multi-fasta sequences to different output files.
 * @author Tyler Izatt
 *
 */
public class JobFactory 
{
	public JobFactory(File f)
	{
		xml = f;
	}

	/**
	 * 
	 */
	public void completeJobs()
	{
		try
		{
			XMLParser parser = new XMLParser(xml);
			parser.parse();
			ArrayList<Job> jobs = parser.getJobs();
			
			if(!outDir.exists())
				ErrorReporter.reportError(this, "Invalid Directory for output: "+outDir.getPath());
			int version = 1;
			File outDirectory = new File(outDir,getDate());
			while(outDirectory.exists()){
				outDirectory = new File(outDir,getDate()+"_"+version++);
			}
			outDirectory.mkdir();
			
			ArrayList<File> files = new ArrayList<File>();
			files.add(new File(outDirectory, "Query_Sequences"));
			files.add(new File(outDirectory, "All_Oligos"));
			files.add(new File(outDirectory, "Rejected_Oligos"));
			files.add(new File(outDirectory, "Valid_Oligos"));
			
			File xmlCopy = xml;
			xmlCopy.renameTo(new File(outDirectory, "XML"));
			
			ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();
			for(File f : files){
				writers.add(new PrintWriter(f));
			}
			
			for(Job j : jobs)
			{
				j.performJob(writers);
			}
			
			for(PrintWriter pw : writers){
				pw.close();
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	private String getDate(){
		final String DATE = "yyyy-MM-dd";
		 Calendar cal = Calendar.getInstance();
		 SimpleDateFormat sdf = new SimpleDateFormat(DATE);
		 
		 return sdf.format(cal.getTime());
	}
	
	public static void setOutDirectory(File dir) {
		outDir = dir;
	}
	
	
	public static void setNetBlastDirectory(File netblast) {
		netBlastDir = netblast;
	}
	
	public static void setLocalBlastDirectory(File localblastDir) {
		localBlastDir = localblastDir;
	}
	
	public static void setBlastType(String blast){
		blastType = blast;
	}
	
	public static void setDatabaseDirectory(File database){
		databaseDir = database;
	}
	
	public static File outDir;
	public static File netBlastDir;
	public static File localBlastDir;
	public static String blastType;
	public static File databaseDir;
	private File xml;
}
