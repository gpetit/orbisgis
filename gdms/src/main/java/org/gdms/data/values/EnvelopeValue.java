package org.gdms.data.values;

import com.vividsolutions.jts.geom.Envelope;

/**
 *
 * @author Vincent Dépériers
 */
public interface EnvelopeValue extends Value {

        /**
         * @param Envelope the envelope to set
         */
        void setValue(Envelope value);
}
