package org.gdms.driver.wms;

import com.vividsolutions.jts.geom.Envelope;
import java.awt.Image;
import org.gdms.data.schema.Metadata;
import org.gdms.data.values.Value;
import org.gdms.driver.DataSet;
import org.gdms.driver.DriverException;
import org.gdms.driver.stream.AbstractRasterStreamDriver;
import org.gvsig.remoteClient.wms.ICancellable;
import org.orbisgis.progress.ProgressMonitor;



/**
 * The concrete WMSDriver managing driver
 * @author Dorian Goepp <dorian.goepp@centraliens-nantes.net>
 */


public class WMSDriver extends AbstractRasterStreamDriver {

    @Override
    public Value getFieldValue(long rowIndex, int fieldId) throws DriverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getRowCount() throws DriverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Number[] getScope(int dimension) throws DriverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Metadata getMetadata() throws DriverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getPrefixes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean write(DataSet dataSource, ProgressMonitor pm) throws DriverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Image getMap(int width, int height, Envelope extent, ICancellable cancel) throws DriverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
