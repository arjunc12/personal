package solPicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import solPicker.job.Configuration;
import solPicker.job.Job;
import solPicker.job.Query;

public class XMLInputWriter {
	public XMLInputWriter(File file) {
		f = file;
	}

	public void writeToXML(ArrayList<Query> queries,
			ArrayList<Configuration> configs, ArrayList<Job> jobs) {
		try {
			PrintWriter writer = new PrintWriter(f);
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writer.println("<tns:SolPicker xmlns:tns=\"http://www.example.org/SolPickerSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.example.org/SolPickerSchema SolPickerSchema.xsd \">");
			for (int i = 0; i < queries.size(); i++) {
				Query q = queries.get(i);
				writer.println(q.toXML());
			}
			for (int i = 0; i < configs.size(); i++) {
				Configuration config = configs.get(i);
				writer.println(config.toXML());
			}
			for (int i = 0; i < jobs.size(); i++) {
				Job j = jobs.get(i);
				writer.println(j.toXML());
			}
			writer.println("</tns:SolPicker>");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		return f;
	}

	private File f;
}
