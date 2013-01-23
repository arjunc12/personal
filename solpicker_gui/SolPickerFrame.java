package solPicker.solpicker_gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * $Id: SolPickerFrame.java,v 1.5 2009/10/14 21:07:15 achandra Exp $ Consists of
 * a Frame that contains four panels that allow the user to add queries, add
 * configurations, add jobs, and perform jobs.
 * 
 * The program SolPicker retrieves a sequence specified by the user from a web
 * server, creates oligos, and filters those oligos based on a set of
 * parameters, returning a list of valid and discarded oligos.
 * 
 * Query: required, holds the information about the target query and its oligos.
 * A Query name is necessary, as it identifies the query and allows the solPicker.job to
 * match configurations with queries. It can be in the form of a name, a gene
 * name, a number, et cetera.
 * 
 * Oligo Length: optional, dictates the number of nucleotide bases in each
 * oligo. The default oligo length is 60.
 * 
 * Oligo distance: required, represents the distance between the first base in
 * one oligo and the first base in the next. The larger the oligo distance, the
 * smaller the final set of oligos, and the less thorough the oligo coverage
 * 
 * Domain: optional, determines the web server that will provide the sequence
 * information to create the oligos. The researcher may choose between the
 * University of California Santa Cruz Genome Browser and the Ensembl Genome
 * Browser. The default domain is Ensembl.
 * 
 * Organism: optional, allows the researcher to specify an organism whose
 * sequences will be retrieved from the web server. The default organism is a
 * human. Below is a table showing the formatting of organism names for each
 * browser. It is essential to enter the organism as shown below, in the format
 * necessary for the desired web server.
 * 
 * Each domain supports a much more extensive list of organisms than is present
 * above. The user may refer to the genome browsers for information on how to
 * identify an organism not included here. The user must input the organism
 * information in the above form for the program to execute correctly.
 * 
 * Chromosome: required, indicates to which chromosome the sequence belongs. For
 * humans, acceptable chromosomes include any integer between 1 and 22, as well
 * as X and Y (not case sensitive). For all other animals, chromosomes must be
 * represented by an integer within the scope of the animal’s genome (see
 * above).
 * 
 * Start position: required, indicates where on the specified chromosome the
 * sequence begins. Be careful to include a valid position, or the program could
 * fail. Additionally, the telomere and centromere regions of chromosomes
 * include long strands of unknown bases, as these regions are extremely
 * difficult to sequence. Be careful not to request sequences primarily in these
 * regions or the program may not function as successfully as desired.
 * 
 * End position: required, indicates where on the specified chromosome the
 * sequence ends. This value must be larger than the start position, or the
 * program will return an error. Also, for the organism and the chromosome, this
 * must be a valid position or the program will fail to run.
 * 
 * 3 prime padding: optional and only an option under the UCSC domain. The
 * default is 0.
 * 
 * 5 prime padding: optional and only an option under the UCSC domain. The
 * default is 0.
 * 
 * 
 * Configuration: stores information about filters to which the oligos will be
 * subject. A Configuration ID is obligatory, and must be in the form of an
 * integer.
 * 
 * Filters: No filters are required, but some are very strongly recommended.
 * Once oligos are created from the target sequence, they are passed through a
 * series of filters designed to pare the number oligos down before sending them
 * to NCBI’s Basic Local Alignment Search Tool (BLAST) and ultimately to produce
 * the most ideal set of oligo probes. Following is a list of filters the
 * current version of the program supports, the function of each, and the
 * parameters of each.
 * 
 * Lowercase: While not required, this filter is very strongly recommended. The
 * genome browsers where the sequences are retrieved mask repeated regions in
 * lowercase letters. This filter removes oligos that have too many repeated
 * strings. If this filter is not used, the program may collapse when the oligos
 * are sent to BLAST because the server will find too many hits to process. The
 * user may specify a maximum number of lowercases allowed in each oligo, but if
 * they do not, the default is 0.
 * 
 * N Count: Many regions of the genome are extremely difficult to sequence,
 * notably the centromere and telomere regions of each chromosome, and thus
 * still contain long strings of unknown bases. The genome browsers mast these
 * unknown bases with the letter n, and this filter removes oligos that have to
 * many n’s, as it is not practical to pick oligos with many unknowns. The user
 * may specify a maximum n count for each oligo, and the default is 0.
 * 
 * GC Percent: The GC content of an oligo affects the experimental conditions of
 * the hybridization process. It is ideal to have relatively even GC contents
 * across all oligos to standardize the process of DNA capture. Furthermore,
 * each organism has a different balance of AT and GC, so researchers may want
 * to dial the GC content based on their target organism. This filter filters
 * oligos based on a minimum and a maximum GC percent, values that can be set by
 * the user. The default minimum is 45% and the default maximum is 60%.
 * 
 * Enzyme: Enzymes are often used to cut the DNA before hybridization. An oligo
 * that compliments the enzyme used in either the forward or reverse direction
 * is useless, as all segments that would bond with that oligo will have been
 * cut. The researcher specifies an enzyme name (optional), and an enzyme
 * sequence (required) and this filter removes oligos with the enzyme.
 * 
 * 
 * TM Score: The TM Score filter calculates the temperature of each oligo by
 * using a chemical algorithm based on the GC content. If the oligo is not
 * within a specified minimum or maximum, the temperature score is 4. The
 * default lower limit is 77 degrees Fahrenheit and the upper limit is 81
 * degrees Fahrenheit.
 * 
 * Homopolymer Score: This filter assigns each oligo a score based on the
 * prevalence of homopolymers (subsequences of 5 or more consecutive identical
 * nucleotides). The score is calculated by adding 11 to the filter score for
 * are any homopolymers that are 7 bases long or more, and adding 2 to the score
 * for every homopolymer that is 5 or 6 bases long. To use this filter, the user
 * must specify a maximum homopolymer score.
 * 
 * Base Fraction Score: Calculates the percent distribution of each of the
 * nucleotide bases, and scored oligos based on the equality of the
 * distributions: the more uneven the distribution, the higher the base fraction
 * score. For each base whose percent distribution is not between 25% and 35%,
 * this filter adds one point to the base fraction score. Users must indicate a
 * maximum to use this filter.
 * 
 * Fail Score: This filter combines the temperature, homoploymer score, and base
 * fraction score to give each oligo a score indicating how “ideal” it is. It is
 * the Agilent method for choosing oligos. The user may specify a maximum
 * failscore, otherwise it is set at 10.
 * 
 * 
 * Job: Each solPicker.job pairs a query with a configuration. This allows the user to run
 * multiple filter configurations on the same query, or multiple queries under
 * the same configuration.
 * 
 * Output: The program outputs a file containing the query sequence, a file
 * containing all oligos created from that sequence, a list of valid oligos
 * (identified by a unique ID and sequence), as well as a separate file
 * containing discarded oligos. Each oligo keeps track of its value for each
 * filter, and the original parameters are stored in the valid and invalid oligo
 * files.
 * @author Arjun Chandrasekhar
 * @see <a href="../../Pictures/OrganismChart.pdf">Organism Identifiers</a>
 */

