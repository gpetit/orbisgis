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
package org.orbisgis.core.sif;

import java.awt.Component;
import java.io.File;
import java.net.URL;

import org.gdms.data.DataSource;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.NonEditableDataSourceException;
import org.gdms.data.file.FileSourceCreation;
import org.gdms.data.schema.DefaultMetadata;
import org.gdms.data.schema.Metadata;
import org.gdms.data.types.Type;
import org.gdms.driver.DriverException;
import org.gdms.driver.driverManager.DriverLoadException;

public class PersistentPanelDecorator implements SQLUIPanel {

	private SQLUIPanel panel;

	public PersistentPanelDecorator(SQLUIPanel panel) {
		this.panel = panel;
	}

	public Component getComponent() {
		return panel.getComponent();
	}

	public String[] getErrorMessages() {
		return panel.getErrorMessages();
	}

	public String[] getFieldNames() {
		return panel.getFieldNames();
	}

	public int[] getFieldTypes() {
		return panel.getFieldTypes();
	}

	public URL getIconURL() {
		return panel.getIconURL();
	}

	public String getId() {
		return panel.getId();
	}

	public String getTitle() {
		return panel.getTitle();
	}

	public String[] getValidationExpressions() {
		return panel.getValidationExpressions();
	}

	public String[] getValues() {
		return panel.getValues();
	}

	public String initialize() {
		return panel.initialize();
	}

	public void setValue(String fieldName, String fieldValue) {
		panel.setValue(fieldName, fieldValue);
	}

	public String validateInput() {
		return panel.validateInput();
	}

	private Metadata getMetadata() {
		DefaultMetadata ddm = new DefaultMetadata();
		String[] names = getFieldNames();
		try {
			ddm.addField("sifName", Type.STRING);
			for (int i = 0; i < names.length; i++) {
				ddm.addField(names[i], Type.STRING);
			}
		} catch (DriverException e) {
			e.printStackTrace();
		}
		return ddm;
	}

	public void saveInput(String inputName) {
		File file = getFile();
		try {
			if (!file.exists()) {
				FileSourceCreation fsc = new FileSourceCreation(file,
						getMetadata());
				UIFactory.dsf.createDataSource(fsc);
			}
			DataSource ds = UIFactory.dsf.getDataSource(file);
			ds.open();
			ds.insertEmptyRow();
			long row = ds.getRowCount() - 1;
			ds.setString(row, 0, inputName);
			String[] values = getValues();
			for (int j = 0; j < values.length; j++) {
				ds.setString(row, j + 1, values[j]);
			}
			ds.commit();
			ds.close();
		} catch (DriverException e) {
		} catch (NonEditableDataSourceException e) {
		} catch (DriverLoadException e) {
		} catch (DataSourceCreationException e) {
		}
	}

	public void removeInput(int selectedIndex) {
		try {
			DataSource ds = UIFactory.dsf.getDataSource(getFile());
			ds.open();
			ds.deleteRow(selectedIndex);
			ds.commit();
			ds.close();
		} catch (DriverException e) {
		} catch (NonEditableDataSourceException e) {
		} catch (DriverLoadException e) {
		} catch (DataSourceCreationException e) {
		}
	}

	private File getFile() {
		return new File(UIFactory.baseDir, getId() + "-favorites.csv");
	}

	public String[] getContents() {
		File file = getFile();
		if (file.exists()) {
			try {
				DataSource ds = UIFactory.dsf.getDataSource(file);
				ds.open();
				String[] ret = new String[(int) ds.getRowCount()];
				for (int i = 0; i < ds.getRowCount(); i++) {
					ret[i] = ds.getString(i, 0);
				}
				ds.close();
				return ret;
			} catch (DriverException e) {
				return new String[0];
			} catch (DriverLoadException e) {
				return new String[0];
			} catch (DataSourceCreationException e) {
				return new String[0];
			}
		} else {
			return new String[0];
		}
	}

	public boolean loadEntry(String inputName) {
		try {
			DataSource ds = UIFactory.dsf.getDataSource(getFile());
			ds.open();
			boolean found = false;
			for (int row = 0; row < ds.getRowCount(); row++) {
				if (ds.getString(row, 0).equals(inputName)) {
					for (int i = 1; i < ds.getFieldCount(); i++) {
						setValue(ds.getFieldName(i), ds.getString(row, i));
					}
					found = true;
				}
			}
			ds.close();

			return found;
		} catch (DriverException e) {
		} catch (DriverLoadException e) {
		} catch (DataSourceCreationException e) {
		}

		return false;
	}

	public void loadEntry(int selectedIndex) {
		try {
			DataSource ds = UIFactory.dsf.getDataSource(getFile());
			ds.open();
			int index = selectedIndex;
			for (int i = 1; i < ds.getFieldCount(); i++) {
				setValue(ds.getFieldName(i), ds.getString(index, i));
			}
			ds.close();
		} catch (DriverException e) {
		} catch (DriverLoadException e) {
		} catch (DataSourceCreationException e) {
		}
	}

	public String getInfoText() {
		return panel.getInfoText();
	}

	public String postProcess() {
		return panel.postProcess();
	}

	public boolean showFavorites() {
		return panel.showFavorites();
	}

}
