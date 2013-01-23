package solPicker;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Handles all error reporting for SolPicker. Provide a message to the class and if it is a fatal error or error it will exit
 * with that message and report it to the {@link Log#logMessage(String)}.  A warning only sends a message to 
 * {@link Log#logMessage(String)} and allows the program to continue running.
 * 
 * @see Log#logMessage(String)
 * @see XMLErrorHandler
 * @author tizatt
 *
 */
public class ErrorReporter {

	

	/**
	 * Report a warning
	 * @param o
	 * @param message
	 */
	public static void reportWarning(Object o, String message){
		printMessage(message, "Warning");
	}
	
	/**
	 * Report an error
	 * @param o
	 * @param message
	 */
	public static void reportError(Object o, String message){
		printMessage(message+" ... exiting", "Error");
		System.exit(0);
	}
	
	/**
	 * Report a fatal error
	 * @param o
	 * @param message
	 */
	public static void reportFatalError(Object o, String message){
		printMessage(message + " ... exiting"+o.toString(), "Fatal Error");
		System.exit(0);
	}
	
	
	/**
	 * Prints a message to the correct output - only "cli" or "gui" are acceptable
	 * @param message
	 * @param errorType
	 */
	private static void printMessage(String message, String errorType){
		if(userInterface == null){
			Log.logMessage("UserInterface (GUI or CLI) was not specified.  Programming error.");
		}
		if(userInterface.equals(CLI)){
			Log.logMessage(errorType+": "+message);
		}
		else if (userInterface.equals(GUI)){
			JOptionPane.showMessageDialog(new JFrame(),message, errorType,
				    JOptionPane.ERROR_MESSAGE);

		}
	}
	
	
	public static void setUserInterface(String user){
		userInterface = user;
	}
	
	public static String getUserInterface() {
		return userInterface;
	}
	
	/**
	 * Command Line Interface {@link #CLI}
	 */
	private static final String CLI = "cli";
	
	/**
	 * 
	 */
	private static final String GUI = "gui";
	
	private static String userInterface;
}
