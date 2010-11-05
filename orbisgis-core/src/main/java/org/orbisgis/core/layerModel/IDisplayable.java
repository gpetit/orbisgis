/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 *
 *
 *  Team leader Erwan BOCHER, scientific researcher,
 *
 *  User support leader : Gwendall Petit, geomatic engineer.
 *
 * Previous computer developer : Pierre-Yves FADET, computer engineer, Thomas LEDUC, scientific researcher, Fernando GONZALEZ
 * CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * Copyright (C) 2010 Erwan BOCHER, Alexis GUEGANNO, Maxence LAURENT
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 *
 * or contact directly:
 * info@orbisgis.org
 **/
package org.orbisgis.core.layerModel;

import com.vividsolutions.jts.geom.Envelope;
import java.util.List;
import org.gdms.data.SpatialDataSourceDecorator;
import org.gdms.driver.DriverException;
import org.orbisgis.core.renderer.legend.Legend;

/**
 * A simple interface that will be used to describe
 * @author alexis
 */
public interface IDisplayable {

	/**
	 * Get the LayerCollection which is the parent of this layer.
	 * @return A LayerCollection, or null if this doesn't have any parent.
	 */
	LayerCollection getParent();

	/**
	 * Return the content of the layer collection, or the layer, in an array.
	 * @return
	 */
	public ILayer[] getLayers();

	/**
	 * Get this layer (if it is a raster one) in an array, or the raster layers of this
	 * if this is a collection.
	 * @return
	 */
	public ILayer[] getRasterLayers() throws DriverException;

	/**
	 * Get this layer (if it is a vector one) in an array, or the vector layers of this
	 * if this is a collection.
	 * @return
	 */
	public ILayer[] getVectorLayers() throws DriverException;

	/**
	 * Get this layer (if it is a WMS one) in an array, or the WMS layers of this
	 * if this is a collection.
	 * @return
	 */
	public ILayer[] getWMSLayers() throws DriverException;

	/**
	 * Get the layer(s) as a list.
	 * @return
	 */
	public List<ILayer> getLayerList();

	/**
	 * Get the name of the layer or layer collection
	 * @return
	 */
	public String getName();

	/**
	 * Get the elements as a list of IDisplayable.
	 * @return
	 */
	public List<IDisplayable> getDisplayableList();

	/**
	 * Set the name of the layer or layer collection.
	 * @param name
	 */
	public void setName(String name) throws LayerException;

	/**
	 * Get the context in which this layers lives.
	 * @return
	 */
	public MapContext getMapContext();

	/**
	 * Set the Context in which this layer lives.
	 * @param mc
	 */
	public void setMapContext(MapContext mc);

	/**
	 * Get the Envelope of this layer.
	 * @return
	 */
	public Envelope getEnvelope();

	/**
	 * Determines if this instance of IDisplayable is a collection
	 * @return
	 */
	public boolean isCollection();

	/**
	 * Returns a {@link DataSource} to access the source of this layer
	 *
	 * @return A DataSource or null if this layer is not backed up by a
	 *         DataSource (Layer collections and WMS layers, for example)
	 */
	SpatialDataSourceDecorator getDataSource();

	/**
	 * Open the given layer
	 * @throws LayerException
	 */
	void open() throws LayerException;

	/**
	 * Close the given layer
	 * @throws LayerException
	 */
	void close() throws LayerException;

	/**
	 * Add a LayerListener to this layer.
	 * @param listener
	 */
	void addLayerListener(LayerListener listener);

	/**
	 * Remove the given LayerListener of the listeners associated to this layer.
	 * @param listener
	 */
	void removeLayerListener(LayerListener listener);

	/**
	 * Add a LayerListener to this layer.
	 * @param listener
	 */
	void addLayerListenerRecursively(LayerListener listener);

	/**
	 * Remove the given LayerListener of the listeners associated to this layer.
	 * @param listener
	 */
	void removeLayerListenerRecursively(LayerListener listener);

	/**
	 * Gets an array of the selected rows
	 *
	 * @return
	 * @throws UnsupportedOperationException
	 *             If this layer doesn't support selection
	 */
	int[] getSelection() throws UnsupportedOperationException;

	/**
	 * Sets the array of the selected rows
	 *
	 * @param newSelection
	 * @throws UnsupportedOperationException
	 *             If this layer doesn't support selection
	 */
	void setSelection(int[] newSelection) throws UnsupportedOperationException;

	/**
	 * Tell you if this layer is visible.
	 * @return true if the layer is visible.
	 */
	boolean isVisible();

	/**
	 * Determines if this layer is visible or not.
	 * @param isVisible True if you want the layer to be visible
	 * @throws LayerException
	 */
	void setVisible(final boolean isVisible) throws LayerException;

	/**
	 * Get all the ancestors of this layer, to the root of the tree.
	 * @return
	 *	the ancestors as an array of IDisplayable
	 */
	public IDisplayable[] getLayerPath();

	/**
	 * Try to retrieve the index of the ILayer layer
	 * @param layer
	 * @return
	 */
	public int getIndex(ILayer layer);

	/**
	 * Get the legend used to represent this IDisplayable
	 * @return
	 * @throws DriverException
	 */
	public Legend[] getRenderingLegend() throws DriverException;

	/**
	 * Retrieve the listeners associated to this layer.
	 * @return
	 */
	public List<LayerListener> getListeners();
}
