package dk.diku.pcsd.keyvaluebase.interfaces;

public interface KeyValueBaseLog<K extends Key<K>, V extends Value>
{
	public void quiesce();
	public void resume();
}
