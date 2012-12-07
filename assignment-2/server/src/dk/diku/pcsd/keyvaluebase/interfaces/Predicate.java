package dk.diku.pcsd.keyvaluebase.interfaces;

import javax.xml.bind.annotation.XmlSeeAlso;

import dk.diku.pcsd.assignment1.impl.ListPredicate;
import dk.diku.pcsd.assignment1.impl.StringLengthPredicate;

@XmlSeeAlso({StringLengthPredicate.class, ListPredicate.class})
public abstract class Predicate<T> {
	public abstract boolean evaluate(T input);
}
