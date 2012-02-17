/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 * 
 *
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
 * info _at_ orbisgis.org
 */
package org.orbisgis.view.main;

import java.awt.Rectangle;
import java.awt.event.WindowListener;
import java.beans.EventHandler;
import javax.swing.SwingUtilities;
import org.orbisgis.base.context.main.MainContext;
import org.orbisgis.utils.I18N;
import org.orbisgis.view.docking.DockingManager;
import org.orbisgis.view.geocatalog.Catalog;
import org.orbisgis.view.main.frames.MainFrame;
import org.orbisgis.view.translation.OrbisGISI18N;

/**
 * The core manage the view of the application
 * This is the main UIContext
 */
public class Core {
    /////////////////////
    //view package
    private MainFrame mainFrame = null;     /*!< The main window */
    private Catalog geoCatalog= null;      /*!< The GeoCatalog */
    private static final Rectangle MAIN_VIEW_POSITION_AND_SIZE = new Rectangle(20,20,800,600);/*!< Bounds of mainView, x,y and width height*/
    private DockingManager dockManager = null; /*!< The DockStation manager */
    
    
    /////////////////////
    //base package :
    private MainContext mainContext; /*!< The larger surrounding part of OrbisGis base */
    
    public Core() {
        this.mainContext = new MainContext();
    }
    
    /**
     * 
     * @return Instance of main context
     */
    public MainContext getMainContext() {
        return mainContext;
    }
    /**
     * Instance of main frame, null if startup() has not be called.
     * @return MainFrame instance
     */
    public MainFrame getMainFrame() {
        return mainFrame;
    }
    /**
     * Create the Instance of the main frame
     */
    private void makeMainFrame() {
        mainFrame = new MainFrame();
        //When the user ask to close OrbisGis it call
        //the shutdown method here, 
        // Link the Swing Events with the MainFrame event
        //Thanks to EventHandler we don't have to build a listener class
        mainFrame.addWindowListener(EventHandler.create(
                WindowListener.class, //The listener class
                this,                 //The event target object
                "onMainWindowClosing",//The event target method to call
                null,                 //the event parameter to pass(none)
                "windowClosing"));    //The listener method to use
    }
    /**
     * Create the GeoCatalog view
     */
    private void makeGeoCatalogPanel() {
        geoCatalog = new Catalog();
        dockManager.show(geoCatalog, dockManager.getScreen(), null);
    }
    /**
     * The user want to close the main window
     * Then the application has to be closed
     */
    public void onMainWindowClosing() {
        this.shutdown();
    }
    /**
    * Starts the application. This method creates the {@link MainFrame},
    * and manage the Look And Feel declarations
    */
    public void startup(){
        if(mainFrame!=null) {
            return;//This method can't be called twice
        }
        initI18n();        
        
        makeMainFrame();
        
        //Initiate the docking management system
        dockManager = new DockingManager(mainFrame);
        
        //Set the main frame position and size
	mainFrame.setBounds(MAIN_VIEW_POSITION_AND_SIZE);
        
        //Load the GeoCatalog
        makeGeoCatalogPanel();
        
        // Show the application when Swing will be ready
        SwingUtilities.invokeLater( new Runnable(){
                public void run(){
                        mainFrame.setVisible( true );
                        //views.getScreen().setShowing( true );
                }
        });
    }

    public DockingManager getDockManager() {
        return dockManager;
    }
    /**
     * Add the properties of OrbisGis view to I18n translation manager
     */
    private void initI18n() {
        // Init I18n
        I18N.addI18n("", "orbisgis", OrbisGISI18N.class);
    }
    /**
     * Free all resources allocated by this object
     */
    public void dispose() {
        //Remove all listeners created by this object

        //Free UI resources
        mainFrame.dispose();
        dockManager.dispose();
    }
    /**
    * Stops this application, closes the {@link MainFrame} and saves
    * all properties if the application is not in a {@link #isSecure() secure environment}.
    * This method is called through the MainFrame.MAIN_FRAME_CLOSING event listener.
    */
    public void shutdown(){
        try{
            this.dispose();
        }
        catch (RuntimeException e) { 
            // If an error occuring while unload resources, java machine
            // may continue to run. In this case, the following command
            // would terminate the application.
            
            SwingUtilities.invokeLater( new Runnable(){
                    public void run(){
                            System.exit(0);
                    }
            } );
        }
    }
}