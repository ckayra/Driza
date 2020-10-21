package com.elelco.elelcomobilesalesdriza;

import org.json.*;

public class TaskResult
{
	String transactionId;
	boolean error;
	String errorMessage;
	String errorMessagedetailed;
	String result;
	String table;
	//ArrayList dati;
	JSONObject mainResult;
	JSONArray dati;
	
	public TaskResult(){
		this.transactionId="";
		this.error=false;
		this.errorMessage="";
		this.errorMessagedetailed="";
		this.result="";
		this.table="";
	}

	public TaskResult(String response)
	{
		try
		{
			
			this.result = response;
			JSONObject mainObj=new JSONObject(this.result);
			JSONArray tabs= mainObj.names();
			mainResult=mainObj.getJSONArray(tabs.get(0).toString()).getJSONObject(0);
			
			//mainResult = mainObj.getJSONArray(tabs.get(0).toString()).getJSONObject(0);
			this.error = mainResult.getString("esito").equals("K") ? false : true;
			this.errorMessage = mainResult.getString("descrEsito");
			this.transactionId = mainResult.getString("transactId");
			if (mainObj.has("righeRes"))
			{
				this.dati = mainObj.getJSONArray("righeRes");
			}
			else this.dati = null;

		}
		catch (Exception ex)
		{
			this.result = response;
			this.error = true;
			this.errorMessage = "Error reading task result " + ex.getMessage();
		}

	}
}
