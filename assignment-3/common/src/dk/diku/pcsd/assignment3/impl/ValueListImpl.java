package dk.diku.pcsd.assignment3.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import dk.diku.pcsd.keyvaluebase.interfaces.ValueList;

@SuppressWarnings("serial")
@XmlType
public class ValueListImpl implements ValueList<ValueImpl>{
	
	private List<ValueImpl> valueList = new ArrayList<ValueImpl>();
	
	public List<ValueImpl> getValueList() {
		return valueList;
	}
	
	public void setValueList(List<ValueImpl> valueList) {
		this.valueList = valueList;
	}
	
	@Override
	public void add(ValueImpl v) {
		valueList.add(v);
	}

	@Override
	public void remove(ValueImpl v) {
		valueList.remove(v);
	}

	@Override
	public void merge(ValueList<ValueImpl> v) {
		valueList.addAll(v.toList());
	}

	@Override
	public List<ValueImpl> toList() {
		return getValueList();
	}
	
	@Override
	public Iterator<ValueImpl> iterator() {
		return valueList.iterator();
	}

}
