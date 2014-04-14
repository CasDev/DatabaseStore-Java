package org.casdev.web.database;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class SaveObject {

	/**
	 * @param connection
	 *            The connection object
	 * @param parameters
	 *            The parameters to save in the database table
	 * @param table
	 *            Table in which to save
	 * @param values
	 *            The values to save
	 * @return Generated key from the database
	 */
	public static int save(Connection connection, String[] parameters,
			String table, DatabaseType[] values) {

		java.sql.Connection conn = connection.getConnection();
		String insert = buildInsertQuery(parameters, table);
		int id = 0;
		FileInputStream fis = null;
		
		try {
			PreparedStatement statement = null;

			statement = conn.prepareStatement(insert,
					Statement.RETURN_GENERATED_KEYS);

			if (values != null && parameters.length == values.length) {

				for (int i = 1; i <= values.length; i++) {

					DatabaseType value = values[(i - 1)];

					switch (value.getType()) {
					case 1:
						statement.setString(i, value.getValue().toString());
						break;
					case 2:
						statement.setInt(i,
								Integer.parseInt(value.getValue().toString()));
						break;
					case 3:
						statement.setDouble(i,
								Double.valueOf(value.getValue().toString()));
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
					case 7:
						try {
							// receives an java.io.File of the expected file
							// TODO: check .jpg
							// .png - WORKS
							// TODO: check .doc
							// TODO: check .txt
							File file = (File) value.getValue();

							fis = new FileInputStream(file);
							statement.setBlob(i, fis, (int) file.length());
						} catch (Exception e) {
							// none
						}
						break;
					default:
						break;
					}

				}

			}

			statement.executeUpdate();
			ResultSet set = statement.getGeneratedKeys();
			conn.commit();
			if (set.next()) {
				id = set.getInt(1);
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
