//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.08.28 at 12:14:22 PM EDT 
//


package gov.loc.marc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * MARC21 Variable Data Fields 010-999
 * 
 * <p>Java class for dataFieldType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dataFieldType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="subfield" type="{http://www.loc.gov/MARC21/slim}subfieldatafieldType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.loc.gov/MARC21/slim}idDataType" />
 *       &lt;attribute name="tag" use="required" type="{http://www.loc.gov/MARC21/slim}tagDataType" />
 *       &lt;attribute name="ind1" use="required" type="{http://www.loc.gov/MARC21/slim}indicatorDataType" />
 *       &lt;attribute name="ind2" use="required" type="{http://www.loc.gov/MARC21/slim}indicatorDataType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataFieldType", propOrder = {
    "subfield"
})
public class DataFieldType {

    @XmlElement(required = true)
    protected List<SubfieldatafieldType> subfield;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    @XmlAttribute(name = "tag", required = true)
    protected String tag;
    @XmlAttribute(name = "ind1", required = true)
    protected String ind1;
    @XmlAttribute(name = "ind2", required = true)
    protected String ind2;

    /**
     * Gets the value of the subfield property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subfield property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubfield().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubfieldatafieldType }
     * 
     * 
     */
    public List<SubfieldatafieldType> getSubfield() {
        if (subfield == null) {
            subfield = new ArrayList<SubfieldatafieldType>();
        }
        return this.subfield;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the tag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets the value of the tag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTag(String value) {
        this.tag = value;
    }

    /**
     * Gets the value of the ind1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInd1() {
        return ind1;
    }

    /**
     * Sets the value of the ind1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInd1(String value) {
        this.ind1 = value;
    }

    /**
     * Gets the value of the ind2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInd2() {
        return ind2;
    }

    /**
     * Sets the value of the ind2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInd2(String value) {
        this.ind2 = value;
    }

}
