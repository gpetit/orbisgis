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
package org.orbisgis.core.ui.plugins.views.information;

import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.gdms.data.DataSource;
import org.gdms.driver.DriverException;
import org.orbisgis.core.Services;
import org.orbisgis.core.ui.pluginSystem.message.ErrorMessages;
import org.orbisgis.utils.I18N;

public class DataSourceTableModel implements TableModel {

	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	private DataSource ds;

	public DataSourceTableModel(DataSource ds) {
		this.ds = ds;
	}

	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public int getColumnCount() {
		try {
			return ds.getFieldCount();
		} catch (DriverException e) {
			Services.getErrorManager().error("Cannot render table", e);
			return 0;
		}
	}

	public String getColumnName(int columnIndex) {
		try {
			return ds.getFieldName(columnIndex);
		} catch (DriverException e) {
			ErrorMessages.error(ErrorMessages.CannotRenderTable, e);
			return I18N.getString("orbisgis.org.orbisgis.name") + "!";
		}
	}

	public int getRowCount() {
		try {
			return (int) ds.getRowCount();
		} catch (DriverException e) {
			ErrorMessages.error(ErrorMessages.CannotRenderTable, e);
			return 0;
		}
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			return ds.getFieldValue(rowIndex, columnIndex);
		} catch (DriverException e) {
			ErrorMessages.error(ErrorMessages.CannotRenderTable, e);
			return null;
		}
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		throw new UnsupportedOperationException(ErrorMessages.CannotEditTable);
	}

	public DataSource getDataSource() {
		return ds;
	}

}