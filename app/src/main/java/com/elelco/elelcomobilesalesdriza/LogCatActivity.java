package com.elelco.elelcomobilesalesdriza;

import android.app.*;
import android.os.*;
import android.util.*;
import android.widget.*;

import java.io.*;

import java.lang.Process;

public class LogCatActivity extends Activity
{
	private static final String TAG = LogCatActivity.class.getCanonicalName();
    private static final String processId = Integer.toString(android.os.Process
															 .myPid());
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.error);
		TextView t=(TextView) findViewById(R.id.errorTextView1);

		StringBuilder builder = new StringBuilder();

        try {
            String[] command = new String[] { "logcat", "-d", "-v", "threadtime" };

            Process process = Runtime.getRuntime().exec(command);

            BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(processId)) {
                    builder.append(line);
					builder.append("\n");
                    //Code here
                }
            }
        } catch (IOException ex) {
            Log.e(TAG, "getLog failed", ex);
        }

    //    return builder;
		t.setText(builder.toString());
	}
	
	
	
}
