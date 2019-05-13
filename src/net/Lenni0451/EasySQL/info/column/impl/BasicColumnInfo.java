package net.Lenni0451.EasySQL.info.column.impl;

import net.Lenni0451.EasySQL.info.column.ColumnInfo;

public class BasicColumnInfo implements ColumnInfo {
	
	private String name;
	private String type;
	private boolean notNull;
	
	public BasicColumnInfo(final String name, final String type, final boolean notNull) {
		this.name = name;
		this.type = type;
		this.notNull = notNull;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}
	
	@Override
	public String toString() {
		return name + " " + this.type + (this.notNull?" not NULL":"");
	}
	
}
