package org.casdev.web.database;

import java.sql.PreparedStatement;
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

public class UpdateObject {

	/**
	 * To update an Object annotated with @DBField and @DBObject
	 * 
	 * @param conn
	 *            - The Connection-class
	 * @param obj
	 *            - The Object to overwrite. Annotated with @DBField and
	 * @DBObject. Parameters in WHERE-clause is used from this object
	 * @param _new
	 *            - The Object with the new parameters to save
	 * @param where
	 *            - The WHERE-clause to use with the UPDATE-statement
	 * @param objParams
	 *            - The parameters to use from the obj-Object in with the
	 *            WHERE-clause
	 * @return boolean - If the update is successfull
	 */
	public static boolean update(Connection conn, Object obj, Object _new,
			String where, String[] objParams) {
		Helper helper = Item.run(_new, Item.SELECT);
		List<String> params = new ArrayList<String>();
		List<DatabaseType> values = new ArrayList<DatabaseType>();
		for (Item item : helper.getItems()) {
			if (!item.isAuto_increment()) {
				if (!item.isNoUpdate()) {
					if (Helper.isList(item.getObject())) {
						if (Helper.isObject(item.getClazz())) {
							try {
								DatabaseType type1 = Item.getParameter(_new,
										item.getParameter());
								DatabaseType type2 = Item.getJoinParameter(obj,
										"." + item.getJoinon());
								List<DatabaseType[]> list = Item.getType(
										(List<Object>) type1.getValue(),
										(Object) type2.getValue(), item);

								DeleteObject.delete(new Connection(),
										item.getTable(),
										"WHERE " + item.getValue() + " = ?",
										new DatabaseType[] { type2 });
								SaveObject.save(new Connection(), new String[] {
										item.getField(), item.getValue() },
										item.getTable(), list, false);
							} catch (Exception e) {
							}
						} else {
							try {
								DatabaseType type1 = Item.getParameter(_new,
										item.getParameter());
								DatabaseType type2 = Item.getJoinParameter(obj,
										"." + item.getJoinon());
								List<DatabaseType[]> list = Item.getType(
										(List<Object>) type1.getValue(),
										(Object) type2.getValue(), item);

								DeleteObject.delete(new Connection(),
										item.getTable(),
										"WHERE " + item.getValue() + " = ?",
										new DatabaseType[] { type2 });
								SaveObject.save(new Connection(), new String[] {
										item.getField(), item.getValue() },
										item.getTable(), list, false);
							} catch (Exception e) {
							}
						}
					} else if (Helper.isMap(item.getObject())) {
						// TODO: fix map
						if (Helper.isObject(item.getClazz())) {
							try {
								DatabaseType type1 = Item.getParameter(_new,
										item.getParameter());

								DatabaseType type2 = Item.getJoinParameter(obj,
										"." + item.getJoinon());
								Map<String, DatabaseType> map = (HashMap<String, DatabaseType>) Item
										.getType(
												(HashMap<String, Object>) type1
														.getValue(), item);
								List<DatabaseType[]> list = new ArrayList<DatabaseType[]>();

								for (String key : map.keySet()) {
									DatabaseType[] types = new DatabaseType[] {
											new DatabaseType(
													DatabaseType.STRING, key),
											new DatabaseType(map.get(key)
													.getType(), map.get(key)
													.getValue()),
											Item.getType(type2.getValue()
													.getClass(), item, type2
													.getValue()) };
									list.add(types);
								}

								map = Item.getType(
										(Map<String, Object>) type1.getValue(),
										item);
								DeleteObject.delete(new Connection(),
										item.getTable(),
										"WHERE " + item.getValue() + " = ?",
										new DatabaseType[] { type2 });
								SaveObject
										.save(new Connection(), new String[] {
												item.getField(), item.getMap(),
												item.getValue() },
												item.getTable(), list, false);
							} catch (Exception e) {
							}
						} else {
							try {
								DatabaseType type1 = Item.getParameter(_new,
										item.getParameter());

								DatabaseType type2 = Item.getJoinParameter(obj,
										"." + item.getJoinon());
								Map<String, DatabaseType> map = (HashMap<String, DatabaseType>) Item
										.getType(
												(HashMap<String, Object>) type1
														.getValue(), item);
								List<DatabaseType[]> list = new ArrayList<DatabaseType[]>();

								for (String key : map.keySet()) {
									DatabaseType[] types = new DatabaseType[] {
											new DatabaseType(
													DatabaseType.STRING, key),
											new DatabaseType(map.get(key)
													.getType(), map.get(key)
													.getValue()),
											Item.getType(type2.getValue()
													.getClass(), item, type2
													.getValue()) };
									list.add(types);
								}

								map = Item.getType(
										(Map<String, Object>) type1.getValue(),
										item);
								DeleteObject.delete(new Connection(),
										item.getTable(),
										"WHERE " + item.getValue() + " = ?",
										new DatabaseType[] { type2 });
								SaveObject
										.save(new Connection(), new String[] {
												item.getField(), item.getMap(),
												item.getValue() },
												item.getTable(), list, false);
							} catch (Exception e) {
							}
						}
					} else if (Helper.isObject(item.getObject())) {
						params.add(item.getJoinon());
						DatabaseType type = Item.getParameter(_new,
								item.getParameter());
						type = Item.getType(type.getValue().getClass(), item,
								type.getValue());
						type = Item.getParameter(type.getValue(),
								item.getConnect());
						values.add(type);
					} else {
						params.add(item.getField());
						DatabaseType type = Item.getParameter(_new,
								item.getParameter());
						type = Item.getType(type.getValue().getClass(), item,
								type.getValue());
						values.add(type);
					}
				}
			}
		}

		String[] parameters = new String[params.size()];
		for (int i = 0; i < params.size(); i++) {
			parameters[i] = params.get(i);
		}

		DatabaseType[] _values = new DatabaseType[values.size()];
		for (int i = 0; i < values.size(); i++) {
			_values[i] = values.get(i);
		}
		DatabaseType[] whereParams = Item.where(obj, objParams);

		return update(conn, parameters, _values, helper.getTable(), where,
				whereParams);
	}

