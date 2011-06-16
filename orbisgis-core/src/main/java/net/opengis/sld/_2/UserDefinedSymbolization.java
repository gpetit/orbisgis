//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.24 at 12:13:09 PM CEST 
//


package net.opengis.sld._2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import net.opengis.wms._2.ExtendedCapabilitiesType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/wms/2.0}_ExtendedCapabilitiesType">
 *       &lt;attribute name="SupportSLD" type="{http://www.w3.org/2001/XMLSchema}boolean" default="0" />
 *       &lt;attribute name="UserLayer" type="{http://www.w3.org/2001/XMLSchema}boolean" default="0" />
 *       &lt;attribute name="UserStyle" type="{http://www.w3.org/2001/XMLSchema}boolean" default="0" />
 *       &lt;attribute name="RemoteWFS" type="{http://www.w3.org/2001/XMLSchema}boolean" default="0" />
 *       &lt;attribute name="InlineFeature" type="{http://www.w3.org/2001/XMLSchema}boolean" default="0" />
 *       &lt;attribute name="RemoteWCS" type="{http://www.w3.org/2001/XMLSchema}boolean" default="0" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class UserDefinedSymbolization
    extends ExtendedCapabilitiesType
{

    @XmlAttribute(name = "SupportSLD")
    protected Boolean supportSLD;
    @XmlAttribute(name = "UserLayer")
    protected Boolean userLayer;
    @XmlAttribute(name = "UserStyle")
    protected Boolean userStyle;
    @XmlAttribute(name = "RemoteWFS")
    protected Boolean remoteWFS;
    @XmlAttribute(name = "InlineFeature")
    protected Boolean inlineFeature;
    @XmlAttribute(name = "RemoteWCS")
    protected Boolean remoteWCS;

    /**
     * Gets the value of the supportSLD property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSupportSLD() {
        if (supportSLD == null) {
            return false;
        } else {
            return supportSLD;
        }
    }

    /**
     * Sets the value of the supportSLD property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSupportSLD(Boolean value) {
        this.supportSLD = value;
    }

    /**
     * Gets the value of the userLayer property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isUserLayer() {
        if (userLayer == null) {
            return false;
        } else {
            return userLayer;
        }
    }

    /**
     * Sets the value of the userLayer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUserLayer(Boolean value) {
        this.userLayer = value;
    }

    /**
     * Gets the value of the userStyle property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isUserStyle() {
        if (userStyle == null) {
            return false;
        } else {
            return userStyle;
        }
    }

    /**
     * Sets the value of the userStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUserStyle(Boolean value) {
        this.userStyle = value;
    }

    /**
     * Gets the value of the remoteWFS property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isRemoteWFS() {
        if (remoteWFS == null) {
            return false;
        } else {
            return remoteWFS;
        }
    }

    /**
     * Sets the value of the remoteWFS property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRemoteWFS(Boolean value) {
        this.remoteWFS = value;
    }

    /**
     * Gets the value of the inlineFeature property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isInlineFeature() {
        if (inlineFeature == null) {
            return false;
        } else {
            return inlineFeature;
        }
    }

    /**
     * Sets the value of the inlineFeature property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInlineFeature(Boolean value) {
        this.inlineFeature = value;
    }

    /**
     * Gets the value of the remoteWCS property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isRemoteWCS() {
        if (remoteWCS == null) {
            return false;
        } else {
            return remoteWCS;
        }
    }

    /**
     * Sets the value of the remoteWCS property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRemoteWCS(Boolean value) {
        this.remoteWCS = value;
    }

}
