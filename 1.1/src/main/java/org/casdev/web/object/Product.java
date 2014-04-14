package org.casdev.web.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.casdev.web.database.annotation.DbField;
import org.casdev.web.database.annotation.DbObject;
import org.casdev.web.database.connection.Join;

@DbObject(table = "_products")
public class Product {

	@DbField(field = "_att", map = "_value", clazz = String.class, join = Join.LEFT, value = "_product", table = "_productattribute", joinon = "_id", connect = "id")
	private Map<String, String> attributes;
	
	@DbField(field = "_username", map = "_userid", clazz = User.class, connect = "id", join = Join.LEFT, value = "_id", table = "_productusers", joinon = "_id")
	private Map<String, User> users;

	@DbField(field = "_title", clazz = String.class)
	private String name;

	@DbField(auto_increment = true, field = "_id", clazz = int.class)
	private int id;

	@DbField(field = "_category", clazz = String.class)
	private String category;

	@DbField(field = "_price", clazz = double.class)
	private double price;

	public Product() {
		this.attributes = new HashMap<String, String>();
		this.users = new HashMap<String, User>();
		this.name = "";
		this.id = 0;
		this.price = 0;
		this.category = "";
	}

	public void addAttributes(String key, String value) {
		this.attributes.put(key, value);
	}
	
	public void addUsers(String key, User value) {
		this.users.put(key, value);
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public Map<String, User> getUsers() {
		return users;
	}

	public String getCategory() {
		return category;
	}

	public double getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

}
