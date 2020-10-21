package com.elelco.elelcomobilesalesdriza;


import android.content.*;
import android.os.*;
import android.preference.PreferenceManager;
import android.util.*;

import java.io.*;
import java.net.*;

class SendErrorFile extends AsyncTask<String, Void, String>
 {
	 private Context _context;
		private Exception exception;
	 public SendErrorFile(Context context) {
		 _context = context;
	 }

		protected String doInBackground(String... urls) {
			try {
				//Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(MainActivity.getContext()));
				
				String fileName = "stack.trace";
				String outfileName = "stack.trace.txt";
				String upLoadServerUri="http://wsdriza.elelco.it/upload.aspx";
				HttpURLConnection conn = null;
				DataOutputStream dos = null;  
				String lineEnd = "\r\n";
				String twoHyphens = "--";
				String boundary = "*****";
				int bytesRead, bytesAvailable, bufferSize;
				byte[] buffer;
				int maxBufferSize = 1 * 1024 * 1024; 
				File sourceFile = new File(fileName); 
				int serverResponseCode = 0;
				try 
				{ 

					// open a URL connection to the Servlet
					File file = _context.getFileStreamPath("stack.trace");
					FileInputStream fileInputStream = new FileInputStream(file);
					
					/*try{
						User user=new User(context);
						outfileName=user.getDeviceID() + ".txt";
					}catch(Exception ex){
						try{
							SharedPreferences sharedPref = MainActivity.getContext().getSharedPreferences(
								MainActivity.getContext().getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
							outfileName = sharedPref.getString("deviceID", "");
							if (outfileName.equals("")) fileName=android.os.Build.SERIAL + ".txt";
						}catch(Exception ex1){}
					}*/

					SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.activity);
					outfileName = sharedPref.getString("deviceID", "");
					if (outfileName.equals("")) fileName = android.os.Build.SERIAL + ".txt";
					
					URL url = new URL(upLoadServerUri);

					// Open a HTTP  connection to  the URL
					conn = (HttpURLConnection) url.openConnection(); 
					conn.setDoInput(true); // Allow Inputs
					conn.setDoOutput(true); // Allow Outputs
					conn.setUseCaches(false); // Don't use a Cached Copy
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("ENCTYPE", "multipart/form-data");
					conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
					conn.setRequestProperty("uploaded_file", fileName); 

					dos = new DataOutputStream(conn.getOutputStream());
					dos.writeBytes(twoHyphens + boundary + lineEnd); 
					dos.writeBytes("Content-Disposition: form-data; name=\""+outfileName+"\";filename=\""
								   + fileName + "\"" + lineEnd);

					dos.writeBytes(lineEnd);

					// create a buffer of  maximum size
					bytesAvailable = fileInputStream.available(); 

					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];

					// read file and write it into form...
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);  

					while (bytesRead > 0) 
					{

						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);   

					}
					// send multipart form data necesssary after file data...
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

					// Responses from the server (code and message)

					serverResponseCode = conn.getResponseCode();
					String serverResponseMessage = conn.getResponseMessage();

					Log.i("uploadFile", "HTTP Response is : " 
						  + serverResponseMessage + ": " + serverResponseCode);

					if(serverResponseCode == 200)
					{         
					}    
					//close the streams //
					fileInputStream.close();
					dos.flush();
					dos.close();

				} 
				catch (MalformedURLException ex) 
				{
				} 
				catch (Exception e) 
				{

				}
				return String.valueOf( serverResponseCode); 
			} catch (Exception e) {
				this.exception = e;

				return null;
			}
		}

		protected void onPostExecute(String result) {
			// TODO: check this.exception
			// TODO: do something with the feed
			String x=result;
			_context.deleteFile("stack.trace");
			
		}
	}

