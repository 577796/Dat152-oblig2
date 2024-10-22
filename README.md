# Oblig #2 – Web Frameworks and Services with JWT Token and OAuth2

In this obligatory task, you will complete the implementation of the RESTful APIs for the eLibrary App and secure the endpoints.

The obligatory project consists of three projects
1. [library-spring-ws-rest](library-spring-ws-rest) project
2. [library-spring-ws-rest-security](library-spring-ws-rest-security) project
3. [library-spring-ws-rest-security-oauth](library-spring-ws-rest-security-oauth) project

Preambles: Clone the repository `https://github.com/tosdanoye/dat152-project2.git`. Then, import the maven projects into your preferred IDE. 
Some ways to run your project:
1.	Run the project from a command prompt (e.g., Mac Terminal). Navigate to the root folder of your project and run the maven command: ./mvnw spring-boot:run
2.	From your IDE: Right click on ‘LibraryApplication.java’ in the “no.hvl.dat152.rest.ws.main” package and “Run As” Java Application.

### Task 0: (Lab 3)
-	This oblig #2 builds on Lab #3 with advanced features and functionalities. Therefore, you are expected to have a working solution for the Book and Author REST APIs from Lab #3 (see `https://github.com/tosdanoye/dat152-lab/tree/main/F17-Lab`)

### Task 1: RESTful API Services 
Project: [library-spring-ws-rest](library-spring-ws-rest)

Implement APIs for users to list, order (borrow) and return a book. You will complete the “TODOs” in the following classes:
1.	UserService
	-	updateUser
	-	deleteUser
	-	createOrdersForUser
	-	getUserOrder
	-	getUserOrders
2.	UserController
	-	createUser
	-	updateUser
	-	deleteUser
	-	createUserOrder
	-	deleteUserOrder
	-	getUserOrder
3.	OrderService
	-	deleteOrder
	-	findAllOrders()
	-	findOrdersByExpiryDate
	-	updateOrder
4.	OrderController
	-	getAllBorrowOrders
	-	getBorrowOrder
	-	updateOrder
	-	deleteBookOrder	

Tips:
-	Note that there are custom CRUD methods already created in the repositories for each model. Feel free to add new ones, if you have needs for them.
-	Look at the JUnit tests for clues

### Task 2: HATEOAS, Pagination and Filtering
You will provide support to list all ‘orders’ that expire by the specified date (date format: yyyy-MM-dd). Also, your Rest API will provide support to paginate the list of orders (e.g., Pageable).

For these tasks, you will complete the “TODOs” in the following classes and methods:
1.	OrderService
	-	findOrdersByExpiryDate (same as in Task 1)
2.	OrderController
	-	getAllBorrowOrders	(same as in Task 1)
3.	Provide HATEOAS support for the createUserOrder such that there are links to other endpoints with actions possible for the resource.
	-	Add your links to orders in createUserOrder

### Task 3: Securing resource endpoints with JWT Access tokens from resource + auth server

Project: [library-spring-ws-rest-security](library-spring-ws-rest-security)

First, obtain access tokens for a super\_admin role (berit) and user role (robert) by running the TestSignupLogin.java Junit test. 

The access\_token can be copied from the console of your IDE. You can also start the application and use Postman to send a POST request with json body {“email”:”berit@email.com”, “password”:”berit\_pwd”} and {“email”:”robert@email.com”, “password”:”robert\_pwd”} to `http://localhost:8090/elibrary/api/v1/auth/login`. 

You will then receive a response with the access\_token. Copy the access\_token and replace the ‘super.admin.token.test’ and ‘user.token.test’ in the “application.properties” with these new values. To make your testing easier, the tokens are configured to expire in 5 days. When they expire, you need to request for new tokens and replace the old ones.

### TODOs:
1.	Copy your solutions from Task 2 to this project. 
2.	Secure the API endpoints you created in Task 0 and Task 1 by using @PreAuthorize(“hasAuthority(‘…’)”)
3.	Implement the APIs for super admin to assign and revoke roles (SUPER_ADMIN, ADMIN, USER) from specific users. To achieve this task: You will update the two methods to assign and revoke roles for specific users in the AdminUserController and AdminUserService classes.

The API support should use query parameter to achieve this task such as	`/users/{id}?role=`
-	Update the method to “Assign role”: updateUserRole(id, role)
-	Update the method to “Revoke role”: deleteUserRole(id, role)
-	Annotate the controller methods with the correct ‘SUPER_ADMIN’ authority (role)
-	Request to your Admin REST APIs will look like:
	-	Assign role: PUT http://localhost:8090/elibrary/api/v1/admin/users/1?role=ADMIN
	-	Revoke role: DELETE http://localhost:8090/elibrary/api/v1/admin/users/1?role=ADMIN
-	Test your solutions.

### Task 4: Securing resource endpoints with JWT Access tokens from 3rd party OAuth2 Server

Project: [library-spring-ws-rest-security-oauth](library-spring-ws-rest-security-oauth)

For this task, you will require the Keycloak Identity Provider server.
-	Follow the instruction [here](https://github.com/tosdanoye/dat152-lab/tree/main/keycloak-docker) to start and run the Keycloak IdP server using docker container.
	- `https://github.com/tosdanoye/dat152-lab/tree/main/keycloak-docker`
-	When you have started the server, you can obtain an access\_token for the admin and normal user by sending a POST request to the keycloak token endpoint:
	```
	curl -X POST http://localhost:8080/realms/SpringBootKeycloak/protocol/openid-connect/token --data 'grant_type=password&client_id=elibrary-rest-api&username=admin_user&password=berit_pwd'
	``` 
	Or use Postman to send the post request. 

You will then receive a response with the access_token. 
- Copy the access\_token and replace the `admin.token.test` and `user.token.test` in the `application.properties` with these new values. When they expire, you need to request for new tokens and replace the old ones.
- Copy your solutions from Task 3 and secure the endpoints by using @PreAuthorize(“hasAuthority(‘…’)”)
- Test your solutions.


### Testing
JUnit tests are provided, and you can run them in your IDE or from a terminal. 

Tips:
-	Watch out for the specific HttpStatus codes in the Junit tests and ensure that they correspond to what you are returning in the controller methods.
-	Use Postman in addition, to support your api testing.
