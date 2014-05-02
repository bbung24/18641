package server;

import handleConnections.DefaultSocketClient;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.DBController;

public class Server extends DefaultSocketClient{
	public Server(String strHost, int iPort) {
		super(strHost, iPort);
		
		DBController md = new DBController();
		try {
			Connection connection = md.getConnection();
			Statement statement = connection.createStatement();

			String tableName = "users";
			
			StringBuilder col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("pw VARCHAR(100),");
			col.append("age INT,");
			col.append("zip_code INT,");
			col.append("is_doctor BOOLEAN");

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
			col.append("voc_loc VARCHAR(100)");

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
	
	public static void main (String arg[]){
		/* debug main; does daytime on local host */
		System.out.println("Server Started..");
		Server server = new Server("", 4444);
		server.start();
	}
}
