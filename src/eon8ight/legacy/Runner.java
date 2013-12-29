package eon8ight.legacy;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Runner implements ActionListener, Runnable
{
	public static void main(String[] args)
	{
		new Thread(new Runner()).start();
	}
	
	private ArrayList<Polynomial> polynomials;
	private Problem[] problems;
	private JTextAreaOutputStream output;
	private JTextArea textArea;
	private JTextField textField;
	private Polynomial currentSolution;
	
	public Runner()
	{
		setupFrame();
		String fileName = JOptionPane.showInputDialog("Enter filename.");
		
		if(fileName == null || fileName.isEmpty())
		{
			polynomials = new ArrayList<Polynomial>();
			
			for(int i = 0; i < 15; i++)
				polynomials.add(PolynomialMath.getRandomPolynomial());
		}
		else
		{
			try
			{
				polynomials = PolynomialIO.loadFile(fileName);
			}
			catch(FileNotFoundException e)
			{
				polynomials = new ArrayList<Polynomial>();
				
				for(int i = 0; i < 15; i++)
					polynomials.add(PolynomialMath.getRandomPolynomial());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		init();
	}
	
	private void setupFrame()
	{
		textArea = new JTextArea(16, 48);
		textArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		output = new JTextAreaOutputStream(textArea);
		System.setOut(new PrintStream(output));
		
		textField = new JTextField();
		textField.addActionListener(this);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.PAGE_START);
		panel.add(textField, BorderLayout.PAGE_END);
		panel.validate();
		
		JFrame frame = new JFrame("Polynomials");
		frame.add(panel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	private void init()
	{
		problems = new Problem[30];
		int index1 = 0, index2 = 0;
		Operation op;
		
		for(int i = 0; i < problems.length; i++)
		{
			switch((int) (Math.random() * 4))
			{
				case 0:		op = Operation.ADDITION;		break;
				case 1:		op = Operation.SUBTRACTION;		break;
				case 2: 	op = Operation.MULTIPLICATION;	break;
				case 3:		op = Operation.DIVISION;		break;
				default:	op = null;						break;
			}
			
			if(op != Operation.DIVISION)
			{
				while(index1 == index2)
				{
					index1 = (int) (Math.random() * polynomials.size());
					index2 = (int) (Math.random() * polynomials.size());
				}
				
				problems[i] = new Problem(polynomials.get(index1), polynomials.get(index2), op);
				index1 = index2 = 0;
			}
			else
			{
				Polynomial[] p = PolynomialMath.getRandomDivisionPolynomials();
				problems[i] = new Problem(p[0], p[1], op);
			}
		}
	}
	
	@Override
	public void run()
	{
		int totalCorrect = 0;
		currentSolution = null;
		
		for(int i = 0; i < problems.length; i++)
		{
			System.out.println((i + 1) + ". " + problems[i]);
			
			while(currentSolution == null)
				System.err.println();	//XXX
			
			if(problems[i].answersMatch(currentSolution))
			{
				System.out.println("Correct!");
				totalCorrect++;
			}
			else
			{
				System.out.println("Incorrect!");
				System.out.println("The correct answer is [" + problems[i].solution + "].");
			}
			
			System.out.println();
			currentSolution = null;
		}
		
		System.out.println("You got " + totalCorrect + " out of " + problems.length + " correct.");
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			currentSolution = PolynomialMath.parsePolynomial(textField.getText());
			textArea.append("Your answer: " + currentSolution.toString() + '\n');
			textField.setText("");
		}
		catch(NumberFormatException ex)
		{
			System.out.println("Enter a polynomial in the form of [ax^b + cx^d + ...]");
			currentSolution = null;
		}
	}
	
	private enum Operation
	{
		ADDITION,
		SUBTRACTION,
		MULTIPLICATION,
		DIVISION;
	}
	
	private class Problem
	{
		private Polynomial p1, p2;
		public Polynomial solution;
		private Operation op;
		
		public Problem(Polynomial p1, Polynomial p2, Operation op)
		{
			this.p1 = (Polynomial) p1.clone();
			this.p2 = (Polynomial) p2.clone();
			this.op = op;
			
			switch(op)
			{
				case ADDITION:			solution = PolynomialMath.add(this.p1, this.p2);		break;
				case SUBTRACTION:		solution = PolynomialMath.subtract(this.p1, this.p2);	break;
				case MULTIPLICATION:	solution = PolynomialMath.multiply(this.p1, this.p2);	break;
				case DIVISION:			solution = PolynomialMath.divide(this.p1, this.p2);		break;
				default:				solution = null;										break;
			}
		}
		
		public boolean answersMatch(Polynomial other)
		{
			return other.equals(solution);
		}
		
		@Override
		public String toString()
		{
			switch(op)
			{
				case ADDITION:			return ("(" + p1.toString() + ") + (" + p2.toString() + ")");
				case SUBTRACTION:		return ("(" + p1.toString() + ") - (" + p2.toString() + ")");
				case MULTIPLICATION:	return ("(" + p1.toString() + ") * (" + p2.toString() + ")");
				case DIVISION:			return ("(" + p1.toString() + ") / (" + p2.toString() + ") (No Remainder)");
				default:				return null;
			}
		}
	}
}