package eon8ight.legacy;

import java.io.*;
import java.util.ArrayList;

public class PolynomialIO
{
	public static final String SUFFIX = ".poly", SAVE_DIRECTORY = "res/", HEADER = "POLYNOMIALS";
	
	public static void saveFile(ArrayList<Polynomial> polynomials, String fileName) throws IOException
	{
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(SAVE_DIRECTORY + fileName + SUFFIX));
		output.writeObject(HEADER);
		output.writeObject(polynomials);
		output.close();
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Polynomial> loadFile(String fileName) throws IOException, ClassNotFoundException
	{
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(SAVE_DIRECTORY + fileName + SUFFIX));
		Object obj = input.readObject();
		
		if(obj instanceof String)
		{
			if(!((String) obj).contains(HEADER))
			{
				input.close();
				throw new IOException("Could not load " + fileName + SUFFIX + ".\n");
			}
			else
			{
				input.close();
				return ((ArrayList<Polynomial>) input.readObject());
			}
		}
		else
		{
			input.close();
			throw new IOException("Could not load " + fileName + SUFFIX + ".\n");
		}
	}
}