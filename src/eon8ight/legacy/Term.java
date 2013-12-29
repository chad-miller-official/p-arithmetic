package eon8ight.legacy;

import java.io.Serializable;

public class Term implements Comparable<Term>, Serializable, Cloneable
{
	private static final long serialVersionUID = 5685146619801002339L;
	private int coeff;
	private int exp;
	
	public Term(int coeff, int exp)
	{
		this.coeff = coeff;
		this.exp = exp;
	}
	
	public Term()
	{
		this(0, 0);
	}
	
	public int getCoefficient()
	{
		return coeff;
	}
	
	public int getExponent()
	{
		return exp;
	}
	
	public void setCoefficient(int coeff)
	{
		this.coeff = coeff;
	}
	
	public void setExponent(int exp)
	{
		this.exp = exp;
	}
	
	@Override
	public int compareTo(Term t)
	{
		if(t.exp > this.exp)
			return 1;
		else if(this.exp > t.exp)
			return -1;
		else
		{
			if(t.coeff > this.coeff)
				return 1;
			else if(this.coeff > t.coeff)
				return -1;
			else
				return 0;
		}
	}
	
	@Override
	public String toString()
	{
		if(coeff == 0)
			return "0";
		else if(coeff == 1)
		{
			if(exp == 0)
				return "1";
			else if(exp == 1)
				return "x";
			else
				return ("x^" + Integer.toString(exp));
		}
		else if(coeff == -1)
		{
			if(exp == 0)
				return "-1";
			else if(exp == 1)
				return "-x";
			else
				return ("-x^" + Integer.toString(exp));
		}
		else
		{
			if(exp == 0)
				return Integer.toString(coeff);
			else if(exp == 1)
				return (Integer.toString(coeff) + "x");
			else
				return (Integer.toString(coeff) + "x^" + Integer.toString(exp));
		}
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + coeff;
		result = prime * result + exp;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(getClass() != obj.getClass())
			return false;
		
		Term other = (Term) obj;
		
		if(coeff != other.coeff)
			return false;
		if(exp != other.exp)
			return false;
		
		return true;
	}

	@Override
	public Object clone()
	{
		return new Term(this.coeff, this.exp);
	}
}