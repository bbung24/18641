package ws.local;

import java.io.Serializable;

import ws.remote.Message;
import ws.remote.RemoteClientConstants;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class BroadcastNotifier {

    private LocalBroadcastManager mBroadcaster;

    /**
     * Creates a BroadcastNotifier containing an instance of LocalBroadcastManager.
     * LocalBroadcastManager is more efficient than BroadcastManager; because it only
     * broadcasts to components within the app, it doesn't have to do parceling and so forth.
     *
     * @param context a Context from which to get the LocalBroadcastManager
     */
    public BroadcastNotifier(Context context) {

        // Gets an instance of the support library local broadcastmanager
        mBroadcaster = LocalBroadcastManager.getInstance(context);

    }

    /**
     *
     * Uses LocalBroadcastManager to send an {@link Intent} containing {@code status}. The
     * {@link Intent} has the action {@code BROADCAST_ACTION} and the category {@code DEFAULT}.
     *
     * @param status {@link Integer} denoting a work request status
     */
    public void broadcastIntentWithMessage(Message msg) {

        Intent localIntent = new Intent();

        // The Intent contains the custom broadcast action for this app
        localIntent.setAction(RemoteClientConstants.BROADCAST_ACTION);

        // Puts the status into the Intent
        localIntent.putExtra(RemoteClientConstants.BROADCAST_RECEV,(Serializable) msg);
        localIntent.addCategory(Intent.CATEGORY_DEFAULT);

        // Broadcasts the Intent
        mBroadcaster.sendBroadcast(localIntent);

    }
}

