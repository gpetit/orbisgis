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
package org.orbisgis.core;

import org.gdms.data.DataSourceFactory;
import org.gdms.source.SourceManager;
import org.junit.Before;
import org.orbisgis.core.errorManager.ErrorListener;
import org.orbisgis.core.errorManager.ErrorManager;

public abstract class AbstractTest {

        protected FailErrorManager failErrorManager;

        @Before
        public void setUp() throws Exception {
                failErrorManager = new FailErrorManager();
                Services.registerService(ErrorManager.class, "", failErrorManager);

                OrbisgisCoreServices.installServices();
        }

        public static void registerDataManager(DataSourceFactory dsf) {
                // Installation of the service
                Services.registerService(
                        DataManager.class,
                        "Access to the sources, to its properties (indexes, etc.) and its contents, either raster or vectorial",
                        new DefaultDataManager(dsf));
        }

        public static void registerDataManager() {
                DataSourceFactory dsf = new DataSourceFactory("target/tempsGdms", "target/tempsGdms");
                registerDataManager(dsf);
        }

        protected SourceManager getsSourceManager() {
                return getDataManager().getSourceManager();
        }

        protected DataManager getDataManager() {
                return (DataManager) Services.getService(DataManager.class);
        }

        protected class FailErrorManager implements ErrorManager {

                private boolean ignoreWarnings;
                private boolean ignoreErrors;

                public void setIgnoreWarnings(boolean ignore) {
                        this.ignoreWarnings = ignore;
                }

                public void addErrorListener(ErrorListener listener) {
                }

                public void error(String userMsg) {
                        if (!ignoreErrors) {
                                throw new RuntimeException(userMsg);
                        }
                }

                public void error(String userMsg, Throwable exception) {
                        if (!ignoreErrors) {
                                throw new RuntimeException(userMsg, exception);
                        }
                }

                public void removeErrorListener(ErrorListener listener) {
                }

                public void warning(String userMsg, Throwable exception) {
                        if (!ignoreWarnings) {
                                throw new RuntimeException(userMsg, exception);
                        }
                }

                public void warning(String userMsg) {
                        if (!ignoreWarnings) {
                                throw new RuntimeException(userMsg);
                        }
                }

                public void setIgnoreErrors(boolean b) {
                        this.ignoreErrors = b;
                }
        }
}
