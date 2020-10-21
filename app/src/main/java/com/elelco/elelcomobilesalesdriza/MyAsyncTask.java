package com.elelco.elelcomobilesalesdriza;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

class MyAsyncTask extends AsyncTask<String, Integer, TaskResult>
{

	private static final String TAG="MyAsyncTask";
	private static String wsAddress = "http://wsdriza.elelco.it/api/";
	private Context _context;
	public String table;
	public String optionalParms;

	public TaskResult response;
	private final String RIS_ERROR="N";
	private final String RIS_OK="K";
	private TextView txtStatus;
	private Fattura fatt;
	public interface TaskListener
	{
		public void onFinished(TaskResult result);
		public void onProgress(int percentage);
		public TaskResult response=null;
	}
	private TaskListener taskListener=null;


	protected MyAsyncTask(Context context,String url, TaskListener listener, TextView txtProgress)
	{
		txtStatus = txtProgress;
		this.table = url;
		this.optionalParms = "";
		this.taskListener = listener;
		_context = context;
	}
	protected MyAsyncTask(Context context,String url)
	{
		this.table = url;
		this.optionalParms = "";
		_context = context;
	}

	protected MyAsyncTask(Context context,String url, Fattura fatt)
	{
		this.table = url;
		this.optionalParms = "";
		this.fatt=fatt;
		_context = context;
	}
	
    public String defaultParms()
	{
		//User u=MainActivity.getUser();
		User u=new User(_context);
		String res=String.format("?transactId=%s&user=%s&password=%s&codiceDitta=%s&lingua=%s&IDtablet=%s", u.getTransactionID(), u.getUser(), u.getPassword(), u.getLibreria(), u.lingua(), u.deviceID());
		return res;
	}

    @Override
    final protected TaskResult doInBackground(String... urls)
	{ 
		table = urls[0];
		try
		{
            if (checkConnection())
			{
				String address=wsAddress + urls[0] + defaultParms() + this.optionalParms;
				Log.d(TAG, "WS Call " + wsAddress);
				//	if (txtStatus !=null) txtStatus.append("oook");
				String callRis="";
				if (table.equals("fattura") || table.equals("preventivo")){
					callRis=putFattura(wsAddress + urls[0], 180000, table, urls[1]);
				}else if(table.equals("nota")){
					callRis=putFattura(wsAddress + urls[0], 180000, table, urls[1]);
				}else if(table.equals("InstalledApp")){
					callRis=putFattura(wsAddress + urls[0], 180000, table, urls[1]);
				}
				else{
					callRis=getJSON(address, 180000, table);
				}
				if (this.response.error) return this.response;
				this.response = new TaskResult(callRis);
				this.response.table=urls[0];
				//this.taskListener.response = new TaskResult(callRis);  #AS
				//this.taskListener.response.table = urls[0];	 #AS

				if (this.response.error) return this.response;

			} 

        }
		catch (Exception e)
		{
			this.response.error = true;
			this.response.errorMessage =_context.getString(R.string.erroreAggiornamentoDati) +": " + e.getMessage();// "Error updating data: " + e.getMessage();
		}

		return this.response;
    }



    @Override
    final protected void onPreExecute()
	{
		try
		{
			this.response = new TaskResult();
			response.table = this.table;
//			this.response.transactionId=0;
//            this.response.error=false;
//			this.response.errorMessage="";
//			this.response.errorMessagedetailed="";
//			this.response.result="";
        }
		catch (Exception ex)
		{

        }

    }

	@Override
	protected void onPostExecute(TaskResult result)
	{
		// TODO: Implement this method
		result = this.response;
		super.onPostExecute(result);
		if (this.taskListener != null)
		{
			//this.taskListener.onFinished(this.taskListener.response);
			this.taskListener.onFinished(this.response);
		}
	}

	@Override
	protected void onProgressUpdate(Integer...values)
	{
		// TODO: Implement this method
		super.onProgressUpdate(values);
		if (this.taskListener != null)
		{
			this.taskListener.onProgress(1);
		}
	}




	private boolean checkConnection()
	{

        ConnectivityManager connMgr = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null)
		{
			this.response.error = true;
			this.response.errorMessage =_context.getString(R.string.mancaConnessione); //"No connection";
			return false;
		}

