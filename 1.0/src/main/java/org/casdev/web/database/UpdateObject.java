package org.casdev.web.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

public class UpdateObject {

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

			try {
				PreparedStatement statement = conn.prepareStatement(update);

				int last = parameters.length;
				for (int i = 1; i <= last; i++) {
					DatabaseType value = parameterValues[(i - 1)];

					switch (value.getType()) {
					case 1:
						statement.setString(i, value.getValue().toString());
						break;
					case 2:
						statement
								.setInt(i, Integer.getInteger(value.getValue()
										.toString()));
						break;
					case 3:
						statement
								.setDouble(i, Double.parseDouble(value
										.getValue().toString()));
						break;
					case 4:
						Calendar date = (Calendar) value.getValue();

						statement.setDate(i, new java.sql.Date(date.getTime()
								.getTime()));
						break;
					case 5:
						statement.setBoolean(i,
								Boolean.valueOf(value.getValue().toString()));
						break;
					case 6:
						statement.setByte(i,
								Byte.parseByte(value.getValue().toString()));
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
					case 1:
						statement.setString(i, value.getValue().toString());
						break;
					case 2:
						statement.setInt(i,
								Integer.parseInt(value.getValue().toString()));
						break;
					case 3:
						statement
								.setDouble(i, Double.parseDouble(value
										.getValue().toString()));
						break;
					case 4:
						Calendar date = (Calendar) value.getValue();

						statement.setDate(i, new java.sql.Date(date.getTime()
								.getTime()));
						break;
					case 5:
						statement.setBoolean(i,
								Boolean.valueOf(value.getValue().toString()));
						break;
					case 6:
						statement.setByte(i,
								Byte.parseByte(value.getValue().toString()));
						break;
					default:
						break;
					}
				}

				statement.executeUpdate();
				conn.commit();

				result = true;

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
