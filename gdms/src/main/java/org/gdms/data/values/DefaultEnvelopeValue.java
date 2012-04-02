package org.gdms.data.values;

import com.vividsolutions.jts.geom.Envelope;
import org.gdms.data.types.Type;

/**
 *
 * @author Vincent Dépériers
 */


public class DefaultEnvelopeValue extends AbstractValue implements EnvelopeValue {

        private Envelope m_envelope;
        
        public DefaultEnvelopeValue(Envelope envelope) {
                this.m_envelope = envelope;
        }
        
        @Override
        public BooleanValue equals(Value value) {
                if (value instanceof Envelope) {
                        return ValueFactory.createValue(m_envelope.equals(((RasterValue) value).getAsEnvelope()));
                } else {
                        return ValueFactory.createValue(false);
                }
        }

        @Override
        public int hashCode() {
                return this.m_envelope.hashCode();
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
        public void setValue(Envelope value) {
                this.m_envelope = value;
        }
        
        @Override
        public Envelope getAsEnvelope() {
                return this.m_envelope;
        }      
}
