<h3>This is a small<h3>
<h1>ORM-framework</h1>
<h2>version 1.1</h2>
<br />
<br />
<p>This current version works only with SELECTs, INSERTs, UPDATEs and DELETEs for MySQL.<br /> It is still under development and plants are to be able to CREATE and DROP DATABASE on the go.<br />Differences from version 1.0 is:</p>
<ul>
<li>An easy to use, and implement, annotation-system in currently available for classes</li>
<li>If any NULL-values are received these won't make it through the annotation-driven system, thou there are still ways to get values by using the non-annotation driven methods</li>
</ul>
<br />
<span>A simple example of how to use the current code is:</span><br />
<code>
	// NOTE: This first bit of code of code is also demonstated in the class
	// Main1.java in the package org.casdev.web.database.main
	// NOTE: this is the so-called "non-annotation driven method
	
	// SaveObject.save is currently returning an auto_increment,
	// or 0 if no auto-increment is available
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DATE, -1);
	int id = SaveObject.save(new Connection(), new String[] { "_username",
			"_email", "_password", "_admin", "_date" }, "_users", new DatabaseType[] {
			new DatabaseType(DatabaseType.STRING,
					"kaffeutvecklare@gmail.com"),
			new DatabaseType(DatabaseType.STRING,
					"kaffeutvecklare@gmail.com"),
			new DatabaseType(DatabaseType.STRING, "secret"),
			new DatabaseType(DatabaseType.BOOLEAN, true),
			new DatabaseType(DatabaseType.CALENDAR, cal) }, true);
	// the last parameter, where here it is set to true indicates that
	// there will be an auto-incremented value assigned

	System.out.println("User #" + id + " was just created");
	// here you get an list with an Map that belongs all them parameters you
	// requested. In this example you will get _id and _email back
	List<Map<String, DatabaseType>> list = GetObject
			.get(new Connection(),
					"_users",
					new DatabaseType[] {
							new DatabaseType(DatabaseType.INTEGER, "_id") },
					null,
					"WHERE _username = ? and _password = ?",
					new DatabaseType[] {
							new DatabaseType(DatabaseType.STRING,
									"kaffeutvecklare@gmail.com"),
							new DatabaseType(DatabaseType.STRING, "secret") },
					null);

	// currently we will only find one input, but if there would be more
	// then one registered user with these credentials that we threw in with the
	// WHERE-clause, there would be more then one Map<String, DatabaseType>
	// containing the parameters we requested
	int id = 0;
	if (list.size() > 0) {
		id = (Integer) list.get(0).get("_id").getValue();
	}
	
	System.out.println("Requested ID === " + id);
	
	// here is the method for throwing an UPDATE-statement
	cal = Calendar.getInstance();
	cal.add(Calendar.DATE, +1);

	boolean updated = UpdateObject
			.update(new Connection(), new String[] { "_email", "_date" },
					new DatabaseType[] {
							new DatabaseType(DatabaseType.STRING,
									"jesus@saves.net"),
							new DatabaseType(DatabaseType.CALENDAR, cal) },
					"_users", "WHERE _id = ?",
					new DatabaseType[] { new DatabaseType(
							DatabaseType.INTEGER, id) });
	System.out.println("Successfully updated information? " + updated);

	// and then we have the DeleteObject.delete that return the number of
	// rows that it actually deleted
	int rows = DeleteObject.delete(new Connection(), "_users",
			"WHERE _id = ?", new DatabaseType[] { new DatabaseType(
					DatabaseType.INTEGER, id) });
	System.out.println(rows + " has been deleted");
	
	// NOTE: Now we will demonstrate the very easily annotation-system
	// we will start out by making a few classes as model
	// First we create an Order.java
	@DbObject(auto_increment = true, table = "_order")
	// NOTE: The @DbObject-annotation can take two parameters
	// auto_increment ... default = false
	//   if the table of choice is having an parameter with auto_increment
	//   ( currently only { auto_increment } is numbers - like IDs )
	// table ... required
	//   the name of the table
	public class Order {
		
		@DbField(field = "_id", join = Join.LEFT, value = "_id", table = "_users", joinon = "_user", clazz = User.class, connect = "id")
		// NOTE: the @DbField-annotation can be used to save down relative information to be used with other
		//    classes
		// field ... required
		//   the name of the field to collect ( NOTE: when handling with sepatate custom classes, this field is collected with the parameter {table} )
		//   in this example it will be '_users._id'
		// join ... default = ""
		//   what sort of join will be used. The class Join.java has a few static Strings to help with choice.
		//   the example will be look like this: LEFT JOIN _users ON _order._user = _users._id
		//   using the parameters table, {@DbObject table}, {joinon} and {field}
		//   NOTE: this example is while using the GetObject.get(...) method
		// value ... default = ""
		//   in the example of fetching an custom object this is the field that will be fetching from the parameter {table}
		//   the example will be looking like this: _users._id
		// table ... default = ""
		//   from which table to fetch information from about the object
		// joinon ... default = ""
		//   from which parameter to get parameter {@DbObject table}
		//   example from SELECT-statement is shown during JOIN-section
		// clazz ... default = String.class
		//   what class the object is
		// connect ... default = ""
		//   what parameter from the Object to add the value
		//   if 'connect = "id"' then the Object need to have an getId(Integer id)-method. It is very important for the get-methods parameter is an Object
		private User user;
		
		@DbField(field = "_product", clazz = Product.class, join = Join.LEFT, value = "_order", table = "_orderproducts", joinon = "_id", connect = "id")
		// NOTE: the @DbField-annotation can be used to save down relative information to be used with list
		//   that holds both custom objects or not
		// field ... required
		//   with List<Object> field is combined with the parameter {table}
		//   in this example there will be selected as "_orderproduct._product" in an SELECT-statement
		// clazz ... default = String.class
		//   it shows which class to instanciate
		// join ... default = ""
		//   what sort of join that should be used in SELECT-statements
		// value ... default = ""
		//   what field in parameter {table} to connect to {@DbObject table}.{joinon} in the join-statement
		// table ... default = ""
		//   from what table to get information from like {field}.{table}
		// joinon ... default = ""
		//   what field to join on
		// connect ... default = ""
		//   what parameter from the Object to add the value
		//   if 'connect = "id"' then the Object need to have an getId(Integer id)-method. It is very important for the get-methods parameter is an Object
		private List<Product> products;
		
		@DbField(field = "_username", clazz = String.class, join = Join.LEFT, value = "_order", table = "_usernames", joinon = "_id", connect = "id")
		// NOTE: the @DbField-annotation can be used to save down relative information to be used with list
		// field ... required
		//   with List<Object> field is combined with the parameter {table}
		//   in this example there will be selected as "_orderproduct._product" in an SELECT-statement
		// clazz ... default = String.class
		//   it shows which class to use
		// join ... default = ""
		//   what sort of join that should be used in SELECT-statements
		// value ... default = ""
		//   what field in parameter {table} to connect to {@DbObject table}.{joinon} in the join-statement
		// table ... default = ""
		//   from what table to get information from like {field}.{table}
		// joinon ... default = ""
		//   what field to join on
		// connect ... default = ""
		//   what parameter from the Object to add the value
		//   if 'connect = "id"' then the Object need to have an getId(Integer id)-method. It is very important for the get-methods parameter is an Object
		private List<String> usernames;
		
		@DbField(field = "_price", clazz = double.class)
		// field ... required
		//   what field to get
		// clazz ... default = String.class
		//   what class to use
		private double price;
		// NOTO: when using setter for the parameters the parameter of the method need to be an Object
		// for example: setPrice(Double price)
		
		@DbField(auto_increment = true, field = "_id", clazz = int.class)
		private int id;
		
		public Order() {
			this.user = new User();
			this.price = 0;
			this.id = 0;
			this.products = new ArrayList<Product>();
			this.usernames = new ArrayList<String>();
		}
		// NOTE: the Constructor's need to be no-parameters for the system to properly works
		
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
		// NOTE: when having an list then the set-method is instead an addProducts ( for example )
		// if the parameter is named products, then the setter-method need to be like "add"+ parameter
		// so to add an Product in the List<Product> products parameter, then the method need to be named
		// addParameters(Product product) - not addParameter(Product product)
		// NOTE: if it is all uppercase or lowercase or mixed doesn't matter
		
		public void addUsernames(String usernames) {
			this.usernames.add(usernames);
		}
		
		public void setUser(User user) {
			this.user = user;
		}
	
	}
	
	// NOTE: and now for the second, of three, models
	@DbObject(table = "_products")
	// NOTE: The @DbObject-annotation can take two parameters
	// table ... required
	//   the name of the table
	// NOTE: if auto_increment isn't listed in the @DbObject annotaion
	//   but annotated in the @DbField-annotaion then it will be overrided
	public class Product {
	
		@DbField(field = "_att", map = "_value", clazz = String.class, join = Join.LEFT, value = "_product", table = "_productattribute", joinon = "_id", connect = "id")
		// NOTE: the @DbField-annotation can be used to save down relative information to be used with maps
		//   that holds both custom objects or not
		//   current version only supports key/value with String/Object
		// field ... required
		//   with Map<String, Object> field is combined with the parameter {table}, the {field} parameter is representing the KEY in the map
		//   in this example there will be selected as "_productattribute._att", which is represeting the key that is obtained, in an SELECT-statement
		//   including with the "_productattribute._value", which is representing the value that is obtained
		// map ... semi-required - default = ""
		//   with Map<String, Object> field is combined with the parameter {table}, the {map} parameter is representing the VALUE in the map
		//   including with the "_productattribute._value", which is representing the value that is obtained, in an SELECT-statement
		//   in this example there will be selected as "_productattribute._att", which is represeting the key that is obtained
		// clazz ... default = String.class
		//   it shows which class to instanciate
		// join ... default = ""
		//   what sort of join that should be used in SELECT-statements
		// value ... default = ""
		//   what field in parameter {table} to connect to {@DbObject table}.{joinon} in the join-statement
		// table ... default = ""
		//   from what table to get information from like {field}.{table}
		// joinon ... default = ""
		//   what field to join on
		// connect ... default = ""
		//   in the example of an Map<String, String> there will be using an table with three parameters
		//   1. first the key-parameter which is set in {field}
		//   2. secondly the value-parameter which is set in {map}
		//   3. and lastly the parameter to bind this data with the Object at hand ( i.e. this )
		//   closely meaning: the {connect}-parameter is set to "id" which will take the value from the method "getId()" and insert it into the parameter which is set in {value}
		private Map<String, String> attributes;
		
		@DbField(field = "_username", map = "_userid", clazz = User.class, connect = "id", join = Join.LEFT, value = "_id", table = "_productusers", joinon = "_id")
		// NOTE: the @DbField-annotation can be used to save down relative information to be used with maps
		//   that holds both custom objects or not
		//   current version only supports key/value with String/Object
		// field ... required
		//   with Map<String, Object> field is combined with the parameter {table}, the {field} parameter is representing the KEY in the map
		//   in this example there will be selected as "_productuser._username", which is represeting the key that is obtained, in an SELECT-statement
		//   including with the "_productuser._userid", which is representing the value that is obtained
		// map ... semi-required - default = ""
		//   with Map<String, Object> field is combined with the parameter {table}, the {map} parameter is representing the VALUE in the map
		//   including with the "_productuser._userid", which is representing the value that is obtained, in an SELECT-statement
		//   in this example there will be selected as "_productuser._username", which is represeting the key that is obtained
		// clazz ... default = String.class
		//   it shows which class to instanciate
		// connect ... default = ""
		//   in the example of an Map<String, String> there will be using an table with three parameters
		//   1. first the key-parameter which is set in {field}
		//   2. secondly the value-parameter which is set in {map}
		//   3. and lastly the parameter to bind this data with the Object that it is pointed to
		//   closely meaning: the {connect}-parameter is set to "id" which will take the value from the method "getId()" and insert it into the parameter which is set in {value}
		// join ... default = ""
		//   what sort of join that should be used in SELECT-statements
		// value ... default = ""
		//   what field in parameter {table} to connect to {@DbObject table}.{joinon} in the join-statement
		// table ... default = ""
		//   from what table to get information from like {field}.{table}
		// joinon ... default = ""
		//   what field to join on
		private Map<String, User> users;
	
		@DbField(field = "_title", clazz = String.class)
		private String name;
	
		@DbField(auto_increment = true, field = "_id", clazz = int.class)
		// NOTE: if the {@DbObject} doesn't involve auto_increment and an {@DbField} does, then the auto_increment will be overridden
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
	
		// NOTE: when it comes to Map<String, Object> the setter-methods are very similare to those
		//   related to List<Object>'s but instead of one parameter of the method, there will be two.
		//   One for key, and one for value :)
		public void addAttributes(String key, String value) {
			this.attributes.put(key, value);
		}

		// NOTE: when it comes to Map<String, Object> the setter-methods are very similare to those
		//   related to List<Object>'s but instead of one parameter of the method, there will be two.
		//   One for key, and one for value :)
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
	
	// NOTE: the next, and last, model is an User
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

	}
	
	// and here after this there will be an demonstration of how to use the system of annotations
	
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
</code><br />
<span>The result is:</span>
<p>User #1 was just created<br />You requested ID#2<br />Belong to user with email kaffeutvecklare@gmail.com<br />1 has been deleted</p><br /><br />
<p>The creator of this framework can be contacted via Twitter @kaffeutvecklare or through email kaffeutvecklare@gmail.com</p>
<span>The result of the annotation-system is:</span>
<p>ProductID === 5</p>
<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Product:<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Category === Animal<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ID === 5<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Name === Tiger<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Price === 1099.98<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Productattribute === color / blue/yellow<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Productattribute === race / norwegian<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Productattribute === country / finlandian highland<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;User associated withe the product === jesse / 3<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;User associated withe the product === tagge / 2<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;User associated withe the product === hasse / 1<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Product has been deleted<br />
<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Order:<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ID === 5<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Price === 199.81<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ProductID === 1<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ProductID === 1<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ProductID === 3<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ProductID === 3<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;User === 5<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Order has been deleted<br />
<br />
new userID? 5
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;User:<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ID === 5<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Username === hanson-jansson<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Password === hasse<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Admin === true<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Email === kakan@home.se<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Member since === 14<br />
<br />
isUpdated? true<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;User has been deleted<br />
<br /><br />
<p>Current version of the annotation system only works with these classes:</p>
<ul>
<li>ArrayList</li>
<ul>
<li>Custom classes &lt;your own&gt;</li>
<li>String</li>
<li>Double</li>
<li>Integer</li>
<li>Long</li>
<li>Date</li>
<li>Calendar</li>
<li>Boolean</li>
<li>Byte</li>
<li>File</li>
<li>Float</li>
</ul>
<li>HashMap</li>
<ul>
<li>String</li>
<li>&nbsp;-&nbsp;</li>
<li>Custom classes &lt;your own&gt;</li>
<li>String</li>
<li>Double</li>
<li>Integer</li>
<li>Long</li>
<li>Date</li>
<li>Calendar</li>
<li>Boolean</li>
<li>Byte</li>
<li>File</li>
<li>Float</li>
</ul>
<li>And all else ofcourse</li>
</ul>