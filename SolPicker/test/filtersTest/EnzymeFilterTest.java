package filtersTest;

import java.util.ArrayList;

import solPicker.job.Oligo;
import solPicker.job.ParseSequence;
import solPicker.filters.EnzymeFilter;


import junit.framework.TestCase;

public class EnzymeFilterTest extends TestCase 
{
	private EnzymeFilter eFilter;
	private ArrayList<String> enzymes;
	private ArrayList<Oligo> oligos;
	public EnzymeFilterTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		oligos = new ParseSequence("ACTAGTATA", 5).parse();//ACTAG CTAGT TAGTA AGTAT GTATA
		enzymes = new ArrayList<String>();
		enzymes.add("GAT");
		enzymes.add("CTA");
		eFilter = new EnzymeFilter(enzymes);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		eFilter = null;
		enzymes = null;
		oligos = null;
	}
	
	public void testEnzymeFilter()
	{
		assertTrue(oligos.size() == 5);
		assertTrue(oligos.toString().equals("[ACTAG, CTAGT, TAGTA, AGTAT, GTATA]"));
		oligos = eFilter.filter(oligos);
		assertTrue(oligos.size() == 2);
		assertTrue(oligos.toString().equals("[AGTAT, GTATA]"));
		ArrayList<Oligo> rejected = eFilter.getRejectedOligos();
		assertTrue(rejected.size() == 3);
		assertTrue(rejected.toString().equals("[ACTAG, CTAGT, TAGTA]"));
		assertTrue(rejected.get(0).getComplementaryEnzyme().equals("GAT"));
		assertTrue(rejected.get(1).getComplementaryEnzyme().equals("GAT"));
		assertTrue(rejected.get(2).getComplementaryEnzyme().equals("CTA"));
	}
}
