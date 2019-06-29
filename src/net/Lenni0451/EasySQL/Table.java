package net.Lenni0451.EasySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.Lenni0451.EasySQL.info.column.ColumnInfo;
import net.Lenni0451.EasySQL.info.rows.RowInfo;

public class Table {
	
	private final Database parent;
	private final String tableName;
	private final String primaryKeyName;

	public Table(final Database parent, final String tableName) {
		this(parent, tableName, "ID");
	}
	
	public Table(final Database parent, final String tableName, final String primaryKeyName) {
		this.parent = parent;
		this.tableName = tableName;
		this.primaryKeyName = primaryKeyName;
	}
	
	public Database getParent() {
		return this.parent;
	}
	
	public Connection getConnection() {
		return this.parent.getConnection();
	}
	
	public String getName() {
		return this.tableName;
	}
	
	public String getPrimaryKeyName() {
		return this.primaryKeyName;
	}
	
	
	public int listRows() throws SQLException {
		Statement st = this.getConnection().createStatement();
        ResultSet res = st.executeQuery("SELECT * FROM " + this.tableName);
        int count = 0;
        while(res.next()) {
            count++;
        }
        res.close();
        st.close();
        return count;
	}
	
	public void clearRows() throws SQLException {
		Statement st = this.getConnection().createStatement();
		st.executeUpdate("DELETE FROM " + this.tableName);
		st.close();
	}
	
	public void truncate() throws SQLException {
		Statement st = this.getConnection().createStatement();
		st.executeUpdate("TRUNCATE " + this.tableName);
		st.close();
	}
	
	public int listColumns() throws SQLException {
		Statement st = this.getConnection().createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM " + this.tableName);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		rs.close();
		st.close();
		return columnCount;
	}
	
	public Map<String, String> getColumns() throws SQLException {
		Statement st = this.getConnection().createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM " + this.tableName);
		ResultSetMetaData rsmd = rs.getMetaData();
		Map<String, String> columns = new HashMap<>();
		int columnCount = rsmd.getColumnCount();
		for(int i = 1; i <= columnCount; i++) {
			String name = rsmd.getColumnName(i);
			String type = rsmd.getColumnTypeName(i);
			
			columns.put(name, type);
		}
		rs.close();
		st.close();
		return columns;
	}
	
	public Row getRow(final int index) {
		return new Row(this, index);
	}
	
	public List<Row> getRowsWhere(final Map<String, Object> elements) throws SQLException {
		return this.getRowsWhere(elements, false);
	}
	
	public List<Row> getRowsWhere(final Map<String, Object> elements, final boolean equalsIgnoreCaseStrings) throws SQLException {
		List<Row> rows = new ArrayList<>();
		Statement st = this.getConnection().createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM " + this.tableName);
		int index = 0;
		while(rs.next()) {
			index++;
			
			ADD_ENTRY: {
				for(Map.Entry<String, Object> entry : elements.entrySet()) {
					Object ob = rs.getObject(entry.getKey());
					boolean isNull = entry.getValue() == null;
					if(!isNull && ob == null) {
						break ADD_ENTRY;
					}
					boolean isValidString = !isNull && ob != null && (ob instanceof String) && (entry.getValue() instanceof String) && (((String) ob).equals(entry.getValue()) || (equalsIgnoreCaseStrings && ((String) ob).equalsIgnoreCase((String) entry.getValue())));
					if(isNull) {
						if(ob != null) {
							break ADD_ENTRY;
						} else {
							;
						}
					} else if(!ob.equals(entry.getValue()) && !isValidString) {
						break ADD_ENTRY;
					}
				}
				
				rows.add(new Row(this, index));
			}
		}
		rs.close();
		st.close();
		return rows;
	}
	
	public List<Row> getRowsWhere(final RowInfo rowInfo, final RowInfo... rowInfos) throws SQLException {
		Map<String, Object> elements = new HashMap<>();
		elements.put(rowInfo.getColumnName(), rowInfo.getElement());
		for(RowInfo info : rowInfos) {
			elements.put(info.getColumnName(), info.getElement());
		}
		return this.getRowsWhere(elements);
	}
	
	public List<Row> getRowsWhere(final boolean equalsIgnoreCaseStrings, final RowInfo rowInfo, final RowInfo... rowInfos) throws SQLException {
		Map<String, Object> elements = new HashMap<>();
		elements.put(rowInfo.getColumnName(), rowInfo.getElement());
		for(RowInfo info : rowInfos) {
			elements.put(info.getColumnName(), info.getElement());
		}
		return this.getRowsWhere(elements, equalsIgnoreCaseStrings);
	}
	
	public void addColumn(final ColumnInfo info) throws SQLException {
		String query = "ALTER TABLE " + this.tableName + " ADD " + info.toString();
		Statement st = this.getConnection().createStatement();
		st.executeUpdate(query);
		st.close();
	}
	
	public void addRow(final Map<String, Object> elements) throws SQLException {
		String columns = "";
		String aditions = "";
		for(String column : elements.keySet()) {
			columns += column + ", ";
			aditions += "?, ";
		}
		if(columns.endsWith(", ")) columns = columns.substring(0, columns.length() - 2);
		if(aditions.endsWith(", ")) aditions = aditions.substring(0, aditions.length() - 2);
		
		String insertSQL = "INSERT INTO "  + this.tableName + " (" + columns + ") VALUES (" + aditions + ")";
		PreparedStatement preparedStatement = getConnection().prepareStatement(insertSQL);
		int current = 1;
		for(Object ob : elements.values()) {
			preparedStatement.setObject(current, ob);
			current++;
		}
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}
	
	public void addRow(final RowInfo rowInfo, final RowInfo... rowInfos) throws SQLException {
		Map<String, Object> elements = new HashMap<>();
		elements.put(rowInfo.getColumnName(), rowInfo.getElement());
		for(RowInfo info : rowInfos) {
			elements.put(info.getColumnName(), info.getElement());
		}
		this.addRow(elements);
	}
	
	public void addRow(final String column, final Object object) throws SQLException {
		Map<String, Object> elements = new HashMap<>();
		elements.put(column, object);
		this.addRow(elements);
	}
	
	public void removeColumn(final String columnLabel) throws SQLException {
		Statement st = this.getConnection().createStatement();
	    st.executeUpdate("ALTER TABLE " + this.tableName + " DROP " + columnLabel);
	    st.close();
	}

	public void removeRow(final Row row) throws SQLException {
		int id = (int) row.getColumn(1);
		Statement st = this.getConnection().createStatement();
		st.executeUpdate("DELETE FROM " + this.tableName + " WHERE ID = " + id);
		st.close();
	}
	
	public void removeRow(final int index) throws SQLException {
		this.removeRow(this.getRow(index));
	}
	
}
