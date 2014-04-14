package org.casdev.web.object;

import java.util.Calendar;
import java.util.Date;

import org.casdev.web.database.GetObject;
import org.casdev.web.database.annotation.DbField;
import org.casdev.web.database.annotation.DbObject;
import org.casdev.web.database.connection.Connection;

@DbObject(table = "_users", auto_increment = false)
public class User {

	@DbField(field = "_username", clazz = String.class)
	private String username;

	@DbField(field = "_password", clazz = String.class)
	private String password;

	@DbField(field = "_email", clazz = String.class)
	private String email;

	@DbField(field = "_id", auto_increment = true, clazz = int.class)
	private int id;

	@DbField(field = "_admin", clazz = boolean.class)
	private boolean admin;

	@DbField(field = "_date", clazz = Calendar.class, noUpdate = true)
	private Calendar date;

	public User() {
		this.username = "";
		this.password = "";
		this.email = "";
		this.id = 0;
		this.admin = false;
		this.date = Calendar.getInstance();
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public int getId() {
		return id;
	}
	
	public Calendar getDate() {
		return date;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	
	public void setDate(Calendar date) {
		this.date = date;
	}

	public void load() {
		User user = null;
		if (this.username.length() > 0 && this.password.length() > 0) {
			user = (User) GetObject.get(new Connection(), this,
					"WHERE _username = ? AND _password = ?", new String[] {
							"username", "password" });
		} else if (this.id > 0) {
			user = (User) GetObject.get(new Connection(), this,
					"WHERE _id = ?", new String[] { "id" });
		}
		
		if (user != null)
		{
			this.copy(user);
		}
	}
	
	public void copy(User user) {
		this.id = user.id;
		this.admin = user.admin;
		this.date = user.date;
		this.email = user.email;
		this.password = user.password;
		this.username = user.username;
	}
}
