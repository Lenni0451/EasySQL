package net.Lenni0451.EasySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Row {
	
	private final Table parent;
	private final int index;
	
	public Row(final Table parent, final int index) {
		this.parent = parent;
		this.index = index;
	}
	
	public Table getParent() {
		return this.parent;
	}
	
	public Connection getConnection() {
		return this.parent.getConnection();
	}
	
	public String getParentTableName() {
		return this.getParent().getName();
	}
	
	public String getPrimaryKeyName() {
		return this.parent.getPrimaryKeyName();
	}
	
	public int getIndex() {
		return this.index;
	}

	
	public Object getColumn(final int index) throws SQLException {
		Statement st = this.getConnection().createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM " + this.getParent().getName());
		int current = 0;
		while(rs.next()) {
			current++;
			if(current == this.index) {
				Object ob = rs.getObject(index);
				rs.close();
				st.close();
				return ob;
			}
		}
		rs.close();
		st.close();
		
		return null;
	}
	
	public Object getColumn(final String columnName) throws SQLException {
		Statement st = this.getConnection().createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM " + this.getParent().getName());
		int current = 0;
		while(rs.next()) {
			current++;
			if(current == this.index) {
				Object ob = rs.getObject(columnName);
				rs.close();
				st.close();
				return ob;
			}
		}
		rs.close();
		st.close();
		
		return null;
	}

	public void updateColumn(final String column, final Object object) throws SQLException {
		String sql = "UPDATE " + this.getParent().getName() + " SET " + column + " = ? WHERE " + this.getPrimaryKeyName() + " = '" + this.getColumn(1) + "'";
		PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
		preparedStatement.setObject(1, object);
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}
	
}
