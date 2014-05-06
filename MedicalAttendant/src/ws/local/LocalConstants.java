package ws.local;

/**	
 * 	S14 18641
 *  Medical Attendant.
 * 	
 * 	@author Sang Rok Shin, Inho Yong
 **/

import android.os.Environment;

public class LocalConstants {
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String PATIENT = "Patient";
	public static final String DOCTOR = "Doctor";
	public static final String USER_ID = "user_id";
	public static final String JOB = "job";
	public static final int CAMERA_REQUEST = 100; 
	public static final int VOICE_REQUEST = 200; 
	public static final String VOC_FILE_LOC = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
	public static final String PIC_FILE_LOC = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
	public static final int MEDIA_TYPE_IMAGE = 1;
}