public class SolPickerFrame {
	/*
	 * static { try { System.setProperty("apple.laf.useScreenMenuBar", "true");
	 * System.setProperty("com.apple.mrj.application.apple.menu.about.name",
	 * "SolPicker");
	 * UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	 * 
	 * } catch (ClassNotFoundException e) {
	 * System.out.println("ClassNotFoundException: " + e.getMessage()); } catch
	 * (InstantiationException e) {
	 * System.out.println("InstantiationException: " + e.getMessage()); } catch
	 * (IllegalAccessException e) {
	 * System.out.println("IllegalAccessException: " + e.getMessage()); } catch
	 * (UnsupportedLookAndFeelException e) {
	 * System.out.println("UnsupportedLookAndFeelException: " + e.getMessage());
	 * } }
	 */
	public void initializeGUI(){
		JFrame frame = new JFrame();
		System.err.println(System.getProperty("user.dir"));
		String directory = JOptionPane.showInputDialog("Enter the directory where u want all this shit 2 go to"); 
		File dir = new File(directory); 
		File xml = new File(dir, "SolPicker XML");
		//JFileChooser chooser = new JFileChooser();
		//chooser.showOpenDialog(null);
		//File xml = chooser.getSelectedFile();
		System.err.println(xml.getAbsolutePath());
		XMLWriter.setFile(xml);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("SolPicker GUI");
		AddQueryPanel queryPanel = new AddQueryPanel();
		AddConfigurationPanel configPanel = new AddConfigurationPanel();
		AddJobPanel jobPanel = new AddJobPanel();
		ExecutePanel exPanel = new ExecutePanel();
		TextFilePanel txtPanel = new TextFilePanel();
		JPanel panel1 = new JPanel(new GridLayout(2, 1));
		panel1.add(queryPanel);
		panel1.add(configPanel);
		frame.add(panel1, BorderLayout.CENTER);
		JPanel panel2 = new JPanel(new GridLayout(1, 3));
		panel2.add(jobPanel);
		panel2.add(txtPanel);
		panel2.add(exPanel);

		frame.add(panel2, BorderLayout.SOUTH);
		frame.setSize(1000, 1000);
		if (false) {
			Random gen = new Random();
			frame.setForeground(new Color(gen.nextFloat(), gen.nextFloat(), gen.nextFloat(), gen.nextFloat()));
			frame.setBackground(new Color(gen.nextFloat(), gen.nextFloat(), gen.nextFloat(), gen.nextFloat()));
		}
		frame.setVisible(true);
	}
}
