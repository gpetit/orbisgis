/*
 * The GDMS library (Generic Datasources Management System)
 * is a middleware dedicated to the management of various kinds of
 * data-sources such as spatial vectorial data or alphanumeric. Based
 * on the JTS library and conform to the OGC simple feature access
 * specifications, it provides a complete and robust API to manipulate
 * in a SQL way remote DBMS (PostgreSQL, H2...) or flat files (.shp,
 * .csv...). GDMS is produced  by the geomatic team of the IRSTV
 * Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALES CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALES CORTES, Thomas LEDUC
 *
 * This file is part of GDMS.
 *
 * GDMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GDMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GDMS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-developers/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-users/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.gdms.data.edition;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

public class FooConnection implements Connection {

	private String pkName;

	public FooConnection(String pkName) {
		this.pkName = pkName;
	}

	public Statement createStatement() throws SQLException {

		return null;
	}

	public PreparedStatement prepareStatement(String arg0) throws SQLException {

		return null;
	}

	public CallableStatement prepareCall(String arg0) throws SQLException {

		return null;
	}

	public String nativeSQL(String arg0) throws SQLException {

		return null;
	}

	public void setAutoCommit(boolean arg0) throws SQLException {

	}

	public boolean getAutoCommit() throws SQLException {

		return false;
	}

	public void commit() throws SQLException {

	}

	public void rollback() throws SQLException {

	}

	public void close() throws SQLException {

	}

	public boolean isClosed() throws SQLException {

		return false;
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return new FooDatabaseMetadata(pkName);
	}

	public void setReadOnly(boolean arg0) throws SQLException {

	}

	public boolean isReadOnly() throws SQLException {

		return false;
	}

	public void setCatalog(String arg0) throws SQLException {

	}

	public String getCatalog() throws SQLException {

		return null;
	}

	public void setTransactionIsolation(int arg0) throws SQLException {

	}

	public int getTransactionIsolation() throws SQLException {

		return 0;
	}

	public SQLWarning getWarnings() throws SQLException {

		return null;
	}

	public void clearWarnings() throws SQLException {

	}

	public Statement createStatement(int arg0, int arg1) throws SQLException {

		return null;
	}

	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2)
			throws SQLException {

		return null;
	}

	public CallableStatement prepareCall(String arg0, int arg1, int arg2)
			throws SQLException {

		return null;
	}

	@SuppressWarnings("unchecked")
	public Map getTypeMap() throws SQLException {

		return null;
	}

	public void setHoldability(int arg0) throws SQLException {

	}

	public int getHoldability() throws SQLException {

		return 0;
	}

	public Savepoint setSavepoint() throws SQLException {

		return null;
	}

	public Savepoint setSavepoint(String arg0) throws SQLException {

		return null;
	}

	public void rollback(Savepoint arg0) throws SQLException {

	}

	public void releaseSavepoint(Savepoint arg0) throws SQLException {

	}

	public Statement createStatement(int arg0, int arg1, int arg2)
			throws SQLException {

		return null;
	}

	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2,
			int arg3) throws SQLException {

		return null;
	}

	public CallableStatement prepareCall(String arg0, int arg1, int arg2,
			int arg3) throws SQLException {

		return null;
	}

	public PreparedStatement prepareStatement(String arg0, int arg1)
			throws SQLException {

		return null;
	}

	public PreparedStatement prepareStatement(String arg0, int[] arg1)
			throws SQLException {

		return null;
	}

	public PreparedStatement prepareStatement(String arg0, String[] arg1)
			throws SQLException {

		return null;
	}

	// FROM JDK-1.5.0_11 TO JDK-1.6.0_01

	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub

	}

	public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}