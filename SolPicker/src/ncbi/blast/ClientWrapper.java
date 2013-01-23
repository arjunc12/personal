package ncbi.blast;

/**
 * 
 * @author tizatt
 * @version $Id: ClientWrapper.java,v 1.1 2009/12/15 21:14:05 tizatt Exp $
 */
public class ClientWrapper {
	public ClientWrapper(){}
	
	public static final class OsUtils
	{
	   private static String OS = null;
	   
	   public static String getOsName()
	   {
	      if(OS == null) { 
	    	  OS = System.getProperty("os.name");
	      }
	      return OS;
	   }
	   
	   
	   public static boolean isWindows(){
	      return getOsName().startsWith("Windows");
	   }

	   
	   
	   public static boolean isUnix(){
		  return getOsName().startsWith("Unix");
	   }
	   
	   public static boolean isMac(){
		   return getOsName().startsWith("Mac");
	   }
	}

	
	
}
