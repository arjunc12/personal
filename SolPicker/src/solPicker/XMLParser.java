package solPicker;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import solPicker.filters.BaseFractionFilter;
import solPicker.filters.BlastFilter;
import solPicker.filters.EnzymeFilter;
import solPicker.filters.FailScoreFilter;
import solPicker.filters.Filter;
import solPicker.filters.GCFilter;
import solPicker.filters.HomoPolymerFilter;
import solPicker.filters.LowerCaseFilter;
import solPicker.filters.NCountFilter;
import solPicker.filters.TMFilter;
import solPicker.job.Configuration;
import solPicker.job.Job;
import solPicker.job.Query;

/**
 * This class takes the XML file containing all input information and parses it
 * using a Document Object Model (DOM) parser, which creates a hierarchical tree
 * with the tags stored as nodes. It then searches that tree and creates query,
 * configuration, and solPicker.job objects that contain all the necessary data.
 * Furthermore, this parser validates the XMl based on the schema
 * SolPickerSchema.xsd, and checks for errors in the input fields.
 * 
 * @author Arjun Chandrasekhar, Tyler Izatt and Erin Price-Wright
 * 
 * @version $Id: XMLParser.java,v 1.9 2009/10/28 21:14:05 tizatt Exp $
 *@see <a href="http://www.w3.org/2001/XMLSchema">XML Schema Website<a>
 *@see <a href="../../Pictures/xmlinput.jpg">XML file<a>
 */
public class XMLParser {
	/**
	 * Constructor
	 */
	public XMLParser(File f) {
		queries = new ArrayList<Query>();
		jobs = new ArrayList<Job>();
		configs = new ArrayList<Configuration>();
		xeh = new XMLErrorHandler();
		xmlFile = f;
		oligoCount = -1;
	}

