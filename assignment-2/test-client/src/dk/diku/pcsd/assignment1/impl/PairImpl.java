
package dk.diku.pcsd.assignment1.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pairImpl complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pairImpl">
 *   &lt;complexContent>
 *     &lt;extension base="{http://impl.assignment1.pcsd.diku.dk/}pair">
 *       &lt;sequence>
 *         &lt;element name="key" type="{http://impl.assignment1.pcsd.diku.dk/}keyImpl" minOccurs="0"/>
 *         &lt;element name="value" type="{http://impl.assignment1.pcsd.diku.dk/}valueListImpl" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pairImpl", propOrder = {
    "key",
    "value"
})
public class PairImpl
    extends Pair
{

    protected KeyImpl key;
    protected ValueListImpl value;

    /**
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link KeyImpl }
     *     
     */
    public KeyImpl getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyImpl }
     *     
     */
    public void setKey(KeyImpl value) {
        this.key = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link ValueListImpl }
     *     
     */
    public ValueListImpl getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueListImpl }
     *     
     */
    public void setValue(ValueListImpl value) {
        this.value = value;
    }

}
