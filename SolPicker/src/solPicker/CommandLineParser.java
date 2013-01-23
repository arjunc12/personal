package solPicker;

import java.io.File;

import solPicker.job.JobFactory;

/**
 * The command line parser for SolPicker. Available options are - {@link #databaseDir},
 * {@link #out},{@link #help},{@link #inFile},{@link #localDir},{@link #netDir}. 
 * 
 * The required options are {@link #out}, {@link #inFile}, and a blast directory.  If you use
 * the local blast {@link #localDir} you must also have the genome database downloaded onto your
 * computer. Instructions can be found here: {@link ncbi.blast.NCBIBlastLocal#setDB(String)}.  
 *  
 * @author tizatt
 *
 */
public class CommandLineParser {
	
	public CommandLineParser(){
	}
	
	
	/**
	 * 
	 * @param args
	 */
	public void parse(String[] args){
		File inputFile = null;
		File outputDir = null;
		File netblastDir = null;
		File localblastDir = null;
		File databaseLoc = null;
		boolean help = false;
		for(int i = 0; i<args.length; i++){
			if(args[i].equals("-i") || args[i].equals("--input")){
				inputFile = new File(args[++i]);
			}
			else if(args[i].equals("-o") || args[i].equals("--output")){
				outputDir = new File(args[++i]);
			}
			else if(args[i].equals("-n") || args[i].equals("--netblast")){
				netblastDir = new File(args[++i]);
			}
			else if(args[i].equals("-h") || args[i].equals("--help")){
				help = true;
				i++;
			}
			else if(args[i].equals("-l") || args[i].equals("--local_blast")){
				localblastDir = new File(args[++i]);
			}
			else if(args[i].equals("-d") || args[i].equals("--database")){
				databaseLoc = new File(args[++i]);
			}
		}
		
		JobFactory jf = null;
		if(help){
			System.out.println(help());
			ErrorReporter.reportFatalError(this, "");
		}
		else if(inputFile != null){
			if(inputFile.exists()){
				jf = new JobFactory(inputFile);
				if(outputDir != null){
					if(outputDir.exists()){
						JobFactory.setOutDirectory(outputDir);
						if(netblastDir != null && localblastDir != null){
							ErrorReporter.reportFatalError(this, "Using local blast and remote blast in same run is not permitted.  Please specify only one of -n or -l");
						}
						else if(netblastDir != null && netblastDir.exists()){
							JobFactory.setNetBlastDirectory(netblastDir);
							JobFactory.setBlastType("remote");
						}

						else if(localblastDir != null && localblastDir.exists() && databaseLoc != null && databaseLoc.exists()){
							JobFactory.setLocalBlastDirectory(localblastDir);
							JobFactory.setDatabaseDirectory(databaseLoc);
							JobFactory.setBlastType("local");
		}
						else{
							ErrorReporter.reportFatalError(this, "One of -l|--local_blast or -n|--netblast must be provided.  The Blast filter is required to run SolPicker.\n"+help());
						}
					}
					else
						ErrorReporter.reportFatalError(this, "The output directory was not found, check to make sure the path provided is correct: "+outputDir.getPath());
				}
				else
					ErrorReporter.reportFatalError(this,"The -o|--output command must be specified to run SolPicker.\n"+help);
			}
			else
				ErrorReporter.reportFatalError(this, "The input file was not found, check to make sure the path provided is correct: "+inputFile.getPath());
		}
		else
			ErrorReporter.reportFatalError(this, "The -i|--input command must be specified to run SolPicker.\n"+help);
		
		jf.completeJobs();
	}
	

	private String help() {
		String output = "SolPicker Usage:\n\n";
		output += inFile+"\n"+out+"\n"+netDir+"\n"+localDir+"\n"+databaseDir+"\n"+help+"\n";
		return output;
	}
	
	/**
	 * {@value}
	 */
	private final String inFile = "-i|--input	A file containing the xml input.  See SolPickerSchema.xsd for information on constructing this file";
	/**
	 * {@value} 
	 */
	private final String out = "-o|--output	The directory where you want output from the program to be stored";
	/**
	 * {@value}
	 */
	private final String netDir = "-n|--netblast	The location of the netblast directory";
	/**
	 * {@value}
	 */
	private final String help = "-h|--help	Prints help information about the commands used in SolPicker.  For more information about using SolPicker see the documentation files";
	/**
	 * {@value}
	 */
	private final String localDir = "-l|--localblast	The location of the localblast directory";
	/**
	 * {@value}
	 */
	private final String databaseDir = "-d|--database	The directory where the blast database is contained.  ";
}
