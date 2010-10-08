//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.10.07 at 04:50:41 PM CEST 
//


package org.orbisgis.core.renderer.persistance.se;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AxisChartType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AxisChartType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/se}GraphicType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/se}UnitOfMeasure" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}Transform" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}Normalization" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}CategoryWidth"/>
 *         &lt;element ref="{http://www.opengis.net/se}CategoryGap" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}AxisScale"/>
 *         &lt;choice>
 *           &lt;element ref="{http://www.opengis.net/se}Categories"/>
 *           &lt;element ref="{http://www.opengis.net/se}StackedBars"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AxisChartType", propOrder = {
    "unitOfMeasure",
    "transform",
    "normalization",
    "categoryWidth",
    "categoryGap",
    "axisScale",
    "categories",
    "stackedBars"
})
public class AxisChartType
    extends GraphicType
{

    @XmlElement(name = "UnitOfMeasure")
    @XmlSchemaType(name = "anyURI")
    protected String unitOfMeasure;
    @XmlElement(name = "Transform")
    protected TransformType transform;
    @XmlElement(name = "Normalization")
    protected ParameterValueType normalization;
    @XmlElement(name = "CategoryWidth", required = true)
    protected ParameterValueType categoryWidth;
    @XmlElement(name = "CategoryGap")
    protected ParameterValueType categoryGap;
    @XmlElement(name = "AxisScale", required = true)
    protected AxisScaleType axisScale;
    @XmlElement(name = "Categories")
    protected CategoriesType categories;
    @XmlElement(name = "StackedBars")
    protected StackedBarsType stackedBars;

    /**
     * Gets the value of the unitOfMeasure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /**
     * Sets the value of the unitOfMeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitOfMeasure(String value) {
        this.unitOfMeasure = value;
    }

    /**
     * Gets the value of the transform property.
     * 
     * @return
     *     possible object is
     *     {@link TransformType }
     *     
     */
    public TransformType getTransform() {
        return transform;
    }

    /**
     * Sets the value of the transform property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransformType }
     *     
     */
    public void setTransform(TransformType value) {
        this.transform = value;
    }

    /**
     * Gets the value of the normalization property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterValueType }
     *     
     */
    public ParameterValueType getNormalization() {
        return normalization;
    }

    /**
     * Sets the value of the normalization property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterValueType }
     *     
     */
    public void setNormalization(ParameterValueType value) {
        this.normalization = value;
    }

    /**
     * Gets the value of the categoryWidth property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterValueType }
     *     
     */
    public ParameterValueType getCategoryWidth() {
        return categoryWidth;
    }

    /**
     * Sets the value of the categoryWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterValueType }
     *     
     */
    public void setCategoryWidth(ParameterValueType value) {
        this.categoryWidth = value;
    }

    /**
     * Gets the value of the categoryGap property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterValueType }
     *     
     */
    public ParameterValueType getCategoryGap() {
        return categoryGap;
    }

    /**
     * Sets the value of the categoryGap property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterValueType }
     *     
     */
    public void setCategoryGap(ParameterValueType value) {
        this.categoryGap = value;
    }

    /**
     * Gets the value of the axisScale property.
     * 
     * @return
     *     possible object is
     *     {@link AxisScaleType }
     *     
     */
    public AxisScaleType getAxisScale() {
        return axisScale;
    }

    /**
     * Sets the value of the axisScale property.
     * 
     * @param value
     *     allowed object is
     *     {@link AxisScaleType }
     *     
     */
    public void setAxisScale(AxisScaleType value) {
        this.axisScale = value;
    }

    /**
     * Gets the value of the categories property.
     * 
     * @return
     *     possible object is
     *     {@link CategoriesType }
     *     
     */
    public CategoriesType getCategories() {
        return categories;
    }

    /**
     * Sets the value of the categories property.
     * 
     * @param value
     *     allowed object is
     *     {@link CategoriesType }
     *     
     */
    public void setCategories(CategoriesType value) {
        this.categories = value;
    }

    /**
     * Gets the value of the stackedBars property.
     * 
     * @return
     *     possible object is
     *     {@link StackedBarsType }
     *     
     */
    public StackedBarsType getStackedBars() {
        return stackedBars;
    }

    /**
     * Sets the value of the stackedBars property.
     * 
     * @param value
     *     allowed object is
     *     {@link StackedBarsType }
     *     
     */
    public void setStackedBars(StackedBarsType value) {
        this.stackedBars = value;
    }

}
