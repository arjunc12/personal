package solPicker.solpicker_gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import solPicker.XMLInputWriter;
import solPicker.job.Configuration;
import solPicker.job.Job;
import solPicker.job.JobFactory;
import solPicker.job.Query;


/**
 * $Id: ExecutePanel.java,v 1.6 2009/10/16 22:15:54 achandra Exp $ Creates a panel
 * with two JButtons: one that allow the user to perform all the jobs that have
 * been inputed, and another that allows the user to write all of the inputed
 * queries, configurations, and jobs, to an XML file
 * 
 * @author Arjun Chandrasekhar
 */
public class ExecutePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor; writeToXMLButton and doJobsButton are initialize.
	 * The writeToXMLButton is given an ActionListener that makes the Button
	 * write all the queries, configurations, and jobs to an XML file when
	 * pressed. The doJobsButton is given an ActionListener that makes the
	 * Button perform all of the jobs when pressed
	 */
	public ExecutePanel() 
	{
		setLayout(new GridLayout(2, 1));
		setBorder(new TitledBorder(new EtchedBorder(), "Execute Program"));
		writeToXMLButton = new JButton("Write to XML");
		doJobsButton = new JButton("Do Jobs");
		if (false)
			assignRandomColors();

		/**
		 * ActionListener that makes the writeToXMLButton write all the queries,
		 * configurations, and jobs to an XML file upon being presses
		 * 
		 * @author Arjun Chandrasekhar
		 */
		class WriteToXMLListener implements ActionListener {
			/**
			 * writes all of the queries, configurations, and jobs to an XML
			 * file.
			 */
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(null,
						"Pick a file to write all your XML ");
				JFileChooser chooser = new JFileChooser();
				int result = chooser.showOpenDialog(null);
				if (result != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "Method is aborting.  You must select a File the next time you run this.");
					return;
				}
				xml = chooser.getSelectedFile();
				ArrayList<Query> queries = AddQueryPanel.getQueries();
				ArrayList<Configuration> configs = AddConfigurationPanel
						.getConfigs();
				ArrayList<Job> jobs = AddJobPanel.getJobs();
				XMLInputWriter writer = new XMLInputWriter(xml);
				writer.writeToXML(queries, configs, jobs);
			}
		}
		writeToXMLButton.addActionListener(new WriteToXMLListener());

		/**
		 * ActionListener that makes the doJobsButton go through all the inputed
		 * jobs and call the doJob method upon being pressed. Once the jobs have
		 * been completed, the class uses a PrintWriter to write all the
		 * sequence data and remaining valid oligos to a text file.
		 * 
		 * @author Arjun Chandrasekhar
		 */
		class DoJobsListener implements ActionListener 
		{
			/**
			 * Goes through all of the inputed jobs, calls on each job to
			 * retrieve the appropriate sequence data, create oligos, and filter
			 * the oligos appropriately. The method then used a PrintWriter
			 * object to write all the sequence data and remaining valid oligos
			 * to a text file.
			 */
			public void actionPerformed(ActionEvent event) 
			{
				XMLWriter.append("</tns:SolPicker>");
				JobFactory pw = new JobFactory(XMLWriter.getFile());
				String directory = JOptionPane.showInputDialog("In What Directory do you want to put all this stuff?");
				JobFactory.setOutDirectory(new File(directory));
				pw.completeJobs();
				JOptionPane.showMessageDialog(null, "done writing to file");
			}
		}
		doJobsButton.addActionListener(new DoJobsListener());
		add(writeToXMLButton);
		add(doJobsButton);
	}

	/**
	 * assigns all the Components of the panel random colors in both the
	 * foreground and the background
	 */
	private void assignRandomColors() {
		setRandomColor(writeToXMLButton);
		setRandomColor(doJobsButton);
		setRandomColor(this);
	}

	/**
	 * Takes a JComponent Object and sets its foreground and background to
	 * random colors
	 * 
	 * @param c
	 *            the JComponent whose foreground and background colors are to
	 *            be set
	 */
	private void setRandomColor(JComponent c) {
		c.setForeground(pickRandomColor());
		c.setBackground(pickRandomColor());
	}

	/**
	 * Uses a Random object to produce four random float variables and uses
	 * these floats to create a color Object.
	 * 
	 * @return a Color with random float parameters
	 */
	private Color pickRandomColor() {
		Random gen = new Random();
		return new Color(gen.nextFloat(), gen.nextFloat(), gen.nextFloat(), gen
				.nextFloat());
	}

	public void setFile(File f) {
		xml = f;
	}

	private JButton writeToXMLButton;
	private JButton doJobsButton;
	private File xml;
}
