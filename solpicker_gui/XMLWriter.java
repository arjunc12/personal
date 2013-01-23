package solPicker.solpicker_gui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class XMLWriter
{
	public static void setFile(File f)
	{
		xml = f;
		prevXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		prevXML += "<tns:SolPicker xmlns:tns=\"http://www.example.org/SolPickerSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.example.org/SolPickerSchema XML/SolPickerSchema.xsd \">\n";
	}
	
	public static void append(String text)
	{
		try
		{
			PrintWriter writer = new PrintWriter(xml);
			prevXML += text;
			writer.println(prevXML);
			//System.err.println(prevXML);
			writer.close();
		}
		catch(IOException i)
		{
			i.printStackTrace();
		}
	}
	
	public static File getFile()
	{
		return xml;
	}
	
	private static File xml;
	private static String prevXML;
}