	/**
	 * Parses the XML file into a set of objects: Query, Configuration and Job.
	 * This parser works by retrieving all relevant elements from the DOM Tree
	 * and storing them according to their respective tag names (using lists).
	 * This parsing method ultimately creates 3 lists containing Query,
	 * Configuration and Job objects. Each solPicker.job object contains its
	 * corresponding Queries and Configurations.
	 * 
	 */
	public void parse() {
		Log.logMessage("Parsing XML...");
		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		validateWithSchema();

		try {
			doc = factory.newDocumentBuilder().parse(xmlFile);
		} catch (SAXException e) {
			ErrorReporter.reportFatalError(this, "SAX Exception Parser : "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			ErrorReporter.reportFatalError(this, "IO Exception Parser : "+e.getMessage());
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			ErrorReporter.reportFatalError(this, "ParserConfiguration Exception : "+e.getMessage());
			e.printStackTrace();
		}

		setElementLists(doc);
		parseQueries();
		for (int i = 0; i < nConfigs.getLength(); i++) {
			parseConfiguration(nConfigs.item(i));
		}
		for (int i = 0; i < nJobs.getLength(); i++) {
			parseJob(nJobs.item(i));
		}
		Log.logMessage(xeh.toString());
	}

	/**
	 * Validates the XML based on the schema XML/SolPickerSchema.xsd. If there
	 * are any faults in the XML where the input does not conform to the schema,
	 * an error message is returned prompting the user to check the input. The
	 * XML will not be parsed if there are any errors. Guidelines for proper XML
	 * construction are given in the documentation for SolPicker.
	 * 
	 */
	private void validateWithSchema() {
		try {
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(SCHEMA_LANG);
			Schema schema;
			
			URL urlSchema = getClass().getResource(SCHEMA_FILE);
			System.out.println("schema = " + urlSchema);
			if (urlSchema == null)
				ErrorReporter.reportError(this, "Could not find xml schema file : "+urlSchema);
			schema = schemaFactory.newSchema(new StreamSource(urlSchema.toString()));
			Validator validator = schema.newValidator();
			validator.setErrorHandler(new XMLErrorHandler());
			validator.validate(new StreamSource(xmlFile));

		} catch (SAXException e1) {
			// Validation error
			ErrorReporter.reportFatalError(this, "XML Format Error: "
					+ e1.getMessage());
		} catch (IOException e) {
			//
			ErrorReporter.reportFatalError(this, "XML I/O exception");
		}
	}

	/**
	 * Gathers all the element tags in the XML and puts them into separate
	 * lists. Each list is a global list that is stored
	 * 
	 * @see solPicker.job.Configuration
	 * @see solPicker.job.Query
	 * @see solPicker.job.Job
	 * @param doc
	 */
	private void setElementLists(Document doc) {
		nConfigs = doc.getElementsByTagName("Configuration");
		nJobs = doc.getElementsByTagName("Job");
		nQueries = doc.getElementsByTagName("Query");
		dNode = doc.getElementsByTagName("Domain");
		olNode = doc.getElementsByTagName("oligoLength");
		oldNode = doc.getElementsByTagName("oligoDistance");
		orgNode = doc.getElementsByTagName("Organism");
		chrNode = doc.getElementsByTagName("Chromosome");
		startNode = doc.getElementsByTagName("startPosition");
		endNode = doc.getElementsByTagName("endPosition");
		p5Node = doc.getElementsByTagName("Padding5");
		p3Node = doc.getElementsByTagName("Padding3");
		olcNode = doc.getElementsByTagName("oligoCount");
		// If oligoCount is given in xml, it is set here
		if (olcNode.getLength() != 0)
			oligoCount = Integer.parseInt(olcNode.item(0).getTextContent());
	}

	/**
	 * Parses queries, retrieving the chromosome, start position, end position,
	 * oligo lengths, oligo distance, organism, 3' padding, 5' padding, and
	 * domain. It also checks to make sure the query name is unique.
	 * 
	 * @see solPicker.job.Query
	 */
	private void parseQueries() {
		for (int i = 0; i < nQueries.getLength(); i++) {

			String id = nQueries.item(i).getAttributes().item(0).getNodeValue();
			long start = Integer.parseInt(startNode.item(i).getTextContent());
			long end = Integer.parseInt(endNode.item(i).getTextContent());
			if(start>= end)
				ErrorReporter.reportFatalError(this, "Query \""+id+"\": StartPosition exceeds EndPosition.");
			Integer.parseInt(olNode.item(i).getTextContent());
			Query q = new Query(chrNode.item(i).getTextContent(), start+"",
					end+"", id, Integer
							.parseInt(olNode.item(i).getTextContent()));
			q.setMinDistance(oldNode.item(i).getTextContent());
			String domain = checkDomain(dNode.item(i));
			q.setOrganism(orgNode.item(i).getTextContent());
			q.setDomain(domain);
			if (domain.equalsIgnoreCase("ucsc")) {
				q.set3Padding(p3Node.item(i).getTextContent());
				q.set5Padding(p5Node.item(i).getTextContent());
			}
			if (isQueryUnique(q)) {
				queries.add(q);
			} else {
				ErrorReporter.reportWarning(this, "QueryName : \"" + id
						+ "\" is a duplicate... using first instance of \""
						+ id + "\"");
			}
		}
	}

	/**
	 * Retrieves the domain information from the xml file
	 * 
	 * @param nDomain
	 * @return domain
	 */
	private String checkDomain(Node nDomain) {
		NodeList nl = nDomain.getChildNodes();
		String domain = nl.item(1).getNodeName();
		return domain;
	}

	/**
	 * Parses the configurations to create configuration objects, which hold
	 * filter objects. Prints an error message if a filter does not exist or if
	 * the configuration id is duplicate.
	 * 
	 * @see solPicker.job.Configuration
	 * @param n
	 */
	private void parseConfiguration(Node n) {
		Configuration config = new Configuration();
		NamedNodeMap nnm = n.getAttributes();
		config.setID(nnm.item(0).getNodeValue());
		NodeList filters = n.getChildNodes();
		for (int i = 1; i < filters.getLength(); i += 2) {
			Node nFilter = filters.item(i);
			config.addFilter(parseFilter(nFilter));
		}
		config.addFilter(new BlastFilter());
		if (isConfigurationUnique(config))
			configs.add(config);
		else
			ErrorReporter.reportWarning(this, "ConfigID : \"" + config.getID()
					+ "\" is a duplicate... using first instance of \""
					+ config.getID() + "\"");
	}

	/**
	 * Parses filters to create filter objects within the configuration ojects.
	 * Valid filters are: lowercase, ncount, enzyme, gcpercent, and filscore.
	 * These filters have specific parameters, which this method retrieves. The
	 * filter objects store their parameters and constraints.
	 * 
	 * @see solPicker.filters.Filter
	 * @param n
	 *            the Node containing the Filter
	 * @return the Filter contained in the Node
	 */

	private Filter parseFilter(Node n) {
		Filter f = null;
		HashMap<String, String> hm;
		NamedNodeMap nnm = n.getAttributes();
		Node temp = nnm.getNamedItem("Type");
		String filterName = temp.getNodeValue();
		hm = parseFilterParameter(n.getChildNodes());
		if (filterName.equals("lowercase")
				&& checkFilterParameter(hm.get("max"), "int",filterName)) {
			f = new LowerCaseFilter(Integer.parseInt(hm.get("max")));
		} else if (filterName.equals("ncount")
				&& checkFilterParameter(hm.get("max"), "int", filterName)) {
			f = new NCountFilter(Integer.parseInt(hm.get("max")));
		} else if (filterName.equals("enzyme")) {
			f = new EnzymeFilter(parseEnzymes(hm));
		} else if (filterName.equals("gcpercent")
				&& checkFilterParameter(hm.get("min"), "double", filterName)
				&& checkFilterParameter(hm.get("max"), "double", filterName)) {
			f = new GCFilter(Double.parseDouble(hm.get("min")), Double
					.parseDouble(hm.get("max")));
		} else if (filterName.equals("failscore")
				&& checkFilterParameter(hm.get("max"),"int",filterName)) {
			f = new FailScoreFilter(Integer.parseInt(hm.get("max")));
		} else if (filterName.equals("homopolymer")
				&& checkFilterParameter(hm.get("max"), "int", filterName)) {
			f = new HomoPolymerFilter(Integer.parseInt("max"));
		} else if (filterName.equals("basefraction")
				&& checkFilterParameter(hm.get("max"), "double", filterName)
				&& checkFilterParameter(hm.get("min"), "double", filterName)) {
			f = new BaseFractionFilter(Double.parseDouble(hm.get("min")),
					Double.parseDouble(hm.get("max")));
		} else if (filterName.equals("tmscore")
				&& checkFilterParameter(hm.get("max"), "int",filterName)
				&& checkFilterParameter(hm.get("min"), "int", filterName)) {
			f = new TMFilter(Integer.parseInt(hm.get("min")), Integer
					.parseInt(hm.get("max")));
		} else {
			ErrorReporter.reportFatalError(this, "Filter \"" + filterName
					+ "\" does not exist");
		}
		return f;
	}

	/**
	 * 
	 * @param filterType
	 * @param parameter
	 * @param hm
	 * @return boolean
	 * 
	 */
	private boolean checkFilterParameter(String value, Object type, String filter) {

		if(value== null){
			ErrorReporter.reportFatalError(this, "Filter: "+filter+" Parameter: \" parameter is missing");
		}
		if(type.equals("int")){
			try{
				Integer.parseInt(value);
			}
			catch (NumberFormatException n){
				ErrorReporter.reportFatalError(this, "Filter: "+filter+" Parameter: \""+value+"\" should be an integer value.");
			}
		}
		else if (type.equals("double")){
			try{
				Double.parseDouble(value);
			}
			catch(NumberFormatException n){
				ErrorReporter.reportFatalError(this, "Filter: "+filter+" Parameter: \""+value+"\" should be a double value");
			}
		}
		
		return true;
	}

	/**
	 * This is a general method for retrieving filter parameters, no matter how
	 * many there are or what constraints they impose. It grabs the parameter
	 * attributes, which are constraints (min, max, et cetera) and the values of
	 * those constraints. Example: Constraint="max" Value="20" - This is stored
	 * as "max"->"20".
	 * 
	 * 
	 * @param nParams
	 * @return params - A map object containing constraints -> values
	 */
	private HashMap<String, String> parseFilterParameter(NodeList nParams) {

		HashMap<String, String> params = new HashMap<String, String>();
		for (int i = 1; i < nParams.getLength(); i += 2) {
			NamedNodeMap nnm = nParams.item(i).getAttributes();
			params.put(nnm.item(0).getNodeValue(), nnm.item(1).getNodeValue());
		}

		return params;
	}

	/**
	 * Converts a HashMap of String objects into an ArrayList containing the
	 * values from that HashMap. This operation is performed because the parser
	 * uses HashMaps to store attributes, which is more convenient for error
	 * checking. But the EnzymeFilter class accepts ArrayLists, so these must be
	 * converted from maps to lists.
	 * 
	 * @param hm
	 * @return list of enzymes
	 */
	private ArrayList<String> parseEnzymes(HashMap<String, String> hm) {
		ArrayList<String> al = new ArrayList<String>();
		Set<Entry<String, String>> entries = hm.entrySet();
		Iterator<Entry<String, String>> it = entries.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			al.add((String) entry.getValue());
		}
		return al;
	}

