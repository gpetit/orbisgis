/**
 * OrbisGIS is a GIS application dedicated to scientific spatial analysis.
 * This cross-platform GIS is developed at the Lab-STICC laboratory by the DECIDE
 * team located in University of South Brittany, Vannes.
 *
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
 * Copyright (C) 2015-2016 CNRS (UMR CNRS 6285)
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

package org.orbisgis.wpsgroovyapi.attributes

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Annotation for values or ranges of values.
 *
 * If the value type is set to "VALUE", "value" should be set.
 * If the value type is set to "RANGE", maximum, minimum and spacing sould be set.
 *
 *  - value : String
 *      Only used if the type is VALUE Value represented.
 *  - type : ValuesType
 *      Type of the value, it can be a simple value, or a range.
 *  - maximum : String
 *      Only used if the type is RANGE, indicates the range maximum.
 *  - minimum : String
 *      Only used if the type is RANGE, indicates the range minimum.
 *  - spacing : String
 *      Only used if the type is RANGE, indicates the spacing between two values. If not defined, there is no spacing.
 *
 * @author Sylvain PALOMINOS
 */
@Retention(RetentionPolicy.RUNTIME)
@interface ValuesAttribute {
    /** Only used if the type is VALUE Value represented. */
    String value() default ""
    /** Type of the value, it can be a simple value, or a range. */
    String type() default "VALUE"
    /** Only used if the type is RANGE, indicates the range maximum. */
    String maximum() default ""
    /** Only used if the type is RANGE, indicates the range minimum. */
    String minimum() default ""
    /**
     * Only used if the type is RANGE, indicates the spacing between two values.
     * If not defined, there is no spacing.
     */
    String spacing() default ""



    /********************/
    /** default values **/
    /********************/
    public static final String defaultValue = ""
    public static final String defaultType = "VALUE"
    public static final String defaultMaximum = ""
    public static final String defaultMinimum = ""
    public static final String defaultSpacing = ""
}