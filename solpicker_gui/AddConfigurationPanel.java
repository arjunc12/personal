package solPicker.solpicker_gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import solPicker.filters.*;
import solPicker.job.Configuration;


/**
 * $Id: AddConfigurationPanel.java,v 1.4 2009/10/14 21:07:15 achandra Exp $
 * JPanel that allows the user to add a new Configuration Object to the program.
 * The user can use check boxes to select the filters that he wants present in
 * his configuration. The user can also use text fields to specify the
 * parameters for these filters. If the text fields are left blank, then default
 * values are used for the filters
 * 
 * @author Arjun Chandrasekhar
 */
public class AddConfigurationPanel extends JPanel 
{
	/**
	 * Default Constructor; Initializes all the labels, fields, and buttons.
	 * Adds ActionListeners to the enzyme buttons that allow the user to add
	 * enzymes to the list of enzymes, remove enzymes, and clear the enzyme
	 * list. Adds ActionListener to the Add Configuration Button that allows the
	 * user to add a Configuration Object to the list of Configurations
	 */

	public AddConfigurationPanel()
	{
		initialize();
		setLayout(new GridLayout(8, 2));

		/**
		 * ActionListener that checks if the user has inputed a valid Enzyme
		 * Sequence and adds the Enzyme to the ArrayList of Enzymes
		 * 
		 * @author Arjun Chandrasekhar
		 */
		class AddEnzymeListener implements ActionListener 
		{
			/**
			 * Reads the Enzyme Name and Enzyme Sequence Text Fields. Checks if
			 * the sequence field is filled with a valid sequence (all letters
			 * must be either A,C,T, or G, and all the letters must be upper
			 * case). If the sequence is valid, then the method adds the Enzyme
			 * to the list of Enzymes
			 */
			public void actionPerformed(ActionEvent event) 
			{
				String name = enzymeNameField.getText().trim();
				String sequence = enzymeSequenceField.getText().trim();
				if (isValidSequence(sequence)) 
				{
					enzymes.add(name + ":" + sequence);
					clearEnzymeFields();
				}
			}
		}
		addEnzymeButton.addActionListener(new AddEnzymeListener());

		/**
		 * ActionListener that allows the user to remove an enzyme from the list
		 * of enzymes
		 * @author Arjun Chandrasekhar
		 */
		class RemoveEnzymeListener implements ActionListener 
		{
			/**
			 * Allows the user to remove an enzyme from the list of enzymes.
			 * First prompts the user for the index of the enzyme that is to be
			 * removed, then checks if the input is a valid number and that the
			 * there actually is an enzyme stored at that index. If the input is
			 * valid, then the appropriate enzyme is removed. If the input is
			 * invalid, then the method continues prompting the user for input
			 * until he or she either enters valid input or presses cancel.
			 */
			public void actionPerformed(ActionEvent event) 
			{
				boolean done = false;
				while (!done) 
				{
					if (enzymes.size() == 0) 
					{
						JOptionPane.showMessageDialog(null,"There aren't any Enzymes, meaning either you haven't added any yet, or you cleared the " +
								"Enzymes and haven't added any since them.  How do you expect to remove an Enzyme if there aren't any Enzymes to " +
						"remove?");
						done = true;
					}
					else 
					{
						String input = JOptionPane.showInputDialog("Enter the index of the Enzyme that you want to remove.  Remember, in "
								+ "Java ArrayLists the first Object is at index 0, the second Object is at index 1, the 100th Object is at "
								+ "index 99, etc.");
						if (input == null)
							done = true;
						else if(!isValidInteger(input))
						{
							JOptionPane.showMessageDialog(null, "Next time, if you want this to work, it would help if you actually entered a " +
							"real number");
						}
						else {
							int pos = Integer.parseInt(input);
							int x = pos + 1;
							if(pos >= enzymes.size())
								JOptionPane.showMessageDialog(null, "You haven't added " + x + " Enzymes.  How do you expect to remove an " +
										"Enzyme that " +
								"doesn't even exist?");
							else 
							{
								enzymes.remove(pos);
								done = true;
							}
						}
					}
				}
			}
		}
		removeEnzymeButton.addActionListener(new RemoveEnzymeListener());

		/**
		 * ActionListener that clears the list of enzymes (thus making it empty)
		 * 
		 * @author Arjun Chandrasekhar
		 */
		class ClearEnzymesListener implements ActionListener 
		{
			/**
			 * removes all enzymes from the list of enzymes
			 */
			public void actionPerformed(ActionEvent event)
			{
				enzymes.clear();
			}
		}
		clearEnzymesButton.addActionListener(new ClearEnzymesListener());

		enzymeInfoPanel.add(enzymeNameLabel);
		enzymeInfoPanel.add(enzymeNameField);
		enzymeInfoPanel.add(enzymeSequenceLabel);
		enzymeInfoPanel.add(enzymeSequenceField);

		enzymeButtonsPanel.add(addEnzymeButton);
		enzymeButtonsPanel.add(removeEnzymeButton);
		enzymeButtonsPanel.add(clearEnzymesButton);

		finalEnzymePanel.add(enzymeInfoPanel);
		finalEnzymePanel.add(enzymeButtonsPanel);
		add(enzymeFilterCheck);
		add(finalEnzymePanel);

		failPanel.add(maxFailLabel);
		failPanel.add(maxFailField);
		add(failScoreCheck);
		add(failPanel);

		gcPanel.add(minGCLabel);
		gcPanel.add(minGCField);
		gcPanel.add(maxGCLabel);
		gcPanel.add(maxGCField);
		add(gcCheck);
		add(gcPanel);

		nPanel.add(maxNLabel);
		nPanel.add(maxNField);
		add(nCheck);
		add(nPanel);

		lowerPanel.add(maxLowerLabel);
		lowerPanel.add(maxLowerField);
		add(lowerCheck);
		add(lowerPanel);

		tmPanel.add(minTMLabel);
		tmPanel.add(minTMField);
		tmPanel.add(maxTMLabel);
		tmPanel.add(maxTMField);
		add(tmCheck);
		add(tmPanel);

		homopolymerPanel.add(maxHomopolymerLabel);
		homopolymerPanel.add(maxHomopolymerField);
		add(homopolymerCheck);
		add(homopolymerPanel);

		addConfigPanel.add(configIDLabel);
		addConfigPanel.add(configIDField);

		/**
		 * ActionListener that checks which filters are selected and adds the
		 * appropriate filters.
		 * 
		 * @author Arjun Chandrasekhar
		 */
		class addConfigListener implements ActionListener 
		{
			/**
			 * Checks which filters are selected, then creates and adds
			 * corresponding Filter Objects. The method first checks that all
			 * the fields that are filled in contain valid numbers. If a Filter
			 * is selected and its corresponding field(s) is left blank then the
			 * filter is given a default parameter.
			 */
			public void actionPerformed(ActionEvent event) 
			{
				if (fieldsFilledProperly())
				{
					Configuration c = new Configuration(configIDField.getText().trim());
					if (lowerCheck.isSelected()) 
					{
						int max = 0;
						if (!isEmpty(maxLowerField))
							max = Integer.parseInt(maxLowerField.getText().trim());
						c.addFilter(new LowerCaseFilter(max));
					}
					if (gcCheck.isSelected()) 
					{
						double min = 45;
						double max = 60;
						if (!isEmpty(minGCField))
							min = Double.parseDouble(minGCField.getText()
									.trim());
						if (!isEmpty(maxGCField))
							max = Double.parseDouble(maxGCField.getText()
									.trim());
						c.addFilter(new GCFilter(min, max));
					}
					if (failScoreCheck.isSelected()) 
					{
						int max = 0;
						if (!isEmpty(maxFailField))
							max = Integer.parseInt(maxFailField.getText()
									.trim());
						c.addFilter(new FailScoreFilter(max));
					}
					if (enzymeFilterCheck.isSelected()) 
					{
						c.addFilter(new EnzymeFilter(enzymes));
					}
					if (tmCheck.isSelected()) 
					{
						double min = 77;
						double max = 81;
						if (!isEmpty(minTMField))
							min = Double.parseDouble(minTMField.getText()
									.trim());
						if (!isEmpty(maxTMField)) 
						{
							max = Double.parseDouble(maxTMField.getText()
									.trim());
						}
						c.addFilter(new TMFilter(min, max));
					}
					if (homopolymerCheck.isSelected()) 
					{
						int max = 0;
						if (!isEmpty(maxHomopolymerField)) 
						{
							max = Integer.parseInt(maxHomopolymerField
									.getText().trim());
						}
						c.addFilter(new HomoPolymerFilter(max));
					}
					if (nCheck.isSelected()) 
					{
						int max = 20;
						if (!isEmpty(maxNField))
							max = Integer.parseInt(maxNField.getText().trim());
						c.addFilter(new NCountFilter(max));
					}
					configs.add(c);
					XMLWriter.append(c.toXML());
					clearFields();
				}
			}
		}
		addConfigButton.addActionListener(new addConfigListener());
		add(addConfigPanel);
		add(addConfigButton);
	}

