package org.casdev.web.database.main;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.casdev.web.database.DeleteObject;
import org.casdev.web.database.GetObject;
import org.casdev.web.database.SaveObject;
import org.casdev.web.database.UpdateObject;
import org.casdev.web.database.connection.Connection;
import org.casdev.web.database.connection.DatabaseType;
import org.casdev.web.database.connection.Join;

public class Main1 {

	public static void main(String[] args) {

		GetObject.get(new Connection(), "_order AS o", new DatabaseType[] {
				new DatabaseType(DatabaseType.INTEGER, "o._price"),
				new DatabaseType(DatabaseType.INTEGER, "op._price") },
				new Join[] { new Join(Join.INNER, "_orderproducts AS op",
						"o._id", "op._order") }, null, null, null);
		/*
		 * // SaveObject.save is currently returning an auto_increment. //
		 * doesn't support tables without auto_increment currently Calendar cal
		 * = Calendar.getInstance(); cal.add(Calendar.DATE, -1); int id =
		 * SaveObject.save(new Connection(), new String[] { "_username",
		 * "_email", "_password", "_admin", "_date" }, "_users", new
		 * DatabaseType[] { new DatabaseType(DatabaseType.STRING,
		 * "kaffeutvecklare@gmail.com"), new DatabaseType(DatabaseType.STRING,
		 * "kaffeutvecklare@gmail.com"), new DatabaseType(DatabaseType.STRING,
		 * "secret"), new DatabaseType(DatabaseType.BOOLEAN, true), new
		 * DatabaseType(DatabaseType.CALENDAR, cal) }, true);
		 * 
		 * System.out.println("User #" + id + " was just created"); // here you
		 * get an list with an Map that belongs all them parameters you //
		 * requested. In this example you will get _id and _email back
		 * List<Map<String, DatabaseType>> list = GetObject .get(new
		 * Connection(), "_users", new DatabaseType[] { new DatabaseType(
		 * DatabaseType.INTEGER, "_id") }, null,
		 * "WHERE _email = ? and _password = ?", new DatabaseType[] { new
		 * DatabaseType(DatabaseType.STRING, "castell_john@hotmail.com"), new
		 * DatabaseType(DatabaseType.STRING, "pascal") }, null);
		 * 
		 * id = 0; if (list.size() > 0) { id = (Integer)
		 * list.get(0).get("_id").getValue(); }
		 * 
		 * System.out.println("Requested ID === " + id); cal =
		 * Calendar.getInstance(); cal.add(Calendar.DATE, +1);
		 * 
		 * boolean updated = UpdateObject.update(new Connection(), new String[]
		 * { "_email", "_date" }, new DatabaseType[] { new
		 * DatabaseType(DatabaseType.STRING, "jesus@saves.net"), new
		 * DatabaseType(DatabaseType.CALENDAR, cal) }, "_users",
		 * "WHERE _id = ?", new DatabaseType[] { new DatabaseType(
		 * DatabaseType.INTEGER, id) });
		 * System.out.println("Successfully updated information? " + updated);
		 * 
		 * // and then we have the DeleteObject.delete that return the number of
		 * // rows that it actually deleted int rows = DeleteObject.delete(new
		 * Connection(), "_users", "WHERE _id = ?", new DatabaseType[] { new
		 * DatabaseType( DatabaseType.INTEGER, id) }); System.out.println(rows +
		 * " has been deleted");
		 */

	}
}
