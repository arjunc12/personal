package solPicker.solpicker_gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import solPicker.job.JobFactory;

public class BlastSelectionPanel extends JPanel
{
	public BlastSelectionPanel(JFrame frame)
	{
		enclosingFrame = frame;
		setLayout(new BorderLayout());
		outputDirectoryLabel = new JLabel("Output Diretory");
		outputDirectoryField = new JTextField();
		outputBrowseButton = new JButton("Browse");
		outputPanel = new JPanel();
		
		class OutputBrowseListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					File f = chooser.getSelectedFile();
					outputDirectoryField.setText(f.getAbsolutePath());
					JobFactory.setOutDirectory(f);
				}
			}
		}
		outputBrowseButton.addActionListener(new OutputBrowseListener());
		outputPanel.add(outputDirectoryLabel);
		outputPanel.add(outputDirectoryField);
		outputPanel.add(outputBrowseButton);
		
		netBlastDirectoryLabel = new JLabel ("Net Blast Directory");
		netBlastDirectoryField = new JTextField();
		netBlastBrowseButton = new JButton("Browse");
		netBlastPanel= new JPanel();
		
		class NetBlastBrowseListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					File f = chooser.getSelectedFile();
					netBlastDirectoryField.setText(f.getAbsolutePath());
					JobFactory.setNetBlastDirectory(f);
				}
			}
		}
		
		netBlastBrowseButton.addActionListener(new NetBlastBrowseListener());
		netBlastPanel.add(netBlastDirectoryLabel);
		netBlastPanel.add(netBlastDirectoryField);
		netBlastPanel.add(netBlastBrowseButton);
		
		localBlastDirectoryLabel = new JLabel("Local Blast Directory");
		localBlastDirectoryField = new JTextField();
		localBlastBrowseButton = new JButton("Browse");
		localBlastPanel1 = new JPanel();
		
		class LocalBlastBrowseListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					File f = chooser.getSelectedFile();
					localBlastDirectoryField.setText(f.getAbsolutePath());
					JobFactory.setLocalBlastDirectory(f);
				}
			}
		}
		
		localBlastBrowseButton.addActionListener(new LocalBlastBrowseListener());
		localBlastPanel1.add(localBlastDirectoryLabel);
		localBlastPanel1.add(localBlastDirectoryField);
		localBlastPanel1.add(localBlastBrowseButton);
		
		databaseDirectoryLabel = new JLabel("Database Directory");
		databaseDirectoryField = new JTextField();
		databaseBrowseButton = new JButton("Browse");
		localBlastPanel2 = new JPanel();
		
		class DatabaseBrowseListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					File f = chooser.getSelectedFile();
					databaseDirectoryField.setText(f.getAbsolutePath());
					JobFactory.setDatabaseDirectory(f);
				}
			}
		}
		databaseBrowseButton.addActionListener(new DatabaseBrowseListener());
		localBlastPanel2.add(databaseDirectoryLabel);
		localBlastPanel2.add(databaseDirectoryField);
		localBlastPanel2.add(databaseBrowseButton);
		
		localBlastPanel = new JPanel(new BorderLayout());
		localBlastPanel.add(localBlastPanel1, BorderLayout.NORTH);
		localBlastPanel.add(localBlastPanel2, BorderLayout.SOUTH);
		
		netBlastButton = new JRadioButton("Net Blast");
		localBlastButton = new JRadioButton("Local Blast");
		ButtonGroup group = new ButtonGroup();
		group.add(netBlastButton);
		group.add(localBlastButton);
		netBlastButton.setSelected(true);
		buttonPanel = new JPanel();
		buttonPanel.add(netBlastButton);
		buttonPanel.add(localBlastButton);
		add(buttonPanel, BorderLayout.NORTH);
		
	}
	
	private static final long serialVersionUID = 1L;
	
	private JRadioButton netBlastButton;
	private JRadioButton localBlastButton;
	private JPanel buttonPanel;
	
	private JLabel outputDirectoryLabel;
	private JTextField outputDirectoryField;
	private JButton outputBrowseButton;
	private JPanel outputPanel;
	
	private JLabel netBlastDirectoryLabel;
	private JTextField netBlastDirectoryField;
	private JButton netBlastBrowseButton;
	private JPanel netBlastPanel;
	
	private JLabel localBlastDirectoryLabel;
	private JTextField localBlastDirectoryField;
	private JButton localBlastBrowseButton;
	private JPanel localBlastPanel1;
	
	private JLabel databaseDirectoryLabel;
	private JTextField databaseDirectoryField;
	private JButton databaseBrowseButton;
	private JPanel localBlastPanel2;
	
	private JPanel localBlastPanel;
	
	private JButton submitButton;
	
	private JFrame enclosingFrame;
}
