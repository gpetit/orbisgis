package org.gdms.data.values;

import com.vividsolutions.jts.geom.Envelope;
import org.gdms.data.stream.GeoStream;

/**
 *
 * @author Vincent Dépériers
 */
public interface StreamValue extends Value {

        /**
         * @param Envelope the envelope to set
         */
        void setValue(GeoStream value);
}
