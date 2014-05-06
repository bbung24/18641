package ws.remote;

/**	
 * 	S14 18641
 *  Medical Attendant.
 * 	
 * 	@author Sang Rok Shin, Inho Yong
 **/

import java.io.Serializable;
import java.util.HashMap;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2911766101045458292L;
	private String strMsg;
	private String command;
	private HashMap<String,Object> map;
	
	public Message(String strMsg, String command, HashMap<String, Object> map){
		this.strMsg = strMsg;
		this.command = command;
		this.map = map;
	}

	public String getStrMsg() {
		return strMsg;
	}

	public void setStrMsg(String strMsg) {
		this.strMsg = strMsg;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setMap(HashMap<String,Object> map)
	{
		this.map = map;
	}
	
	
	public HashMap<String,Object> getMap()
	{
		return this.map;
	}
}
