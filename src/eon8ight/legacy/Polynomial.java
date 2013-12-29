package eon8ight.legacy;

import java.io.Serializable;
import java.util.*;

public class Polynomial implements Serializable, Cloneable
{
	private static final long serialVersionUID = -7673601736138006755L;
	private LinkedList<Term> eq;
	
	public Polynomial(Term... terms)
	{
		eq = new LinkedList<Term>();
		
		for(Term t : terms)
			addTerm(t);
	}
	
	public Polynomial()
	{
		eq = new LinkedList<Term>();
	}
	
	public void addTerm(Term t)
	{
		if(t == null)
			return;
		
		if(t.getCoefficient() != 0)
		{
			eq.add(t);
			simplify();
		}
	}
	
	public void removeTerm(Term t)
	{
		eq.remove(t);
		simplify();
	}
	
	public void removeTerm(int index)
	{
		eq.remove(index);
		simplify();
	}
	
	public void simplify()
	{
		cleanupEquation();
		Term t1, t2;
		
		for(int i = 0; i < eq.size() - 1; i++)
		{
			t1 = eq.get(i);
			t2 = eq.get(i + 1);
			
			if(t1.getExponent() == t2.getExponent())
			{
				t1.setCoefficient(t1.getCoefficient() + t2.getCoefficient());
				eq.remove(t2);
				i--;
			}
		}
	}
	
	public Term getTerm(int index)
	{
		return eq.get(index);
	}
	
	public int indexOf(Term t)
	{
		return eq.indexOf(t);
	}
	
	protected void cleanupEquation()
	{
		Collections.sort(eq);
		
		for(int i = 0; i < eq.size(); i++)
			if(eq.get(i).getCoefficient() == 0)
				eq.remove(i);
	}
	
	@Override
	public String toString()
	{
		String s = "";
		Term t;
		
		for(int i = 0; i < eq.size(); i++)
		{
			t = eq.get(i);
			s += (i > 0) ? ((t.getCoefficient() < 0) ? t.toString().substring(1) : t.toString()) : t.toString();
			
			if(i < eq.size() - 1)
			{
				t = eq.get(i + 1);
				
				if(t.getCoefficient() < 0)
					s += " - ";
				else if(t.getCoefficient() > 0)
					s += " + ";
			}
		}
		
		return s;
	}
	
	public Term[] toTermArray()
	{
		Term[] toReturn = new Term[eq.size()];
		
		for(int i = 0; i < toReturn.length; i++)
			toReturn[i] = (Term) eq.get(i).clone();
		
		return toReturn;
	}
	
	public int length()
	{
		return eq.size();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eq == null) ? 0 : eq.hashCode());
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
		
		Polynomial other = (Polynomial) obj;
		Term[] arrayOther = other.toTermArray(), arrayThis = this.toTermArray();
		
		return Arrays.equals(arrayOther, arrayThis);
	}

	@Override
	public Object clone()
	{
		return new Polynomial(this.toTermArray());
	}
}