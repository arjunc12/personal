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

import solPicker.job.Configuration;
import solPicker.job.Job;
import solPicker.job.Query;


/**
 * JPanel that allows the user to add a Job by specifying the Query ID and the
 * Configuration ID. The Panel checks that the user has entered Query and
 * Configuration ID's that actually exist, and then retrieves the lists of
 * Queries and Configurations and finds the appropriate configuration.
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: AddJobPanel.java,v 1.2 2009/10/16 17:47:31 achandra Exp $
 */
public class AddJobPanel extends JPanel 
{
	/**
	 * Default Constructor; Initializes all the Components, Adds them to the
	 * final panel. Adds ActionListener to the JButton which causes it to add a
	 * Job upon being pressed.
	 */

	public AddJobPanel()
	{
		setLayout(new GridLayout(1, 3));
		setBorder(new TitledBorder(new EtchedBorder(), "Add Job"));
		// initialize the ArrayList containing all the Job objects that have
		// been added
		jobs = new ArrayList<Job>();
		// initialize all the components
		queryIdLabel = new JLabel("Query ID");
		queryIdField = new JTextField();
		configIdLabel = new JLabel("Configuration ID");
		configIdField = new JTextField();
		addJobButton = new JButton("Add Job");
		labelPanel = new JPanel(new GridLayout(2, 1));
		fieldPanel = new JPanel(new GridLayout(2, 1));
		jobID = 0;
		if (false)
			assignRandomColors();
		labelPanel.add(queryIdLabel);
		labelPanel.add(configIdLabel);
		fieldPanel.add(queryIdField);
		fieldPanel.add(configIdField);
		add(labelPanel);
		add(fieldPanel);

		/**
		 * ActionListener that checks if the user has inputed valid Query and
		 * Configuration ID's (An ID input is valid if there is a Query or
		 * Configuration that has that ID). If the ID is invalid an error
		 * message is produced. Otherwise, the Listener reads the input, finds
		 * the corresponding Query and Configuration objects, and creates a JOb
		 * object, passing along the retrieved Configuration and Query Objects
		 * as parameters.
		 * 
		 * @author Arjun Chandrasekhar
		 */
		class AddJobListener implements ActionListener {
			/**
			 * Checks if the user has inputed valid ID numbers, retrieves the
			 * appropriate Query and Configuration Objects, and uses them to
			 * create a new Job Object and add it to the List of Jobs
			 */
			public void actionPerformed(ActionEvent event) 
			{
				// check if the fields have been filled properly
				if (fieldsFilledProperly()) 
				{
					// parses the Query ID
					String queryId = queryIdField.getText().trim();
					// parses the Configuration ID
					String configId = configIdField.getText().trim();
					// finds the appropriate Configuration Object
					Configuration c = findConfig(configId);
					// Finds the appropriate Query Object
					Query q = findQuery(queryId);
					jobID++;
					//Creates a new Job Object with the Query and Configuration Objects passed as parameters.  adds the Job Object to the list of Jobs
					Job job = new Job(c, q, jobID);
					jobs.add(job);
					XMLWriter.append(job.toXML());
					// clear the Query
					clearFields();
				}
			}
		}
		// add action listener to the JButton that allows the user to add a solPicker.job
		addJobButton.addActionListener(new AddJobListener());
		// add the JButton to the final panel
		add(addJobButton);
	}

	/**
	 * Retrieves the list of Jobs that the user has added
	 * 
	 * @return the list of Jobs
	 */
	public static ArrayList<Job> getJobs() {
		return jobs;
	}

	/**
	 * Takes each component and sets its foreground and background to a random
	 * color
	 */
	private void assignRandomColors() {
		// Goes through all the components and sets their foregrounds and
		// backgrounds to random colors
		pickRandomColor(queryIdLabel);
		pickRandomColor(queryIdField);
		pickRandomColor(configIdLabel);
		pickRandomColor(configIdField);
		pickRandomColor(addJobButton);
		pickRandomColor(labelPanel);
		pickRandomColor(fieldPanel);
		pickRandomColor(this);
	}

	/**
	 * Assigns random colors to a JComponent's foreground and background
	 * 
	 * @param c
	 *            the Component to be given random colors
	 */
	private void pickRandomColor(JComponent c) {
		// sets the foreground and background of the parameter to a random color
		c.setForeground(pickRandomColor());
		c.setBackground(pickRandomColor());
	}

	/**
	 * Uses a Random generator to create a Color Object with random floating
	 * point values used as the Red, Green, Blue, and Alpha values of the color.
	 * 
	 * @return a Color Object with random Red, Green, Blue, and Alpha values
	 */
	private Color pickRandomColor() {
		// Creates a new Random Object
		Random gen = new Random();
		// generate three random floating point numbers and use them as
		// parameters for a new Color Object
		return new Color(gen.nextFloat(), gen.nextFloat(), gen.nextFloat(), gen.nextFloat());
	}

	/**
	 * checks if the ID Text Fields are filled properly. For the Fields to be
	 * filled properly, the two ID's must not be duplicate ID's (meaning there
	 * cannot be an already existing Query of Configuration with the same ID. A
	 * Query and a Configuration can have the same ID, but two Queries cannot
	 * share an ID, and two Chromosomes cannot share an ID). Additionally, the
	 * Configuration ID Text Field must contain a real number.
	 * 
	 * @return true if none of the inputed ID's are duplicates and the
	 *         Configuration ID field contains a real number, false if any one
	 *         of these conditions is not met.
	 */
	private boolean fieldsFilledProperly() {
		// Checks if the inputed Query ID exists, gives an error message if it
		// does not exist
		if (!queryExists(queryIdField.getText().trim())) {
			JOptionPane.showMessageDialog(null,
					"Invalid Query ID: No Such Query Exists");
			return false;
		}
		// checks that the inputed Configuration ID exists, gives an error
		// message if it does not exist
		if (!configExists(configIdField.getText().trim())) {
			JOptionPane.showMessageDialog(null,
					"Invalid Configuration ID: No Such Configuration Exists");
			return false;
		}
		// returns true if none of the tests failed
		return true;
	}

