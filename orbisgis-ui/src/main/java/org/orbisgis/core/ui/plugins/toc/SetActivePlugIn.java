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
package org.orbisgis.core.ui.plugins.toc;

import javax.swing.JOptionPane;
import org.gdms.data.NonEditableDataSourceException;
import org.gdms.driver.DriverException;
import org.orbisgis.core.layerModel.ILayer;
import org.orbisgis.core.layerModel.MapContext;
import org.orbisgis.core.sif.UIFactory;
import org.orbisgis.core.ui.pluginSystem.AbstractPlugIn;
import org.orbisgis.core.ui.pluginSystem.PlugInContext;
import org.orbisgis.core.ui.pluginSystem.PlugInContext.LayerAvailability;
import org.orbisgis.core.ui.pluginSystem.PlugInContext.SelectionAvailability;
import org.orbisgis.core.ui.pluginSystem.message.ErrorMessages;
import org.orbisgis.core.ui.pluginSystem.workbench.Names;
import org.orbisgis.core.ui.pluginSystem.workbench.WorkbenchContext;
import org.orbisgis.core.ui.pluginSystem.workbench.WorkbenchFrame;
import org.orbisgis.core.ui.preferences.lookandfeel.OrbisGISIcon;
import org.orbisgis.utils.I18N;

public class SetActivePlugIn extends AbstractPlugIn {

        public boolean execute(PlugInContext context) throws Exception {
                MapContext mapContext = getPlugInContext().getMapContext();
                ILayer[] selectedResources = mapContext.getSelectedLayers();
                ILayer layerToBeActivated = selectedResources[0];
                ILayer activeLayer = mapContext.getActiveLayer();
                //Test if one layer is already in edition
                if ((activeLayer != null) && (activeLayer != layerToBeActivated)) {
                        if (activeLayer.getDataSource().isModified()) {
                                int option = JOptionPane.showConfirmDialog(UIFactory.getMainFrame(), I18N.getString("orbisgis.org.orbisgis.edit.saveChange"), I18N.getString("orbisgis.org.orbisgis.edit.stopEdition"),
                                        JOptionPane.YES_NO_CANCEL_OPTION);
                                if (option == JOptionPane.YES_OPTION) {
                                        try {
                                                activeLayer.getDataSource().commit();
                                                mapContext.setActiveLayer(null);
                                        } catch (DriverException e) {
                                                ErrorMessages.error(ErrorMessages.CannotSavelayer, e);
                                        } catch (NonEditableDataSourceException e) {
                                                ErrorMessages.error(ErrorMessages.CannotSavelayer, e);
                                        }
                                } else if (option == JOptionPane.NO_OPTION) {
                                        try {
                                                activeLayer.getDataSource().syncWithSource();
                                                mapContext.setActiveLayer(null);
                                        } catch (DriverException e) {
                                                ErrorMessages.error(ErrorMessages.CannotRevertlayer, e);
                                        }
                                } else {
                                       return true;
                                }
                        }

                }

                mapContext.setActiveLayer(layerToBeActivated);

                return true;
        }

        public void initialize(PlugInContext context) throws Exception {
                WorkbenchContext wbContext = context.getWorkbenchContext();
                WorkbenchFrame frame = wbContext.getWorkbench().getFrame().getToc();
                context.getFeatureInstaller().addPopupMenuItem(frame, this,
                        new String[]{Names.POPUP_TOC_ACTIVE_PATH1},
                        Names.POPUP_TOC_ACTIVE_GROUP, false, OrbisGISIcon.PENCIL,
                        wbContext);
        }

        public boolean isEnabled() {
                return getPlugInContext().checkLayerAvailability(
                        new SelectionAvailability[]{SelectionAvailability.EQUAL},
                        1,
                        new LayerAvailability[]{LayerAvailability.VECTORIAL,
                                LayerAvailability.NOT_ACTIVE_LAYER,
                                LayerAvailability.IS_EDTABLE});
        }
}
