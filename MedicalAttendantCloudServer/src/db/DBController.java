package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBController {  
	public Connection getConnection() {  
		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
		}

		try {
			String jdbcUrl = "jdbc:mysql://localhost:3306/test";
			String userId = "ec2-user";
			String userPass = "";

			conn = DriverManager.getConnection(jdbcUrl, userId, userPass);
		}catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
		return conn; 
	}

	public void createTable(String tableName,
			String format, Statement stmt) throws SQLException { 
		StringBuilder command = new StringBuilder();
		command.append("CREATE TABLE ");
		command.append(tableName);
		command.append("(");
		command.append(format);
		command.append(");");
		stmt.executeUpdate(command.toString());
	}
	
	public void insertData(String tableName,String col, 
			String value, Statement stmt) throws SQLException {
		StringBuilder command = new StringBuilder();
		command.append("INSERT INTO ");
		command.append(tableName);
		command.append(" (");
		command.append(col);
		command.append(")");
		command.append(" VALUES (");
		command.append(value);
		command.append(");");
		stmt.executeUpdate(command.toString());
	}
	
	public void updateDate(String tableName, ArrayList<String> fields, 
			ArrayList<String> values, Statement stmt) throws SQLException {
		StringBuilder command = new StringBuilder();
		command.append("UPDATE ");
		command.append(tableName);
		command.append(" SET ");
		for(int i = 0; i < fields.size(); i++){
			command.append(fields.get(i));
			command.append("=");
			command.append(values.get(i));
			if (i != fields.size()-1)
				command.append(",");
		}
		command.append(";");
		stmt.executeUpdate(command.toString());
	}

	public void deleteData(String tableName, String field, 
			String value, Statement stmt) throws SQLException {
		StringBuilder command = new StringBuilder();
		command.append("DELETE FROM ");
		command.append(tableName);
		command.append(" WHERE ");
		command.append(field);
		command.append("=");
		command.append(value);
		command.append(";");
		stmt.executeUpdate(command.toString());
	}
	
	public void showTable(String tableName,
			Statement stmt) throws SQLException {  
		String query = "SELECT * FROM " + tableName;
		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				if (i > 1) System.out.print(", ");
				System.out.print(rs.getString(i));
			}
			System.out.println();
		}
		rs.close();
	}
}
