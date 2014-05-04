package ws.remote;

public class RemoteClientConstants {
	public final static String LOGIN = "Login";
	public final static String LOGIN_FAIL = "Login failed";
	public final static String LOGIN_SUCCESS = "Login success";
	public final static String LOGIN_ID ="user_id";
	public final static String LOGIN_PW = "pw";
	
	public final static String REGISTER = "Register";
	public final static String REGISTER_FAIL = "Register failed";
	public final static String REGISTER_SUCCESS = "Register success";
	
	public final static String REGISTSER_INFO_ID = "user_id";
	public final static String REGISTSER_INFO_PW = "pw";
	public final static String REGISTSER_INFO_AGE = "age";
	public final static String REGISTSER_INFO_ADDRESS = "address";
	public final static String REGISTSER_INFO_JOB = "job";
	
	public final static String REGISTER_JOB_DOCTOR = "Doctor";
	public final static String REGISTER_JOB_PATIENT = "Patient";
	
	
	
	public final static String BROADCAST_ACTION = "com.BROADCAST";
	public final static String BROADCAST_RECEV = "received";
	public final static String INTERNAL_FAIL = "internal fail";
	
	public final static String EXAM_ID = "Exam_ID";
	public final static String EXAM_NAME = "Exam_Name";
	
	
	//CHECK UP TABLE COLUMNS
	public static final String CHECKUP_RESULT = "result";
	public static final String CHECKUP_MED_LIST = "med_list";
	
	//REQUEST DEFINITIONS
	public final static String REQUEST_LIST_ALLDOC = "Request all doctor";
	public final static String REQUEST_LIST_EXAM = "Request examination records";
	public static final String REQUEST_CREATE_CHECKUP = "Reqest create checkup";
	public static final String REQUEST_MED_LIST = "Request med_list";

	
}
