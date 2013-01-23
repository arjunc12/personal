package solPicker.solpicker_gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class TextFilePanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	public TextFilePanel()
	{
		browseButton = new JButton("Upload Text File");
		if(false)
		{
			assignRandomColors();
		}
		class UploadFileListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event)
			{
				JFileChooser chooser = new JFileChooser();
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					
				}
			}
		}
		browseButton.addActionListener(new UploadFileListener());
		add(browseButton);
		setBorder(new TitledBorder(new EtchedBorder(), "Upload Text File"));
	}
	
	private Color pickRandomColor()
	{
		Random gen = new Random();
		return new Color(gen.nextFloat(), gen.nextFloat(), gen.nextFloat(), gen.nextFloat());
	}
	
	private void setRandomColor(JComponent comp)
	{
		comp.setForeground(pickRandomColor());
		comp.setBackground(pickRandomColor());
	}
	
	private void assignRandomColors()
	{
		setRandomColor(browseButton);
	}
	
	private JButton browseButton;
}
