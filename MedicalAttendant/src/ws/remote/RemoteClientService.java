package ws.remote;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class RemoteClientService extends IntentService{
	private String command;
	private RemoteClient rc;

	/**
	 * A constructor is required, and must call the super IntentService(String)
	 * constructor with a name for the worker thread.
	 */
	public RemoteClientService() {
		super("RemoteClientService");
	}

	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns, IntentService
	 * stops the service, as appropriate.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		rc = new RemoteClient();
		command = intent.getStringExtra("command");
	}

	public void sendOutput(Message output){
		rc.sendOutput(output);
	}

	public void handleInput(Message input){
		rc.handleInput(input);
	}

	public Message readInput(){
		Message m = rc.readInput();
		return m;
	}
}
