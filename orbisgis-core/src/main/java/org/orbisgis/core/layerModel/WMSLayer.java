package org.orbisgis.core.layerModel;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

import org.gdms.data.AlreadyClosedException;
import org.gdms.data.DataSource;
import org.gdms.driver.DriverException;
import org.grap.model.GeoRaster;
import org.gvsig.remoteClient.utils.BoundaryBox;
import org.orbisgis.core.layerModel.persistence.LayerType;
import org.orbisgis.core.renderer.legend.Legend;
import org.orbisgis.core.renderer.legend.RasterLegend;
import org.orbisgis.core.renderer.legend.WMSLegend;
import org.orbisgis.utils.I18N;

import com.vividsolutions.jts.geom.Envelope;
import org.gdms.driver.wms.SimpleWMSDriver;
import org.orbisgis.core.Services;

public class WMSLayer extends GdmsLayer {

    private static final String NOT_SUPPORTED = I18N.getString("orbisgis-core.org.orbisgis.wMSLayer.methodNotSupportedInWMSLayer"); //$NON-NLS-1$
    private DataSource ds;
    private String wmslayerName;
    private SimpleWMSDriver driver;

    public WMSLayer(String name, DataSource ds) {
        super(name);
        this.ds = ds;
    }

    @Override
    public DataSource getDataSource() {
        return null;
    }

    @Override
    public Envelope getEnvelope() {
        return getDriver().getEnvelope();
        /*
        Envelope result = new Envelope();

        if (null != ds) {
            try {
                result = ds.getFullExtent();
            } catch (DriverException e) {
                Services.getErrorManager().error(
                        I18N.getString("org.orbisgis.layerModel.layer.cannotGetTheExtentOfLayer") //$NON-NLS-1$
                        + ds.getName(), e);
            }
        }
        return result;
        */
        
    }

    public SimpleWMSDriver getDriver() {
        return driver;
    }

    @Override
    public GeoRaster getRaster() throws DriverException {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public RasterLegend[] getRasterLegend() throws DriverException,
            UnsupportedOperationException {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public RasterLegend[] getRasterLegend(String fieldName)
            throws IllegalArgumentException, DriverException {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public Legend[] getRenderingLegend() throws DriverException {
        try {
        return new Legend[]{
            new WMSLegend(
                ((SimpleWMSDriver)ds.getDriver()).getWMSClient(),
                ((SimpleWMSDriver)ds.getDriver()).getWMSStatus(),
                wmslayerName)};
        } catch(ConnectException e) {
            throw new DriverException(e);
        } catch(IOException e) {
            throw new DriverException(e);
        }
    }

    @Override
    public int[] getSelection() {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public Legend[] getVectorLegend() throws DriverException,
            UnsupportedOperationException {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public Legend[] getVectorLegend(String fieldName)
            throws IllegalArgumentException, DriverException {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

//    public WMSLegend getWMSLegend() {
//        return new WMSLegend(getWMSConnection(), wmslayerName);
//
//    }

    @Override
    public boolean isRaster() throws DriverException {
        return false;
    }

    @Override
    public boolean isVectorial() throws DriverException {
        return false;
    }

    @Override
    public void open() throws LayerException {
        super.open();
        try {
            ds.open();
            driver = (SimpleWMSDriver) ds.getDriver();
            /*
             * String host = driver.getHost();              *
             *
             *
             * //String host = ds.getString(0, "host"); //$NON-NLS-1$
             * System.out.println("Host : " + host);
             *
             *
             * WMSClient client = WMSClientPool.getWMSClient(host);
             * client.getCapabilities(null, false, null); WMSStatus status = new
             * WMSStatus(); wmslayerName = ds.getString(0, "layer");
             * //$NON-NLS-1$ status.addLayerName(wmslayerName);
             * status.setSrs(ds.getString(0, "srs")); //$NON-NLS-1$
             *
             * BoundaryBox bbox = getLayerBoundingBox(wmslayerName, client
             * .getRootLayer(), status.getSrs()); status.setExtent(new
             * Rectangle2D.Double(bbox.getXmin(), bbox .getYmin(),
             * bbox.getXmax() - bbox.getXmin(), bbox.getYmax() -
             * bbox.getYmin())); envelope = new Envelope(bbox.getXmin(),
             * bbox.getXmax(), bbox .getYmin(), bbox.getYmax());
             * status.setFormat(ds.getString(0, "format")); //$NON-NLS-1$
             * connection = new WMSConnection(client, status);
             *
             */

            //TODO A revoir une fois que le gdms gerera tout les clients
            //WMSClient client = driver.getWMSClient();
            //connection = new WMSConnection(client, driver.getWMSStatus());
            ds.close();
        } catch (AlreadyClosedException e) {
            throw new LayerException(I18N.getString("orbisgis-core.org.orbisgis.wMSLayer.bug"), e); //$NON-NLS-1$
        } catch (DriverException e) {
            throw new LayerException(I18N.getString("orbisgis-core.org.orbisgis.wMSLayer.cannotOpenWMSDescription"), e); //$NON-NLS-1$
//        } catch (ConnectException e) {
//            throw new LayerException(I18N.getString("orbisgis-core.org.orbisgis.wMSLayer.cannotConnectToWmsServer"), e); //$NON-NLS-1$
//        } catch (IOException e) {
//            throw new LayerException(I18N.getString("orbisgis-core.org.orbisgis.wMSLayer.cannotRetrieveWMSServerContent"), e); //$NON-NLS-1$
        }
    }

    private org.gvsig.remoteClient.wms.WMSLayer find(String layerName,
            org.gvsig.remoteClient.wms.WMSLayer layer) {
        if (layerName.equals(layer.getName())) {
            return layer;
        } else {
            ArrayList<?> children = layer.getChildren();
            for (Object object : children) {
                org.gvsig.remoteClient.wms.WMSLayer child = (org.gvsig.remoteClient.wms.WMSLayer) object;
                org.gvsig.remoteClient.wms.WMSLayer ret = find(layerName, child);
                if (ret != null) {
                    return ret;
                }
            }
        }

        return null;
    }

    private BoundaryBox getLayerBoundingBox(String layerName,
            org.gvsig.remoteClient.wms.WMSLayer layer, String srs) {
        org.gvsig.remoteClient.wms.WMSLayer wmsLayer = find(layerName, layer);
        // Obtain the bbox at current level
        BoundaryBox bbox = wmsLayer.getBbox(srs);
        while ((bbox == null) && (wmsLayer.getParent() != null)) {
            wmsLayer = wmsLayer.getParent();
            bbox = wmsLayer.getBbox(srs);
        }

        // Some wrong bbox to not have null pointer exceptions
        if (bbox == null) {
            bbox = new BoundaryBox();
            bbox.setXmin(0);
            bbox.setYmin(0);
            bbox.setXmax(100);
            bbox.setYmax(100);
            bbox.setSrs(srs);
        }
        return bbox;
    }

    public boolean isWMS() {
        return true;
    }

    @Override
    public void restoreLayer(LayerType layer) throws LayerException {
        this.setName(layer.getName());
        this.setVisible(layer.isVisible());
    }

    @Override
    public LayerType saveLayer() {
        LayerType ret = new LayerType();
        ret.setName(getName());
        ret.setSourceName(getMainName());
        ret.setVisible(isVisible());
        return ret;
    }

    @Override
    public void setLegend(Legend... legends) throws DriverException {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void setLegend(String fieldName, Legend... legends)
            throws IllegalArgumentException, DriverException {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void setSelection(int[] newSelection) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public boolean isStream() {
        return true;
    }
}
