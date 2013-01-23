package jobTest;

import solPicker.job.Configuration;
import solPicker.filters.EnzymeFilter;
import solPicker.filters.FailScoreFilter;
import solPicker.filters.GCFilter;
import solPicker.filters.LowerCaseFilter;
import solPicker.filters.NCountFilter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConfigurationTest extends TestCase 
{
	private Configuration config;
	public ConfigurationTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		config = new Configuration("1");
		config.addFilter(new EnzymeFilter(null));
		config.addFilter(new FailScoreFilter(10));
		config.addFilter(new GCFilter(20, 60));
		config.addFilter(new LowerCaseFilter(10));
		config.addFilter(new NCountFilter(10));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		config = null;
	}
	
	public void testConfig()
	{
		assertTrue(config.getID().equals("1"));
		String filterTypes = "[Enzyme Filter, Fail Score Filter, GC Filter, lowercase, N Count Filter]";
		assertTrue(config.getFilters().toString().equals(filterTypes));
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new ConfigurationTest("testConfig"));
		return suite;
	}

}
