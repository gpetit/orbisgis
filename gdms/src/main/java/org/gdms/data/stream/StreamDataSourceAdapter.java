package org.gdms.data.stream;

import com.vividsolutions.jts.geom.Envelope;
import java.awt.Image;
import java.util.List;
import org.apache.log4j.Logger;
import org.gdms.data.DataSource;
import org.gdms.data.DriverDataSource;
import org.gdms.data.edition.Commiter;
import org.gdms.data.edition.DeleteEditionInfo;
import org.gdms.data.edition.EditionInfo;
import org.gdms.data.edition.PhysicalRowAddress;
import org.gdms.driver.*;
import org.gdms.driver.wms.SimpleWMSDriver;
import org.gdms.source.CommitListener;
import org.gdms.source.DefaultSourceManager;
import org.gdms.source.Source;
import org.gvsig.remoteClient.wms.ICancellable;
import org.orbisgis.progress.NullProgressMonitor;

/**
 * Adapter to the DataSource interface for stream drivers
 *
 * @author Vincent Dépériers
 */
public class StreamDataSourceAdapter extends DriverDataSource implements Commiter, CommitListener {

    private StreamDriver driver;
    private StreamSource def;
    
    private static final Logger LOG = Logger.getLogger(StreamDataSourceAdapter.class);

    /**
     * Creates a new StreamDataSourceAdapter
     *
     *
     * @param src
     * @param def
     * @param driver
     */
    public StreamDataSourceAdapter(Source src, StreamSource def, StreamDriver driver) {
        super(src);
        this.def = def;
        this.driver = driver;
        LOG.trace("Constructor");
    }

    @Override
    public void open() throws DriverException {
        LOG.trace("Opening");
        driver.open(def);
        fireOpen(this);
        DefaultSourceManager sm = (DefaultSourceManager) getDataSourceFactory().getSourceManager();
        sm.addCommitListener(this);
    }

    @Override
    public void close() throws DriverException {
        LOG.trace("Closing");
        driver.close();
        fireCancel(this);
        DefaultSourceManager sm = (DefaultSourceManager) getDataSourceFactory().getSourceManager();
        sm.removeCommitListener(this);
    }

    /**
     * Save the data in the stream driver
     * @param ds
     * @throws DriverException 
     */
    @Override
    public void saveData(DataSource ds) throws DriverException {
        LOG.trace("Saving Data");
        ds.open();
        ((StreamReadWriteDriver) driver).write(ds, new NullProgressMonitor());
        ds.close();
    }

    /**
     * Get the driver of the SteamData
     * @return 
     */
    @Override
    public Driver getDriver() {
        return driver;
    }

    @Override
    public void syncWithSource() throws DriverException {
        sync();
    }

    @Override
    public void commitDone(String name) throws DriverException {
        sync();
    }

    private void sync() throws DriverException {
        driver.close();
        //Faire ce dont on a besoin en parametre de close et open cf(DBTableDataSourceAdapter)
        driver.open(def);

    }

    /**
     * Commit the StreamDataSource
     * @param rowsDirections
     * @param fieldNames
     * @param schemaActions
     * @param editionActions
     * @param deletedPKs
     * @param modifiedSource
     * @return
     * @throws DriverException 
     */
    @Override
    public boolean commit(List<PhysicalRowAddress> rowsDirections, String[] fieldNames, List<EditionInfo> schemaActions, List<EditionInfo> editionActions, List<DeleteEditionInfo> deletedPKs, DataSource modifiedSource) throws DriverException {
        LOG.trace("Commiting");
        boolean changed = ((StreamReadWriteDriver) driver).write(modifiedSource, new NullProgressMonitor());
        try {
            driver.close();
        } catch (DriverException e) {
            throw new DriverException("Cannot free resources: stream writen ...", e);
        }
        driver.open(def);

        fireCommit(this);

        return changed;
    }

    @Override
    public void isCommiting(String name, Object source) throws DriverException {
    }

    /**
     * This method is used by the {@code Renderer} to know whether or not it is
     * dealing with a stream datasource
     *
     * @return
     */
    @Override
    public boolean isStream() {
        return true;
    }

    @Override
    public Envelope getFullExtent() throws DriverException {
        return getStream(0).getEnvelope();
    }
}
