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
package org.orbisgis.core.ui.plugins.views.geocatalog.actions.create;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gdms.data.types.Constraint;
import org.gdms.data.types.ConstraintFactory;
import org.orbisgis.core.sif.AbstractUIPanel;

public class ChoiceConstraintPanel extends AbstractUIPanel implements
		UIConstraintPanel {

	private JPanel panel;
	private int constraintCode;
	private JComboBox cmbOptions;
	private int[] choiceCodes;

	public ChoiceConstraintPanel(int constraintCode, String[] choices,
			int[] choiceCodes, String value) {
		this.constraintCode = constraintCode;
		panel = new JPanel();
		panel.add(new JLabel(ConstraintFactory
				.getConstraintName(constraintCode)
				+ ": "));
		cmbOptions = new JComboBox(choices);
		cmbOptions.setSelectedItem(value);
		if (cmbOptions.getSelectedIndex() == -1) {
			cmbOptions.setSelectedIndex(0);
		}
		panel.add(cmbOptions);
		this.choiceCodes = choiceCodes;
	}

	public Constraint getConstraint() {
		int code = cmbOptions.getSelectedIndex();
		return ConstraintFactory.createConstraint(constraintCode,
				choiceCodes[code]);
	}

	public Component getComponent() {
		return panel;
	}

	public String getTitle() {
		return "Enter new "
				+ ConstraintFactory.getConstraintName(constraintCode)
				+ " value";
	}

	public String validateInput() {
		return null;
	}

}