	/**
	 * @param connection
	 *            The connection object
	 * @param parameters
	 *            The parameters to update in the databse
	 * @param parameterValues
	 *            The values to set at the parameters
	 * @param table
	 *            The table to update
	 * @param where
	 *            I.e. ID or something that identifies what to update
	 * @param whereParameters
	 *            Value to add to the WHERE-clause
	 * @return True or False, depending on if it succed's or not
	 */
	public static boolean update(Connection connection, String[] parameters,
			DatabaseType[] parameterValues, String table, String where,
			DatabaseType[] whereParameters) {

		boolean result = false;

		if (parameters.length > 0
				&& (parameters.length == parameterValues.length)
				&& where != null && whereParameters.length > 0) {

			java.sql.Connection conn = connection.getConnection();

			String update = buildUpdateQuery(table, parameters, where);

			System.out.println(update);

			try {
				PreparedStatement statement = conn.prepareStatement(update);

				int last = parameters.length;
				for (int i = 1; i <= last; i++) {
					DatabaseType value = parameterValues[(i - 1)];

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
						Calendar date = (Calendar) value.getValue();

						statement.setDate(i, new java.sql.Date(date.getTime()
								.getTime()));
						break;
					case DatabaseType.CALENDAR:
						date = (Calendar) value.getValue();

						statement.setDate(i, new java.sql.Date(date.getTime()
								.getTime()));
						break;
					case DatabaseType.BOOLEAN:
						statement.setBoolean(i,
								Boolean.valueOf(value.getValue().toString()));
						break;
					case DatabaseType.BYTE:
						statement.setByte(i,
								Byte.parseByte(value.getValue().toString()));
						break;
					case DatabaseType.FLOAT:
						statement.setFloat(i,
								Float.parseFloat(value.getValue().toString()));
						break;
					case DatabaseType.LONG:
						statement.setLong(i,
								Long.parseLong(value.getValue().toString()));
						break;
					default:
						break;
					}

				}

				int n = 0;
				last = last + 1;
				int start = last;
				last = last + whereParameters.length;
				for (int i = start; i < last; i++) {
					DatabaseType value = whereParameters[n];
					n = n + 1;

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
						Calendar date = (Calendar) value.getValue();

						statement.setDate(i, new java.sql.Date(date.getTime()
								.getTime()));
						break;
					case DatabaseType.CALENDAR:
						date = (Calendar) value.getValue();

						statement.setDate(i, new java.sql.Date(date.getTime()
								.getTime()));
						break;
					case DatabaseType.BOOLEAN:
						statement.setBoolean(i,
								Boolean.valueOf(value.getValue().toString()));
						break;
					case DatabaseType.BYTE:
						statement.setByte(i,
								Byte.parseByte(value.getValue().toString()));
						break;
					case DatabaseType.FLOAT:
						statement.setFloat(i,
								Float.parseFloat(value.getValue().toString()));
						break;
					case DatabaseType.LONG:
						statement.setLong(i,
								Long.parseLong(value.getValue().toString()));
						break;
					default:
						break;
					}
				}

				int rows = statement.executeUpdate();
				conn.commit();

				result = (rows > 0 ? true : false);

			} catch (SQLException e) {

			} finally {
				connection.close();
			}

		} else {

		}

		return result;
	}

	private static String buildUpdateQuery(String table, String[] parameters,
			String where) {

		StringBuilder builder = new StringBuilder();

		builder.append("UPDATE ");
		builder.append(table);
		builder.append(" SET ");

		int last = parameters.length;
		for (int i = 1; i <= last; i++) {

			builder.append(parameters[(i - 1)]);
			builder.append(" = ?");
			if (i != last) {
				builder.append(", ");
			}

		}

		builder.append(" ");
		builder.append(where.trim());

		return builder.toString().trim();
	}

}
