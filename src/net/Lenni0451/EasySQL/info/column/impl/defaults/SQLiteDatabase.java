package net.Lenni0451.EasySQL.info.column.impl.defaults;

import net.Lenni0451.EasySQL.info.column.ColumnInfo;

public class SQLiteDatabase implements ColumnInfo {
	
	private final String columnName;
	
	public SQLiteDatabase() {
		this("ID");
	}
	
	public SQLiteDatabase(final String columnName) {
		this.columnName = columnName;
	}
	
	@Override
	public String toString() {
		return this.columnName + " INTEGER PRIMARY KEY AUTOINCREMENT";
	}
	
}
