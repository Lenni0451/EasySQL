package net.Lenni0451.EasySQL.info.column.impl.defaults;

import net.Lenni0451.EasySQL.info.column.ColumnInfo;

public class MicrosoftAccessDatabase implements ColumnInfo {
	
	private final String columnName;
	
	public MicrosoftAccessDatabase() {
		this("ID");
	}
	
	public MicrosoftAccessDatabase(final String columnName) {
		this.columnName = columnName;
	}
	
	@Override
	public String toString() {
		return this.columnName + " AUTOINCREMENT PRIMARY KEY";
	}
	
}
