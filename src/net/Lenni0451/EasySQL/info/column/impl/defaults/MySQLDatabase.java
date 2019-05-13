package net.Lenni0451.EasySQL.info.column.impl.defaults;

import net.Lenni0451.EasySQL.info.column.ColumnInfo;
import net.Lenni0451.EasySQL.info.column.ExtraColumnInfo;

public class MySQLDatabase implements ColumnInfo, ExtraColumnInfo {
	
	private final String columnName;
	
	public MySQLDatabase() {
		this("ID");
	}
	
	public MySQLDatabase(final String columnName) {
		this.columnName = columnName;
	}
	
	@Override
	public String toString() {
		return this.columnName + " INTEGER not null AUTO_INCREMENT";
	}

	@Override
	public String getExtraInfo() {
		return "PRIMARY KEY (" + this.columnName + ")";
	}
	
}
