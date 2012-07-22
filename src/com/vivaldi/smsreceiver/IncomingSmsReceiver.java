package com.vivaldi.smsreceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * This is where we get a call. Just delegate the event to the IntentService to process this intent.
 * @author gopinath
 *
 */
public class IncomingSmsReceiver extends BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("smsreceiver", "We got a call in our incomingsmsreceiver");
		Intent serviceIntent = new Intent(context, SmsProcessor.class);
		serviceIntent.putExtras(intent);
		context.startService(serviceIntent);
	}

}
