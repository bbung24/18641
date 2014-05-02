package handleConnections;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DefaultSocketClient extends Thread implements SocketClientInterface, SocketClientConstants{
	protected ObjectInputStream reader;
	protected ObjectOutputStream writer;
	
	protected Socket sock;
	protected String strHost;
	protected int iPort;
	
	protected ServerSocket serverSocket = null;
	
	public DefaultSocketClient(String strHost, int iPort) {       
		setPort (iPort);
		setHost (strHost);
	}//constructor

	public void run(){
		if (openConnection()){
			handleSession();
		}
	}//run

	public boolean openConnection(){
		try {
			serverSocket = new ServerSocket(iPort);
		} catch (IOException e) {
			if (DEBUG) System.err.println
			("Could not listen on port: " + iPort);
			return false;
		}

		return listen();
	}
	
	public boolean listen() {
		Socket sock = null;
		try {
			sock = serverSocket.accept();
		} catch (IOException e) {
			if (DEBUG) System.err.println
			("Unable to connect to " + strHost);
			return false;
		}
		
		try {
			reader = new ObjectInputStream(sock.getInputStream());
			writer = new ObjectOutputStream(sock.getOutputStream());
		}
		catch (Exception e){
			if (DEBUG) System.err.println
			("Unable to obtain stream to/from " + strHost);
			return false;
		}
		
		if (DEBUG) {
			System.out.println("Client Connected");
		}
		
		return true;
	}

	public void handleSession(){
		Object strInput;
		if (DEBUG) System.out.println ("Handling session with "
				+ strHost + ":" + iPort);
		
		try {
			while ( (strInput = reader.readObject()) != null)
				handleInput((Message)strInput);
		} catch (EOFException e) {
			if(DEBUG) {
				System.out.println("Client Disconnected");
				if(listen()){
					handleSession();
				}
			}
		} catch (IOException e){
			if (DEBUG) {
				System.err.println ("Handling session with "
					+ strHost + ":" + iPort);
				System.err.println(e.toString());
			}
		} catch (ClassNotFoundException e) {
			if (DEBUG) {
				System.err.println("Handling session with "
					+ strHost + ":" + iPort);
				System.err.println(e.toString());
			}
		}
	}       

	public void sendOutput(Message output){
		try {
			writer.writeObject(output);
		}
		catch (IOException e){
			if (DEBUG) System.err.println 
			("Error writing to " + strHost);
		}
	}
	
	// This handler will have to do two tasks.
	// One is that of saving data that was sent from android app.
	// Another is sending data when needed by android app.
	public void handleInput(Message input){
		// TODO: Need to think of communication between 
		// receiving data and sending data.
		// Also, in what steps will it be done.
		Message output = new Message(null, null, null);
		
		
//		if(input.getCommand().equalsIgnoreCase("BUILDAUTO")){
//			if (DEBUG) System.out.println("BUILDAUTO");
//			Automobile a = input.getA();
//			if(a == null) {
//				if (DEBUG) System.out.println
//				("Automobile received is null");
//			}
//			bcmo.buildAuto(a);
//			output.setCommand("BUILDAUTO");
//			output.setStrMsg("1");
//			sendOutput(output);
//			if (DEBUG) System.out.println("BUILDAUTO - DONE");
//		} else if(input.getCommand().equalsIgnoreCase("displayAutomobile")) {
//			if (DEBUG) System.out.println("displayAutomobile");
//			output.setCommand("displayAutomobile");
//			output.setList(bcmo.getListModels());
//			sendOutput(output);
//			if (DEBUG) System.out.println("displayAutomobile - DONE");
//		} else if(input.getCommand().equalsIgnoreCase("getAuto")) {
//			if (DEBUG) System.out.println("getAuto");
//			output.setCommand("getAuto");
//			output.setA(bcmo.displayAutomobile(input.getStrMsg()));
//			sendOutput(output);
//			if (DEBUG) System.out.println("getAuto - DONE");
//		}
	}       

	public void closeSession(){
		try {
			writer = null;
			reader = null;
			sock.close();
		}
		catch (IOException e){
			if (DEBUG) System.err.println
			("Error closing socket to " + strHost);
		}       
	}

	public void setHost(String strHost){
		this.strHost = strHost;
	}

	public void setPort(int iPort){
		this.iPort = iPort;
	}

	public static void main (String arg[]){
		/* debug main; does daytime on local host */
		DefaultSocketClient server = new DefaultSocketClient("", 4444);
		server.start();
	}
}
