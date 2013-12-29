package eon8ight.legacy;

import java.util.Random;

public final class PolynomialMath
{
	private PolynomialMath() throws Exception
	{
		throw new Exception("This class cannot be instantiated.");
	}
	
	public static Polynomial add(Polynomial p1, Polynomial p2)
	{
		Polynomial toReturn = (Polynomial) p1.clone();
		
		for(Term t : p2.toTermArray())
			toReturn.addTerm(t);
		
		return toReturn;
	}
	
	/**
	 * Performs a subtraction operation on two polynomials. Order matters.
	 * 
	 * @param p1 the first ordered polynomial.
	 * @param p2 the second ordered polynomial.
	 * @return (p1 - p2), in polynomial form.
	 */
	public static Polynomial subtract(Polynomial p1, Polynomial p2)
	{
		Polynomial toReturn = (Polynomial) p1.clone();
		
		for(Term t : p2.toTermArray())
			toReturn.addTerm(new Term(-t.getCoefficient(), t.getExponent()));
		
		return toReturn;
	}
	
	public static Polynomial multiply(Polynomial p1, Polynomial p2)
	{
		Polynomial toReturn = new Polynomial();
		int tempCoeff, tempExp;
		
		for(Term t1 : p1.toTermArray())
			for(Term t2 : p2.toTermArray())
			{
				tempCoeff = t1.getCoefficient() * t2.getCoefficient();
				tempExp = t1.getExponent() + t2.getExponent();
				toReturn.addTerm(new Term(tempCoeff, tempExp));
			}
		
		return toReturn;
	}
	
	/**
	 * Attempts a synthetic division operation on two polynomials.
	 * 
	 * @param divisor is a polynomial following the form: [x^n + x^(n-1) + x^(n-2) + ... + x^0].
	 * @param dividend is a polynomial is the dividend following the form: [x + k].
	 * @return the quotient of the dividend by the divisor.
	 */
	public static Polynomial divide(Polynomial divisor, Polynomial dividend)
	{
		Polynomial toReturn = new Polynomial();
		
		int constant = -dividend.getTerm(1).getCoefficient(), exp = divisor.getTerm(0).getExponent() - 1, drop;
		int[] divisorCoeffs = new int[divisor.length()];
		
		for(int i = 0; i < divisorCoeffs.length - 1; i++)
			divisorCoeffs[i] = divisor.getTerm(i).getCoefficient();
		
		drop = divisorCoeffs[0];
		
		for(int i = 0; i < divisorCoeffs.length - 1; i++)
		{
			toReturn.addTerm(new Term(drop, exp));
			drop = divisorCoeffs[i + 1] + (constant * drop);
			exp--;
		}
		
		return toReturn;
	}
	
	/**
	 * Attempts to parse a Term from a String.
	 * 
	 * @param s is a Term in the form [ax^b].
	 * @return a new Term parsed from the parameter s.
	 */
	public static Term parseTerm(String s)
	{
		String coeff, exp;
		
		if(s.contains("x"))
		{
			int xIndex = s.indexOf("x");
			coeff = s.substring(0, xIndex);
			exp = s.contains("^") ? s.substring(xIndex + 2) : s.substring(xIndex + 1);
		}
		else
			return new Term(Integer.parseInt(s), 0);
			
		if(coeff.isEmpty())
			coeff = "1";
		else if(coeff.equals("-"))
			coeff = "-1";
		
		if(exp.isEmpty())
			exp = "1";
		
		return new Term(Integer.parseInt(coeff), Integer.parseInt(exp));
	}
	
	/**
	 * Attempts to parse a Polynomial from a String.
	 * 
	 * @param s is a Polynomial in the form [ax^b + cx^d + ...].
	 * @return a new Polynomial parsed from the parameter s.
	 */
	public static Polynomial parsePolynomial(String s)
	{	
		char[] charArray = s.toCharArray();
		s = "";
		
		for(char c : charArray)
			if(!Character.isWhitespace(c) && c != '\n')
				s += c;
		
		Polynomial toReturn = new Polynomial();
		String substr = "";
		
		for(int i = 0; i < s.length(); i++)
		{
			if(s.charAt(i) == '+' || (i != 0 && s.charAt(i) == '-'))
			{
				toReturn.addTerm(parseTerm(substr));
				substr = (s.charAt(i) == '+') ? "" : "-";
			}
			else
				substr += s.charAt(i);
		}
		
		if(!substr.isEmpty())
			toReturn.addTerm(parseTerm(substr));
		
		return toReturn;
	}
	
	public static boolean isQuadraticEquation(Polynomial p)
	{
		if(p.toTermArray().length < 3)
			return (p.toTermArray()[0].getExponent() == 2);
		
		return false;
	}
	
	public static Polynomial getRandomPolynomial()
	{
		Polynomial toReturn = new Polynomial();
		Random r = new Random();
		int lim = r.nextInt(6), coeff, exp;
		byte[] mod = new byte[2];
		
		for(int i = 0; i < lim; i++)
		{
			r.nextBytes(mod = new byte[2]);
			coeff = (int) (Math.random() * mod[0]);
			exp = Math.abs((int) (Math.random() * mod[1]));
			toReturn.addTerm(new Term(coeff, exp));
		}
		
		if(toReturn.length() < 3)
			return getRandomPolynomial();
		
		return toReturn;
	}
	
	/**
	 * Get two random polynomials for division operations.
	 * 
	 * @return an array of two polynomials. The first one is the
	 * 		   divisor; the second one is the dividend.
	 */
	public static Polynomial[] getRandomDivisionPolynomials()
	{
		Polynomial[] toReturn = new Polynomial[2];
		Random r = new Random();
		byte[] mod = new byte[1];
		mod[0] = 0;
		int coeff = 0;
		
		/* DIVIDEND */
		toReturn[1] = new Polynomial();
		toReturn[1].addTerm(new Term(1, 1));
		
		while(mod[0] == 0)
			r.nextBytes(mod = new byte[1]);
		
		while(coeff == 0)
			coeff = (int) (Math.random() * mod[0]);
			
		toReturn[1].addTerm(new Term(coeff, 0));
		
		/* DIVISOR */
		toReturn[0] = new Polynomial();
		int lim = r.nextInt(3) + 3;
		mod[0] = 0;
		coeff = 0;
		
		for(int i = lim - 1; i >= 0; i--)
		{
			while(mod[0] == 0)
				r.nextBytes(mod = new byte[1]);
			
			while(coeff == 0)
				coeff = (int) (Math.random() * mod[0]);
			
			toReturn[0].addTerm(new Term(coeff, i));
			coeff = 0;
			mod[0] = 0;
		}
		
		return toReturn;
	}
}