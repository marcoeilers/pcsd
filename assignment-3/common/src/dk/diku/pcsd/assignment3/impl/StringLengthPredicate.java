package dk.diku.pcsd.assignment3.impl;

import java.util.List;

import javax.xml.bind.annotation.XmlType;

import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;

/*
 * Returns true if a ValueListImpl contains at least one String longer than five
 * symbols. Pretty random, I know.
 */
@XmlType
public class StringLengthPredicate extends Predicate<ValueListImpl> {

	@Override
	public boolean evaluate(ValueListImpl input) {
		List<ValueImpl> values = input.getValueList();
		
		for (ValueImpl v : values){
			Object o = v.getValue();
			if (o instanceof String && ((String) o).length() > 5)
				return true;
		}
		return false;

	}
		


}
