package solPicker;

import java.util.ArrayList;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class handles all errors associated with parsing an xml document. There
 * are three classifications: 1) Warning 2) error 3) fatal error Each error is
 * handled separately.
 * 
 * @author Tyler Izatt
 * @version $Id: XMLErrorHandler.java,v 1.5 2009/10/07 20:19:41 tizatt Exp $
 * @see solPicker.XMLParser
 */
public class XMLErrorHandler implements ErrorHandler {

	public XMLErrorHandler() {
		errors = new ArrayList<String>();
		fatalErrors = new ArrayList<String>();
		warnings = new ArrayList<String>();
	}

	/**
	 * Receive notification of a recoverable error. This corresponds to the
	 * definition of "error" in section 1.2 of the W3C XML 1.0 Recommendation.
	 * For example, a validating parser would use this callback to report the
	 * violation of a validity constraint. The default behaviour is to take no
	 * action. The SAX parser must continue to provide normal parsing events
	 * after invoking this method: it should still be possible for the
	 * application to process the document through to the end. If the
	 * application cannot do so, then the parser should report a fatal error
	 * even if the XML 1.0 recommendation does not require it to do so. Filters
	 * may use this method to report other, non-XML errors as well.
	 * 
	 * @throws SAXException
	 * @see <a href="http://www.w3.org/TR/REC-xml/#sec-terminology">W3C XML 1.0
	 *      Recommendation: Section 1.2</a>
	 */
	public void error(SAXParseException exception) throws SAXException {
		ErrorReporter.reportError(this, message + " Error: "+exception.getMessage());
		errors.add(exception.getMessage());
	}

	/**
	 *Receive notification of a non-recoverable error. This corresponds to the
	 * definition of "fatal error" in section 1.2 of the W3C XML 1.0
	 * Recommendation. For example, a parser would use this callback to report
	 * the violation of a well-formedness constraint. The application must
	 * assume that the document is unusable after the parser has invoked this
	 * method, and should continue (if at all) only for the sake of collecting
	 * addition error messages: in fact, SAX parsers are free to stop reporting
	 * any other events once this method has been invoked.
	 * 
	 * @throws SAXException
	 * @see <a href="http://www.w3.org/TR/REC-xml/#sec-terminology">W3C XML 1.0
	 *      Recommendation: Section 1.2</a>
	 */
	public void fatalError(SAXParseException exception) throws SAXException {
		ErrorReporter.reportFatalError(this, message + " Fatal Error : "+exception.getMessage());
		fatalErrors.add("Fatal Error : " + exception.getMessage());
	}

	/**
	 * Receive notification of a warning. SAX parsers will use this method to
	 * report conditions that are not errors or fatal errors as defined by the
	 * XML 1.0 recommendation. The default behaviour is to take no action. The
	 * SAX parser must continue to provide normal parsing events after invoking
	 * this method: it should still be possible for the application to process
	 * the document through to the end. Filters may use this method to report
	 * other, non-XML warnings as well.
	 * 
	 * @throws SAXException
	 * @see <a href="http://www.w3.org/TR/REC-xml/#sec-terminology">W3C XML 1.0
	 *      Recommendation: Section 1.2</a>
	 */
	public void warning(SAXParseException exception) throws SAXException {
		ErrorReporter.reportWarning(this, message + " Error: "+exception.getMessage());
		warnings.add("Warning : " + exception.getMessage());
	}

	/**
	 * Check if this ErrorHandler has any fatalErrors.
	 * 
	 * @return - true if there is at least one fatal error, false if there are no fatal errors.
	 */
	public boolean hasFatalError() {
		if (fatalErrors.size() > 1)
			return true;
		else
			return false;
	}

	/**
	 * Check if this ErrorHandler has any errors.
	 * 
	 * @return - true if there are errors, false if there are no errors
	 */
	public boolean hasError() {
		if (errors.size() > 1)
			return true;
		else
			return false;
	}

	/**
	 * Check if this ErrorHandler has any warnings.
	 * 
	 * @return - true if there are any warnings, false if there are none.
	 */
	public boolean hasWarning() {
		if (warnings.size() > 1)
			return true;
		else
			return false;
	}

	/**
	 * Returns all fatal error, error and warning messages with headers for each
	 * category.
	 */
	public String toString() {
		String out = "";
		String end = "";
		if(this.hasFatalError() && this.hasError() && this.hasWarning()){
			out += "Errors Processing the XML...\n";
			end += "\n\n\n";
		}
		if (this.hasFatalError()) {
			out += "Fatal Errors:\n\n";
			for (int i = 0; i < fatalErrors.size(); i++) {
				out += fatalErrors.get(i) + "\n";
			}
			out += "\n\n";
		}
		if (this.hasError()) {
			out += "Errors:\n\n";
			for (int i = 0; i < errors.size(); i++) {
				out += errors.get(i) + "\n";
			}
			out += "\n\n";
		}
		if (this.hasWarning()) {
			out += "Warnings:\n\n";
			for (int i = 0; i < warnings.size(); i++) {
				out += warnings.get(i) + "\n";
			}
		}
		
		return out+end;
	}

	private String message = "XMLParser";
	/**
	 * fatal error message
	 */
	public ArrayList<String> fatalErrors;
	/**
	 * warning message
	 */
	public ArrayList<String> warnings;
	/**
	 * error message
	 */
	public ArrayList<String> errors;
}
