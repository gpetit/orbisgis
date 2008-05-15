/*
 * ColorPicker.java
 *
 * Created on 23 de febrero de 2008, 16:53
 */

package org.orbisgis.geoview.cui.gui.widgets;

import java.awt.Color;
import java.awt.Component;
import java.net.URL;

import org.sif.UIPanel;

/**
 *
 * @author  david
 */
public class ColorPicker extends javax.swing.JPanel implements UIPanel{
    
    /** Creates new form ColorPicker */
    public ColorPicker() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jColorChooser1 = new javax.swing.JColorChooser();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jColorChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jColorChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JColorChooser jColorChooser1;
    // End of variables declaration//GEN-END:variables

    public Color getColor(){
    	return jColorChooser1.getColor();
    }
    

	public Component getComponent() {
		return this;
	}

	public URL getIconURL() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getInfoText() {
		return "Please, select the color";
	}

	public String getTitle() {
		return "Color Picker";
	}

	public String initialize() {
		// TODO Auto-generated method stub
		return null;
	}

	public String postProcess() {
		return null;
	}

	public String validateInput() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
