package org.casdev.web.database;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.casdev.web.database.annotation.Helper;
import org.casdev.web.database.annotation.Item;
import org.casdev.web.database.connection.Connection;
import org.casdev.web.database.connection.DatabaseType;

public class SaveObject {

	/**
	 * A method to save an Object annotated with @DBField and @DBobject
	 * 
	 * @param conn
	 *            - The Connection-class
	 * @param obj
	 *            - The Object to save down to the database
	 * @return int - The auto-incremented integer, like an ID
	 */
	public static int save(Connection conn, Object obj) {
		Helper helper = Item.run(obj, Item.SELECT);
		List<String> params = new ArrayList<String>();
		List<DatabaseType> values = new ArrayList<DatabaseType>();
		for (Item item : helper.getItems()) {
			if (!item.isAuto_increment()) {
				if (Helper.isList(item.getObject())) {
					if (Helper.isObject(item.getClazz())) {
						try {
							DatabaseType type = Item.getParameter(obj,
									item.getParameter());
							List<DatabaseType[]> list = Item.getType(
									(List<Object>) type.getValue(), item);

							SaveObject.save(new Connection(),
									new String[] { item.getField() },
									item.getTable(), list, false);
						} catch (Exception e) {
						}
					} else {
						try {
							DatabaseType type = Item.getParameter(obj,
									item.getParameter());
							List<DatabaseType[]> list = Item.getType(
									(List<Object>) type.getValue(), item);

							SaveObject.save(new Connection(),
									new String[] { item.getField() },
									item.getTable(), list, false);
						} catch (Exception e) {
						}
					}
				} else if (Helper.isMap(item.getObject())) {
					if (Helper.isObject(item.getClazz())) {
						try {
							DatabaseType type = Item.getParameter(obj,
									item.getParameter());
							HashMap<String, DatabaseType> map = (HashMap<String, DatabaseType>) Item
									.getType((HashMap<String, Object>) type
											.getValue(), item);
							List<DatabaseType[]> list = new ArrayList<DatabaseType[]>();

							for (String key : map.keySet()) {
								DatabaseType[] types = new DatabaseType[] {
										new DatabaseType(DatabaseType.STRING,
												key),
										new DatabaseType(
												map.get(key).getType(), map
														.get(key).getValue()) };
								list.add(types);
							}

							SaveObject.save(new Connection(), new String[] {
									item.getField(), item.getMap() },
									item.getTable(), list, false);
						} catch (Exception e) {
						}
					} else {
						DatabaseType type = Item.getParameter(obj,
								item.getParameter());
						HashMap<String, DatabaseType> map = (HashMap<String, DatabaseType>) Item
								.getType((HashMap<String, Object>) type
										.getValue(), item);
						List<DatabaseType[]> list = new ArrayList<DatabaseType[]>();

						for (String key : map.keySet()) {
							DatabaseType[] types = new DatabaseType[] {
									new DatabaseType(DatabaseType.STRING, key),
									new DatabaseType(map.get(key).getType(),
											map.get(key).getValue()) };
							list.add(types);
						}

						SaveObject
								.save(new Connection(),
										new String[] { item.getField(),
												item.getMap() },
										item.getTable(), list, false);
					}

				} else if (Helper.isObject(item.getObject())) {
					params.add(item.getJoinon());
					DatabaseType type = Item.getParameter(obj,
							item.getParameter());
					type = Item.getType(type.getValue().getClass(), item,
							type.getValue());
					type = Item
							.getParameter(type.getValue(), item.getConnect());
					values.add(type);
				} else {
					params.add(item.getField());
					DatabaseType type = Item.getParameter(obj,
							item.getParameter());
					type = Item.getType(type.getValue().getClass(), item,
							type.getValue());
					values.add(type);
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

		return save(conn, parameters, helper.getTable(), _values,
				helper.isAuto_increment());
	}

	public static int save(Connection connection, String[] parameters,
			String table, List<DatabaseType[]> values, boolean auto_increment) {

		java.sql.Connection conn = connection.getConnection();
		String insert = buildInsertQuery(parameters, values.size(), table);
		int id = 0;
		FileInputStream fis = null;

		System.out.println(insert);

		try {
			PreparedStatement statement = null;

			if (auto_increment) {
				statement = conn.prepareStatement(insert,
						Statement.RETURN_GENERATED_KEYS);
			} else {
				statement = conn.prepareStatement(insert);
			}

			if (values != null) {

				int t = 1;

				for (int x = 0; x < values.size(); x++) {

					if (parameters.length == values.get(x).length) {

						for (int i = 1; i <= values.get(x).length; i++) {

							DatabaseType value = values.get(x)[(i - 1)];

							switch (value.getType()) {
							case DatabaseType.STRING:
								statement.setString(t, value.getValue()
										.toString());
								break;
							case DatabaseType.INTEGER:
								statement.setInt(t, Integer.parseInt(value
										.getValue().toString()));
								break;
							case DatabaseType.DOUBLE:
								statement.setDouble(t, Double.valueOf(value
										.getValue().toString()));
								break;
							case DatabaseType.DATE:
								Calendar date = (Calendar) value.getValue();

								statement.setDate(t, new java.sql.Date(date
										.getTime().getTime()));
								break;
							case DatabaseType.CALENDAR:
								date = (Calendar) value.getValue();

								statement.setDate(t, new java.sql.Date(date
										.getTime().getTime()));
								break;
							case DatabaseType.BOOLEAN:
								statement.setBoolean(t, Boolean.valueOf(value
										.getValue().toString()));
								break;
							case DatabaseType.BYTE:
								statement.setByte(t, Byte.parseByte(value
										.getValue().toString()));
								break;
							case DatabaseType.FILE:
								try {
									// receives an java.io.File of the expected
									// file
									// TODO: check .jpg
									// .png - WORKS
									// TODO: check .doc
									// TODO: check .txt
									// TODO: check .rtf
									File file = (File) value.getValue();

									fis = new FileInputStream(file);
									statement.setBlob(t, fis,
											(int) file.length());
								} catch (Exception e) {
									// none
								}
								break;
							case DatabaseType.FLOAT:
								statement.setFloat(t, Float.parseFloat(value
										.getValue().toString()));
								break;
							case DatabaseType.LONG:
								statement.setLong(t, Long.parseLong(value
										.getValue().toString()));
								break;
							default:
								break;
							}

							t++;

						}

					}

				}

			}

			statement.executeUpdate();

			if (auto_increment) {
				ResultSet set = statement.getGeneratedKeys();
				conn.commit();
				if (set.next()) {
					id = set.getInt(1);
				}
			} else {
				conn.commit();
			}

		} catch (SQLException e) {
		} finally {
			connection.close();
			try {
				fis.close();
			} catch (Exception e) {
				// nothing
			}
		}

		return id;

	}

	/**
	 * A method to save an certain amount of parameters
	 * 
	 * @param connection
	 *            The connection object
	 * @param parameters
	 *            The parameters to save in the database table
	 * @param table
	 *            Table in which to save
	 * @param values
	 *            The values to save
	 * @param auto_increment
	 *            - If the statement should implement auto-increment
	 * @return Generated key from the database
	 */
	public static int save(Connection connection, String[] parameters,
			String table, DatabaseType[] values, boolean auto_increment) {

		java.sql.Connection conn = connection.getConnection();
		String insert = buildInsertQuery(parameters, table);
		int id = 0;
		FileInputStream fis = null;

		System.out.println(insert);

		try {
			PreparedStatement statement = null;

			if (auto_increment) {
				statement = conn.prepareStatement(insert,
						Statement.RETURN_GENERATED_KEYS);
			} else {
				statement = conn.prepareStatement(insert);
			}

			if (values != null && parameters.length == values.length) {

				for (int i = 1; i <= values.length; i++) {

					DatabaseType value = values[(i - 1)];

					switch (value.getType()) {
					case DatabaseType.STRING:
						statement.setString(i, value.getValue().toString());
						break;
					case DatabaseType.INTEGER:
						statement.setInt(i,
								Integer.parseInt(value.getValue().toString()));
						break;
					case DatabaseType.DOUBLE:
						statement.setDouble(i,
								Double.valueOf(value.getValue().toString()));
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
					case DatabaseType.FILE:
						try {
							// receives an java.io.File of the expected file
							// TODO: check .jpg
							// .png - WORKS
							// TODO: check .doc
							// TODO: check .txt
							// TODO: check .rtf
							File file = (File) value.getValue();

							fis = new FileInputStream(file);
							statement.setBlob(i, fis, (int) file.length());
						} catch (Exception e) {
							// none
						}
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

			}

			statement.executeUpdate();

			if (auto_increment) {
				ResultSet set = statement.getGeneratedKeys();
				conn.commit();
				if (set.next()) {
					id = set.getInt(1);
				}
			} else {
				conn.commit();
			}

		} catch (SQLException e) {
		} finally {
			connection.close();
			try {
				fis.close();
			} catch (Exception e) {
				// nothing
			}
		}

		return id;
	}

	private static String buildInsertQuery(String[] parameters, int times,
			String table) {

		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO ");
		builder.append(table);
		builder.append(" (");

		int last = parameters.length;
		for (int i = 1; i <= last; i++) {

			builder.append(parameters[(i - 1)]);

			if (i != last) {
				builder.append(", ");
			}

		}

		builder.append(") VALUES ");
		for (int i = 0; i < times; i++) {
			builder.append("( ");

			for (int x = 1; x <= last; x++) {

				builder.append("?");

				if (x != last) {
					builder.append(", ");
				}

			}

			builder.append(" )");
			if (i < (times - 1)) {
				builder.append(",");
			}
		}
		builder.append(";");

		return builder.toString().trim();

	}

	private static String buildInsertQuery(String[] parameters, String table) {

		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO ");
		builder.append(table);
		builder.append(" (");

		int last = parameters.length;
		for (int i = 1; i <= last; i++) {

			builder.append(parameters[(i - 1)]);

			if (i != last) {
				builder.append(", ");
			}

		}

		builder.append(") VALUES ( ");

		for (int i = 1; i <= last; i++) {

			builder.append("?");

			if (i != last) {
				builder.append(", ");
			}

		}

		builder.append(" );");

		return builder.toString().trim();
	}

}
