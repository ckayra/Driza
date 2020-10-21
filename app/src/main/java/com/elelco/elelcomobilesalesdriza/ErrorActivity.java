package com.elelco.elelcomobilesalesdriza;

import android.app.*;
import android.os.*;
import android.widget.*;

public class ErrorActivity extends Activity
{
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try{
		String newString;
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if(extras == null) {
				newString= null;
			} else {
				newString= extras.getString("error");
			}
		} else {
			newString= (String) savedInstanceState.getSerializable("error");
		}
		
		setContentView(R.layout.error);
		TextView t=(TextView) findViewById(R.id.errorTextView1);
		t.setText(newString);
		}
		
		catch (Exception ex){
			
		}
		}
}
