package org.gdms.driver.stream;

import org.gdms.data.schema.Metadata;
import org.gdms.data.values.Value;
import org.gdms.driver.DataSet;
import org.gdms.driver.DriverException;
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
}
