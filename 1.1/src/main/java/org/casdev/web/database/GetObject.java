package org.casdev.web.database;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.casdev.web.database.annotation.Helper;
import org.casdev.web.database.annotation.Item;
import org.casdev.web.database.connection.Connection;
import org.casdev.web.database.connection.DatabaseType;
import org.casdev.web.database.connection.Join;

public class GetObject {

	/**
	 * A method to get a certain Object which is annotated with @DBField and @DBObject
	 * 
	 * @param conn
	 *            - The Connection-class
	 * @param obj
	 *            - The Object to get. Has to be annotated with @DBField and @DBobject
	 * @param where
	 *            - The WHERE-clause to use with the SELECT-statement
	 * @param objParams
	 *            - The parameters to use from the obj-Object in the
	 *            WHERE-clause
	 * @return Object - Get the obj-Object back
	 */
	public static Object get(Connection conn, Object obj, String where,
			String[] objParams) {
		Helper helper = Item.run(obj, Item.SELECT);

		// fixing the parameters that should be in the SELECT-statement
		List<DatabaseType> params = new ArrayList<DatabaseType>();
		for (int i = 0; i < helper.getItems().size(); i++) {
			String field = "";
			field = field
					.concat((helper.getItems().get(i).getTable().length() > 0 ? helper
							.getItems().get(i).getTable()
							: helper.getTable()));
			field = field.concat("." + helper.getItems().get(i).getField());
			if (helper.getItems().get(i).getMap().length() > 0) {
				params.add(Item.getType(String.class, helper.getItems().get(i),
						field));

				field = "";
				field = field.concat((helper.getItems().get(i).getTable()
						.length() > 0 ? helper.getItems().get(i).getTable()
						: helper.getTable()));
				field = field.concat("." + helper.getItems().get(i).getMap());
				params.add(Item.getType(helper.getItems().get(i).getClazz(),
						helper.getItems().get(i), field));
			} else {
				params.add(Item.getType(helper.getItems().get(i).getClazz(),
						helper.getItems().get(i), field));
			}
		}
		DatabaseType[] _params = new DatabaseType[params.size()];
		System.out.println();
		for (int i = 0; i < params.size(); i++) {
			_params[i] = params.get(i);
		}
		Join[] _joins = Item.joins(helper);
		DatabaseType[] _whereParams = Item.where(obj, objParams);

		List<Map<String, DatabaseType>> list = get(conn, helper.getTable(),
				_params, _joins, where, _whereParams, null);

		if (list.size() > 0) {
			for (Map<String, DatabaseType> map : list) {
				for (String key : map.keySet()) {

					String field = null;
					String table = null;
					try {
						field = key.substring(key.indexOf(".") + 1);
						table = key.substring(0, key.indexOf("."));
					} catch (Exception e) {
					}
					for (Item i : helper.getItems()) {
						if (!i.isFinished()
								&& i.getField().equals(field)
								&& (i.getTable().equals(table) || helper
										.getTable().equals(table))) {
							if (!Helper.isList(i.getObject())
									&& !Helper.isMap(i.getObject())) {
								i.setFinished();
							}
							if (Helper.isMap(i.getObject())) {
								// if Map<String, Object> kind of parameter
								String v = "";
								v = v.concat((i.getTable().length() > 0 ? i
										.getTable() : helper.getTable()));
								v = v.concat("." + i.getMap());

								Item.insertValue(map.get(key), map.get(v), obj,
										i.getParameter(), i);
								break;
							} else {
								Item.insertValue(map.get(key), obj,
										i.getParameter(), i);
								break;
							}
						}
					}
				}
			}
		} else {
			obj = null;
		}

		return obj;
	}

