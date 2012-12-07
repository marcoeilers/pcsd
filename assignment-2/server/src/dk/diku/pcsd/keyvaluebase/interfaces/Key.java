package dk.diku.pcsd.keyvaluebase.interfaces;

public interface Key<K extends Key<K>> extends Comparable<K>
{
	public String toString();
}
