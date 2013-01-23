package solPicker.job;

import java.util.ArrayList;

/**
 * The ParseSequence Class takes a sequence and creates all possible oligos of
 * the specified length. For example the following code would be used to parse
 * the sequence ACCCTAAGTAA into seven oligos of length 5 ParseSequence sequence
 * = new ParseSequence("ACCCTAAGTAA", 5); ArrayList<Oligo> oligos =
 * sequence.parse(); this code would produce an ArrayList with the following
 * contents: [ACCCT, CCCTA, CCTAA, CTAAG, TAAGT, AAGTA, AGTAA]
 * 
 * The User can also force the parser to put a minimum distance between oligos
 * in the following way: ParseSequence sequence = new
 * ParseSequence("ACCCTAAGTAA", 5, 3); the parse method would then produce the
 * following ArrayList: [ACCCT, CTAAG, AGTAA]
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: ParseSequence.java,v 1.1 2009/08/18 21:31:41 tizatt Exp $
 */
public class ParseSequence {
	/**
	 * Creates a ParseSequence Object with the Sequence Field set equal to the
	 * first parameter and the oligo length field set equal to the second
	 * parameter
	 * 
	 * @param seq
	 *            the sequence to be parsed
	 * @param length
	 *            the length of the oligos to be created
	 */
	public ParseSequence(String seq, int length) {
		sequence = seq;
		oLength = length;
		minDistance = 1;
	}

	/**
	 * Creates a ParseSequence Object with the Sequence Field set equal to the first parameter and the oligo length field set equal to 
	 * the second parameter
	 * @param seq the sequence to be parsed
	 * @param length the length of the oligos to be created
	 * @param id the ID of the Query from which the oligos are being created
	 */
	public ParseSequence(String seq, int length, String id)
	{
		sequence = seq;
		oLength = length;
		minDistance = 1;
		qID = id;
	}
	
	/**
	 * Creates a ParseSequence Object with the Sequence Field set equal to the
	 * first parameter, the oligo length field set equal to the second
	 * parameter, and the distance field set equal to the third parameter
	 * 
	 * @param seq
	 *            the sequence to be parsed
	 * @param length
	 *            the length of the oligos to be created
	 * @param dist
	 *            the distance between the oligos that are created

	 */
	public ParseSequence(String seq, int length, int dist) {
		sequence = seq;
		oLength = length;
		minDistance = dist;
	}

	/**
	 * Creates a ParseSequence Object with the Sequence Field set equal to the first parameter, the oligo length field set equal to the
	 * second parameter, and the distance field set equal to the third parameter
	 * @param seq the sequence to be parsed 
	 * @param length the length of the oligos to be created
	 * @param dist the distance between the oligos that are created
	 * @param id the ID of the Query from which the oligos are being created
	 */
	public ParseSequence(String seq, int length, int dist, String id) {
		sequence = seq;
		oLength = length;
		minDistance = dist;
		qID = id;
	}

	/**
	 * Goes through the sequence and creates Oligos that contain every possible
	 * subsequence that can be created from the original sequence. Spaces oligos
	 * out based on the minimum distance specified by the user.
	 * 
	 * @return an ArrayList of Oligos containing every possible subsequence that
	 *         can be created from the sequence supplied to the ParseSequence
	 *         Object.
	 */
	public ArrayList<Oligo> parse() {
		ArrayList<Oligo> oligos = new ArrayList<Oligo>();
		for (int i = 0; i < sequence.length() - oLength + 1; i += minDistance) {
			String subSequence = sequence.substring(i, i + oLength);
			Oligo o = new Oligo(subSequence);
			o.setQID(qID);
			oligos.add(o);
		}
		return oligos;
	}

	private String sequence;
	private int oLength;
	private int minDistance;
	private String qID;
}
