package net.Lenni0451.EasySQL.info.rows;

public class RowInfo {
	
	private final String columnName;
	private final Object element;
	
	public RowInfo(final String columnName, final Object element) {
		this.columnName = columnName;
		this.element = element;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public Object getElement() {
		return this.element;
	}
	
}
