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
	// private BufferedReader stdIn;
	private Socket sock;
	private String strHost = "54.86.51.234";
	private int iPort = 4444;

	// public RemoteClient(String strHost, int iPort) {
	// setPort(iPort);
	// setHost(strHost);
	// }

	public void run() {
		if (openConnection()) {
			//handleSession();
		}
	}// run

	public boolean openConnection() {
		try {
			sock = new Socket(strHost, iPort);
		} catch (IOException socketError) {
			if (DEBUG)
				System.err.println("Unable to connect to " + strHost);
			socketError.printStackTrace();
		}
		try {
			writer = new ObjectOutputStream(sock.getOutputStream());
			reader = new ObjectInputStream(sock.getInputStream());
			// stdIn = new BufferedReader(new InputStreamReader(System.in));
		} catch (Exception e) {
			if (DEBUG)
				System.err
						.println("Unable to obtain stream to/from " + strHost);
			return false;
		}
		return true;
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

	// public void showMenu() {
	// CarModelOptionsIO cmo = new CarModelOptionsIO();
	// StringBuilder sb = new StringBuilder();
	// sb = new StringBuilder();
	// sb.append("Available Menus:\n");
	// sb.append("	1. Build Automobile\n");
	// sb.append("	2. Show Automobile Information\n");
	// sb.append("Enter your choice: ");
	// System.out.println(sb.toString());
	// String userInput;
	// try {
	// userInput = stdIn.readLine();
	// } catch (IOException e1) {
	// if (DEBUG) System.err.println
	// ("System.in error");
	// return;
	// }
	// System.out.println("Client: " +userInput);
	// Message m = new Message(null, null, null, null);
	// if (userInput.equalsIgnoreCase("1") ||
	// userInput.equalsIgnoreCase("Build Automobile")) {
	// sb = new StringBuilder();
	// sb.append("Build Automobile\n");
	// sb.append("Enter a filename which you want to build: ");
	// System.out.println(sb.toString());
	// try {
	// userInput = stdIn.readLine();
	// } catch (IOException e1) {
	// if (DEBUG) System.err.println
	// ("System.in error");
	// return;
	// }
	// StringTokenizer st = new StringTokenizer(userInput, ".");
	// Automobile a = cmo.buildAuto(st.nextToken(), st.nextToken());
	// m.setA(a);
	// m.setCommand("BUILDAUTO");
	// sendOutput(m);
	// } else if(userInput.equalsIgnoreCase("2") ||
	// userInput.equalsIgnoreCase("Show Automobile Information")){
	// m.setCommand("displayAutomobile");
	// sendOutput(m);
	// } else {
	// System.out.println("Wrong Input please try again");
	// showMenu();
	// }
	// }

	public void sendOutput(Message output) {
		try {
			writer.writeObject(output);
		} catch (IOException e) {
			if (DEBUG)
				System.out.println("Error writing to " + strHost);
			System.out.println(e.toString());
		}
	}

	public void handleInput(Message input) {
		StringBuilder sb;
		String userInput;
		Message m = new Message(null, null, null);

		System.out.println(input.getStrMsg());
		System.out.println(input.getCommand());
		// if(input.getCommand().equalsIgnoreCase("displayAutomobile")){
		// modelList = input.getList();
		//
		// sb = new StringBuilder();
		// sb.append("Show Automobile Information\n");
		// sb.append("List of available models:\n");
		// sb.append(modelList.toString());
		// sb.append("\nEnter name of model you want to know:");
		// System.out.println(sb.toString());
		// try {
		// userInput = stdIn.readLine();
		// } catch (IOException e1) {
		// if (DEBUG) System.err.println
		// ("System.in error");
		// return;
		// }
		// Message mm = new Message(null,null,null,null);
		// mm.setCommand("getAuto");
		// mm.setStrMsg(userInput);
		// sendOutput(mm);
		// try {
		// Message strInput = (Message) reader.readObject();
		// Automobile a = strInput.getA();
		//
		// if(a == null) {
		// if (DEBUG) System.err.println
		// ("Error receiving Automobile from server");
		// return;
		// }
		// System.out.println("Information about your chosen model:");
		// a.print();
		// }
		// catch (IOException e){
		// if (DEBUG) System.out.println ("Handling session with "
		// + strHost + ":" + iPort);
		// } catch (ClassNotFoundException e) {
		// if (DEBUG) System.out.println ("Handling session with "
		// + strHost + ":" + iPort);
		// }
		// } else if(input.getCommand().equalsIgnoreCase("BUILDAUTO")){
		// if(input.getStrMsg().equalsIgnoreCase("1"))
		// System.out.println("Server: Automobile was built successfully");
		// }
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
