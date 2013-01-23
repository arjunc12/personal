package solPicker;


/**
 * Records all information about the actual program run into a log file
 * @author tizatt
 *
 */
public class Log {

	
	
	/**
	 * Logs a message to the logfile.
	 * @param message - a message to be output to the log file
	 */
	public static void logMessage(String message){
			System.out.println(message);
			
			/*try{
			 * System.out.println(XMLWriter.getFile().getAbsolutePath());
			logFile = XMLWriter.getFile().getAbsolutePath()+logFile;
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File(logFile), true));
			writer.write(message+"\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
	}
	
	@SuppressWarnings("unused")
	private static String logFile = "log.txt";
}
