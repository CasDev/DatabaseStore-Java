package org.casdev.web.database;

public class DatabaseType {

	private int type;
	private Object value;
	
	public static final int STRING = 1;
	public static final int INTEGER = 2;
	public static final int DOUBLE = 3;
	public static final int DATE = 4;
	public static final int BOOLEAN = 5;
	public static final int BYTE = 6;
	public static final int FILE = 7;

	public DatabaseType(int type, Object value) {
		this.type = type;
		this.value = value;
	}

	public int getType() {

		return type;
	}

	public Object getValue() {
		return value;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
