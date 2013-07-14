package dk.diku.pcsd.keyvaluebase.interfaces;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import dk.diku.pcsd.assignment3.impl.PairImpl;
import dk.diku.pcsd.assignment3.impl.TimestampListPair;
import dk.diku.pcsd.assignment3.impl.TimestampPair;

@XmlSeeAlso({PairImpl.class, TimestampPair.class, TimestampListPair.class})
public abstract class Pair<K, V> implements Comparable<Pair<K,V>>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6120085642616703347L;
	
	public Pair()
	{
	}
	
	
	public abstract K getKey();
	
	public abstract V getValue();
	
}
