package org.gdms.data.stream;

import org.gdms.source.directory.StreamDefinitionType;
import org.gdms.data.AbstractDataSourceDefinition;
import org.gdms.data.DataSource;
import org.gdms.data.DataSourceCreationException;
import org.gdms.driver.DataSet;
import org.gdms.driver.Driver;
import org.gdms.driver.DriverException;
import org.gdms.source.directory.DefinitionType;
import org.orbisgis.progress.ProgressMonitor;
import org.apache.log4j.Logger;
import org.gdms.data.DataSourceDefinition;
import org.gdms.driver.*;
import org.gdms.driver.driverManager.DriverManager;

/**
 *
 * <p>Definition of stream source<br> This is in a way the interface between the
 * management of the data itself and their integration into orbisgis.<br> Here
 * we will store the StreamSource to give it as a parameter when we create the {@code StreamDatasource},
 * through the {@code StreamDataSourceAdapter}. </p>
 *
 * @author Vincent Dépériers
 */
public class StreamSourceDefinition extends AbstractDataSourceDefinition {

    private StreamSource m_streamSource;
    private static final Logger LOG = Logger.getLogger(StreamSourceDefinition.class);

    public StreamSourceDefinition(StreamSource streamSource) {
        LOG.trace("Constructor");
        this.m_streamSource = streamSource;
    }

    /**
     *
     * @param obj
     * @return true if the object is an instance of StreamSourceDifinition
     * having the same attributes values as the m_StreamSource
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StreamSourceDefinition) {
            StreamSourceDefinition ssd = (StreamSourceDefinition) obj;
            return (equals(ssd.m_streamSource.getDbms(), m_streamSource.getDbms())
                    && equals(ssd.m_streamSource.getLayerName(), m_streamSource.getLayerName())
                    && equals(ssd.m_streamSource.getHost(), m_streamSource.getHost())
                    && equals(ssd.m_streamSource.getPassword(), m_streamSource.getPassword())
                    && (ssd.m_streamSource.getPort() == m_streamSource.getPort())
                    && equals(ssd.m_streamSource.getUser(), m_streamSource.getUser())
                    && equals(ssd.m_streamSource.getPrefix(), m_streamSource.getPrefix()));
        } else {
            return false;
        }
    }

    private boolean equals(String str, String str2) {
        if (str == null) {
            return str2 == null;
        } else {
            return str.equals(str2);
        }
    }

    @Override
    public int hashCode() {
        return 48 + m_streamSource.hashCode();
    }

    public StreamSource getStreamSource() {
        return this.m_streamSource;
    }

    @Override
    protected Driver getDriverInstance() {
        return DriverUtilities.getStreamDriver(getDataSourceFactory().getSourceManager().getDriverManager(), m_streamSource.getPrefix());
    }

    @Override
    public DataSource createDataSource(String tableName, ProgressMonitor pm) throws DataSourceCreationException {
        LOG.trace("Creating datasource");
        getDriver().setDataSourceFactory(getDataSourceFactory());

        StreamDataSourceAdapter sdsa = new StreamDataSourceAdapter(
                getSource(tableName), m_streamSource, (StreamDriver) getDriver());
        sdsa.setDataSourceFactory(getDataSourceFactory());

        LOG.trace("Datasource created");
        return sdsa;
    }

    @Override
    public void createDataSource(DataSet contents, ProgressMonitor pm) throws DriverException {
        throw new UnsupportedOperationException("Cannot create stream sources");
    }

    @Override
    public DefinitionType getDefinition() {
        StreamDefinitionType ret = new StreamDefinitionType();
        ret.setLayerName(m_streamSource.getLayerName());
        ret.setHost(m_streamSource.getHost());
        //ret.setPort(Integer.toString(m_streamSource.getPort()));
        //ret.setPassword(m_streamSource.getPassword());
        //ret.setUser(m_streamSource.getUser());
        //ret.setPrefix(m_streamSource.getPrefix());
        ret.setImageFormat(m_streamSource.getImageFormat());
        ret.setSRS(m_streamSource.getSRS());

        return ret;
    }

    @Override
    public String getDriverTableName() {
        return DriverManager.DEFAULT_SINGLE_TABLE_NAME;
    }

    public StreamSource getSourceDefinition() {
        return this.m_streamSource;
    }

    public static DataSourceDefinition createFromXML(StreamDefinitionType definition) {
        StreamSource streamSource = new StreamSource(definition.getHost(), Integer.parseInt(definition.getPort()),
                definition.getLayerName(), definition.getPrefix(), 
                definition.getImageFormat(), definition.getSRS(), 
                definition.getUser(), definition.getPassword());

        return new StreamSourceDefinition(streamSource);
    }
}
