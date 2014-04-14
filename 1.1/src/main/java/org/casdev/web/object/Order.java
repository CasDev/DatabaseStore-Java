package org.casdev.web.object;

import java.util.ArrayList;
import java.util.List;

import org.casdev.web.database.annotation.DbField;
import org.casdev.web.database.annotation.DbObject;
import org.casdev.web.database.connection.Join;

@DbObject(auto_increment = true, table = "_order")
public class Order {
	
	@DbField(field = "_id", join = Join.LEFT, value = "_id", table = "_users", joinon = "_user", clazz = User.class, connect = "id")
	private User user;
	
	@DbField(field = "_product", clazz = Product.class, join = Join.LEFT, value = "_order", table = "_orderproducts", joinon = "_id", connect = "id")
	private List<Product> products;
	
	@DbField(field = "_username", clazz = String.class, join = Join.LEFT, value = "_order", table = "_usernames", joinon = "_id", connect = "id")
	private List<String> usernames;
	
	@DbField(field = "_price", clazz = double.class)
	private double price;
	
	@DbField(auto_increment = true, field = "_id", clazz = int.class)
	private int id;
	
	public Order() {
		this.user = new User();
		this.price = 0;
		this.id = 0;
		this.products = new ArrayList<Product>();
		this.usernames = new ArrayList<String>();
	}
	
	public List<String> getUsernames() {
		return usernames;
	}
	
	public int getId() {
		return id;
	}
	
	public List<Product> getProducts() {
		return products;
	}
	
	public double getPrice() {
		return price;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public void addProducts(Product products) {
		this.products.add(products);
	}
	
	public void addUsernames(String usernames) {
		this.usernames.add(usernames);
	}
	
	public void setUser(User user) {
		this.user = user;
	}

}
