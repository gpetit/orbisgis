//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-600 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.07 at 03:19:01 PM CEST 
//


package org.orbisgis.renderer.legend.carto.persistence;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.orbisgis.renderer.symbol.collection.persistence.SymbolType;


/**
 * <p>Java class for unique-value-legend-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="unique-value-legend-type">
 *   &lt;complexContent>
 *     &lt;extension base="{org.orbisgis.legend}legend-type">
 *       &lt;sequence>
 *         &lt;element name="default-symbol" type="{org.orbisgis.symbol}symbol-type"/>
 *         &lt;element ref="{org.orbisgis.legend}value-classification" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="default-label" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="field-name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="field-type" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unique-value-legend-type", propOrder = {
    "defaultSymbol",
    "valueClassification"
})
public class UniqueValueLegendType
    extends LegendType
{

    @XmlElement(name = "default-symbol", required = true)
    protected SymbolType defaultSymbol;
    @XmlElement(name = "value-classification", namespace = "org.orbisgis.legend")
    protected List<ValueClassification> valueClassification;
    @XmlAttribute(name = "default-label", required = true)
    protected String defaultLabel;
    @XmlAttribute(name = "field-name", required = true)
    protected String fieldName;
    @XmlAttribute(name = "field-type", required = true)
    protected int fieldType;

    /**
     * Gets the value of the defaultSymbol property.
     * 
     * @return
     *     possible object is
     *     {@link SymbolType }
     *     
     */
    public SymbolType getDefaultSymbol() {
        return defaultSymbol;
    }

    /**
     * Sets the value of the defaultSymbol property.
     * 
     * @param value
     *     allowed object is
     *     {@link SymbolType }
     *     
     */
    public void setDefaultSymbol(SymbolType value) {
        this.defaultSymbol = value;
    }

    /**
     * Gets the value of the valueClassification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the valueClassification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValueClassification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueClassification }
     * 
     * 
     */
    public List<ValueClassification> getValueClassification() {
        if (valueClassification == null) {
            valueClassification = new ArrayList<ValueClassification>();
        }
        return this.valueClassification;
    }

    /**
     * Gets the value of the defaultLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultLabel() {
        return defaultLabel;
    }

    /**
     * Sets the value of the defaultLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultLabel(String value) {
        this.defaultLabel = value;
    }

    /**
     * Gets the value of the fieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the value of the fieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldName(String value) {
        this.fieldName = value;
    }

    /**
     * Gets the value of the fieldType property.
     * 
     */
    public int getFieldType() {
        return fieldType;
    }

    /**
     * Sets the value of the fieldType property.
     * 
     */
    public void setFieldType(int value) {
        this.fieldType = value;
    }

}
