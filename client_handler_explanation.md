# ClientHandler - Detailed Explanation

## What is ClientHandler?

`ClientHandler` is a **worker thread** that handles communication with a single client. When a client connects to the server, the server creates a new `ClientHandler` instance for that specific client and runs it in its own thread.

## Why Use Threads?

Without threads, the server could only handle ONE client at a time. With threads, the server can handle MULTIPLE clients simultaneously because each client gets its own dedicated handler running in parallel.

```
Server (Main Thread)
├── Client #1 → ClientHandler Thread #1
├── Client #2 → ClientHandler Thread #2  
├── Client #3 → ClientHandler Thread #3
└── ... (keeps accepting more clients)
```

---

## How ClientHandler Works - Step by Step

### 1. **Initialization (Constructor)**
```java
public ClientHandler(Socket socket, int clientId) {
    this.socket = socket;      // Socket connection to THIS specific client
    this.clientId = clientId;  // Unique ID for logging (Client #1, #2, etc.)
}
```

When created, it receives:
- **Socket**: The network connection to the client
- **Client ID**: A number for identifying this client in logs

---

### 2. **Thread Execution (run method)**

When the thread starts, it does three main things:

#### A. **Setup Communication Streams**
```java
// INPUT STREAM - To READ messages FROM the client
InputStreamReader stream = new InputStreamReader(socket.getInputStream());
reader = new BufferedReader(stream);

// OUTPUT STREAM - To WRITE messages TO the client
writer = new PrintWriter(socket.getOutputStream(), true);
```

Think of it like this:
- **Reader** = Your ears (listening to client)
- **Writer** = Your mouth (talking to client)

#### B. **Listen for Requests (Main Loop)**
```java
String request;
while ((request = reader.readLine()) != null) {
    System.out.println("Client #" + clientId + " request: " + request);
    
    String response = processRequest(request);  // Process the request
    writer.println(response);                   // Send response back
    
    System.out.println("Client #" + clientId + " response: " + response);
}
```

This loop:
1. **Waits** for a message from the client (`reader.readLine()`)
2. **Processes** the message
3. **Sends back** a response
4. **Repeats** until the client disconnects

#### C. **Cleanup**
```java
finally {
    try {
        if (socket != null) socket.close();
        System.out.println("Client #" + clientId + " disconnected");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

When the client disconnects or an error occurs, close the connection properly.

---

### 3. **Request Processing (processRequest method)**

This is the **brain** of the handler. It parses the client's request and routes it to the correct action.

```java
private String processRequest(String request) {
    // Parse the request: "ACTION|TABLE|DATA"
    String[] parts = request.split("\\|", 3);
    
    String action = parts[0];  // What to do: ADD, UPDATE, DELETE, FIND, LIST
    String table = parts[1];   // Which table: Cars, Branches, Insurance, etc.
    String data = parts[2];    // The actual data
    
    // Route to the correct handler
    switch (action) {
        case "ADD":    return handleAdd(table, data);
        case "UPDATE": return handleUpdate(table, data);
        case "DELETE": return handleDelete(table, data);
        case "FIND":   return handleFind(table, data);
        case "LIST":   return handleList(table);
    }
}
```

**Example Request Flow:**
```
Client sends: "ADD|Cars|Toyota,Camry,2020,ABC123,50.00,Available,Red,10000"

ClientHandler parses:
- action = "ADD"
- table = "Cars"
- data = "Toyota,Camry,2020,ABC123,50.00,Available,Red,10000"

ClientHandler calls: handleAdd("Cars", "Toyota,Camry,2020...")
```

---

## The 5 Main Actions Explained

### 1. **handleAdd** - Insert new records

```java
private String handleAdd(String table, String data) {
    // 1. Connect to database
    Connection conn = DbConnection.getConnection();
    
    // 2. Parse the data fields
    String[] fields = data.split(",");
    
    // 3. Based on table, call specific add method
    switch (table) {
        case "Cars":     return addCar(conn, fields);
        case "Branches": return addBranch(conn, fields);
        // etc...
    }
}

private String addCar(Connection conn, String[] fields) {
    // Execute SQL INSERT
    String sql = "INSERT INTO Cars(...) VALUES (?,?,?,...)";
    PreparedStatement pst = conn.prepareStatement(sql);
    pst.setString(1, fields[0]);  // make
    pst.setString(2, fields[1]);  // model
    // ... set all fields
    pst.executeUpdate();
    
    return "SUCCESS|Car added successfully";
}
```

**Flow:**
```
Client → "ADD|Cars|Toyota,Camry,..." 
       ↓
ClientHandler → Parses data → Executes SQL INSERT 
       ↓
ClientHandler → "SUCCESS|Car added successfully"
       ↓