	/**
	 * Parses to create solPicker.job objects. Returns an error if the
	 * solPicker.job is missing a valid query name or configuration id.
	 * 
	 * @see solPicker.job.Job
	 * @param n
	 */
	private void parseJob(Node n) {
		NamedNodeMap jobAttributes = n.getAttributes();
		Node nConfigID = jobAttributes.getNamedItem("ConfigID");
		Node nQueryID = jobAttributes.getNamedItem("QueryName");
		Node nJobID = jobAttributes.getNamedItem("JobID");
		Query q = null;
		Configuration c = null;
		for (int i = 0; i < queries.size(); i++) {
			if (queries.get(i).getID()
					.equalsIgnoreCase(nQueryID.getNodeValue())) {
				q = queries.get(i);
			}
		}
		for (int i = 0; i < configs.size(); i++) {
			if (configs.get(i).getID().equalsIgnoreCase(
					nConfigID.getNodeValue())) {
				c = configs.get(i);
			}
		}
		if (c == null)
			ErrorReporter.reportFatalError(this, "Configuration ID: "
					+ nConfigID.getNodeValue() + " does not exist");
		else if (q == null)
			ErrorReporter.reportFatalError(this, "Query ID: "
					+ nQueryID.getNodeValue() + " does not exist");
		Job j = new Job(c, q, Integer.parseInt(nJobID.getNodeValue()));
		jobs.add(j);
	}