	/**
	 * Checks if there is a Query that has the same ID as the parameter
	 * 
	 * @param id
	 *            the prospective Query ID
	 * @return true if there is a Query whose ID is identical to the parameter
	 */
	private boolean queryExists(String id) {
		// retrieves the list of Queries that the user has added
		ArrayList<Query> queries = AddQueryPanel.getQueries();
		// loop that goes through every Query in the list of Queries
		for (int i = 0; i < queries.size(); i++) {
			// checks each Query's ID number, returns true if any of the Queries
			// has an ID that matches the parameter
			if (queries.get(i).getID().equalsIgnoreCase(id))
				return true;
		}
		// returns fale if none of the Query ID's matched the parameter
		return false;
	}

	/**
	 * Checks if there is a Configuration that has the same ID as the parameter
	 * 
	 * @param id
	 *            the prospective Configuration ID
	 * @return true if there is a Configuration whose ID is identical to the
	 *         parameter
	 */
	private boolean configExists(String id) {
		// retrieves the list of Configurations that have been added by the user
		ArrayList<Configuration> configs = AddConfigurationPanel.getConfigs();
		// loop that goes through the list of Configurations
		for (int i = 0; i < configs.size(); i++) {
			// checks each Configuration's ID number, returns true if any of the
			// Configurations has an ID that matches the parameter
			if (configs.get(i).getID().equalsIgnoreCase(id))
				return true;
		}
		// returns false if none of the Configuration ID's matched the parameter
		return false;
	}

	/**
	 * Finds the Query whose ID is identical to the parameter. Returns null if
	 * no such Query is found
	 * 
	 * @param id
	 *            the Query ID
	 * @return a Query whose ID is equal to the parameter, null if no such Query
	 *         exists
	 */
	private Query findQuery(String id) {
		// retrieves the list of Queries that have been added by the user
		ArrayList<Query> queries = AddQueryPanel.getQueries();
		// loop that goes through the list of Queries
		for (int i = 0; i < queries.size(); i++) {
			// Checks each Query's ID number. If a given Query has the same ID
			// as the parameter, then the method returns that Query.
			Query q = queries.get(i);
			if (q.getID().equalsIgnoreCase(id))
				return q;
		}
		// returns null if no Queries matched the parameter (note: this should
		// never happen, because the program is designed so that this method is
		// never
		// called unless it has been verified that the the user has inputed a
		// valid Query ID
		return null;
	}

	/**
	 * Finds the Configuration whose ID is identical to the parameter. Returns
	 * null if no such Configuration is found
	 * 
	 * @param id
	 *            the Configuration ID
	 * @return a Configuration whose ID is equal to the parameter, null if no
	 *         such Configuration exists
	 */
	private Configuration findConfig(String id) {
		// retrieves the list of Configurations that have been added by the user
		ArrayList<Configuration> configs = AddConfigurationPanel.getConfigs();
		// loop that goes through the list of configurations
		for (int i = 0; i < configs.size(); i++) {
			// checks each Configuration's ID. If a given Configuration has the
			// same ID as the parameter than the method returns that
			// Configuration.
			Configuration c = configs.get(i);
			if (c.getID().equalsIgnoreCase(id))
				return c;
		}
		// returns null if no Configurations matched the parameter (note: this
		// should never happen, because the program is designed so that this
		// method is
		// never called unless it has been verified that the the user has
		// inputed a valid Configuration ID
		return null;
	}

	/**
	 * Removes all text from text from the Query ID and Configuration ID text
	 * fields
	 */
	private void clearFields() {
		// sets the text in both the txt fields to an empty String
		queryIdField.setText("");
		configIdField.setText("");
	}

	private static final long serialVersionUID = 1L;
	/**
	 * JLabel labelling the field where the user may input the unique ID of the
	 * Query that he/she wants to pass on to the Job object that he/she is
	 * adding
	 */
	private JLabel queryIdLabel;
	/**
	 * JTextField where the user may input the unique ID of the Query that
	 * he/she wants to pass on to the Job object that he/she is adding
	 */
	private JTextField queryIdField;
	/**
	 * JLabel labelling the field where the user may input the unique ID of the
	 * Configuration that he/she wants to pass on to the Job object that he/she
	 * is adding
	 */
	private JLabel configIdLabel;
	/**
	 * JTextField where the user may input the unique ID of the Configuration
	 * that he/she wants to pass on to the Job object that he/she is adding
	 */
	private JTextField configIdField;
	/**
	 * JButton that allows the user to add a Job to the list of Jobs
	 */
	private JButton addJobButton;
	/**
	 * JPanel containing all the JLabels in the class
	 */
	private JPanel labelPanel;
	/**
	 * JPanel containing all the JTextFields in the class
	 */
	private JPanel fieldPanel;
	/**
	 * the unique ID of the Job that is being added
	 */
	private static int jobID = 0;
	/**
	 * the list of Jobs that the user has added
	 */
	private static ArrayList<Job> jobs;
}
