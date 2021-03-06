package com.taskmanager.asynctasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class LoginConnection extends AsyncTask<String,Void,HashMap<String, String>> {

	private ProgressDialog pleaseWait;
	
	public LoginConnection(ProgressDialog pleaseWait) {
		super();
		this.pleaseWait = pleaseWait;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		pleaseWait.setTitle("Please wait");
		pleaseWait.setMessage("Connecting to server");
		pleaseWait.show();
	}
	
	@Override
	protected HashMap<String, String> doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		String login = arg0[0];
		String password = arg0[1];
		
		String url = "http://json-login.heroku.com/login";
		HttpPost request = new HttpPost(url);
		JSONObject holder = new JSONObject();
		JSONObject client = new JSONObject();
		
		try {
			client.put("login", login);
			client.put("password", password);
			holder.put("taskmanager", client);
			StringEntity se = new StringEntity(holder.toString());
			request.setEntity(se);
			request.setHeader("Accept", "taskmanager/json");
			request.setHeader("Content-Type", "taskmanager/json");
			Log.i("user", holder.toString());
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		ResponseHandler<String> rhandler = new BasicResponseHandler();
		HttpClient httpClient = new DefaultHttpClient();
		
		String responseBody = null;
		
		try {
			responseBody = httpClient.execute(request, rhandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<String, String> sessionTokens = parseToken(responseBody);
		return sessionTokens;
		
	}

	private HashMap<String, String> parseToken(String jsonResponse) {
		HashMap<String, String> sessionTokens = new HashMap<String, String>();
		if(jsonResponse != null) {
		JSONObject jObject;
		try {
				jObject = new JSONObject(jsonResponse);
				JSONObject sessionObject = jObject.getJSONObject("session");
				String attributeError = sessionObject.getString("error");
				String attributeToken = sessionObject.getString("auth_token");
				String attributeConsumerKey = sessionObject.getString("consumer_key");
				String attributeConsumerSecret = sessionObject
				.getString("consumer_secret");
				
				sessionTokens.put("error", attributeError);
				sessionTokens.put("auth_token", attributeToken);
				sessionTokens.put("consumer_key", attributeConsumerKey);
				sessionTokens.put("consumer_secret", attributeConsumerSecret);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			
			sessionTokens.put("error", "Error");
		}
		return sessionTokens;	
	}
	
	@Override
	protected void onPostExecute(HashMap<String, String> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pleaseWait.dismiss();
	}
	
}