	/**
	 * Checks whether this Query object has already been declared in the xml. By
	 * default the program will use the first instance of the id, and send a
	 * warning to the user.
	 * 
	 * @see solPicker.job.Query
	 * @param q
	 *            - Query object
	 * @return true if there are no Queries that currently exist and that have
	 *         the same id as the Query that was passed as a parameter, false if
	 *         no Query exists that has the same ID
	 */
	private boolean isQueryUnique(Query q) {
		for (int i = 0; i < queries.size(); i++) {
			if (queries.get(i).getID().equalsIgnoreCase(q.getID())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks whether this Configuration object has been previously declared in
	 * the xml. By default the program will use the first instance of the id
	 * that it sees in the xml. A warning to the user is sent out.
	 * 
	 * @see solPicker.job.Configuration
	 * @param c
	 *            - Configuration object
	 * @return - true if Configuration object is unique, false if it is a
	 *         duplicate
	 */
	private boolean isConfigurationUnique(Configuration c) {
		for (int i = 0; i < configs.size(); i++) {
			if (configs.get(i).getID().equalsIgnoreCase(c.getID())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns a list of query objects
	 * 
	 * @see solPicker.job.Query
	 * @return queries
	 */
	public ArrayList<Query> getQueries() {
		return queries;
	}

	/**
	 * Returns a list of solPicker.job objects
	 * 
	 * @see solPicker.job.Job
	 * @return jobs
	 */
	public ArrayList<Job> getJobs() {
		return jobs;
	}

	/**
	 * Returns a list of configuration objects
	 * 
	 * @see solPicker.job.Configuration
	 * @return configurations
	 */
	public ArrayList<Configuration> getConfigurations() {
		return configs;
	}

	/**
	 * The maximum number of oligos that should be picked by SolPicker. This
	 * value is used to limit the number of oligos output by the program. If no
	 * value is specified in xml, the program does not restrict the number of
	 * oligos picked.
	 * 
	 * @see solPicker.job.Oligo
	 * @return any positive integer or -1 if no value is specified
	 */
	public int getOligoCount() {
		return oligoCount;
	}


	/**
	 * Error handler used in parsing the XML tree.
	 * 
	 * @see solPicker.XMLErrorHandler
	 */
	protected XMLErrorHandler xeh;

	/**
	 * Nodelist representing each Configuration element in the xml document.
	 */
	protected NodeList nConfigs;
	/**
	 * NodeList representing each Job element in the xml document.
	 */
	protected NodeList nJobs;
	/**
	 * NodeList representing each "Query element in the xml document.
	 */
	protected NodeList nQueries;
	/**
	 * NodeList representing each "Domain" element in the xml document.
	 */
	protected NodeList dNode;
	/**
	 * NodeList representing each "Oligo length" element in the xml document.
	 */
	protected NodeList olNode;
	/**
	 * NodeList representing each "Oligo distance" element in the xml document.
	 */
	protected NodeList oldNode;
	/**
	 * NodeList representing each "Organism" element in the xml document.
	 */
	protected NodeList orgNode;
	/**
	 * NodeList representing each "chromosome" element in the xml document.
	 */
	protected NodeList chrNode;
	/**
	 * NodeList representing each "start" base element in the xml document.
	 */
	protected NodeList startNode;
	/**
	 * NodeList representing each "end" (base) element in the xml document.
	 */
	protected NodeList endNode;
	/**
	 * NodeList representing each "padding5" element in the xml document.
	 */
	protected NodeList p5Node;
	/**
	 * NodeList representing each "padding3" element in the xml document.
	 */
	private NodeList p3Node;
	/**
	 * NodeList representing each "Oligo count" element in the xml document.
	 */
	private NodeList olcNode;

	/**
	 * Integer representing the maximum number of oligos that the user
	 * specifies. If the user does not provide an oligo count in the xml it is
	 * set to -1 in the constructor.
	 */
	private int oligoCount;

	/**
	 * List containing all Query objects found in the xml document.
	 * 
	 * @see solPicker.job.Query
	 */
	private ArrayList<Query> queries;
	/**
	 * List containing all Configuration objects found in the xml document.
	 * 
	 * @see solPicker.job.Configuration
	 */
	private ArrayList<Configuration> configs;
	/**
	 * List containing all Job objects found in the xml document.
	 * 
	 * @see solPicker.job.Job
	 */
	private ArrayList<Job> jobs;

	/**
	 * The user provided xml file to be parsed.
	 * 
	 * @see <a href="../../PosterPictures/xmlinput.jpg">Example XML file<a>
	 */
	private File xmlFile;

	/**
	 * Default schema langauge used by parser. * {@value}
	 * 
	 * @see <a href="http://www.w3.org/2001/XMLSchema">XML Schema Website<a>
	 */
	private static final String SCHEMA_LANG = "http://www.w3.org/2001/XMLSchema";
	/**
	 * SolPicker schema file * {@value}
	 */
	private final String SCHEMA_FILE = "SolPickerSchema.xsd";
}
