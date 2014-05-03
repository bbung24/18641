package ws.remote;

import handleConnections.DefaultSocketClient;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import db.DBController;

public class Server extends DefaultSocketClient{
	private DBController md;

	public Server(String strHost, int iPort) {
		super(strHost, iPort);

		md = new DBController();
		try {
			Connection connection = md.getConnection();
			Statement statement = connection.createStatement();

			String tableName = "users";

			StringBuilder col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("user_id VARCHAR(100),");
			col.append("pw VARCHAR(100),");
			col.append("age INT,");
			col.append("adress VARCHAR(100),");
			col.append("job VARCHAR(100)");

			md.createTable(tableName, col.toString(), statement);

			tableName = "checkups";

			col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("patient_id INT,");
			col.append("date TIMESTAMP,");
			col.append("result VARCHAR(100),");
			col.append("medication_list_id INT");

			md.createTable(tableName, col.toString(), statement);

			tableName = "distantdiagnosis";

			col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("name VARCHAR(100),");
			col.append("symptom VARCHAR(100),");
			col.append("pic_loc VARCHAR(100),");
			col.append("voc_loc VARCHAR(100),");
			col.append("doctor_id INT");

			md.createTable(tableName, col.toString(), statement);

			tableName = "medication_list";

			col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("check_up_id INT,");
			col.append("medication_id INT");

			md.createTable(tableName, col.toString(), statement);

			tableName = "medications";

			col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("name VARCHAR(100)");

			md.createTable(tableName, col.toString(), statement);

			statement.close();
			connection.close();
		} catch (SQLException ex) {  
			System.out.println ("SQLException:");
			while (ex != null)
			{  
				System.out.println ("SQLState: "
						+ ex.getSQLState());
				System.out.println ("Message:  "
						+ ex.getMessage());
				System.out.println ("Vendor:   "
						+ ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println ("");
			}
		}
	}

	// This handler will have to do two tasks.
	// One is that of saving data that was sent from android app.
	// Another is sending data when needed by android app.
	@Override
	public void handleInput(Message input) {
		// TODO: Need to think of communication between 
		// receiving data and sending data.
		// Also, in what steps will it be done.
		Connection connection = md.getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();

			String command = input.getCommand();

			if(command.equals(RemoteClientConstants.REGISTER)){
				register(input, statement);
			} else if(command.equals(RemoteClientConstants.LOGIN)){
				login(input, statement);
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void login(Message input, Statement statement){
		HashMap<String, Object> response = new HashMap<String, Object>();
		Message m = new Message("Server", null, response);
		HashMap<String, Object> data = input.getMap();
		try {
			System.out.println("ID: "+(String)data.get(RemoteClientConstants.LOGIN_ID));
			ArrayList<HashMap<String, Object>> db = md.getAllDataString("users", RemoteClientConstants.LOGIN_ID, 
					(String)data.get(RemoteClientConstants.LOGIN_ID), statement);
			if(db.size() == 0) {
				m.setCommand(RemoteClientConstants.LOGIN_FAIL);
			} else {
				HashMap<String, Object> user = db.get(0);
				System.out.println("input: "+ (String) data.get(RemoteClientConstants.LOGIN_PW));
				System.out.println("db: " +(String) user.get(RemoteClientConstants.LOGIN_PW));
				String pwd = (String) user.get(RemoteClientConstants.LOGIN_PW);
				if(pwd.equals((String) data.get(RemoteClientConstants.LOGIN_PW))){
					m.setCommand(RemoteClientConstants.LOGIN_SUCCESS);
				} else {
					m.setCommand(RemoteClientConstants.LOGIN_FAIL);
				}
			}
			sendOutput(m);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void register(Message input, Statement statement) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String,Object> data = input.getMap();
		int count = -1;
		try {
			count = md.countDataString("users", "user_id", (String)data.get("user_id"), statement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (count == 0){
			StringBuilder col = new StringBuilder();
			StringBuilder value = new StringBuilder();
			Iterator<Entry<String, Object>> it = data.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> pairs = it.next();
				System.out.println(pairs.getKey() + " = " + pairs.getValue());

				String key = pairs.getKey();
				Object dataValue = pairs.getValue();

				if(it.hasNext() && dataValue instanceof String){
					col.append(key+",");
					value.append("\""+dataValue+"\",");
				} else if(it.hasNext()){
					col.append(key+",");
					value.append(dataValue+",");
				} else {
					col.append(key);
					value.append(dataValue);
				}

				it.remove(); // avoids a ConcurrentModificationException
			}

			try {
				md.insertData("users", col.toString(), value.toString(), statement);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Message output = new Message("Server", RemoteClientConstants.REGISTER_SUCCESS, response);
			sendOutput(output);
		} else {
			Message output = new Message("Server", RemoteClientConstants.REGISTER_FAIL, response);
			sendOutput(output);
		}
	}


	public static void main (String arg[]){
		/* debug main; does daytime on local host */
		System.out.println("Server Started..");
		Server server = new Server("", 4444);
		server.start();
	}
}
