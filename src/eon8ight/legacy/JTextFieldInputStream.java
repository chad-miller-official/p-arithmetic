package eon8ight.legacy;

import java.awt.event.*;
import java.io.*;
import javax.swing.JTextField;

public class JTextFieldInputStream extends InputStream implements ActionListener
{
	private JTextField textField;
	private String text;
	private int pos;
	
	public JTextFieldInputStream(JTextField textField)
	{
		this.textField = textField;
		text = "";
		pos = 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		text = textField.getText() + "\n";
		pos = 0;
		textField.setText("");
		
		synchronized(this)
		{
			this.notifyAll();
		}
	}

	@Override
	public int read() throws IOException
	{
		if(text != null && pos == text.length())
		{
			text = null;
			return StreamTokenizer.TT_EOF;
		}
		
		while(text == null || pos >= text.length())
		{
			try
			{
				synchronized(this)
				{
					wait();
				}
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		return text.charAt(pos++);
	}
}