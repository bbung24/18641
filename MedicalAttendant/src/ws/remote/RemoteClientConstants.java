package ws.remote;

public class RemoteClientConstants {
	public final static String LOGIN = "Login";
	public final static String LOGIN_FAIL = "Login failed";
	public final static String LOGIN_SUCCESS = "Login success";
	public final static String LOGIN_ID = "user_id";
	public final static String LOGIN_PW = "pw";

	public final static String REGISTER = "Register";
	public final static String REGISTER_FAIL = "Register failed";
	public final static String REGISTER_SUCCESS = "Register success";

	public final static String REGISTER_INFO_ID = "user_id";
	public final static String REGISTER_INFO_PW = "pw";
	public final static String REGISTER_INFO_AGE = "age";
	public final static String REGISTER_INFO_ADDRESS = "address";
	public final static String REGISTER_INFO_JOB = "job";

	public final static String REGISTER_JOB_DOCTOR = "Doctor";
	public final static String REGISTER_JOB_PATIENT = "Patient";

	public final static String BROADCAST_ACTION = "com.BROADCAST";
	public final static String BROADCAST_RECEV = "received";
	public final static String INTERNAL_FAIL = "internal fail";

	public final static String EXAM_ID = "Exam_ID";
	public final static String EXAM_NAME = "Exam_Name";

	// CHECK UP TABLE COLUMNS

	public static final String CHECK_UP_ROW = "Entire row for CHECKUP_ID";

	public static final String TABLE_CHECKUP = "checkups";
	public static final String CHECKUP_ID = "id";
	public static final String CHECKUP_PATIENT_ID = "patient_id";
	public static final String CHECKUP_DATE = "date";
	public static final String CHECKUP_RESULT = "result";
	public static final String CHECKUP_DOCTOR_ID = "doctor_id";

	// examination_relationship table
	public static final String TABLE_EXAMINATION = "examination_relationpship";
	public static final String EXAMINATION_CHECKUP_ID = "check_up_id";
	public static final String EXAMINATION_MED_ID = "medication_id";

	// taken_relatipnship table
	public static final String TABLE_TAKEN = "taken_relatinpship";
	public static final String TAKEN_CHECKUP_ID = "check_up_id";
	public static final String TAKEN_MED_ID = "medication_id";
	public static final String TAKEN_DATE = "date";

	// DIST TABLE COLUMNS
	public static final String DIST_SYMPTOM = "symptom";
	public static final String DIST_PIC_LOC = "pic_loc";
	public static final String DIST_VOC_LOC = "voc_loc";
	public static final String DIST_DOC_ID = "doctor_id";
	public static final String DIST_DATE = "date";
	public static final String DIST_PATIENT_ID = "patient_id";

	public static final String DIST_PIC_FILE = "pic file";
	public static final String DIST_VOC_FILE = "voc file";

	// MEDICATION TABLE
	public static final String TABLE_MED = "medications";
	public static final String MED_ID = "id";
	public static final String MED_NAME = "name";

	public static final String SAVE_DIST = "Save Distant Diagnosis";
	public static final String SAVE_CHECKUPS = "Save Check ups";
	public static final String SAVE_TAKEN = "Save taken medication";
	public static final String SAVE_SUCCESS = "save successful";
	public static final String SAVE_FAIL = "save failed";
	
	// REQUEST DEFINITIONS
	public final static String REQUEST_LIST_DOC_ADD = "Request all doctor's address";
	public final static String REQUEST_LIST_EXAM = "Request examination records";
	public static final String REQUEST_CREATE_CHECKUP = "Reqest create checkup";
	public static final String REQUEST_MED_LIST = "Request med_list";
	public static final String REQUEST_LIST_DOC_ID = "Request all doctor's ids";
	public static final String REQUEST_CHECKUPS = "Request patient's checkups";
	public static final String REQUEST_DISTS = "Request doctor's dists";
	public static final String REQUEST_CHECKUPS_DOCTOR = "Request check ups for doctors";
	public static final String REQUEST_LIST_PATIENT = "Request patients list";
	public static final String REQUEST_MED_SUG = "Request suggested medicine";
	public static final String REQUEST_MED_HIST = "Request history of dosage";

	
	//ETC
	public static final String MED_CHECKUP_SELECTED = "medines selected in create checkup";
	
}
