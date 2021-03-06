package com.taskmanager.activities;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.taskmanager.R;
import com.taskmanager.asynctasks.Registration;
import com.taskmanager.helpers.CheckFields;

public class Register extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);
		
		Button registerB = (Button) findViewById(R.id.doRegisterB);
		
		registerB.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				String firstName = ((TextView) findViewById(R.id.firstName)).getText().toString();
				String lastName = ((TextView) findViewById(R.id.lastName)).getText().toString();
				String login = ((TextView) findViewById(R.id.login)).getText().toString();
				String password = ((TextView) findViewById(R.id.password)).getText().toString();
				
				ArrayList<String> errors = CheckFields.checkBeforeRegistraton(firstName,lastName,login,password);
				if(errors == null){
					ProgressDialog pleaseWait = new ProgressDialog(Register.this);
					try {
						String serverError = new Registration(pleaseWait).execute(firstName,lastName,login,password).get();
						if(serverError.equals("Success")){
							//show register succesfully
							Log.i("registration", "Completed successfuly!");
							//redirect to login page
							startActivity(new Intent(Register.this, LogInActivity.class));
						}else{
							//show errors
							Log.i("registration", serverError);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					//error handling
				}
			}
		});
	}
	
}
