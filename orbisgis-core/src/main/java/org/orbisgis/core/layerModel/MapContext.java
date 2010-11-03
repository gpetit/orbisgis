/*
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
 */
package org.orbisgis.core.layerModel;

import java.awt.image.BufferedImage;

import org.orbisgis.progress.IProgressMonitor;

import com.vividsolutions.jts.geom.Envelope;
import java.util.List;
import java.util.Set;

/**
 * This interface provides information to the tool system and receives
 * notifications from it. Also registers the tool system as a listener in order
 * to notify it about certain events during edition
 */
public interface MapContext {

	/**
	 * Gets the root layer of the layer collection in this edition context
	 * 
	 * @return
	 * @throws IllegalStateException
	 *             If the map is closed
	 */
	List<IDisplayable> getLayerModel() throws IllegalStateException;

        /**
         * Try to retrieve a layer by its name.
         * @param name
         * @return
         */
        public IDisplayable getLayerByName(String name);

	/**
	 * Get the layer at index i
	 * @param i
	 * @return
	 */
	public IDisplayable getLayer(int i);

	/**
	 * Gets all the layers in the map context
	 * 
	 * @return
	 * @throws IllegalStateException
	 *             If the map is closed
	 */
	public ILayer[] getLayers() throws IllegalStateException;

        /**
         * Get all the raster layers of this map context as an array of ILayer. 
         * @return
         * @throws IllegalStateException
         */
        public ILayer[] getRasterLayers() throws IllegalStateException;

        /**
         * Get all the vectorial layers of this map context as an array of ILayer.
         * @return
         * @throws IllegalStateException
         */
        public ILayer[] getVectorLayers() throws IllegalStateException;

        /**
         * Get all the WMS layers of this map context as an array of ILayer.
         * @return
         * @throws IllegalStateException
         */
        public ILayer[] getWMSLayers() throws IllegalStateException;
	/**
	 * Gets the selected layers
	 * 
	 * @return
	 * @throws IllegalStateException
	 *             If the map is closed
	 */
	public ILayer[] getSelectedLayers() throws IllegalStateException;

        /**
         * Get the number of layers in this MapContext. We just consider the first level
         * of layers, i.e. inner layers of the layercollections are not counted.
         * @return
         */
        public int getLayerCount();

        /**
         * Count all the layers of this context, ie in both levels.
         * @return
         */
        public int getAllLayersCount();

	/**
	 * Adds a listener for map context events
	 * 
	 * @param listener
	 */
	public void addMapContextListener(MapContextListener listener);

	/**
	 * This method is uses instead of get id to have a unique id for a
	 * mapcontext that cannot change.Based on time creation
	 * 
	 * @return a unique identifier for the mapContext
	 */
	long getIdTime();

	/**
	 * Get the mapcontext boundingbox (visible layers)
	 * 
	 * @return
	 */
	public Envelope getBoundingBox();

	/**
	 * Set the mapcontext boundingbox (visible layers)
	 * 
	 * @param extent
	 */
	void setBoundingBox(Envelope extent);

        /**
         * Get the envelope of the layers of this map context
         * @return
         */
        public Envelope getEnvelope();

        /**
         * Retrieve the names of all the layers in this MapContext
         * @return
         */
        public Set<String> getAllLayersNames();

	/**
	 * Removes a listener for map context events
	 * 
	 * @param listener
	 */
	public void removeMapContextListener(MapContextListener listener);

	/**
	 * Sets the selected layers. If the specified layers are not in the map
	 * context they are removed from selection.
	 * 
	 * @param selectedLayers
	 * @throws IllegalStateException
	 *             If the map is closed
	 */
	public void setSelectedLayers(IDisplayable[] selectedLayers)
			throws IllegalStateException;

	/**
	 * Returns a JAXB object containing all the persistent information of this
	 * MapContext
	 * 
	 * @return
	 */
	Object getJAXBObject();

	/**
	 * Populates the content of this MapContext with the information stored in
	 * the specified JAXB Object. The map must be closed.
	 * 
	 * @param jaxbObject
	 * @throws IllegalStateException
	 *             If the map is open
	 */
	void setJAXBObject(Object jaxbObject) throws IllegalStateException;

	/**
	 * Opens all the layers in the map. All layers added to an open map are
	 * opened automatically. Layers that cannot be created are removed from the
	 * layer tree and an error message is sent to the ErrorManager service
	 * 
	 * @param pm
	 * @throws LayerException
	 *             If some layer cannot be open. In this case all already open
	 *             layers are closed again
	 * @throws IllegalStateException
	 *             If the map is already open
	 */
	void open(IProgressMonitor pm) throws LayerException, IllegalStateException;

	/**
	 * Closes all the layers in the map
	 * 
	 * @param pm
	 * @throws IllegalStateException
	 *             If the map is closed
	 */
	void close(IProgressMonitor pm) throws IllegalStateException;

	/**
	 * Return true if this map context is open and false otherwise
	 * 
	 * @return
	 */
	boolean isOpen();

	/**
	 * Draws an image of the layers in the specified image.
	 * 
	 * @param inProcessImage
	 *            Image where the drawing will take place
	 * @param extent
	 *            Extent of the data to take into account. It must have the same
	 *            proportions than the image
	 * @param pm
	 *            Object to report process and check the cancelation condition
	 * @throws IllegalStateException
	 *             If the map is closed
	 */
	void draw(BufferedImage inProcessImage, Envelope extent, IProgressMonitor pm)
			throws IllegalStateException;

	/**
	 * Gets the layer where all the edition actions take place
	 * 
	 * @return
	 * @throws IllegalStateException
	 *             If the map is closed
	 */
	IDisplayable getActiveLayer() throws IllegalStateException;

	/**
	 * Sets the layer where all the edition actions take place
	 * 
	 * @return
	 * @throws IllegalStateException
	 *             If the map is closed
	 */
	void setActiveLayer(IDisplayable activeLayer) throws IllegalStateException;

	/**
	 * get the mapcontext {@link CoordinateReferenceSystem}
	 * 
	 * @return
	 */

	// CoordinateReferenceSystem getCoordinateReferenceSystem();

	/**
	 * set the {@link CoordinateReferenceSystem} to the mapcontext
	 * 
	 * @param crs
	 */
	// void setCoordinateReferenceSystem(CoordinateReferenceSystem crs);

        /**
         * Add a new Layer or LayerCollection in this context
         * @param layer
         * @throws LayerException
         */
        public void add(IDisplayable layer) throws LayerException;

        /**
         * Remove a Layer or LayerCollection from this context
         * @param layer
         * @throws LayerException
         */
        public void remove(IDisplayable layer) throws LayerException;

        /**
         * Move a Layer or LayerCollection in this context. <code>layer</code> will be placed
	 * just before <code>position</code> in this list.
         * @param layer
         * @param i
         * @throws LayerException
         */
        public void moveLayerBefore(IDisplayable layer, IDisplayable position) throws LayerException;

        /**
         * Remove the layer listener ll from all the LayerListeners associated to
         * the layers and layer collections in this map context.
         * @param ll
         */
        public void removeLayerListenerRecursively(LayerListener ll);

         /**
         * Add the layer listener ll to all the LayerListeners associated to
         * the layers and layer collections in this map context.
         * @param ll
         */
        public void addLayerListenerRecursively(LayerListener ll);

        /**
         * Insert the displayable artifact dis at the index i in the collection of layers.
         * @param dis
         * @param i
         * @throws IndexOutOfBoundsException
         */
        public void insertLayer(IDisplayable dis, int i) throws IndexOutOfBoundsException;
}