        if (networkInfo.isConnected())
		{
            return true;
        }
		else
		{
			this.response.error = true;
			this.response.errorMessage = _context.getString(R.string.mancaConnessione); //"No connection";

            return false;
        }
    }

	private String BufferReaderToString(BufferedReader br)
	{
		//BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
		try
		{
			StringBuffer sb=new StringBuffer();
			String line;
			Integer num=0;
			while ((line = br.readLine()) != null)
			{
				sb.append(line);
				//publishProgress(num);
				num++;
			}
			br.close();
			return sb.toString();
		}
		catch (IOException ex)
		{

		}
		return "";
	}
	
	
	public String putFattura(String url, int timeout, String table, String jsonFatt)
	{
        
        URL u;
        HttpURLConnection c = null;  
        try {
			u = new URL(url);
			//HttpClient httpclient = new DefaultHttpClient();
			//HttpPost httppost = new HttpPost(url);

			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			//List NameValuePair=new ArrayList();
			//NameValuePair.add(new BasicNameValuePair("",jsonFatt));
			//httppost.setEntity(new UrlEncodedFormEntity( NameValuePair));
			Uri.Builder builder = new Uri.Builder()
					.appendQueryParameter("", jsonFatt);
			String query = builder.build().getEncodedQuery();

			//HttpResponse resp = httpclient.execute(httppost);
			//HttpEntity ent = resp.getEntity();

			//Add  parms
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
			writer.write(query);
			writer.flush();
			writer.close();
			os.close();

			int responseCode=conn.getResponseCode();
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				String line;
				String response="";
				BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line=br.readLine()) != null) {
					response+=line;
				}
				return response;
			}
			else {
				this.response.error = true;
				this.response.errorMessage =_context.getString(R.string.erroreRichiamoWs) + " status " + c.getResponseCode(); // "Error loading page (status " + c.getResponseCode() + ") " + url;

				return "";

			}

         //if (ent!=null){
		//	String risp= EntityUtils.toString(ent);
		//	table=url;
		//	return risp;
		// }





			//else
			//{
			//	this.response.error = true;
			//	this.response.errorMessage =mcontext.getString(R.string.erroreRichiamoWs) + " status " + c.getResponseCode(); // "Error loading page (status " + c.getResponseCode() + ") " + url;

            //}

        }
		
		catch (MalformedURLException ex)
		{
			this.response.error = true;
			this.response.errorMessage =_context.getString(R.string.erroreLetturaDati) + ": " + ex.getMessage(); // "Error reading data: " + ex.getMessage();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
		catch (IOException ex)
		{
			this.response.error = true;
			this.response.errorMessage =_context.getString(R.string.erroreLetturaDati) +": " + ex.getMessage();// "Error reading data: " + ex.getMessage();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
		finally
		{
            if (c != null)
			{
                try
				{
                    c.disconnect();
                }
				catch (Exception ex)
				{
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
	
	
	public String getJSON(String url, int timeout, String table)
	{
        HttpURLConnection c = null;
        try
		{
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
			if (c.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				//String response=BufferReaderToString(new BufferedReader(new InputStreamReader(u.openStream())));
				String response=BufferReaderToString(new BufferedReader(new InputStreamReader(c.getInputStream())));
				
				table = url;
				return response;
			}
			else
			{
				this.response.error = true;
				this.response.errorMessage = _context.getString(R.string.erroreRichiamoWs) +" status " + c.getResponseCode();//"Error loading page (status " + c.getResponseCode() + ") " + url;

				if (c.getErrorStream() != null)
				{
					String error=BufferReaderToString(new BufferedReader(new InputStreamReader(c.getErrorStream()), 512));
					this.response.errorMessagedetailed = error;
				}

            }

        }
		catch (MalformedURLException ex)
		{
			this.response.error = true;
			this.response.errorMessage =_context.getString(R.string.erroreLetturaDati) +": " + ex.getMessage();// "Error reading data: " + ex.getMessage();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
		catch (IOException ex)
		{
			this.response.error = true;
			this.response.errorMessage = _context.getString(R.string.erroreLetturaDati) +": " + ex.getMessage();//"Error reading data: " + ex.getMessage();
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
		finally
		{
            if (c != null)
			{
                try
				{
                    c.disconnect();
                }
				catch (Exception ex)
				{
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
}
