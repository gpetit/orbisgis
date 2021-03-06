/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-1012 IRSTV (FR CNRS 2488)
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
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.core.ui.editor;

import org.orbisgis.core.edition.EditableElement;
import org.orbisgis.core.ui.pluginSystem.PlugInContext;
import org.orbisgis.core.ui.pluginSystem.ViewPlugIn;

public interface IEditor {

	/**
	 * Sets the element to edit. This method is called just once in the
	 * lifecycle of an editor
	 * 
	 * @param element
	 *            instance to be edited. The class of the object inside the
	 *            element corresponds to the typeId accepted in the
	 *            acceptDocument method
	 */
	void setElement(EditableElement element);

	/**
	 * Returns true if this editor can edit a geocognition element of the
	 * specified type
	 * 
	 * @param typeId
	 * @return
	 */
	boolean acceptElement(String typeId);

	/**
	 * Gets the title of the editor. Typically related to the name of the
	 * document
	 * 
	 * @return
	 */
	String getTitle();

	/**
	 * Gets the element being edited by this editor
	 * 
	 * @return
	 */
	EditableElement getElement();

	// Not null if View PlugIn is Editor too (mapEditor or TableEditor)
	ViewPlugIn getView();

	// MapEditor needs Default Automaton tool
	void initialize(PlugInContext wbContext)throws Exception;

}
