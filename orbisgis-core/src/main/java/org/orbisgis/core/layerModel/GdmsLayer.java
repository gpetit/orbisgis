/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 *
 *  Team leader Erwan BOCHER, scientific researcher,
 *
 *  User support leader : Gwendall Petit, geomatic engineer.
 *
 * Previous computer developer : Pierre-Yves FADET, computer engineer,
Thomas LEDUC, scientific researcher, Fernando GONZALEZ
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

import org.gdms.data.SourceAlreadyExistsException;
import org.gdms.driver.DriverException;
import org.gdms.source.SourceEvent;
import org.gdms.source.SourceListener;
import org.gdms.source.SourceManager;
import org.gdms.source.SourceRemovalEvent;
import org.gdms.sql.strategies.TableNotFoundException;
import org.orbisgis.core.DataManager;
import org.orbisgis.core.Services;

public abstract class GdmsLayer extends AbstractLayer {

	private boolean isVisible = true;

	private String mainName;
	private SourceListener listener = new NameSourceListener();

	public GdmsLayer(String name, MapContext mc) {
		super(name, mc);
		this.mainName = name;
	}

    @Override
        public int getIndex(ILayer layer){
            return -1;
        }
	/**
	 * 
	 * @see org.orbisgis.core.layerModel.ILayer#isVisible()
	 */
    @Override
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * @throws LayerException
	 * @see org.orbisgis.core.layerModel.ILayer#setVisible(boolean)
	 */
    @Override
	public void setVisible(boolean isVisible) throws LayerException {
		this.isVisible = isVisible;
		fireVisibilityChanged();
	}

	@Override
	public void setName(String name) throws LayerException {
		SourceManager sourceManager = ((DataManager) Services
				.getService(DataManager.class)).getDataSourceFactory().getSourceManager();
		if(mainName==null){
			mainName=this.getName();
		}
		// Remove previous alias
		if ( !mainName.equals(getName())) {
			sourceManager.removeName(getName());
		}
		if (!name.equals(mainName)) {
			super.setName(name);
			try {
				sourceManager.addName(mainName, name);
			} catch (TableNotFoundException e) {
				throw new RuntimeException("Can't find a table with such a name!", e);
			} catch (SourceAlreadyExistsException e) {
				throw new LayerException("Source already exists", e);
			}
		} else {
			super.setName(name);
		}
	}

    @Override
	public void close() throws LayerException {
		SourceManager sourceManager = Services.getService(DataManager.class)
				.getSourceManager();

		sourceManager.removeSourceListener(listener);

		// Remove alias
		if (!mainName.equals(getName())) {
			sourceManager.removeName(getName());
		}
	}

	@Override
	public void open() throws LayerException {
		SourceManager sourceManager = Services.getService(DataManager.class)
				.getSourceManager();
		sourceManager.addSourceListener(listener);
	}

	protected String getMainName() {
		return mainName;
	}

	private class NameSourceListener implements SourceListener {

		@Override
		public void sourceAdded(SourceEvent e) {
		}

		@Override
		public void sourceNameChanged(SourceEvent e) {
			// If this layer source name was changed
			if (e.getName().equals(mainName)) {
				mainName = e.getNewName();
				// Add alias if necessary
				if (!getName().equals(mainName)
						&& (getName().equals(e.getName()))) {
					SourceManager sourceManager = Services.getService(
							DataManager.class).getSourceManager();
					try {
						// If this layer name was the mainName
						sourceManager.addName(mainName, getName());
					} catch (TableNotFoundException e1) {
						// The table exists since mainName is the new name
						throw new RuntimeException("bug!", e1);
					} catch (SourceAlreadyExistsException e1) {
						// This layer had the old source name so there is no
						// possibility for a conflict to happen
						throw new RuntimeException("bug!", e1);
					}
				}
			}
		}

		@Override
		public void sourceRemoved(SourceRemovalEvent e) {
		}

	}
}