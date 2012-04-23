package org.gdms.data.values;

import com.vividsolutions.jts.geom.Envelope;
import org.gdms.data.stream.GeoStream;
import org.gdms.data.types.Type;

/**
 *
 * @author Vincent Dépériers
 */


public class DefaultStreamValue extends AbstractValue implements StreamValue {

        private GeoStream m_GeoStream;
        
        /**
         * Creat a new DefaultStreamValue
         * @param geoStream 
         */
        public DefaultStreamValue(GeoStream geoStream) {
                this.m_GeoStream = geoStream;
        }
        
        /**
         * compare to an Value-objet 
         * @param obj
         * @return 
         */
        @Override
        public BooleanValue equals(Value obj) {
                if (obj instanceof StreamValue) {
                        return ValueFactory.createValue(m_GeoStream.equals(((StreamValue) obj).getAsStream()));
                } else {
                        return ValueFactory.createValue(false);
                }
        }

        /**
         * Get the hashCode
         * @return 
         */
        @Override
        public int hashCode() {
                return this.m_GeoStream.hashCode();
        }

        /**
         * Get the String Value, "Envelope"
         * @param writer
         * @return 
         */
        @Override
        public String getStringValue(ValueWriter writer) {
                return "Envelope";
        }

        /**
         * Get the type of the value-Stream
         * @return 
         */
        @Override
        public int getType() {
                return Type.STREAM;
        }

        /**
         * Get the bytes
         * @return 
         */
        @Override
        public byte[] getBytes() {
                throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Set the value with a parameter
         * @param value 
         */
        @Override
        public void setValue(GeoStream value) {
                this.m_GeoStream = value;
        }
        
        /**
         * Get the GeoStream
         * @return 
         */
        @Override
        public GeoStream getAsStream() {
                return this.m_GeoStream;
        }      
}
