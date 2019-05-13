package net.Lenni0451.EasySQL.info.column.impl;

import net.Lenni0451.EasySQL.info.column.ColumnInfo;

public class AdvancedColumnInfo implements ColumnInfo {
	
	private final String columnText;
	
	public AdvancedColumnInfo(final String columnText) {
		this.columnText = columnText;
	}
	
	@Override
	public String toString() {
		return this.columnText;
	}
	
}
