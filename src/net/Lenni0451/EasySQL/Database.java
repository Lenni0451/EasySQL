package net.Lenni0451.EasySQL;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.Lenni0451.EasySQL.info.column.ColumnInfo;
import net.Lenni0451.EasySQL.info.column.ExtraColumnInfo;
import net.Lenni0451.EasySQL.results.TableCreationResult;

public class Database implements AutoCloseable {
	
	private final Connection connection;
	
	public Database(final Connection connection) {
		this.connection = connection;
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	@Override
	public void close() throws Exception {
		if(!this.connection.isClosed()) {
			this.connection.close();
		}
	}
	
	public void silentClose() {
		try {
			this.close();
		} catch (Exception e) {}
	}
	
	
	public int listTables() throws SQLException {
		DatabaseMetaData md = this.connection.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		int count = 0;
		while (rs.next()) {
			count++;
		}
		rs.close();
		return count;
	}
	
	public List<String> getTableNames() throws SQLException {
		DatabaseMetaData md = this.connection.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		List<String> names = new ArrayList<>();
		while (rs.next()) {
			names.add(rs.getString(3));
		}
		rs.close();
		return names;
	}
	
	public boolean hasTable(final String name) throws SQLException {
		return this.getTableNames().contains(name);
	}
	
	public Table getTable(final String tableName) {
		try {
			if(!this.hasTable(tableName)) {
				return null;
			}
		} catch (Exception e) {}
		return this.getTable(tableName, "ID");
	}
	
	public Table getTable(final String tableName, final String primaryKeyName) {
		return new Table(this, tableName, primaryKeyName);
	}
	

	public TableCreationResult addTable(final String tableName, final ColumnInfo... columns) throws SQLException {
		return this.addTable(tableName, false, columns);
	}
	
	public TableCreationResult addTable(final String tableName, final boolean failSave, final ColumnInfo... columns) throws SQLException {
		String query = "CREATE TABLE" + (failSave?" IF NOT EXISTS":"") + " " + tableName + " (";
		String columnStrings = "";
		for(ColumnInfo info : columns) {
			String infoString = info.toString();
			if(info instanceof ExtraColumnInfo) {
				infoString = infoString + ", " + ((ExtraColumnInfo) info).getExtraInfo();
			}
			columnStrings += infoString + ", ";
		}
		query += columnStrings;
		if(query.endsWith(", ")) {
			query = query.substring(0, query.length() - 2);
		}
		query += ")";
		
		Statement st = this.connection.createStatement();
		st.execute(query);
		st.close();
		
		return new TableCreationResult(query, this.getTable(tableName));
	}
	
	public void removeTable(final String tableName) throws SQLException {
		Statement st = this.connection.createStatement();
		st.executeUpdate("DROP TABLE " + tableName);
		st.close();
	}
	
}
