package dk.diku.pcsd.keyvaluebase.interfaces;

import javax.xml.bind.annotation.XmlSeeAlso;

import dk.diku.pcsd.assignment1.impl.PairImpl;

@XmlSeeAlso({PairImpl.class})
public abstract class Pair<K, V> implements Comparable<Pair<K,V>> {
	protected K k;
	protected V v;
	
	public Pair()
	{
	}
	
	public Pair (K k, V v){
		this.k = k;
		this.v = v;
	}
	
	public K getKey(){
		return k;
	}
	
	public V getValue(){
		return v;
	}
}
