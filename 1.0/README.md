<h3>This is a small<h3>
<h1>ORM-framework</h1>
<h2>version 1.0</h2>
<br />
<br />
<p>This current version works only with SELECTs, INSERTs, UPDATEs and DELETEs for MySQL.<br /> It is still under development and plans are to be able to CREATE and DROP DATABASE, and implement simple annotation-implementations into classes that are to be loaded and/or saved/updated</p>
<br />
<span>A simple example of hor to use the current code is:</span><br />
<code>
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
</code><br />
<span>The result is:</span>
<p>User #1 was just created<br />You requested ID#2<br />Belong to user with email kaffeutvecklare@gmail.com<br />1 has been deleted</p><br /><br />
<p>The creator of this framework can be contacted via Twitter @kaffeutvecklare or through email kaffeutvecklare@gmail.com</p>
