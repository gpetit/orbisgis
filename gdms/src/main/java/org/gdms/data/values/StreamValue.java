package org.gdms.data.values;

import com.vividsolutions.jts.geom.Envelope;
import org.gdms.data.stream.GeoStream;

/**
 *
 * @author Vincent Dépériers
 */
public interface StreamValue extends Value {

        /**
         * Set value with the parameter
         * @param Envelope 
         */
        void setValue(GeoStream value);
}
