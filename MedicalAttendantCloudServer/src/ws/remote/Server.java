package ws.remote;

import handleConnections.DefaultSocketClient;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import db.DBController;

public class Server extends DefaultSocketClient
{
	private DBController md;

	public Server(String strHost, int iPort)
	{
		super(strHost, iPort);

		String tableName;
		StringBuilder col;
		md = new DBController();
		Connection connection = md.getConnection();
		Statement statement = null;
		try
		{
			statement = connection.createStatement();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		try
		{
			tableName = "users";

			col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("user_id VARCHAR(100),");
			col.append("pw VARCHAR(100),");
			col.append("age INT,");
			col.append("address VARCHAR(100),");
			col.append("job VARCHAR(100)");

			md.createTable(tableName, col.toString(), statement);
		} catch (SQLException ex)
		{
			System.out.println("SQLException:");
			while (ex != null)
			{
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("Message:  " + ex.getMessage());
				System.out.println("Vendor:   " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}
		try
		{
			tableName = "checkups";

			col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("patient_id VARCHAR(100),");
			col.append("date VARCHAR(100),");
			col.append("result VARCHAR(100),");
			col.append("doctor_id VARCHAR(100)");

			md.createTable(tableName, col.toString(), statement);
		} catch (SQLException ex)
		{
			System.out.println("SQLException:");
			while (ex != null)
			{
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("Message:  " + ex.getMessage());
				System.out.println("Vendor:   " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}
		try
		{
			tableName = "distantdiagnosis";

			col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("patient_id VARCHAR(100),");
			col.append("doctor_id VARCHAR(100),");
			col.append("symptom VARCHAR(100),");
			col.append("pic_loc VARCHAR(100),");
			col.append("voc_loc VARCHAR(100),");
			col.append("date VARCHAR(100)");

			md.createTable(tableName, col.toString(), statement);
		} catch (SQLException ex)
		{
			System.out.println("SQLException:");
			while (ex != null)
			{
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("Message:  " + ex.getMessage());
				System.out.println("Vendor:   " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}
		try
		{
			tableName = "medications";

			col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("name VARCHAR(100)");

			md.createTable(tableName, col.toString(), statement);
		} catch (SQLException ex)
		{
			System.out.println("SQLException:");
			while (ex != null)
			{
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("Message:  " + ex.getMessage());
				System.out.println("Vendor:   " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}
		try
		{
			tableName = "examination_relationships";

			col = new StringBuilder();
			col.append("check_up_id INT,");
			col.append("medication_id INT");

			md.createTable(tableName, col.toString(), statement);
		} catch (SQLException ex)
		{
			System.out.println("SQLException:");
			while (ex != null)
			{
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("Message:  " + ex.getMessage());
				System.out.println("Vendor:   " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}
		try
		{
			tableName = "taken_relationships";

			col = new StringBuilder();
			col.append("check_up_id INT,");
			col.append("medication_id INT,");
			col.append("date VARCHAR(100)");

			md.createTable(tableName, col.toString(), statement);

			statement.close();
			connection.close();
		} catch (SQLException ex)
		{
			System.out.println("SQLException:");
			while (ex != null)
			{
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("Message:  " + ex.getMessage());
				System.out.println("Vendor:   " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}
	}

	// This handler will have to do two tasks.
	// One is that of saving data that was sent from android app.
	// Another is sending data when needed by android app.
	@Override
	public void handleInput(Message input)
	{
		// TODO: Need to think of communication between
		// receiving data and sending data.
		// Also, in what steps will it be done.
		Connection connection = md.getConnection();
		Statement statement = null;
		try
		{
			statement = connection.createStatement();

			String command = input.getCommand();

			if (command.equals(RemoteClientConstants.REGISTER))
			{
				register(input, statement);
			} else if (command.equals(RemoteClientConstants.LOGIN))
			{
				login(input, statement);
			} else if (command
					.equals(RemoteClientConstants.REQUEST_LIST_DOC_ADD))
			{
				sendAllDocAdd(input, statement);
			} else if (command
					.equals(RemoteClientConstants.REQUEST_LIST_DOC_ID))
			{
				sendAllDocID(input, statement);
			} else if (command.equals(RemoteClientConstants.REQUEST_CHECKUPS))
			{
				sendAllCheckups(input, statement);
			} else if (command.equals(RemoteClientConstants.SAVE_DIST))
			{
				saveDist(input, statement);
			} else if (command
					.equals(RemoteClientConstants.REQUEST_CHECKUPS_DOCTOR))
			{
				sendAllCheckupsDoctorID(input, statement);
			} else if (command.equals(RemoteClientConstants.REQUEST_DISTS))
			{
				sendAllDists(input, statement);
			} else if (command.equals(RemoteClientConstants.REQUEST_MED_SUG))
			{
				sendMedSug(input, statement);
			} else if (command.equals(RemoteClientConstants.REQUEST_MED_HIST))
			{

			}

			statement.close();
			connection.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * The method handles login command. Check if the DB contains ID requested,
	 * and if so, check password. Finally send the result back to application.
	 */
	public void login(Message input, Statement statement)
	{
		HashMap<String, Object> response = new HashMap<String, Object>();
		Message m = new Message("Server", null, response);
		HashMap<String, Object> data = input.getMap();
		try
		{
			System.out.println("ID: "
					+ (String) data.get(RemoteClientConstants.LOGIN_ID));
			ArrayList<HashMap<String, Object>> db = md.getAllDataString(
					"users", RemoteClientConstants.LOGIN_ID,
					(String) data.get(RemoteClientConstants.LOGIN_ID),
					statement);
			if (db.size() == 0)
			{
				m.setCommand(RemoteClientConstants.LOGIN_FAIL);
			} else
			{
				HashMap<String, Object> user = db.get(0);
				System.out.println("input: "
						+ (String) data.get(RemoteClientConstants.LOGIN_PW));
				System.out.println("db: "
						+ (String) user.get(RemoteClientConstants.LOGIN_PW));
				System.out.println("id: " + user.get("id"));
				String pwd = (String) user.get(RemoteClientConstants.LOGIN_PW);
				if (pwd.equals((String) data
						.get(RemoteClientConstants.LOGIN_PW)))
				{
					response.put(RemoteClientConstants.LOGIN_ID,
							(String) data.get(RemoteClientConstants.LOGIN_ID));
					response.put(RemoteClientConstants.LOGIN_SUCCESS,
							user.get("job"));
					response.put(RemoteClientConstants.LOGIN, user.get("id"));
					m.setCommand(RemoteClientConstants.LOGIN_SUCCESS);
				} else
				{
					m.setCommand(RemoteClientConstants.LOGIN_FAIL);
				}
			}
			sendOutput(m);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

	}

	/**	 */
	public void register(Message input, Statement statement)
	{
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> data = input.getMap();
		int count = -1;
		try
		{
			count = md.countDataString("users", "user_id",
					(String) data.get("user_id"), statement);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		if (count == 0)
		{
			StringBuilder col = new StringBuilder();
			StringBuilder value = new StringBuilder();
			Iterator<Entry<String, Object>> it = data.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry<String, Object> pairs = it.next();
				System.out.println(pairs.getKey() + " = " + pairs.getValue());

				String key = pairs.getKey();
				Object dataValue = pairs.getValue();

				if (it.hasNext() && dataValue instanceof String)
				{
					col.append(key + ",");
					value.append("\"" + dataValue + "\",");
				} else if (it.hasNext())
				{
					col.append(key + ",");
					value.append(dataValue + ",");
				} else if (dataValue instanceof String)
				{
					col.append(key);
					value.append("\"" + dataValue + "\"");
				} else
				{
					col.append(key);
					value.append(dataValue);
				}

				it.remove(); // avoids a ConcurrentModificationException
			}

			try
			{
				md.insertData("users", col.toString(), value.toString(),
						statement);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			response.put(RemoteClientConstants.REGISTER_SUCCESS,
					(String) data.get("user_id"));
			Message output = new Message("Server",
					RemoteClientConstants.REGISTER_SUCCESS, response);
			sendOutput(output);
		} else
		{
			Message output = new Message("Server",
					RemoteClientConstants.REGISTER_FAIL, response);
			sendOutput(output);
		}
	}

	public void sendAllDocAdd(Message input, Statement statement)
	{
		// Map will contain <doc_name, doc_add>
		HashMap<String, Object> response = new HashMap<String, Object>();

		try
		{
			// Pull list of all doctors
			ArrayList<HashMap<String, Object>> db = md.getAllDataString(
					"users", RemoteClientConstants.REGISTER_INFO_JOB,
					RemoteClientConstants.REGISTER_JOB_DOCTOR, statement);

			// add userID and address of all doctor.
			for (HashMap<String, Object> map : db)
			{
				String doc_id = (String) map
						.get(RemoteClientConstants.REGISTER_INFO_ID);
				String doc_add = (String) map
						.get(RemoteClientConstants.REGISTER_INFO_ADDRESS);
				response.put(doc_id, doc_add);
			}
			Message m = new Message("Server",
					RemoteClientConstants.REQUEST_LIST_DOC_ADD, response);
			sendOutput(m);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void sendAllDocID(Message input, Statement statement)
	{
		// Map will contain <doc_name, doc_id>
		HashMap<String, Object> response = new HashMap<String, Object>();

		try
		{
			// Pull list of all doctors
			ArrayList<HashMap<String, Object>> db = md.getAllDataString(
					"users", RemoteClientConstants.REGISTER_INFO_JOB,
					RemoteClientConstants.REGISTER_JOB_DOCTOR, statement);

			// add userID and address of all doctor.
			for (HashMap<String, Object> map : db)
			{
				String docUserId = (String) map
						.get(RemoteClientConstants.REGISTER_INFO_ID);
				response.put(docUserId, "");
			}
			Message m = new Message("Server",
					RemoteClientConstants.REQUEST_LIST_DOC_ID, response);
			sendOutput(m);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void sendAllCheckups(Message input, Statement statement)
	{
		// Map will contain <REQUEST_LIST_DOC_ID, ArrayList of checkup HashMaps>
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> data = input.getMap();
		try
		{
			// Pull list of all checkups for certain user
			ArrayList<HashMap<String, Object>> db = md
					.getAllDataString(
							"checkups",
							RemoteClientConstants.CHECKUP_PATIENT_ID,
							(String) data
									.get(RemoteClientConstants.CHECKUP_PATIENT_ID),
							statement);

			response.put(RemoteClientConstants.REQUEST_CHECKUPS, db);
			Message m = new Message("Server",
					RemoteClientConstants.REQUEST_CHECKUPS, response);
			sendOutput(m);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void sendAllCheckupsDoctorID(Message input, Statement statement)
	{
		// Map will contain <REQUEST_LIST_DOC_ID, ArrayList of checkup HashMaps>
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> data = input.getMap();
		try
		{
			// Pull list of all checkups for certain user
			ArrayList<HashMap<String, Object>> db = md.getAllDataString(
					"checkups", RemoteClientConstants.CHECKUP_DOCTOR_ID,
					(String) data.get(RemoteClientConstants.CHECKUP_DOCTOR_ID),
					statement);

			response.put(RemoteClientConstants.REQUEST_CHECKUPS_DOCTOR, db);
			Message m = new Message("Server",
					RemoteClientConstants.REQUEST_CHECKUPS_DOCTOR, response);
			sendOutput(m);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void sendMedSug(Message input, Statement statement)
	{
		// Get checkUpID for search.
		String checkUpID;
		ArrayList<HashMap<String, Object>> db;
		HashMap<String, Object> response;
		ArrayList<String> medSugList;

		checkUpID = (String) input.getMap().get(
				RemoteClientConstants.CHECKUP_ID);

		try
		{
			// ArrayList of examination_relationship table row.
			db = md.getAllDataString(RemoteClientConstants.TABLE_EXAMINATION,
					RemoteClientConstants.EXAMINATION_CHECKUP_ID, checkUpID,
					statement);

			medSugList = new ArrayList<String>();
			for (HashMap<String, Object> row : db)
			{
				medSugList.add((String) row
						.get(RemoteClientConstants.EXAMINATION_MED_ID));
			}
			// put arraylist of suggested medicine to map
			response = new HashMap<String, Object>();
			response.put(RemoteClientConstants.REQUEST_MED_SUG, medSugList);

			// attach the map
			Message msg = new Message("Server",
					RemoteClientConstants.REQUEST_MED_SUG, response);
			sendOutput(msg);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMedHist(Message input, Statement statement)
	{
		// Get checkUpID for search.
		String checkUpID;
		// DB query result
		ArrayList<HashMap<String, Object>> db;
		// Reponse map to client.
		HashMap<String, Object> response;

		checkUpID = (String) input.getMap().get(
				RemoteClientConstants.CHECKUP_ID);

		try
		{
			// ArrayList of examination_relationship table row.
			db = md.getAllDataString(RemoteClientConstants.TABLE_TAKEN,
					RemoteClientConstants.TAKEN_CHECKUP_ID, checkUpID,
					statement);

			// put arraylist of suggested medicine to map
			response = new HashMap<String, Object>();
			response.put(RemoteClientConstants.REQUEST_MED_HIST, db);

			// attach the map
			Message msg = new Message("Server",
					RemoteClientConstants.REQUEST_MED_HIST, response);
			sendOutput(msg);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveDist(Message input, Statement statement)
	{
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> data = input.getMap();

		byte[] imageByte = (byte[]) data
				.get(RemoteClientConstants.DIST_PIC_FILE);
		String imageLoc = (String) data.get(RemoteClientConstants.DIST_PIC_LOC);
		File imageFile = new File(imageLoc);
		File parent = imageFile.getParentFile();
		if (!parent.exists())
			parent.mkdirs();
		convertByteToFile(imageLoc, imageByte);

		byte[] voiceByte = (byte[]) data
				.get(RemoteClientConstants.DIST_VOC_FILE);
		String voiceLoc = (String) data.get(RemoteClientConstants.DIST_VOC_LOC);
		File voiceFile = new File(voiceLoc);
		parent = voiceFile.getParentFile();
		if (!parent.exists())
			parent.mkdirs();
		convertByteToFile(voiceLoc, voiceByte);

		StringBuilder col = new StringBuilder();
		StringBuilder value = new StringBuilder();
		int count = 0;
		Iterator<Entry<String, Object>> it = data.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<String, Object> pairs = it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());

			String key = pairs.getKey();
			Object dataValue = pairs.getValue();

			if (!(key.equals(RemoteClientConstants.DIST_PIC_FILE) || key
					.equals(RemoteClientConstants.DIST_VOC_FILE)))
			{
				if (count < 5 && dataValue instanceof String)
				{
					col.append(key + ",");
					value.append("\"" + dataValue + "\",");
					count++;
				} else if (count < 5)
				{
					col.append(key + ",");
					value.append(dataValue + ",");
					count++;
				} else if (dataValue instanceof String)
				{
					col.append(key);
					value.append("\"" + dataValue + "\"");
					count++;
				} else
				{
					col.append(key);
					value.append(dataValue);
					count++;
				}
			}
			it.remove(); // avoids a ConcurrentModificationException
		}

		try
		{
			md.insertData("distantdiagnosis", col.toString(), value.toString(),
					statement);
		} catch (SQLException e)
		{
			e.printStackTrace();
			Message output = new Message("Server",
					RemoteClientConstants.SAVE_FAIL, response);
			sendOutput(output);
			return;
		}

		Message output = new Message("Server",
				RemoteClientConstants.SAVE_SUCCESS, response);
		sendOutput(output);
		return;
	}

	public void sendAllDists(Message input, Statement statement)
	{
		// Map will contain <REQUEST_LIST_DOC_ID, ArrayList of checkup HashMaps>
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> data = input.getMap();
		try
		{
			// Pull list of all distants for certain user
			ArrayList<HashMap<String, Object>> db = md.getAllDataString(
					"distantdiagnosis", RemoteClientConstants.DIST_DOC_ID,
					(String) data.get(RemoteClientConstants.DIST_DOC_ID),
					statement);

			for (int i = 0; i < db.size(); i++)
			{
				HashMap<String, Object> temp = db.get(i);
				String imageLoc = (String) temp
						.get(RemoteClientConstants.DIST_PIC_LOC);
				File imageFile = new File(imageLoc);
				byte[] imageByte = convertFileToByte(imageFile);
				temp.put(RemoteClientConstants.DIST_PIC_FILE, imageByte);
				String voiceLoc = (String) temp
						.get(RemoteClientConstants.DIST_VOC_LOC);
				File voiceFile = new File(voiceLoc);
				byte[] voiceByte = convertFileToByte(voiceFile);
				temp.put(RemoteClientConstants.DIST_VOC_FILE, voiceByte);
			}

			response.put(RemoteClientConstants.REQUEST_DISTS, db);
			Message m = new Message("Server",
					RemoteClientConstants.REQUEST_DISTS, response);
			sendOutput(m);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	private byte[] convertFileToByte(File file)
	{
		FileInputStream fileInputStream = null;

		byte[] bFile = new byte[(int) file.length()];

		try
		{
			// convert file into array of bytes
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return bFile;
	}

	private void convertByteToFile(String filename, byte[] b)
	{
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(filename);
			bos = new BufferedOutputStream(fos);
			bos.write(b);
		} catch (FileNotFoundException fnfe)
		{
			System.err.println("File not found" + fnfe);
			fnfe.printStackTrace();
		} catch (IOException e)
		{
			System.err.println("Error while writing to file" + e);
			e.printStackTrace();
		} finally
		{
			try
			{
				if (bos != null)
				{
					bos.flush();
					bos.close();
				}
			} catch (IOException e)
			{
				System.err.println("Error while closing streams" + e);
				e.printStackTrace();
			}

		}
	}

	public static void main(String arg[])
	{
		/* debug main; does daytime on local host */
		System.out.println("Server Started..");
		Server server = new Server("", 4444);
		server.start();
	}
}