Client receives success message
```

---

### 2. **handleUpdate** - Modify existing records

```java
private String updateCar(Connection conn, String[] fields) {
    // First field is the ID, rest are new values
    String sql = "UPDATE Cars SET make=?, model=?, ... WHERE car_id=?";
    PreparedStatement pst = conn.prepareStatement(sql);
    pst.setString(1, fields[1]);  // new make
    pst.setString(2, fields[2]);  // new model
    // ...
    pst.setInt(9, Integer.parseInt(fields[0]));  // car_id (WHERE clause)
    pst.executeUpdate();
    
    return "SUCCESS|Car updated successfully";
}
```

**Data Format:** `UPDATE|Cars|5,Toyota,Camry,2021,ABC123,...`
- First value (5) = car_id to update
- Rest = new values

---

### 3. **handleDelete** - Remove records

```java
private String handleDelete(String table, String data) {
    int id = Integer.parseInt(data);  // data is just the ID
    
    String sql = "DELETE FROM Cars WHERE car_id=?";
    PreparedStatement pst = conn.prepareStatement(sql);
    pst.setInt(1, id);
    pst.executeUpdate();
    
    return "SUCCESS|Record deleted successfully";
}
```

**Data Format:** `DELETE|Cars|5`
- Just the ID to delete

---

### 4. **handleFind** - Retrieve one record

```java
private String findCar(Connection conn, int id) {
    String sql = "SELECT * FROM Cars WHERE car_id=?";
    PreparedStatement pst = conn.prepareStatement(sql);
    pst.setInt(1, id);
    ResultSet rs = pst.executeQuery();
    
    if (rs.next()) {
        // Build response with all fields separated by commas
        return "SUCCESS|" + 
               rs.getString("make") + "," +
               rs.getString("model") + "," +
               rs.getInt("year") + "," +
               // ... all fields
    }
    return "ERROR|Car not found";
}
```

**Request:** `FIND|Cars|5`
**Response:** `SUCCESS|Toyota,Camry,2020,ABC123,50.00,Available,Red,10000`

The client then parses this response to fill the form fields.

---

### 5. **handleList** - Get all records for ComboBoxes

```java
private String listCars(Connection conn) {
    String sql = "SELECT car_id, make, model FROM Cars ORDER BY car_id";
    ResultSet rs = st.executeQuery(sql);
    
    StringBuilder result = new StringBuilder("SUCCESS|");
    boolean first = true;
    
    while (rs.next()) {
        if (!first) result.append(";");  // Separate items with semicolon
        result.append(rs.getInt("car_id"))
              .append(" - ")
              .append(rs.getString("make"))
              .append(" ")
              .append(rs.getString("model"));
        first = false;
    }
    return result.toString();
}
```

**Request:** `LIST|Cars|`
**Response:** `SUCCESS|1 - Toyota Camry;2 - Honda Civic;3 - Ford Focus`

The client splits by `;` to populate ComboBox items.

---

## Real-World Example: Adding a Car

### Step 1: Client Form
User fills in:
- Make: Toyota
- Model: Camry
- Year: 2020
- Plate: ABC123
- Rate: 50.00
- Status: Available
- Color: Red
- Mileage: 10000

### Step 2: Client Sends Request
```java
String data = "Toyota,Camry,2020,ABC123,50.00,Available,Red,10000";
String request = "ADD|Cars|" + data;
writer.println(request);  // Send to server
```

### Step 3: Server Receives (ClientHandler)
```
Client #1 request: ADD|Cars|Toyota,Camry,2020,ABC123,50.00,Available,Red,10000
```

### Step 4: ClientHandler Processes
1. `processRequest()` splits: action="ADD", table="Cars"
2. Calls `handleAdd("Cars", "Toyota,Camry,...")`
3. Calls `addCar(conn, fields)`
4. Executes SQL INSERT
5. Returns "SUCCESS|Car added successfully"

### Step 5: Client Receives Response
```java
String response = reader.readLine();  // "SUCCESS|Car added successfully"
String[] parts = response.split("\\|");

if (parts[0].equals("SUCCESS")) {
    JOptionPane.showMessageDialog(this, parts[1]);  // Show success message
}
```

---

## Key Benefits of This Design

✅ **Separation of Concerns**
- Client = UI/Display
- Server = Database/Business Logic

✅ **Multiple Clients**
- Each client gets its own thread
- All can work simultaneously

✅ **Centralized Database**
- Only server connects to database
- Better security and control

✅ **Easy to Maintain**
- Change database logic on server only
- No need to update all clients

✅ **Scalability**
- Add more tables/features easily
- Just add new cases in switch statements

---

## Summary

**ClientHandler** is like a **personal assistant** for each client:

1. **Listens** to what the client wants (requests)
2. **Understands** the request format (parses)
3. **Does the work** (database operations)
4. **Reports back** (sends response)
5. **Repeats** until client leaves

Each client gets their own assistant (thread), so many clients can be served at the same time! 🎉
