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
package org.orbisgis.core.ui.plugins.views.geocognition.wizards;

import java.util.Map;

import javax.swing.Icon;

import org.orbisgis.core.geocognition.Folder;
import org.orbisgis.core.geocognition.GeocognitionElement;
import org.orbisgis.core.geocognition.GeocognitionElementFactory;
import org.orbisgis.core.ui.plugins.views.geocognition.wizard.ElementRenderer;
import org.orbisgis.core.ui.plugins.views.geocognition.wizard.INewGeocognitionElement;
import org.orbisgis.core.ui.preferences.lookandfeel.OrbisGISIcon;

public class NewFolder implements INewGeocognitionElement {

	public GeocognitionElementFactory[] getFactory() {
		return null;
	}

	public void runWizard() {
	}

	public String getName() {
		return "Folder";
	}

	public ElementRenderer getElementRenderer() {
		return new ElementRenderer() {

			public Icon getIcon(String contentTypeId,
					Map<String, String> properties) {
				return getDefaultIcon(contentTypeId);
			}

			public Icon getDefaultIcon(String contentTypeId) {
				if (contentTypeId
						.equals("org.orbisgis.core.geocognition.Folder")) {
					return OrbisGISIcon.FOLDER;
				} else {
					return null;
				}
			}

			public String getTooltip(GeocognitionElement element) {
				return null;
			}

		};
	}

	public Object getElement(int index) {
		return new Folder();
	}

	public int getElementCount() {
		return 1;
	}

	public String getFixedName(int index) {
		return null;
	}

	public boolean isUniqueIdRequired(int index) {
		return false;
	}

	public String getBaseName(int elementIndex) {
		return "Folder";
	}
}
