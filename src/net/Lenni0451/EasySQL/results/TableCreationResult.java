package net.Lenni0451.EasySQL.results;

import net.Lenni0451.EasySQL.Table;

public class TableCreationResult {
	
	private final String creationQuery;
	private final Table table;
	
	public TableCreationResult(final String creationQuery, final Table table) {
		this.creationQuery = creationQuery;
		this.table = table;
	}
	
	public String getCreationQuery() {
		return this.creationQuery;
	}
	
	public Table getTable() {
		return this.table;
	}
	
}
