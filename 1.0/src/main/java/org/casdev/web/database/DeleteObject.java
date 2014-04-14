package org.casdev.web.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Calendar;

public class DeleteObject {

	/**
	 * @param connection
	 *            The connection object
	 * @param table
	 *            The database-table to use
	 * @param where
	 *            The WHERE-clause
	 * @param parameters
	 *            The parameters for the WHERE-clause
	 * @return How many rows has been affected
	 */
	public static int delete(Connection connection, String table, String where,
			DatabaseType[] parameters) {

		java.sql.Connection conn = connection.getConnection();
		String delete = buildDeleteQuery(table, where);
		int rows = 0;

		try {
			/**
			 * FOR DELETE WITH conn.setAutoCommit(false) conn.commit() NEEDS TO
			 * BE INVOKED
			 */
			PreparedStatement statement = conn.prepareStatement(delete);

			if (where != null && parameters.length > 0) {

				for (int i = 1; i <= parameters.length; i++) {

					int n = (i - 1);
					DatabaseType value = parameters[n];

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
						Calendar cal = (Calendar) value.getValue();
						statement.setDate(i, new Date(cal.getTime().getTime()));
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

				rows = statement.executeUpdate();
				conn.commit();

			}
		} catch (Exception e) {

		} finally {
			connection.close();
		}

		return rows;

	}

	private static String buildDeleteQuery(String table, String where) {

		StringBuilder builder = new StringBuilder();
		builder.append("DELETE FROM ");
		builder.append(table);
		builder.append(" ");
		builder.append(where);
		builder.append(" ");

		return builder.toString();
	}

}