	/**
	 * Checks if an enzyme sequence is a valid sequence. A valid sequence
	 * contains only Capital letters and only contains the letters A, C, T, and
	 * G.
	 * 
	 * @param seq
	 *            the Enzyme sequence
	 * @return true if the enzyme sequence is a valid sequence
	 */
	private boolean isValidSequence(String seq) 
	{
		for (int i = 0; i < seq.length(); i++)
		{
			char ch = seq.charAt(i);
			if (ch != 'A' && ch != 'C' && ch != 'G' && ch != 'T') 
			{
				if (ch == 'a' || ch == 'c' || ch == 'g' || ch == 't') 
				{
					JOptionPane.showMessageDialog(null, "Make sure all the Sequence Characters are Upper Case");
					return false;
				} 
				else 
				{
					JOptionPane.showMessageDialog(null, "Invalid Sequence: All Characters must be either A, C, T, or G");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * initializes all the JComponents that make up the final panel
	 */
	private void initialize() 
	{
		setBorder(new TitledBorder(new EtchedBorder(), "Add Configuration"));
		enzymes = new ArrayList<String>();
		configs = new ArrayList<Configuration>();

		enzymeFilterCheck = new JCheckBox("Filter By Enzyme");
		enzymeNameLabel = new JLabel("Enzyme Name");
		enzymeNameField = new JTextField();
		enzymeSequenceLabel = new JLabel("Enzyme Sequence");
		enzymeSequenceField = new JTextField();
		addEnzymeButton = new JButton("Add Enzyme");
		removeEnzymeButton = new JButton("Remove Enzyme");
		clearEnzymesButton = new JButton("Clear Enzymes");

		failScoreCheck = new JCheckBox("Filter by Fail Score");
		maxFailLabel = new JLabel("Max Fail Score");
		maxFailField = new JTextField();

		gcCheck = new JCheckBox("Filter by GC Percent");
		minGCLabel = new JLabel("Min GC Percent");
		minGCField = new JTextField();
		maxGCLabel = new JLabel("Max GC Percent");
		maxGCField = new JTextField();

		nCheck = new JCheckBox("Filter By N Count");
		maxNLabel = new JLabel("Max N Count");
		maxNField = new JTextField();

		lowerCheck = new JCheckBox("Filter By Lower Case");
		maxLowerLabel = new JLabel("Max Lower Case");
		maxLowerField = new JTextField();

		enzymeInfoPanel = new JPanel(new GridLayout(2, 2));
		enzymeButtonsPanel = new JPanel(new GridLayout(3, 1));
		finalEnzymePanel = new JPanel(new GridLayout(1, 2));

		failPanel = new JPanel(new GridLayout(1, 2));
		gcPanel = new JPanel(new GridLayout(2, 2));
		nPanel = new JPanel(new GridLayout(1, 2));
		lowerPanel = new JPanel(new GridLayout(1, 2));

		configIDLabel = new JLabel("Configuration ID");
		configIDField = new JTextField();
		addConfigButton = new JButton("Add Configuration");
		addConfigPanel = new JPanel(new GridLayout(1, 2));

		tmCheck = new JCheckBox("Filter by TM Score");
		minTMLabel = new JLabel("Min Tm Score");
		minTMField = new JTextField();
		maxTMLabel = new JLabel("Max TM Score");
		maxTMField = new JTextField();
		tmPanel = new JPanel(new GridLayout(2, 2));

		homopolymerCheck = new JCheckBox("Filter by Homopolymer Score");
		maxHomopolymerLabel = new JLabel("Max Homopolymer Score");
		maxHomopolymerField = new JTextField();
		homopolymerPanel = new JPanel(new GridLayout(1, 2));

		if(false)
			assignRandomColors();
	}

	/**
	 * Checks if a text field is empty
	 * 
	 * @param field
	 *            the text field to be checked for emptiness
	 * @return true if the text field is empty, false if it contains text
	 */
	private boolean isEmpty(JTextField field) 
	{
		return field.getText().trim().equals("");
	}

	/**
	 * Checks if a configuration ID is a duplicate ID (meaning there is a
	 * pre-existing Configuration that has that same ID)
	 * 
	 * @param id
	 *            the ID to be compared against all the Configuration ID's
	 *            currently in existence
	 * @return true is there is a Configuration in existence whose ID is
	 *         identical to the parameter
	 */
	private boolean isDuplicateID(String id)
	{
		for (int i = 0; i < configs.size(); i++)
		{
			if (configs.get(i).getID().equalsIgnoreCase(id)) 
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Makes sure that all the text fields have proper input values. The user is
	 * permitted to leave fields blank (which would cause the program to use
	 * predetermined default values), but any fields that are filled must have
	 * valid numbers. For the Minimum and Maximum GC Percent fields, the number
	 * entered into the minimum GC percent field must be smaller than the number
	 * the number entered into the maximum GC percent field, and the user is not
	 * permitted to enter a value for one of the two fields. The Configuration
	 * ID field must have a valid, non-duplicate ID number entered; the user
	 * cannot leave it blank.
	 * 
	 * @return true if all the necessary fields are filled, and all the fields
	 *         that are filled contain valid input values
	 */
	private boolean fieldsFilledProperly() 
	{
		if (failScoreCheck.isSelected() && !isEmpty(maxFailField)) 
		{
			if (!isValidInteger(maxFailField.getText().trim())) 
			{
				JOptionPane.showInputDialog(null, "Invalid Max Fail Score: Input is not a valid integer");
				return false;
			}
			if (Integer.parseInt(maxFailField.getText().trim()) < 0) 
			{
				JOptionPane.showMessageDialog(null, "Invalid Max Fail Score: Input must be a positive number");
				return false;
			}
		}
		if (gcCheck.isSelected())
		{
			if (!isEmpty(minGCField) && !isValidDouble(minGCField.getText().trim())) 
			{
				JOptionPane.showMessageDialog(null, "Invalid Min GC Percent: Input is not a valid Floating-Point Number");
				return false;
			}
			if (!isEmpty(maxGCField) && !isValidDouble(maxGCField.getText().trim())) 
			{
				JOptionPane.showMessageDialog(null, "Invalid Max GC: Input is not a valid Floating-Point Number");
				return false;
			}
			if ((!isEmpty(minGCField) && isEmpty(maxGCField)) || (isEmpty(minGCField) && !isEmpty(maxGCField))) 
			{
				JOptionPane.showMessageDialog(null, "Invalid GC Percent Input: Either Input Values for both fields or Input Values for Neither Field");
				return false;
			}
			if(!isEmpty(minGCField) && !isEmpty(maxGCField))
			{
				double minGC = Double.parseDouble(minGCField.getText().trim());
				double maxGC = Double.parseDouble(maxGCField.getText().trim());
				if (!isEmpty(minGCField) && !isEmpty(maxGCField) && minGC > maxGC) 
				{
					JOptionPane.showMessageDialog(null, "Invalid Input: Min GC must be lower than Max GC");
					return false;
				}
				if (minGC < 0 || minGC > 100) 
				{
					JOptionPane.showMessageDialog(null, "Invalid Min GC: input must be between 0 and 100");
					return false;
				}
				if (maxGC < 0 || maxGC > 100) 
				{
					JOptionPane.showMessageDialog(null, "Invalid Max GC: input must be between 0 and 100");
					return false;
				}
			}
		}
		if (lowerCheck.isSelected() && !isEmpty(maxLowerField)) 
		{
			if (!isValidInteger(maxLowerField.getText().trim()))
			{
				JOptionPane.showMessageDialog(null, "Invalid Max Lower Case Count: Input is not a valid integer");
				return false;
			}
			if (Integer.parseInt(maxLowerField.getText().trim()) < 0) {
				JOptionPane.showMessageDialog(null, "Invalid Max Lowercase Count: Input must be a positive number");
				return false;
			}
		}
		if (nCheck.isSelected() && !isEmpty(maxNField)) 
		{
			if (!isValidInteger(maxNField.getText().trim())) {
				JOptionPane.showMessageDialog(null, "Invalid Max N Count: Input is not a valid integer");
				return false;
			}
			if (Integer.parseInt(maxNField.getText().trim()) < 0) 
			{
				JOptionPane.showMessageDialog(null, "Invalid Max N Count: Input must be a positive number");
				return false;
			}
		}
		if (tmCheck.isSelected()) 
		{
			if (!isEmpty(minTMField) && !isValidDouble(minTMField.getText().trim())) {
				JOptionPane.showMessageDialog(null, "Invalid Min TM Score: Input is not a valid Floating-Point Number");
				return false;
			}
			if (!isEmpty(maxTMField)
					&& !isValidDouble(maxTMField.getText().trim())) {
				JOptionPane.showMessageDialog(null, "Invalid Max TM Score: Input is not a valid Floating-Point Number");
				return false;
			}
			if ((!isEmpty(minTMField) && isEmpty(maxTMField))
					|| (isEmpty(minTMField) && !isEmpty(maxTMField))) {
				JOptionPane.showMessageDialog(null, "Invalid TM Score Input: Either Input Values for both fields or Input Values for Neither Field");
				return false;
			}
			if(!isEmpty(minTMField) && !isEmpty(maxTMField))
			{
				double minTM = Double.parseDouble(minTMField.getText().trim());
				double maxTM = Double.parseDouble(maxTMField.getText().trim());
				if (minTM > maxTM) 
				{
					JOptionPane.showMessageDialog(null, "Invalid Input: Min TM Score must be less than Max TM Score");
					return false;
				}
			}
		}
		if (homopolymerCheck.isSelected() && !isEmpty(maxHomopolymerField)) {
			if (!isValidInteger(maxHomopolymerField.getText().trim())) {
				JOptionPane
				.showMessageDialog(null,
				"Invalid Input Max Homopolymer Score: Input is not a valid integer");
				return false;
			}
			if (Integer.parseInt(maxHomopolymerField.getText().trim()) < 0) {
				JOptionPane
				.showMessageDialog(null,
				"Invalid Max Homopolymer Score: Input must be a positive number");
				return false;
			}
		}
		if(isEmpty(configIDField))
		{
			JOptionPane.showMessageDialog(null, "Enter a Configuration ID");
		}
		if (isDuplicateID(configIDField.getText().trim())) {
			JOptionPane
			.showMessageDialog(null,
			"Invalid Configuration ID: You Cannot Enter Duplicate ID Numbers");
			return false;
		}
		return true;
	}

	/**
	 * Checks if a String is a valid integer
	 * 
	 * @param num
	 *            the String to be checked to see if it is a valid integer
	 * @return true if the String is a valid number that can be parsed into an
	 *         integer, false if not
	 */
	private boolean isValidInteger(String num) 
	{
		try 
		{
			Integer.parseInt(num);
			return true;
		} 
		catch (NumberFormatException n) 
		{
			return false;
		}
	}

	/**
	 * Checks if a String is a valid floating point number (decimal)
	 * 
	 * @param num
	 *            the String to be checked to see if it is a decimal number
	 * @return true if the String is a valid number that can be parsed into a
	 *         floating point number, false if not
	 */
	private boolean isValidDouble(String num) 
	{
		try {
			Double.parseDouble(num);
			return true;
		} catch (NumberFormatException n) {
			return false;
		}

	}

	/**
	 * Retrieves the list of Configurations that have been added
	 * 
	 * @return the list of Configurations
	 */
	public static ArrayList<Configuration> getConfigs() 
	{
		return configs;
	}

	/**
	 * Uses a Random Number Generator to generate four random floating point
	 * numbers and use these numbers as the Red, Green, Blue, and Alpha numbers
	 * for a new Color Object
	 * 
	 * @return a Color Object with four random float variables used as the Red,
	 *         Green, Blue, and Alpha parameters
	 */
	private Color pickRandomColor() 
	{
		Random gen = new Random();
		return new Color(gen.nextFloat(), gen.nextFloat(), gen.nextFloat(), gen
				.nextFloat());
	}

	/**
	 * Takes a JComponent as a parameter and sets its foreground and background
	 * colors to random colors
	 * 
	 * @param comp
	 *            the JComponent whose foreground and background colors are to
	 *            be set
	 */
	private void setRandomColor(JComponent comp) 
	{
		comp.setForeground(pickRandomColor());
		comp.setBackground(pickRandomColor());
	}

	/**
	 * Goes through Every Component on the panel and sets its foreground and
	 * background colors to random colors
	 */
	private void assignRandomColors() 
	{
		setRandomColor(enzymeFilterCheck);
		setRandomColor(enzymeNameLabel);
		setRandomColor(enzymeNameField);
		setRandomColor(enzymeSequenceLabel);
		setRandomColor(enzymeSequenceField);
		setRandomColor(addEnzymeButton);
		setRandomColor(removeEnzymeButton);
		setRandomColor(clearEnzymesButton);
		setRandomColor(failScoreCheck);
		setRandomColor(maxFailLabel);
		setRandomColor(maxFailField);
		setRandomColor(gcCheck);
		setRandomColor(minGCLabel);
		setRandomColor(minGCField);
		setRandomColor(maxGCLabel);
		setRandomColor(maxGCField);
		setRandomColor(lowerCheck);
		setRandomColor(maxLowerLabel);
		setRandomColor(maxLowerField);
		setRandomColor(nCheck);
		setRandomColor(maxNLabel);
		setRandomColor(maxNField);
		setRandomColor(enzymeInfoPanel);
		setRandomColor(enzymeButtonsPanel);
		setRandomColor(finalEnzymePanel);
		setRandomColor(failPanel);
		setRandomColor(gcPanel);
		setRandomColor(lowerPanel);
		setRandomColor(nPanel);
		setRandomColor(configIDLabel);
		setRandomColor(configIDField);
		setRandomColor(addConfigButton);
		setRandomColor(addConfigPanel);
		setRandomColor(tmCheck);
		setRandomColor(minTMLabel);
		setRandomColor(minTMField);
		setRandomColor(maxTMLabel);
		setRandomColor(maxTMField);
		setRandomColor(tmPanel);
		setRandomColor(homopolymerCheck);
		setRandomColor(maxHomopolymerLabel);
		setRandomColor(maxHomopolymerField);
		setRandomColor(this);
	}

	/**
	 * Clears all text from the text fields containing the Enzyme Name and
	 * Enzyme Fields
	 */
	private void clearEnzymeFields() {
		enzymeNameField.setText("");
		enzymeSequenceField.setText("");
	}

	/**
	 * Clears all text from every text field
	 */
	private void clearFields() {
		clearEnzymeFields();
		enzymes.clear();
		maxNField.setText("");
		minGCField.setText("");
		maxGCField.setText("");
		maxFailField.setText("");
		configIDField.setText("");
		maxLowerField.setText("");
		minTMField.setText("");
		maxTMField.setText("");
		maxHomopolymerField.setText("");
	}

	private static final long serialVersionUID = 1L;

	/**
	 * The list of enzymes that the user has added
	 */
	private ArrayList<String> enzymes;
	/**
	 * the list of Configurations that the user has added
	 */
	private static ArrayList<Configuration> configs;

	/**
	 * the Check Box allowing the user to select whether or not to include an
	 * Enzyme Filter in the Configuration Object that he or she adds
	 */
	private JCheckBox enzymeFilterCheck;
	/**
	 * JLabel labeling the field where the user may input enzyme names
	 */
	private JLabel enzymeNameLabel;
	/**
	 * JTextField where the user may input enzyme names
	 */
	private JTextField enzymeNameField;
	/**
	 * JLabel labeling the field where the user may enter enzyme sequences
	 */
	private JLabel enzymeSequenceLabel;
	/**
	 * Text Field where the user may input Enzyme sequences
	 */
	private JTextField enzymeSequenceField;
	/**
	 * JButton that allows the user to add an enzyme
	 */
	private JButton addEnzymeButton;
	/**
	 * JButton that allows the user to remove an enzyme
	 */
	private JButton removeEnzymeButton;
	/**
	 * JButton that allows the user to clear the list of ezymes
	 */
	private JButton clearEnzymesButton;

	/**
	 * Check Box that allows the user to select whether or not to include a Fail
	 * Score Filter in the Configuration that he or she adds
	 */
	private JCheckBox failScoreCheck;
	/**
	 * JLabel labeling the field where the user may input the maximum acceptable
	 * fail score
	 */
	private JLabel maxFailLabel;
	/**
	 * JTextField where the user may input the maximum acceptable fail score
	 */
	private JTextField maxFailField;

	/**
	 * Check Box that allows the user to select whether or not to include a GC
	 * Filter in the Configuration that he or she adds
	 */
	private JCheckBox gcCheck;
	/**
	 * JLabel labeling the field where the user may input the minimum acceptable
	 * GC percent
	 */
	private JLabel minGCLabel;
	/**
	 * JTextField where the user may input the minimum acceptable GC percent
	 */
	private JTextField minGCField;
	/**
	 * JLabel labelling the field where the user may input the maximum
	 * acceptable GC percent
	 */
	private JLabel maxGCLabel;
	/**
	 * JTextField where the user may input the maximum acceptable GC percent
	 */
	private JTextField maxGCField;

	/**
	 * Check Box that allows the user to select whether or not to include a
	 * Lower Case Filter in the Configuration that he or she adds
	 */
	private JCheckBox lowerCheck;
	/**
	 * JLabel labelling the field where the user may input the maximum
	 * acceptable lower case count
	 */
	private JLabel maxLowerLabel;
	/**
	 * JTextField where the user may input the maximum acceptable lower case
	 * count
	 */
	private JTextField maxLowerField;

	/**
	 * Check Box where the user may select whether or not to include an N Count
	 * Filter in the Configuration that he or she adds
	 */
	private JCheckBox nCheck;
	/**
	 * JLabel labelling the field where the user may input the maximum
	 * acceptable N Count
	 */
	private JLabel maxNLabel;
	/**
	 * JTextField where the user may input the maximum acceptablen count
	 */
	private JTextField maxNField;

	/**
	 * Check Box where the user may select whether or not to include a TM Score
	 * Filter in the Configuration that he or she adds
	 */
	private JCheckBox tmCheck;
	/**
	 * JLabel labelling the field where the user may input the minimum
	 * acceptable TM Score
	 */
	private JLabel minTMLabel;
	/**
	 * JTextField where the user may input the minumum acceptable TM Score
	 */
	private JTextField minTMField;
	/**
	 * JLabel labelling the field where the user may input the maximum
	 * acceptable TM score
	 */
	private JLabel maxTMLabel;
	/**
	 * JTextField where the user may input the maximum acceptable TM score
	 */
	private JTextField maxTMField;

	/**
	 * JCheckBox where the user may select whether or not to include a
	 * Homopolymer Score Filter in the Configuration that he or she adds
	 */
	private JCheckBox homopolymerCheck;
	/**
	 * JLabel labelling the field where the user may input the maximum
	 * acceptable Homopolymer score
	 */
	private JLabel maxHomopolymerLabel;
	/**
	 * JTextField where the user may input the maximum acceptable homopolymer
	 * score
	 */
	private JTextField maxHomopolymerField;

	/**
	 * JPanel containing all the labels and text fields where the user may input
	 * enzyme information
	 */
	private JPanel enzymeInfoPanel;
	/**
	 * JPanel containing the buttons that allow the user to add, remove, and
	 * clear enzymes
	 */
	private JPanel enzymeButtonsPanel;
	/**
	 * JPanel containing all the enzyme-related labels, fields, and buttons
	 */
	private JPanel finalEnzymePanel;

	/**
	 * JPanel containing all the labels, fields, and buttons related to the Fail
	 * Score Filter
	 */
	private JPanel failPanel;
	/**
	 * JPanel containing all the labels, fields, and buttons related to the GC
	 * Percent Filter
	 */
	private JPanel gcPanel;
	/**
	 * JPanel containing all the labels, fields, and buttons related to the
	 * Lowercase Count Filter
	 */
	private JPanel lowerPanel;
	/**
	 * JPanel containing all the labels, fields, and buttons related to the N
	 * Count Filter
	 */
	private JPanel nPanel;
	/**
	 * JPanel containing all the labels, fields, and buttons related to the TM
	 * Score Filter
	 */
	private JPanel tmPanel;
	/**
	 * JPanel containing all the labels, fields, and buttons related to the
	 * Homopolymer Score Filter
	 */
	private JPanel homopolymerPanel;

	/**
	 * JLabel labelling the field where the user may input the unique ID of the
	 * configuration that he or she adds
	 */
	private JLabel configIDLabel;
	/**
	 * JTextField where the user may input the unique ID of the Configuration
	 * that he or she adds
	 */
	private JTextField configIDField;
	/**
	 * JButton that allows the user to add a Configuration to the list of
	 * Configurations
	 */
	private JButton addConfigButton;
	/**
	 * JPanel containing all the components, including the labels, fields, and
	 * buttons
	 */
	private JPanel addConfigPanel;
}
