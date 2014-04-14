package org.casdev.web.database;

import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {

		// SaveObject.save is currently returning an auto_increment.
		// doesn't support tables without auto_increment currently
		int id = SaveObject.save(new Connection(), new String[] { "_username",
				"_email", "_password" }, "_users", new DatabaseType[] {
				new DatabaseType(DatabaseType.STRING,
						"kaffeutvecklare@gmail.com"),
				new DatabaseType(DatabaseType.STRING,
						"kaffeutvecklare@gmail.com"),
				new DatabaseType(DatabaseType.STRING, "secret") });

		System.out.println("User #" + id + " was just created");
		// here you get an list with an Map that belongs all them parameters you
		// requested. In this example you will get _id and _email back
		List<Map<String, DatabaseType>> list = GetObject
				.get(new Connection(),
						"_users",
						new DatabaseType[] {
								new DatabaseType(DatabaseType.INTEGER, "_id"),
								new DatabaseType(DatabaseType.STRING, "_email") },
						null,
						"WHERE _username = ? and _password = ?",
						new DatabaseType[] {
								new DatabaseType(DatabaseType.STRING,
										"kaffeutvecklare@gmail.com"),
								new DatabaseType(DatabaseType.STRING, "secret") },
						null);

		for (Map<String, DatabaseType> map : list) {
			System.out.println("You requested ID#" + map.get("_id").getValue());
			System.out.println("Belonging to user with email "
					+ map.get("_email").getValue());
		}

		// and then we have the DeleteObject.delete that return the number of
		// rows that it actually deleted
		int rows = DeleteObject.delete(new Connection(), "_users",
				"WHERE _id = ?", new DatabaseType[] { new DatabaseType(
						DatabaseType.INTEGER, id) });
		System.out.println(rows + " has been deleted");

	}
}
