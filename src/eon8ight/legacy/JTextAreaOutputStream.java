package eon8ight.legacy;

import java.io.*;
import javax.swing.*;

public class JTextAreaOutputStream extends OutputStream
{
	private JTextArea console;
	private StringBuilder builder;
	
	public JTextAreaOutputStream(JTextArea console)
	{
		this.console = console;
		builder = new StringBuilder();
	}

	@Override
	public void write(int b) throws IOException
	{
		if(b == '\r')
			return;
		else if(b == '\n')
		{
			final String text = (builder.toString() + "\n");
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run()
				{
					console.append(text);
				}
			});
			
			builder.setLength(0);
		}
		else
			builder.append((char) b);
	}
}