package xmlTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.xml.sax.SAXParseException;

import solPicker.XMLParser;


public class XMLParserTest extends TestCase{
	
	public XMLParserTest(String name, int test){
		super(name);
		this.test = test-1;
		
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		testFiles = new ArrayList<File>();
		testFiles.add(new File("XML/test1.xml"));
		testFiles.add(new File("XML/test2.xml"));
		testFiles.add(new File("XML/test3.xml"));
		testFiles.add(new File("XML/test4.xml"));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void test1() throws SAXParseException{
		x = new XMLParser(testFiles.get(test));
		x.parse();
		
		assertTrue(x.getConfigurations().size() == 1);
		assertTrue(x.getJobs().size() == 1);
		assertTrue(x.getQueries().size() == 1);
		assertTrue(x.getOligoCount() == -1);
	}
	
	//
	public void test2() throws FileNotFoundException{
		@SuppressWarnings("unused")
		String testText="",objectText="";
		x = new XMLParser(testFiles.get(test));
		x.parse();
		testText = readTestFile(testFiles.get(test));
		objectText = readObjects();
		assertTrue(x.getConfigurations().size() == 1);
		assertTrue(x.getJobs().size() == 1);
		assertTrue(x.getConfigurations().get(0).getFilters().get(0).getFilterType().equals("lowercase"));
		//System.out.println(testText+"\n"+objectText);
		//assertTrue(testText.equals(objectText));
	}

	
	public void test3(){
		x = new XMLParser(testFiles.get(test));
		x.parse();	
	}

	
	public void test4(){
		x = new XMLParser(testFiles.get(test));
		x.parse();
	}
	
	// reads a test xml file using Scanner class - removes all whitespace so that the parsed text can
	// be compared with the output of the XMLParser class.
	public String readTestFile(File f){
		String text="";
		try {
			Scanner s = new Scanner(f);
			while(s.hasNext()){
				text += s.next();
			}
			text = text.replaceAll("\\s+", "");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();}
		return text;
	}
	
	//accesses toXML() of each object class : Job, Query, Configuration and creates xml file from this information
	// to be compared against the input xml.  All whitespace is removed.
	public String readObjects(){
		String text="";
		text=HEADER;
		for(int i = 0; i< x.getQueries().size(); i++){
			text += x.getQueries().get(i).toXML();
		}
		for(int i = 0; i< x.getConfigurations().size(); i++){
			text += x.getConfigurations().get(i).toXML();
		}
		for(int i = 0; i< x.getJobs().size(); i++){
			text += x.getJobs().get(i).toXML();
		}
		text+="</SolPicker>";
		text = text.replaceAll("\\s+", "");
		return text;
	}
	
	
	
	public void addTestFile(String file){
		testFiles.add(new File(file));
	}
	
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		//suite.addTest(new XMLParserTest("test1",1));
		//suite.addTest(new XMLParserTest("test2",2));
		//suite.addTest(new XMLParserTest("test3",3));
		//suite.addTest(new XMLParserTest("test4",4));
		return suite;
	}
	
	
	private XMLParser x;
	private int test;
	private ArrayList<File> testFiles;
	private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<SolPicker xmlns=\"http://www.example.org/SolPickerSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.example.org/SolPickerSchema SolPickerSchema.xsd \">";
	 
}
