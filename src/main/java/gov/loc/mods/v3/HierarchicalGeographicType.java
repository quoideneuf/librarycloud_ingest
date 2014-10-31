//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.02.02 at 03:34:01 PM MST 
//


package gov.loc.mods.v3;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 *  752
 * 
 * <p>Java class for hierarchicalGeographicType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="hierarchicalGeographicType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="extraterrestrialArea" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="continent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="country" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="province" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="region" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="territory" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="county" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="citySection" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="island" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="area" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "hierarchicalGeographicType", propOrder = {
    "extraterrestrialAreaOrContinentOrCountry"
})
public class HierarchicalGeographicType {

    @XmlElementRefs({
        @XmlElementRef(name = "extraterrestrialArea", namespace = "http://www.loc.gov/mods/v3", type = JAXBElement.class),
        @XmlElementRef(name = "county", namespace = "http://www.loc.gov/mods/v3", type = JAXBElement.class),
        @XmlElementRef(name = "region", namespace = "http://www.loc.gov/mods/v3", type = JAXBElement.class),
        @XmlElementRef(name = "island", namespace = "http://www.loc.gov/mods/v3", type = JAXBElement.class),
        @XmlElementRef(name = "area", namespace = "http://www.loc.gov/mods/v3", type = JAXBElement.class),
        @XmlElementRef(name = "continent", namespace = "http://www.loc.gov/mods/v3", type = JAXBElement.class),
        @XmlElementRef(name = "citySection", namespace = "http://www.loc.gov/mods/v3", type = JAXBElement.class),
        @XmlElementRef(name = "province", namespace = "http://www.loc.gov/mods/v3", type = JAXBElement.class),
        @XmlElementRef(name = "city", namespace = "http://www.loc.gov/mods/v3", type = JAXBElement.class),
        @XmlElementRef(name = "territory", namespace = "http://www.loc.gov/mods/v3", type = JAXBElement.class),
        @XmlElementRef(name = "country", namespace = "http://www.loc.gov/mods/v3", type = JAXBElement.class),
        @XmlElementRef(name = "state", namespace = "http://www.loc.gov/mods/v3", type = JAXBElement.class)
    })
    protected List<JAXBElement<String>> extraterrestrialAreaOrContinentOrCountry;

    /**
     * Gets the value of the extraterrestrialAreaOrContinentOrCountry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extraterrestrialAreaOrContinentOrCountry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtraterrestrialAreaOrContinentOrCountry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<String>> getExtraterrestrialAreaOrContinentOrCountry() {
        if (extraterrestrialAreaOrContinentOrCountry == null) {
            extraterrestrialAreaOrContinentOrCountry = new ArrayList<JAXBElement<String>>();
        }
        return this.extraterrestrialAreaOrContinentOrCountry;
    }

}
