package com.elelco.elelcomobilesalesdriza;

import android.content.*;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 * Created by int93 on 08/07/2016.
 */
public class TopExceptionHandler implements Thread.UncaughtExceptionHandler
{

    private Thread.UncaughtExceptionHandler defaultUEH;
	private static final String TAG="TopExceptionHandler";
    private Context app = null;

    public TopExceptionHandler(Context app) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.app = app;

    }
    public void uncaughtException(Thread t, Throwable e)
    {
        StackTraceElement[] arr = e.getStackTrace();
        String report = e.toString()+"\n\n";
        report += "--------- Stack trace ---------\n\n";
        for (int i=0; i<arr.length; i++)
        {
            report += "    "+arr[i].toString()+"\n";
        }
        report += "-------------------------------\n\n";
// If the exception was thrown in a background thread inside
// AsyncTask, then the actual exception can be found with getCause
        report += "--------- Cause ---------\n\n";
        Throwable cause = e.getCause();
        if(cause != null) {
            report += cause.toString() + "\n\n";
            arr = cause.getStackTrace();
            for (int i=0; i<arr.length; i++)
            {
                report += "    "+arr[i].toString()+"\n";
            }
        }
        report += "-------------------------------\n\n";
		
		try{
			String fullClassName = e.getStackTrace()[2].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
			String methodName = e.getStackTrace()[2].getMethodName();
			int lineNumber = e.getStackTrace()[2].getLineNumber();

			report+=className + "." + methodName + "():" + lineNumber+"\n\n";
			
			
		} catch(Exception ex){}
		
		
		try{
			Calendar c=Calendar.getInstance();
			SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy.hh.mm.ss");
			report=df.format(c.getTime()) + "\n\n" + report;
		}catch(Exception ex){}
		
        try {
			//document directory
//			FileOutputStream trace = new FileOutputStream(
//			new File(Environment.getExternalStorageDirectory() + "/Documents","stack.trace"), true);
			//internal private storage
       //   FileOutputStream trace = app.openFileOutput( "stack.trace", Context.MODE_PRIVATE);
			FileOutputStream trace = app.openFileOutput( "stack.trace", Context.MODE_APPEND);
	
            trace.write(report.getBytes());
            trace.close();
			
			
			
			
        } catch(IOException ioe) {
 
        }
catch (Exception ex){
	
}
		
		//android.os.Process.killProcess(android.os.Process.myPid());
      //  System.exit(10);
		
//	if (defaultUEH != null)
//			defaultUEH.uncaughtException(t,e);
//		else System.exit(10);

        Intent intent = new Intent(app, ErrorActivity.class);
        intent.putExtra("error", report.toString());
        app.startActivity(intent);
		System.exit(10);
//		
		
        //defaultUEH.uncaughtException(t, e);
    }
}
