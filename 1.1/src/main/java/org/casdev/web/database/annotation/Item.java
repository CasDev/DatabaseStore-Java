package org.casdev.web.database.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.casdev.web.database.connection.DatabaseType;
import org.casdev.web.database.connection.Join;
import org.casdev.web.object.Product;

public class Item {

	public static final int INSERT = 0;
	public static final int SELECT = 1;

	private String value;
	private boolean auto_increment;
	private boolean finished;
	private boolean noUpdate;
	private String field;
	private String parameter;
	private String connect;
	private String table;
	private String joinon;
	private String join;
	private String map;
	private Class clazz;
	private Class object;

	public Item() {
		this.table = "";
		this.join = "";
		this.joinon = "";
		this.value = "";
		this.map = "";
		this.auto_increment = false;
		this.noUpdate = false;
		this.field = "";
		this.clazz = String.class;
		this.object = String.class;
		this.parameter = "";
		this.connect = "";
		this.finished = false;
	}

	public Class getClazz() {
		return clazz;
	}

	public String getMap() {
		return map;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isAuto_increment() {
		return auto_increment;
	}

	public String getConnect() {
		return connect;
	}

	public boolean isNoUpdate() {
		return noUpdate;
	}

	public String getField() {
		return field;
	}

	public String getJoin() {
		return join;
	}

	public String getJoinon() {
		return joinon;
	}

	public Class getObject() {
		return object;
	}

	public String getParameter() {
		return parameter;
	}

	public String getTable() {
		return table;
	}

	public String getValue() {
		return value;
	}

	public void setFinished() {
		this.finished = true;
	}

	/**
	 * This method is for creating the basic Helper-method
	 * 
	 * @param obj
	 *            - The Object that is annotated with @DbObject and @DbField
	 * @param intMethod
	 *            If it is for an SELECT-statement (Item.SELECT) or other
	 *            (Item.INSERT)
	 * @return
	 */
	public static Helper run(Object obj, int intMethod) {
		Helper helper = new Helper();
		Field[] f = obj.getClass().getDeclaredFields();
		Method[] m = obj.getClass().getDeclaredMethods();
		boolean auto_increment = false;
		helper.setObjectClass(obj);

		for (Field fld : f) {
			DbField anno = fld.getAnnotation(DbField.class);
			if (anno != null) {
				Item a = new Item();
				a.auto_increment = anno.auto_increment();
				if (anno.auto_increment()) {
					helper.setAuto_increment(anno.auto_increment());
				}
				if (intMethod == Item.INSERT) {
					break;
				}
				a.map = anno.map();
				a.field = anno.field();
				a.parameter = fld.getName();
				a.value = anno.value();
				a.clazz = anno.clazz();
				a.table = anno.table();
				a.join = anno.join();
				a.joinon = anno.joinon();
				a.connect = anno.connect();
				a.auto_increment = anno.auto_increment();
				a.noUpdate = anno.noUpdate();
				for (Method mtd : m) {
					if (mtd.getName().toLowerCase().equals("get" + a.parameter)
							|| mtd.getName().toLowerCase()
									.equals("is" + a.parameter)) {
						try {
							Object o = mtd.invoke(obj, new Class[] {});
							a.object = o.getClass();
						} catch (Exception e) {
						}
					}
				}
				if (a.auto_increment) {
					auto_increment = true;
				}
				helper.getItems().add(a);
			}
		}

		DbObject anno = obj.getClass().getAnnotation(DbObject.class);
		if (anno != null) {
			helper.setTable(anno.table());
			if (auto_increment) {
				helper.setAuto_increment(true);
			}
		}

		return helper;
	}

	public static DatabaseType getType(Class clazz, Item item, Object insert) {
		DatabaseType type = null;

		if (Helper.isString(clazz)) {
			type = new DatabaseType(DatabaseType.STRING, insert);
		} else if (Helper.isFile(clazz)) {
			type = new DatabaseType(DatabaseType.FILE, insert);
		} else if (Helper.isBoolean(clazz)) {
			type = new DatabaseType(DatabaseType.BOOLEAN, insert);
		} else if (Helper.isDouble(clazz)) {
			type = new DatabaseType(DatabaseType.DOUBLE, insert);
		} else if (Helper.isFloat(clazz)) {
			type = new DatabaseType(DatabaseType.FLOAT, insert);
		} else if (Helper.isInteger(clazz)) {
			type = new DatabaseType(DatabaseType.INTEGER, insert);
		} else if (Helper.isLong(clazz)) {
			type = new DatabaseType(DatabaseType.LONG, insert);
		} else if (Helper.isDate(clazz)) {
			type = new DatabaseType(DatabaseType.DATE, insert);
		} else if (Helper.isCalendar(clazz)) {
			type = new DatabaseType(DatabaseType.CALENDAR, insert);
		} else if (Helper.isArrayList(clazz)) {
			type = new DatabaseType(DatabaseType.LIST, insert);
		} else if (Helper.isHashMap(clazz)) {
			type = new DatabaseType(DatabaseType.MAP, insert);
		} else if (Helper.isStringArray(clazz)) {

		} else if (Helper.isCalendarArray(clazz)) {

		} else if (Helper.isBooleanArray(clazz)) {

		} else if (Helper.isDoubleArray(clazz)) {

		} else if (Helper.isFloatArray(clazz)) {

		} else if (Helper.isIntegerArray(clazz)) {

		} else if (Helper.isLongArray(clazz)) {

		} else if (Helper.isDateArray(clazz)) {
		} else if (Helper.isObject(clazz)) {
			// This should be fixed before,
			// maybe automate it here in future versions
			Class c = null;
			try {
				c = Item.getPropertyClass(item.clazz.newInstance(),
						item.connect);
			} catch (Exception e) {
			}
			if (c == null) {
				type = new DatabaseType(DatabaseType.OBJECT, insert);
			} else {
				type = Item.getType(c, item, insert);
			}
		}

		return type;
	}

	public static Class getPropertyClass(Object obj, String property) {
		Class c = null;
		try {
			DbField anno = obj.getClass().getDeclaredField(property)
					.getAnnotation(DbField.class);
			if (anno != null) {
				c = anno.clazz();
			}
		} catch (Exception e) {
		}
		return c;
	}

	public static Join[] joins(Helper helper) {
		Join[] joins = new Join[0];
		List<Join> list = new ArrayList<Join>();

		for (Item item : helper.getItems()) {
			if (item.join.length() > 1) {
				Join j = new Join(item.join, item.getTable(), helper.getTable()
						+ "." + item.joinon, item.getTable() + "."
						+ item.getValue());
				list.add(j);
			}
		}
		joins = new Join[list.size()];
		for (int i = 0; i < list.size(); i++) {
			joins[i] = list.get(i);
		}

		return joins;
	}

	public static void insertValue(DatabaseType type, Object obj,
			String parameter, Item item) {
		if (type.getValue() != null) {
			Method[] m = obj.getClass().getDeclaredMethods();

			if (Helper.isMap(item.getObject())) {
				// TODO: fix for Maps
				// need to?
			} else if (Helper.isList(item.getObject())) {
				// here fills the lists
				if (Helper.isObject(item.getClazz())) {
					Object object = null;
					try {
						object = item.getClazz().newInstance();
						Method[] _m = object.getClass().getDeclaredMethods();
						for (Method mtd : _m) {
							if (mtd.getName()
									.toLowerCase()
									.equals("set"
											+ item.getConnect().toLowerCase())) {
								Method _mtd = object.getClass()
										.getDeclaredMethod(
												mtd.getName(),
												new Class[] { type.getValue()
														.getClass() });
								_mtd.invoke(object, type.getValue());
							}
						}

					} catch (Exception e) {
					}

					// add the object to list
					for (Method mtd : m) {
						if (mtd.getName().toLowerCase()
								.equals("add" + parameter.toLowerCase())) {
							try {
								Method _mtd = obj.getClass().getDeclaredMethod(
										mtd.getName(),
										new Class[] { object.getClass() });
								_mtd.invoke(obj, object);
								break;
							} catch (Exception e) {
							}
						}
					}
				} else {
					for (Method mtd : m) {
						if (mtd.getName().toLowerCase()
								.equals("add" + parameter.toLowerCase())) {
							try {
								Method _mtd = obj.getClass().getDeclaredMethod(
										mtd.getName(),
										new Class[] { type.getValue()
												.getClass() });
								_mtd.invoke(obj, type.getValue());
								break;
							} catch (Exception e) {
							}
						}
					}
				}
			} else if (Helper.isObject(item.getObject())) {
				// here fixes the object
				Object object = null;
				try {
					object = item.getClazz().newInstance();
					Method[] _m = object.getClass().getDeclaredMethods();
					for (Method mtd : _m) {
						if (mtd.getName()
								.toLowerCase()
								.equals("set" + item.getConnect().toLowerCase())) {
							Method _mtd = object.getClass().getDeclaredMethod(
									mtd.getName(),
									new Class[] { type.getValue().getClass() });
							_mtd.invoke(object, type.getValue());
						}
					}

				} catch (Exception e) {
				}

				// add the object to list
				for (Method mtd : m) {
					if (mtd.getName().toLowerCase()
							.equals("set" + parameter.toLowerCase())) {
						try {
							Method _mtd = obj.getClass().getDeclaredMethod(
									mtd.getName(),
									new Class[] { object.getClass() });
							_mtd.invoke(obj, object);
							break;
						} catch (Exception e) {
						}
					}
				}
			} else {
				for (Method mtd : m) {
					if (mtd.getName().toLowerCase()
							.equals("set" + parameter.toLowerCase())) {
						try {
							Method _mtd = obj.getClass().getDeclaredMethod(
									mtd.getName(),
									new Class[] { type.getValue().getClass() });
							_mtd.invoke(obj, type.getValue());
							break;
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}

	public static void insertValue(DatabaseType key, DatabaseType value,
			Object obj, String parameter, Item item) {
		// this is strictly for Map<String, Object>
		Method[] m = obj.getClass().getDeclaredMethods();

		if (Helper.isObject(item.getClazz())) {
			boolean exist = true;
			for (Method mtd : m) {
				if (mtd.getName().toLowerCase()
						.equals("get" + parameter.toLowerCase())) {
					try {
						Method _mtd = obj.getClass().getDeclaredMethod(
								mtd.getName(), new Class[] {});
						Map<String, Object> map = (Map<String, Object>) _mtd
								.invoke(obj);
						if (map != null) {
							Object o = map.get(key.getValue());
							if (o == null) {
								exist = false;
							}
						}
						break;
					} catch (Exception e) {
					}
				}
			}
			if (!exist) {
				try {
					Object object = item.getClazz().newInstance();
					Method[] ms = object.getClass().getDeclaredMethods();
					for (Method mtd : ms) {
						if (mtd.getName().toLowerCase()
								.equals("set" + item.connect)) {
							Method _mtd = object.getClass()
									.getDeclaredMethod(
											mtd.getName(),
											new Class[] { value.getValue()
													.getClass() });
							_mtd.invoke(object, value.getValue());
						}
					}

					for (Method mtd : m) {
						if (mtd.getName().toLowerCase()
								.equals("add" + parameter.toLowerCase())) {
							try {
								Method _mtd = obj.getClass().getDeclaredMethod(
										mtd.getName(),
										new Class[] {
												key.getValue().getClass(),
												object.getClass() });
								_mtd.invoke(obj, key.getValue(), object);
							} catch (Exception e) {
							}
						}
					}
				} catch (Exception e) {
				}
			}
		} else if (Helper.isList(item.getClazz())) {
			// TODO: fix list
		} else {
			for (Method mtd : m) {
				if (mtd.getName().toLowerCase()
						.equals("add" + parameter.toLowerCase())) {
					try {
						Method _mtd = obj.getClass().getDeclaredMethod(
								mtd.getName(),
								new Class[] { key.getValue().getClass(),
										value.getValue().getClass() });
						_mtd.invoke(obj, key.getValue(), value.getValue());
						break;
					} catch (Exception e) {
					}
				}
			}
		}
	}

	public static DatabaseType[] where(Object obj, String[] whereParams) {

		whereParams = (whereParams != null ? whereParams : new String[0]);
		Class clazz = obj.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		List<DatabaseType> list = new ArrayList<DatabaseType>();
		for (String param : whereParams) {
			Object value = "null";
			for (Method mtd : methods) {
				if (mtd.getName().toLowerCase()
						.equals("get" + param.toLowerCase())
						|| mtd.getName().toLowerCase()
								.equals("is" + param.toLowerCase())) {
					try {
						Method m = clazz.getDeclaredMethod(mtd.getName(),
								new Class[] {});
						value = m.invoke(obj);
					} catch (Exception e) {
					}
					break;
				}
			}
			list.add(Item.getType(value.getClass(), new Item(),
					value.toString()));
		}
		DatabaseType[] types = new DatabaseType[list.size()];
		for (int i = 0; i < list.size(); i++) {
			types[i] = list.get(i);
		}

		return types;
	}

	public static DatabaseType getParameter(Object obj, String parameter) {
		Method[] methods = obj.getClass().getDeclaredMethods();
		DatabaseType type = null;
		for (Method mtd : methods) {
			if (mtd.getName().toLowerCase()
					.equals("get" + parameter.toLowerCase())
					|| mtd.getName().toLowerCase()
							.equals("is" + parameter.toLowerCase())) {
				try {
					Method m = obj.getClass().getDeclaredMethod(mtd.getName(),
							new Class[] {});
					Object value = m.invoke(obj);
					type = Item.getType(value.getClass(), new Item(), value);
				} catch (Exception e) {
				}
			}
		}

		return type;
	}

	public static List<DatabaseType[]> getType(List<Object> list, Item item) {
		List<DatabaseType[]> types = new ArrayList<DatabaseType[]>();

		if (Helper.isObject(item.getClazz())) {
			for (Object obj : list) {
				DatabaseType[] _types = new DatabaseType[] { Item.getParameter(
						obj, item.getConnect()) };
				types.add(_types);
			}
		} else if (Helper.isMap(item.getClazz())) {

		} else if (Helper.isList(item.getClazz())) {

		} else {
			for (Object obj : list) {
				DatabaseType[] _type = new DatabaseType[] { Item.getType(
						obj.getClass(), item, obj) };
				types.add(_type);
			}
		}

		return types;
	}

	public static List<DatabaseType[]> getType(List<Object> list, Object extra,
			Item item) {
		List<DatabaseType[]> types = new ArrayList<DatabaseType[]>();

		if (Helper.isObject(item.getClazz())) {
			for (Object obj : list) {
				DatabaseType[] _types = new DatabaseType[] {
						Item.getParameter(obj, item.getConnect()),
						Item.getType(extra.getClass(), item, extra) };
				types.add(_types);
			}
		} else if (Helper.isMap(item.getClazz())) {

		} else if (Helper.isList(item.getClazz())) {

		} else {
			for (Object obj : list) {
				DatabaseType[] _type = new DatabaseType[] {
						Item.getType(obj.getClass(), item, obj),
						Item.getType(extra.getClass(), item, extra) };
				types.add(_type);
			}
		}

		return types;
	}

	public static Map<String, DatabaseType> getType(Map<String, Object> map,
			Item item) {
		Map<String, DatabaseType> types = new HashMap<String, DatabaseType>();

		if (Helper.isObject(item.getClazz())) {
			for (String key : map.keySet()) {
				types.put(key,
						Item.getParameter(map.get(key), item.getConnect()));
			}
		} else if (Helper.isMap(item.getClazz())) {

		} else if (Helper.isList(item.getClazz())) {

		} else {
			for (String key : map.keySet()) {
				types.put(
						key,
						Item.getType(map.get(key).getClass(), item,
								map.get(key)));
			}
		}

		return types;
	}

	public static DatabaseType getJoinParameter(Object obj, String joinon) {
		Field[] fields = obj.getClass().getDeclaredFields();
		DatabaseType type = null;

		for (Field field : fields) {
			DbField anno = field.getAnnotation(DbField.class);
			if (anno != null) {
				if (joinon.equals(anno.table() + "." + anno.field())) {
					DatabaseType _type = Item
							.getParameter(obj, field.getName());
					type = _type;
					break;
				}
			}
		}

		return type;
	}

	public static Item getJoinItem(Object obj, String joinon) {
		Helper helper = Item.run(obj, Item.SELECT);
		Item item = null;

		for (Item _item : helper.getItems()) {
			if (joinon.equals(_item.getTable() + "." + _item.getField())) {
				item = _item;
				break;
			}
		}

		return item;
	}

}
