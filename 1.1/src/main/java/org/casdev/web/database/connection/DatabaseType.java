package org.casdev.web.database.connection;

import java.lang.Object;

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
	public static final int FLOAT = 8;
	public static final int LONG = 9;
	public static final int CALENDAR = 10;
	public static final int OBJECT = 11;
	public static final int MAP = 12;
	public static final int LIST = 13;

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
}
