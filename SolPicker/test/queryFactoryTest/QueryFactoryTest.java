package queryFactoryTest;

import java.util.ArrayList;

import solPicker.job.Query;
import solPicker.job.QueryFactory;
import solPicker.job.Range;



import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/**
 * @deprecated
 * @author Tyler Izatt
 */
@SuppressWarnings("unused")
public class QueryFactoryTest extends TestCase {
	
	private QueryFactory qf;
	private Query q,q1,q2,q3,q4;
	private ArrayList<Query> queries;
	
	private String chr1 = "A", chr2="x",chr3="X",chr4="Y",chr5="a";
	private String s1="1",s2="2",s3="2",s4="5",s5="1";
	private String e1="10",e2="9",e3="5",e4="10",e5="5";
	private ArrayList<Range> r;
	
	public QueryFactoryTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		/*super.setUp();
		qf = new QueryFactory(300);
		q = new Query(chr1, s1,e1,6, "0", "0", "Homo_sapiens", true, true, true, true, 1, 1, 0, 100, null,1);
		q1 = new Query(chr2,s2,e2,6, "0", "0", "Homo_sapiens", true, true, true, true, 1, 1, 0, 100, null,1);
		q2 = new Query(chr3,s3,e3,6, "0", "0", "Homo_sapiens", true, true, true, true, 1, 1, 0, 100, null,1);
		q3 = new Query(chr4,s4,e4,6, "0", "0", "Homo_sapiens", true, true, true, true, 1, 1, 0, 100, null,1);
		q4 = new Query(chr5,s5,e5,6, "0","0","Homo_sapiens", true, true, true, true, 1, 1, 0, 100, null,1);
		queries = new ArrayList<Query>();
		queries.add(q);
		queries.add(q1);
		queries.add(q2);
		queries.add(q3);
		queries.add(q4);
		qf.joinQueries(queries);
		r = qf.getRangeArray();
		for(int i =0; i<r.size(); i++){
			qf.separateSequenceToQuery(r.get(i), "AATTGGCCAATTGAACT");
		}*/
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	public void testSortQueries()
	{
		
		ArrayList<Query> sorted = new ArrayList<Query>();
		sorted.add(q);
		sorted.add(q4);
		sorted.add(q1);
		sorted.add(q2);
		sorted.add(q3);

		/*
		for(int i = 0; i<queries.size(); i++){
			assertTrue(queries.get(i).equals(sorted.get(i)));
		}
		*/
		
	}
	
	
	
	public void testJoinQueries()
	{
		/*
		assertTrue(qf.getMinDistance() == 300);
		ArrayList<String> start = new ArrayList<String>();
		ArrayList<String> end = new ArrayList<String>();
		start.add(s1);
		end.add(e1);
		start.add(s2);
		end.add(e2);
		start.add(s4);
		end.add(e4);
		assertTrue(r.size() == 3);
		
		for(int i = 0; i< r.size(); i++){
			System.out.println(r.get(i).getStartBase()+" : "+start.get(i));
			System.out.println(r.get(i).getEndBase()+" : "+end.get(i));
			assertTrue(r.get(i).getStartBase().equals(start.get(i)));
			assertTrue(r.get(i).getEndBase().equals(end.get(i)));
		}
		*/	
	}
	
	public void testSeparateSequenceToQuery(){
		
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new QueryFactoryTest("testSortQueries"));
		suite.addTest(new QueryFactoryTest("testJoinQueries"));
		suite.addTest(new QueryFactoryTest("testSeparateSequenceToQuery"));
		return suite;
	}

}
