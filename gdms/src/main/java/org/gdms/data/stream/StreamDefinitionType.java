package org.gdms.data.stream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.gdms.data.DataSourceDefinition;
import org.gdms.source.directory.DefinitionType;

/**
 *
 * @author Vincent Dépériers
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Stream-definition-type")
public class StreamDefinitionType extends DefinitionType {

        @XmlAttribute(required = true)
        protected String host;
        @XmlAttribute(required = true)
        protected String port;
        @XmlAttribute(name = "layer-name", required = true)
        protected String layerName;
        @XmlAttribute(required = true)
        protected String user;
        @XmlAttribute(required = true)
        protected String password;
        @XmlAttribute(required = true)
        protected String prefix;

        /**
         * Gets the value of the host property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHost() {
                return host;
        }

        /**
         * Sets the value of the host property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHost(String value) {
                this.host = value;
        }

        /**
         * Gets the value of the port property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPort() {
                return port;
        }

        /**
         * Sets the value of the port property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPort(String value) {
                this.port = value;
        }

        /**
         * Gets the value of the tableName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLayerName() {
                return layerName;
        }

        /**
         * Sets the value of the tableName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLayerName(String value) {
                this.layerName = value;
        }

        /**
         * Gets the value of the user property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUser() {
                return user;
        }

        /**
         * Sets the value of the user property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUser(String value) {
                this.user = value;
        }

        /**
         * Gets the value of the password property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPassword() {
                return password;
        }

        /**
         * Sets the value of the password property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPassword(String value) {
                this.password = value;
        }

        /**
         * Gets the value of the prefix property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPrefix() {
                return prefix;
        }

        /**
         * Sets the value of the prefix property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPrefix(String value) {
                this.prefix = value;
        }


        @Override
        public DataSourceDefinition toDataSourceDefinition() {
                return StreamSourceDefinition.createFromXML(this);
        }
}
