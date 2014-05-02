package ws.remote;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2911766101045458292L;
	private String strMsg;
	private String command;
	private ArrayList<String> list;
	
	public Message(String strMsg, String command, ArrayList<String> list){
		this.strMsg = strMsg;
		this.command = command;
		this.list = list;
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

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
	}
}