	/**
	 * A a list of parameters requested by the SELECT-statement
	 * 
	 * @param connection
	 *            The connection object
	 * @param table
	 *            The database table
	 * @param parameters
	 *            The parameters to get from the database
	 * @param where
	 *            An WHERE-clause - null:able
	 * @param whereParams
	 *            Values to assign to the WHERE-clause
	 * @param order
	 *            An ORDER-clause - null:able
	 * @return An List<Object> full of the variables that you wanted
	 *         (parameters)
	 */
	public static List<Map<String, DatabaseType>> get(Connection connection,
			String table, DatabaseType[] parameters, Join[] joins,
			String where, DatabaseType[] whereParams, String order) {

		java.sql.Connection conn = connection.getConnection();
		String select = buildSelectQuery(parameters, table, joins, where, order);
		List<Map<String, DatabaseType>> objects = new ArrayList<Map<String, DatabaseType>>();

		System.out.println(select);

		try {
			PreparedStatement statement = conn.prepareStatement(select);

			if (where != null && whereParams != null) {

				for (int i = 1; i <= whereParams.length; i++) {

					int n = (i - 1);
					DatabaseType value = whereParams[n];

					switch (value.getType()) {
					case DatabaseType.STRING:
						statement.setString(i, value.getValue().toString());
						break;
					case DatabaseType.INTEGER:
						statement.setInt(i,
								Integer.parseInt(value.getValue().toString()));
						break;
					case DatabaseType.DOUBLE:
						statement
								.setDouble(i, Double.parseDouble(value
										.getValue().toString()));
						break;
					case DatabaseType.DATE:
						Calendar cal = (Calendar) value.getValue();

						statement.setDate(i, new Date(cal.getTime().getTime()));
						break;
					case DatabaseType.CALENDAR:
						cal = (Calendar) value.getValue();

						statement.setDate(i, new Date(cal.getTime().getTime()));
						break;
					case DatabaseType.BOOLEAN:
						statement.setBoolean(i,
								Boolean.valueOf(value.getValue().toString()));
						break;
					case DatabaseType.BYTE:
						statement.setByte(i,
								Byte.parseByte(value.getValue().toString()));
						break;
					case DatabaseType.FILE:
						// TODO: fix this
						break;
					case DatabaseType.FLOAT:
						// TODO: fix this
						break;
					default:
						break;
					}

				}

			}

			ResultSet set = statement.executeQuery();

			while (set.next()) {

				Map<String, DatabaseType> map = new HashMap<String, DatabaseType>();

				for (int i = 1; i <= parameters.length; i++) {

					DatabaseType value = parameters[(i - 1)];

					switch (value.getType()) {
					case DatabaseType.STRING:
						map.put((String) value.getValue(), new DatabaseType(
								DatabaseType.STRING, set.getString(i)));
						break;
					case DatabaseType.INTEGER:
						map.put((String) value.getValue(), new DatabaseType(
								DatabaseType.INTEGER, set.getInt(i)));
						break;
					case DatabaseType.DOUBLE:
						map.put((String) value.getValue(), new DatabaseType(
								DatabaseType.DOUBLE, set.getDouble(i)));
						break;
					case DatabaseType.DATE:
						map.put((String) value.getValue(), new DatabaseType(
								DatabaseType.DATE, set.getDate(i)));
						break;
					case DatabaseType.CALENDAR:
						Calendar cal = Calendar.getInstance();
						Date date = set.getDate(i);
						cal.setTime(date);

						map.put((String) value.getValue(), new DatabaseType(
								DatabaseType.CALENDAR, cal));
						break;
					case DatabaseType.BOOLEAN:
						map.put((String) value.getValue(), new DatabaseType(
								DatabaseType.STRING, set.getBoolean(i)));
						break;
					case DatabaseType.BYTE:
						map.put((String) value.getValue(), new DatabaseType(
								DatabaseType.STRING, set.getByte(i)));
						break;
					case DatabaseType.FILE:
						Blob blob = set.getBlob(i);

						int length = (int) blob.length();
						byte[] array = blob.getBytes(1, length);

						blob.free();
						map.put((String) value.getValue(), new DatabaseType(
								DatabaseType.FILE, array));
						break;
					default:
						break;
					}

				}

				objects.add(map);

			}

		} catch (SQLException e) {
		} finally {
			connection.close();
		}

		return objects;

	}

	private static String buildSelectQuery(DatabaseType[] parameters,
			String table, Join[] joins, String where, String order) {

		StringBuilder builder = new StringBuilder();

		builder.append("SELECT ");

		int last = parameters.length;

		for (int i = 1; i <= last; i++) {

			builder.append(parameters[(i - 1)].getValue().toString());

			if (i != last) {

				builder.append(", ");

			}

		}

		builder.append(" FROM ");
		builder.append(table);

		if (joins != null) {

			for (Join join : joins) {

				builder.append(" " + join.getJoin() + " " + join.getTable()
						+ " ON " + join.getKey() + " = " + join.getValue());

			}

		}

		if (where != null && where.length() > 0) {

			builder.append(" ");
			builder.append(where.trim());
			builder.append(" ");

		}

		if (order != null && order.length() > 0) {

			builder.append(" ");
			builder.append(order.trim());
			builder.append(" ");

		}

		return builder.toString().trim();
	}

}
