
package dk.diku.pcsd.assignment1.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for valueListImpl complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="valueListImpl">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="valueList" type="{http://impl.assignment1.pcsd.diku.dk/}valueImpl" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "valueListImpl", propOrder = {
    "valueList"
})
public class ValueListImpl {

    @XmlElement(nillable = true)
    protected List<ValueImpl> valueList;

    /**
     * Gets the value of the valueList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valueList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValueList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueImpl }
     * 
     * 
     */
    public List<ValueImpl> getValueList() {
        if (valueList == null) {
            valueList = new ArrayList<ValueImpl>();
        }
        return this.valueList;
    }

}
