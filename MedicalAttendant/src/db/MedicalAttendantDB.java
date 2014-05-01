package db;

import java.util.ArrayList;

import model.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MedicalAttendantDB extends SQLiteOpenHelper
{
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Datbase Name
	private static final String DATABASE_NAME = "ddcu_db";
	// Table names
	private static final String TABLE_USER = "user";
	private static final String TABLE_DD = "distant_diagnosis";
	
	// Table column
	private static final String USER_KEY_ID = "id";
	private static final String USER_KEY_PW = "pw";
	private static final String USER_KEY_AGE = "age";
	private static final String USER_KEY_JOB = "job";
	private static final String USER_KEY_ZIP = "zipcode";

	private static final String DD_KEY_ID = "id";
	private static final String DD_KEY_SYMPTOM = "symptom";
	private static final String DD_PIC_LOC = "pic_loc";
	private static final String DD_VOC_LOC = "voc_loc";
	
	
	
	// Columns
	private static final String[] USER_COLUMNS =
	{ USER_KEY_ID, USER_KEY_PW, USER_KEY_AGE, USER_KEY_JOB, USER_KEY_ZIP };
	
	private static final String[] DD_COLUMNS = {
		DD_KEY_ID, DD_KEY_SYMPTOM, DD_PIC_LOC, DD_VOC_LOC};
	
	
	// Strings
	private static final String PATIENT = "patient";
	private static final String DOCTOR = "doctor";

	public MedicalAttendantDB(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ USER_KEY_ID + " TEXT," 
				+ USER_KEY_PW + " TEXT,"
				+ USER_KEY_AGE + " INT," 
				+ USER_KEY_JOB + " TEXT,"
				+ USER_KEY_ZIP + " INT" 
				+ ")";
		db.execSQL(CREATE_USER_TABLE);

		String CREATE_DD_TABLE = "CREATE TABLE " + TABLE_DD + "("
				+DD_KEY_ID + " TEXT,"
				+DD_KEY_SYMPTOM + " TEXT,"
				+DD_PIC_LOC + " TEXT,"
				+DD_VOC_LOC + " TEXT"
				+")";
		db.execSQL(CREATE_DD_TABLE);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.d(MedicalAttendantDB.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

		// Create tables again
		onCreate(db);
	}

	// CRUD Operation

	/**
	 * The method inserts a user into the database.
	 * */
	public void addUser(User user)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(USER_KEY_ID, user.getID());
		values.put(USER_KEY_PW, user.getPW());
		values.put(USER_KEY_AGE, user.getAge());

		if (user.getIsDoctor())
			values.put(USER_KEY_JOB, DOCTOR);
		else
			values.put(USER_KEY_JOB, PATIENT);

		values.put(USER_KEY_ZIP, user.getZip());

		db.insert(TABLE_USER, null, values);
		db.close();
		Log.d(MedicalAttendantDB.class.getName(),
				"Added a user: " + user.getID());

	}

	public ArrayList<User> getAllDoctor()
	{
		ArrayList<User> doctors = new ArrayList<User>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USER, USER_COLUMNS, USER_KEY_JOB + "=?",
				new String[]
				{ DOCTOR }, null, null, null, null);

		if (cursor.moveToFirst())
		{
			do
			{
				String id = cursor.getString(0);
				String pw = cursor.getString(1);
				int age = Integer.parseInt(cursor.getString(2));
				String job = cursor.getString(3);

				int zip = Integer.parseInt(cursor.getString(4));

				doctors.add(new User(id, pw, age, zip, (job.equals(DOCTOR))));
			} while (cursor.moveToNext());
		}
		return doctors;
	}

	public User getUser(String id, String pw)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USER, USER_COLUMNS, USER_KEY_ID
				+ "=? and " + USER_KEY_PW + "=?", new String[]
		{ id, pw }, null, null, null, null);

		if (cursor != null)
			return new User(cursor.getString(0), cursor.getString(1),
					Integer.parseInt(cursor.getString(2)),
					Integer.parseInt(cursor.getString(3)), cursor.getString(4)
							.equals(DOCTOR));
		else
			return null;
	}
}
