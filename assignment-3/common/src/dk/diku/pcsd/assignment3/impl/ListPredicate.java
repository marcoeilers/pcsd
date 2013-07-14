package dk.diku.pcsd.assignment3.impl;

import dk.diku.pcsd.keyvaluebase.interfaces.Predicate;

/*
 * Checks if a ValueListImpl contains more than one value.
 */
public class ListPredicate extends Predicate<ValueListImpl>{

	@Override
	public boolean evaluate(ValueListImpl input) {
		return input.getValueList().size() > 1;
	}

}
