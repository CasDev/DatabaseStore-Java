package org.casdev.web.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.casdev.web.database.annotation.Helper;
import org.casdev.web.database.annotation.Item;
import org.casdev.web.database.connection.Connection;
import org.casdev.web.database.connection.DatabaseType;

public class DeleteObject {

	/**
	 * Method to delete from the database
	 * 
	 * @param conn
	 *            - The Connection-class
	 * @param obj
	 *            - The Object to save. Has to be annotated with @DBField and @DBObject
	 * @param where
	 *            - The WHERE-clause to use with the DELETE-statement
	 * @param objParams
	 *            - The parameters to use from the obj-Object with the
	 *            WHERE-clause
	 * @return int - The numbers of rows deleted
	 */
	public static int delete(Connection conn, Object obj, String where,
			String[] objParams) {
		Helper helper = Item.run(obj, Item.SELECT);
		DatabaseType[] whereParams = Item.where(obj, objParams);

		for (Item item : helper.getItems()) {
			if (Helper.isMap(item.getObject())
					|| Helper.isList(item.getObject())) {
				String _where = "WHERE " + item.getValue() + " = ?";
				DatabaseType type = Item.getParameter(obj, item.getConnect());
				DatabaseType[] _objParams = new DatabaseType[] { (type != null ? type
						: new DatabaseType(DatabaseType.STRING, "")) };
				
				DeleteObject.delete(new Connection(), item.getTable(), _where, _objParams);
			}
		}
		return delete(conn, helper.getTable(), where, whereParams);
	}

	/**
	 * Method to delete from the database
	 * 
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

		System.out.println(delete);

		try {
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
