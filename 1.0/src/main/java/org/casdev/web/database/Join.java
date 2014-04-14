package org.casdev.web.database;

public class Join {
	
	public static final String INNER = "INNER JOIN";
	public static final String OUTER = "OUTER JOIN";
	public static final String JOIN = "JOIN";
	
	private String join;
	private String table;
	private String key;
	private String value;
	
	public Join(String join, String table, String key, String value) {
		this.join = join;
		this.table = table;
		this.key = key;
		this.value = value;
	}
	
	public String getJoin() {
		return join;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getTable() {
		return table;
	}
	
	public String getValue() {
		return value;
	}

}
