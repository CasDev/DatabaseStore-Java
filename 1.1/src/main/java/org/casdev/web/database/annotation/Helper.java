package org.casdev.web.database.annotation;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class Helper {

	private String table;
	private Class clazz;
	private List<Item> items;
	private boolean auto_increment;

	public Helper() {
		this.table = "";
		this.items = new ArrayList<Item>();
		this.clazz = String.class;
		this.auto_increment = false;
	}

	public boolean isAuto_increment() {
		return auto_increment;
	}

	public List<Item> getItems() {
		return items;
	}

	public String getTable() {
		return table;
	}

	public Class getObjectClass() {
		return clazz;
	}

	public void addItem(Item item) {
		this.items.add(item);
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setObjectClass(Class clazz) {
		this.clazz = clazz;
	}

	public void setObjectClass(Object obj) {
		this.clazz = obj.getClass();
	}

	public void setAuto_increment(boolean auto_increment) {
		this.auto_increment = auto_increment;
	}

	public static boolean isDate(Class clazz) {
		return clazz.equals(Date.class);
	}

	public static boolean isDateArray(Class clazz) {
		return clazz.equals(Date[].class);
	}

	public static boolean isCalendar(Class clazz) {
		return (clazz.equals(Calendar.class) || clazz
				.equals(GregorianCalendar.class));
	}

	public static boolean isCalendarArray(Class clazz) {
		return (clazz.equals(Calendar[].class) || clazz
				.equals(GregorianCalendar[].class));
	}

	public static boolean isStringArray(Class clazz) {
		return clazz.equals(String[].class);
	}

	public static boolean isString(Class clazz) {
		return clazz.equals(String.class);
	}
	
	public static boolean isMap(Class clazz) {
		return isHashMap(clazz);
	}
	
	public static boolean isHashMap(Class clazz) {
		return clazz.equals(HashMap.class);
	}

	public static boolean isList(Class clazz) {
		return isArrayList(clazz);
	}

	public static boolean isArrayList(Class clazz) {
		return clazz.equals(ArrayList.class);
	}

	public static boolean isBoolean(Class clazz) {
		return clazz.equals(Boolean.class) || clazz.equals(boolean.class);
	}

	public static boolean isBooleanArray(Class clazz) {
		return clazz.equals(Boolean[].class) || clazz.equals(boolean[].class);
	}

	public static boolean isInteger(Class clazz) {
		return clazz.equals(Integer.class) || clazz.equals(int.class);
	}

	public static boolean isIntegerArray(Class clazz) {
		return clazz.equals(Integer[].class) || clazz.equals(int[].class);
	}

	public static boolean isDouble(Class clazz) {
		return clazz.equals(Double.class) || clazz.equals(double.class);
	}

	public static boolean isDoubleArray(Class clazz) {
		return clazz.equals(Double[].class) || clazz.equals(double[].class);
	}

	public static boolean isFloat(Class clazz) {
		return clazz.equals(Float.class) || clazz.equals(float.class);
	}

	public static boolean isFloatArray(Class clazz) {
		return clazz.equals(Float[].class) || clazz.equals(float[].class);
	}

	public static boolean isLong(Class clazz) {
		return clazz.equals(Long.class) || clazz.equals(long.class);
	}

	public static boolean isLongArray(Class clazz) {
		return clazz.equals(Long[].class) || clazz.equals(long[].class);
	}
	
	public static boolean isFile(Class clazz) {
		return clazz.equals(File.class);
	}

	public static boolean isObject(Class clazz) {
		return !isBoolean(clazz) && !isBooleanArray(clazz) && !isDouble(clazz)
				&& !isDoubleArray(clazz) && !isFloat(clazz)
				&& !isFloatArray(clazz) && !isInteger(clazz)
				&& !isIntegerArray(clazz) && !isString(clazz)
				&& !isStringArray(clazz) && !isLong(clazz)
				&& !isLongArray(clazz) && !isDate(clazz) && !isDateArray(clazz)
				&& !isCalendar(clazz) && !isCalendarArray(clazz)
				&& !isList(clazz) && !isMap(clazz) && !isFile(clazz);
	}

}
