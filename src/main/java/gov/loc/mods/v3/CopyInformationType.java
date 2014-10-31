//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.02.02 at 03:34:01 PM MST 
//


package gov.loc.mods.v3;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for copyInformationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="copyInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="form" type="{http://www.loc.gov/mods/v3}stringPlusAuthority" minOccurs="0"/>
 *         &lt;element name="subLocation" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="shelfLocator" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="electronicLocator" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="note" type="{http://www.loc.gov/mods/v3}stringPlusDisplayLabelPlusType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="enumerationAndChronology" type="{http://www.loc.gov/mods/v3}enumerationAndChronologyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "copyInformationType", propOrder = {
    "form",
    "subLocation",
    "shelfLocator",
    "electronicLocator",
    "note",
    "enumerationAndChronology"
})
public class CopyInformationType {

    protected StringPlusAuthority form;
    protected List<String> subLocation;
    protected List<String> shelfLocator;
    protected List<String> electronicLocator;
    protected List<StringPlusDisplayLabelPlusType> note;
    protected List<EnumerationAndChronologyType> enumerationAndChronology;

    /**
     * Gets the value of the form property.
     * 
     * @return
     *     possible object is
     *     {@link StringPlusAuthority }
     *     
     */
    public StringPlusAuthority getForm() {
        return form;
    }

    /**
     * Sets the value of the form property.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPlusAuthority }
     *     
     */
    public void setForm(StringPlusAuthority value) {
        this.form = value;
    }

    /**
     * Gets the value of the subLocation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subLocation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubLocation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSubLocation() {
        if (subLocation == null) {
            subLocation = new ArrayList<String>();
        }
        return this.subLocation;
    }

    /**
     * Gets the value of the shelfLocator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the shelfLocator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getShelfLocator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getShelfLocator() {
        if (shelfLocator == null) {
            shelfLocator = new ArrayList<String>();
        }
        return this.shelfLocator;
    }

    /**
     * Gets the value of the electronicLocator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the electronicLocator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElectronicLocator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getElectronicLocator() {
        if (electronicLocator == null) {
            electronicLocator = new ArrayList<String>();
        }
        return this.electronicLocator;
    }

    /**
     * Gets the value of the note property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the note property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNote().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StringPlusDisplayLabelPlusType }
     * 
     * 
     */
    public List<StringPlusDisplayLabelPlusType> getNote() {
        if (note == null) {
            note = new ArrayList<StringPlusDisplayLabelPlusType>();
        }
        return this.note;
    }

    /**
     * Gets the value of the enumerationAndChronology property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enumerationAndChronology property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnumerationAndChronology().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnumerationAndChronologyType }
     * 
     * 
     */
    public List<EnumerationAndChronologyType> getEnumerationAndChronology() {
        if (enumerationAndChronology == null) {
            enumerationAndChronology = new ArrayList<EnumerationAndChronologyType>();
        }
        return this.enumerationAndChronology;
    }

}
