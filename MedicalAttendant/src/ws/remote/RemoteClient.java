package ws.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.net.Socket;

public class RemoteClient extends Thread implements RemoteClientInterface {
	private final boolean DEBUG = true;
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private Socket sock;
	private String strHost = "54.86.51.234";
	private int iPort = 4444;
	private boolean running = false;
	private boolean error = false;


	public void run() {
		openConnection();
	}// run

	public void openConnection() {
		try {
			sock = new Socket(strHost, iPort);
		} catch (IOException socketError) {
			if (DEBUG)
				System.err.println("Unable to connect to " + strHost);
			socketError.printStackTrace();
			error = true;
		}
		try {
			writer = new ObjectOutputStream(sock.getOutputStream());
			reader = new ObjectInputStream(sock.getInputStream());
		} catch (Exception e) {
			if (DEBUG)
				System.err
				.println("Unable to obtain stream to/from " + strHost);
			error = true;
		}
		if(!error){
			running = true;
		}
	}

	public boolean ready() {
		while(!error && !running) {
//			System.out.println("Running");
		}
		
		if(running) {
//			System.out.println("Return true");
			running = false;
			error = false;
			return true;
		} else {
			running = false;
			error = false;
//			System.out.println("Return false");
			return false;
		}
	}

	public void handleSession() {
		Object strInput;

		if (DEBUG)
			System.out
			.println("Handling session with " + strHost + ":" + iPort);

		// showMenu();
		Message m = new Message("Client", "Test", null);
		sendOutput(m);

		try {
			while ((strInput = reader.readObject()) != null) {
				handleInput((Message) strInput);
				// showMenu();
			}
		} catch (IOException e) {
			if (DEBUG)
				System.out.println("Handling session with " + strHost + ":"
						+ iPort);
		} catch (ClassNotFoundException e) {
			if (DEBUG)
				System.out.println("Handling session with " + strHost + ":"
						+ iPort);
		}
	}

	public void sendOutput(Message output) {
		try {
			if(writer == null){
				System.out.println("writer null");
			}
			if(output == null){
				System.out.println("output null");
			}
			writer.writeObject(output);
		} catch (IOException e) {
			if (DEBUG)
				System.out.println("Error writing to " + strHost);
			System.out.println(e.toString());
		}
	}

	public void handleInput(Message input) {
//		StringBuilder sb;
//		String userInput;
//		Message m = new Message(null, null, null);

		System.out.println(input.getStrMsg());
		System.out.println(input.getCommand());
	}

	public void closeSession() {
		try {
			writer = null;
			reader = null;
			sock.close();
		} catch (IOException e) {
			if (DEBUG)
				System.err.println("Error closing socket to " + strHost);
		}
	}

	public void setHost(String strHost) {
		this.strHost = strHost;
	}

	public void setPort(int iPort) {
		this.iPort = iPort;
	}

	public Message readInput() {
		try {
			return (Message) reader.readObject();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
