package org.casdev.web.database.connection;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connection {

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		}
	}

	private java.sql.Connection conn;
	private String server;
	private String database;
	private String user;
	private String password;

	public Connection() {
		Properties prop = new Properties();
		InputStream stream = null;
		String path = "database.properties";

		try {
			stream = this.getClass().getClassLoader().getResourceAsStream(path);
			prop.load(stream);
		} catch (Exception e) {
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
				}
			}
		}

		connect(prop.getProperty("server"), prop.getProperty("database"),
				prop.getProperty("user"), prop.getProperty("password"));

	}

	public Connection(String server, String database, String username,
			String password) {

		if (server == null || server.equals("localhost")) {
			server = "127.0.0.1";
		}

		connect(server, database, username, password);
	}

	private void connect(String server, String database, String username,
			String password) {

		this.server = server;
		this.database = database;
		this.user = username;
		this.password = password;

		try {
			this.conn = DriverManager.getConnection("jdbc:mysql://"
					+ this.server + "/" + this.database, this.user,
					this.password);
			conn.setAutoCommit(false);
		} catch (SQLException e) {
		}
	}

	public java.sql.Connection getConnection() {
		try {
			if (this.conn.isClosed()) {
				this.connect(this.server, this.database, this.user,
						this.password);
			}
		} catch (Exception e) {
		}

		return this.conn;
	}

	public void close() {

		try {
			if (this.conn != null && !this.conn.isClosed()) {
				this.conn.close();
			}
		} catch (SQLException e) {
		}
	}

}
