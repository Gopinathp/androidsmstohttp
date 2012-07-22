package com.vivaldi.smsreceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * The service responsible for processing the incoming SMS and sending the http request.
 * @author gopinath
 *
 */
public class SmsProcessor extends IntentService {

	private static final String TAG = "smsreceiver";

	public SmsProcessor() {
		super("SMS processor");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "sms processor got invocation");
		Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String body = "";
        if (bundle != null) {
            // ---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                body += msgs[i].getMessageBody().toString();
                sendToRegistrationServer(msgs[i].getOriginatingAddress(), body);
            }
        }

	}

	private void sendToRegistrationServer(String senderMobile, String body) {
		String url = "http://127.0.0.1:8080/register/RegisterServlet?mb=" + URLEncoder.encode(senderMobile) + "&ms=" + URLEncoder.encode(body) + "&debug=true";
		HttpGet get = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(get);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String aLine = "";
			StringBuilder resultBuilder = new StringBuilder(aLine);
			while((aLine = reader.readLine()) != null)
			{
				resultBuilder.append(aLine);
			}
			Log.d(TAG, resultBuilder.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
