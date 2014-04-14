package org.casdev.web.database.main;

import java.util.Calendar;

import org.casdev.web.database.DeleteObject;
import org.casdev.web.database.GetObject;
import org.casdev.web.database.SaveObject;
import org.casdev.web.database.UpdateObject;
import org.casdev.web.database.connection.Connection;
import org.casdev.web.object.Order;
import org.casdev.web.object.Product;
import org.casdev.web.object.User;

public class Main2 {

	public static void main(String[] args) {

		final String TAB = "   ";
		
		Product p = new Product();
		p.setCategory("Animal");
		p.setName("Panda");
		p.setPrice(101.90);
		int id = SaveObject.save(new Connection(), p);
		p.setId(id);
		Product _p = new Product();
		_p.setCategory("Animal");
		_p.setName("Tiger");
		_p.setPrice(1099.98);
		User u = new User();
		u = new User();
		u.setId(1);
		_p.addUsers("hasse", u);
		u = new User();
		u.setId(2);
		_p.addUsers("tagge", u);
		u = new User();
		u.setId(3);
		_p.addUsers("jesse", u);
		_p.addAttributes("color", "blue/yellow");
		_p.addAttributes("race", "norwegian");
		_p.addAttributes("country", "finlandian highland");
		UpdateObject.update(new Connection(), p, _p, "WHERE _id = ?",
				new String[] { "id" });
		System.out.println("ProductID === " + id);
		p = (Product) GetObject.get(new Connection(), p,
				"WHERE _products._id = ?", new String[] { "id" });
		if (p != null) {
			System.out.println("\n"+ TAB + TAB +"Product:");
			System.out.println(TAB + "Category === " + p.getCategory());
			System.out.println(TAB + "ID === " + p.getId());
			System.out.println(TAB + "Name === " + p.getName());
			System.out.println(TAB + "Price === " + p.getPrice());
			for (String att : p.getAttributes().keySet()) {
				System.out.println(TAB + "Productattribute === " + att + " / "
						+ p.getAttributes().get(att));
			}
			for (String user : p.getUsers().keySet()) {
				System.out.println(TAB + "User associated withe the product === "
						+ user + " / " + p.getUsers().get(user).getId());
			}
			int rows = DeleteObject.delete(new Connection(), p,
					"WHERE _id = ?", new String[] { "id" });
			if (rows > 0) {
				System.out.println(TAB + "Product has been deleted");
			} else {
				System.out.println(TAB + "No product has been deleted");
			}
		}
		Order o = new Order();
		u = new User();
		u.setId(3);
		o.setUser(u);
		o.setPrice(139.99);
		id = SaveObject.save(new Connection(), o);
		o.setId(id);
		Order _o = new Order();
		_o.setId(id);
		u = new User();
		u.setId(7);
		_o.setUser(u);
		_o.setPrice(199.81);
		p = new Product();
		p.setId(1);
		_o.addProducts(p);
		p = new Product();
		p.setId(3);
		_o.addProducts(p);
		_o.addUsernames("Hieronemus");
		_o.addUsernames("Veronica");
		GetObject.get(new Connection(), new Order(), null, null);
		UpdateObject.update(new Connection(), o, _o, "WHERE _id = ?",
				new String[] { "id" });
		o = (Order) GetObject.get(new Connection(), new Order(), null, null);
		if (o != null) {
			System.out.println("\n"+ TAB + TAB +"Order:");
			System.out.println(TAB + "ID === " + o.getId());
			System.out.println(TAB + "Price === " + o.getPrice());
			for (Product product : o.getProducts()) {
				System.out.println(TAB + "ProductID === " + product.getId());
			}
			System.out.println(TAB + "User === " + o.getUser().getId());
			int rows = DeleteObject.delete(new Connection(), o,
					"WHERE _id = ?", new String[] { "id" });
			if (rows > 0) {
				System.out.println(TAB + "Order has been deleted");
			} else {
				System.out.println(TAB + "No order is deleted");
			}
		}

		User _u = new User();
		_u.setAdmin(true);
		_u.setEmail("kakan@home.se");
		_u.setPassword("hasse");
		_u.setUsername("hanson-jansson");
		id = SaveObject.save(new Connection(), _u);
		System.out.println("new userID? " + id);
		_u.setId(id);
		u = _u;
		u = (User) GetObject.get(new Connection(), _u,
				"WHERE _email = ? AND _password = ?", new String[] { "email",
						"password" });
		if (u != null) {
			System.out.println("\n"+ TAB + TAB +"User:");
			System.out.println(TAB + "ID === " + u.getId());
			System.out.println(TAB + "Username === " + u.getUsername());
			System.out.println(TAB + "Password === " + u.getPassword());
			System.out.println(TAB + "Admin === " + u.isAdmin());
			System.out.println(TAB + "Email === " + u.getEmail());
			System.out.println(TAB + "Member since === "
					+ u.getDate().get(Calendar.DATE));

			_u = u;
			_u.setAdmin(false);
			_u.setUsername("mohammed");
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2000);
			cal.set(Calendar.DATE, 5);
			_u.setDate(cal);
			System.out.println("");
			boolean update = UpdateObject.update(new Connection(), u, _u,
					"WHERE _id = ?", new String[] { "id" });
			System.out.println("isUpdated? " + update);
			int rows = DeleteObject.delete(new Connection(), _u, "WHERE _id = ?", new String[] { "id" });
			if (rows > 0) {
				System.out.println(TAB + "User has been deleted");
			} else {
				System.out.println(TAB + "User has not been succesfully deleted");
			}
		}

	}
}
