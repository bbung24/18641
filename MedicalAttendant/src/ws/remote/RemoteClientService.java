package ws.remote;

import java.util.HashMap;

import ws.local.BroadcastNotifier;
import android.app.IntentService;
import android.content.Intent;

public class RemoteClientService extends IntentService{
	private RemoteClient rc;
	private Message message;

	// Defines and instantiates an object for handling status updates.
	private BroadcastNotifier mBroadcaster = new BroadcastNotifier(this);

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
		rc.start();
		boolean ready = rc.ready();
		Message m;
		if(ready){
			message = (Message) intent.getSerializableExtra("message");
			sendOutput(message);
			m = readInput();
		} else {
			m = new Message("Server", RemoteClientConstants.INTERNAL_FAIL, new HashMap<String, Object>());
		}
		mBroadcaster.broadcastIntentWithMessage(m);
		if(ready){
			rc.closeSession();
		}
	}

	public void sendOutput(Message output){
		rc.sendOutput(output);
	}

	public Message readInput(){
		Message m = rc.readInput();
		return m;
	}
}
