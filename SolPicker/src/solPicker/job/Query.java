package solPicker.job;

import server.Domain;

/**
 * The Query class represents a segment of the genome whose sequence is to be
 * retrieved. Each Query object contains information about its Chromosome
 * Number, the start and end position on the chromosome, the length of the
 * oligos that are to be created from its sequence, the amount of 3' and 5'
 * padding, the organism whose genome is being examined, the minimum distance
 * between oligos, and a unique id
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: Query.java,v 1.6 2009/10/14 17:38:14 achandra Exp $
 */
public class Query implements Comparable<Query> {
	/**
	 * Creates a Query Object with Chromosome Number, Start Position, End
	 * Position, id, and length fields initialized to the values provided as
	 * parameters. Organism, 5' padding, 3' padding, domain, and minimum
	 * distance fields initialized to default values of Homo_sapiens, 0, 0,
	 * server.ucsc, and 1 respectively
	 * 
	 * @param chr
	 *            chromosome number
	 * @param startPos
	 *            start position
	 * @param endPos
	 *            end position
	 * @param id
	 *            Query ID
	 * @param length
	 *            oligo length
	 */
	public Query(String chr, String startPos, String endPos, String id,
			int length) {
		this.chr = chr;
		start = startPos;
		end = endPos;
		this.id = id;
		oligoLength = length;
		organism = "Homo_sapiens";
		p5 = "0";
		p3 = "0";
		domain = "UCSC";
		minDistance = 1;
		oligoCount = 1000;
	}

	/**
	 * Changes the Query's organism field to the parameter
	 * 
	 * @param org
	 *            the Query's new organism
	 */
	public void setOrganism(String org) {
		organism = org;
	}

	/**
	 * Changes the Query's 5' padding field to the parameter
	 * 
	 * @param pad
	 *            the Query's new 5' padding
	 */
	public void set5Padding(String pad) {
		p5 = pad;
	}

	/**
	 * Changes the Query's 5' padding field to the parameter
	 * 
	 * @param pad
	 *            the Query's new 5' padding
	 */
	public void set5Padding(int pad) {
		p5 = pad + "";
	}

	/**
	 * Changes the Query's 3' padding field to the parameter
	 * 
	 * @param pad
	 *            the Query's new 3' padding
	 */
	public void set3Padding(String pad) {
		p3 = pad;
	}

	/**
	 * Changes the Query's 3' padding field to the parameter
	 * 
	 * @param pad
	 *            the Query's new 3' padding
	 */
	public void set3Padding(int pad) {
		p3 = pad + "";
	}

	/**
	 * Changes the Query's domain field to the parameter
	 * 
	 * @param dom
	 *            the Query's new domain
	 */
	public void setDomain(String dom) {
		domain = dom;
	}

	/**
	 * Changes the Query's domain field to the parameter
	 * 
	 * @param d
	 *            the Query's new domain
	 */
	public void setDomain(Domain d) {
		domain = d.getDomain();
	}

	/**
	 * Changes the Query's minimum distance field to the parameter
	 * 
	 * @param distance
	 *            the Query's new minimum distance
	 */
	public void setMinDistance(int distance) {
		minDistance = distance;
	}

	/**
	 * Changes the Query's minimum distance field to the parameter
	 * 
	 * @param distance
	 *            the Query's new distance
	 */
	public void setMinDistance(String distance) {
		minDistance = Integer.parseInt(distance);
	}
	
	
	public void setSequence(String sequence){
		this.sequence = sequence;
	}

	/**
	 * Retrieves the Query's chromosome number
	 * 
	 * @return the chromosome number
	 */
	public String getChromosomeIdentifier() {
		return chr;
	}

	/**
	 * Retrieve's the Query's start position
	 * 
	 * @return the start position
	 */
	public String getStart() {
		return start;
	}

	/**
	 * Retrieves the Query's end position
	 * 
	 * @return the end position
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * Retrieves the chromosome's unique ID number
	 * 
	 * @return the ID number
	 */
	public String getID() {
		return id;
	}

	/**
	 * Retrieves the Query's organism
	 * 
	 * @return the organism name
	 */
	public String getOrganism() {
		return organism;
	}

	/**
	 * Retrieves the Query's 5' padding
	 * 
	 * @return the 5' padding
	 */
	public String get5Padding() {
		return p5;
	}

	/**
	 * Retrieves the Query's 3' padding
	 * 
	 * @return the 3' padding
	 */
	public String get3Padding() {
		return p3;
	}

	/**
	 * Retrieves the Query's domain
	 * 
	 * @return the domain name
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Retrieves the Query's oligo length
	 * 
	 * @return the oligo length
	 */
	public int getOligoLength() {
		return oligoLength;
	}

	/**
	 * Retrieves the Query's minimum distance between Oligos
	 * 
	 * @return the minimum distance between Oligos
	 */
	public int getMinDistance() {
		return minDistance;
	}
	
	public String getSequence(){
		return sequence;
	}

	/**
	 * Compares two Query first by Chromosome Number and then by Start position.
	 * If the two Queries have different Chromosome numbers then the method
	 * compares the two chromosome numbers using the compareToIgnorecase method
	 * of the String class and returns that result. If the two Queries have the
	 * same chromosome number then the method subtracts the second Query's start
	 * position from the start position of the Query Object making the method
	 * call and returns the difference.
	 * 
	 * @return 0 if the two Queries have the same Chromosome Number and Start
	 *         Position. If the Chromosome numbers are different the method
	 *         returns the result of the compareToIgnoreCase method of the
	 *         String class, and if not then the method returns the difference
	 *         between the two start positions
	 */
	public int compareTo(Query o) {
		Query q = (Query) o;
		int s1 = Integer.parseInt(start);
		int s2 = Integer.parseInt(q.start);
		if (chr.equalsIgnoreCase(q.chr)) {
			return s1 - s2;
		} else {
			return chr.compareToIgnoreCase(q.chr);
		}
	}
	
	public String toFasta() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Creates an XML tag signifying the start of a Query Object with subsequent
	 * tags denoting the Query Name, Oligo Length, Oligo Distance, Domain,
	 * Organism Name,Chromosome Number, Start Position, End Position, 5'
	 * Padding, and 3' Padding
	 * 
	 * @return an XML representation of the Query
	 */
	public String toXML() 
	{
		String output = "";
		//output+= "<oligoCount>"+oligoCount+"</oligoCount>\n";
		
		output += "<Query QueryName=\"" + id + "\">\n";
		//output += "<oligoCount>"+oligoCount+"</oligoCount>\n";
		output += "<oligoLength>" + oligoLength + "</oligoLength>\n";
		output += "<oligoDistance>" + minDistance + "</oligoDistance>\n";
		output += "<Domain>\n";
		output += "<" + domain + ">\n";
		output += "<Organism>" + organism + "</Organism>\n";
		output += "<Chromosome>" + chr + "</Chromosome>\n";
		output += "<startPosition>" + start + "</startPosition>\n";
		output += "<endPosition>" + end + "</endPosition>\n";
		if (domain.equalsIgnoreCase("ucsc")) {
			output += "<Padding5>" + p5 + "</Padding5>\n";
			output += "<Padding3>" + p3 + "</Padding3>\n";
		}
		output += "</" + domain + ">\n";
		output += "</Domain>\n";
		output += "</Query>\n";
		return output;
	}

	private String sequence;
	private String chr;
	private String start;
	private String end;
	private String id;
	private String organism;
	private String p5;
	private String p3;
	@SuppressWarnings("unused")
	private int oligoCount;
	private String domain;
	private int oligoLength;
	private int minDistance;
}
