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

import org.gdms.data.types.Type;
import org.gdms.driver.DriverException;
import org.grap.model.GeoRaster;
import org.orbisgis.core.layerModel.persistence.LayerType;
import org.orbisgis.core.renderer.legend.Legend;
import org.orbisgis.core.renderer.legend.RasterLegend;
import org.orbisgis.core.renderer.legend.WMSLegend;

public interface ILayer extends IDisplayable {

        /**
         * Set the LayerCollection parent as the parent of this layer.
         * @param parent
         * @throws LayerException
         */
	void setParent(final LayerCollection parent) throws LayerException;

        /**
         * Move this layer in the LayerCollection layercol at the index index.
         * @param layercol
         * @param index
         * @throws LayerException
         */
	void moveTo(LayerCollection layercol, int index) throws LayerException;

        /**
         * Move this layer into the LayerCollection layercol
         * @param layercol
         * @throws LayerException
         */
	void moveTo(LayerCollection layercol) throws LayerException;

	/**
	 * Gets the status of this object as a xml object
	 * 
	 * @return
	 */
	LayerType saveLayer();

	/**
	 * Sets the status of the layer from a xml object
	 * 
	 * @param layer
	 * @throws LayerException
	 *             If the status cannot be set
	 */
	void restoreLayer(LayerType layer) throws LayerException;

	WMSLegend getWMSLegend();

	/**
	 * Returns true if the default spatial field of this layer is of type
	 * {@link Type}.RASTER. Return false if the layer is a collection of layers
	 * or it doesn't contain any spatial field
	 * 
	 * @return
	 * @throws DriverException
	 */
	boolean isRaster() throws DriverException;

	/**
	 * Returns true if the default spatial field of this layer is of type
	 * {@link Type}.GEOMETRY. Return false if the layer is a collection of
	 * layers or it doesn't contain any spatial field
	 * 
	 * @return
	 * @throws DriverException
	 */
	boolean isVectorial() throws DriverException;

	/**
	 * Returns true if this layer represents a WMS source
	 * 
	 * @return
	 */
	boolean isWMS();

	/**
	 * Gets the legend used to draw the default spatial field in this layer if
	 * it is of type raster.
	 * 
	 * @return
	 * @throws DriverException
	 *             If there is some problem accessing the default spatial field
	 * @throws UnsupportedOperationException
	 *             If the spatial field is not raster but vector
	 */
	RasterLegend[] getRasterLegend() throws DriverException,
			UnsupportedOperationException;

	/**
	 * Gets the legends used to draw the default spatial field in this layer if
	 * it is of type vector.
	 * 
	 * @return
	 * @throws DriverException
	 *             If there is some problem accessing the default spatial field
	 * @throws UnsupportedOperationException
	 *             If the spatial field is not vector but raster
	 */
	Legend[] getVectorLegend() throws DriverException,
			UnsupportedOperationException;

	/**
	 * Sets the legend used to draw the default spatial field in this layer
	 * 
	 * @param legends
	 * @throws DriverException
	 *             If there is some problem accessing the contents of the layer
	 */
	void setLegend(Legend... legends) throws DriverException;

	/**
	 * Gets the legend used to draw the specified vector field in this layer
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 *             If the specified name does not exist or it's not of type
	 *             vector
	 * @throws DriverException
	 */
	Legend[] getVectorLegend(String fieldName) throws IllegalArgumentException,
			DriverException;

	/**
	 * Gets the legend used to draw the specified raster field in this layer
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 *             If the specified name does not exist or it's not of type
	 *             raster
	 * @throws DriverException
	 */
	RasterLegend[] getRasterLegend(String fieldName)
			throws IllegalArgumentException, DriverException;

	/**
	 * Sets the legend used to draw the specified spatial field in this layer
	 * 
	 * @param legends
	 * @throws IllegalArgumentException
	 *             If the specified name does not exist
	 * @throws DriverException
	 *             If there is some problem accessing the contents of the layer
	 */
	void setLegend(String fieldName, Legend... legends)
			throws IllegalArgumentException, DriverException;

	/**
	 * If isRaster is true returns the first raster in the layer DataSource.
	 * Otherwise it throws an {@link UnsupportedOperationException}. The method
	 * is just a shortcut for getDataSource().getRaster(0)
	 * 
	 * @return
	 * @throws DriverException
	 * @throws UnsupportedOperationException
	 */
	GeoRaster getRaster() throws DriverException, UnsupportedOperationException;

	/**
	 * Gets an object to manage the WMS contents in this layer.
	 * 
	 * @return
	 * @throws UnsupportedOperationException
	 *             If this layer is not a WMS layer. This is {@link #isWMS()}
	 *             returns false
	 */
	WMSConnection getWMSConnection() throws UnsupportedOperationException;


	/**
	 * Gets the legend to perform the rendering. The actual class of the
	 * returned legends may not be the same of those set by setLegend methods
	 * 
	 * @return
	 * @throws DriverException
	 */
	Legend[] getRenderingLegend() throws DriverException;

}