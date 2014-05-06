package ws.remote;

/**	
 * 	S14 18641
 *  Medical Attendant.
 * 	
 * 	@author Sang Rok Shin, Inho Yong
 **/

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

public class Server extends DefaultSocketClient {
	private DBController md;

	public Server(String strHost, int iPort) {
		super(strHost, iPort);

		String tableName;
		StringBuilder col;
		md = new DBController();
		Connection connection = md.getConnection();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			tableName = "users";

			col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("user_id VARCHAR(100),");
			col.append("pw VARCHAR(100),");
			col.append("age INT,");
			col.append("address VARCHAR(100),");
			col.append("job VARCHAR(100)");

			md.createTable(tableName, col.toString(), stmt);
		} catch (SQLException ex) {
			System.out.println("SQLException:");
			while (ex != null) {
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("Message:  " + ex.getMessage());
				System.out.println("Vendor:   " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}
		try {
			tableName = "checkups";

			col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("patient_id VARCHAR(100),");
			col.append("date VARCHAR(100),");
			col.append("result VARCHAR(100),");
			col.append("doctor_id VARCHAR(100)");

			md.createTable(tableName, col.toString(), stmt);
		} catch (SQLException ex) {
			System.out.println("SQLException:");
			while (ex != null) {
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("Message:  " + ex.getMessage());
				System.out.println("Vendor:   " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}
		try {
			tableName = "distantdiagnosis";

			col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("patient_id VARCHAR(100),");
			col.append("doctor_id VARCHAR(100),");
			col.append("symptom VARCHAR(100),");
			col.append("pic_loc VARCHAR(100),");
			col.append("voc_loc VARCHAR(100),");
			col.append("date VARCHAR(100)");

			md.createTable(tableName, col.toString(), stmt);
		} catch (SQLException ex) {
			System.out.println("SQLException:");
			while (ex != null) {
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("Message:  " + ex.getMessage());
				System.out.println("Vendor:   " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}
		try {
			tableName = "medications";

			col = new StringBuilder();
			col.append("id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");
			col.append("name VARCHAR(100)");

			md.createTable(tableName, col.toString(), stmt);
		} catch (SQLException ex) {
			System.out.println("SQLException:");
			while (ex != null) {
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("Message:  " + ex.getMessage());
				System.out.println("Vendor:   " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}
		try {
			tableName = "examination_relationships";

			col = new StringBuilder();
			col.append("check_up_id INT,");
			col.append("medication_id INT");

			md.createTable(tableName, col.toString(), stmt);
		} catch (SQLException ex) {
			System.out.println("SQLException:");
			while (ex != null) {
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("Message:  " + ex.getMessage());
				System.out.println("Vendor:   " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}
		try {
			tableName = "taken_relationships";

			col = new StringBuilder();
			col.append("check_up_id INT,");
			col.append("medication_id INT,");
			col.append("date VARCHAR(100)");

			md.createTable(tableName, col.toString(), stmt);

			stmt.close();
			connection.close();
		} catch (SQLException ex) {
			System.out.println("SQLException:");
			while (ex != null) {
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
	public void handleInput(Message input) {
		Connection connection = md.getConnection();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();

			String cmd = input.getCommand();

			if (cmd.equals(RemoteClientConstants.REGISTER)) {
				register(input, stmt);
			} else if (cmd.equals(RemoteClientConstants.LOGIN)) {
				login(input, stmt);
			} else if (cmd.equals(RemoteClientConstants.REQUEST_LIST_DOC_ADD)) {
				sendAllDocAdd(input, stmt);
			} else if (cmd.equals(RemoteClientConstants.REQUEST_LIST_DOC_ID)) {
				sendAllDocID(input, stmt);
			} else if (cmd.equals(RemoteClientConstants.REQUEST_CHECKUPS)) {
				sendAllCheckups(input, stmt);
			} else if (cmd.equals(RemoteClientConstants.SAVE_DIST)) {
				saveDist(input, stmt);
			} else if (cmd.equals(RemoteClientConstants.REQUEST_CHECKUPS_DOC)) {
				sendAllCheckupsDoctorID(input, stmt);
			} else if (cmd.equals(RemoteClientConstants.REQUEST_DISTS)) {
				sendAllDists(input, stmt);
			} else if (cmd.equals(RemoteClientConstants.REQUEST_MED_SUG)) {
				sendMedSug(input, stmt);
			} else if (cmd.equals(RemoteClientConstants.REQUEST_MED_HIST)) {
				sendMedHist(input, stmt);
			} else if (cmd.equals(RemoteClientConstants.REQUEST_MED_LIST)) {
				sendMedList(input, stmt);
			} else if (cmd.equals(RemoteClientConstants.REQUEST_CREATE_CHECKUP)) {
				createCheckUp(input, stmt);
			} else if (cmd.equals(RemoteClientConstants.REQUEST_ADD_TAKEN)) {
				addMedTaken(input, stmt);
			}

			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The method handles login command. Check if the DB contains ID requested,
	 * and if so, check password. Finally send the result back to application.
	 */
	public void login(Message input, Statement stmt) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		Message m = new Message("Server", null, response);
		HashMap<String, Object> data = input.getMap();
		try {
			System.out.println("ID: "
					+ (String) data.get(RemoteClientConstants.LOGIN_ID));
			ArrayList<HashMap<String, Object>> db = md.getAllDataString(
					"users", RemoteClientConstants.LOGIN_ID,
					(String) data.get(RemoteClientConstants.LOGIN_ID), stmt);
			if (db.size() == 0) {
				m.setCommand(RemoteClientConstants.LOGIN_FAIL);
			} else {
				HashMap<String, Object> user = db.get(0);
				System.out.println("input: "
						+ (String) data.get(RemoteClientConstants.LOGIN_PW));
				System.out.println("db: "
						+ (String) user.get(RemoteClientConstants.LOGIN_PW));
				System.out.println("id: " + user.get("id"));
				String pwd = (String) user.get(RemoteClientConstants.LOGIN_PW);
				if (pwd.equals((String) data
						.get(RemoteClientConstants.LOGIN_PW))) {
					response.put(RemoteClientConstants.LOGIN_ID,
							(String) data.get(RemoteClientConstants.LOGIN_ID));
					response.put(RemoteClientConstants.LOGIN_SUCCESS,
							user.get("job"));
					response.put(RemoteClientConstants.LOGIN, user.get("id"));
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

	/**	 */
	public void register(Message input, Statement stmt) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> data = input.getMap();
		int count = -1;
		try {
			count = md.countDataString("users", "user_id",
					(String) data.get("user_id"), stmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (count == 0) {
			StringBuilder col = new StringBuilder();
			StringBuilder value = new StringBuilder();
			Iterator<Entry<String, Object>> it = data.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> pairs = it.next();
				System.out.println(pairs.getKey() + " = " + pairs.getValue());

				String key = pairs.getKey();
				Object dataValue = pairs.getValue();

				if (it.hasNext() && dataValue instanceof String) {
					col.append(key + ",");
					value.append("\"" + dataValue + "\",");
				} else if (it.hasNext()) {
					col.append(key + ",");
					value.append(dataValue + ",");
				} else if (dataValue instanceof String) {
					col.append(key);
					value.append("\"" + dataValue + "\"");
				} else {
					col.append(key);
					value.append(dataValue);
				}

				it.remove(); // avoids a ConcurrentModificationException
			}

			try {
				md.insertData("users", col.toString(), value.toString(), stmt);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			response.put(RemoteClientConstants.REGISTER_SUCCESS,
					(String) data.get("user_id"));
			Message output = new Message("Server",
					RemoteClientConstants.REGISTER_SUCCESS, response);
			sendOutput(output);
		} else {
			Message output = new Message("Server",
					RemoteClientConstants.REGISTER_FAIL, response);
			sendOutput(output);
		}
	}

	public void sendAllDocAdd(Message input, Statement stmt) {
		// Map will contain <doc_name, doc_add>
		HashMap<String, Object> response = new HashMap<String, Object>();

		try {
			// Pull list of all doctors
			ArrayList<HashMap<String, Object>> db = md.getAllDataString(
					"users", RemoteClientConstants.REGISTER_INFO_JOB,
					RemoteClientConstants.REGISTER_JOB_DOCTOR, stmt);

			// add userID and address of all doctor.
			for (HashMap<String, Object> map : db) {
				String doc_id = (String) map
						.get(RemoteClientConstants.REGISTER_INFO_ID);
				String doc_add = (String) map
						.get(RemoteClientConstants.REGISTER_INFO_ADDRESS);
				response.put(doc_id, doc_add);
			}
			Message m = new Message("Server",
					RemoteClientConstants.REQUEST_LIST_DOC_ADD, response);
			sendOutput(m);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void sendAllDocID(Message input, Statement stmt) {
		// Map will contain <doc_name, doc_id>
		HashMap<String, Object> response = new HashMap<String, Object>();

		try {
			// Pull list of all doctors
			ArrayList<HashMap<String, Object>> db = md.getAllDataString(
					"users", RemoteClientConstants.REGISTER_INFO_JOB,
					RemoteClientConstants.REGISTER_JOB_DOCTOR, stmt);

			// add userID and address of all doctor.
			for (HashMap<String, Object> map : db) {
				String docUserId = (String) map
						.get(RemoteClientConstants.REGISTER_INFO_ID);
				response.put(docUserId, "");
			}
			Message m = new Message("Server",
					RemoteClientConstants.REQUEST_LIST_DOC_ID, response);
			sendOutput(m);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void sendAllCheckups(Message input, Statement stmt) {
		// Map will contain <REQUEST_LIST_DOC_ID, ArrayList of checkup HashMaps>
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> data = input.getMap();
		try {
			// Pull list of all checkups for certain user
			ArrayList<HashMap<String, Object>> db = md
					.getAllDataString(
							"checkups",
							RemoteClientConstants.CHECKUP_PATIENT_ID,
							(String) data
									.get(RemoteClientConstants.CHECKUP_PATIENT_ID),
							stmt);

			response.put(RemoteClientConstants.REQUEST_CHECKUPS, db);
			Message m = new Message("Server",
					RemoteClientConstants.REQUEST_CHECKUPS, response);
			sendOutput(m);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void sendAllCheckupsDoctorID(Message input, Statement stmt) {
		// Map will contain <REQUEST_LIST_DOC_ID, ArrayList of checkup HashMaps>
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> data = input.getMap();
		try {
			// Pull list of all checkups for certain user
			ArrayList<HashMap<String, Object>> db = md.getAllDataString(
					"checkups", RemoteClientConstants.CHECKUP_DOCTOR_ID,
					(String) data.get(RemoteClientConstants.CHECKUP_DOCTOR_ID),
					stmt);

			response.put(RemoteClientConstants.REQUEST_CHECKUPS_DOC, db);
			Message m = new Message("Server",
					RemoteClientConstants.REQUEST_CHECKUPS_DOC, response);
			sendOutput(m);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void sendMedSug(Message input, Statement stmt) {
		// Get checkUpID for search.
		Integer checkUpID;
		ArrayList<HashMap<String, Object>> examDB, checkUpDB;
		HashMap<String, Object> checkUpRow;
		HashMap<String, Object> response;
		ArrayList<Integer> medSugIDList;
		ArrayList<HashMap<String, Object>> medDB;
		String result = "";

		checkUpID = (Integer) input.getMap().get(
				RemoteClientConstants.CHECKUP_ID);

		try {
			// ArrayList of examination_relationship table row.
			examDB = md.getAllDataString(
					RemoteClientConstants.TABLE_EXAMINATION,
					RemoteClientConstants.EXAMINATION_CHECKUP_ID,
					String.valueOf(checkUpID), stmt);

			checkUpDB = md.getAllDataString(
					RemoteClientConstants.TABLE_CHECKUP,
					RemoteClientConstants.CHECKUP_ID,
					String.valueOf(checkUpID), stmt);

			// Find a row and its result for the check.
			for (HashMap<String, Object> row : checkUpDB) {
				if (row.get(RemoteClientConstants.CHECKUP_ID).equals(checkUpID))
					result = (String) row
							.get(RemoteClientConstants.CHECKUP_RESULT);
			}

			medDB = md.getTable(RemoteClientConstants.TABLE_MED, stmt);
			medSugIDList = new ArrayList<Integer>();
			for (HashMap<String, Object> row : examDB) {

				Integer i = ((Integer) row
						.get(RemoteClientConstants.EXAMINATION_MED_ID));
				medSugIDList.add(i);
			}
			
			System.out.println(result);
			System.out.println(medSugIDList);
			
			// put arraylist of suggested medicine to map
			response = new HashMap<String, Object>();

			response.put(RemoteClientConstants.CHECKUP_RESULT, result);
			response.put(RemoteClientConstants.REQUEST_MED_SUG, medSugIDList);

			// Map of Medicine ID and name
			response.put(RemoteClientConstants.TABLE_MED, medDB);
			// attach the map
			Message msg = new Message("Server",
					RemoteClientConstants.REQUEST_MED_SUG, response);
			sendOutput(msg);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMedHist(Message input, Statement stmt) {
		// Get checkUpID for search.
		Integer checkUpID;
		// DB query result
		ArrayList<HashMap<String, Object>> db;

		ArrayList<HashMap<String, Object>> medDB;
		// Reponse map to client.
		HashMap<String, Object> response;

		checkUpID = (Integer) input.getMap().get(
				RemoteClientConstants.CHECKUP_ID);

		try {
			// ArrayList of examination_relationship table row.
			db = md.getAllDataString(RemoteClientConstants.TABLE_TAKEN,
					RemoteClientConstants.TAKEN_CHECKUP_ID,
					String.valueOf(checkUpID), stmt);
			medDB = md.getTable(RemoteClientConstants.TABLE_MED, stmt);

			System.out.println(db);
			System.out.println(medDB);
			// put arraylist of suggested medicine to map
			response = new HashMap<String, Object>();
			response.put(RemoteClientConstants.REQUEST_MED_HIST, db);
			response.put(RemoteClientConstants.TABLE_MED, medDB);
			// attach the map
			Message msg = new Message("Server",
					RemoteClientConstants.REQUEST_MED_HIST, response);
			sendOutput(msg);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMedList(Message input, Statement stmt) {

		HashMap<String, Object> response = new HashMap<String, Object>();

		try {
			ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
			result = md.getTable(RemoteClientConstants.TABLE_MED, stmt);
			response.put(RemoteClientConstants.REQUEST_MED_LIST, result);
			Message msg = new Message("Server",
					RemoteClientConstants.REQUEST_MED_LIST, response);
			sendOutput(msg);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveDist(Message input, Statement stmt) {
		System.out.println("Save Dist");
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
		while (it.hasNext()) {
			Map.Entry<String, Object> pairs = it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());

			String key = pairs.getKey();
			Object dataValue = pairs.getValue();

			if (!(key.equals(RemoteClientConstants.DIST_PIC_FILE) || key
					.equals(RemoteClientConstants.DIST_VOC_FILE))) {
				if (count < 5 && dataValue instanceof String) {
					col.append(key + ",");
					value.append("\"" + dataValue + "\",");
					count++;
				} else if (count < 5) {
					col.append(key + ",");
					value.append(dataValue + ",");
					count++;
				} else if (dataValue instanceof String) {
					col.append(key);
					value.append("\"" + dataValue + "\"");
					count++;
				} else {
					col.append(key);
					value.append(dataValue);
					count++;
				}
			}
			it.remove(); // avoids a ConcurrentModificationException
		}

		try {
			md.insertData("distantdiagnosis", col.toString(), value.toString(),
					stmt);
		} catch (SQLException e) {
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

	public void sendAllDists(Message input, Statement stmt) {
		// Map will contain <REQUEST_LIST_DOC_ID, ArrayList of checkup HashMaps>
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> data = input.getMap();
		try {
			// Pull list of all distants for certain user
			ArrayList<HashMap<String, Object>> db = md.getAllDataString(
					"distantdiagnosis", RemoteClientConstants.DIST_DOC_ID,
					(String) data.get(RemoteClientConstants.DIST_DOC_ID), stmt);

			for (int i = 0; i < db.size(); i++) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void createCheckUp(Message input, Statement stmt) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> data = input.getMap();
		// auto increment col-> set garbage val.
		data.put(RemoteClientConstants.CHECKUP_ID, 0);
		Integer checkUpID = -1;

		String docID = (String) data
				.get(RemoteClientConstants.CHECKUP_DOCTOR_ID);

		String date = (String) data.get(RemoteClientConstants.CHECKUP_DATE);

		StringBuilder col = new StringBuilder();
		StringBuilder value = new StringBuilder();
		int count = 0;
		Iterator<Entry<String, Object>> it = data.entrySet().iterator();

		// Insert patient_id, result, date and doctor id to checkups table
		while (it.hasNext()) {
			Map.Entry<String, Object> pairs = it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());

			String key = pairs.getKey();
			Object dataValue = pairs.getValue();
			if (!key.equals(RemoteClientConstants.MED_CHECKUP_SELECTED)) {
				if (count < 4 && dataValue instanceof String) {
					col.append(key + ",");
					value.append("\"" + dataValue + "\",");
					count++;
				} else if (count < 4) {
					col.append(key + ",");
					value.append(dataValue + ",");
					count++;
				} else if (dataValue instanceof String) {
					col.append(key);
					value.append("\"" + dataValue + "\"");
					count++;
				} else {
					col.append(key);
					value.append(dataValue);
					count++;
				}
				it.remove(); // avoids a ConcurrentModificationException
			}
		}

		try {
			md.insertData(RemoteClientConstants.TABLE_CHECKUP, col.toString(),
					value.toString(), stmt);

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Insert selected medicine to examination table.
		try {
			// Get the entry we have just passed in.
			// We need id of the checkUp for examinatin table.
			// combination of timestamp and doctorID is unique,
			// b/c one cannot enter multiple entries simultaneously.
			ArrayList<HashMap<String, Object>> entry = md.getAllDataString(
					RemoteClientConstants.TABLE_CHECKUP,
					RemoteClientConstants.CHECKUP_DATE, date, stmt);

			for (HashMap<String, Object> map : entry) {
				if (map.get(RemoteClientConstants.CHECKUP_DOCTOR_ID).equals(
						docID))
					checkUpID = (Integer) map
							.get(RemoteClientConstants.CHECKUP_ID);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		ArrayList<HashMap<String, Integer>> examRows = new ArrayList<HashMap<String, Integer>>();

		ArrayList<Integer> medSelIDs = (ArrayList<Integer>) data
				.get(RemoteClientConstants.MED_CHECKUP_SELECTED);
		for (Integer medID : medSelIDs) {
			HashMap<String, Integer> row = new HashMap<String, Integer>();
			row.put(RemoteClientConstants.EXAMINATION_CHECKUP_ID, checkUpID);
			row.put(RemoteClientConstants.EXAMINATION_MED_ID, medID);
			examRows.add(row);
		}

		for (HashMap<String, Integer> m : examRows) {

			StringBuilder examCol = new StringBuilder();
			StringBuilder examVal = new StringBuilder();
			Iterator<Entry<String, Integer>> examIt = m.entrySet().iterator();

			while (examIt.hasNext()) {
				Map.Entry<String, Integer> pairs = examIt.next();
				System.out.println(pairs.getKey() + " = " + pairs.getValue());

				String key = pairs.getKey();
				Object dataValue = pairs.getValue();

				if (examIt.hasNext() && dataValue instanceof String) {
					examCol.append(key + ",");
					examVal.append("\"" + dataValue + "\",");
				} else if (examIt.hasNext()) {
					examCol.append(key + ",");
					examVal.append(dataValue + ",");
				} else if (dataValue instanceof String) {
					examCol.append(key);
					examVal.append("\"" + dataValue + "\"");
				} else {
					examCol.append(key);
					examVal.append(dataValue);
				}

				examIt.remove(); // avoids a ConcurrentModificationException
			}

			try {
				md.insertData(RemoteClientConstants.TABLE_EXAMINATION,
						examCol.toString(), examVal.toString(), stmt);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Message output = new Message("Server",
				RemoteClientConstants.REQUEST_CREATE_CHECKUP, response);
		sendOutput(output);

	}

	@SuppressWarnings("unchecked")
	public void addMedTaken(Message input, Statement stmt) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		HashMap<String, Object> data = input.getMap();
		ArrayList<HashMap<String, Object>> takenRows = new ArrayList<HashMap<String,Object>>();
		
		takenRows = (ArrayList<HashMap<String, Object>>) data
				.get(RemoteClientConstants.TABLE_TAKEN);
		
		System.out.println(takenRows.size());
		
		for (HashMap<String, Object> row : takenRows) {
			StringBuilder col = new StringBuilder();
			StringBuilder value = new StringBuilder();
			Iterator<Entry<String, Object>> it = row.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> pairs = it.next();
				System.out.println(pairs.getKey() + " = " + pairs.getValue());

				String key = pairs.getKey();
				Object dataValue = pairs.getValue();

				if (it.hasNext() && dataValue instanceof String) {
					col.append(key + ",");
					value.append("\"" + dataValue + "\",");
				} else if (it.hasNext()) {
					col.append(key + ",");
					value.append(dataValue + ",");
				} else if (dataValue instanceof String) {
					col.append(key);
					value.append("\"" + dataValue + "\"");
				} else {
					col.append(key);
					value.append(dataValue);
				}

				it.remove(); // avoids a ConcurrentModificationException
			}

			try {
				md.insertData(RemoteClientConstants.TABLE_TAKEN,
						col.toString(), value.toString(), stmt);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Message output = new Message("Server",
				RemoteClientConstants.REQUEST_ADD_TAKEN, response);
		sendOutput(output);
	}

	private byte[] convertFileToByte(File file) {
		FileInputStream fileInputStream = null;

		byte[] bFile = new byte[(int) file.length()];

		try {
			// convert file into array of bytes
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bFile;
	}

	private void convertByteToFile(String filename, byte[] b) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filename);
			bos = new BufferedOutputStream(fos);
			bos.write(b);
		} catch (FileNotFoundException fnfe) {
			System.err.println("File not found" + fnfe);
			fnfe.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error while writing to file" + e);
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.flush();
					bos.close();
				}
			} catch (IOException e) {
				System.err.println("Error while closing streams" + e);
				e.printStackTrace();
			}

		}
	}

	public static void main(String arg[]) {
		/* debug main; does daytime on local host */
		System.out.println("Server Started..");
		Server server = new Server("", 4444);
		server.start();
	}
}
