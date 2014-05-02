package ws.remote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2911766101045458292L;
	private String strMsg;
	private String command;
	private HashMap<String,String> map;
	
	public Message(String strMsg, String command, HashMap<String, String> map){
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

	public void setMap(HashMap<String,String> map)
	{
		this.map = map;
	}
	
	
	public HashMap<String,String> getMap()
	{
		return this.map;
	}
}
