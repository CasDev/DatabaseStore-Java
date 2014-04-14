package org.casdev.web.database;

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

public class GetObject {

	/**
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
	public static List<Map<String, DatabaseType>> get(Connection connection, String table,
			DatabaseType[] parameters, Join[] joins, String where,
			DatabaseType[] whereParams, String order) {

		java.sql.Connection conn = connection.getConnection();
		String select = buildSelectQuery(parameters, table, joins, where, order);
		List<Map<String, DatabaseType>> objects = new ArrayList<Map<String, DatabaseType>>();

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
					case DatabaseType.BOOLEAN:
						statement.setBoolean(i,
								Boolean.valueOf(value.getValue().toString()));
						break;
					case DatabaseType.BYTE:
						statement.setByte(i,
								Byte.parseByte(value.getValue().toString()));
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
						map.put((String)value.getValue(), new DatabaseType(
								DatabaseType.STRING, set.getString(i)));
						break;
					case DatabaseType.INTEGER:
						map.put((String)value.getValue(), new DatabaseType(
								DatabaseType.INTEGER, set.getInt(i)));
						break;
					case DatabaseType.DOUBLE:
						map.put((String)value.getValue(), new DatabaseType(
								DatabaseType.DOUBLE, set.getDouble(i)));
						break;
					case DatabaseType.DATE:
						map.put((String)value.getValue(), new DatabaseType(
								DatabaseType.DATE, set.getDate(i)));
						break;
					case DatabaseType.BOOLEAN:
						map.put((String)value.getValue(), new DatabaseType(
								DatabaseType.STRING, set.getBoolean(i)));
						break;
					case DatabaseType.BYTE:
						map.put((String)value.getValue(), new DatabaseType(
								DatabaseType.STRING, set.getByte(i)));
						break;
					case DatabaseType.FILE:
						Blob blob = set.getBlob(i);

						int length = (int) blob.length();
						byte[] array = blob.getBytes(1, length);

						blob.free();
						map.put((String)value.getValue(), new DatabaseType(
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
