package solPicker.solpicker_gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import solPicker.job.Query;


/**
 * JPanel that consists of With JLabels and seven JTextFields that allow
 * the user to input all the query information. Additionally there are three
 * buttons: One that reads all the information in the text fields and uses this
 * information to create the appropriate Query object and add it to the list of
 * Queries; A second JButton that removes a Query by prompting the user for the
 * index of the Query to be removed, checking if this is an appropriate index,
 * and removing the appropriate Query if the index is correct; And a third
 * JButton that removes all the Queries from the list of Queries.
 * 
 * When the Text Fields are checked, the user is forced to input valid entries
 * into the fields the correspond to the Chromosome Number, the Start and End
 * Position, the oligo length, and the Query ID. If the user leaves blank the
 * text fields corresponding to the 5' and 3' padding, the domain, the organism
 * name, and the minimum oligo distance, then these fields are given default
 * values. However if the user does enter input into these fields then the GUI
 * makes sure that the user has given valid input.
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: AddQueryPanel.java,v 1.3 2009/10/16 17:47:31 achandra Exp $
 */
public class AddQueryPanel extends JPanel {
	/**
	 * Creates a JPanel with seven JLabel and JTextField pairs and three
	 * JButtons, adds action listeners to the JButtons
	 */
	public AddQueryPanel()
	{
		initialize();

		/**
		 * ActionListener that reads all the text fields, checks if they are
		 * filled properly, creates the appropriate Query Object, and adds it to
		 * the list of Queries.
		 * 
		 * @author Arjun Chandrasekhar
		 */
		class AddQueryListener implements ActionListener {
			/**
			 * Checks if the text fields are filled with valid values, and if
			 * they are then the method creates a Query Object using the values
			 * specified by the user as parameters for the constructor.
			 */
			public void actionPerformed(ActionEvent event) {
				// check if all the fields are filled correctly
				if (fieldsFilledCorrectly()) {
					// retrieve all the Query information from the text fields
					String cNum = cNumField.getText().trim();
					String start = startField.getText().trim();
					String end = endField.getText().trim();
					String id = idField.getText().trim();
					int oligoLength = 60;
					if (!isEmpty(lengthField)) {
						oligoLength = Integer.parseInt(lengthField.getText()
								.trim());
					}
					// construct a Query Object using all the input from the
					// text fields as parameters
					Query q = new Query(cNum, start, end, id, oligoLength);
					// check if the organism field has input, if it does the
					// Query's organism is set equal to the text in the organism
					// field
					if (!isEmpty(organismField)) {
						q.setOrganism(organismField.getText().trim());
					}
					// checks if the 5' padding field has input, if it does the
					// Query's 5' padding is set equal to to the text in the 5'
					// padding field.
					if (!isEmpty(p5Field)) {
						q.set5Padding(p5Field.getText().trim());
					}
					// checks if the 3' padding field has input, if it does the
					// Query's 3' padding is set equal to to the text in the 3'
					// padding field
					if (!isEmpty(p3Field)) {
						q.set3Padding(p3Field.getText().trim());
					}
					// checks if the domain field has input, if if does then the
					// Query's domain is set equal to the text in the domain
					// field
					if (!isEmpty(domainField)) {
						q.setDomain(domainField.getText().trim());
					}
					// checks if the oligo distance field has input, if it does
					// then the Query's minimum distance is set equal to the
					// text in the oligo distance
					// field
					if (!isEmpty(distanceField)) {
						q.setMinDistance(distanceField.getText().trim());
					}
					// add the Query to the list of Queries
					queries.add(q);
					XMLWriter.append(q.toXML());
					// clear out all text from the text fields
					clearQueryFields();
				}
			}
		}
		// add an action lisener to the addQueryButton so that pressing the
		// button allows the user to add a Query
		addQueryButton.addActionListener(new AddQueryListener());

		/**
		 * ActionListener that removes a Query by prompting the user for the
		 * index of the Query to be removed, checking if this is a valid index,
		 * and removing the appropriate Query if the index is appropriate.
		 * 
		 * @author Arjun Chandrasekhar
		 */
		class RemoveQueryListener implements ActionListener {
			/**
			 * Prompts the user for the index of the Query to be removed. The
			 * method checks if this is a valid index by first making sure that
			 * the user has entered a valid number, and then making sure that
			 * the the if the program were to try to remove a Query at the
			 * specified index then no ArrayIndexOutOfBoundsException would be
			 * generated. If the user has entered a valid index, then the
			 * program removes the appropriate Query. If not, the program
			 * continues prompting for input until the user either enters a
			 * valid index or presses cancel on the JOptionPane.
			 */
			public void actionPerformed(ActionEvent event) {
				boolean done = false;
				while (!done) {
					// makes sure that at least one Query has been added. If no
					// Queries have been added, an error message is generated
					if (queries.size() == 0) {
						JOptionPane.showMessageDialog(null, "There aren't any Queries, meaning either you haven't added any yet, or you cleared the Queries " +
								"and haven't added any since them.  How do you expect to remove a Query if there aren't any Queries to remove?");
						done = true;
					} else {
						// prompts the user for the index of the Query that
						// he/she wants to remove
						String input = JOptionPane.showInputDialog("Enter the index of the Query that you want to remove.  Remember, in Java ArrayLists " +
								"the first Object is at index 0, the second Object is at index 1, the 100th Object is at index 99, etc.");
						// checks if the user has pressed cancel, in which case
						// the method terminates
						if (input == null)
							done = true;
						// checks that the user has inputed a real number. An
						// error message is generated if this is not the case
						else if (!isValidNumber(input)) {
							JOptionPane.showMessageDialog(null, "Next time, if you want this to work, it would help if you actually entered a real number");
						} else {
							// parses the input into an integer
							int pos = Integer.parseInt(input);
							// checks to make sure that the user hasn't entered
							// an index that is out of bounds. If the user has
							// entered an index that would
							// cause an ArrayIndexOutOfBoundsException to be
							// thrown, then the method generates an error
							// message
							if (pos >= queries.size()) {
								int x = pos + 1;
								JOptionPane.showMessageDialog(null, "You haven't added " + x + " Queries.  How do you expect to remove a Query that doesn't " +
										"even exist?");
							} else {
								// removes the Query at the specified position
								// and terminates the loop
								queries.remove(pos);
								done = true;
							}
						}
					}
				}
			}
		}
		// adds an ActionListener to the removeQueryButton so that pressing the
		// button allows the user to remove a Query
		removeQueryButton.addActionListener(new RemoveQueryListener());

		class ClearQueriesListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				// clears every Query from the list of Queries
				queries.clear();
			}
		}
		// adds an ActionListener to the clearQueriesButton so that pressing the
		// button allows the user to clear the list of Queries
		clearQueriesButton.addActionListener(new ClearQueriesListener());

		// adds all the JButtons to the final panel
		qButtonsPanel.add(addQueryButton);
		qButtonsPanel.add(removeQueryButton);
		qButtonsPanel.add(clearQueriesButton);
		add(qInfoPanel);
		add(qButtonsPanel);
	}

	/**
	 * initializes all the JComponents, assigns random colors, and adds all the
	 * JLabels and JTextFields to the appropriate panel.
	 */
	private void initialize() {
		// set the Layout to a 2 * 1 Grid Layout
		setLayout(new GridLayout(1, 2));
		setBorder(new TitledBorder(new EtchedBorder(), "Add Query"));
		// initialize all the JComponents
		cNumLabel = new JLabel("Chromosome Number");
		cNumField = new JTextField();
		startLabel = new JLabel("Start Position");
		startField = new JTextField();
		endLabel = new JLabel("End Position");
		endField = new JTextField();
		idLabel = new JLabel("Query ID");
		idField = new JTextField();
		lengthLabel = new JLabel("Oligo Length");
		lengthField = new JTextField();
		organismLabel = new JLabel("Organism");
		organismField = new JTextField();
		p5Label = new JLabel("5' padding");
		p5Field = new JTextField();
		p3Label = new JLabel("3' padding");
		p3Field = new JTextField();
		domainLabel = new JLabel("Domain");
		domainField = new JTextField();
		distanceLabel = new JLabel("Oligo Distance");
		distanceField = new JTextField();

		addQueryButton = new JButton("Add Query");
		removeQueryButton = new JButton("Remove Query");
		clearQueriesButton = new JButton("Clear Queries");

		queries = new ArrayList<Query>();

		qInfoPanel = new JPanel(new GridLayout(10, 2));
		qButtonsPanel = new JPanel(new GridLayout(3, 1));

		if (false)
			assignRandomColors();

		// add all the labels and fields to the appropriate panel
		qInfoPanel.add(cNumLabel);
		qInfoPanel.add(cNumField);
		qInfoPanel.add(startLabel);
		qInfoPanel.add(startField);
		qInfoPanel.add(endLabel);
		qInfoPanel.add(endField);
		qInfoPanel.add(idLabel);
		qInfoPanel.add(idField);
		qInfoPanel.add(lengthLabel);
		qInfoPanel.add(lengthField);
		qInfoPanel.add(organismLabel);
		qInfoPanel.add(organismField);
		qInfoPanel.add(p5Label);
		qInfoPanel.add(p5Field);
		qInfoPanel.add(p3Label);
		qInfoPanel.add(p3Field);
		qInfoPanel.add(domainLabel);
		qInfoPanel.add(domainField);
		qInfoPanel.add(distanceLabel);
		qInfoPanel.add(distanceField);
	}

	/**
	 * Checks if all the text fields are filled properly. The user is obligated
	 * to fill the Chromosome Number, Start Position, End Position, and Oligo
	 * Length Fields with valid numbers, and the Chromosome ID field must be
	 * filled (although it is not necessary to input a number). The user is not
	 * obligated to fill in fields relating to 5' and 3' padding. Oligo
	 * Distance, Genome Browser domain, or Organism name. However, the 5' and 3'
	 * padding fields and the oligo distance fields must contain valid numbers
	 * if they are filled in, and the domain field input must either be "server.ucsc",
	 * "ensemble", or "server.ensembl" if the field is not empty.
	 * 
	 * @return true if the necessary text fields have been filled in and those
	 *         that have been filled in have been given valid input, false
	 *         otherwise
	 */
	private boolean fieldsFilledCorrectly() {
		// checks if the user has input a valid chromosome number, generates an
		// error message if this is not the case
		String cNum = cNumField.getText().trim();
		if (!isValidChromosome(cNum)) {
			JOptionPane.showMessageDialog(null, "Invalid Chromosome Number");
			return false;
		}
		// checks if the user has input a valid start position, generates an
		// error message if this is not the case
		String start = startField.getText().trim();
		if (!isValidNumber(start)) {
			JOptionPane.showMessageDialog(null, "Invalid Start Position");
			return false;
		}
		// checks if the user has input a valid end position, generates an error
		// message if this is not the case
		String end = endField.getText().trim();
		if (!isValidNumber(end)) {
			JOptionPane.showMessageDialog(null, "Invalid End Position");
			return false;
		}
		int startPos = Integer.parseInt(start);
		int endPos = Integer.parseInt(end);
		// checks that the start position is smaller than the end position,
		// generates an error message if this is not the case
		if (startPos > endPos) {
			JOptionPane.showMessageDialog(null, "Invalid Start and End Positions: Start Position must be smaller than End Position");
			return false;
		}
		// checks if the user has input a valid Query ID, generates an error
		// message if this is not the case
		String id = idField.getText().trim();
		if(id.equals(""))
		{
			JOptionPane.showMessageDialog(null, "Enter a Query ID");
			return false;
		}
		// checks if the inputed Configuration ID is a duplicate ID, generates
		// an error messge if it is a duplicate
		if (isDuplicateID(id)) {
			JOptionPane.showMessageDialog(null, "Invalid ID Number: You Cannot Enter Duplicate ID Numbers");
			return false;
		}
		// checks if the user has input a valid oligo length, generates an error
		// message if this is not the case
		String length = lengthField.getText().trim();
		if (!length.equals("") && !isValidNumber(length)) {
			JOptionPane.showMessageDialog(null, "Invalid Oligo Length");
			return false;
		}
		// if the organism field is filled, then the method checks that it has a
		// valid input. An error message is generated if the input is invalid
		String organism = organismField.getText().trim();
		if (!organism.equals("") && !isValidOrganism(organism)) 
		{
			return false;
		}
		// if the 5' padding field is filled, then the method checks that it has
		// a valid input. An error message is generated if the input is invalid
		String p5 = p5Field.getText().trim();
		if (!p5.equals("") && !isValidNumber(p5)) {
			JOptionPane.showMessageDialog(null, "Invalid 5' Padding");
			return false;
		}
		// if the 3' padding field is filled, then the method checks that it has
		// a valid input. An error message is generated if the input is invalid
		String p3 = p3Field.getText().trim();
		if (!p3.equals("") && !isValidNumber(p3)) {
			JOptionPane.showMessageDialog(null, "Invalid 3' Padding");
			return false;
		}
		// if the domain field is filled, then the method checks that it has a
		// valid input. An error message is generated if the input is invalid
		String domain = domainField.getText().trim();
		if (!domain.equalsIgnoreCase("") && !isValidDomain(domain)) {
			JOptionPane.showMessageDialog(null, "Invalid Domain");
			return false;
		}
		// if the oligo distance field is filled, then the method checks that it
		// has a valid input. An error message is generated if the input is
		// invalid
		String distance = distanceField.getText().trim();
		if (!distance.equals("") && !isValidNumber(distance)) {
			JOptionPane.showMessageDialog(null, "Invalid Oligo Distance");
		}
		// returns true if none of the tests failed
		return true;
	}

	/**
	 * Checks to see if a JTextField is devoid of text
	 * 
	 * @param field
	 *            the Field to be checked for emptiness
	 * @return true if the text field is empty, false if not
	 */
	private boolean isEmpty(JTextField field) {
		// checks if the the text is equal to an empty string
		return field.getText().trim().equals("");
	}

	/**
	 * Checks if a the domain inputed is a valid domain. At the time of this
	 * writing the only valid domains were server.ucsc and ensemble
	 * 
	 * @param domain
	 *            the domain inputed by the user
	 * @return true if the user has inputed either "server.ucsc", "ensemble", or
	 *         "server.ensembl"
	 */
	private boolean isValidDomain(String domain) {
		// the list of valid domains
		String[] domains = {"Ensembl", "UCSC" };
		// loop that goes checks if the parameter
		for (int i = 0; i < domains.length; i++) {
			//
			String dom = domains[i];
			if (dom.equalsIgnoreCase(domain))
				return true;
		}
		return false;
	}

	/**
	 * Checks if there are any chromosomes that have already been added and that
	 * have the same ID as the Query ID inputed by the user
	 * 
	 * @param id
	 *            the ID inputed by the user
	 * @return true if any already existing Queries have the same ID as the ID
	 *         inputed by the user, false if the user has inputed a unique ID
	 */
	private boolean isDuplicateID(String id) {
		for (int i = 0; i < queries.size(); i++) {
			if (queries.get(i).getID().equals(id))
				return true;
		}
		return false;
	}

	/**
	 * Checks if the User has inputed a valid chromosome number. A Chromosome
	 * number is valid if it is either or Y, or if it is a number greater than 0
	 * and less than 23
	 * 
	 * @param cNum
	 *            the Chromosome Number inputed by the user
	 * @return true if the user has inputed a valid chromosome number, false if
	 *         not
	 */
	private boolean isValidChromosome(String cNum) {
		if (cNum.equalsIgnoreCase("X") || cNum.equalsIgnoreCase("Y"))
			return true;
		try {
			int x = Integer.parseInt(cNum);
			for (int i = 1; i <= 22; i++) {
				if (x == i)
					return true;
			}
		} catch (NumberFormatException n) {
			return false;
		}
		return false;
	}

	/**
	 * Checks to see if a given String is a valid number. This method is used to
	 * check whether the user has inputed valid numbers in all the fields that
	 * require numbers
	 * 
	 * @param num
	 *            the String input that is supposed to be a number.
	 * @return whether or not the String is a valid number
	 */
	private boolean isValidNumber(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException n) {
			return false;
		}
	}

	/**
	 * Checks if the organism inputed by the user is valid. To be a valid
	 * organism, the first letter must be capitalized, every subsequent letter
	 * must be lowercase, and an underscore separating the genus and species
	 * must be present.
	 * 
	 * @param organism
	 *            the organism inputed by the user
	 * @return whether or not the user has inputed a valid organism
	 */
	private boolean isValidOrganism(String organism) {
		if (organism.length() < 2) {
			JOptionPane.showMessageDialog(null, "Invalid Organism");
			return false;
		}
		String first = organism.substring(0, 1);
		if (!isUpperCase(first)) {
			JOptionPane.showMessageDialog(null,
					"Invalid Organism: First Letter must be Upper Case");
			return false;
		}
		for (int i = 1; i < organism.length(); i++) {
			String str = organism.charAt(i) + "";
			if (!str.equals("_") && isUpperCase(str)) {
				JOptionPane
						.showMessageDialog(null,
								"Invalid Organism: Every Letter after the first Letter must be LowerCase");
				return false;
			}

		}
		return true;
	}

	/**
	 * Checks if a String is Capitalizes
	 * 
	 * @param str
	 *            the String to be checked for capitalization
	 * @return true if the parameter is uppercase, false if it is lowercase
	 */
	private boolean isUpperCase(String str) {
		String strUpper = str.toUpperCase();
		return str.equals(strUpper);
	}

	/**
	 * For each component contained in the panel (including the class itself),
	 * the method sets the foreground and background colors to random colors
	 */
	private void assignRandomColors() {
		setRandomColor(cNumLabel);
		setRandomColor(cNumField);
		setRandomColor(startLabel);
		setRandomColor(startField);
		setRandomColor(endLabel);
		setRandomColor(endField);
		setRandomColor(idLabel);
		setRandomColor(idField);
		setRandomColor(lengthLabel);
		setRandomColor(lengthField);
		setRandomColor(organismLabel);
		setRandomColor(organismField);
		setRandomColor(p5Label);
		setRandomColor(p5Field);
		setRandomColor(p3Label);
		setRandomColor(p3Field);
		setRandomColor(domainLabel);
		setRandomColor(domainField);
		setRandomColor(distanceLabel);
		setRandomColor(distanceField);
		setRandomColor(addQueryButton);
		setRandomColor(removeQueryButton);
		setRandomColor(clearQueriesButton);
		setRandomColor(qInfoPanel);
		setRandomColor(qButtonsPanel);
		setRandomColor(this);
	}

	/**
	 * Uses a Random Number Generator to generate four random floating point
	 * numbers (representing red, green, blue, and alpha values), and uses
	 * thesee floats variables to generate a Color Object
	 * 
	 * @return a Color Object with four random floating point numbers as red,
	 *         green, blue, and alpha values
	 */
	private Color pickRandomColor() {
		Random gen = new Random();
		float r = gen.nextFloat();
		float g = gen.nextFloat();
		float b = gen.nextFloat();
		float a = gen.nextFloat();
		return new Color(r, g, b, a);
	}

	/**
	 * Takes a JComponent as a parameter and sets its foreground and background
	 * to random colors
	 * 
	 * @param comp
	 *            the JComponent whose foreground and background colors are to
	 *            be set
	 */
	private void setRandomColor(JComponent comp) {
		comp.setForeground(pickRandomColor());
		comp.setBackground(pickRandomColor());
	}

	/**
	 * Retrieves the ArrayList containing all the Queries that have been added
	 * by the user
	 * 
	 * @return the list of queries that have been added by the user
	 */
	public static ArrayList<Query> getQueries() {
		return queries;
	}

	/**
	 * Clears all the text from all the text fields
	 */
	private void clearQueryFields() {
		cNumField.setText("");
		startField.setText("");
		endField.setText("");
		idField.setText("");
		lengthField.setText("");
		organismField.setText("");
		p5Field.setText("");
		p3Field.setText("");
		domainField.setText("");
		distanceField.setText("");
	}

	/**
	 * JLabel labelling the field where the user may input the Chromosome Number
	 * of the Query that he/she is adding
	 */
	private JLabel cNumLabel;
	/**
	 * JTextField where the user may input the Chromosome Number of the Query
	 * that he/she is adding
	 */
	private JTextField cNumField;
	/**
	 * JLabel labelling the field where the user may input the start location of
	 * the Query that he/she is adding
	 */
	private JLabel startLabel;
	/**
	 * JTextField where the user may input the start location of the Query that
	 * he/she is adding
	 */
	private JTextField startField;
	/**
	 * JLabel labelling the start location of the Query that he/she is adding
	 */
	private JLabel endLabel;
	/**
	 * JTextField where the user may input the end location of the Query that
	 * he/she is adding
	 */
	private JTextField endField;
	/**
	 * JLabel labelling the field where the user may input the unique ID of the
	 * Query that he/she is adding
	 */
	private JLabel idLabel;
	/**
	 * JTextField where the user may input the unique ID of the Query that
	 * he/she is adding
	 */
	private JTextField idField;
	/**
	 * JLabel labelling the field where the user may input the length of the
	 * Oligos that he/she wants to create from the Query that he/she is adding
	 */
	private JLabel lengthLabel;
	/**
	 * JTextField where the user may input the length of the Oligos that he/she
	 * wants to create from the Query that he/she is adding
	 */
	private JTextField lengthField;
	/**
	 * JLabel labelling the field where the user may input the genus-species
	 * name of the organism for the Query that he/she is adding
	 */
	private JLabel organismLabel;
	/**
	 * JTextField where the user may input the genus-species name of the
	 * organism for the Query that he/she is adding
	 */
	private JTextField organismField;
	/**
	 * JLabel labelling the field where the user may input the 5' padding of the
	 * Query that he/she is adding
	 */
	private JLabel p5Label;
	/**
	 * JTextField where the user may input the 5' padding of the Query that
	 * he/she is adding
	 */
	private JTextField p5Field;
	/**
	 * JLabel labelling the field where the user may input the 3' padding of the
	 * Query that he/she is adding
	 */
	private JLabel p3Label;
	/**
	 * JTextField where the user may input the 3' padding of the Query that
	 * he/she is adding
	 */
	private JTextField p3Field;
	/**
	 * JLabel labelling the field where the user may input the domain from which
	 * the program should retrieve sequence information for the Query that
	 * he/she is adding
	 */
	private JLabel domainLabel;
	/**
	 * JTextField where the user may input the domain from which the program
	 * should retrieve sequence information for the Query that he/she is adding
	 */
	private JTextField domainField;
	/**
	 * JLabel labelling the field where the user may input the minmum distance
	 * between the Oligos that are to be created from the Query that he/she is
	 * adding
	 */
	private JLabel distanceLabel;
	/**
	 * JTextField where the user may input the minmum distance between the
	 * Oligos that are to be created from the Query that he/she is adding
	 */
	private JTextField distanceField;

	/**
	 * JButton that allows the user to add a Query to the list of Queries
	 */
	private JButton addQueryButton;
	/**
	 * JButton that allows the user to remove a Query from the list of Queries
	 */
	private JButton removeQueryButton;
	/**
	 * JButton that allows the user to clear the list of Queries
	 */
	private JButton clearQueriesButton;

	/**
	 * The list of Queries that the user has added
	 */
	private static ArrayList<Query> queries;

	/**
	 * JPanel containing all the labels and fields that allow the user to input
	 * Query information
	 */
	private JPanel qInfoPanel;
	/**
	 * JPanel containing all the buttons that allow the user ti add and remove
	 * Queries
	 */
	private JPanel qButtonsPanel;

	private static final long serialVersionUID = 606029470816850450L;
}
