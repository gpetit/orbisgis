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
        
        public DefaultStreamValue(GeoStream geoStream) {
                this.m_GeoStream = geoStream;
        }
        
        @Override
        public BooleanValue equals(Value obj) {
                if (obj instanceof StreamValue) {
                        return ValueFactory.createValue(m_GeoStream.equals(((StreamValue) obj).getAsStream()));
                } else {
                        return ValueFactory.createValue(false);
                }
        }

        @Override
        public int hashCode() {
                return this.m_GeoStream.hashCode();
        }

        @Override
        public String getStringValue(ValueWriter writer) {
                return "Envelope";
        }

        @Override
        public int getType() {
                return Type.STREAM;
        }

        @Override
        public byte[] getBytes() {
                throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setValue(GeoStream value) {
                this.m_GeoStream = value;
        }
        
        @Override
        public GeoStream getAsStream() {
                return this.m_GeoStream;
        }      
}
