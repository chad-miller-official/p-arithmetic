package eon8ight.legacy;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

public class PolynomialInputPanel extends JPanel
{
	private static final long serialVersionUID = 4600064444817085323L;
	private Polynomial currentPolynomial;
	private JTextField polynomialField;
	private JList<Polynomial> polynomialList;
	private DefaultListModel<Polynomial> polynomialListModel;
	private int currentIndex;
	
	public PolynomialInputPanel()
	{
		ArrayList<Polynomial> polynomials = new ArrayList<Polynomial>();
		
		currentIndex = -1;
		currentPolynomial = null;
		
		/* TEXT FIELD */
		polynomialField = new JTextField();
		polynomialField.setPreferredSize(new Dimension(192, 24));
		
		/* POLYNOMIAL LIST MODEL */
		polynomialListModel = new DefaultListModel<Polynomial>();
		
		for(Polynomial p : polynomials)
			polynomialListModel.addElement(p);
		
		init();
	}
	
	/**
	 * Initialize and create the GUI.
	 */
	private void init()
	{
		/* POLYNOMIAL LIST */
		polynomialList = new JList<Polynomial>(polynomialListModel);
		polynomialList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		polynomialList.setVisibleRowCount(5);
		polynomialList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if(e.getValueIsAdjusting())
				{
					polynomialListModel.set(currentIndex, PolynomialMath.parsePolynomial(polynomialField.getText()));
					currentIndex = polynomialList.getSelectedIndex();
					currentPolynomial = (Polynomial) polynomialListModel.getElementAt(polynomialList.getSelectedIndex());
					polynomialField.setText(currentPolynomial.toString());
				}
			}
		});
		
		JScrollPane polynomialListScrollPane = new JScrollPane(polynomialList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		polynomialListScrollPane.setWheelScrollingEnabled(true);
		polynomialListScrollPane.getVerticalScrollBar().setUnitIncrement(24);
		polynomialListScrollPane.setPreferredSize(new Dimension(192, 96));
		
		/* BUTTONS */
		JButton saveButton = new JButton("Save File");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				polynomialListModel.set(currentIndex, PolynomialMath.parsePolynomial(polynomialField.getText()));
				String fileName = JOptionPane.showInputDialog("Enter filename.");
				
				ArrayList<Polynomial> polynomials = new ArrayList<Polynomial>();
				
				for(Object p : polynomialListModel.toArray())
					polynomials.add((Polynomial) p);
				
				try
				{
					PolynomialIO.saveFile(polynomials, fileName);
					System.out.println("Saved file as " + fileName + PolynomialIO.SUFFIX + ".");
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		
		JButton loadButton = new JButton("Load File");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String fileName = JOptionPane.showInputDialog("Enter filename.");
				ArrayList<Polynomial> polynomials = new ArrayList<Polynomial>();
				
				try
				{
					polynomials = PolynomialIO.loadFile(fileName);
				}
				catch(FileNotFoundException ex)
				{
					polynomials = new ArrayList<Polynomial>();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
				currentIndex = -1;
				
				if(polynomials.size() > 0)
				{
					currentPolynomial = polynomials.get(0);
					currentIndex++;
					polynomialList.setSelectedIndex(currentIndex);
				}
				else
					currentPolynomial = null;
				
				polynomialField.setText(currentPolynomial.toString());
				polynomialListModel.removeAllElements();
				
				for(Polynomial p : polynomials)
					polynomialListModel.addElement(p);
				
				System.out.println("Loaded file " + fileName + PolynomialIO.SUFFIX + ".");
			}
		});
		
		JButton newPolynomialButton = new JButton("New Polynomial");
		newPolynomialButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(currentIndex > 0)
					polynomialListModel.set(currentIndex, PolynomialMath.parsePolynomial(polynomialField.getText()));
				
				currentPolynomial = PolynomialMath.parsePolynomial(JOptionPane.showInputDialog("Enter a polynomial in the form ax^b + cx^d + ..."));
				polynomialListModel.addElement(currentPolynomial);
				currentIndex = polynomialListModel.indexOf(currentPolynomial);
				polynomialList.setSelectedIndex(currentIndex);
				polynomialField.setText(currentPolynomial.toString());
				
			}
		});
		
		JButton deletePolynomialButton = new JButton("Delete Polynomial");
		deletePolynomialButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				polynomialListModel.removeElementAt(currentIndex);
				
				if(currentIndex > 0)
				{
					currentIndex--;
					currentPolynomial = (Polynomial) polynomialListModel.elementAt(currentIndex);
					polynomialList.setSelectedIndex(currentIndex);
				}
				else
					currentPolynomial = null;
				
				polynomialField.setText(currentPolynomial.toString());
			}
		});
		
		JButton randomPolynomialButton = new JButton("Random Polynomial");
		randomPolynomialButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(currentIndex > 0)
					polynomialListModel.set(currentIndex, PolynomialMath.parsePolynomial(polynomialField.getText()));
				
				currentPolynomial = PolynomialMath.getRandomPolynomial();
				polynomialListModel.addElement(currentPolynomial);
				currentIndex = polynomialListModel.indexOf(currentPolynomial);
				polynomialList.setSelectedIndex(currentIndex);
				polynomialField.setText(currentPolynomial.toString());
			}
		});
		
		/* ADD COMPONENTS */
		setLayout(new BorderLayout());
		
		JPanel buttonPane = new JPanel();
		buttonPane.add(saveButton);
		buttonPane.add(loadButton);
		buttonPane.add(newPolynomialButton);
		buttonPane.add(deletePolynomialButton);
		buttonPane.add(randomPolynomialButton);
		buttonPane.validate();
		
		JPanel contentPane = new JPanel();
		contentPane.add(polynomialField);
		contentPane.add(polynomialListScrollPane);
		contentPane.validate();
		
		add(buttonPane, BorderLayout.PAGE_START);
		add(contentPane, BorderLayout.CENTER);
		validate();
	}
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Polynomial Editor");
		frame.add(new PolynomialInputPanel());
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}